/**
 * Copyright 2015 yezi.gl. All Rights Reserved.
 */
package com.orion.zhibo.spider;

import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.orion.core.utils.HttpUtils;
import com.orion.zhibo.entity.Game;
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
    public void run() {
        List<Game> games = gameDao.listByPlatform(platform);
        for (Game game : games) {
            Document document = Jsoup.parse(HttpUtils.get(game.getPlatformUrl(), header, "UTF-8"));
            Elements elements = document.select("#hotList li a.js-jump-link");
            for (Element element : elements) {
                try {
                    String uri = element.attr("href");
                    String url = platform.getUrl() + uri.replace("/", "");
                    LiveRoom liveRoom = liveRooms.get(url);
                    Element views = element.select(".info-area .meat .views span.dv").first();
                    int number = Utils.parseViews(views.text());
                    if (liveRoom != null) {
                        Element thumbnail = element.select(".imgBox img").first();
                        liveRoom.setTitle(thumbnail.attr("alt"));
                        liveRoom.setThumbnail(thumbnail.attr("src"));
                        liveRoom.setNumber(number);
                        liveRoom.setViews(views.text());
                        liveRoom.setUrl(url);
                        liveRoom.setStatus(LiveStatus.LIVING);
                        updateRoom(liveRoom);
                    } else {
                        liveRoom = new LiveRoom();
                        liveRoom.setPlatform(platform);
                        liveRoom.setGame(game);
                        liveRoom.setStatus(LiveStatus.LIVING);
                        liveRoom.setNumber(number);
                        liveRoom.setViews(views.text());
                        document = Jsoup.parse(HttpUtils.get(url, header, "UTF-8"));
                        Elements scripts = document.select("script");
                        for (int i = 0; i < scripts.size(); i++) {
                            String script = scripts.get(i).data();
                            if (script.contains("window.oPageConfig.oRoom =")) {
                                int s = script.indexOf("window.oPageConfig.oRoom =");
                                String room = script.substring(s + 26);
                                s = room.indexOf("};");
                                room = room.substring(0, s + 1);
                                JSONObject roomObject;
                                try {
                                    roomObject = JSON.parseObject(room);
                                } catch (Exception e) {
                                    logger.error("parse json error", room);
                                    break;
                                }
                                liveRoom.setUid(roomObject.getString("uid"));
                                liveRoom.setName(roomObject.getString("nickname"));
                                liveRoom.setRoomId(roomObject.getString("code"));
                                liveRoom.setTitle(roomObject.getString("title"));
                                liveRoom.setDescription(roomObject.getString("roomDesc"));
                                liveRoom.setUrl(url);
                                liveRoom.setThumbnail(roomObject.getString("bpic"));
                                liveRoom.setAvatar(roomObject.getString("avatar") + "-medium");
                                liveRoom.setNumber(roomObject.getIntValue("online"));
                                break;
                            }
                        }
                        createRoom(liveRoom);
                    }
                } catch (Exception e) {
                    logger.error("parse error", e);
                }
            }
        }
    }

}
