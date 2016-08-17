/**
 * Copyright 2015 meituan.com. All Rights Reserved.
 */
package com.orion.zhibo.service;

import java.util.ArrayList;
import java.util.List;

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
        List<Platform> list = platformDao.findAll();
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }

    public Platform getByAbbr(String abbr) {
        return platformDao.findByAbbr(abbr);
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
            platformDao.save(p);
        } else {
            platformDao.save(platform);
        }
    }

    /**
     * @param id
     * @return
     */
    public Platform get(String id) {
        return platformDao.findOne(id);
    }

    public void delete(Platform platform) {
        platformDao.delete(platform);
    }

    /**
     * @param platform
     */
    public void upsert(Platform platform) {
        platformDao.save(platform);
        logger.info("upsert platform {}", platform);
    }
}
