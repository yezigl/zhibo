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
 * @author yezi
 * @since 2015年9月5日
 */
@Component
public class CheckLiveTask {

    private Logger logger = LoggerFactory.getLogger(getClass());
    
    public static final long UNLIVING_TIME = 10 * 60 * 1000L;
    
    public static final long DELETE_TIME = 7 * 24 * 60 * 60 * 1000L;

    @Autowired
    List<Spider> spiders;

    @Scheduled(initialDelay = 360000, fixedDelay = 600000)
    public void run() {
        logger.info("check live run");
        spiders.forEach(s -> s.check());
    }
}