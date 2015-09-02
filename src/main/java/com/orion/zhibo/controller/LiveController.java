/**
 * Copyright 2015 meituan.com. All Rights Reserved.
 */
package com.orion.zhibo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * description here
 *
 * @author lidehua
 * @since 2015年9月2日
 */
@Controller
public class LiveController extends BasicController {

    @RequestMapping("/live/{platform}/{id}")
    public String liveroom(@PathVariable String platform, @PathVariable String id, Model model) {
        
        return "liveroom";
    }
    
}
