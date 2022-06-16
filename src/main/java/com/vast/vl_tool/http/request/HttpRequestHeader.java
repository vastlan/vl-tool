package com.vast.vl_tool.http.request;

import com.vast.vl_tool.http.config.annotation.HttpProcessorAdepter;
import com.vast.vl_tool.http.config.annotation.HttpRequestProcessor;
import org.springframework.http.HttpHeaders;

/**
 * @author vastlan
 * @description
 * @created 2022/6/16 16:37
 */

public class HttpRequestHeader extends HttpProcessorAdepter<HttpRequestProcessor> {

  private HttpHeaders headers = new HttpHeaders();

  public HttpHeaders value() {
    return headers;
  }

  public HttpRequestHeader setHeaders(HttpHeaders headers) {
    this.headers = headers;
    return this;
  }

  public HttpRequestHeader put(String key, String value) {
    headers.add(key, value);
    return this;
  }

  public HttpRequestHeader set(String key, String value) {
    headers.set(key, value);
    return this;
  }
}
