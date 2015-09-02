/**
 * Copyright 2015 meituan.com. All Rights Reserved.
 */
package com.orion.zhibo.entity;

import org.mongodb.morphia.annotations.Entity;

import orion.mongodb.entity.AbstractEntity;

/**
 * description here
 *
 * @author lidehua
 * @since 2015年9月2日
 */
@Entity("game")
public class Game extends AbstractEntity {

    private String name;
    private String alias;
    private String abbr;
}
