package com.vast.vl_tool.http.request.annotaion;

import java.util.HashMap;
import java.util.Map;

/**
 * @author vastlan
 * @description
 * @created 2022/8/1 10:17
 */
public abstract class AbstractHttpRequestBody<T> extends HttpProcessorAdepter<HttpRequestProcessor> implements HttpRequestBody<T> {

  protected final Map<String, Object> PARAM_MAP = new HashMap<>();

  @Override
  public HttpRequestBody put(String key, Object value) {
    PARAM_MAP.put(key, value);
    return this;
  }
}
