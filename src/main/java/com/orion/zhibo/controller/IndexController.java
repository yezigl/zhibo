/**
 * Copyright 2015 meituan.com. All Rights Reserved.
 */
package com.orion.zhibo.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.orion.core.utils.HttpUtils;
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
    public String index(@RequestParam(required = false) String q, Model model) {
        setUpModel(model, "all", "all", 0, 20, q);
        return "index";
    }

    @RequestMapping(value = "/s/{game}/{offset}", method = RequestMethod.GET)
    public String gameplarform(@PathVariable String game, @PathVariable int offset,
            Model model) {
        setUpModel(model, game, "all", offset, 20);
        return "index";
    }

    private void setUpModel(Model model, String game, String platform, int offset, int limit) {
        setUpModel(model, game, platform, offset, limit, null);
    }

    private void setUpModel(Model model, String game, String platform, int offset, int limit, String keyword) {
        model.addAttribute("currentGame", game);
        model.addAttribute("currentPlatform", platform);
        model.addAttribute("platforms", platformService.getAll());
        model.addAttribute("games", gameService.getAll());
        model.addAttribute("offset", offset);
        model.addAttribute("limit", limit);
        model.addAttribute("q", keyword);
        Platform p = platform.equalsIgnoreCase("all") ? Platform.ALL : platformService.getByAbbr(platform);
        Game g = game.equalsIgnoreCase("all") ? Game.ALL : gameService.getByAbbr(game);
        List<LiveRoom> list = liveRoomService.list(p, g, offset, limit, keyword);
        model.addAttribute("liveRooms", list);
        model.addAttribute("size", list.size());
    }

    @RequestMapping(value = "/image", method = RequestMethod.GET)
    public void fetchImage(@RequestParam String url, HttpServletRequest request, HttpServletResponse response) {
        byte[] bytes = HttpUtils.getBytes(url, null);

        response.setContentType("image/jpeg");

        try {
            OutputStream output = response.getOutputStream();
            output.write(bytes);
            output.flush();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
