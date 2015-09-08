/**
 * Copyright 2015 meituan.com. All Rights Reserved.
 */
package com.orion.zhibo.dao;

import java.util.List;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.orion.mongodb.dao.AbstractEntityDao;
import com.orion.zhibo.entity.Platform;
import com.orion.zhibo.entity.PlatformGame;

/**
 * description here
 *
 * @author lidehua
 * @since 2015年9月2日
 */
@Repository
public class PlatformGameDao extends AbstractEntityDao<PlatformGame> {

    /**
     * @param datastore
     */
    @Autowired
    public PlatformGameDao(Datastore datastore) {
        super(datastore);
    }
    
    public List<PlatformGame> listByPlatform(Platform platform) {
        Query<PlatformGame> query = createQuery();
        query.field("platform").equal(platform);
        return query.asList();
    }

}
