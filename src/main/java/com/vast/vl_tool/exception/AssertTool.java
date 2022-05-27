package com.vast.vl_tool.exception;

import org.springframework.lang.Nullable;

/**
 * @author vastlan
 * @description
 * @created 2022/3/21 10:51
 */
public class AssertTool {

  public static void isNull(Object param, String msg) {
    isNull(param, new PropertyException(msg));
  }

  public static void isNull(Object param, RuntimeException runtimeException) {
     if (param == null) {
       throw runtimeException;
     }
  }

  public static void isNull(RuntimeException runtimeException, Object... params) {
    for (Object param : params) {
      isNull(param, runtimeException);
    }
  }

  public static void isTrue(Boolean option, RuntimeException runtimeException) {
    if (option) {
      throw runtimeException;
    }
  }
}
