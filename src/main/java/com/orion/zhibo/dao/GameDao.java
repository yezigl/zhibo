/**
 * Copyright 2015 meituan.com. All Rights Reserved.
 */
package com.orion.zhibo.dao;

import com.orion.mongodb.dao.MongoDao;
import com.orion.zhibo.entity.Game;

/**
 * description here
 *
 * @author lidehua
 * @since 2015年9月2日
 */
public interface GameDao extends MongoDao<Game> {

    /**
     * @return
     */
    Game findByAbbr(String abbr);

}
