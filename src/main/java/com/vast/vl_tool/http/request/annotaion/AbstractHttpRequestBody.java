package com.vast.vl_tool.http.request.annotaion;

import java.util.HashMap;
import java.util.Map;

/**
 * @author vastlan
 * @description
 * @created 2022/8/1 10:17
 */
public abstract class AbstractHttpRequestBody<T> extends HttpProcessorAdepter<HttpRequestProcessor> implements HttpRequestBody<T> {

  protected Map<String, Object> paramMap = new HashMap<>();

  @Override
  public AbstractHttpRequestBody put(String key, Object value) {
    paramMap.put(key, value);
    return this;
  }

  @Override
  public HttpRequestBody setBody(T t) {
    return null;
  }

  @Override
  public HttpRequestProcessor and() {
    return super.and();
  }
}
