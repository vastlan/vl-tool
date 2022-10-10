package com.vast.vl_tool.http.request.entity;

import com.vast.vl_tool.http.request.annotaion.HttpProcessorAdepter;
import com.vast.vl_tool.http.request.annotaion.HttpRequestBody;
import com.vast.vl_tool.http.request.annotaion.HttpRequestProcessor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * @author vastlan
 * @description
 * @created 2022/8/1 10:19
 */
public class FormDataHttpRequestBody extends HttpProcessorAdepter<HttpRequestProcessor> implements HttpRequestBody<MultiValueMap<String, Object>> {

  private final MultiValueMap<String, Object> PARAM_MAP = new LinkedMultiValueMap<>();

  @Override
  public MultiValueMap<String, Object> body() {
    return PARAM_MAP;
  }

  @Override
  public FormDataHttpRequestBody put(String key, Object value) {
    PARAM_MAP.add(key, value);
    return this;
  }
}
