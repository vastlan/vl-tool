package com.vast.vl_tool.file.config.annotation;

import org.springframework.util.Assert;

import java.io.IOException;

/**
 * @author vastlan
 * @description
 * @created 2022/7/25 17:08
 */

public abstract class AbstractIOHandler<T> implements IOHandler<T> {

  protected T body;

  public T invoke() throws IOException {
    checkParamValidity();
    return handle();
  }

  public T invoke(IOHandler ioHandler) throws IOException {
    checkParamValidity();
    return (T) ioHandler.handle();
  }

  protected void setBody(T body) {
    this.body = body;
  }

  protected void checkParamValidity() {
    Assert.notNull(body, "IO body 不能为空");
  }

}
