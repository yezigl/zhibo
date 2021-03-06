/**
 * Copyright 2015 meituan.com. All Rights Reserved.
 */
package com.orion.zhibo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import com.orion.core.utils.Utils;
import com.orion.zhibo.entity.LiveRoom;

/**
 * description here
 *
 * @author lidehua
 * @since 2015年9月2日
 */
@Controller
public class LiveController extends BasicController {

    @RequestMapping("/live/{id}")
    public String liveroom(@PathVariable String id,
            @RequestHeader(value = "User-Agent", required = false) String userAgent, Model model) {
        LiveRoom liveRoom = liveRoomRepository.findOne(id);
        model.addAttribute("liveRoom", liveRoom);
        model.addAttribute("isMobile", Utils.isMobile(userAgent));
        model.addAttribute("path", "/");
        return "liveroom";
    }

    @RequestMapping("/live/{platform}/{game}/{uid}")
    public String liveroomall(@PathVariable String platform, @PathVariable String game, @PathVariable String uid,
            @RequestHeader(value = "User-Agent", required = false) String userAgent, Model model) {
        LiveRoom liveRoom = liveRoomRepository.findByPlatformAndGameAndUid(platformRepository.findByAbbr(platform),
                gameRepository.findByAbbr(game), uid);
        model.addAttribute("liveRoom", liveRoom);
        model.addAttribute("isMobile", Utils.isMobile(userAgent));
        model.addAttribute("path", "/");
        return "liveroom";
    }
}
