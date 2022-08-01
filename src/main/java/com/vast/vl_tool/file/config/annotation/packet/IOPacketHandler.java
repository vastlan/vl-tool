package com.vast.vl_tool.file.config.annotation.packet;

import com.vast.vl_tool.file.entity.FileBody;
import com.vast.vl_tool.file.config.annotation.AbstractIOHandler;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.List;

/**
 * @author vastlan
 * @description
 * @use
 *  tool
 *    .packet()
 *    .compress() / .decompress()
 *    .content(xxx)
 *    .target(xxx)
 *    .zip() / .rar()
 *    .invoke()
 * @created 2022/7/27 10:26
 */
public class IOPacketHandler extends AbstractIOHandler<List<FileBody>> {

  private Boolean isCompress;

  private IOPacketExecutor ioPacketExecutor;

  /** 压缩后存放路径 */
  private String targetPath;

  public IOPacketHandler compress() {
    isCompress = true;
    return this;
  }

  public IOPacketHandler decompress() {
    isCompress = false;
    return this;
  }

  public IOPacketHandler content(List<FileBody> fileBodyList) {
    setBody(fileBodyList);
    return this;
  }

  public IOPacketHandler target(String targetPath) {
    this.targetPath = targetPath;
    return this;
  }

  public IOPacketHandler zip() {
    if (isCompress) {
      ioPacketExecutor = new ZipCompressExecutor(body, targetPath);
    } else {
      ioPacketExecutor = new ZipDecompressExecutor((FileBody) body, targetPath);
    }

    return this;
  }

  public IOPacketHandler rar() {
    if (isCompress) {
      Assert.notNull(ioPacketExecutor, "rar压缩功能暂未实现");
    } else {
      ioPacketExecutor = new RarDecompressExecutor((FileBody) body, targetPath);
    }

    return this;
  }

  @Override
  protected void checkParamValidity() {
    super.checkParamValidity();
    Assert.notNull(isCompress, "未确定解压还是压缩目标内容，请选择 compress() 或 decompress()");
  }

  @Override
  public List<FileBody> handle() throws IOException {
    if (ioPacketExecutor == null) {
      zip();
    }

    return isCompress ? handleCompress() : handleDecompress();
  }

  private List<FileBody> handleCompress() throws IOException {
    ioPacketExecutor.action();
    return (List<FileBody>) ioPacketExecutor.actionSuccessfulResult();
  }

  private List<FileBody> handleDecompress() throws IOException {
    ioPacketExecutor.action();
    return (List<FileBody>) ioPacketExecutor.actionSuccessfulResult();
  }
}