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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @ModelAttribute("noad")
    public boolean analyse() {
        return true;
    }

    @RequestMapping(value = "/manage", method = RequestMethod.GET)
    public String man(Model model) {
        return "manage";
    }

    @RequestMapping(value = "/manage/games", method = RequestMethod.GET)
    public String games(@ModelAttribute Game game, Model model) {
        model.addAttribute("games", gameRepository.findAll());

        return "games";
    }

    @RequestMapping(value = "/manage/games/{id}", method = RequestMethod.GET)
    public String gameGet(@PathVariable String id, Model model) {
        if (!id.equals("0")) {
            model.addAttribute("game", gameRepository.findOne(id));
        }

        return "game";
    }

    @RequestMapping(value = "/manage/games/{id}", method = RequestMethod.POST)
    public String gamePost(@ModelAttribute Game game, @PathVariable String id, Model model, RedirectAttributes ra) {
        if (!id.equals("0")) {
            game.setId(new ObjectId(id).toString());
        }
        gameRepository.save(game);

        return "redirect:/manage/games";
    }

    @RequestMapping(value = "/manage/platformgames", method = RequestMethod.GET)
    public String listPlatformGames(Model model) {
        model.addAttribute("platformgames", platformGameRepository.findAll());

        return "platformgames";
    }

    @RequestMapping(value = "/manage/platformgames/{id}", method = RequestMethod.GET)
    public String platformGameGet(@PathVariable String id, Model model) {
        model.addAttribute("platforms", platformRepository.findAll());
        model.addAttribute("games", gameRepository.findAll());
        if (!id.equals("0")) {
            model.addAttribute("pg", platformGameRepository.findOne(id));
        }
        return "platformgame";
    }

    @RequestMapping(value = "/manage/platformgames/{id}", method = RequestMethod.POST)
    public String platformGamePost(@PathVariable String id, @RequestParam String platform, @RequestParam String game,
            @RequestParam String platformUrl, Model model, RedirectAttributes ra) {
        if (!id.equals("0")) {
            PlatformGame pg = platformGameRepository.findOne(id);
            pg.setPlatformUrl(platformUrl);
            platformGameRepository.save(pg);
        } else {
            PlatformGame pg = new PlatformGame();
            pg.setPlatform(platformRepository.findByAbbr(platform));
            pg.setGame(gameRepository.findByAbbr(game));
            pg.setPlatformUrl(platformUrl);
            platformGameRepository.save(pg);
        }

        return "redirect:/manage/platformgames";
    }

    @RequestMapping(value = "/manage/platforms", method = RequestMethod.GET)
    public String platformsGet(Model model) {
        model.addAttribute("platforms", platformRepository.findAll());

        return "platforms";
    }

    @RequestMapping(value = "/manage/platforms/{id}", method = RequestMethod.GET)
    public String platformsGet(@PathVariable String id, Model model) {
        if (!id.equals("0")) {
            Platform platform = platformRepository.findOne(id);
            model.addAttribute("platform", platform);
        }

        return "platform";
    }

    @RequestMapping(value = "/manage/platforms/{id}", method = RequestMethod.POST)
    public String platformsPost(@ModelAttribute Platform platform, @PathVariable String id, Model model,
            RedirectAttributes ra) {
        model.addAttribute("platforms", platformRepository.findAll());
        if (!id.equals("0")) {
            platform.setId(new ObjectId(id).toString());
        }
        platformRepository.save(platform);

        return "redirect:/manage/platforms";
    }

    @RequestMapping(value = "/manage/platforms/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public String platformsDelete(@PathVariable String id, Model model) {
        if (!id.equals("0")) {
            Platform platform = platformRepository.findOne(id);
            platformRepository.delete(platform);
        }
        return "ok";
    }
}
