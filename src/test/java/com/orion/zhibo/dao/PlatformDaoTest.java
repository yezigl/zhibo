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
import com.orion.zhibo.entity.Platform;

/**
 * description here
 *
 * @author yezi
 * @since 2015年9月3日
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class PlatformDaoTest {
    
    @Autowired
    PlatformDao platformDao;

    @Test
    public void testCreate() {
        Platform p = new Platform();
        p.setName("斗鱼");
        p.setAbbr("douyu");
        p.setUrl("http://www.douyutv.com/");
        platformDao.create(p);
    }
    
    @Test
    public void updatePlatform() {
        Platform p = platformDao.get("55e85ec20e37de20d024f49b");
        p.setHost("www.douyutv.com");
        System.out.println(p);
        platformDao.update(p);
    }

}
