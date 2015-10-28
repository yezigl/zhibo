/**
 * Copyright 2015 yezi.gl. All Rights Reserved.
 */
package com.orion.zhibo.controller;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @RequestMapping(value = "/manage", method = RequestMethod.GET)
    public String man(Model model) {
        model.addAttribute("platforms", platformService.listAll());
        model.addAttribute("games", gameService.listAll());
        return "manage";
    }

    @RequestMapping(value = "/manage/platforms", method = RequestMethod.POST)
    public String manPlatforms(@ModelAttribute Platform platform, Model model) {
        model.addAttribute("platforms", platformService.listAll());
        model.addAttribute("games", gameService.listAll());

        platformService.create(platform);
        return "redirect:/manage";
    }

    @RequestMapping(value = "/manage/games", method = RequestMethod.POST)
    public String manGames(@ModelAttribute Game game, Model model) {
        model.addAttribute("platforms", platformService.listAll());
        model.addAttribute("games", gameService.listAll());

        gameService.create(game);
        return "redirect:/manage";
    }

    @RequestMapping(value = "/manage/platformgames", method = RequestMethod.POST)
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
        return "redirect:/manage";
    }
    
    @RequestMapping(value = "/manage/platformgames", method = RequestMethod.GET)
    public String listPlatformGames(Model model) {
        model.addAttribute("platformgames", platformGameService.getAll());
        return "platformgames";
    }
    
    @RequestMapping(value = "/manage/platforms", method = RequestMethod.GET)
    public String platformsGet(Model model) {
        model.addAttribute("platforms", platformService.listAll());

        return "platformlist";
    }
    
    @RequestMapping(value = "/manage/platforms/{id}", method = RequestMethod.GET)
    public String platformsGet(@PathVariable String id, Model model) {
        if (!id.equals("0")) {
            Platform platform = platformService.get(id);
            model.addAttribute("platform", platform);
        }

        return "platform";
    }
    
    @RequestMapping(value = "/manage/platforms/{id}", method = RequestMethod.POST)
    public String platformsPost(@ModelAttribute Platform platform, @PathVariable String id, Model model) {
        model.addAttribute("platforms", platformService.listAll());
        if (!id.equals("0")) {
            platform.setId(new ObjectId(id));
        }
        platformService.upsert(platform);

        return "platformlist";
    }
    
    @RequestMapping(value = "/manage/platforms/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public String platformsDelete(@PathVariable String id, Model model) {
        if (!id.equals("0")) {
            Platform platform = platformService.get(id);
            platformService.delete(platform);
        }
        return "ok";
    }
}
