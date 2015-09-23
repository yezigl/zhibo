/**
 * Copyright 2015 meituan.com. All Rights Reserved.
 */
package com.orion.zhibo.task;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.orion.zhibo.dao.LiveRoomDao;
import com.orion.zhibo.entity.LiveRoom;
import com.orion.zhibo.model.LiveStatus;
import com.orion.zhibo.service.LiveRoomService;

/**
 * description here
 *
 * @author yezi
 * @since 2015年9月5日
 */
@Component
public class CheckLiveTask {

    private Logger logger = LoggerFactory.getLogger(getClass());
    
    private static final long UNLIVING_TIME = 5 * 60 * 1000;
    
    private static final long DELETE_TIME = 7 * 24 * 60 * 60 * 1000;

    @Autowired
    LiveRoomDao liveRoomDao;
    @Autowired
    LiveRoomService liveRoomService;

    @Scheduled(initialDelay = 360000, fixedDelay = 60000)
    public void run() {
        logger.info("check live run");
        List<LiveRoom> list = liveRoomService.listAllLiving();
        for (LiveRoom liveRoom : list) {
            if (System.currentTimeMillis() - liveRoom.getUpdateTime().getTime() > UNLIVING_TIME) {
                liveRoom.setNumber(0);
                liveRoom.setViews("0");
                liveRoom.setStatus(LiveStatus.CLOSE);
                liveRoomDao.update(liveRoom);
                logger.info("live room {} {} close", liveRoom.getPlatform().getAbbr(), liveRoom.getName());
            } else if (System.currentTimeMillis() - liveRoom.getUpdateTime().getTime() > DELETE_TIME) {
                liveRoomDao.delete(liveRoom);
                logger.info("live room {} {} delete", liveRoom.getPlatform().getAbbr(), liveRoom.getName());
            }
        }
    }
}
