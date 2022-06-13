package com.vast.vl_tool.http.response;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author vastlan
 * @description
 * @created 2022/3/24 16:57
 */

@SpringBootConfiguration
public class AutoConfigurationResponseBodyAdvice {

  @Resource
  private ResponseBodyAdvice responseBodyAdvice;

  @PostConstruct
  public void configure() {
    if (responseBodyAdvice == null) {
      return;
    }

    ((AbstractResponseBodyAdvice) responseBodyAdvice).initConfigure(new ResponseBodyConfigure());
  }

}
