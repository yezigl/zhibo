/**
 * Copyright 2015 meituan.com. All Rights Reserved.
 */
package com.orion.zhibo.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.orion.zhibo.dao.ActorRepository;
import com.orion.zhibo.dao.GameRepository;
import com.orion.zhibo.dao.LiveRoomRepository;
import com.orion.zhibo.dao.PlatformGameRepository;
import com.orion.zhibo.dao.PlatformRepository;
import com.orion.zhibo.entity.Game;
import com.orion.zhibo.model.ActorTag;

/**
 * description here
 *
 * @author lidehua
 * @since 2015年9月2日
 */
public abstract class BasicController {

    protected Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    protected GameRepository gameRepository;
    @Autowired
    protected PlatformRepository platformRepository;
    @Autowired
    protected PlatformGameRepository platformGameRepository;
    @Autowired
    protected ActorRepository actorRepository;
    @Autowired
    protected LiveRoomRepository liveRoomRepository;
    
    @Value("${profile}")
    String profile;
    
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("id");
    }
    
    @ModelAttribute("games")
    public List<Game> games() {
        return gameRepository.findAll();
    }
    
    @ModelAttribute("actorTags")
    public ActorTag[] actorTags() {
        return ActorTag.values();
    }
    
    @ModelAttribute("profile")
    public String profile() {
        return profile;
    }
}
