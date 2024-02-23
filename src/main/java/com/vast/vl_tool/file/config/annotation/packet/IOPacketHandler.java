package com.vast.vl_tool.file.config.annotation.packet;

import com.vast.vl_tool.file.entity.FileBody;
import com.vast.vl_tool.file.config.annotation.AbstractIOHandler;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;

/**
 * @author vastlan
 * @description
 * @use
 *  tool
 *    .packet()
 *    .compress(xxx) / .decompress(xxx)
 *    .target(xxx)
 *    .zip() / .rar()
 *    .invoke()
 * @created 2022/7/27 10:26
 */
public class IOPacketHandler extends AbstractIOHandler<Object> {

  private Boolean isCompress;

  private IOPacketExecutor ioPacketExecutor;

  private String fileName;

  private String fileExtensionName;

  /** 压缩后存放路径 */
  private String targetPath;

  public IOPacketHandler fileName(String fileName) {
    this.fileName = fileName;
    return this;
  }

  public IOPacketHandler fileExtensionName(String extensionName) {
    this.fileExtensionName = extensionName;
    return this;
  }

  public IOPacketHandler compress(List<FileBody> fileBodyList) {
    isCompress = true;
    setBody(fileBodyList);
    return this;
  }

  public IOPacketHandler decompress(FileBody fileBody) {
    isCompress = false;
    setBody(fileBody);
    return this;
  }

  public IOPacketHandler target(String targetPath) {
    this.targetPath = targetPath;
    return this;
  }

  public IOPacketHandler zip() {
    if (StringUtils.hasLength(fileName)) {
      if (StringUtils.hasLength(fileExtensionName)) {
        zip(fileName, fileExtensionName);
      } else {
        zip(fileName);
      }
    } else {
      ioPacketExecutor = new ZipCompressExecutor((List<FileBody>) body, targetPath);
    }

    return this;
  }

  public IOPacketHandler zip(String fileName) {
    ioPacketExecutor = new ZipCompressExecutor((List<FileBody>) body, targetPath, fileName);
    return this;
  }

  public IOPacketHandler zip(String fileName, String fileExtensionName) {
    ioPacketExecutor = new ZipCompressExecutor((List<FileBody>) body, targetPath, fileName, fileExtensionName);
    return this;
  }

  public IOPacketHandler unzip() {
    ioPacketExecutor = new ZipDecompressExecutor((FileBody) body, targetPath);
    return this;
  }

  public IOPacketHandler rar() {
    Assert.notNull(ioPacketExecutor, "rar压缩功能暂未实现");
    return this;
  }

  public IOPacketHandler unrar() {
    ioPacketExecutor = new RarDecompressExecutor((FileBody) body, targetPath);
    return this;
  }

  @Override
  protected void checkParamValidity() {
    super.checkParamValidity();
    Assert.notNull(isCompress, "未确定解压还是压缩目标内容，请选择 compress() 或 decompress()");
  }

  @Override
  public Object handle() throws IOException {
    if (ioPacketExecutor == null) {
      zip();
    }

    return isCompress ? handleCompress() : handleDecompress();
  }

  private FileBody handleCompress() throws IOException {
    ioPacketExecutor.action();
    return (FileBody) ioPacketExecutor.actionSuccessfulResult();
  }

  private FileBody handleDecompress() throws IOException {
    ioPacketExecutor.action();
    return (FileBody) ioPacketExecutor.actionSuccessfulResult();
  }
}
