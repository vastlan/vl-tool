package com.vast.vl_tool.http;

import com.vast.vl_tool.http.request.annotaion.HttpRequestProcessor;
import com.vast.vl_tool.http.response.annotation.HttpResponseProcessor;

import javax.servlet.ServletResponse;

/**
 * @author vastlan
 * @description
 * @created 2022/7/29 11:32
 */
public class HttpTool {

  public static HttpRequestProcessor createRequest() {
    return new HttpRequestProcessor();
  }

  public static HttpResponseProcessor createResponse(ServletResponse response) {
    return new HttpResponseProcessor(response);
  }
}
