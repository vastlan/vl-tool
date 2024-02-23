package com.vast.vl_tool.file;

import com.vast.vl_tool.file.config.annotation.create.IOCreationHandler;
import com.vast.vl_tool.file.config.annotation.download.IODownloadHandler;
import com.vast.vl_tool.file.config.annotation.format.IOFormatHandler;
import com.vast.vl_tool.file.config.annotation.grab.IOGrabHandler;
import com.vast.vl_tool.file.config.annotation.packet.IOPacketHandler;
import com.vast.vl_tool.file.config.annotation.upload.IOUploadHandler;
import com.vast.vl_tool.file.config.annotation.upload.OssUploadHandler;
import com.vast.vl_tool.file.entity.FileBody;
import com.vast.vl_tool.time.DateTool;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

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

//  public static void main(String[] args) throws Exception {
//    final Pattern URL_PATTERN = Pattern.compile("^[a-z]+:.*");
//    String url = "https://192.168.1.35:3090/media/thumbnail/picture/20240118043912_thumbnail_20240118043912_DJI_0244.jpg";
//    System.out.println(url.substring(0, 10));
//    System.out.println(URL_PATTERN.matcher(url.substring(0, 10).trim()).matches());
//    System.out.println(Pattern.matches("^[a-z]+:.*", "https://123.123.123.123"));
//  }
}
