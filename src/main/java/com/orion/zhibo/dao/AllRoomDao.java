/**
 * Copyright 2015 meituan.com. All Rights Reserved.
 */
package com.orion.zhibo.dao;

import org.mongodb.morphia.Datastore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.orion.mongodb.dao.AbstractEntityDao;
import com.orion.zhibo.entity.AllRoom;

/**
 * description here
 *
 * @author lidehua
 * @since 2015年9月2日
 */
@Repository
public class AllRoomDao extends AbstractEntityDao<AllRoom> {

    /**
     * @param datastore
     */
    @Autowired
    public AllRoomDao(Datastore datastore) {
        super(datastore);
    }
}
