package com.vast.vl_tool.file.entity;

import com.vast.vl_tool.file.FileTool;
import lombok.*;

import java.io.File;
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
public class FileBody {

  private Path path;

  private File file;

  private String fileAbsolutePath;

  private String pathRoot;

  private String fileRelativePath;

  private String fileName;

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
    return !existFile() && FileTool.isFile(file);
  }

  private void init() {
    this.fileAbsolutePath = path.toAbsolutePath().toString();
    this.pathRoot = path.getRoot().toString();
    this.fileRelativePath = fileAbsolutePath.replace(pathRoot, "\\");
    this.fileName = path.getFileName().toString();
  }
}
