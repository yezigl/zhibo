/**
 * Copyright 2015 meituan.com. All Rights Reserved.
 */
package com.orion.zhibo.dao;

import org.mongodb.morphia.Datastore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.orion.mongodb.dao.AbstractEntityDao;
import com.orion.zhibo.entity.Actor;

/**
 * description here
 *
 * @author lidehua
 * @since 2015年10月19日
 */
@Repository
public class ActorDao extends AbstractEntityDao<Actor> {

    /**
     * @param datastore
     */
    @Autowired
    public ActorDao(Datastore datastore) {
        super(datastore);
        // TODO Auto-generated constructor stub
    }

}
