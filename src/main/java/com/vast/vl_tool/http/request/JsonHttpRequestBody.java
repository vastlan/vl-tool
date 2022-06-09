package com.vast.vl_tool.http.request;

import com.vast.vl_tool.http.config.annotation.HttpProcessorAdepter;
import com.vast.vl_tool.http.config.annotation.HttpRequestProcessor;
import com.vast.vl_tool.json.JsonTool;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

/**
 * @author vastlan
 * @description
 * @created 2022/6/8 9:58
 */

public class JsonHttpRequestBody extends HttpProcessorAdepter<HttpRequestProcessor>
  implements HttpRequestBody {

  private Object obj;

  public JsonHttpRequestBody set(Object obj) {
    this.obj = obj;
    return this;
  }

  @Override
  public HttpEntity body(HttpHeaders headers) {
    return new HttpEntity(JsonTool.parseToJSONString(obj), headers);
  }
}
