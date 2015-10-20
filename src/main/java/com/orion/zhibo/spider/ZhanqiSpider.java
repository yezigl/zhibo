/**
 * Copyright 2015 yezi.gl. All Rights Reserved.
 */
package com.orion.zhibo.spider;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.orion.core.utils.HttpUtils;
import com.orion.zhibo.entity.Actor;
import com.orion.zhibo.entity.LiveRoom;
import com.orion.zhibo.model.LiveStatus;
import com.orion.zhibo.utils.Utils;

/**
 * description here
 *
 * @author yezi
 * @since 2015年9月6日
 */
@Component
public class ZhanqiSpider extends AbstractSpider {

    @Override
    protected String customPlatform() {
        return "zhanqi";
    }

    @Override
    public void parse(Actor actor) {
        Document document = Jsoup.parse(HttpUtils.get(actor.getLiveUrl(), header, "UTF-8"));
        LiveRoom liveRoom = liveRoomService.getByActor(actor);
        
        JSONObject roomObject = null;
        Elements scripts = document.select("script");
        for (int i = 0; i < scripts.size(); i++) {
            String script = scripts.get(i).data();
            if (script.contains("window.oPageConfig.oRoom =")) {
                int s = script.indexOf("window.oPageConfig.oRoom =");
                String room = script.substring(s + 26);
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
        liveRoom.setUid(roomObject.getString("uid"));
        liveRoom.setRoomId(roomObject.getString("id"));
        liveRoom.setFlashUrl(String.format(platform.getSharePattern(), liveRoom.getRoomId()));
        liveRoom.setName(roomObject.getString("nickname"));
        liveRoom.setTitle(roomObject.getString("title"));
        liveRoom.setDescription(roomObject.getString("roomDesc"));
        liveRoom.setThumbnail(roomObject.getString("bpic"));
        liveRoom.setAvatar(roomObject.getString("avatar") + "-medium");
        
        // 直播情况
        liveRoom.setStatus(roomObject.getIntValue("status") == 4 ? LiveStatus.LIVING : LiveStatus.CLOSE);
        liveRoom.setNumber(roomObject.getIntValue("online"));
        liveRoom.setViews(Utils.convertView(liveRoom.getNumber()));
        
        upsertLiveRoom(liveRoom);
    }

}
