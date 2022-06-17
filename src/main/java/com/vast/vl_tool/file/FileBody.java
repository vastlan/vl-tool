package com.vast.vl_tool.file;

import lombok.*;

import javax.persistence.Transient;
import java.io.File;

/**
 * @author vastlan
 * @description
 * @created 2022/5/31 17:11
 */

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FileBody {

  private File file;

  private String fileName;

  private String fileAbsolutePath;

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
