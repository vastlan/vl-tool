package com.vast.vl_tool.file.config.annotation.download;

import com.vast.vl_tool.file.entity.FileBody;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipOutputStream;

/**
 * @author vastlan
 * @description
 * @created 2022/7/26 16:33
 */
public class MultiFileDownloadExecutor extends AbstractIODownloadExecutor {

  public MultiFileDownloadExecutor(HttpServletResponse httpServletResponse, List<FileBody> fileBodyList) {
    super(httpServletResponse, fileBodyList, "DataPackage", "zip");
  }

  @Override
  public void execute() throws IOException {
    List<FileBody> fileBodyList = (List<FileBody>) content;

    try (ZipOutputStream zipOutputStream = new ZipOutputStream(new BufferedOutputStream(response.getOutputStream()), StandardCharsets.UTF_8)) {
      for (FileBody fileBody : fileBodyList) {
        try {
          if (!fileBody.existFile()) {
            System.err.println("找不到文件" + fileBody.getFileName());
            continue;
          }

          ZipEntry zipEntry = new ZipEntry(fileBody.getFileName());
          zipOutputStream.putNextEntry(zipEntry);

          try (InputStream inputStream = Files.newInputStream(fileBody.getPath())) {
            inputStream.transferTo(zipOutputStream);
          }
        } catch (ZipException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
