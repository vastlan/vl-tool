package com.vast.vl_tool.file.entity;

import com.vast.vl_tool.file.FileTool;
import lombok.*;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author vastlan
 * @description
 * @created 2022/5/31 17:11
 */

@Getter
@Setter
@ToString
@NoArgsConstructor
public class FileBody implements Closeable {

  private Path path;

  private File file;

  private String fileAbsolutePath;

  private String pathRoot;

  private String fileRelativePath;

  private String fileName;

  private String fileSuffix;

  private InputStream inputStream;

  private String longitude;

  private String latitude;

  private String altitude;

  public FileBody(Path path) {
    this.path = path;
    this.file = path.toFile();
    init();
  }

  public FileBody(File file) {
    this.file = file;
    this.path = Paths.get(file.getAbsolutePath());
    init();
  }

  public FileBody(InputStream inputStream) {
    this.setInputStream(inputStream);
  }

  public static FileBody create(Path path) {
    return new FileBody(path);
  }

  public static FileBody create(File file) {
    return new FileBody(file);
  }

  public static FileBody create(String fileAbsolutePath) {
    return new FileBody(Paths.get(fileAbsolutePath));
  }

  public static FileBody create(String folderAbsolutePath, String fileName) {
    return new FileBody(Paths.get(folderAbsolutePath).resolve(fileName));
  }

  public static FileBody create(String source, String folderRelativePath, String fileName) {
    return new FileBody(Paths.get(source).resolve(folderRelativePath).resolve(fileName));
  }

  public boolean existFile() {
    return Files.exists(path);
  }

  public boolean notExistAndIsFile() {
    return !existFile() && isFile();
  }

  public boolean isFile() {
    return FileTool.isFile(this);
  }

  private void init() {
    this.fileAbsolutePath = path.toAbsolutePath().toString();
    this.pathRoot = path.getRoot().toString();
    this.fileRelativePath = fileAbsolutePath.replace(pathRoot, "\\");
    this.fileName = path.getFileName().toString();

    int suffixPointIdx = this.fileName.lastIndexOf(".");

    if (suffixPointIdx != -1) {
      this.fileSuffix = this.fileName.substring(suffixPointIdx);
    }
  }

  public void setInputStream(InputStream inputStream) {
    try {
      if (path != null) {
        this.inputStream = Files.newInputStream(path);
      }
    } catch (IOException e) {
      e.printStackTrace();
      return;
    }

    this.inputStream = inputStream;
  }

  public InputStream getInputStream() {
    try {
      if (path != null) {
        return Files.newInputStream(path);
      }
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }

    return inputStream;
  }

  @Override
  public void close() throws IOException {
    if (inputStream != null) {
      inputStream.close();
    }
  }

  @Override
  public String toString() {
    return "FileBody{" +
      "path=" + path +
      ", file=" + file +
      ", fileAbsolutePath='" + fileAbsolutePath + '\'' +
      ", pathRoot='" + pathRoot + '\'' +
      ", fileRelativePath='" + fileRelativePath + '\'' +
      ", fileName='" + fileName + '\'' +
      ", fileSuffix='" + fileSuffix + '\'' +
      '}';
  }
}
