/**
 * Copyright 2015 meituan.com. All Rights Reserved.
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
import com.orion.zhibo.entity.Game;
import com.orion.zhibo.entity.LiveRoom;
import com.orion.zhibo.model.LiveStatus;
import com.orion.zhibo.utils.Utils;

import orion.core.utils.HttpUtils;

/**
 * description here
 *
 * @author yezi
 * @since 2015年9月3日
 */
@Component
public class DouyuSpider extends AbstractSpider {

    @Override
    protected String customPlatform() {
        return "douyu";
    }

    @Override
    public void run() {
        List<Game> games = gameDao.listByPlatform(platform);
        for (Game game : games) {
            Document document = Jsoup.parse(HttpUtils.get(game.getPlatformUrl(), header, "UTF-8"));
            Elements elements = document.select("#item_data ul li a.list");
            for (Element element : elements) {
                try {
                    String uri = element.attr("href");
                    String url = platform.getUrl() + uri.replace("/", "");
                    LiveRoom liveRoom = liveRooms.get(url);
                    if (liveRoom != null) {
                        Element thumbnail = element.select("img.lazy").first();
                        Element views = element.select("p.moreMes .view").first();
                        liveRoom.setTitle(element.attr("title"));
                        liveRoom.setThumbnail(thumbnail.attr("data-original"));
                        liveRoom.setNumber(Utils.parseViews(views.text()));
                        liveRoom.setViews(views.text());
                        liveRoom.setUrl(url);
                        liveRoom.setStatus(LiveStatus.LIVING);
                        liveRoomDao.update(liveRoom);
                        logger.info("update room {}", liveRoom);
                    } else {
                        liveRoom = new LiveRoom();
                        liveRoom.setPlatform(platform);
                        liveRoom.setGame(game);
                        liveRoom.setStatus(LiveStatus.LIVING);
                        document = Jsoup.parse(HttpUtils.get(url, header, "UTF-8"));
                        Elements scripts = document.select("script");
                        Element avatar = document.select(".room_mes .h_tx img").first();
                        for (int i = 0; i < scripts.size(); i++) {
                            if (scripts.get(i).data().contains("var $ROOM =")) {
                                String room = scripts.get(i).data().replace("var $ROOM =", "").replace(";", "");
                                JSONObject roomObject;
                                try {
                                    roomObject = JSON.parseObject(room);
                                } catch (Exception e) {
                                    logger.error("parse json error", room);
                                    break;
                                }
                                liveRoom.setUid(roomObject.getString("owner_uid"));
                                liveRoom.setName(roomObject.getString("owner_name"));
                                liveRoom.setRoomId(roomObject.getString("room_id"));
                                liveRoom.setTitle(roomObject.getString("room_name"));
                                liveRoom.setDescription(roomObject.getJSONObject("room_gg").getString("show"));
                                liveRoom.setUrl(roomObject.getString("room_url"));
                                liveRoom.setThumbnail(roomObject.getString("room_pic"));
                                liveRoom.setAvatar(avatar.attr("src"));
                                break;
                            }
                        }
                        if (liveRoom.isAvaliable()) {
                            liveRoomDao.create(liveRoom);
                            liveRooms.put(liveRoom.getUrl(), liveRoom);
                            logger.info("create new room {}", liveRoom);
                        }
                    }
                } catch (Exception e) {
                    logger.error("parse error", e);
                }
            }
        }
    }

}
