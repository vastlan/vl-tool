package com.vast.vl_tool.exception;

/**
 * @author vastlan
 * @description
 * @created 2022/2/24 17:39
 */
public class PropertyException extends ExceptionBase {
  public final static String PROPERTY_ERROR_MESSAGE = "属性错误";

  public PropertyException() {
    super(PROPERTY_ERROR_MESSAGE);
  }

  public PropertyException(Object message) {
    super(message);
  }

  public PropertyException(Object message, Object errorCode) {
    super(message, errorCode);
  }

}
