/**
 * Copyright 2015 meituan.com. All Rights Reserved.
 */
package com.orion.zhibo.service;

import java.util.List;

import org.mongodb.morphia.query.Query;
import org.springframework.stereotype.Service;

import com.orion.zhibo.entity.Actor;
import com.orion.zhibo.entity.Platform;

/**
 * description here
 *
 * @author lidehua
 * @since 2015年10月19日
 */
@Service
public class ActorService extends BasicService {

    /**
     * @param id
     * @return
     */
    public Actor get(String id) {
        return actorDao.get(id);
    }

    public String create(Actor actor) {
        logger.info("create actor {}", actor);
        return actorDao.create(actor);
    }

    public boolean update(Actor actor) {
        logger.info("update actor {}", actor);
        return actorDao.update(actor);
    }

    public void upsert(Actor actor) {
        if (actor.getId() == null) {
            create(actor);
        } else {
            update(actor);
        }
    }

    /**
     * @return
     */
    public List<Actor> listAll() {
        Query<Actor> query = actorDao.createQuery();
        query.order("-utime");
        return query.asList();
    }

    /**
     * @param platform
     * @return
     */
    public List<Actor> listByPlatform(Platform platform) {
        Query<Actor> query = actorDao.createQuery();
        query.field("platform").equal(platform);
        return query.asList();
    }

    /**
     * @param liveUrl
     * @return
     */
    public Actor getByUrl(String liveUrl) {
        Query<Actor> query = actorDao.createQuery();
        query.field("liveUrl").equal(liveUrl);
        return query.get();
    }
}
