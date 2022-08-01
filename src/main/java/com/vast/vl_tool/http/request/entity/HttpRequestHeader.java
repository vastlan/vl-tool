package com.vast.vl_tool.http.request.entity;

import com.vast.vl_tool.http.request.annotaion.HttpProcessorAdepter;
import com.vast.vl_tool.http.request.annotaion.HttpRequestProcessor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author vastlan
 * @description
 * @created 2022/7/29 15:42
 */

public class HttpRequestHeader extends HttpProcessorAdepter<HttpRequestProcessor> {

  private final Map<String, Object> HEADER_MAP = new HashMap<>();

  public HttpRequestHeader put(String key, Object value) {
    HEADER_MAP.put(key, value);
    return this;
  }

  public Map<String, Object> getHeaders() {
    return HEADER_MAP;
  }
}
