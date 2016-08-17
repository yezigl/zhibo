/**
 * Copyright 2015 meituan.com. All Rights Reserved.
 */
package com.orion.zhibo.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
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
        return actorDao.findOne(id);
    }

    public Actor create(Actor actor) {
        logger.info("create actor {}", actor);
        return actorDao.save(actor);
    }

    public Actor update(Actor actor) {
        logger.info("update actor {}", actor);
        return actorDao.save(actor);
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
        return actorDao.findAll(new Sort(Direction.DESC, "utime"));
    }

    /**
     * @param platform
     * @return
     */
    public List<Actor> listByPlatform(Platform platform) {
        return actorDao.findByPlatform(platform);
    }

    /**
     * @param liveUrl
     * @return
     */
    public Actor getByUrl(String liveUrl) {
        return actorDao.findByLiveUrl(liveUrl);
    }
}
