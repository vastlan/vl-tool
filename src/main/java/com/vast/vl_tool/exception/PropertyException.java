package com.vast.vl_tool.exception;

/**
 * @author vastlan
 * @description
 * @created 2022/2/24 17:39
 */
public class PropertyException extends RuntimeException {
  public PropertyException() {
    super("属性错误");
  }

  public PropertyException(String errMsg) {
    super(errMsg);
  }
}
