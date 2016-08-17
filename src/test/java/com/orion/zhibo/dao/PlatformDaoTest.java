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
import com.orion.zhibo.service.PlatformService;

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
    @Autowired
    PlatformService platformService;

    @Test
    public void testCreate() {
        Platform p = new Platform();
        p.setName("战旗");
        p.setAbbr("zhanqi");
        p.setUrl("http://www.zhanqi.tv/");
        p.setHost("www.zhanqi.tv");
        p.setIcon("http://www.zhanqi.tv/favicon.ico");
        platformDao.save(p);
    }
    
    @Test
    public void testDelete() {
        platformDao.delete("55f0f6243887bf3c688c3d0c");
    }
    
    @Test
    public void testUpdate() {
        Platform platform = platformService.getByAbbr("zhanqi");
        platform.setSharePattern("http://dlstatic.cdn.zhanqi.tv/assets/swf/shell.swf?20151021.01");
        platformDao.save(platform);
    }
    
}
