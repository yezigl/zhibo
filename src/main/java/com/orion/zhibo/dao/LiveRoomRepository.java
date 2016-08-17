/**
 * Copyright 2015 meituan.com. All Rights Reserved.
 */
package com.orion.zhibo.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.orion.mongodb.repository.OrionMongoRepository;
import com.orion.zhibo.entity.Game;
import com.orion.zhibo.entity.LiveRoom;
import com.orion.zhibo.entity.Platform;

/**
 * description here
 *
 * @author lidehua
 * @since 2015年9月2日
 */
public interface LiveRoomRepository extends OrionMongoRepository<LiveRoom> {

    /**
     * @param game
     * @param pageable
     */
    List<LiveRoom> findByGame(Game game, Pageable pageable);

    /**
     * @param platform
     * @param game
     * @param uid
     * @return
     */
    LiveRoom findByPlatformAndGameAndUid(Platform platform, Game game, String uid);

    /**
     * @param platform
     * @param living
     * @return
     */
    List<LiveRoom> findByPlatformAndStatus(Platform platform, int status);

    /**
     * @param liveUrl
     * @return
     */
    LiveRoom findByLiveUrl(String liveUrl);

    /**
     * @param name
     * @param pageable
     * @return
     */
    List<LiveRoom> findByNameContaining(String name, Pageable pageable);

}
