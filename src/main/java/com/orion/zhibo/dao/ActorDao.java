/**
 * Copyright 2015 meituan.com. All Rights Reserved.
 */
package com.orion.zhibo.dao;

import java.util.List;

import com.orion.mongodb.dao.MongoDao;
import com.orion.zhibo.entity.Actor;
import com.orion.zhibo.entity.Platform;

/**
 * description here
 *
 * @author lidehua
 * @since 2015年10月19日
 */
public interface ActorDao extends MongoDao<Actor> {

    /**
     * @return
     */
    List<Actor> findByPlatform(Platform platform);

    /**
     * @return
     */
    Actor findByLiveUrl(String liveUrl);

}
