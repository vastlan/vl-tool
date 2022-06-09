package com.vast.vl_tool.http.request;

import com.vast.vl_tool.http.config.annotation.HttpProcessorAdepter;
import com.vast.vl_tool.http.config.annotation.HttpRequestProcessor;
import com.vast.vl_tool.json.JsonTool;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * @author vastlan
 * @description
 * @created 2022/6/8 9:58
 */

public class FormDataHttpRequestBody extends HttpProcessorAdepter<HttpRequestProcessor>
  implements HttpRequestBody {

  private MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();

  public FormDataHttpRequestBody add(String key, Object value) {
    map.add(key, value);
    return this;
  }

  public FormDataHttpRequestBody addAll(MultiValueMap<String, Object> map) {
    map.addAll(map);
    return this;
  }

  @Override
  public HttpEntity body(HttpHeaders headers) {
    return new HttpEntity(map, headers);
  }
}
