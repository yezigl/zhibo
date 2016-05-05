/**
 * Copyright 2015 yezi.gl. All Rights Reserved.
 */
package com.orion.zhibo.spider;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import com.orion.zhibo.entity.Game;
import com.orion.zhibo.entity.LiveRoom;
import com.orion.zhibo.entity.Platform;
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
    Pattern chtopidP = Pattern.compile("CHTOPID = '(\\d+)';");
    Pattern subchidP = Pattern.compile("SUBCHID = '(\\d+)';");

    String liveInfoUrl = "http://phone.huya.com/api/liveinfo?sid=%s&from=android&client=3.2.3&version=1&subsid=%s";

    @Override
    protected String customPlatform() {
        return "huya";
    }

    @Override
    public LiveRoom parse(String liveUrl) {
        Document document = Jsoup.parse(HttpUtils.get(liveUrl, header, "UTF-8"));
        LiveRoom liveRoom = liveRoomService.getByUrl(liveUrl);

        String chtopid, subchid;
        Elements scripts = document.select("script");
        for (int i = 0; i < scripts.size(); i++) {
            String script = scripts.get(i).data();
            if (script.contains("CHTOPID")) {
                Matcher chtopidmatcher = chtopidP.matcher(script);
                Matcher subchidmatcher = subchidP.matcher(script);
                chtopid = chtopidmatcher.find() ? chtopidmatcher.group(1) : "";
                subchid = subchidmatcher.find() ? subchidmatcher.group(1) : "";
                if (StringUtils.isBlank(subchid) || chtopid.equals("0")) {
                    if (liveRoom != null) {
                        liveRoom.setStatus(LiveStatus.CLOSE);
                        liveRoom.setNumber(0);
                        liveRoom.setViews(Utils.convertView(liveRoom.getNumber()));
                    }
                    break;
                }
                JSONObject liveInfo = JSON.parseObject(HttpUtils.get(String.format(liveInfoUrl, chtopid, subchid)));
                JSONObject info = liveInfo.getJSONObject("info");
                if (info == null || info.isEmpty()) {
                    break;
                }
                String title = document.select(".host-info .host-title").text();
                // 一般来说不变的信息
                if (liveRoom == null) {
                    liveRoom = new LiveRoom();
                    liveRoom.setLiveUrl(liveUrl);
                }
                liveRoom.setUid(info.getString("yyid"));
                liveRoom.setRoomId(info.getString("liveUid"));
                liveRoom.setLiveId(liveRoom.getRoomId());
                liveRoom.setFlashUrl(document.select("#flash-link").attr("value"));
                liveRoom.setName(info.getString("liveNick"));
                liveRoom.setTitle(StringUtils.defaultIfBlank(title, info.getString("liveName")));
                liveRoom.setDescription(info.getString("contentIntro"));
                liveRoom.setThumbnail(info.getString("snapshot"));
                liveRoom.setAvatar(info.getString("livePortait"));
                liveRoom.setNumber(info.getIntValue("users"));
                liveRoom.setViews(Utils.convertView(liveRoom.getNumber()));
                liveRoom.setStatus(LiveStatus.LIVING);
                break;
            }
        }

        return liveRoom;
    }

    @Override
    public void run() {
        Platform platform = platformService.getByAbbr("huya");
        List<PlatformGame> pgs = platformGameService.listByPlatform(platform);
        for (PlatformGame pg : pgs) {
            if (pg.getGame().getAbbr().equals(GameCate.LOL.abbr)) {
                loadByAjax(platform, pg.getGame());
                continue;
            }
            Document document = Jsoup.parse(HttpUtils.get(pg.getPlatformUrl(), header, "UTF-8"));
            Elements elements = document.select("#video-item-live li");
            for (Element element : elements) {
                try {
                    String url = element.select(".video-info").attr("href");
                    LiveRoom liveRoom = parse(url);
                    upsertLiveRoom(liveRoom);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

    private void loadByAjax(Platform platform, Game game) {
        String ret = HttpUtils.get(
                "http://www.huya.com/cache.php?gid=1&do=frontRecommendLive&m=GameSubject&callback=GameSubjectfrontRecommendLive");
        ret = ret.replace("GameSubjectfrontRecommendLive(", "");
        ret = ret.substring(0, ret.length() - 1);
        JSONObject retJson = JSON.parseObject(ret);
        JSONArray roomJsons = retJson.getJSONArray("result");
        parseJson(platform, game, ret, roomJsons);
        ret = HttpUtils.get(
                "http://www.huya.com/cache.php?gid=1&do=allKingLive&m=GameSubject&callback=GameSubjectallKingLive");
        ret = ret.replace("GameSubjectallKingLive(", "");
        ret = ret.substring(0, ret.length() - 1);
        retJson = JSON.parseObject(ret);
        roomJsons = retJson.getJSONArray("result");
        parseJson(platform, game, ret, roomJsons);
    }

    private void parseJson(Platform platform, Game game, String ret, JSONArray roomJsons) {
        for (int i = 0; i < roomJsons.size(); i++) {
            JSONObject roomJson = roomJsons.getJSONObject(i);
            if (roomJson.getString("privateHost") == null) {
                continue;
            }
            String url = platform.getUrl() + roomJson.getString("privateHost");
            LiveRoom liveRoom = parse(url);
            upsertLiveRoom(liveRoom);
        }
    }

}
