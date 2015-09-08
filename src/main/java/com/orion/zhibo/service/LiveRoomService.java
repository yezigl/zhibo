/**
 * Copyright 2015 meituan.com. All Rights Reserved.
 */
package com.orion.zhibo.service;

import java.util.List;

import org.mongodb.morphia.query.Query;
import org.springframework.stereotype.Service;

import com.orion.zhibo.entity.Game;
import com.orion.zhibo.entity.LiveRoom;
import com.orion.zhibo.entity.Platform;
import com.orion.zhibo.model.LiveStatus;

/**
 * description here
 *
 * @author lidehua
 * @since 2015年9月2日
 */
@Service
public class LiveRoomService extends BasicService {

    public List<LiveRoom> listLiving(Platform platform, Game game, int offset, int limit) {
        Query<LiveRoom> query = liveRoomDao.createQuery();
        if (!Platform.ALL.getAbbr().equals(platform.getAbbr())) {
            query.field("platform").equal(platform);
        }
        if (!Game.ALL.getAbbr().equals(game.getAbbr())) {
            query.field("game").equal(game);
        }
        query.order("-number");
        query.field("status").equal(LiveStatus.LIVING);
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

}
