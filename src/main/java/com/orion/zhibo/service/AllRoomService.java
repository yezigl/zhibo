/**
 * Copyright 2015 meituan.com. All Rights Reserved.
 */
package com.orion.zhibo.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.mongodb.morphia.query.Query;
import org.springframework.stereotype.Service;

import com.orion.zhibo.entity.Actor;
import com.orion.zhibo.entity.AllRoom;
import com.orion.zhibo.entity.Game;
import com.orion.zhibo.entity.Platform;

/**
 * description here
 *
 * @author lidehua
 * @since 2015年9月2日
 */
@Service
public class AllRoomService extends BasicService {

    public List<AllRoom> list(Game game, int offset, int limit, String keyword) {
        Query<AllRoom> query = allRoomDao.createQuery();
        Query<Actor> actorQuery = actorDao.createQuery();
        if (!Game.ALL.getAbbr().equals(game.getAbbr())) {
            actorQuery.field("game").equal(game);
        }
        List<Actor> actors = actorQuery.asList();
        if (actors.isEmpty()) {
            return new ArrayList<>();
        }
        query.field("actor").in(actors);
        if (StringUtils.isNotBlank(keyword)) {
            query.field("name").containsIgnoreCase(keyword);
        }
        query.order("-status, -number");
        query.offset(offset).limit(limit);
        return query.asList();
    }

    /**
     * @param p
     * @param g
     * @param uid
     */
    public AllRoom getByUid(Platform platform, Game game, String uid) {
        Query<AllRoom> query = allRoomDao.createQuery();
        query.field("platform").equal(platform);
        query.field("game").equal(game);
        query.field("uid").equal(uid);
        return query.get();
    }

    public List<AllRoom> listByPlatform(Platform platform) {
        Query<Actor> actorQuery = actorDao.createQuery();
        actorQuery.field("platform").equal(platform);
        List<Actor> actors = actorQuery.asList();
        Query<AllRoom> query = allRoomDao.createQuery();
        query.field("actor").in(actors);
        return query.asList();
    }

    /**
     * @param liveRoom
     */
    public String create(AllRoom liveRoom) {
        logger.info("create liveRoom {}", liveRoom);
        return allRoomDao.create(liveRoom);
    }

    /**
     * @param liveRoom
     */
    public boolean update(AllRoom liveRoom) {
        logger.info("update liveRoom {}", liveRoom);
        return allRoomDao.update(liveRoom);
    }

    /**
     * @param actor
     * @return
     */
    public AllRoom getByActor(Actor actor) {
        Query<AllRoom> query = allRoomDao.createQuery();
        query.field("actor").equal(actor);
        return query.get();
    }

    /**
     * @param id
     * @return
     */
    public AllRoom get(String id) {
        return allRoomDao.get(id);
    }

}
