/**
 * Copyright 2015 meituan.com. All Rights Reserved.
 */
package com.orion.zhibo.spider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

import com.orion.zhibo.entity.Actor;
import com.orion.zhibo.entity.LiveRoom;
import com.orion.zhibo.entity.Platform;
import com.orion.zhibo.service.ActorService;
import com.orion.zhibo.service.GameService;
import com.orion.zhibo.service.LiveRoomService;
import com.orion.zhibo.service.PlatformGameService;
import com.orion.zhibo.service.PlatformService;

/**
 * description here
 *
 * @author yezi
 * @since 2015年9月3日
 */
public abstract class AbstractSpider implements Spider, InitializingBean {

    protected Logger logger = LoggerFactory.getLogger(getClass());

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

    Platform platform;

    Map<String, String> header = new HashMap<>();

    boolean isDebug;

    protected ScheduledExecutorService exe = Executors.newScheduledThreadPool(5);

    @Override
    public void afterPropertiesSet() throws Exception {
        platform = platformService.getByAbbr(customPlatform());
        customHeader();
    }
    
    @Override
    public void run() {
        List<Actor> actors = actorService.listByPlatform(platform);
        for (Actor actor : actors) {
            try {
                parse(actor);
                TimeUnit.SECONDS.sleep(5);
            } catch (Exception e) {
                logger.error("prase error {}", e.getMessage(), e);
            }
        }
    }

    protected abstract String customPlatform();

    protected void customHeader() {
        header.put(HttpHeaders.USER_AGENT,
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.13 Safari/537.36");
        header.put(HttpHeaders.HOST, platform.getHost());
        header.put(HttpHeaders.REFERER, platform.getUrl());
    }

    protected void upsertLiveRoom(LiveRoom liveRoom) {
        if (!isDebug) {
            if (liveRoom.getId() != null) {
                liveRoomService.update(liveRoom);
            } else {
                liveRoomService.create(liveRoom);
            }
        }
    }
    
    protected String parseScript(String script, String var) {
        int s = script.indexOf(var);
        String room = script.substring(s + var.length());
        s = room.indexOf("};");
        room = room.substring(0, s + 1);
        return room;
    }
}
