package com.vast.vl_tool.annotation;

import com.vast.vl_tool.ws.WebSocketConfigurer;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author vastlan
 * @description
 * @created 2022/1/21 16:54
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(WebSocketConfigurer.class)
public @interface EnableAutoLoadWebSocketConfig {
}
