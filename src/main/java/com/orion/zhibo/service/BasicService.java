/**
 * Copyright 2015 meituan.com. All Rights Reserved.
 */
package com.orion.zhibo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.orion.zhibo.dao.GameDao;
import com.orion.zhibo.dao.LiveRoomDao;
import com.orion.zhibo.dao.PlatformDao;

/**
 * description here
 *
 * @author lidehua
 * @since 2015年9月2日
 */
public class BasicService {

    protected Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    protected GameDao gameDao;
    @Autowired
    protected PlatformDao platformDao;
    @Autowired
    protected LiveRoomDao liveRoomDao;
}
