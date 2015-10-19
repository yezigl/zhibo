/**
 * Copyright 2015 meituan.com. All Rights Reserved.
 */
package com.orion.zhibo.model;

/**
 * description here
 *
 * @author lidehua
 * @since 2015年10月19日
 */
public enum ActorTag {

    PRO("职业选手"),
    PASSER("路人大神"),
    GIRL("妹子");
    
    public String title;
    
    /**
     * 
     */
    private ActorTag(String title) {
        this.title = title;
    }
    
    public String getTitle() {
        return title;
    }
}
