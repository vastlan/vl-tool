package com.vast.vl_tool.file;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.hibernate.query.criteria.internal.PathSource;
import org.springframework.core.io.PathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author vastlan
 * @description
 * @created 2022/1/21 11:53
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

  public static String uploadFile(String targetPath, MultipartFile file) {
    File convertFile = new File(targetPath);

    if(convertFile.exists() && convertFile.isFile()) {
      convertFile.delete();
    } else {
      File createdFile = createFile(convertFile);

      if (createdFile == null) {
        return "";
      }
    }

    try (FileOutputStream fileOutputStream = new FileOutputStream(convertFile)) {
      fileOutputStream.write (file.getBytes());
    } catch (Exception e) {
      e.printStackTrace();
      return "";
    }

    return targetPath;
  }

  public static boolean deleteFile(String filePath) {
    File file = new File(filePath);

    return deleteFile(file);
  }

  public static boolean deleteFile(String targetPath, String fileName) {
    File file = new File(targetPath + fileName);

    return deleteFile(file);
  }

  public static boolean deleteFile(File file) {
    boolean result = false;

    if (file.exists() && file.isFile()) {
      file.delete();

      result = true;
    }

    return result;
  }

  /**
   *
   * @param targetPath
   * @param fileName 文件名.拓展名
   * @return
   * @throws IOException
   */
  public static ResponseEntity<byte[]> downloadFile(String targetPath, String fileName) throws IOException {
    return downloadFile(FileBody.create(targetPath, fileName));
  }

  public static ResponseEntity<byte[]> downloadFile(FileBody fileBody) throws IOException {
    if (!fileBody.existFile()) {
      throw new FileNotFoundException("找不到指定文件");
    }

    InputStream inputStream= new FileInputStream(fileBody.getFile());
    byte[] body = new byte[inputStream.available()];

    String newFileName = new String(fileBody.getFileName().getBytes("UTF-8"), "ISO-8859-1");

    HttpHeaders headers = new HttpHeaders();
    headers.add ( "Content-Disposition","attachment;filename=" + newFileName);
    ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(body, headers, HttpStatus.OK);

    return responseEntity;
  }

  public static ResponseEntity<byte[]> downloadFileWithZip(List<FileBody> fileList, String zipFileName) throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);

    byte[] bytes = new byte[1024];

    for (FileBody fileBody : fileList) {
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

  /***
   * 获取视频缩略图
   * @param videoPath 视频目录
   * @param thumbnailPath 储存缩略图路径
   * @param frameNumber 截取帧数
   * @return 返回缩略图储存的绝对路径
   */
  public static String grabVideoThumbnail(String videoPath, String thumbnailPath, int frameNumber) {
    return grabVideoThumbnail(new File(videoPath), thumbnailPath, frameNumber);
  }

  public static String grabVideoThumbnail(File videoFile, String thumbnailPath, int frameNumber) {
    File thumbnailFile = isFileExist(thumbnailPath);

    if (thumbnailFile == null && (thumbnailFile = createFile(thumbnailPath)) == null) {
      return "";
    }

    return grabVideoThumbnail(videoFile, thumbnailFile, frameNumber);
  }

  public static String grabVideoThumbnail(File videoFile, File thumbnailFile, int frameNumber) {
    String result = "";

    FFmpegFrameGrabber videoGrabber = new FFmpegFrameGrabber(videoFile);

    if (videoGrabber == null) {
      return "";
    }

    try {
      videoGrabber.start();

      Frame grabbedFrame = null;

      int videoTotalFrameNumber = videoGrabber.getLengthInFrames();
      int currentFrameNumber = 0;

      while (currentFrameNumber < videoTotalFrameNumber) {
        if (currentFrameNumber >= videoTotalFrameNumber) {
          return result;
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

      ImageIO.write(thumbnailBufferedImage, "jpg", thumbnailFile);
      result = thumbnailFile.getAbsolutePath();

    } catch (FFmpegFrameGrabber.Exception e) {
      e.printStackTrace();
      return result;
    } catch (FrameGrabber.Exception e) {
      e.printStackTrace();
      return result;
    } catch (IOException e) {
      e.printStackTrace();
      return result;
    } finally {
      try {
        videoGrabber.stop();
      } catch (FFmpegFrameGrabber.Exception e) {
        e.printStackTrace();
      }
    }

    return result;
  }

  public static String grabVideoThumbnail(String videoPath, String thumbnailPath) {
    return grabVideoThumbnail(videoPath, thumbnailPath, DEFAULT_CUT_OUT_VIDEO_FRAME_NUMBER);
  }

  public static String grabVideoThumbnail(File videoFile, String thumbnailPath) {
    return grabVideoThumbnail(videoFile, thumbnailPath, DEFAULT_CUT_OUT_VIDEO_FRAME_NUMBER);
  }

  public static File isFileExist(String targetPath) {
    File file = new File(targetPath);

    return file.exists() ? file : null;
  }

  public static File createFile(String filePath) {
    File file = new File(filePath);
    return createFile(file);
  }

  public static File createFile(File fileSource) {
    File parentFile = fileSource.getParentFile();

    if (!parentFile.exists()) {
      parentFile.mkdirs();
    }

    if (fileSource.isFile() && !fileSource.exists()) {
      try {
        fileSource.createNewFile();
      } catch(IOException e) {
        e.printStackTrace();
        return null;
      }
    }

    return fileSource;
  }

  public static PathResource transformToPathSource(String absolutePath) {
    Path path = Paths.get(absolutePath);
    return new PathResource(path);
  }
}
