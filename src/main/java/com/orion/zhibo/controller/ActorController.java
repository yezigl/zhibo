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
import org.springframework.web.bind.annotation.RequestParam;

import com.orion.zhibo.entity.Actor;
import com.orion.zhibo.model.ActorTag;

/**
 * description here
 *
 * @author lidehua
 * @since 2015年10月19日
 */
@Controller
public class ActorController extends BasicController {

    @RequestMapping(value = "/actors", method = RequestMethod.GET)
    public String actorsGet(Model model) {
        model.addAttribute("actors", actorService.listAll());
        return "actors";
    }

    @RequestMapping(value = "/actors/{id}", method = RequestMethod.GET)
    public String actorGet(Model model, @PathVariable String id) {
        model.addAttribute("platforms", platformService.listAll());
        model.addAttribute("games", gameService.listAll());
        model.addAttribute("tags", ActorTag.values());

        if (!id.equals("0")) {
            Actor actor = actorService.get(id);
            model.addAttribute("actor", actor);
        }

        return "actor";
    }

    @RequestMapping(value = "/actors/{id}", method = RequestMethod.POST)
    public String actorPost(Model model, @PathVariable String id, @RequestParam String platform,
            @RequestParam String game, @RequestParam String name, @RequestParam String liveUrl,
            @RequestParam List<ActorTag> tags) {

        Actor actor;
        if (!id.equals("0")) {
            actor = actorService.get(id);
        } else {
            actor = actorService.getByUrl(liveUrl);
            if (actor == null) {
                actor = new Actor();
            }
        }
        actor.setPlatform(platformService.getByAbbr(platform));
        actor.setGame(gameService.getByAbbr(game));
        actor.setName(name);
        actor.setLiveUrl(liveUrl);
        actor.setTags(tags);
        
        actorService.upsert(actor);

        return "redirect:/actors";
    }
}
