/**
 * Copyright 2015 yezi.gl. All Rights Reserved.
 */
package com.orion.zhibo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * description here
 *
 * @author yezi
 * @since 2015年6月14日
 */
@ControllerAdvice
public class AppExceptionHandler {
    
    private Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public String errorResponse(Exception exception) {
        logger.error(exception.getMessage(), exception);
        return "error";
    }

}