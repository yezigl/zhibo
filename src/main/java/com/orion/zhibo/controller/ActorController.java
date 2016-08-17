/**
 * Copyright 2015 meituan.com. All Rights Reserved.
 */
package com.orion.zhibo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    
    @ModelAttribute("noad")
    public boolean analyse() {
        return true;
    }

    @RequestMapping(value = "/actors", method = RequestMethod.GET)
    public String actorsGet(Model model) {
        model.addAttribute("actors", actorRepository.findAll());
        return "actorlist";
    }

    @RequestMapping(value = "/actors/{id}", method = RequestMethod.GET)
    public String actorGet(Model model, @PathVariable String id) {
        model.addAttribute("platforms", platformRepository.findAll());
        model.addAttribute("games", gameRepository.findAll());
        model.addAttribute("tags", ActorTag.values());

        if (!id.equals("0")) {
            Actor actor = actorRepository.findOne(id);
            model.addAttribute("actor", actor);
        }

        return "actor";
    }

    @RequestMapping(value = "/actors/{id}", method = RequestMethod.POST)
    public String actorPost(Model model, @PathVariable String id, @RequestParam String platform,
            @RequestParam String game, @RequestParam String name, @RequestParam String liveUrl,
            @RequestParam List<ActorTag> tags, RedirectAttributes ra) {

        Actor actor;
        if (!id.equals("0")) {
            actor = actorRepository.findOne(id);
        } else {
            actor = actorRepository.findByLiveUrl(liveUrl);
            if (actor == null) {
                actor = new Actor();
            }
        }
        actor.setPlatform(platformRepository.findByAbbr(platform));
        actor.setGame(gameRepository.findByAbbr(game));
        actor.setName(name);
        actor.setLiveUrl(liveUrl);
        actor.setTags(tags);
        
        actorRepository.save(actor);

        return "redirect:/actors";
    }
}
