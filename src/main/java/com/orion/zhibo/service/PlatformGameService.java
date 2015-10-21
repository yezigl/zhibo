/**
 * Copyright 2015 meituan.com. All Rights Reserved.
 */
package com.orion.zhibo.service;

import java.util.List;

import org.mongodb.morphia.query.Query;
import org.springframework.stereotype.Service;

import com.orion.zhibo.entity.Platform;
import com.orion.zhibo.entity.PlatformGame;

/**
 * description here
 *
 * @author lidehua
 * @since 2015年9月2日
 */
@Service
public class PlatformGameService extends BasicService {

    /**
     * @param game
     * @return
     */
    public List<PlatformGame> listByPlatform(Platform platform) {
        Query<PlatformGame> query = platformGameDao.createQuery();
        query.field("platform").equal(platform);
        return query.asList();
    }

    /**
     * @param game
     */
    public void create(PlatformGame pg) {
        logger.info("create platformGame {}", pg);
        platformGameDao.create(pg);
    }

    /**
     * @return
     */
    public Object getAll() {
        return platformGameDao.getAll();
    }

}
