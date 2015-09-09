/**
 * Copyright 2015 yezi.gl. All Rights Reserved.
 */
package com.orion.zhibo.spider;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.orion.core.utils.HttpUtils;
import com.orion.zhibo.entity.Game;
import com.orion.zhibo.entity.LiveRoom;
import com.orion.zhibo.entity.PlatformGame;
import com.orion.zhibo.model.GameCate;
import com.orion.zhibo.model.LiveStatus;
import com.orion.zhibo.utils.Utils;

/**
 * description here
 *
 * @author yezi
 * @since 2015年9月6日
 */
@Component
public class HuyaSpider extends AbstractSpider {
    
    Pattern yyid = Pattern.compile("YYID = '(\\d+)';");
    Pattern uid = Pattern.compile("l_p = '(\\d+)';");

    @Override
    protected String customPlatform() {
        return "huya";
    }

    @Override
    public void run() {
        List<PlatformGame> pgs = platformGameDao.listByPlatform(platform);
        for (PlatformGame pg : pgs) {
            if (pg.getGame().getAbbr().equals(GameCate.LOL.abbr)) {
                loadByAjax(pg.getGame());
                continue;
            }
            Document document = Jsoup.parse(HttpUtils.get(pg.getPlatformUrl(), header, "UTF-8"));
            Elements elements = document.select("#video-item-live li");
            for (Element element : elements) {
                try {
                    String url = element.select(".video-info").attr("href");
                    LiveRoom liveRoom = liveRooms.get(url);
                    String views = Utils.convertView(element.select(".txt .num").first().text());
                    String thumbnail = element.select(".video-info .pic").attr("src");
                    String name = element.select(".video-info .game-anchor p").text();
                    String avatar = element.select(".video-info .avatar img").attr("src");
                    String title = element.select(".video-info .game-anchor h6").text();
                    int number = Utils.parseViews(views);
                    if (liveRoom != null) {
                        liveRoom.setTitle(title);
                        liveRoom.setThumbnail(thumbnail);
                        liveRoom.setNumber(number);
                        liveRoom.setViews(views);
                        liveRoom.setUrl(url);
                        liveRoom.setStatus(LiveStatus.LIVING);
                        updateRoom(liveRoom);
                    } else {
                        liveRoom = new LiveRoom();
                        liveRoom.setPlatform(platform);
                        liveRoom.setGame(pg.getGame());
                        liveRoom.setStatus(LiveStatus.LIVING);
                        liveRoom.setNumber(number);
                        liveRoom.setViews(views);
                        document = Jsoup.parse(HttpUtils.get(url, header, "UTF-8"));
                        Elements scripts = document.select("script");
                        for (int i = 0; i < scripts.size(); i++) {
                            String script = scripts.get(i).data();
                            if (script.contains("CHTOPID")) {
                                Matcher yyidmatcher = yyid.matcher(script);
                                Matcher uidmatcher = uid.matcher(script);
                                String uid = uidmatcher.find() ? uidmatcher.group(1) : "";
                                liveRoom.setUid(yyidmatcher.find() ? yyidmatcher.group(1) : "");
                                liveRoom.setName(name);
                                liveRoom.setRoomId(uid);
                                liveRoom.setTitle(title);
                                String ret = HttpUtils.get(url, header, "UTF-8");
                                Document doc = Jsoup.parse(ret);
                                liveRoom.setLiveId(doc.select("#flash-link").attr("value"));                                liveRoom.setDescription("");
                                liveRoom.setUrl(url);
                                liveRoom.setThumbnail(thumbnail);
                                liveRoom.setAvatar(avatar);
                                liveRoom.setNumber(number);
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
    
    private void loadByAjax(Game game) {
        String ret = HttpUtils.get("http://www.huya.com/cache.php?gid=1&do=frontRecommendLive&m=GameSubject&callback=GameSubjectfrontRecommendLive");
        ret = ret.replace("GameSubjectfrontRecommendLive(", "");
        ret = ret.substring(0, ret.length() - 1);
        JSONObject retJson = JSON.parseObject(ret);
        JSONArray roomJsons = retJson.getJSONArray("result");
        parseJson(game, ret, roomJsons);
        ret = HttpUtils.get("http://www.huya.com/cache.php?gid=1&do=allKingLive&m=GameSubject&callback=GameSubjectallKingLive");
        ret = ret.replace("GameSubjectallKingLive(", "");
        ret = ret.substring(0, ret.length() - 1);
        retJson = JSON.parseObject(ret);
        roomJsons = retJson.getJSONArray("result");
        parseJson(game, ret, roomJsons);
    }
    
    private void parseJson(Game game, String ret, JSONArray roomJsons) {
        for (int i = 0; i < roomJsons.size(); i++) {
            JSONObject roomJson = roomJsons.getJSONObject(i);
            if (roomJson.getString("privateHost") == null) {
                continue;
            }
            String url = platform.getUrl() + roomJson.getString("privateHost");
            LiveRoom liveRoom = liveRooms.get(url);
            if (liveRoom != null) {
                liveRoom.setAvatar(roomJson.getString("avatar180"));
                liveRoom.setTitle(roomJson.getString("introduction"));
                liveRoom.setName(roomJson.getString("nick"));
                liveRoom.setThumbnail(roomJson.getString("screenshot"));
                liveRoom.setNumber(roomJson.getIntValue("totalCount"));
                liveRoom.setViews(Utils.convertView(roomJson.getString("totalCount")));
                liveRoom.setUid(roomJson.getString("yyid"));
                liveRoom.setRoomId(roomJson.getString("uid"));
                liveRoom.setUrl(url);
                liveRoom.setStatus(roomJson.getBooleanValue("isLive") ? LiveStatus.LIVING : LiveStatus.CLOSE);
                updateRoom(liveRoom);
            } else {
                liveRoom = new LiveRoom();
                liveRoom.setPlatform(platform);
                liveRoom.setGame(game);
                liveRoom.setStatus(roomJson.getBooleanValue("isLive") ? LiveStatus.LIVING : LiveStatus.CLOSE);
                liveRoom.setAvatar(roomJson.getString("avatar180"));
                liveRoom.setTitle(roomJson.getString("introduction"));
                liveRoom.setName(roomJson.getString("nick"));
                liveRoom.setThumbnail(roomJson.getString("screenshot"));
                liveRoom.setNumber(roomJson.getIntValue("totalCount"));
                liveRoom.setViews(Utils.convertView(roomJson.getString("totalCount")));
                liveRoom.setUid(roomJson.getString("yyid"));
                liveRoom.setRoomId(roomJson.getString("uid"));
                liveRoom.setUrl(url);
                ret = HttpUtils.get(url, header, "UTF-8");
                Document document = Jsoup.parse(ret);
                liveRoom.setLiveId(document.select("#flash-link").attr("value"));
                liveRoom.setDescription("");
                createRoom(liveRoom);
            }
        }
    }

}
