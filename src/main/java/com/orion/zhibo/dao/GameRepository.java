/**
 * Copyright 2015 meituan.com. All Rights Reserved.
 */
package com.orion.zhibo.dao;

import com.orion.mongodb.repository.OrionMongoRepository;
import com.orion.zhibo.entity.Game;

/**
 * description here
 *
 * @author lidehua
 * @since 2015年9月2日
 */
public interface GameRepository extends OrionMongoRepository<Game> {

    /**
     * @return
     */
    Game findByAbbr(String abbr);

}
