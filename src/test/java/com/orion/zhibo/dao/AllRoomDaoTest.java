/**
 * Copyright 2015 meituan.com. All Rights Reserved.
 */
package com.orion.zhibo.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.orion.zhibo.TestConfig;
import com.orion.zhibo.entity.AllRoom;

/**
 * description here
 *
 * @author lidehua
 * @since 2015年12月7日
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class AllRoomDaoTest {
    
    @Autowired
    AllRoomDao allRoomDao;

    /**
     * Test method for {@link com.orion.mongodb.dao.AbstractEntityDao#create(com.orion.mongodb.entity.AbstractEntity)}.
     */
    @Test
    public void testCreate() {
        AllRoom allRoom = new AllRoom();
        allRoomDao.create(allRoom);
        allRoomDao.delete(allRoom);
    }

}
