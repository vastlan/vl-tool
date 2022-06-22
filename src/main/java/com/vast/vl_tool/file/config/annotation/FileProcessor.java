package com.vast.vl_tool.file.config.annotation;

import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.rarfile.FileHeader;
import com.vast.vl_tool.exception.AssertTool;
import com.vast.vl_tool.file.FileBody;
import com.vast.vl_tool.file.FileTool;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.tomcat.util.http.fileupload.IOUtils;
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
import java.lang.annotation.Documented;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
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
    return createFile(fileBody.getFile());
  }

  public FileBody createFile(File fileSource) {
    AssertTool.isTrue(!FileTool.isFile(fileSource), new IllegalArgumentException("非文件路径参数异常"));

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

  /**
   * 多文件下载
   * @return
   * @throws IOException
   */
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

  /**
   * 解压zip文件
   * @param absoluteFolderPath 解压文件夹存储绝对路径
   * @return
   */
  public FileBody unzip(String absoluteFolderPath) {
    AssertTool.isNull(this.fileBody, new NullPointerException(NULL_FILE_BODY_ERROR_MESSAGE));
    AssertTool.isNull(this.fileBody, new NullPointerException("压缩文件存储文件夹绝对路径不能为空"));
    AssertTool.isTrue(!fileBody.existFile(), new IllegalArgumentException("文件不存在"));

    try {
      // 系统编码
      // Charset.forName(System.getProperty("sun.jnu.encoding") 获取系统默认编码格式
      // 若无设置 Charset 且压缩包名称为中文名，报 java.util.zip.ZipException: invalid CEN header (bad entry name)
      ZipFile zipFile = new ZipFile(fileBody.getFile(), Charset.forName(System.getProperty("sun.jnu.encoding")));

      // 开始解压
      Enumeration<? extends ZipEntry> entries = zipFile.entries();

      while (entries.hasMoreElements()) {
        ZipEntry entry = (ZipEntry) entries.nextElement();

        // 如果是文件夹，就创建个文件夹
        if (entry.isDirectory()) {
          createFolder(fileBody.getFile());
          continue;
        }

        // 如果是文件，就先创建一个文件，然后用io流把内容copy过去
        File targetFile = new File(absoluteFolderPath + "/" + entry.getName());

        // 保证这个文件的父文件夹必须要存在
        if (!targetFile.getParentFile().exists()) {
          targetFile.getParentFile().mkdirs();
        }

        if (targetFile.isFile() && !targetFile.exists()) {
          targetFile.createNewFile();
        }

        try (FileOutputStream fos = new FileOutputStream(targetFile);
             InputStream is = zipFile.getInputStream(entry)) {
          IOUtils.copy(is, fos);
        }
      }

      return FileBody.create(absoluteFolderPath);
    } catch (ZipException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return null;
  }

  /**
   * 解压rar文件
   * @param absoluteFolderPath
   * @return
   */
  public FileBody unrar(String absoluteFolderPath) {
    AssertTool.isNull(this.fileBody, new NullPointerException(NULL_FILE_BODY_ERROR_MESSAGE));
    AssertTool.isNull(this.fileBody, new NullPointerException("压缩文件存储文件夹绝对路径不能为空"));
    AssertTool.isTrue(!fileBody.existFile(), new IllegalArgumentException("文件不存在"));

    FileBody absoluteFolderPathFileBody = FileBody.create(absoluteFolderPath);

    if (!absoluteFolderPathFileBody.existFile()) {
      createFolder(absoluteFolderPathFileBody.getFile());
    }

    try(Archive archive = new Archive(fileBody.getFile())) {

      if (archive == null) {
        return null;
      }

      // 打印文件信息
      archive.getMainHeader().print();

      FileHeader fileHeader;

      while ((fileHeader = archive.nextFileHeader()) != null) {
        FileBody dirFileBody = FileBody.create(absoluteFolderPath + File.separator + fileHeader.getFileNameString());
        File file = dirFileBody.getFile();

        // 如果是文件夹
        if (fileHeader.isDirectory()) {
          createFolder(file);
          continue;
        }

        if (dirFileBody.notExistAndIsFile()) {
          createFile(file);
        }

        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
          archive.extractFile(fileHeader, fileOutputStream);
        }
      }

      return FileBody.create(absoluteFolderPath);

    } catch (IOException e) {
      e.printStackTrace();
    } catch (RarException e) {
      e.printStackTrace();
    }

    return null;
  }

  public FileBody grabVideoThumbnail(String targetAbsoluteFilePath) {
    return grabVideoThumbnail(targetAbsoluteFilePath, DEFAULT_CUT_OUT_VIDEO_FRAME_NUMBER);
  }

  /***
   * 获取视频缩略图
   * @param targetAbsoluteFilePath 储存缩略图绝对路径
   * @param frameNumber 截取帧数
   * @return 返回缩略图储存的绝对路径
   */
  public FileBody grabVideoThumbnail(String targetAbsoluteFilePath, int frameNumber) {
    AssertTool.isNull(this.fileBody, new NullPointerException(NULL_FILE_BODY_ERROR_MESSAGE));
    AssertTool.isTrue(!FileTool.isVideo(fileBody.getFileName()), new IllegalArgumentException("非视频文件异常"));

    FFmpegFrameGrabber videoGrabber = new FFmpegFrameGrabber(fileBody.getFile());

    if (videoGrabber == null) {
      return null;
    }

    try {
      videoGrabber.start();

      Frame grabbedFrame = null;

      int videoTotalFrameNumber = videoGrabber.getLengthInFrames();
      int currentFrameNumber = 0;

      while (currentFrameNumber < videoTotalFrameNumber) {
        if (currentFrameNumber >= videoTotalFrameNumber) {
          return null;
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

      FileBody thumbnailFileBody = path(targetAbsoluteFilePath).createFile();

      ImageIO.write(thumbnailBufferedImage, "jpg", thumbnailFileBody.getFile());

      return thumbnailFileBody;

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

    return null;
  }

  public FileBody grabPictureThumbnail(String targetAbsoluteThumbnailFilePath) {
    return grabPictureThumbnail(targetAbsoluteThumbnailFilePath, 0.5, 0.5);
  }

  /**
   *
   * @param targetAbsoluteThumbnailFilePath
   * @param scale 图片大小（长宽）压缩比例 从0-1，1表示原图
   * @param outputQuality 图片质量压缩比例 从0-1，越接近1质量越好
   * @return
   * @throws IOException
   */
  public FileBody grabPictureThumbnail(String targetAbsoluteThumbnailFilePath, Double scale, Double outputQuality) {
    AssertTool.isNull(this.fileBody, new NullPointerException(NULL_FILE_BODY_ERROR_MESSAGE));
    AssertTool.isTrue(!FileTool.isPicture(fileBody.getFileName()), new IllegalArgumentException("非图片文件异常"));

    FileBody thumbnailFileBody = FileBody.create(targetAbsoluteThumbnailFilePath);

    if (thumbnailFileBody.notExistAndIsFile()) {
      FileTool.createFileProcessor().path(targetAbsoluteThumbnailFilePath).createFile();
    }

    FileOutputStream fileOutputStream = null;

    try {
      try {
        fileOutputStream = new FileOutputStream(thumbnailFileBody.getFile());

        Thumbnails.of(fileBody.getFile())
          .scale(scale)
          .outputQuality(outputQuality)
          .toOutputStream(fileOutputStream);
      } finally {
        if (fileOutputStream != null) {
          fileOutputStream.close();
        }
      }

      return thumbnailFileBody;
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return null;
  }

}
