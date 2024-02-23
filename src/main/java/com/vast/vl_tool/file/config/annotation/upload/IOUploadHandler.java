package com.vast.vl_tool.file.config.annotation.upload;

import com.vast.vl_tool.file.entity.FileBody;
import com.vast.vl_tool.file.FileTool;
import com.vast.vl_tool.file.config.annotation.AbstractIOHandler;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;

/**
 * @author vastlan
 * @description
 * @use
 *  tool
 *    .upload(multipartFile)
 *    .to(xxx)
 *    .onCover(bol)
 *    .invoke()
 * @created 2022/7/26 10:18
 */
public class IOUploadHandler extends AbstractIOHandler<FileBody> {

  private boolean onCover = true;

  private InputStream inputStream;

  public IOUploadHandler(InputStream inputStream) {
    this.inputStream = inputStream;
  }

  public IOUploadHandler to(FileBody fileBody) {
    setBody(fileBody);
    return this;
  }

  public IOUploadHandler to(String fileAbsolutePath) {
    return to(FileBody.create(fileAbsolutePath));
  }

  public IOUploadHandler onCover(boolean onCover) {
    this.onCover = onCover;
    return this;
  }

  @Override
  public FileBody handle() throws IOException {
    Assert.notNull(inputStream, "文件源为空");
    return onCover ? coverAndUpload() : upload();
  }

  private FileBody coverAndUpload() throws IOException {
    File convertFile = body.getFile();

    if(!body.existFile()) {
      FileBody fileBody = FileTool.create().createFile(body).invoke();
      convertFile = fileBody != null ? fileBody.getFile() : null;
    }

    if (convertFile == null) {
      return null;
    }

    inputStream.transferTo(Files.newOutputStream(body.getPath()));

    return body;
  }

  private FileBody upload() throws IOException {
    File file = body.getFile();

    if (!body.existFile()) {
      return coverAndUpload();
    }

    File parentFile = file.getParentFile();
    File[] existedFile = parentFile.listFiles();

    String fileName = file.getName();
    String finalFileName = fileName.substring(0, fileName.lastIndexOf("."));;
    int num = (int) Arrays.stream(existedFile).map(File::getName).filter(name -> name.indexOf(finalFileName) != -1).count();

    String folderAbsolutePath = parentFile.getAbsolutePath();

    FileBody newFileBody = FileBody.create(folderAbsolutePath, finalFileName + "-副本" + num + fileName.substring(fileName.lastIndexOf(".") + 1));
    newFileBody = FileTool.create().createFile(newFileBody).invoke();

    inputStream.transferTo(Files.newOutputStream(newFileBody.getPath()));

    return newFileBody;
  }

  /** 以缓冲区的形式读取文件，适配读取大文件数据，避免内存溢出 */
  public void readAndWrite(InputStream inputStream, OutputStream outputStream) throws IOException {
    inputStream.transferTo(outputStream);
  }
}
