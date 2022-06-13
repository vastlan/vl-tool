package com.vast.vl_tool.threadpool.config.annotation;

import com.vast.vl_tool.threadpool.ThreadPoolConfigurer;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author vastlan
 * @description
 * @created 2022/1/24 16:54
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(ThreadPoolConfigurer.class)
public @interface EnableAutoLoadThreadPoolConfig {
}
