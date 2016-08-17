/**
 * Copyright 2015 meituan.com. All Rights Reserved.
 */
package com.orion.zhibo.dao;

import java.util.List;

import com.orion.mongodb.dao.MongoDao;
import com.orion.zhibo.entity.Platform;
import com.orion.zhibo.entity.PlatformGame;

/**
 * description here
 *
 * @author lidehua
 * @since 2015年9月2日
 */
public interface PlatformGameDao extends MongoDao<PlatformGame> {

    /**
     * @param platform
     * @return
     */
    List<PlatformGame> findByPlatform(Platform platform);

    
}
