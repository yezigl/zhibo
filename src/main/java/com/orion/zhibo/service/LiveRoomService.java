/**
 * Copyright 2015 meituan.com. All Rights Reserved.
 */
package com.orion.zhibo.service;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
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

    public List<LiveRoom> list(Game game, int offset, int limit, String keyword) {
        Pageable pageable = new PageRequest(offset / limit, limit, new Sort(Direction.DESC, "status", "number"));
        if (!Game.ALL.getAbbr().equals(game.getAbbr())) {
            liveRoomDao.findByGame(game, pageable).getContent();
        }
        return liveRoomDao.findAll(pageable).getContent();
    }

    /**
     * @param p
     * @param g
     * @param uid
     */
    public LiveRoom getByUid(Platform platform, Game game, String uid) {
        return liveRoomDao.findByPlatformAndGameAndUid(platform, game, uid);
    }

    /**
     * @param liveRoom
     */
    public LiveRoom create(LiveRoom liveRoom) {
        logger.info("create liveRoom {}", liveRoom);
        return liveRoomDao.save(liveRoom);
    }

    /**
     * @param liveRoom
     */
    public LiveRoom update(LiveRoom liveRoom) {
        logger.info("update liveRoom {}", liveRoom);
        return liveRoomDao.save(liveRoom);
    }

    /**
     * @param id
     * @return
     */
    public LiveRoom get(String id) {
        return liveRoomDao.findOne(id);
    }

    public List<LiveRoom> listAllLiving(Platform platform) {
        return liveRoomDao.findByPlatformAndStatus(platform, LiveStatus.LIVING);
    }

    /**
     * @param liveUrl
     * @return
     */
    public LiveRoom getByUrl(String liveUrl) {
        return liveRoomDao.findByLiveUrl(liveUrl);
    }

    /**
     * @param liveRoom
     */
    public void delete(LiveRoom liveRoom) {
        liveRoomDao.delete(liveRoom);
    }
}
