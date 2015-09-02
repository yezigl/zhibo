/**
 * Copyright 2015 yezi.gl. All Rights Reserved.
 */
package com.orion.zhibo;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * description here
 *
 * @author yezi
 * @since 2015年6月14日
 */
@Configuration
@PropertySource(value = { "classpath:application.properties", "classpath:mongodb.properties" })
@ComponentScan(value = { "orion.mongodb" })
@EnableScheduling
public class AppConfig {

}
