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
        setUpModel(model, "all", 0, 20);
        return "index";
    }

    @RequestMapping(value = "/s/{game}", method = RequestMethod.GET)
    public String games(@PathVariable String game, Model model) {
        setUpModel(model, "all", 0, 20);
        return "index";
    }

    @RequestMapping(value = "/s/{game}/{offset}", method = RequestMethod.GET)
    public String pgames(@PathVariable String game, @PathVariable int offset, Model model) {
        setUpModel(model, "all", offset, 20);
        return "index";
    }

    private void setUpModel(Model model, String game, int offset, int limit) {
        model.addAttribute("currentGame", game);
        List<Platform> platforms = platformService.getAll();
        model.addAttribute("platforms", platforms);
        model.addAttribute("games", gameService.getAll());
        model.addAttribute("offset", offset);
        model.addAttribute("limit", limit);
        Platform p = Platform.ALL;
        Game g;
        if (game.equalsIgnoreCase("all")) {
            g = Game.ALL;
        } else {
            g = gameService.getByAbbr(game);
        }
        List<LiveRoom> list = liveRoomService.listLiving(p, g, offset, limit);
        model.addAttribute("liveRooms", list);
        model.addAttribute("size", list.size());
    }

}
