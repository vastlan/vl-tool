package com.vast.vl_tool.http.config.annotation;

import org.springframework.util.Assert;

/**
 * @author vastlan
 * @description
 * @created 2022/6/8 11:18
 */

public class HttpProcessorAdepter<P> {

  P httpProcessor;

  public P and() {
    return getProcessor();
  }

  protected P getProcessor() {
    Assert.state(this.httpProcessor != null, "httpProcessor cannot be null");
    return this.httpProcessor;
  }

  public void setProcessor(P processor) {
    this.httpProcessor = processor;
  }

}
