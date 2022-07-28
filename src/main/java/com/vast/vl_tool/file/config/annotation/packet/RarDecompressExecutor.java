package com.vast.vl_tool.file.config.annotation.packet;

import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.rarfile.FileHeader;
import com.vast.vl_tool.exception.AssertTool;
import com.vast.vl_tool.file.FileBody;
import com.vast.vl_tool.file.config.annotation.create.FolderCreationExecutor;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author vastlan
 * @description
 * @created 2022/7/27 18:54
 */
public class RarDecompressExecutor extends AbstractIODecompressExecutor {

  private FileBody fileBody;

  public RarDecompressExecutor(FileBody fileBody, String targetPath) {
    super(fileBody, targetPath);
    this.fileBody = fileBody;
  }

  @Override
  public boolean checkParamValidity() {
    Assert.notNull(fileContent, "FileBody cannot be null");
    AssertTool.isTrue(!StringUtils.hasLength(targetPath), new NullPointerException("TargetPath cannot be null"));
    AssertTool.isTrue(!fileBody.existFile(), new IllegalArgumentException("文件不存在"));

    return true;
  }

  @Override
  public void execute() throws IOException {
    FileBody absoluteFolderPathFileBody = FileBody.create(targetPath);

    if (!absoluteFolderPathFileBody.existFile()) {
      FolderCreationExecutor.createFolder(absoluteFolderPathFileBody.getFile());
    }

    try(Archive archive = new Archive(fileBody.getFile())) {

      if (archive == null) {
        throw new IOException("获取文件源失败");
      }

      // 打印文件信息
      archive.getMainHeader().print();

      FileHeader fileHeader;

      while ((fileHeader = archive.nextFileHeader()) != null) {
        FileBody dirFileBody = FileBody.create(targetPath + File.separator + fileHeader.getFileNameString());
        File file = dirFileBody.getFile();

        // 如果是文件夹
        if (fileHeader.isDirectory()) {
          FolderCreationExecutor.createFolder(file);
          continue;
        }

        if (dirFileBody.notExistAndIsFile()) {
          FolderCreationExecutor.createFolder(file);
        }

        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
          archive.extractFile(fileHeader, fileOutputStream);
        }
      }

      setActionSuccessfulResult(FileBody.create(targetPath));

    } catch (RarException e) {
      e.printStackTrace();
      throw new IOException("unrar失败");
    }
  }
}
