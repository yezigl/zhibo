/**
 * Copyright 2015 meituan.com. All Rights Reserved.
 */
package com.orion.zhibo.service;

import java.util.List;

import org.mongodb.morphia.query.Query;
import org.springframework.stereotype.Service;

import com.orion.zhibo.entity.Actor;

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
        return actorDao.create(actor);
    }

    public boolean update(Actor actor) {
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
}
