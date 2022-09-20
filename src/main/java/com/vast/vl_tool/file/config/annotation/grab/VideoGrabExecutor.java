package com.vast.vl_tool.file.config.annotation.grab;

import com.vast.vl_tool.exception.AssertTool;
import com.vast.vl_tool.file.entity.FileBody;
import com.vast.vl_tool.file.FileTool;
import com.vast.vl_tool.time.DateTool;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author vastlan
 * @description
 * @created 2022/7/28 16:06
 */

public class VideoGrabExecutor extends AbstractIOGrabExecutor {

  private FileBody fileBody;

  /** 忽略截取帧数 */
  private Integer frameNumber = 5;

  private Integer hints = Image.SCALE_SMOOTH;

  private Double width = 0.5;

  private Double height = 0.5;

  public VideoGrabExecutor(FileBody fileBody, String targetPath) {
    super(fileBody, targetPath);
    this.fileBody = fileBody;
  }

  public VideoGrabExecutor frame(Integer frameNumber) {
    this.frameNumber = frameNumber;
    return this;
  }

  public VideoGrabExecutor width(Double width) {
    this.width = width;
    return this;
  }

  public VideoGrabExecutor height(Double height) {
    this.height = height;
    return this;
  }

  public VideoGrabExecutor hints(Integer hints) {
    this.hints = hints;
    return this;
  }

  @Override
  public void execute() throws IOException {
    AssertTool.isTrue(width < 0 || width > 1, new IllegalArgumentException("图片宽度比例 width 参数区间为 0-1"));
    AssertTool.isTrue(height < 0 || height > 1, new IllegalArgumentException("图片高度比例 height 参数区间为 0-1"));

    FFmpegFrameGrabber videoGrabber = new FFmpegFrameGrabber(fileBody.getFile());

    if (videoGrabber == null) {
      throw new IOException("解析文件源异常");
    }

    try {
      videoGrabber.start();

      Frame grabbedFrame = null;

      int videoTotalFrameNumber = videoGrabber.getLengthInFrames();
      int currentFrameNumber = 0;

      while (currentFrameNumber < videoTotalFrameNumber) {
        if (currentFrameNumber >= videoTotalFrameNumber) {
          throw new IOException("获取帧数异常");
        }

        grabbedFrame = videoGrabber.grabFrame();

        if (currentFrameNumber > frameNumber && grabbedFrame != null && grabbedFrame.image != null) {
          break;
        }

        currentFrameNumber++;
      }

      Java2DFrameConverter frameConverter = new Java2DFrameConverter();
      BufferedImage frameBufferedImage = frameConverter.getBufferedImage(grabbedFrame);
      int frameImagePrimaryWidth = frameBufferedImage.getWidth();
      int frameImagePrimaryHeight = frameBufferedImage.getHeight();

      int thumbnailWidth = (int) Math.floor(frameImagePrimaryWidth * width);
      int thumbnailHeight = (int) Math.floor(frameImagePrimaryHeight * height);

      BufferedImage thumbnailBufferedImage = new BufferedImage(thumbnailWidth, thumbnailHeight, BufferedImage.TYPE_3BYTE_BGR);
      thumbnailBufferedImage
        .getGraphics()
        .drawImage(frameBufferedImage.getScaledInstance(thumbnailWidth, thumbnailHeight, hints), 0, 0, null);

      FileBody targetFileBody = FileBody.create(targetPath);

      if (!targetFileBody.isFile()) {
        String videoFileName = this.fileBody.getFileName();

        if (!StringUtils.hasLength(grabbedFileName)) {
          grabbedFileName(String.format("%s_thumbnail_%s_%d", DateTool.getFormattedCurrentDateTime("yyyyMMddHHmmss"), videoFileName.substring(0, videoFileName.lastIndexOf(".")), frameNumber));
        }

        targetFileBody = FileBody.create(targetPath + File.separator + getGrabbedFileName());
      }

      FileBody thumbnailFileBody =
        FileTool.create()
          .createFile(targetFileBody)
          .invoke();

      ImageIO.write(thumbnailBufferedImage, "jpg", thumbnailFileBody.getFile());

      setGrabResult(thumbnailFileBody);

    } finally {
      try {
        videoGrabber.stop();
      } catch (FFmpegFrameGrabber.Exception e) {
        e.printStackTrace();
      }
    }
  }
}
