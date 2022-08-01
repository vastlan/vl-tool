package com.vast.vl_tool.http.request.annotaion;

/**
 * @author vastlan
 * @description
 * @created 2022/7/29 15:16
 */
public interface HttpRequestSender<T> {
  T send() throws Exception;
}
