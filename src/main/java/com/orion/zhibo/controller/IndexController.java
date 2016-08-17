/**
 * Copyright 2015 meituan.com. All Rights Reserved.
 */
package com.orion.zhibo.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.orion.core.utils.HttpUtils;
import com.orion.zhibo.entity.LiveRoom;
import com.orion.zhibo.utils.Pagination;

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
        setUpModel(model, "all", 0, 20, q);
        return "index";
    }
    
    @RequestMapping(value = "/{abbr}", method = RequestMethod.GET)
    public String games(@PathVariable String abbr, Model model) {
        setUpModel(model, abbr, 0, Pagination.PAGE_SIZE);
        return "index";
    }

    @RequestMapping(value = "/{abbr}/{offset}", method = RequestMethod.GET)
    public String games(@PathVariable String abbr, @PathVariable int offset, Model model) {
        setUpModel(model, abbr, offset, Pagination.PAGE_SIZE);
        return "index";
    }

    private void setUpModel(Model model, String abbr, int offset, int limit) {
        setUpModel(model, abbr, offset, limit, null);
    }

    private void setUpModel(Model model, String abbr, int offset, int limit, String keyword) {
        model.addAttribute("uri", "/" + abbr);
        model.addAttribute("offset", offset);
        model.addAttribute("limit", limit);
        model.addAttribute("q", keyword);
        List<LiveRoom> list;
        Pageable pageable = new PageRequest(offset / limit, limit, new Sort(Direction.DESC, "status", "number"));
        if (abbr.equals("all")) {
            if (keyword != null) {
                list = liveRoomRepository.findByNameContaining(keyword, pageable);
            } else {
                list = liveRoomRepository.findAll(pageable).getContent();
            }
        } else {
            list = liveRoomRepository.findByGame(gameRepository.findByAbbr(abbr), pageable);
        }
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
            logger.error(e.getMessage(), e);
        }
    }

}
