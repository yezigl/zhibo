/**
 * Copyright 2015 meituan.com. All Rights Reserved.
 */
package com.orion.zhibo.model;

import java.util.ArrayList;
import java.util.List;

import com.orion.zhibo.entity.Game;

/**
 * description here
 *
 * @author yezi
 * @since 2015年9月4日
 */
public enum GameCate {

    ALL("全部", "all", ""), DOTA2("DOTA2", "dota2", "http://www.dota2.com/favicon.ico"), LOL("英雄联盟", "lol",
            "http://lol.qq.com/favicon.ico");

    String name;
    String abbr;
    String icon;

    /**
     * 
     */
    private GameCate(String name, String abbr, String icon) {
        this.name = name;
        this.abbr = abbr;
        this.icon = icon;
    }
    
    public String getIcon() {
        return icon;
    }

    public static List<Game> toList() {
        List<Game> list = new ArrayList<>();
        for (GameCate gc : values()) {
            list.add(new Game(gc.name, gc.abbr));
        }
        return list;
    }
}
