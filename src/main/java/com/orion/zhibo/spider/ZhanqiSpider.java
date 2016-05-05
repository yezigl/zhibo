/**
 * Copyright 2015 yezi.gl. All Rights Reserved.
 */
package com.orion.zhibo.spider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
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
    public LiveRoom parse(String liveUrl) {
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
            logger.warn("parser {} fail", liveUrl);
            if (liveRoom != null) {
                liveRoom.setStatus(LiveStatus.CLOSE);
                return liveRoom;
            }
            return null;
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

        return liveRoom;
    }

    @SuppressWarnings("unused")
    private String toFlashVars(JSONObject object) {
        List<String> list = new ArrayList<>();
        for (Entry<String, Object> entry : object.entrySet()) {
            if (entry.getValue() instanceof JSONObject) {
                return toFlashVars(object);
            } else if (entry.getValue() instanceof JSONArray) {
                continue;
            } else {
                list.add(entry.getKey() + "=" + entry.getValue());
            }
        }
        return StringUtils.join(list, "&");
    }

    @Override
    public void run() {
        List<PlatformGame> pgs = platformGameService.listByPlatform(platform);
        for (PlatformGame pg : pgs) {
            Document document = Jsoup.parse(HttpUtils.get(pg.getPlatformUrl(), header, "UTF-8"));
            Elements elements = document.select("#hotList li a.js-jump-link");
            for (Element element : elements) {
                try {
                    String uri = element.attr("href");
                    String url = platform.getUrl() + uri.replace("/", "");
                    LiveRoom liveRoom = parse(url);
                    liveRoom.setPlatform(pg.getPlatform());
                    liveRoom.setGame(pg.getGame());
                    upsertLiveRoom(liveRoom);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

}
