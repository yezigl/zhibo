/**
 * Copyright 2015 meituan.com. All Rights Reserved.
 */
package com.orion.zhibo.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.orion.zhibo.TestConfig;
import com.orion.zhibo.dao.GameDao;
import com.orion.zhibo.entity.Game;

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
        game.setName("DOTA2");
        game.setAlias("DotA2");
        game.setAbbr("dota2");
        game.setPlatformUrl("http://www.douyutv.com/directory/game/DOTA2");
        game.setPlatform(platformDao.getByAbbr("douyu"));
        String id = gameDao.create(game);
        System.out.println(id);
    }

    @Test
    public void testDelete() {
        gameDao.delete("55e8519c0e37de20a10542b3");
    }
}
