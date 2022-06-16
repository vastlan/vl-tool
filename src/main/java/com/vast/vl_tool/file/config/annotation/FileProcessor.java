package com.vast.vl_tool.file.config.annotation;

import com.vast.vl_tool.exception.AssertTool;
import com.vast.vl_tool.file.FileBody;
import com.vast.vl_tool.file.FileTool;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author vastlan
 * @description
 * @created 2022/6/10 14:25
 */
public class FileProcessor {

  private static final String NULL_FILE_BODY_ERROR_MESSAGE = "fileBody 不能为空";

  /***
   * 默认截取视频帧数
   */
  private final static int DEFAULT_CUT_OUT_VIDEO_FRAME_NUMBER = 5;

  private FileBody fileBody;

  private List<FileBody> fileBodyList;

  /**
   * @param fileAbsolutePath 文件绝对路径
   * @return
   */
  public FileProcessor path(String fileAbsolutePath) {
    this.fileBody = FileBody.create(fileAbsolutePath);
    return this;
  }

  public FileProcessor path(File file) {
    this.fileBody = FileBody.create(file);
    return this;
  }

  public FileProcessor path(FileBody fileBody) {
    this.fileBody = fileBody;
    return this;
  }

  /**
   * @param folderAbsolutePath 文件夹路径
   * @param fileName 文件名称
   * @return
   */
  public FileProcessor path(String folderAbsolutePath, String fileName) {
    this.fileBody = FileBody.create(folderAbsolutePath, fileName);
    return this;
  }

  /**
   * @param source 文件盘符
   * @param folderRelativePath 文件夹相对路径
   * @param fileName
   * @return
   */
  public FileProcessor path(String source, String folderRelativePath, String fileName) {
    this.fileBody = FileBody.create(source + "/" + folderRelativePath + "/" + fileName);
    return this;
  }

  public FileProcessor multiPath(List<FileBody> fileBodyList) {
    this.fileBodyList = fileBodyList;
    return this;
  }

  public Boolean exist() {
    AssertTool.isNull(this.fileBody, new NullPointerException(NULL_FILE_BODY_ERROR_MESSAGE));
    return fileBody.existFile();
  }

  public Boolean delete() {
    AssertTool.isNull(this.fileBody, new NullPointerException(NULL_FILE_BODY_ERROR_MESSAGE));

    File file = fileBody.getFile();

    if(fileBody.existFile() && file.isFile()) {
      return file.delete();
    }

    System.out.println(file.getName() + "文件不存在");
    return false;
  }

  public FileBody createFile() {
    AssertTool.isNull(this.fileBody, new NullPointerException(NULL_FILE_BODY_ERROR_MESSAGE));

    File fileSource = fileBody.getFile();
    AssertTool.isTrue(!FileTool.isFile(fileBody.getFile()), new IllegalArgumentException("非文件路径参数异常"));

    File parentFolder = createFolder(fileSource.getParentFile());

    if (parentFolder == null) {
      return null;
    }

    if (fileBody.notExistAndIsFile()) {
      try {
        if (fileSource.createNewFile()) {
          return fileBody;
        }
      } catch(IOException e) {
        e.printStackTrace();
      }
    }

    return null;
  }

  public File createFolder() {
    AssertTool.isNull(this.fileBody, new NullPointerException(NULL_FILE_BODY_ERROR_MESSAGE));

    File fileSource = fileBody.getFile();
    AssertTool.isTrue(FileTool.isFile(fileBody.getFile()), new IllegalArgumentException("非文件夹路径参数异常"));

    return createFolder(fileSource);
  }

  public File createFolder(File folderFile) {
    AssertTool.isNull(folderFile, new NullPointerException(NULL_FILE_BODY_ERROR_MESSAGE));

    if (!folderFile.exists()) {
      folderFile.mkdirs();
    }

    return folderFile;
  }

  /**
   * @param fileSource
   * @return 返回文件的存储路径
   */
  public FileBody coverAndUpload(MultipartFile fileSource) {
    AssertTool.isNull(this.fileBody, new NullPointerException(NULL_FILE_BODY_ERROR_MESSAGE));

    File convertFile = fileBody.getFile();

    if(!fileBody.existFile()) {
      FileBody fileBody = createFile();
      convertFile = fileBody != null ? fileBody.getFile() : null;
    }

    if (convertFile == null) {
      return null;
    }

    try (FileOutputStream fileOutputStream = new FileOutputStream(convertFile)) {
      fileOutputStream.write (fileSource.getBytes());
      return fileBody;
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }

  public FileBody upload(MultipartFile fileSource) {
    AssertTool.isNull(this.fileBody, new NullPointerException(NULL_FILE_BODY_ERROR_MESSAGE));

    File file = fileBody.getFile();

    if (!fileBody.existFile()) {
      return coverAndUpload(fileSource);
    }

    File parentFile = file.getParentFile();
    File[] existedFile = parentFile.listFiles();

    String fileName = file.getName();
    String finalFileName = fileName.substring(0, fileName.lastIndexOf("."));;
    int num = (int) Arrays.stream(existedFile).map(File::getName).filter(name -> name.indexOf(finalFileName) != -1).count();

    String folderAbsolutePath = parentFile.getAbsolutePath();
    fileBody = FileBody.create(folderAbsolutePath, finalFileName + "-副本" + num + fileName.substring(fileName.lastIndexOf(".") + 1));

    return createFile();
  }

  public ResponseEntity<byte[]> download() throws IOException {
    AssertTool.isNull(this.fileBody, new NullPointerException(NULL_FILE_BODY_ERROR_MESSAGE));

    if (!exist()) {
      new FileNotFoundException("找不到指定文件");
    }

    InputStream inputStream= new FileInputStream(fileBody.getFile());
    byte[] body = new byte[inputStream.available()];

    String newFileName = new String(fileBody.getFileName().getBytes("UTF-8"), "ISO-8859-1");

    HttpHeaders headers = new HttpHeaders();
    headers.add ( "Content-Disposition","attachment;filename=" + newFileName);
    ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(body, headers, HttpStatus.OK);

    return responseEntity;
  }

  public ResponseEntity<byte[]> downloadFileWithZip() throws IOException {
    return downloadFileWithZip("DataPackage");
  }

  public ResponseEntity<byte[]> downloadFileWithZip(String zipFileName) throws IOException {
    AssertTool.isTrue(fileBodyList == null || fileBodyList.isEmpty(), new IllegalArgumentException("fileBodyList 不能为空"));

    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);

    byte[] bytes = new byte[1024];

    for (FileBody fileBody : this.fileBodyList) {
      try(FileInputStream fileInputStream = new FileInputStream(fileBody.getFile())) {
        zipOutputStream.putNextEntry(new ZipEntry(fileBody.getFileName()));

        int len;

        while ((len = fileInputStream.read(bytes) ) > 0) {
          zipOutputStream.write(bytes, 0, len);
        }
      }

      zipOutputStream.closeEntry();
    }

    String newFileName = new String(zipFileName.getBytes("UTF-8"), "ISO-8859-1");

    HttpHeaders headers = new HttpHeaders();
    headers.add ( "Content-Disposition","attachment;filename=" + newFileName + ".zip");
    ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(byteArrayOutputStream.toByteArray(), headers, HttpStatus.OK);

    zipOutputStream.close();

    return responseEntity;
  }

  public FileBody grabVideoThumbnail(String targetAbsolutePath) {
    return grabVideoThumbnail(targetAbsolutePath, DEFAULT_CUT_OUT_VIDEO_FRAME_NUMBER);
  }

  /***
   * 获取视频缩略图
   * @param targetAbsolutePath 储存缩略图绝对路径
   * @param frameNumber 截取帧数
   * @return 返回缩略图储存的绝对路径
   */
  public FileBody grabVideoThumbnail(String targetAbsolutePath, int frameNumber) {
    AssertTool.isNull(this.fileBody, new NullPointerException(NULL_FILE_BODY_ERROR_MESSAGE));
    AssertTool.isTrue(!FileTool.isVideo(fileBody.getFileName()), new IllegalArgumentException("非视频文件异常"));

    FileBody thumbnailFileBody = null;

    FFmpegFrameGrabber videoGrabber = new FFmpegFrameGrabber(fileBody.getFile());

    if (videoGrabber == null) {
      return thumbnailFileBody;
    }

    try {
      videoGrabber.start();

      Frame grabbedFrame = null;

      int videoTotalFrameNumber = videoGrabber.getLengthInFrames();
      int currentFrameNumber = 0;

      while (currentFrameNumber < videoTotalFrameNumber) {
        if (currentFrameNumber >= videoTotalFrameNumber) {
          return thumbnailFileBody;
        }

        if (currentFrameNumber < frameNumber) {
          currentFrameNumber++;
          continue;
        }

        grabbedFrame = videoGrabber.grabFrame();

        if (grabbedFrame != null && grabbedFrame.image != null) {
          break;
        }
      }

      Java2DFrameConverter frameConverter = new Java2DFrameConverter();
      BufferedImage frameBufferedImage = frameConverter.getBufferedImage(grabbedFrame);
      int frameImagePrimaryWidth = frameBufferedImage.getWidth();
      int frameImagePrimaryHeight = frameBufferedImage.getHeight();

      int thumbnailWidth = (int) Math.floor(frameImagePrimaryWidth * 0.5);
      int thumbnailHeight = (int) Math.floor(frameImagePrimaryHeight * 0.5);

      BufferedImage thumbnailBufferedImage = new BufferedImage(thumbnailWidth, thumbnailHeight, BufferedImage.TYPE_3BYTE_BGR);
      thumbnailBufferedImage
        .getGraphics()
        .drawImage(frameBufferedImage.getScaledInstance(thumbnailWidth, thumbnailHeight, Image.SCALE_SMOOTH), 0, 0, null);

      thumbnailFileBody = FileBody.create(targetAbsolutePath);

      ImageIO.write(thumbnailBufferedImage, "jpg", thumbnailFileBody.getFile());

    } catch (FFmpegFrameGrabber.Exception e) {
      e.printStackTrace();
    } catch (FrameGrabber.Exception e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        videoGrabber.stop();
      } catch (FFmpegFrameGrabber.Exception e) {
        e.printStackTrace();
      }
    }

    return thumbnailFileBody;
  }

}
