package com.vast.vl_tool.http.request.entity;

import com.vast.vl_tool.http.request.annotaion.HttpProcessorAdepter;
import com.vast.vl_tool.http.request.annotaion.HttpRequestBody;
import com.vast.vl_tool.http.request.annotaion.HttpRequestProcessor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Collection;
import java.util.List;

/**
 * @author vastlan
 * @description
 * @created 2022/8/1 10:19
 */
public class FormDataHttpRequestBody extends HttpProcessorAdepter<HttpRequestProcessor> implements HttpRequestBody<MultiValueMap<String, Object>> {

  private MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();

  @Override
  public MultiValueMap<String, Object> body() {
    return paramMap;
  }

  @Override
  public FormDataHttpRequestBody put(String key, Object value) {
    if (value instanceof Collection) {
      paramMap.addAll(key, (List<?>) value);
    } else {
      paramMap.add(key, value);
    }

    return this;
  }

  @Override
  public HttpRequestBody setBody(MultiValueMap<String, Object> stringObjectMultiValueMap) {
    this.paramMap = stringObjectMultiValueMap;
    return this;
  }
}
