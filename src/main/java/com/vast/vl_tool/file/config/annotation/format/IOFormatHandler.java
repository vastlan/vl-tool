package com.vast.vl_tool.file.config.annotation.format;

import com.vast.vl_tool.file.constant.FileType;
import com.vast.vl_tool.file.entity.ExifFileBody;
import com.vast.vl_tool.file.entity.FileBody;
import com.vast.vl_tool.file.config.annotation.AbstractIOHandler;
import kotlin.jvm.internal.PackageReference;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

/**
 * @author vastlan
 * @description
 * @created 2022/7/28 17:59
 */
public class IOFormatHandler extends AbstractIOHandler<Object> {

  private IOFormatExecutor ioFormatExecutor;

  private FileType fileType = FileType.PICTURE;

  public IOFormatHandler content(InputStream inputStream) {
    setBody(new FileBody(inputStream));
    return this;
  }

  public IOFormatHandler toEXIF() {
    return toEXIF((FileBody) body);
  }

  public IOFormatHandler toEXIF(FileBody fileBody) {
    ExifFileBody exifFileBody = ExifFileBody.create(fileBody);
    ioFormatExecutor = new FormatToEXIFExecutor(exifFileBody, fileType);
    return this;
  }

  public IOFormatHandler video() {
    fileType = FileType.VIDEO;
    return this;
  }

  public IOFormatHandler picture() {
    fileType = FileType.PICTURE;
    return this;
  }

  public String toBase64(InputStream inputStream) {
    try {
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

      int bytesRead;
      byte[] buffer = new byte[8192]; // Smaller buffer size for less memory usage

      while ((bytesRead = inputStream.read(buffer)) != -1) {
        outputStream.write(buffer, 0, bytesRead);
      }

      inputStream.close();

      return Base64.getEncoder().encodeToString(outputStream.toByteArray());
    } catch (IOException e) {
      e.printStackTrace();
    }

    return null;
  }

  @Override
  public Object handle() throws IOException {
    ioFormatExecutor.format();
    return ioFormatExecutor.formattedResult();
  }
}
