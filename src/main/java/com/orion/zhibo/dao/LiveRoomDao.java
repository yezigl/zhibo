/**
 * Copyright 2015 meituan.com. All Rights Reserved.
 */
package com.orion.zhibo.dao;

import org.mongodb.morphia.Datastore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.orion.zhibo.entity.LiveRoom;

import orion.mongodb.dao.AbstractEntityDao;

/**
 * description here
 *
 * @author lidehua
 * @since 2015年9月2日
 */
@Repository
public class LiveRoomDao extends AbstractEntityDao<LiveRoom> {

    /**
     * @param datastore
     */
    @Autowired
    public LiveRoomDao(Datastore datastore) {
        super(datastore);
    }

}
