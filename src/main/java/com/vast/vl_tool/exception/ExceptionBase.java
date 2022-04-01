package com.vast.vl_tool.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * @author vastlan
 * @description
 * @created 2022/1/21 12:55
 */

@Getter
@Setter
public abstract class ExceptionBase extends RuntimeException {
  public final static String UNKNOWN_ERROR_MESSAGE = "未知错误";
  public final static Integer UNKNOWN_ERROR_CODE = 999;

  private Integer errorCode;

  public ExceptionBase() {
    this(UNKNOWN_ERROR_MESSAGE);
  }

  public ExceptionBase(String message) {
    super(String.valueOf(message));
    this.setErrorCode(UNKNOWN_ERROR_CODE);
  }

  public ExceptionBase(String message, Integer errorCode) {
    this(message);
    this.setErrorCode(errorCode);
  }
}