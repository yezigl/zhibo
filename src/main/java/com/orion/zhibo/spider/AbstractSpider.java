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

import com.orion.zhibo.dao.ActorRepository;
import com.orion.zhibo.dao.GameRepository;
import com.orion.zhibo.dao.LiveRoomRepository;
import com.orion.zhibo.dao.PlatformGameRepository;
import com.orion.zhibo.dao.PlatformRepository;
import com.orion.zhibo.entity.LiveRoom;
import com.orion.zhibo.entity.Platform;
import com.orion.zhibo.model.LiveStatus;
import com.orion.zhibo.service.CacheService;
import com.orion.zhibo.task.CheckLiveTask;

/**
 * description here
 *
 * @author yezi
 * @since 2015年9月3日
 */
public abstract class AbstractSpider implements Spider, InitializingBean {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    GameRepository gameRepository;
    @Autowired
    PlatformRepository platformRepository;
    @Autowired
    PlatformGameRepository platformGameRepository;
    @Autowired
    ActorRepository actorRepository;
    @Autowired
    CacheService cacheService;
    @Autowired
    LiveRoomRepository liveRoomRepository;

    Platform platform;

    Map<String, String> header = new HashMap<>();

    boolean isDebug = false;

    protected ScheduledExecutorService exe = Executors.newScheduledThreadPool(5);

    @Override
    public void afterPropertiesSet() throws Exception {
        platform = platformRepository.findByAbbr(customPlatform());
        if (platform != null) {
            customHeader();
        }
    }
    
    @Override
    public void run() {
        if (platform == null) {
            logger.warn("platform is null");
            return;
        }
        if (platform.isFetch()) {
            runFetch();
        }
    }
    
    @Override
    public void check() {
        long s = System.currentTimeMillis();
        List<LiveRoom> list = liveRoomRepository.findByPlatformAndStatus(platform, LiveStatus.LIVING);
        logger.info("get living: {}, spend {}ms", list.size(), System.currentTimeMillis() - s);
        for (LiveRoom liveRoom : list) {
            if (System.currentTimeMillis() - liveRoom.getUpdateTime().getTime() > CheckLiveTask.UNLIVING_TIME) {
                liveRoom.setNumber(0);
                liveRoom.setViews("0");
                liveRoom.setStatus(LiveStatus.CLOSE);
                liveRoomRepository.save(liveRoom);
                logger.info("liveroom {} {} close", liveRoom.getPlatform().getAbbr(), liveRoom.getName());
            } else if (System.currentTimeMillis() - liveRoom.getUpdateTime().getTime() > CheckLiveTask.DELETE_TIME) {
                liveRoomRepository.delete(liveRoom);
                logger.info("liveroom {} {} delete", liveRoom.getPlatform().getAbbr(), liveRoom.getName());
            }
        }
    }
    
    protected abstract String customPlatform();
    
    protected abstract void runFetch();

    protected void customHeader() {
        header.put(HttpHeaders.USER_AGENT,
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.13 Safari/537.36");
        header.put(HttpHeaders.HOST, platform.getHost());
        header.put(HttpHeaders.REFERER, platform.getUrl());
    }

    protected void upsertLiveRoom(LiveRoom liveRoom) {
        if (liveRoom == null) {
            return;
        }
        if (!isDebug) {
            if (liveRoom.getId() != null) {
                liveRoomRepository.save(liveRoom);
            } else {
                if (liveRoom.getNumber() > 1000) {
                    liveRoomRepository.save(liveRoom);
                }
            }
        } else {
            logger.info("liveRoom {}", liveRoom);
        }
    }
    
    protected String parseScript(String script, String var) {
        int s = script.indexOf(var);
        String room = script.substring(s + var.length());
        s = room.indexOf("};");
        room = room.substring(0, s + 1);
        return room;
    }
    
    protected void schedule(Runnable runnable) {
        exe.scheduleAtFixedRate(new Runnable() {
            
            @Override
            public void run() {
                if (platform != null) {
                    runnable.run();
                }
            }
        }, 1, 10, TimeUnit.MINUTES);
    }
}
