/**
 * Copyright 2015 meituan.com. All Rights Reserved.
 */
package com.orion.zhibo.utils;

import org.apache.commons.lang.math.NumberUtils;

/**
 * description here
 *
 * @author yezi
 * @since 2015年9月4日
 */
public class Utils {
    
    private static final String WAN = "万";

    public static int parseViews(String views) {
        if (views.contains(WAN)) {
            return (int) (NumberUtils.toFloat(views.replace(WAN, "")) * 10000);
        }
        return NumberUtils.toInt(views.replace(",", ""));
    }
}
