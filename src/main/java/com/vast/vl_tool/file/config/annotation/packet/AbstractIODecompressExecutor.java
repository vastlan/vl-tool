package com.vast.vl_tool.file.config.annotation.packet;

/**
 * @author vastlan
 * @description
 * @created 2022/7/27 17:53
 */
public abstract class AbstractIODecompressExecutor extends AbstractIOPacketExecutor {
  public AbstractIODecompressExecutor(Object fileContent, String targetPath) {
    super(fileContent, targetPath);
  }
}
