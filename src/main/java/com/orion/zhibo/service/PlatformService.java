/**
 * Copyright 2015 meituan.com. All Rights Reserved.
 */
package com.orion.zhibo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.orion.zhibo.entity.Platform;

/**
 * description here
 *
 * @author lidehua
 * @since 2015年9月2日
 */
@Service
public class PlatformService extends BasicService {

    public List<Platform> getAll() {
        List<Platform> list = platformDao.getAll();
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(Platform.ALL);
        return list;
    }
}
