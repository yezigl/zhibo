/**
 * Copyright 2015 yezi.gl. All Rights Reserved.
 */
package com.orion.zhibo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.orion.zhibo.entity.Game;
import com.orion.zhibo.entity.Platform;
import com.orion.zhibo.entity.PlatformGame;

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
        model.addAttribute("platforms", platformService.listAll());
        model.addAttribute("games", gameService.listAll());
        return "manage";
    }

    @RequestMapping(value = "/man/platforms", method = RequestMethod.POST)
    public String manPlatforms(@ModelAttribute Platform platform, Model model) {
        model.addAttribute("platforms", platformService.listAll());
        model.addAttribute("games", gameService.listAll());

        platformService.create(platform);
        return "redirect:/man";
    }

    @RequestMapping(value = "/man/games", method = RequestMethod.POST)
    public String manGames(@ModelAttribute Game game, Model model) {
        model.addAttribute("platforms", platformService.listAll());
        model.addAttribute("games", gameService.listAll());

        gameService.create(game);
        return "redirect:/man";
    }

    @RequestMapping(value = "/man/platformgames", method = RequestMethod.POST)
    public String manPlatformGames(@RequestParam String platform, @RequestParam String game,
            @RequestParam String platformUrl, Model model) {
        model.addAttribute("platforms", platformService.listAll());
        model.addAttribute("games", gameService.listAll());

        Platform p = platformService.getByAbbr(platform);
        Game g = gameService.getByAbbr(game);
        PlatformGame pg = new PlatformGame();
        pg.setGame(g);
        pg.setPlatform(p);
        pg.setPlatformUrl(platformUrl);
        platformGameService.create(pg);
        return "redirect:/man";
    }
    
    @RequestMapping(value = "/man/platformgames", method = RequestMethod.GET)
    public String listPlatformGames(Model model) {
        model.addAttribute("platformgames", platformGameService.getAll());
        return "platformgames";
    }
}
