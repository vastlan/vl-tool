package com.vast.vl_tool.file.config.annotation;

import java.io.IOException;

/**
 * @author vastlan
 * @description
 * @created 2022/7/27 9:35
 */
public abstract class AbstractIOExecutor<H extends IOHandler> extends IOHandlerAdepter<H> implements IOExecutor {

  public void doAction() throws IOException {
    if (checkParamValidity()) {
      execute();
      return;
    }

    throw new IOException("IO操作相关参数异常");
  }

  public abstract boolean checkParamValidity();
}
