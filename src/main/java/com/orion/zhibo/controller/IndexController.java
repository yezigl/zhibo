/**
 * Copyright 2015 meituan.com. All Rights Reserved.
 */
package com.orion.zhibo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.orion.zhibo.entity.Game;
import com.orion.zhibo.entity.LiveRoom;
import com.orion.zhibo.entity.Platform;
import com.orion.zhibo.model.GameCate;

/**
 * description here
 *
 * @author lidehua
 * @since 2015年9月2日
 */
@Controller
public class IndexController extends BasicController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model) {
        setUpModel(model, "all", "all");
        return "index";
    }

    @RequestMapping(value = "/s/{platform}", method = RequestMethod.GET)
    public String platform(@PathVariable String platform, Model model) {
        setUpModel(model, platform, "all");
        return "index";
    }

    @RequestMapping(value = "/s/{platform}/{game}", method = RequestMethod.GET)
    public String game(@PathVariable String platform, @PathVariable String game, Model model) {
        setUpModel(model, platform, game);
        return "index";
    }

    private void setUpModel(Model model, String platform, String game) {
        model.addAttribute("currentPlatform", platform);
        model.addAttribute("currentGame", game);
        List<Platform> platforms = platformService.getAll();
        model.addAttribute("platforms", platforms);
        model.addAttribute("games", GameCate.toList());
        Platform p;
        Game g;
        if (platform.equalsIgnoreCase("all")) {
            p = Platform.ALL;
        } else {
            p = platformService.getByAbbr(platform);
        }
        if (game.equalsIgnoreCase("all")) {
            g = Game.ALL;
        } else {
            g = gameService.getByAbbr(game);
        }
        List<LiveRoom> list = liveRoomService.listLiving(p, g);
        model.addAttribute("liveRooms", list);
    }

}
