package com.vast.vl_tool.http.request.entity;

import com.vast.vl_tool.http.constant.HttpRequestMethod;
import com.vast.vl_tool.http.request.annotaion.HttpProcessorAdepter;
import com.vast.vl_tool.http.request.annotaion.HttpRequestProcessor;

/**
 * @author vastlan
 * @description
 * @created 2022/8/1 11:16
 */
public class HttpRequestMethodWrap extends HttpProcessorAdepter<HttpRequestProcessor> {

  private HttpRequestContent httpRequestContent;

  public HttpRequestMethodWrap(HttpRequestContent httpRequestContent) {
    this.httpRequestContent = httpRequestContent;
  }

  public HttpRequestMethodWrap get() {
    httpRequestContent.setMethod(HttpRequestMethod.GET);
    return this;
  }

  public HttpRequestMethodWrap head() {
    httpRequestContent.setMethod(HttpRequestMethod.HEAD);
    return this;
  }

  public HttpRequestMethodWrap post() {
    httpRequestContent.setMethod(HttpRequestMethod.POST);
    return this;
  }

  public HttpRequestMethodWrap put() {
    httpRequestContent.setMethod(HttpRequestMethod.PUT);
    return this;
  }

  public HttpRequestMethodWrap patch() {
    httpRequestContent.setMethod(HttpRequestMethod.PATCH);
    return this;
  }

  public HttpRequestMethodWrap delete() {
    httpRequestContent.setMethod(HttpRequestMethod.DELETE);
    return this;
  }

  public HttpRequestMethodWrap options() {
    httpRequestContent.setMethod(HttpRequestMethod.OPTIONS);
    return this;
  }

  public HttpRequestMethodWrap trace() {
    httpRequestContent.setMethod(HttpRequestMethod.TRACE);
    return this;
  }

}
