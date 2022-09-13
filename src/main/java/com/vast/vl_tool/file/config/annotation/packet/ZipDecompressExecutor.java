package com.vast.vl_tool.file.config.annotation.packet;

import com.vast.vl_tool.exception.AssertTool;
import com.vast.vl_tool.file.entity.FileBody;
import com.vast.vl_tool.file.config.annotation.create.FolderCreationExecutor;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author vastlan
 * @description
 * @created 2022/7/27 17:54
 */

public class ZipDecompressExecutor extends AbstractIODecompressExecutor {

  private FileBody fileBody;

  public ZipDecompressExecutor(FileBody fileBody, String targetPath) {
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
    // 系统编码
    // Charset.forName(System.getProperty("sun.jnu.encoding") 获取系统默认编码格式
    // 若无设置 Charset 且压缩包名称为中文名，报 java.util.zip.ZipException: invalid CEN header (bad entry name)
    ZipFile zipFile = new ZipFile(fileBody.getFile(), Charset.forName(System.getProperty("sun.jnu.encoding")));

    // 开始解压
    Enumeration<? extends ZipEntry> entries = zipFile.entries();
    System.out.println("开始解压 " + fileBody.getFileAbsolutePath());

    while (entries.hasMoreElements()) {
      ZipEntry entry = (ZipEntry) entries.nextElement();

      // 如果是文件夹，就创建个文件夹
      if (entry.isDirectory()) {
        FolderCreationExecutor.createFolder(fileBody.getFile());
        continue;
      }

      // 如果是文件，就先创建一个文件，然后用io流把内容copy过去
      File targetFile = new File(targetPath + "/" + entry.getName());

      // 保证这个文件的父文件夹必须要存在
      if (!targetFile.getParentFile().exists()) {
        targetFile.getParentFile().mkdirs();
      }

      if (targetFile.isFile() && !targetFile.exists()) {
        targetFile.createNewFile();
      }

      try (FileOutputStream fos = new FileOutputStream(targetFile);
           InputStream is = zipFile.getInputStream(entry)) {
        IOUtils.copy(is, fos);
      }
    }

    System.out.println(fileBody.getFileName() + " 解压完毕 " + targetPath);
    setActionSuccessfulResult(FileBody.create(targetPath));
  }
}
