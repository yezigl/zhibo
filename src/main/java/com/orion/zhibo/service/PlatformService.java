/**
 * Copyright 2015 meituan.com. All Rights Reserved.
 */
package com.orion.zhibo.service;

import java.util.ArrayList;
import java.util.List;

import org.mongodb.morphia.query.Query;
import org.springframework.stereotype.Service;

import com.orion.zhibo.entity.Platform;

/**
 * description here
 *
 * @author lidehua
 * @since 2015年9月2日
 */
@Service
public class PlatformService extends BasicService {

    public List<Platform> listAll() {
        List<Platform> list = platformDao.getAll();
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }

    public Platform getByAbbr(String abbr) {
        Query<Platform> query = platformDao.createQuery();
        query.field("abbr").equal(abbr);
        return query.get();
    }

    /**
     * @param platform
     */
    public void create(Platform platform) {
        logger.info("create platform {}", platform);
        Platform p = getByAbbr(platform.getAbbr());
        if (p != null) {
            p.setName(platform.getName());
            p.setHost(platform.getHost());
            p.setIcon(platform.getIcon());
            p.setLinkProtect(platform.isLinkProtect());
            p.setLogo(platform.getLogo());
            p.setSharePattern(platform.getSharePattern());
            p.setUrl(platform.getUrl());
            platformDao.update(p);
        } else {
            platformDao.create(platform);
        }
    }

    /**
     * @param id
     * @return
     */
    public Platform get(String id) {
        return platformDao.get(id);
    }
    
    public void delete(Platform platform) {
        platformDao.delete(platform);
    }

    /**
     * @param platform
     */
    public void upsert(Platform platform) {
        if (platform.getId() != null) {
            platformDao.update(platform);
        } else {
            platformDao.save(platform);
        }
        logger.info("upsert platform {}", platform);
    }
}
