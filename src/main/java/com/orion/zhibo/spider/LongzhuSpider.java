/**
 * Copyright 2015 yezi.gl. All Rights Reserved.
 */
package com.orion.zhibo.spider;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.orion.core.utils.HttpUtils;
import com.orion.zhibo.entity.LiveRoom;
import com.orion.zhibo.entity.PlatformGame;
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
    public Optional<LiveRoom> parse(String liveUrl) {
        Document document = Jsoup.parse(HttpUtils.get(liveUrl, header, "UTF-8"));
        LiveRoom liveRoom = liveRoomService.getByUrl(liveUrl);

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
            logger.error("parser {} fail", liveUrl);
            if (liveRoom != null) {
                liveRoom.setStatus(LiveStatus.CLOSE);
                return Optional.of(liveRoom);
            }
            return Optional.empty();
        }
        // 一般来说不变的信息
        if (liveRoom == null) {
            liveRoom = new LiveRoom();
            liveRoom.setLiveUrl(liveUrl);
        }
        liveRoom.setUid(roomObject.getString("UserId"));
        liveRoom.setRoomId(roomObject.getString("RoomId"));
        liveRoom.setLiveId(roomObject.getString("BoardCast_Address"));
        liveRoom.setFlashUrl(String.format(platform.getSharePattern(), liveRoom.getLiveId()));
        liveRoom.setName(roomObject.getString("Name"));
        liveRoom.setTitle(StringUtils.defaultIfBlank(roomObject.getString("BoardCast_TitleV2"),
                roomObject.getString("BoardCast_Title")));
        liveRoom.setDescription(roomObject.getString("Desc"));
        liveRoom.setThumbnail("http://img.plures.net/live/screenshots/" + liveRoom.getRoomId() + "/"
                + roomObject.getString("CategoryId") + ".jpg");
        liveRoom.setAvatar(roomObject.getString("Logo"));

        // 直播情况
        liveRoom.setStatus((pageData != null && pageData.containsKey("live")) ? LiveStatus.LIVING : LiveStatus.CLOSE);
        if (liveRoom.getStatus() == LiveStatus.LIVING) {
            liveRoom.setTitle(pageData.getJSONObject("live").getString("title"));
            JSONObject data = JSON.parseObject(
                    HttpUtils.get("http://mb.tga.plu.cn/chatroom/joinroom?roomId=" + liveRoom.getRoomId()));
            liveRoom.setNumber(data.getIntValue("online"));
        } else {
            liveRoom.setNumber(0);
        }
        liveRoom.setViews(Utils.convertView(liveRoom.getNumber()));

        return Optional.of(liveRoom);
    }

    @Override
    public void run() {
        List<PlatformGame> pgs = platformGameService.listByPlatform(platform);
        for (PlatformGame pg : pgs) {
            Document document = Jsoup.parse(HttpUtils.get(pg.getPlatformUrl(), header, "UTF-8"));
            Elements elements = document.select("#list-con a");
            if (elements.size() == 0) {
                logger.error("parse html error {}", pg.getPlatformUrl());
            }
            for (Element element : elements) {
                try {
                    if (Utils.parseViews(element.select(".livecard-meta .livecard-meta-item-text").first().text()) < 1000) {
                        continue;
                    }
                    String url = element.attr("href");
                    Optional<LiveRoom> liveRoom = parse(url);
                    liveRoom.ifPresent(e -> {
                        e.setPlatform(pg.getPlatform());
                        e.setGame(pg.getGame());
                        upsertLiveRoom(e);
                    });
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }
}
