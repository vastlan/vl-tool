package com.vast.vl_tool.file;

import com.vast.vl_tool.file.config.annotation.FileProcessor;
import org.springframework.core.io.PathResource;
import org.springframework.util.StringUtils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * @author vastlan
 * @description
 * @created 2022/6/10 14:17
 */
public class FileTool {

  /***
   * 默认截取视频帧数
   */
  private final static int DEFAULT_CUT_OUT_VIDEO_FRAME_NUMBER = 5;

  public static boolean isPicture(String fileName) {
    String[] pictureSuffixArray = new String[] {
      "jpg", "jpeg", "png", "bmp", "gif", "tif"
    };

    return Arrays.stream(pictureSuffixArray)
      .filter(item -> fileName.indexOf(item) != -1)
      .findAny()
      .orElse(null) != null;
  }

  public static boolean isVideo(String fileName) {
    String[] videoSuffixArray = new String[] {
      "mp4", "flv", "wmv"
    };

    return Arrays.stream(videoSuffixArray)
      .filter(item -> fileName.indexOf(item) != -1)
      .findAny()
      .orElse(null) != null;
  }

  public static Boolean isFile(File file) {
    if (file.exists()) {
      return file.isFile();
    }

    String fileName = file.getName();
    return StringUtils.hasLength(fileName) && fileName.indexOf(".") != -1;
  }


  /**
   * FileTool
   *  .createFileProcessor()
   *  .path(xxx)
   *  .upload(xxx)
   * @return
   */
  public static FileProcessor createFileProcessor() {
    return new FileProcessor();
  }

  public static PathResource transformToPathSource(String absolutePath) {
    Path path = Paths.get(absolutePath);
    return new PathResource(path);
  }
}
