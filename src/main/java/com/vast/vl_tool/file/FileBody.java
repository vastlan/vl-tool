package com.vast.vl_tool.file;

import lombok.*;
import org.aspectj.util.FileUtil;

import javax.persistence.Transient;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

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

  private File file;

  private String fileName;

  private String fileAbsolutePath;

  private Double latitude;

  private Double longitude;

  public FileBody(File file, String fileName, String fileAbsolutePath) {
    this.file = file;
    this.fileName = fileName;
    this.fileAbsolutePath = fileAbsolutePath;
  }

  public static FileBody create(String fileAbsolutePath) {
    File file = new File(fileAbsolutePath);
    return new FileBody(file, file.getName(), file.getAbsolutePath());
  }

  public static FileBody create(File file) {
    return new FileBody(file, file.getName(), file.getAbsolutePath());
  }

  public static FileBody create(String folderAbsolutePath, String fileName) {
    String path = folderAbsolutePath + "/" + fileName;
    File file = new File(path);
    return new FileBody(file, fileName, path);
  }

  public boolean existFile() {
    return file.exists();
  }

  public boolean notExistAndIsFile() {
    return !existFile() && FileTool.isFile(file);
  }
}
