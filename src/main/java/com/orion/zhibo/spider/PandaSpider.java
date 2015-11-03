/**
 * Copyright 2015 yezi.gl. All Rights Reserved.
 */
package com.orion.zhibo.spider;

import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.orion.core.utils.HttpUtils;
import com.orion.zhibo.entity.Actor;
import com.orion.zhibo.entity.LiveRoom;
import com.orion.zhibo.entity.PlatformGame;
import com.orion.zhibo.model.LiveStatus;
import com.orion.zhibo.utils.Utils;

/**
 * description here
 *
 * @author yezi
 * @since 2015年11月2日
 */
@Component
public class PandaSpider extends AbstractSpider {
    
    final String PANDA_ROOM = "panda-room-";
    
    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        super.schedule(new Runnable() {
            public void run() {
                try {
                    List<PlatformGame> pgs = platformGameService.listByPlatform(platform);
                    for (PlatformGame pg : pgs) {
                        logger.info("fetch thumbnail {}", pg.getPlatformUrl());
                        Document document = Jsoup.parse(HttpUtils.get(pg.getPlatformUrl(), header, "UTF-8"));
                        Elements elements = document.select("#sortdetail-container li a");
                        for (Element element : elements) {
                            String uri = element.attr("href");
                            String roomId = uri.replace("/room/", "");
                            Element thumbnail = element.select(".video-cover img").first();
                            cacheService.set(PANDA_ROOM + roomId, thumbnail.attr("src"));
                        }
                    }
                } catch (Exception e) {
                    logger.error("parse error", e);
                }
            }
        });
    }

    @Override
    protected String customPlatform() {
        return "panda";
    }
    
    @Override
    public void parse(Actor actor) {
        Document document = Jsoup.parse(HttpUtils.get(actor.getLiveUrl(), header, "UTF-8"));
        LiveRoom liveRoom = liveRoomService.getByActor(actor);
        
        JSONObject roomObject = null;
        Elements scripts = document.select("script");
        for (int i = 0; i < scripts.size(); i++) {
            String script = scripts.get(i).data();
            if (script.contains("var ROOM =")) {
                int s = script.indexOf("var ROOM =");
                String room = script.substring(s + 10);
                s = room.indexOf("};");
                room = room.substring(0, s + 1);
                try {
                    roomObject = JSON.parseObject(room);
                } catch (Exception e) {
                    logger.error("parse json error", room, e);
                    break;
                }
                break;
            }
        }
        if (roomObject == null) {
            logger.warn("parser {} fail", actor.getLiveUrl());
            return;
        }
        // 一般来说不变的信息
        if (liveRoom == null) {
            liveRoom = new LiveRoom();
            liveRoom.setActor(actor);
        }
        roomObject = roomObject.getJSONObject("room");
        JSONObject hostInfo = roomObject.getJSONObject("hostinfo");
        JSONObject roomInfo = roomObject.getJSONObject("roominfo");
        JSONObject videoInfo = roomObject.getJSONObject("videoinfo");
        liveRoom.setUid(hostInfo.getString("rid"));
        liveRoom.setRoomId(roomInfo.getString("id"));
        liveRoom.setLiveId(videoInfo.getString("room_key"));
        liveRoom.setFlashUrl(String.format(platform.getSharePattern(), liveRoom.getLiveId(), liveRoom.getRoomId()));
        liveRoom.setName(hostInfo.getString("name"));
        liveRoom.setTitle(roomInfo.getString("name"));
        liveRoom.setDescription(roomInfo.getString("bulletin"));
        liveRoom.setThumbnail(cacheService.get(PANDA_ROOM + liveRoom.getRoomId()));
        liveRoom.setAvatar(hostInfo.getString("avatar"));
        
        // 直播情况
        liveRoom.setStatus(videoInfo.getIntValue("status") == 2 ? LiveStatus.LIVING : LiveStatus.CLOSE);
        liveRoom.setNumber(roomInfo.getIntValue("person_num"));
        liveRoom.setViews(Utils.convertView(liveRoom.getNumber()));
        
        upsertLiveRoom(liveRoom);
        
    }

}
