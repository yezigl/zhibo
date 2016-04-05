/**
 * Copyright 2015 meituan.com. All Rights Reserved.
 */
package com.orion.zhibo.spider;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.orion.core.utils.HttpUtils;
import com.orion.zhibo.entity.Actor;
import com.orion.zhibo.entity.AllRoom;
import com.orion.zhibo.entity.Game;
import com.orion.zhibo.entity.LiveRoom;
import com.orion.zhibo.entity.Platform;
import com.orion.zhibo.entity.PlatformGame;
import com.orion.zhibo.model.GameCate;
import com.orion.zhibo.service.ActorService;
import com.orion.zhibo.service.AllRoomService;
import com.orion.zhibo.service.GameService;
import com.orion.zhibo.service.LiveRoomService;
import com.orion.zhibo.service.PlatformGameService;
import com.orion.zhibo.service.PlatformService;

/**
 * description here
 *
 * @author lidehua
 * @since 2015年12月7日
 */
@Component
public class AllPlatformSpider {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    GameService gameService;
    @Autowired
    PlatformService platformService;
    @Autowired
    LiveRoomService liveRoomService;
    @Autowired
    PlatformGameService platformGameService;
    @Autowired
    ActorService actorService;
    @Autowired
    AllRoomService allRoomService;

    @Autowired
    DouyuSpider douyuSpider;
    @Autowired
    HuyaSpider huyaSpider;
    @Autowired
    LongzhuSpider longzhuSpider;
    @Autowired
    PandaSpider pandaSpider;
    @Autowired
    ZhanqiSpider zhanqiSpider;

    boolean isDebug = false;

    public void run() {
        new Thread(() -> parseDouyu()).start();
        new Thread(() -> parseZhanqi()).start();
        new Thread(() -> parseLongzhu()).start();
        new Thread(() -> parsePanda()).start();
        new Thread(() -> parseHuya()).start();
    }

    private void upsertLiveRoom(AllRoom allRoom, Platform platform, Game game, String liveUrl) {
        if (allRoom == null) {
            return;
        }
        allRoom.setPlatform(platform);
        allRoom.setGame(game);
        allRoom.setLiveUrl(liveUrl);
        if (!isDebug) {
            AllRoom room = allRoomService.getByUid(platform, game, allRoom.getUid());
            if (room != null && room.getId() != null) {
                allRoom.setId(room.getId());
                allRoomService.update(allRoom);
            } else {
                allRoomService.create(allRoom);
            }
        } else {
            logger.info("liveRoom {}", allRoom);
        }
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void parseDouyu() {
        Platform platform = platformService.getByAbbr("douyu");
        List<PlatformGame> pgs = platformGameService.listByPlatform(platform);
        for (PlatformGame pg : pgs) {
            Document document = Jsoup.parse(HttpUtils.get(pg.getPlatformUrl(), douyuSpider.header, "UTF-8"));
            Elements elements = document.select("#live-list-contentbox li a");
            for (Element element : elements) {
                try {
                    String uri = element.attr("href");
                    String url = platform.getUrl() + uri.replace("/", "");
                    Actor actor = new Actor();
                    actor.setPlatform(platform);
                    actor.setGame(pg.getGame());
                    actor.setLiveUrl(url);
                    LiveRoom liveRoom = douyuSpider.parse(actor);
                    upsertLiveRoom(AllRoom.ofLiveRoom(liveRoom), platform, pg.getGame(), url);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

    private void parseZhanqi() {
        Platform platform = platformService.getByAbbr("zhanqi");
        List<PlatformGame> pgs = platformGameService.listByPlatform(platform);
        for (PlatformGame pg : pgs) {
            Document document = Jsoup.parse(HttpUtils.get(pg.getPlatformUrl(), zhanqiSpider.header, "UTF-8"));
            Elements elements = document.select("#hotList li a.js-jump-link");
            for (Element element : elements) {
                try {
                    String uri = element.attr("href");
                    String url = platform.getUrl() + uri.replace("/", "");
                    Actor actor = new Actor();
                    actor.setPlatform(platform);
                    actor.setGame(pg.getGame());
                    actor.setLiveUrl(url);
                    LiveRoom liveRoom = zhanqiSpider.parse(actor);
                    upsertLiveRoom(AllRoom.ofLiveRoom(liveRoom), platform, pg.getGame(), url);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

    private void parseLongzhu() {
        Platform platform = platformService.getByAbbr("longzhu");
        List<PlatformGame> pgs = platformGameService.listByPlatform(platform);
        for (PlatformGame pg : pgs) {
            Document document = Jsoup.parse(HttpUtils.get(pg.getPlatformUrl(), longzhuSpider.header, "UTF-8"));
            Elements elements = document.select("#list-con a");
            for (Element element : elements) {
                try {
                    String url = element.attr("href");
                    Actor actor = new Actor();
                    actor.setPlatform(platform);
                    actor.setGame(pg.getGame());
                    actor.setLiveUrl(url);
                    LiveRoom liveRoom = longzhuSpider.parse(actor);
                    upsertLiveRoom(AllRoom.ofLiveRoom(liveRoom), platform, pg.getGame(), url);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

    private void parsePanda() {
        Platform platform = platformService.getByAbbr("panda");
        List<PlatformGame> pgs = platformGameService.listByPlatform(platform);
        for (PlatformGame pg : pgs) {
            Document document = Jsoup.parse(HttpUtils.get(pg.getPlatformUrl(), pandaSpider.header, "UTF-8"));
            Elements elements = document.select("#sortdetail-container li a");
            for (Element element : elements) {
                try {
                    String uri = element.attr("href");
                    String url = platform.getUrl() + uri.substring(1, uri.length());
                    Actor actor = new Actor();
                    actor.setPlatform(platform);
                    actor.setGame(pg.getGame());
                    actor.setLiveUrl(url);
                    LiveRoom liveRoom = pandaSpider.parse(actor);
                    upsertLiveRoom(AllRoom.ofLiveRoom(liveRoom), platform, pg.getGame(), url);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

    private void parseHuya() {
        Platform platform = platformService.getByAbbr("huya");
        List<PlatformGame> pgs = platformGameService.listByPlatform(platform);
        for (PlatformGame pg : pgs) {
            if (pg.getGame().getAbbr().equals(GameCate.LOL.abbr)) {
                loadByAjax(platform, pg.getGame());
                continue;
            }
            Document document = Jsoup.parse(HttpUtils.get(pg.getPlatformUrl(), huyaSpider.header, "UTF-8"));
            Elements elements = document.select("#video-item-live li");
            for (Element element : elements) {
                try {
                    String url = element.select(".video-info").attr("href");
                    Actor actor = new Actor();
                    actor.setPlatform(platform);
                    actor.setGame(pg.getGame());
                    actor.setLiveUrl(url);
                    LiveRoom liveRoom = huyaSpider.parse(actor);
                    upsertLiveRoom(AllRoom.ofLiveRoom(liveRoom), platform, pg.getGame(), url);
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
            Actor actor = new Actor();
            actor.setPlatform(platform);
            actor.setGame(game);
            actor.setLiveUrl(url);
            LiveRoom liveRoom = huyaSpider.parse(actor);
            upsertLiveRoom(AllRoom.ofLiveRoom(liveRoom), platform, game, url);
        }
    }
}
