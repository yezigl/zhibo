/**
 * Copyright 2015 meituan.com. All Rights Reserved.
 */
package com.orion.zhibo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.orion.zhibo.service.ActorService;
import com.orion.zhibo.service.GameService;
import com.orion.zhibo.service.LiveRoomService;
import com.orion.zhibo.service.PlatformGameService;
import com.orion.zhibo.service.PlatformService;

/**
 * description here
 *
 * @author lidehua
 * @since 2015年9月2日
 */
public abstract class BasicController {

    protected Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    protected GameService gameService;
    @Autowired
    protected PlatformService platformService;
    @Autowired
    protected LiveRoomService liveRoomService;
    @Autowired
    protected PlatformGameService platformGameService;
    @Autowired
    protected ActorService actorService;
}
