package com.vast.vl_tool.http.request.entity;

import com.vast.vl_tool.format.JsonTool;
import com.vast.vl_tool.http.request.annotaion.HttpProcessorAdepter;
import com.vast.vl_tool.http.request.annotaion.HttpRequestProcessor;
import com.vast.vl_tool.http.request.annotaion.RequestBody;
import org.springframework.util.Assert;

/**
 * @author vastlan
 * @description
 * @created 2023/3/15 15:57
 */
public class JsonHttpRequestObjectBody extends HttpProcessorAdepter<HttpRequestProcessor> implements RequestBody<String> {

  private final Object param;

  public JsonHttpRequestObjectBody(Object param) {
    this.param = param;
  }

  @Override
  public String body() {
    Assert.notNull(param, "body must not be null");
    return JsonTool.parseToJSONString(param);
  }
}
