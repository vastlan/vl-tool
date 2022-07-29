package com.vast.vl_tool.file.config.annotation.download;

import com.vast.vl_tool.file.entity.FileBody;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author vastlan
 * @description
 * @created 2022/7/26 15:23
 */

public class OneFileDownloadExecutor extends AbstractIODownloadExecutor {

  public OneFileDownloadExecutor(HttpServletResponse response, FileBody fileBody) {
    super(response, fileBody, fileBody.getFileName());
  }

  @Override
  public void execute() throws IOException{
    FileBody fileBody = (FileBody) content;

    if (!fileBody.existFile()) {
      throw new FileNotFoundException("找不到指定文件");
    }

    try (InputStream inputStream= new FileInputStream(fileBody.getFile())) {
      inputStream.transferTo(response.getOutputStream());
    }
  }
}
