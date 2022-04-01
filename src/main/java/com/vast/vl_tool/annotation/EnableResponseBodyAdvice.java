package com.vast.vl_tool.annotation;

import com.vast.vl_tool.http.response.AutoConfigurationResponseBodyAdvice;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author vastlan
 * @description
 * @created 2022/3/24 15:49
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(AutoConfigurationResponseBodyAdvice.class)
public @interface EnableResponseBodyAdvice {
}
