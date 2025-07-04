package com.vast.vl_tool.file.config.annotation.create;

import com.vast.vl_tool.file.FileTool;
import com.vast.vl_tool.file.entity.FileBody;
import com.vast.vl_tool.file.config.annotation.AbstractIOHandler;

import java.io.IOException;

/**
 * @author vastlan
 * @description
 * @use
 *  tool
 *    .createFile(xxxx)
 *    .invoke();
 * @created 2022/7/26 9:42
 */

public class IOCreationHandler extends AbstractIOHandler<FileBody> {

  private IOCreationExecutor<FileBody> ioExecutor;

  public IOCreationHandler createFile(FileBody fileBody) {
    setBody(fileBody);
    ioExecutor = new FileCreationExecutor();

    return this;
  }

  public IOCreationHandler createFolder(FileBody fileBody) {
    setBody(fileBody);
    ioExecutor = new FolderCreationExecutor();

    return this;
  }

  @Override
  public FileBody handle() throws IOException {
    return ioExecutor.create(body);
  }
}
