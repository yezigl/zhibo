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
        game.setPlatformUrl("http://www.douyutv.com/directory/game/How");
        game.setPlatform(platformDao.getByAbbr("douyu"));
        game.setIcon(GameCate.HEARTHSTONE.getIcon());
        String id = gameDao.create(game);
        System.out.println(id);
    }

    @Test
    public void testDelete() {
        gameDao.delete("55e8519c0e37de20a10542b3");
    }
    
    @Test
    public void testUpdate() {
        Game dota2 = gameDao.get("55e8606e0e37de20d263fa9d");
        dota2.setIcon(GameCate.DOTA2.getIcon());
        gameDao.update(dota2);
        Game lol = gameDao.get("55eb90440e37de26a24bd9be");
        lol.setIcon(GameCate.LOL.getIcon());
        gameDao.update(lol);
    }
}
