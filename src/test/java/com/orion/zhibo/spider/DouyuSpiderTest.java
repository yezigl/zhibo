/**
 * Copyright 2015 meituan.com. All Rights Reserved.
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
 * @since 2015年9月4日
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class DouyuSpiderTest {

    @Autowired
    DouyuSpider douyuSpider;
    
    /**
     * Test method for {@link com.orion.zhibo.spider.DouyuSpider#run()}.
     */
    @Test
    public void testRun() {
        douyuSpider.run();
    }

}
