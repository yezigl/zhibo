/**
 * Copyright 2015 meituan.com. All Rights Reserved.
 */
package com.orion.zhibo.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.orion.zhibo.entity.Game;
import com.orion.zhibo.model.ActorTag;
import com.orion.zhibo.service.ActorService;
import com.orion.zhibo.service.AllRoomService;
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
    @Autowired
    protected AllRoomService allRoomService;
    
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("id");
    }
    
    @ModelAttribute("games")
    public List<Game> games() {
        return gameService.listAll();
    }
    
    @ModelAttribute("actorTags")
    public ActorTag[] actorTags() {
        return ActorTag.values();
    }
}
