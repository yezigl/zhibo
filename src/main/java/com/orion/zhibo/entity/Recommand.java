/**
 * Copyright 2015 meituan.com. All Rights Reserved.
 */
package com.orion.zhibo.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;
import org.mongodb.morphia.annotations.Reference;
import org.mongodb.morphia.annotations.Transient;

import com.orion.mongodb.entity.AbstractEntity;
import com.orion.zhibo.model.LiveStatus;

/**
 * description here
 *
 * @author lidehua
 * @since 2015年10月19日
 */
@Entity("recommand")
@Indexes({ @Index(fields = { @Field("actor") }) })
public class Recommand extends AbstractEntity {

    @Reference
    private Actor actor;
    private String uid; // 直播id
    private String name; // 直播名字
    private String avatar; // 头像
    private String roomId; // 直播室id
    private String liveId; // 直播id，可能和roomId不一致
    private String title; // 直播室名字
    private String description;
    private String thumbnail;
    private String views;
    private int number;
    private int status;
    @Transient
    private String shareUrl;
    private String flashUrl;
    
    public boolean isLiving() {
        return this.status == LiveStatus.LIVING;
    }

    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getLiveId() {
        return liveId;
    }

    public void setLiveId(String liveId) {
        this.liveId = liveId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title == null || title.trim().length() == 0) {
            return;
        }
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getFlashUrl() {
        return flashUrl;
    }

    public void setFlashUrl(String flashUrl) {
        this.flashUrl = flashUrl;
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE);
        builder.append("actor", actor.getName());
        builder.append("uid", uid);
        builder.append("name", name);
        builder.append("avatar", avatar);
        builder.append("roomId", roomId);
        builder.append("liveId", liveId);
        builder.append("title", title);
        builder.append("thumbnail", thumbnail);
        builder.append("views", views);
        builder.append("number", number);
        builder.append("status", status);
        builder.append("flashUrl", flashUrl);
        return builder.toString();
    }

}
