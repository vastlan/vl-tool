package com.vast.vl_tool.exception;

/**
 * @author vastlan
 * @description
 * @created 2022/3/21 10:51
 */
public class AssertTool {

  public static void isBlank(Object param, String msg) {
    isBlank(param, new PropertyException(msg));
  }

  public static void isBlank(Object param, ExceptionBase exceptionBase) {
     if (param == null) {
       throw exceptionBase;
     }
  }

  public static void isBlank(ExceptionBase exceptionBase, Object... params) {
    for (Object param : params) {
      isBlank(param, exceptionBase);
    }
  }
}
