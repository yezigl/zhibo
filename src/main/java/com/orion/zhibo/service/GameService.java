/**
 * Copyright 2015 meituan.com. All Rights Reserved.
 */
package com.orion.zhibo.service;

import java.util.List;

import org.springframework.data.domain.Sort;
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
        return gameDao.findByAbbr(abbr);
    }

    /**
     * @param game
     */
    public void create(Game game) {
        gameDao.save(game);
    }

    /**
     * @return
     */
    public List<Game> listAll() {
        return gameDao.findAll(new Sort("order"));
    }

    /**
     * @param id
     * @return
     */
    public Object get(String id) {
        return gameDao.findOne(id);
    }

    /**
     * @param game
     */
    public void upsert(Game game) {
        gameDao.save(game);
    }

}
