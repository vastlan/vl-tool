package com.vast.vl_tool.file.config.annotation.packet;

import com.vast.vl_tool.file.config.annotation.IOExecutor;

import java.io.IOException;

/**
 * @author vastlan
 * @description
 * @created 2022/7/27 9:28
 */
public interface IOPacketExecutor extends IOExecutor {
  void action() throws IOException;

  Object actionSuccessfulResult();
}
