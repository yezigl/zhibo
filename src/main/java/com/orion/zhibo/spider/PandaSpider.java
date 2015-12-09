/**
 * Copyright 2015 yezi.gl. All Rights Reserved.
 */
package com.orion.zhibo.spider;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    
    final Pattern ROOMID_PATTERN  = Pattern.compile("(\\d+)");
    
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
                            String roomId = uri.replace("/", "");
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
    public LiveRoom parse(Actor actor) {
        LiveRoom liveRoom = liveRoomService.getByActor(actor);
        
        Matcher matcher = ROOMID_PATTERN.matcher(actor.getLiveUrl());
        String roomId = null;
        while (matcher.find()) {
            roomId = matcher.group(1);
        }
        if (roomId == null) {
            logger.warn("parser {} fail", actor.getLiveUrl());
            return null;
        }
        String ret = HttpUtils.get("http://www.panda.tv/api_room?roomid=" + roomId, header, "UTF-8");
        JSONObject roomObject = JSON.parseObject(ret);
        if (roomObject == null) {
            logger.warn("parser {} fail {}", actor.getLiveUrl(), ret);
            return null;
        }
        // 一般来说不变的信息
        if (liveRoom == null) {
            liveRoom = new LiveRoom();
            liveRoom.setActor(actor);
        }
        roomObject = roomObject.getJSONObject("data");
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
        liveRoom.setNumber(liveRoom.isLiving() ? roomInfo.getIntValue("person_num") : 0);
        liveRoom.setViews(Utils.convertView(liveRoom.getNumber()));
        
        return liveRoom;
    }

}
