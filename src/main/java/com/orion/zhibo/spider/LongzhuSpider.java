/**
 * Copyright 2015 yezi.gl. All Rights Reserved.
 */
package com.orion.zhibo.spider;

import java.util.List;

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
    public void run() {
        List<PlatformGame> pgs = platformGameDao.listByPlatform(platform);
        for (PlatformGame pg : pgs) {
            Document document = Jsoup.parse(HttpUtils.get(pg.getPlatformUrl(), header, "UTF-8"));
            Elements elements = document.select("#list-con a");
            for (Element element : elements) {
                try {
                    String url = element.attr("href");
                    LiveRoom liveRoom = liveRooms.get(url);
                    Element views = element.select("ul.livecard-meta .livecard-meta-item-text").first();
                    int number = Utils.parseViews(views.text());
                    String thumbnail = element.select("img.livecard-thumb").attr("src");
                    if (liveRoom != null) {
                        liveRoom.setTitle(element.select("h3.listcard-caption").attr("title"));
                        liveRoom.setThumbnail(thumbnail);
                        liveRoom.setNumber(number);
                        liveRoom.setViews(views.text());
                        liveRoom.setUrl(url);
                        liveRoom.setStatus(LiveStatus.LIVING);
                        updateRoom(liveRoom);
                    } else {
                        liveRoom = new LiveRoom();
                        liveRoom.setPlatform(platform);
                        liveRoom.setGame(pg.getGame());
                        liveRoom.setStatus(LiveStatus.LIVING);
                        liveRoom.setNumber(number);
                        liveRoom.setViews(views.text());
                        document = Jsoup.parse(HttpUtils.get(url, header, "UTF-8"));
                        Elements scripts = document.select("script");
                        for (int i = 0; i < scripts.size(); i++) {
                            String script = scripts.get(i).data();
                            if (script.contains("var roomInfo =")) {
                                int s = script.indexOf("var roomInfo =");
                                String room = script.substring(s + 14);
                                s = room.indexOf("};");
                                room = room.substring(0, s + 1);
                                JSONObject roomObject;
                                try {
                                    roomObject = JSON.parseObject(room);
                                } catch (Exception e) {
                                    logger.error("parse json error", room, e);
                                    break;
                                }
                                liveRoom.setUid(roomObject.getString("UserId"));
                                liveRoom.setName(roomObject.getString("Name"));
                                liveRoom.setRoomId(roomObject.getString("BoardCast_Address"));
                                liveRoom.setTitle(StringUtils.defaultIfBlank(roomObject.getString("BoardCast_TitleV2"), roomObject.getString("BoardCast_Title")));
                                liveRoom.setLiveId(roomObject.getString("BoardCast_Address"));
                                liveRoom.setDescription(roomObject.getString("Desc"));
                                liveRoom.setUrl(url);
                                liveRoom.setThumbnail(thumbnail);
                                liveRoom.setAvatar(roomObject.getString("Logo"));
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
