/**
 * Copyright 2015 meituan.com. All Rights Reserved.
 */
package com.orion.zhibo.dao;

import java.util.List;

import org.mongodb.morphia.Datastore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.orion.mongodb.dao.AbstractEntityDao;
import com.orion.zhibo.entity.Game;

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
    
    @Override
    public List<Game> getAll() {
        return this.createQuery().order("abbr").asList();
    }

}
