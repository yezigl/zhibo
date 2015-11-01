/**
 * Copyright 2015 meituan.com. All Rights Reserved.
 */
package com.orion.zhibo.spider;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

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
 * TODO 需解决imba等直播室的问题
 *
 * @author yezi
 * @since 2015年9月3日
 */
@Component
public class DouyuSpider extends AbstractSpider {

    private static Map<String, Integer> viewsMap = new ConcurrentHashMap<>();

    /**
     * 
     */
    public DouyuSpider() {
        exe.scheduleAtFixedRate(new Runnable() {
            public void run() {
                try {
                    List<PlatformGame> pgs = platformGameService.listByPlatform(platform);
                    for (PlatformGame pg : pgs) {
                        logger.info("fetch view number {}", pg.getPlatformUrl());
                        Document document = Jsoup.parse(HttpUtils.get(pg.getPlatformUrl(), header, "UTF-8"));
                        Elements elements = document.select("#item_data ul li a.list");
                        for (Element element : elements) {
                            String uri = element.attr("href");
                            String url = platform.getUrl() + uri.replace("/", "");
                            Element views = element.select("p.moreMes .view").first();
                            int number = Utils.parseViews(views.text());
                            viewsMap.put(url, number);
                        }
                    }
                } catch (Exception e) {
                    logger.error("parse error", e);
                }
            }
        }, 0, 1, TimeUnit.MINUTES);
    }

    @Override
    protected String customPlatform() {
        return "douyu";
    }

    @Override
    public void parse(Actor actor) {
        Document document = Jsoup.parse(HttpUtils.get(actor.getLiveUrl(), header, "UTF-8"));
        LiveRoom liveRoom = liveRoomService.getByActor(actor);
        // 解析房间信息
        JSONObject roomObject = null;
        Elements scripts = document.select("script");
        for (int i = 0; i < scripts.size(); i++) {
            if (scripts.get(i).data().contains("var $ROOM =")) {
                String room = scripts.get(i).data().replace("var $ROOM =", "").replace(";", "");
                try {
                    roomObject = JSON.parseObject(room);
                } catch (Exception e) {
                    logger.error("parse {} json error {}", actor.getLiveUrl(), room, e);
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
        liveRoom.setUid(roomObject.getString("owner_uid"));
        liveRoom.setRoomId(roomObject.getString("room_id"));
        liveRoom.setLiveId(liveRoom.getRoomId());
        liveRoom.setFlashUrl(String.format(platform.getSharePattern(), liveRoom.getRoomId()));
        liveRoom.setShareUrl(roomObject.getString("room_url"));
        // 头像
        Element avatar = document.select(".room_mes .h_tx img").first();
        liveRoom.setAvatar(avatar.attr("src"));
        liveRoom.setName(roomObject.getString("owner_name"));
        liveRoom.setTitle(roomObject.getString("room_name"));
        liveRoom.setDescription(roomObject.getJSONObject("room_gg").getString("show"));
        liveRoom.setThumbnail(roomObject.getString("room_pic"));

        // 直播情况
        liveRoom.setStatus(roomObject.getIntValue("show_status") == 1 ? LiveStatus.LIVING : LiveStatus.CLOSE);
        if (liveRoom.getStatus() == LiveStatus.LIVING) {
            // String ret = HttpUtils.get("http://www.douyutv.com/api/v1/room/"
            // + liveRoom.getRoomId());
            // JSONObject jsonObject = JSON.parseObject(ret);
            // if (jsonObject.containsKey("data") && (jsonObject.get("data")
            // instanceof JSONObject)) {
            // liveRoom.setNumber(jsonObject.getJSONObject("data").getIntValue("online"));
            // }
            Integer views = viewsMap.get(liveRoom.getShareUrl());
            liveRoom.setNumber(views != null ? views.intValue() : 0);
        } else {
            liveRoom.setNumber(0);
        }
        liveRoom.setViews(Utils.convertView(liveRoom.getNumber()));

        upsertLiveRoom(liveRoom);
    }

}
