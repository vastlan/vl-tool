package com.vast.vl_tool.file.config.annotation.download;

import com.vast.vl_tool.file.config.annotation.IOExecutor;

import java.io.IOException;

/**
 * @author vastlan
 * @description
 * @created 2022/7/26 11:56
 */

public interface IODownloadExecutor extends IOExecutor {

  Object getContent();

  void download() throws IOException;
}
