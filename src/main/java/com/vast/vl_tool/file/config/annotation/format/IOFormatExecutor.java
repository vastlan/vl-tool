package com.vast.vl_tool.file.config.annotation.format;

import com.vast.vl_tool.file.FileBody;
import com.vast.vl_tool.file.config.annotation.IOExecutor;

import java.io.IOException;

/**
 * @author vastlan
 * @description
 * @created 2022/7/28 18:14
 */

public interface IOFormatExecutor extends IOExecutor {

  void format() throws IOException;

  FileBody formattedResult();
}
