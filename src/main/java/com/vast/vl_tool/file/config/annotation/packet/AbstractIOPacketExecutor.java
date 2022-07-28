package com.vast.vl_tool.file.config.annotation.packet;

import com.vast.vl_tool.file.config.annotation.AbstractIOExecutor;

import java.io.IOException;

/**
 * @author vastlan
 * @description
 * @created 2022/7/27 17:38
 */

public abstract class AbstractIOPacketExecutor extends AbstractIOExecutor implements IOPacketExecutor {

  protected Object fileContent;

  protected Object actionSuccessfulResult;

  protected String targetPath;

  public AbstractIOPacketExecutor(Object fileContent, String targetPath) {
    this.fileContent = fileContent;
    this.targetPath = targetPath;
  }

  @Override
  public void action() throws IOException {
    doAction();
  }

  public void setActionSuccessfulResult(Object actionSuccessfulResult) {
    this.actionSuccessfulResult = actionSuccessfulResult;
  }

  @Override
  public Object actionSuccessfulResult() {
    return actionSuccessfulResult;
  }

}
