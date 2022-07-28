package com.vast.vl_tool.file.config.annotation;

import java.io.IOException;

/**
 * @author vastlan
 * @description
 * @created 2022/7/26 9:50
 */

public interface IOExecutor {

  /**
   * 具体实现
   * @throws IOException
   */
  void execute() throws IOException;
}
