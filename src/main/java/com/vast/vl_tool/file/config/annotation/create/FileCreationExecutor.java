package com.vast.vl_tool.file.config.annotation.create;

import com.vast.vl_tool.exception.AssertTool;
import com.vast.vl_tool.file.FileTool;
import com.vast.vl_tool.file.entity.FileBody;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;

/**
 * @author vastlan
 * @description
 * @created 2022/7/26 9:56
 */

public class FileCreationExecutor implements IOCreationExecutor<FileBody> {

  private FileBody fileBody;

  @Override
  public FileBody create(FileBody fileBody) throws IOException {
    AssertTool.isTrue(!FileTool.isFile(fileBody.getFile()), new IllegalArgumentException("非文件路径参数异常"));

    this.fileBody = fileBody;

    execute();

    return fileBody;
  }

  @Override
  public void execute() throws IOException {
    File file = fileBody.getFile();

    String errMsg = null;

    boolean isCreatedSuccessfully = FolderCreationExecutor.createFolder(file.getParentFile());

    if (!isCreatedSuccessfully) {
      errMsg = "创建" + file.getName() + "父路径失败";
    }
    else if (fileBody.notExistAndIsFile()) {
      if (!file.createNewFile()) {
        errMsg = file.getName() + "创建失败";
      }
    }

    if (StringUtils.hasLength(errMsg)) {
      throw new IOException(errMsg);
    }
  }
}
