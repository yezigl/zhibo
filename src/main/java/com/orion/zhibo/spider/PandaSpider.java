/**
 * Copyright 2015 yezi.gl. All Rights Reserved.
 */
package com.orion.zhibo.spider;

import java.util.List;
import java.util.Optional;
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

    static final String PANDA_ROOM = "panda-room-";

    static final Pattern ROOMID_PATTERN = Pattern.compile("(\\d+)");

    final String ulSelector = "#sortdetail-container li a";

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        super.schedule(() -> {
            try {
                List<PlatformGame> pgs = platformGameRepository.findByPlatform(platform);
                for (PlatformGame pg : pgs) {
                    logger.info("fetch thumbnail {}", pg.getPlatformUrl());
                    Document document = Jsoup.parse(HttpUtils.get(pg.getPlatformUrl(), header, "UTF-8"));
                    Elements elements = document.select(ulSelector);
                    for (Element element : elements) {
                        String roomId = element.attr("data-id");
                        Element thumbnail = element.select(".video-cover img").first();
                        cacheService.set(PANDA_ROOM + roomId, thumbnail.attr("data-original"));
                    }
                }
            } catch (Exception e) {
                logger.error("parse error", e);
            }
        });
    }

    @Override
    protected String customPlatform() {
        return "panda";
    }

    @Override
    public Optional<LiveRoom> parse(String liveUrl) {
        LiveRoom liveRoom = liveRoomRepository.findByLiveUrl(liveUrl);

        Matcher matcher = ROOMID_PATTERN.matcher(liveUrl);
        String roomId = null;
        while (matcher.find()) {
            roomId = matcher.group(1);
        }
        if (roomId == null) {
            logger.error("parser {} fail", liveUrl);
            return null;
        }
        String ret = HttpUtils.get("http://www.panda.tv/api_room?roomid=" + roomId, header, "UTF-8");
        JSONObject roomObject = JSON.parseObject(ret);
        if (roomObject == null) {
            logger.error("parser {} fail {}", liveUrl, ret);
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
        roomObject = roomObject.getJSONObject("data");
        JSONObject hostInfo = roomObject.getJSONObject("hostinfo");
        JSONObject roomInfo = roomObject.getJSONObject("roominfo");
        JSONObject videoInfo = roomObject.getJSONObject("videoinfo");
        JSONObject userInfo = roomObject.getJSONObject("userinfo");
        liveRoom.setUid(userInfo.getString("rid"));
        liveRoom.setRoomId(roomInfo.getString("id"));
        liveRoom.setLiveId(videoInfo.getString("room_key"));
        liveRoom.setFlashUrl(String.format(platform.getSharePattern(), liveRoom.getLiveId(), liveRoom.getUid(),
                liveRoom.getRoomId()));
        liveRoom.setName(hostInfo.getString("name"));
        liveRoom.setTitle(roomInfo.getString("name"));
        liveRoom.setDescription(roomInfo.getString("bulletin"));
        liveRoom.setThumbnail(cacheService.get(PANDA_ROOM + liveRoom.getRoomId()));
        liveRoom.setAvatar(hostInfo.getString("avatar"));

        // 直播情况
        liveRoom.setStatus(videoInfo.getIntValue("status") == 2 ? LiveStatus.LIVING : LiveStatus.CLOSE);
        liveRoom.setNumber(liveRoom.getStatus() == LiveStatus.LIVING ? roomInfo.getIntValue("person_num") : 0);
        liveRoom.setViews(Utils.convertView(liveRoom.getNumber()));

        return Optional.of(liveRoom);
    }

    @Override
    public void runFetch() {
        List<PlatformGame> pgs = platformGameRepository.findByPlatform(platform);
        for (PlatformGame pg : pgs) {
            Document document = Jsoup.parse(HttpUtils.get(pg.getPlatformUrl(), header, "UTF-8"));
            Elements elements = document.select(ulSelector);
            if (elements.size() == 0) {
                logger.error("parse html error {}", pg.getPlatformUrl());
            }
            for (Element element : elements) {
                try {
                    if (Utils.parseViews(element.select(".video-info .video-number").first().text()) < 1000) {
                        continue;
                    }
                    String uri = element.attr("href");
                    String url = platform.getUrl() + uri.substring(1, uri.length());
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
