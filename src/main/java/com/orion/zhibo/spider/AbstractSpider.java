/**
 * Copyright 2015 meituan.com. All Rights Reserved.
 */
package com.orion.zhibo.spider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

import com.orion.zhibo.dao.GameDao;
import com.orion.zhibo.dao.LiveRoomDao;
import com.orion.zhibo.dao.PlatformDao;
import com.orion.zhibo.dao.PlatformGameDao;
import com.orion.zhibo.entity.LiveRoom;
import com.orion.zhibo.entity.Platform;

/**
 * description here
 *
 * @author yezi
 * @since 2015年9月3日
 */
public abstract class AbstractSpider implements Spider, InitializingBean {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    GameDao gameDao;
    @Autowired
    LiveRoomDao liveRoomDao;
    @Autowired
    PlatformDao platfromDao;
    @Autowired
    PlatformGameDao platformGameDao;

    Platform platform;

    Map<String, String> header = new HashMap<>();

    Map<String, LiveRoom> liveRooms = new ConcurrentHashMap<>();

    boolean isDebug;
    
    ExecutorService exe = Executors.newCachedThreadPool();

    @Override
    public void afterPropertiesSet() throws Exception {
        platform = platfromDao.getByAbbr(customPlatform());
        exe.submit(new Runnable() {
            public void run() {
                List<LiveRoom> list = liveRoomDao.listByPlatform(platform);
                for (LiveRoom liveRoom : list) {
                    liveRooms.put(liveRoom.getUrl(), liveRoom);
                }
                logger.info("load {} live room count {}", platform.getName(), list.size());
            }
        });
        
        customHeader();
    }

    protected abstract String customPlatform();

    protected void customHeader() {
        header.put(HttpHeaders.USER_AGENT,
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.13 Safari/537.36");
        header.put(HttpHeaders.HOST, platform.getHost());
        header.put(HttpHeaders.REFERER, platform.getUrl());
    }

    protected void updateRoom(LiveRoom liveRoom) {
        if (!isDebug) {
            liveRoomDao.update(liveRoom);
        }
        logger.info("update room {}", liveRoom);
    }

    protected void createRoom(LiveRoom liveRoom) {
        LiveRoom temp = liveRoomDao.getByPlatformAndUid(platform, liveRoom.getUid());
        if (temp != null) {
            liveRooms.put(temp.getUrl(), temp);
        } else {
            if (isDebug || liveRoom.isAvaliable()) {
                if (!isDebug) {
                    liveRoomDao.create(liveRoom);
                }
                liveRooms.put(liveRoom.getUrl(), liveRoom);
                logger.info("create new room {}", liveRoom);
            }
        }
    }
}
