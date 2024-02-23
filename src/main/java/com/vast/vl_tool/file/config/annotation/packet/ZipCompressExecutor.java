package com.vast.vl_tool.file.config.annotation.packet;

import com.vast.vl_tool.exception.AssertTool;
import com.vast.vl_tool.file.constant.FileExtension;
import com.vast.vl_tool.file.entity.FileBody;
import com.vast.vl_tool.time.DateTool;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author vastlan
 * @description
 * @created 2022/7/27 9:32
 */

public class ZipCompressExecutor extends AbstractIOCompressExecutor {

  private List<FileBody> fileBodyList;

  private String fileName = DateTool.getFormattedCurrentDateTime("yyyyMMddHHmmss");
  private String fileExtensionName = FileExtension.ZIP.name().toLowerCase(Locale.ROOT);

  public ZipCompressExecutor(List<FileBody> fileBodyList, String targetPath) {
    super(fileBodyList, targetPath);
    this.fileBodyList = fileBodyList;
  }

  public ZipCompressExecutor(List<FileBody> fileBodyList, String targetPath, String fileName) {
    super(fileBodyList, targetPath);
    this.fileBodyList = fileBodyList;

    if (StringUtils.hasLength(fileName)) {
      this.fileName = fileName;
    }
  }

  public ZipCompressExecutor(List<FileBody> fileBodyList, String targetPath, String fileName, String fileExtensionName) {
    super(fileBodyList, targetPath);
    this.fileBodyList = fileBodyList;

    if (StringUtils.hasLength(fileName)) {
      this.fileName = fileName;
    }

    if (StringUtils.hasLength(fileExtensionName)) {
      this.fileExtensionName = fileExtensionName;
    }
  }

  @Override
  public boolean checkParamValidity() {
    Assert.notNull(fileContent, "FileBodyList cannot be null");
    AssertTool.isTrue(fileBodyList.isEmpty(), new IllegalArgumentException("FileBodyList must be have content"));
    AssertTool.isTrue(!StringUtils.hasLength(targetPath), new NullPointerException("TargetPath cannot be null"));

    return true;
  }

  @Override
  public void execute() throws IOException {
    String zipFileName = String.format("%s/%s.%s", targetPath, fileName, fileExtensionName);

    Path path = Paths.get(zipFileName);
    path = Files.createFile(path);

    try (ZipOutputStream zipOutputStream = new ZipOutputStream(new BufferedOutputStream(Files.newOutputStream(path)), StandardCharsets.UTF_8)) {
      for (FileBody fileBody : fileBodyList) {
        if (fileBody.getFile().exists()) {
          compressFiles(zipOutputStream, fileBody.getPath(), fileBody.getFileName());
        }
      }
    }

    fileBodyList.clear();
    setActionSuccessfulResult(FileBody.create(zipFileName));
  }

  private void compressFiles(ZipOutputStream zipOutputStream, Path fileSource, String fileName) throws IOException {
    if (Files.isDirectory(fileSource)) {
      List<Path> childList = Files.list(fileSource).collect(Collectors.toList());

      // 创建一个目录进入点
      // 文件名称后跟File.separator表示这是一个文件夹
      String parentPathName = fileName + File.separator;
      zipOutputStream.putNextEntry(new ZipEntry(parentPathName));

      if (!childList.isEmpty()) {
        for (Path child : childList) {
          compressFiles(zipOutputStream, child, parentPathName + child.getFileName().toString());
        }
      }

      return;
    }

    ZipEntry zipEntry = new ZipEntry(fileName);
    zipOutputStream.putNextEntry(zipEntry);

    try (InputStream inputStream = Files.newInputStream(fileSource)) {
      inputStream.transferTo(zipOutputStream);
    }
  }

}
