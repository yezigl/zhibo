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
        p.setName("战旗");
        p.setAbbr("zhanqi");
        p.setUrl("http://www.zhanqi.tv/");
        p.setHost("www.zhanqi.tv");
        p.setIcon("http://www.zhanqi.tv/favicon.ico");
        platformDao.create(p);
    }
    
    @Test
    public void updatePlatform() {
        Platform p = platformDao.get("55ebc2d00e37de27e88c34b1");
        p.setSharePattern("http://dlstatic.cdn.zhanqi.tv/assets/swf/shell.swf?v=20150901.04&AnchorId=1&RoomId=%s&fhost=unknow&LiveUrl=1&ComDef=1&Mute=0&logoPos=1&EmbedDc=1&pv=20150908.04");
        System.out.println(p);
        platformDao.update(p);
    }

}
