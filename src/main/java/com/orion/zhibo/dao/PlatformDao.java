/**
 * Copyright 2015 meituan.com. All Rights Reserved.
 */
package com.orion.zhibo.dao;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.orion.mongodb.dao.AbstractEntityDao;
import com.orion.zhibo.entity.Platform;

/**
 * description here
 *
 * @author lidehua
 * @since 2015年9月2日
 */
@Repository
public class PlatformDao extends AbstractEntityDao<Platform> {

    /**
     * @param datastore
     */
    @Autowired
    public PlatformDao(Datastore datastore) {
        super(datastore);
    }

    /**
     * @param string
     * @return
     */
    public Platform getByAbbr(String abbr) {
        Query<Platform> query = createQuery();
        query.field("abbr").equal(abbr);
        return query.get();
    }

}
