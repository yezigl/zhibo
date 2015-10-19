/**
 * Copyright 2015 meituan.com. All Rights Reserved.
 */
package com.orion.zhibo.entity;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Reference;

import com.orion.mongodb.entity.AbstractEntity;
import com.orion.zhibo.model.ActorTag;

/**
 * description here
 *
 * @author lidehua
 * @since 2015年10月19日
 */
@Entity("actor")
public class Actor extends AbstractEntity {

    @Reference
    private Platform platform;
    @Reference
    private Game game;
    private List<ActorTag> tags;
    private String name;
    private String liveUrl;

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

    public List<ActorTag> getTags() {
        return tags;
    }

    public void setTags(List<ActorTag> tags) {
        this.tags = tags;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLiveUrl() {
        return liveUrl;
    }

    public void setLiveUrl(String liveUrl) {
        this.liveUrl = liveUrl;
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE);
        builder.append("platform", platform.getName());
        builder.append("game", game.getName());
        builder.append("tags", tags);
        builder.append("name", name);
        builder.append("liveUrl", liveUrl);
        return builder.toString();
    }

}
