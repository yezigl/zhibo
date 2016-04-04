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
import com.orion.zhibo.utils.Utils;


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
    public LiveRoom parse(Actor actor) {
        Document document = Jsoup.parse(HttpUtils.get(actor.getLiveUrl(), header, "UTF-8"));
        LiveRoom liveRoom = liveRoomService.getByActor(actor);
        
        JSONObject roomObject = null, pageData = null;
        Elements scripts = document.select("script");
        for (int i = 0; i < scripts.size(); i++) {
            String script = scripts.get(i).data();
            if (script.contains("var roomInfo =")) {
                String room = parseScript(script, "var roomInfo =");
                try {
                    roomObject = JSON.parseObject(room);
                } catch (Exception e) {
                    logger.error("parse json error", room, e);
                }
            }
            if (script.contains("var pageData =")) {
                String json = parseScript(script, "var pageData =");
                try {
                    pageData = JSON.parseObject(json);
                } catch (Exception e) {
                    logger.error("parse json error", json, e);
                }
            }
        }
        if (roomObject == null) {
            logger.warn("parser {} fail", actor.getLiveUrl());
            if (liveRoom != null) {
                liveRoom.setStatus(LiveStatus.CLOSE);
                return liveRoom;
            }
            return null;
        }
        // 一般来说不变的信息
        if (liveRoom == null) {
            liveRoom = new LiveRoom();
            liveRoom.setActor(actor);
        }
        liveRoom.setUid(roomObject.getString("UserId"));
        liveRoom.setRoomId(roomObject.getString("RoomId"));
        liveRoom.setLiveId(roomObject.getString("BoardCast_Address"));
        liveRoom.setFlashUrl(String.format(platform.getSharePattern(), liveRoom.getLiveId()));
        liveRoom.setName(roomObject.getString("Name"));
        liveRoom.setTitle(StringUtils.defaultIfBlank(roomObject.getString("BoardCast_TitleV2"), roomObject.getString("BoardCast_Title")));
        liveRoom.setDescription(roomObject.getString("Desc"));
        liveRoom.setThumbnail("http://img.plures.net/live/screenshots/" + liveRoom.getRoomId() + "/" + roomObject.getString("CategoryId") + ".jpg");
        liveRoom.setAvatar(roomObject.getString("Logo"));
        
        // 直播情况
        liveRoom.setStatus((pageData != null && pageData.containsKey("live")) ? LiveStatus.LIVING : LiveStatus.CLOSE);
        if (liveRoom.getStatus() == LiveStatus.LIVING) {
            liveRoom.setTitle(pageData.getJSONObject("live").getString("title"));
            JSONObject data = JSON.parseObject(HttpUtils.get("http://mb.tga.plu.cn/chatroom/joinroom?roomId=" + liveRoom.getRoomId()));
            liveRoom.setNumber(data.getIntValue("online"));
        } else {
            liveRoom.setNumber(0);
        }
        liveRoom.setViews(Utils.convertView(liveRoom.getNumber()));
        
        return liveRoom;
    }
}
