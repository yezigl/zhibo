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
    private static final String GE = "个观众";

    public static int parseViews(String views) {
        if (views.contains(WAN)) {
            return (int) (NumberUtils.toFloat(views.replace(WAN, "")) * 10000);
        }
        if (views.contains(GE)) {
            return NumberUtils.toInt(views.replace(GE, ""));
        }
        return NumberUtils.toInt(views.replace(",", ""));
    }
    
    public static String convertView(String views) {
        int number = parseViews(views);
        return convertView(number);
    }
    
    public static String convertView(int number) {
        if (number > 10000) {
            return String.format("%.1f", number / 10000f) + "万";
        }
        return String.valueOf(number);
    }
}
