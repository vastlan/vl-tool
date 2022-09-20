package com.vast.vl_tool.file.config.annotation.grab;

import com.vast.vl_tool.exception.AssertTool;
import com.vast.vl_tool.file.entity.FileBody;
import com.vast.vl_tool.file.FileTool;
import com.vast.vl_tool.time.DateTool;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author vastlan
 * @description
 * @created 2022/7/28 14:21
 */

public class PictureGrabExecutor extends AbstractIOGrabExecutor {

  private FileBody fileBody;

  /** 图片大小（长宽）压缩比例 从0-1，1表示原图 */
  private Double scale = 0.5;

  /** 图片质量压缩比例 从0-1，越接近1质量越好 */
  private Double outputQuality = 0.5;

  public PictureGrabExecutor(FileBody fileBody, String targetPath) {
    super(fileBody, targetPath);
    this.fileBody = fileBody;
  }

  public PictureGrabExecutor scale(Double scale) {
    this.scale = scale;
    return this;
  }

  public PictureGrabExecutor outputQuality(Double outputQuality) {
    this.outputQuality = outputQuality;
    return this;
  }

  @Override
  public void execute() throws IOException {
    AssertTool.isTrue(scale < 0 || scale > 1, new IllegalArgumentException("图片大小 scale 参数区间为 0-1"));
    AssertTool.isTrue(outputQuality < 0 || outputQuality > 1, new IllegalArgumentException("图片质量压缩比例 outputQuality 参数区间为 0-1"));

    if (!StringUtils.hasLength(grabbedFileName)) {
      grabbedFileName(String.format("%s_thumbnail_%s", DateTool.getFormattedCurrentDateTime("yyyyMMddHHmmss"), fileBody.getFileName()));
    }

    FileBody thumbnailFileBody = FileBody.create(targetPath);

    if (!thumbnailFileBody.isFile()) {
      thumbnailFileBody = FileBody.create(targetPath + File.separator + getGrabbedFileName());
    }

    if (thumbnailFileBody.notExistAndIsFile()) {
      FileTool.create().createFile(thumbnailFileBody).invoke();
    }

    FileOutputStream fileOutputStream = null;

    try {
      fileOutputStream = new FileOutputStream(thumbnailFileBody.getFile());

      Thumbnails.of(fileBody.getFile())
        .scale(scale)
        .outputQuality(outputQuality)
        .toOutputStream(fileOutputStream);

      setGrabResult(thumbnailFileBody);
    } finally {
      if (fileOutputStream != null) {
        fileOutputStream.close();
      }
    }
  }
}
