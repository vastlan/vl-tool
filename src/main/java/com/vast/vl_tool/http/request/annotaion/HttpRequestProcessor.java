package com.vast.vl_tool.http.request.annotaion;

import com.vast.vl_tool.http.request.entity.HttpRequestBodyWrap;
import com.vast.vl_tool.http.request.entity.HttpRequestContent;
import com.vast.vl_tool.http.request.entity.HttpRequestHeader;
import com.vast.vl_tool.http.request.entity.HttpRequestMethodWrap;

/**
 * @author vastlan
 * @description
 * @use
 *  HttpTool
 *   .createRequest()
 *     .url(xxx)
 *     .method()
 *      .get()
 *   .and()
 *     .mediaType(xxx)
 *     .headers()
 *       .put(xxx, xxx)
 *   .and()
 *     .body()
 *       .json() | .formData()
 *          .put(xxx, xxx)
 *   .and()
 *     .restTemplate()
 *     .send()
 * @created 2022/7/29 16:01
 */

public class HttpRequestProcessor {

  private final HttpRequestContent HTTP_REQUEST_CONTENT = new HttpRequestContent();

  public HttpRequestProcessor url(String url) {
    HTTP_REQUEST_CONTENT.setUrl(url);
    return this;
  }

  public HttpRequestProcessor method(String method) {
    HTTP_REQUEST_CONTENT.setMethod(method);
    return this;
  }

  public HttpRequestMethodWrap method() {
    HttpRequestMethodWrap httpRequestMethodWrap = new HttpRequestMethodWrap(HTTP_REQUEST_CONTENT);
    createAndApply(httpRequestMethodWrap);
    return httpRequestMethodWrap;
  }

  public HttpRequestProcessor mediaType(String mediaType) {
    HTTP_REQUEST_CONTENT.setMediaType(mediaType);
    return this;
  }

  public HttpRequestHeader headers() {
    HttpRequestHeader httpRequestHeader = new HttpRequestHeader();
    createAndApply(httpRequestHeader);
    HTTP_REQUEST_CONTENT.setHeader(httpRequestHeader);
    return httpRequestHeader;
  }

  public HttpRequestBodyWrap body() {
    return new HttpRequestBodyWrap(HTTP_REQUEST_CONTENT, this);
  }

  public RestTemplateHttpRequestSender restTemplate() {
    return new RestTemplateHttpRequestSender(HTTP_REQUEST_CONTENT);
  }

  public OkHttpRequestSender okHttp() {
    return new OkHttpRequestSender(HTTP_REQUEST_CONTENT);
  }

  public <T> T send(HttpRequestSender<T> httpRequestSender) throws Exception {
    return httpRequestSender.send();
  }

  public void createAndApply(HttpProcessorAdepter adepter) {
    adepter.setProcessor(this);
  }
}
