/**
 * Copyright 2015 yezi.gl. All Rights Reserved.
 */
package com.orion.zhibo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.orion.zhibo.entity.Game;
import com.orion.zhibo.entity.Platform;
import com.orion.zhibo.model.GameCate;

/**
 * description here
 *
 * @author yezi
 * @since 2015年9月7日
 */
@Controller
public class ManageController extends BasicController {

    @RequestMapping(value = "/man", method = RequestMethod.GET)
    public String man(Model model) {
        model.addAttribute("platforms", platformService.getAll());
        model.addAttribute("games", GameCate.toList());
        return "manage";
    }

    @RequestMapping(value = "/man/platforms", method = RequestMethod.POST)
    public String manPlatforms(Platform platform, Model model) {
        model.addAttribute("platforms", platformService.getAll());
        model.addAttribute("games", GameCate.toList());

        platformService.create(platform);
        return "manage";
    }

    @RequestMapping(value = "/man/games", method = RequestMethod.POST)
    public String manGames(@RequestParam String platform, @RequestParam String game, @RequestParam String platformUrl,
            Model model) {
        model.addAttribute("platforms", platformService.getAll());
        model.addAttribute("games", GameCate.toList());

        Platform p = platformService.getByAbbr(platform);
        GameCate gc = GameCate.valueOfAbbr(game);
        Game g = new Game();
        g.setPlatform(p);
        g.setPlatformUrl(platformUrl);
        g.setName(gc.name);
        g.setAbbr(gc.abbr);
        g.setIcon(gc.icon);
        gameService.create(g);
        return "manage";
    }
}
