/**
 * Copyright 2015 meituan.com. All Rights Reserved.
 */
package com.orion.zhibo.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import com.orion.mongodb.entity.MongoEntity;

/**
 * description here
 *
 * @author lidehua
 * @since 2015年9月2日
 */
@Document(collection = "game")
public class Game extends MongoEntity {

    public static Game ALL = new Game("全部", "all");

    private String name; // 名字，如英雄联盟 DOTA2
    private String alias; // 别名 LOL 刀塔
    private String abbr; // 英雄联盟=lol DOTA2=dota2
    private String icon;
    private int order;

    /**
     * 
     */
    public Game() {
    }

    public Game(String name, String abbr) {
        this.name = name;
        this.abbr = abbr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getAbbr() {
        return abbr;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

}
