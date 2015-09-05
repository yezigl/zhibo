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

import com.orion.zhibo.spider.Spider;

/**
 * description here
 *
 * @author lidehua
 * @since 2015年9月2日
 */
@Component
public class SpiderTask {

    private Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    List<Spider> spiders;
    
    @Scheduled(cron = "0 */5 * * * ?")
    public void run() {
        logger.info("spider run");
        for (Spider spider : spiders) {
            spider.run();
        }
    }
}
