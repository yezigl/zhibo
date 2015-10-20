/**
 * Copyright 2015 yezi.gl. All Rights Reserved.
 */
package com.orion.zhibo.spider;

import org.apache.commons.lang3.StringUtils;
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


/**
 * description here
 *
 * @author yezi
 * @since 2015年9月8日
 */
@Component
public class LongzhuSpider extends AbstractSpider {
    
    @Override
    protected String customPlatform() {
        return "longzhu";
    }

    @Override
    public void parse(Actor actor) {
        Document document = Jsoup.parse(HttpUtils.get(actor.getLiveUrl(), header, "UTF-8"));
        LiveRoom liveRoom = liveRoomService.getByActor(actor);
        
        JSONObject roomObject = null;
        Elements scripts = document.select("script");
        for (int i = 0; i < scripts.size(); i++) {
            String script = scripts.get(i).data();
            if (script.contains("var roomInfo =")) {
                int s = script.indexOf("var roomInfo =");
                String room = script.substring(s + 14);
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
            liveRoom.setUid(roomObject.getString("UserId"));
            liveRoom.setRoomId(roomObject.getString("BoardCast_Address"));
            liveRoom.setFlashUrl(String.format(platform.getSharePattern(), liveRoom.getRoomId()));
        }
        
        liveRoom.setName(roomObject.getString("Name"));
        liveRoom.setTitle(StringUtils.defaultIfBlank(roomObject.getString("BoardCast_TitleV2"), roomObject.getString("BoardCast_Title")));
        liveRoom.setDescription(roomObject.getString("Desc"));
        liveRoom.setThumbnail("");
        liveRoom.setAvatar(roomObject.getString("Logo"));
        liveRoom.setNumber(0);
        liveRoom.setStatus(roomObject.getIntValue("Status") == 1 ? LiveStatus.LIVING : LiveStatus.CLOSE);
        
        upsertLiveRoom(liveRoom);
    }
}
