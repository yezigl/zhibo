/**
 * Copyright 2015 meituan.com. All Rights Reserved.
 */
package com.orion.zhibo.service;

import java.util.List;

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
        return platformGameDao.findByPlatform(platform);
    }

    /**
     * @param game
     */
    public void create(PlatformGame pg) {
        logger.info("create platformGame {}", pg);
        platformGameDao.save(pg);
    }

    /**
     * @return
     */
    public List<PlatformGame> listAll() {
        return platformGameDao.findAll();
    }

    /**
     * @param id
     * @return
     */
    public PlatformGame get(String id) {
        return platformGameDao.findOne(id);
    }

    /**
     * @param pg
     */
    public void update(PlatformGame pg) {
        platformGameDao.save(pg);
    }

}
