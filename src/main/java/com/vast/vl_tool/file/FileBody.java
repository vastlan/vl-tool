package com.vast.vl_tool.file;

import lombok.*;

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

  public static FileBody create(String targetPath) {
    File file = new File(targetPath);
    return new FileBody(file, file.getName());
  }

  public static FileBody create(File file) {
    return new FileBody(file, file.getName());
  }

  public static FileBody create(String targetPath, String fileName) {
    File file = new File(targetPath + fileName);
    return new FileBody(file, fileName);
  }

  public boolean existFile() {
    return file.exists();
  }
}
