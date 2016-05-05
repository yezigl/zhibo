/**
 * Copyright 2015 meituan.com. All Rights Reserved.
 */
package com.orion.zhibo.dao;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.orion.zhibo.TestConfig;
import com.orion.zhibo.entity.Recommand;
import com.orion.zhibo.model.LiveStatus;

/**
 * description here
 *
 * @author yezi
 * @since 2015年9月5日
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class LiveRoomDaoTest {

    @Autowired
    RecomandDao liveRoomDao;
    @Autowired
    PlatformDao platformDao;
    
    @Test
    public void test() {
        List<Recommand> list = liveRoomDao.getAll();
        for (Recommand liveRoom : list) {
            liveRoom.setStatus(LiveStatus.LIVING);
            liveRoomDao.update(liveRoom);
        }
    }
    
    @Test
    public void testOK() {
        List<Recommand> list = liveRoomDao.getAll();
        for (Recommand liveRoom : list) {
            if ((System.currentTimeMillis() - liveRoom.getUpdateTime().getTime()) > 3600000) {
                System.out.println(liveRoom.getName() + " " + liveRoom.getNumber());
                liveRoomDao.delete(liveRoom);
            }
            if (liveRoom.getNumber() < 1000) {
                System.out.println(liveRoom.getName() + " " + liveRoom.getNumber());
                liveRoomDao.delete(liveRoom);
            }
        }
    }
}
