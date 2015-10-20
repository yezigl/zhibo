/**
 * Copyright 2015 meituan.com. All Rights Reserved.
 */
package com.orion.zhibo.spider;

import com.orion.zhibo.entity.Actor;

/**
 * description here
 *
 * @author lidehua
 * @since 2015年9月1日
 */
public interface Spider {

    /**
     * 启动spider
     */
    void run();
    
    /**
     * 解析一个页面
     */
    void parse(Actor actor);
}
