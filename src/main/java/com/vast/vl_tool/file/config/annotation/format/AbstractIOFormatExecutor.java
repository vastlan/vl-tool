package com.vast.vl_tool.file.config.annotation.format;

import com.vast.vl_tool.file.entity.FileBody;
import com.vast.vl_tool.file.config.annotation.AbstractIOExecutor;
import org.springframework.util.Assert;

import java.io.IOException;

/**
 * @author vastlan
 * @description
 * @created 2022/7/28 18:21
 */

public abstract class AbstractIOFormatExecutor extends AbstractIOExecutor<IOFormatHandler> implements IOFormatExecutor {

  protected Object body;

  private Object formattedResult;

  public AbstractIOFormatExecutor(Object body) {
    this.body = body;
  }

  @Override
  public void format() throws IOException {
    doAction();
  }

  @Override
  public boolean checkParamValidity() {
    Assert.notNull(body, "Body 不能为空");
    return true;
  }

  public void setFormattedResult(Object result) {
    this.formattedResult = result;
  }

  @Override
  public Object formattedResult() {
    return formattedResult;
  }
}
