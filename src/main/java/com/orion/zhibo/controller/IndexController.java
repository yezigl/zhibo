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
import com.orion.zhibo.entity.AllRoom;
import com.orion.zhibo.entity.Game;
import com.orion.zhibo.entity.LiveRoom;
import com.orion.zhibo.model.ActorTag;

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
        setUpModel(model, "/game/all", "all", "ALL", 0, 20, q);
        return "index";
    }

    @RequestMapping(value = "/game/{game}/{offset}", method = RequestMethod.GET)
    public String games(@PathVariable String game, @PathVariable int offset, Model model) {
        setUpModel(model, "/game/" + game, game, "ALL", offset, 20);
        return "index";
    }

    @RequestMapping(value = "/tag/{tag}/{offset}", method = RequestMethod.GET)
    public String tags(@PathVariable String tag, @PathVariable int offset, Model model) {
        setUpModel(model, "/tag/" + tag, "all", tag, offset, 20);
        return "index";
    }

    private void setUpModel(Model model, String path, String game, String tag, int offset, int limit) {
        setUpModel(model, path, game, tag, offset, limit, null);
    }

    private void setUpModel(Model model, String path, String game, String tag, int offset, int limit, String keyword) {
        model.addAttribute("path", "/");
        model.addAttribute("uri", path);
        model.addAttribute("offset", offset);
        model.addAttribute("limit", limit);
        model.addAttribute("q", keyword);
        Game g = game.equals("all") ? Game.ALL : gameService.getByAbbr(game);
        ActorTag t = tag.equals("ALL") ? null : ActorTag.valueOf(tag);
        List<LiveRoom> list = liveRoomService.list(g, t, offset, limit, keyword);
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

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String all(@RequestParam(required = false) String q, Model model) {
        setUpAllModel(model, "all", 0, 20, q);
        return "indexall";
    }

    @RequestMapping(value = "/{game}/{offset}", method = RequestMethod.GET)
    public String allPage(@PathVariable String game, @PathVariable int offset, @RequestParam(required = false) String q,
            Model model) {
        setUpAllModel(model, "all", offset, 20, q);
        return "indexall";
    }

    private void setUpAllModel(Model model, String game, int offset, int limit, String keyword) {
        model.addAttribute("path", "/all");
        model.addAttribute("uri", "/all");
        model.addAttribute("offset", offset);
        model.addAttribute("limit", limit);
        model.addAttribute("q", keyword);
        Game g = game.equals("all") ? Game.ALL : gameService.getByAbbr(game);
        List<AllRoom> list = allRoomService.list(g, offset, limit, keyword);
        model.addAttribute("liveRooms", list);
        model.addAttribute("size", list.size());
    }
}
