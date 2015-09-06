/**
 * Copyright 2015 meituan.com. All Rights Reserved.
 */
package com.orion.zhibo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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
public class LiveController extends BasicController {

    @RequestMapping("/live/{platform}/{game}/{uid}")
    public String liveroom(@PathVariable String platform, @PathVariable String game, @PathVariable String uid,
            Model model) {
        Platform p = platformService.getByAbbr(platform);
        Game g = gameService.getByAbbr(game);
        LiveRoom liveRoom = liveRoomService.getByUid(p, g, uid);
        model.addAttribute("liveRoom", liveRoom);
        return "liveroom";
    }

}
