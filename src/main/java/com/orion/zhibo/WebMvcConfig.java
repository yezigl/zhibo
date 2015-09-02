/**
 * Copyright 2015 yezi.gl. All Rights Reserved.
 */
package com.orion.zhibo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.orion.zhibo.interceptor.AuthInterceptor;

/**
 * description here
 *
 * @author yezi
 * @since 2015年6月14日
 */
@Configuration
@ImportResource("classpath:applicationContext.xml")
public class WebMvcConfig extends WebMvcConfigurationSupport {

    @Autowired
    AuthInterceptor authInterceptor;

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor);
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
    }

}
