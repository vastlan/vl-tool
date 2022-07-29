package com.vast.vl_tool.file.config.annotation.format;

import com.vast.vl_tool.file.entity.ExifFileBody;
import com.vast.vl_tool.file.entity.FileBody;
import com.vast.vl_tool.file.config.annotation.AbstractIOHandler;

import java.io.IOException;

/**
 * @author vastlan
 * @description
 * @created 2022/7/28 17:59
 */
public class IOFormatHandler extends AbstractIOHandler<Object> {

  private IOFormatExecutor ioFormatExecutor;

  public IOFormatHandler content(FileBody fileBody) {
    setBody(fileBody);
    return this;
  }

  public IOFormatHandler toEXIF() {
    ExifFileBody exifFileBody = ExifFileBody.create((FileBody) body);
    ioFormatExecutor = new FormatToEXIFExecutor(exifFileBody);
    return this;
  }

  @Override
  public Object handle() throws IOException {
    ioFormatExecutor.format();
    return ioFormatExecutor.formattedResult();
  }
}
