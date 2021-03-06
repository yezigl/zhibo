/**
 * Copyright 2015 meituan.com. All Rights Reserved.
 */
package com.orion.zhibo.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.data.mongodb.core.mapping.Document;

import com.orion.mongodb.entity.MongoEntity;

/**
 * description here
 *
 * @author lidehua
 * @since 2015年9月2日
 */
@Document(collection = "platform")
public class Platform extends MongoEntity {

    public static Platform ALL = new Platform("全部", "all");

    private String name;
    private String abbr;
    private String url;
    private String host;
    private String icon;
    private String logo;
    private String sharePattern;
    private boolean linkProtect;
    private boolean iframe;
    private boolean fetch;

    /**
     * 
     */
    public Platform() {
        super();
    }

    public Platform(String name, String abbr) {
        super();
        this.name = name;
        this.abbr = abbr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbr() {
        return abbr;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getSharePattern() {
        return sharePattern;
    }

    public void setSharePattern(String sharePattern) {
        this.sharePattern = sharePattern;
    }

    public boolean isLinkProtect() {
        return linkProtect;
    }

    public void setLinkProtect(boolean linkProtect) {
        this.linkProtect = linkProtect;
    }

    public boolean isIframe() {
        return iframe;
    }

    public void setIframe(boolean isIframe) {
        this.iframe = isIframe;
    }

    public boolean isFetch() {
        return fetch;
    }

    public void setFetch(boolean fetch) {
        this.fetch = fetch;
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE);
        builder.append("name", name);
        builder.append("abbr", abbr);
        builder.append("url", url);
        builder.append("host", host);
        builder.append("icon", icon);
        builder.append("logo", logo);
        builder.append("sharePattern", sharePattern);
        builder.append("linkProtect", linkProtect);
        builder.append("isIframe", iframe);
        builder.append("fetch", fetch);
        return builder.toString();
    }

}
