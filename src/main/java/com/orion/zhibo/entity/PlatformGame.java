/**
 * Copyright 2015 meituan.com. All Rights Reserved.
 */
package com.orion.zhibo.entity;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Reference;

import com.orion.mongodb.entity.AbstractEntity;

/**
 * description here
 *
 * @author lidehua
 * @since 2015年9月8日
 */
@Entity("platformgame")
public class PlatformGame extends AbstractEntity {

    @Reference
    private Platform platform;
    @Reference
    private Game game;
    private String platformUrl; // 游戏在平台的地址

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public String getPlatformUrl() {
        return platformUrl;
    }

    public void setPlatformUrl(String platformUrl) {
        this.platformUrl = platformUrl;
    }

}
