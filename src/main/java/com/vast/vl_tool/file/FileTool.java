package com.vast.vl_tool.file;

import com.vast.vl_tool.file.config.annotation.create.IOCreationHandler;
import com.vast.vl_tool.file.config.annotation.download.IODownloadHandler;
import com.vast.vl_tool.file.config.annotation.format.IOFormatHandler;
import com.vast.vl_tool.file.config.annotation.grab.IOGrabHandler;
import com.vast.vl_tool.file.config.annotation.packet.IOPacketHandler;
import com.vast.vl_tool.file.config.annotation.upload.IOUploadHandler;
import com.vast.vl_tool.file.config.annotation.upload.OssUploadHandler;
import com.vast.vl_tool.file.entity.FileBody;
import org.springframework.util.StringUtils;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author vastlan
 * @description
 * @created 2022/7/25 17:40
 */
public class FileTool {

  /**
   * @param content 可以为 文件路劲、文件名
   * @return 媒体文件类型
   */
  public static String getContentType(String content) {
    try {
      return Files.probeContentType(Path.of(content));
    } catch (IOException e) {
      e.printStackTrace();
    }

    return null;
  }

  public static boolean isPicture(String content) {
    String contentType = getContentType(content);
    return contentType != null && contentType.contains("image");
  }

  public static boolean isVideo(String content) {
    String contentType = getContentType(content);
    return contentType != null && contentType.contains("video");
  }

  public static boolean isText(String content) {
    String contentType = getContentType(content);
    return contentType != null && contentType.contains("text");
  }

  public static boolean isApplication(String content) {
    String contentType = getContentType(content);
    return contentType != null && contentType.contains("application");
  }

  public static Boolean isFile(File file) {
    return isFile(FileBody.create(file));
  }

  public static Boolean isFile(Path path) {
    return isFile(FileBody.create(path));
  }

  public static Boolean isFile(FileBody fileBody) {
    String contentType = getContentType(fileBody.getFileAbsolutePath());
    return StringUtils.hasLength(contentType);
  }

  public static void delete(FileBody fileBody) throws IOException {
    Files.delete(fileBody.getPath());
  }

  public static IOCreationHandler create() {
    return new IOCreationHandler();
  }

  public static IOUploadHandler upload(InputStream inputStream) {
    return new IOUploadHandler(inputStream);
  }

  public static OssUploadHandler uploadToOss() {
    return new OssUploadHandler();
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
}
