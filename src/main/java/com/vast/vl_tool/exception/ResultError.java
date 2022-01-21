package com.vast.vl_tool.exception;

import lombok.Data;

/**
 * @author vastlan
 * @description
 * @created 2022/1/21 11:59
 */

@Data
public class ResultError {

  private Integer code;

  private String message;
}
