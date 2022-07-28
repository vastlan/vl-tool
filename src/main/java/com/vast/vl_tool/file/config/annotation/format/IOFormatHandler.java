package com.vast.vl_tool.file.config.annotation.format;

import com.vast.vl_tool.file.FileBody;
import com.vast.vl_tool.file.config.annotation.AbstractIOHandler;

import java.io.IOException;

/**
 * @author vastlan
 * @description
 * @created 2022/7/28 17:59
 */
public class IOFormatHandler extends AbstractIOHandler<FileBody> {

  private IOFormatExecutor ioFormatExecutor;

  public IOFormatHandler content(FileBody fileBody) {
    setBody(fileBody);
    return this;
  }

  public IOFormatHandler toEXIF() {
    ioFormatExecutor = new FormatToEXIFExecutor(body);
    return this;
  }

  @Override
  public FileBody handle() throws IOException {
    ioFormatExecutor.format();
    return ioFormatExecutor.formattedResult();
  }
}
