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
import com.orion.zhibo.entity.Game;
import com.orion.zhibo.entity.LiveRoom;
import com.orion.zhibo.entity.Platform;
import com.orion.zhibo.model.ActorTag;
import com.orion.zhibo.model.LiveStatus;

/**
 * description here
 *
 * @author lidehua
 * @since 2015年9月2日
 */
@Service
public class LiveRoomService extends BasicService {

    public List<LiveRoom> list(Game game, ActorTag tag, int offset, int limit, String keyword) {
        Query<LiveRoom> query = liveRoomDao.createQuery();
        Query<Actor> actorQuery = actorDao.createQuery();
        if (!Game.ALL.getAbbr().equals(game.getAbbr())) {
            actorQuery.field("game").equal(game);
        }
        if (tag != null) {
            actorQuery.field("tags").hasThisOne(tag);
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
     * @return
     */
    public List<LiveRoom> listAllLiving() {
        Query<LiveRoom> query = liveRoomDao.createQuery();
        query.field("status").equal(LiveStatus.LIVING);
        return query.asList();
    }

    /**
     * @param p
     * @param g
     * @param uid
     */
    public LiveRoom getByUid(Platform platform, Game game, String uid) {
        Query<LiveRoom> query = liveRoomDao.createQuery();
        query.field("platform").equal(platform);
        query.field("game").equal(game);
        query.field("uid").equal(uid);
        return query.get();
    }

    public List<LiveRoom> listByPlatform(Platform platform) {
        Query<Actor> actorQuery = actorDao.createQuery();
        actorQuery.field("platform").equal(platform);
        List<Actor> actors = actorQuery.asList();
        Query<LiveRoom> query = liveRoomDao.createQuery();
        query.field("actor").in(actors);
        return query.asList();
    }

    /**
     * @param liveRoom
     */
    public String create(LiveRoom liveRoom) {
        logger.info("create liveRoom {}", liveRoom);
        return liveRoomDao.create(liveRoom);
    }

    /**
     * @param liveRoom
     */
    public boolean update(LiveRoom liveRoom) {
        logger.info("update liveRoom {}", liveRoom);
        return liveRoomDao.update(liveRoom);
    }

    /**
     * @param actor
     * @return
     */
    public LiveRoom getByActor(Actor actor) {
        Query<LiveRoom> query = liveRoomDao.createQuery();
        query.field("actor").equal(actor);
        return query.get();
    }

    /**
     * @param id
     * @return
     */
    public LiveRoom get(String id) {
        return liveRoomDao.get(id);
    }

}
