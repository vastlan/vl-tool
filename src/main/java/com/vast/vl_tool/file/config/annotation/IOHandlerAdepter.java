package com.vast.vl_tool.file.config.annotation;

/**
 * @author vastlan
 * @description
 * @created 2022/7/28 14:44
 */
public class IOHandlerAdepter<H extends IOHandler> {
  H ioHandler;

  public H and() {
    return getIOHandler();
  }

  protected H getIOHandler() {
    return ioHandler;
  }

  public void setIOHandler(H ioHandler) {
    this.ioHandler = ioHandler;
  }
}
