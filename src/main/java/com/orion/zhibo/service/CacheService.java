/**
 * Copyright 2015 meituan.com. All Rights Reserved.
 */
package com.orion.zhibo.service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * description here
 *
 * @author lidehua
 * @since 2015年11月3日
 */
@Service
public class CacheService extends BasicService {

    @Autowired
    StringRedisTemplate redisTemplate;
    
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }
    
    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }
    
    public void set(String key, String value, long expire) {
        redisTemplate.opsForValue().set(key, value, expire, TimeUnit.MILLISECONDS);
    }
}
