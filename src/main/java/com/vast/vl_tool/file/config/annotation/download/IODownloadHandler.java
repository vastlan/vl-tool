package com.vast.vl_tool.file.config.annotation.download;

import com.vast.vl_tool.file.FileBody;
import com.vast.vl_tool.file.config.annotation.AbstractIOHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @author vastlan
 * @description
 * @use
 *  tool()
 *    .download()
 *    .content(xxx) / multiContent(xxx)
 *    .invoke()
 * @created 2022/7/26 10:18
 */
public class IODownloadHandler extends AbstractIOHandler<Object> {

  public static final String HEADER_CONTENT_DISPOSITION = "Content-Disposition";

  private IODownloadExecutor ioDownloadExecutor;

  private HttpServletResponse response;

  public IODownloadHandler(HttpServletResponse response) {
    this.response = response;
  }

  public static String transformToContentDispositionValue(String fileName, String fileType) throws UnsupportedEncodingException {
    if (fileName.indexOf(".") != -1) {
      fileName = fileName.substring(0, fileName.indexOf("."));
    }

    return transformToContentDispositionValue(String.format("%s.%s", fileName, fileType));
  }

  public static String transformToContentDispositionValue(String fileName) throws UnsupportedEncodingException {
    fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
    return String.format("attachment;filename=%s", fileName);
  }

  public IODownloadHandler multiContent(List<FileBody> fileBodyList) {
    setBody(fileBodyList);
    ioDownloadExecutor = new MultiFileDownloadExecutor(response, fileBodyList);
    return this;
  }

  public IODownloadHandler content(FileBody fileBody) {
    setBody(fileBody);
    ioDownloadExecutor = new OneFileDownloadExecutor(response, fileBody);
    return this;
  }

  @Override
  public Object handle() throws IOException {
    ioDownloadExecutor.download();
    return null;
  }

}
