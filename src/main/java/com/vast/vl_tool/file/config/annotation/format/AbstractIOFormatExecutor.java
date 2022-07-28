package com.vast.vl_tool.file.config.annotation.format;

import com.vast.vl_tool.file.FileBody;
import com.vast.vl_tool.file.config.annotation.AbstractIOExecutor;
import org.springframework.util.Assert;

import java.io.IOException;

/**
 * @author vastlan
 * @description
 * @created 2022/7/28 18:21
 */

public abstract class AbstractIOFormatExecutor extends AbstractIOExecutor<IOFormatHandler> implements IOFormatExecutor {

  protected FileBody fileBody;

  private FileBody formattedResult;

  public AbstractIOFormatExecutor(FileBody fileBody) {
    this.fileBody = fileBody;
  }

  @Override
  public void format() throws IOException {
    doAction();
  }

  @Override
  public boolean checkParamValidity() {
    Assert.notNull(fileBody, "FileBody不能为空");
    return true;
  }

  public void setFormattedResult(FileBody fileBody) {
    this.formattedResult = fileBody;
  }

  @Override
  public FileBody formattedResult() {
    return formattedResult;
  }
}
