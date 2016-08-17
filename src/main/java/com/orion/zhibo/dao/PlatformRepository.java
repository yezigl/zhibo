/**
 * Copyright 2015 meituan.com. All Rights Reserved.
 */
package com.orion.zhibo.dao;

import com.orion.mongodb.repository.OrionMongoRepository;
import com.orion.zhibo.entity.Platform;

/**
 * description here
 *
 * @author lidehua
 * @since 2015年9月2日
 */
public interface PlatformRepository extends OrionMongoRepository<Platform> {

    /**
     * @param abbr 
     * @return
     */
    Platform findByAbbr(String abbr);

}
