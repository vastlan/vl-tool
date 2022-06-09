package com.vast.vl_tool.annotation;

import com.vast.vl_tool.http.config.WebMvcCrossDomainConfigurer;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author vastlan
 * @description 允许跨域
 * @created 2022/1/24 17:00
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(WebMvcCrossDomainConfigurer.class)
public @interface EnableCrossDomain {
}
