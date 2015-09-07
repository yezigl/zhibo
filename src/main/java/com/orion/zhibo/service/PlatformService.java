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

    public List<Platform> getAll() {
        List<Platform> list = platformDao.getAll();
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(0, Platform.ALL);
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
        platformDao.create(platform);
    }
}
