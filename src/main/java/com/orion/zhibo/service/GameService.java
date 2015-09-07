/**
 * Copyright 2015 meituan.com. All Rights Reserved.
 */
package com.orion.zhibo.service;

import org.mongodb.morphia.query.Query;
import org.springframework.stereotype.Service;

import com.orion.zhibo.entity.Game;

/**
 * description here
 *
 * @author lidehua
 * @since 2015年9月2日
 */
@Service
public class GameService extends BasicService {

    /**
     * @param game
     * @return
     */
    public Game getByAbbr(String abbr) {
        Query<Game> query = gameDao.createQuery();
        query.field("abbr").equal(abbr);
        return query.get();
    }

    /**
     * @param game
     */
    public void create(Game game) {
        gameDao.create(game);
    }

}
