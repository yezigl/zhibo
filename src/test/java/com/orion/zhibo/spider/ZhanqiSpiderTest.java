/**
 * Copyright 2015 yezi.gl. All Rights Reserved.
 */
package com.orion.zhibo.spider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.orion.zhibo.TestConfig;
import com.orion.zhibo.entity.Actor;
import com.orion.zhibo.service.ActorService;

/**
 * description here
 *
 * @author yezi
 * @since 2015年10月27日
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class ZhanqiSpiderTest {

    @Autowired
    ZhanqiSpider zhanqiSpider;
    @Autowired
    ActorService actorService;
    
    @Test
    public void test() {
        Actor actor = actorService.get("562f6e5319a2ab5e7a57294f");
        zhanqiSpider.parse(actor);
    }

}
