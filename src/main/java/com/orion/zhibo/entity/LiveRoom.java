/**
 * Copyright 2015 meituan.com. All Rights Reserved.
 */
package com.orion.zhibo.entity;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;
import org.mongodb.morphia.annotations.Reference;

import orion.mongodb.entity.AbstractEntity;

/**
 * description here
 *
 * @author lidehua
 * @since 2015年9月2日
 */
@Entity("liveroom")
@Indexes({ @Index(fields = { @Field("platform"), @Field("identify") }) })
public class LiveRoom extends AbstractEntity {

    @Reference
    private Platform platform;
    private String identify;
    private String name;
    private String title;
    private String url;
    private String thumbnail;
    private int number;
    private int maxNumber;

}
