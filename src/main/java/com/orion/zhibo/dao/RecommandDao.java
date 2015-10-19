/**
 * Copyright 2015 meituan.com. All Rights Reserved.
 */
package com.orion.zhibo.dao;

import org.mongodb.morphia.Datastore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.orion.mongodb.dao.AbstractEntityDao;
import com.orion.zhibo.entity.Recommand;

/**
 * description here
 *
 * @author lidehua
 * @since 2015年10月19日
 */
@Repository
public class RecommandDao extends AbstractEntityDao<Recommand> {

    /**
     * @param datastore
     */
    @Autowired
    public RecommandDao(Datastore datastore) {
        super(datastore);
    }

}
