package com.vast.vl_tool.file.config.annotation.grab;

import com.vast.vl_tool.exception.AssertTool;
import com.vast.vl_tool.file.FileBody;
import com.vast.vl_tool.file.config.annotation.AbstractIOExecutor;
import com.vast.vl_tool.time.DateTool;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * @author vastlan
 * @description
 * @created 2022/7/28 10:55
 */
public abstract class AbstractIOGrabExecutor extends AbstractIOExecutor<IOGrabHandler> implements IOGrabExecutor<IOGrabHandler> {
  protected Object fileContent;

  protected FileBody grabResult;

  protected String targetPath;

  protected String grabbedFileName;

  public AbstractIOGrabExecutor(Object fileContent, String targetPath) {
    this.fileContent = fileContent;
    this.targetPath = targetPath;
  }

  @Override
  public void grab() throws IOException {
    doAction();
  }

  public void setGrabResult(FileBody grabResult) {
    this.grabResult = grabResult;
  }

  @Override
  public FileBody grabResult() {
    return grabResult;
  }

  @Override
  public boolean checkParamValidity() {
    Assert.notNull(fileContent, "FileBody cannot be null");
    AssertTool.isTrue(!StringUtils.hasLength(targetPath), new NullPointerException("TargetPath cannot be null"));

    return true;
  }

  @Override
  public void setIOHandler(IOGrabHandler ioHandler) {
    super.setIOHandler(ioHandler);
  }

  public AbstractIOGrabExecutor grabbedFileName(String name) {
    this.grabbedFileName = name;
    return this;
  }

  public String grabbedFileName() {
    if (StringUtils.hasLength(grabbedFileName)) {
      int idx = grabbedFileName.lastIndexOf(".");

      if (idx != -1) {
        grabbedFileName = grabbedFileName.substring(0, idx);
      }
    }
    else {
      grabbedFileName = defaultGrabbedFileName();
    }

    return grabbedFileName + ".jpg";
  }

  public String defaultGrabbedFileName() {
    return DateTool.getFormattedCurrentDateTime("yyyyMMddHHmmdd") + "_thumbnail";
  }
}
