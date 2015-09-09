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

    ALL("全部", "all", ""), //
    LOL("英雄联盟", "lol", "http://lol.qq.com/favicon.ico"), //
    DOTA2("DOTA2", "dota2", "http://www.dota2.com/favicon.ico"), //
    HEARTHSTONE("炉石传说", "hearthstone", "http://hearthstone.nos.netease.com/3/common/hs.ico"), //
    TV("主机游戏", "tv", "https://www.playstation.com/favicon.ico"), //
    CF("穿越火线", "cf", "http://cf.qq.com/favicon.ico"),//
    STARSCRAFT("星际争霸", "sc", "http://us.battle.net/sc2/static/images/icons/favicon.ico"), //
    WOW("魔兽世界", "wow", "http://us.battle.net/wow/static/images/meta/favicon.ico?v=58-92"), //
    ;

    public String name;
    public String abbr;
    public String icon;

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
            Game game = new Game(gc.name, gc.abbr);
            game.setIcon(gc.icon);
            list.add(game);
        }
        return list;
    }

    /**
     * @param game
     * @return
     */
    public static GameCate valueOfAbbr(String abbr) {
        for (GameCate gc : values()) {
            if (gc.abbr.equals(abbr)) {
                return gc;
            }
        }
        return GameCate.ALL;
    }
}
