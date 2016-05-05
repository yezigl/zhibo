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
import com.orion.zhibo.entity.Recommand;
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
public class RecommandService extends BasicService {

    public List<Recommand> list(Game game, ActorTag tag, int offset, int limit, String keyword) {
        Query<Recommand> query = recommandDao.createQuery();
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
            query.or(query.criteria("name").containsIgnoreCase(keyword),
                    query.criteria("title").containsIgnoreCase(keyword));

        }
        query.order("-status, -number");
        query.offset(offset).limit(limit);
        return query.asList();
    }

    /**
     * @return
     */
    public List<Recommand> listAllLiving() {
        Query<Recommand> query = recommandDao.createQuery();
        query.field("status").equal(LiveStatus.LIVING);
        return query.asList();
    }

    /**
     * @param p
     * @param g
     * @param uid
     */
    public Recommand getByUid(Platform platform, Game game, String uid) {
        Query<Recommand> query = recommandDao.createQuery();
        query.field("platform").equal(platform);
        query.field("game").equal(game);
        query.field("uid").equal(uid);
        return query.get();
    }

    public List<Recommand> listByPlatform(Platform platform) {
        Query<Actor> actorQuery = actorDao.createQuery();
        actorQuery.field("platform").equal(platform);
        List<Actor> actors = actorQuery.asList();
        Query<Recommand> query = recommandDao.createQuery();
        query.field("actor").in(actors);
        return query.asList();
    }

    /**
     * @param liveRoom
     */
    public String create(Recommand liveRoom) {
        logger.info("create liveRoom {}", liveRoom);
        return recommandDao.create(liveRoom);
    }

    /**
     * @param liveRoom
     */
    public boolean update(Recommand liveRoom) {
        logger.info("update liveRoom {}", liveRoom);
        return recommandDao.update(liveRoom);
    }

    /**
     * @param actor
     * @return
     */
    public Recommand getByActor(Actor actor) {
        Query<Recommand> query = recommandDao.createQuery();
        query.field("actor").equal(actor);
        return query.get();
    }

    /**
     * @param id
     * @return
     */
    public Recommand get(String id) {
        return recommandDao.get(id);
    }

}
