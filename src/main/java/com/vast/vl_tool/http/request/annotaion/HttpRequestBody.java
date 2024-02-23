package com.vast.vl_tool.http.request.annotaion;

/**
 * @author vastlan
 * @description
 * @created 2022/7/29 15:42
 */

public interface HttpRequestBody<T> extends RequestBody<T> {

  HttpRequestBody put(String key, Object value);

  HttpRequestBody setBody(T t);
}
