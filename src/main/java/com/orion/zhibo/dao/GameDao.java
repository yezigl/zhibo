/**
 * Copyright 2015 meituan.com. All Rights Reserved.
 */
package com.orion.zhibo.dao;

import java.util.List;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.orion.zhibo.entity.Game;
import com.orion.zhibo.entity.Platform;

import orion.mongodb.dao.AbstractEntityDao;

/**
 * description here
 *
 * @author lidehua
 * @since 2015年9月2日
 */
@Repository
public class GameDao extends AbstractEntityDao<Game> {

    /**
     * @param datastore
     */
    @Autowired
    public GameDao(Datastore datastore) {
        super(datastore);
    }
    
    public List<Game> listByPlatform(Platform platform) {
        Query<Game> query = createQuery();
        query.field("platform").equal(platform);
        return query.asList();
    }

}
