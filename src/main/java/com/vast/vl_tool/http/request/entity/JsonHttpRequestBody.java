package com.vast.vl_tool.http.request.entity;

import com.vast.vl_tool.http.request.annotaion.AbstractHttpRequestBody;
import com.vast.vl_tool.format.JsonTool;

/**
 * @author vastlan
 * @description
 * @created 2022/8/1 10:12
 */
public class JsonHttpRequestBody extends AbstractHttpRequestBody<String> {

  @Override
  public String body() {
    return JsonTool.parseToJSONString(paramMap);
  }
}
