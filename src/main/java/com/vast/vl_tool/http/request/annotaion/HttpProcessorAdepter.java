package com.vast.vl_tool.http.request.annotaion;

import org.springframework.util.Assert;

/**
 * @author vastlan
 * @description
 * @created 2022/8/1 10:43
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
