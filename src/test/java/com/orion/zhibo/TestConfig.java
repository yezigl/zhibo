/**
 * Copyright 2015 yezi.gl. All Rights Reserved.
 */
package com.orion.zhibo;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;

/**
 * description here
 *
 * @author yezi
 * @since 2015年6月14日
 */
@Configuration
@PropertySource(value = { "classpath:application.properties", "classpath:mongodb.properties" })
@ImportResource("classpath:applicationContext.xml")
@ComponentScan(value = { "com.orion.mongodb", "com.orion.zhibo.dao", "com.orion.zhibo.spider", "com.orion.zhibo.service" })
public class TestConfig {

}
