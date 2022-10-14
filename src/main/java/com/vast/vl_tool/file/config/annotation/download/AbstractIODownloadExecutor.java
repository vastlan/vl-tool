package com.vast.vl_tool.file.config.annotation.download;

import com.vast.vl_tool.file.config.annotation.AbstractIOExecutor;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author vastlan
 * @description
 * @created 2022/7/26 15:19
 */

public abstract class AbstractIODownloadExecutor extends AbstractIOExecutor implements IODownloadExecutor {

  protected HttpServletResponse response;

  protected Object content;

  protected String fileName;

  protected String fileNameSuffix;

  public AbstractIODownloadExecutor(HttpServletResponse response, Object content, String fileName) {
    this.response = response;
    this.content = content;
    this.fileName = fileName;
  }

  public AbstractIODownloadExecutor(HttpServletResponse response, Object content, String fileName, String fileNameSuffix) {
    this.response = response;
    this.content = content;
    this.fileName = fileName;
    this.fileNameSuffix = fileNameSuffix;
  }

  @Override
  public Object getContent() {
    return content;
  }

  @Override
  public void download() throws IOException {
    response.setHeader (IODownloadHandler.HEADER_CONTENT_DISPOSITION,IODownloadHandler.transformToContentDispositionValue(fileName, fileNameSuffix));
    doAction();
  }

  public void download(String fileName) throws IOException {
    this.fileName = fileName;
    this.fileNameSuffix = fileName.substring(fileName.lastIndexOf(".")  + 1);
    download();
  }

  public void download(String fileName, String fileNameSuffix) throws IOException {
    this.fileName = fileName;
    this.fileNameSuffix = fileNameSuffix;
    download();
  }

  @Override
  public boolean checkParamValidity() {
    Assert.notNull(response, "HttpServletResponse could not be null");
    Assert.notNull(content, "Content could not be null");
    Assert.isTrue(StringUtils.hasLength(fileName), "FileName could not be null");
    Assert.isTrue(fileName.lastIndexOf(".") != -1 || StringUtils.hasLength(fileNameSuffix), "FileNameSuffix could not be null");

    return true;
  }
}
