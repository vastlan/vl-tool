package com.vast.vl_tool.http;

import com.vast.vl_tool.exception.ResultError;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author vastlan
 * @description
 * @created 2022/1/21 11:56
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseResult<T> implements Serializable {

  private int status;

  private String message;

  private transient T data;

  public ResponseResult(T data) {
    this(1, "ok", data);
  }

  public ResponseResult(String message, T data) {
    this(1, message, data);
  }

  public ResponseResult(Exception exception) {
    this(0, exception.getMessage(), null);
  }

  public ResponseResult(Exception exception, T data) {
    this(0, exception.getMessage(), data);
  }

  public ResponseResult(ResultError errorCode) {
    this(0, errorCode.getMessage(), (T) errorCode.getCode());
  }

  public static <T> ResponseResult<T> success() {
    return new ResponseResult<>(1, "ok", null);
  }

  public static <T> ResponseResult<T> success(T data) {
    return new ResponseResult<>(1, "ok", data);
  }

  public static <T> ResponseResult<T> success(String message, T data) {
    return new ResponseResult<>(1, message, data);
  }

  public static <T> ResponseResult<T> fail() {
    return new ResponseResult<>(0, "fail", null);
  }

  public static <T> ResponseResult<T> fail(Exception exception) {
    return new ResponseResult<>(0, exception.getMessage(), null);
  }

  public static <T> ResponseResult<T> fail(Exception exception, T data) {
    return new ResponseResult<>(0, exception.getMessage(), data);
  }

  public static <T> ResponseResult<T> fail(ResultError errorCode) {
    return new ResponseResult<>(0, errorCode.getMessage(), (T) errorCode.getCode());
  }

  public static <T> ResponseResult<T> fail(ResultError errorCode, String message) {
    return new ResponseResult<>(0, message, (T) errorCode.getCode());
  }
}
