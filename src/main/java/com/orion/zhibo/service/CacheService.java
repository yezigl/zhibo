/**
 * Copyright 2015 meituan.com. All Rights Reserved.
 */
package com.orion.zhibo.service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.collections4.map.LRUMap;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * description here
 *
 * @author lidehua
 * @since 2015年11月3日
 */
@Service
public class CacheService extends BasicService implements InitializingBean, DisposableBean {

    LRUMap<String, String> cache = new LRUMap<>(10000);

    @Value("${logging.path}")
    String filePath;

    public String get(String key) {
        return cache.get(key);
    }

    public void set(String key, String value) {
        cache.put(key, value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filePath + "/cache"));
            LRUMap<String, String> temp = (LRUMap<String, String>) inputStream.readObject();
            inputStream.close();
            if (temp != null) {
                cache = temp;
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void destroy() throws Exception {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filePath + "/cache"));
            outputStream.writeObject(cache);
            outputStream.close();
        } catch (Exception e) {
        }
    }
}
