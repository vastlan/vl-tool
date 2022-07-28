package com.vast.vl_tool.file.config.annotation.grab;

import com.vast.vl_tool.file.FileBody;
import com.vast.vl_tool.file.config.annotation.IOExecutor;
import com.vast.vl_tool.file.config.annotation.IOHandler;

import java.io.IOException;

/**
 * @author vastlan
 * @description
 * @created 2022/7/28 10:47
 */

public interface IOGrabExecutor<H extends IOHandler> extends IOExecutor {

  void grab() throws IOException;

  FileBody grabResult();

  void setIOHandler(H ioHandler);
}
