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

/**
 * description here
 *
 * @author yezi
 * @since 2015年9月9日
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class HuyaSpiderTest {

    @Autowired
    HuyaSpider huyaSpider;
    
    /**
     * Test method for {@link com.orion.zhibo.spider.HuyaSpider#run()}.
     */
    @Test
    public void testRun() {
        huyaSpider.run();
    }

}
