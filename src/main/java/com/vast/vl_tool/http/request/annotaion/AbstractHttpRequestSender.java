package com.vast.vl_tool.http.request.annotaion;

/**
 * @author vastlan
 * @description
 * @created 2022/7/29 15:19
 */
public abstract class AbstractHttpRequestSender<T> implements HttpRequestSender<T> {

  @Override
  public T send() throws Exception {
    if (checkParamValidity()) {
      return handle();
    }

    return null;
  }

  public abstract boolean checkParamValidity() throws Exception;

  public abstract T handle() throws Exception;
}
