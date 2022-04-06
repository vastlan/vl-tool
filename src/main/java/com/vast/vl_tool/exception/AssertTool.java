package com.vast.vl_tool.exception;

import org.springframework.lang.Nullable;

/**
 * @author vastlan
 * @description
 * @created 2022/3/21 10:51
 */
public class AssertTool {

  public static void isNotNull(Object param, String msg) {
    isNotNull(param, new PropertyException(msg));
  }

  public static void isNotNull(Object param, RuntimeException runtimeException) {
     if (param == null) {
       throw runtimeException;
     }
  }

  public static void isNotNull(RuntimeException runtimeException, Object... params) {
    for (Object param : params) {
      isNotNull(param, runtimeException);
    }
  }
}
