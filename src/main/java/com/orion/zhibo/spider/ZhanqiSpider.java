/**
 * Copyright 2015 yezi.gl. All Rights Reserved.
 */
package com.orion.zhibo.spider;

import java.util.List;
import java.util.Optional;

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
 * @since 2015年9月6日
 */
@Component
public class ZhanqiSpider extends AbstractSpider {

    @Override
    protected String customPlatform() {
        return "zhanqi";
    }

    @Override
    public Optional<LiveRoom> parse(String liveUrl) {
        Document document = Jsoup.parse(HttpUtils.get(liveUrl, header, "UTF-8"));
        LiveRoom liveRoom = liveRoomService.getByUrl(liveUrl);

        JSONObject roomObject = null;
        Elements scripts = document.select("script");
        for (int i = 0; i < scripts.size(); i++) {
            String script = scripts.get(i).data();
            if (script.contains("window.oPageConfig.oRoom =")) {
                String room = parseScript(script, "window.oPageConfig.oRoom =");
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
        liveRoom.setUid(roomObject.getString("uid"));
        liveRoom.setRoomId(roomObject.getString("id"));
        liveRoom.setLiveId(liveRoom.getRoomId());
        liveRoom.setFlashUrl(String.format(platform.getSharePattern(), liveRoom.getRoomId()));
        liveRoom.setName(roomObject.getString("nickname"));
        liveRoom.setTitle(roomObject.getString("title"));
        liveRoom.setDescription(roomObject.getString("roomDesc"));
        liveRoom.setThumbnail(roomObject.getString("bpic"));
        liveRoom.setAvatar(roomObject.getString("avatar") + "-medium");

        // 直播情况
        liveRoom.setStatus(roomObject.getIntValue("status") == 4 ? LiveStatus.LIVING : LiveStatus.CLOSE);
        liveRoom.setNumber(liveRoom.getStatus() == LiveStatus.LIVING ? roomObject.getIntValue("online") : 0);
        liveRoom.setViews(Utils.convertView(liveRoom.getNumber()));

        return Optional.of(liveRoom);
    }

    @Override
    public void runFetch() {
        List<PlatformGame> pgs = platformGameService.listByPlatform(platform);
        for (PlatformGame pg : pgs) {
            Document document = Jsoup.parse(HttpUtils.get(pg.getPlatformUrl(), header, "UTF-8"));
            Elements elements = document.select("ul.js-room-list-ul a");
            if (elements.size() == 0) {
                logger.error("parse html error {}", pg.getPlatformUrl());
            }
            for (Element element : elements) {
                try {
                    if (Utils.parseViews(element.select(".info-area .views span.dv").first().text()) < 1000) {
                        continue;
                    }
                    String uri = element.attr("href");
                    String url = platform.getUrl() + uri.replace("/", "");
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
