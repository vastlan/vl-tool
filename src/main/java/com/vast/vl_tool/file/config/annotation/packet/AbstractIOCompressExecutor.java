package com.vast.vl_tool.file.config.annotation.packet;

/**
 * @author vastlan
 * @description
 * @created 2022/7/27 9:33
 */

public abstract class AbstractIOCompressExecutor extends AbstractIOPacketExecutor {
  public AbstractIOCompressExecutor(Object fileContent, String targetPath) {
    super(fileContent, targetPath);
  }
}
