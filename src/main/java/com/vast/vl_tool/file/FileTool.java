package com.vast.vl_tool.file;

import com.vast.vl_tool.file.config.annotation.create.IOCreationHandler;
import com.vast.vl_tool.file.config.annotation.download.IODownloadHandler;
import com.vast.vl_tool.file.config.annotation.format.IOFormatHandler;
import com.vast.vl_tool.file.config.annotation.grab.IOGrabHandler;
import com.vast.vl_tool.file.config.annotation.packet.IOPacketHandler;
import com.vast.vl_tool.file.config.annotation.upload.IOUploadHandler;
import com.vast.vl_tool.file.entity.FileBody;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author vastlan
 * @description
 * @created 2022/7/25 17:40
 */
public class FileTool {

  public static boolean isPicture(String fileName) {
    String[] pictureSuffixArray = new String[] {
      "jpg", "jpeg", "png", "bmp", "gif", "tif"
    };

    return Arrays.stream(pictureSuffixArray)
      .filter(item -> fileName.toLowerCase().indexOf(item) != -1)
      .findAny()
      .orElse(null) != null;
  }

  public static boolean isVideo(String fileName) {
    String[] videoSuffixArray = new String[] {
      "mp4", "flv", "wmv"
    };

    return Arrays.stream(videoSuffixArray)
      .filter(item -> fileName.toLowerCase().indexOf(item) != -1)
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

  public static IOCreationHandler create() {
    return new IOCreationHandler();
  }

  public static IOUploadHandler upload(MultipartFile multipartFile) {
    return new IOUploadHandler(multipartFile);
  }

  public static IODownloadHandler download(HttpServletResponse httpServletResponse) {
    return new IODownloadHandler(httpServletResponse);
  }

  public static IOPacketHandler packet() {
    return new IOPacketHandler();
  }

  public static IOGrabHandler grab() {
    return new IOGrabHandler();
  }

  public static IOFormatHandler format() {
    return new IOFormatHandler();
  }

  public static void main(String[] args) throws IOException {
    System.out.println(FileTool.format().content(FileBody.create("D:\\resource\\20220623052541_DJI_0557.jpg")).toEXIF().invoke());
  }
}
