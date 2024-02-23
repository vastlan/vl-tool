package com.vast.vl_tool.http.request.entity;

import com.vast.vl_tool.http.constant.HttpRequestMethod;
import com.vast.vl_tool.http.request.annotaion.HttpRequestBody;
import com.vast.vl_tool.http.request.annotaion.RequestBody;
import lombok.*;

/**
 * @author vastlan
 * @description
 * @created 2022/7/29 15:26
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HttpRequestContent {

  private String url;

  private String method = "GET";

  private String mediaType = "application/json;charset=UTF-8";

  private HttpRequestHeader header;

  private RequestBody body;

  private Response response = new Response();

  public void setMethod(HttpRequestMethod method) {
    this.method = method.name().toUpperCase();
  }

  public void setMethod(String method) {
    this.method = method.toUpperCase();
  }

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  @ToString
  public class Response {

    private Integer statusCode;

    private Class type = String.class;

    private Object result;

  }
}
