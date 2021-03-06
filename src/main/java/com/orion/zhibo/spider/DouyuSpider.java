/**
 * Copyright 2015 meituan.com. All Rights Reserved.
 */
package com.orion.zhibo.spider;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

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
 * TODO 需解决imba等直播室的问题
 *
 * @author yezi
 * @since 2015年9月3日
 */
@Component
public class DouyuSpider extends AbstractSpider {

    static final String ROOM = "douyu-room-";
    final String ulSelector = "#live-list-contentbox li";
    final String viewsSelector = ".mes .dy-num";

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        super.schedule(() -> {
            try {
                int n = 0;
                List<PlatformGame> pgs = platformGameRepository.findByPlatform(platform);
                for (PlatformGame pg : pgs) {
                    logger.info("fetch view number {}", pg.getPlatformUrl());
                    Document document = Jsoup.parse(HttpUtils.get(pg.getPlatformUrl(), header, "UTF-8"));
                    Elements elements = document.select(ulSelector);
                    for (Element element : elements) {
                        String roomId = element.attr("data-rid");
                        Element views = element.select(viewsSelector).first();
                        cacheService.set(ROOM + roomId, views.text());
                        if (n++ > 20) {
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("parse error", e);
            }
        });
    }

    @Override
    protected String customPlatform() {
        return "douyu";
    }

    @Override
    public Optional<LiveRoom> parse(String liveUrl) {
        Document document = Jsoup.parse(HttpUtils.get(liveUrl, header, "UTF-8"));
        LiveRoom liveRoom = liveRoomRepository.findByLiveUrl(liveUrl);
        // 解析房间信息
        JSONObject roomObject = null;
        Elements scripts = document.select("script");
        for (int i = 0; i < scripts.size(); i++) {
            if (scripts.get(i).data().contains("var $ROOM =")) {
                String room = parseScript(scripts.get(i).data(), "var $ROOM =");
                try {
                    roomObject = JSON.parseObject(room);
                } catch (Exception e) {
                    logger.error("parse {} json error {}", liveUrl, room, e);
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
        liveRoom.setUid(roomObject.getString("owner_uid"));
        liveRoom.setRoomId(roomObject.getString("room_id"));
        liveRoom.setLiveId(liveRoom.getRoomId());
        liveRoom.setFlashUrl(String.format(platform.getSharePattern(), liveRoom.getRoomId()));
        liveRoom.setShareUrl(roomObject.getString("room_url"));
        // 头像
        Element avatar = document.select("#anchor-info .anchor-pic img").first();
        liveRoom.setAvatar(avatar.attr("src"));
        liveRoom.setName(roomObject.getString("owner_name"));
        liveRoom.setTitle(roomObject.getString("room_name"));
        liveRoom.setDescription(roomObject.getJSONObject("room_gg").getString("show"));
        liveRoom.setThumbnail(roomObject.getString("room_pic"));

        // 直播情况
        liveRoom.setStatus(roomObject.getIntValue("show_status") == 1 ? LiveStatus.LIVING : LiveStatus.CLOSE);
        if (liveRoom.getStatus() == LiveStatus.LIVING) {
            int views = Utils.parseViews(cacheService.get(ROOM + liveRoom.getRoomId()));
            liveRoom.setNumber(views);
        } else {
            liveRoom.setNumber(0);
        }
        liveRoom.setViews(Utils.convertView(liveRoom.getNumber()));

        return Optional.of(liveRoom);
    }

    @Override
    public void runFetch() {
        List<PlatformGame> pgs = platformGameRepository.findByPlatform(platform);
        for (PlatformGame pg : pgs) {
            Document document = Jsoup.parse(HttpUtils.get(pg.getPlatformUrl(), header, "UTF-8"));
            Elements elements = document.select(ulSelector + " a");
            if (elements.size() == 0) {
                logger.error("parse html error {}", pg.getPlatformUrl());
            }
            for (Element element : elements) {
                try {
                    if (Utils.parseViews(element.select(viewsSelector).first().text()) < 1000) {
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
                    TimeUnit.SECONDS.sleep(1);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

}
