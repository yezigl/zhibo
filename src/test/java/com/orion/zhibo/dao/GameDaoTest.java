/**
 * Copyright 2015 meituan.com. All Rights Reserved.
 */
package com.orion.zhibo.dao;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.orion.zhibo.TestConfig;
import com.orion.zhibo.dao.GameDao;
import com.orion.zhibo.entity.Game;
import com.orion.zhibo.model.GameCate;

/**
 * description here
 *
 * @author yezi
 * @since 2015年9月3日
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class GameDaoTest {
    
    @Autowired
    GameDao gameDao;
    @Autowired
    PlatformDao platformDao;

    @Test
    public void testCreate() {
        Game game = new Game();
        game.setName("炉石传说");
        game.setAlias("HearthStone");
        game.setAbbr("hearthstone");
        game.setIcon(GameCate.HEARTHSTONE.getIcon());
        game = gameDao.save(game);
        System.out.println(game.getId());
    }

    @Test
    public void testDelete() {
        gameDao.delete("55f0f60e3887bf3c688c3d0b");
    }
    
    @Test
    public void testUpdate() {
        List<Game> list = gameDao.findAll();
        for (Game game : list) {
            GameCate gc = GameCate.valueOfAbbr(game.getAbbr());
            game.setOrder(gc.ordinal());
            gameDao.save(game);
        }
    }
}
