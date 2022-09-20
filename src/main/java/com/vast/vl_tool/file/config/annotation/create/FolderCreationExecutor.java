package com.vast.vl_tool.file.config.annotation.create;

import com.vast.vl_tool.exception.AssertTool;
import com.vast.vl_tool.file.FileTool;
import com.vast.vl_tool.file.entity.FileBody;

import java.io.File;
import java.io.IOException;

/**
 * @author vastlan
 * @description
 * @created 2022/7/26 9:58
 */

public class FolderCreationExecutor implements IOCreationExecutor<FileBody> {

  private FileBody fileBody;

  public static boolean createFolder(File file) {
    if (file.exists()) {
      return true;
    }

    return file.mkdirs();
  }

  @Override
  public FileBody create(FileBody fileBody) throws IOException {
    AssertTool.isTrue(fileBody.isFile(), new IllegalArgumentException("非文件夹路径参数异常"));
    this.fileBody = fileBody;

    execute();

    return fileBody;
  }

  @Override
  public void execute() throws IOException {
    if (!createFolder(fileBody.getFile())) {
      throw new IOException("文件夹创建失败");
    }
  }
}
