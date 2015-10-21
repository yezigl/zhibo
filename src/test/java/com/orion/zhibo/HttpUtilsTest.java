/**
 * Copyright 2015 meituan.com. All Rights Reserved.
 */
package com.orion.zhibo;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.orion.core.utils.HttpUtils;

/**
 * description here
 *
 * @author lidehua
 * @since 2015年10月21日
 */
public class HttpUtilsTest {

    @Test
    public void testGet() {
        String ret = HttpUtils.get("http://mb.tga.plu.cn/chatroom/joinroom?roomId=15556");
        System.out.println(JSON.parseObject(ret).getIntValue("online"));
    }
}
