package com.vast.vl_tool.file.config.annotation.upload;

import com.vast.vl_tool.file.entity.FileBody;
import com.vast.vl_tool.file.FileTool;
import com.vast.vl_tool.file.config.annotation.AbstractIOHandler;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.handler.WebRequestHandlerInterceptorAdapter;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
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

  private MultipartFile multipartFile;

  public IOUploadHandler(MultipartFile multipartFile) {
    this.multipartFile = multipartFile;
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
    Assert.notNull(multipartFile, "文件源为空");
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

//    BufferedInputStream bufferedInputStream = new BufferedInputStream(multipartFile.getInputStream());
//    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(Files.newOutputStream(body.getPath()));
//
//    byte[] buffer = new byte[10];
//    int len = 0;
//
//    while ((len = bufferedInputStream.read(buffer)) != -1) {
//      bufferedOutputStream.write(buffer, 0, len);
//    }
//
//    bufferedOutputStream.close();
//    bufferedInputStream.close();

    readAndWrite(multipartFile.getInputStream(), Files.newOutputStream(body.getPath()));

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

//    BufferedInputStream bufferedInputStream = new BufferedInputStream(multipartFile.getInputStream());
//    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(Files.newOutputStream(newFileBody.getPath()));
//
//    byte[] buffer = new byte[10];
//    int len = 0;
//
//    while ((len = bufferedInputStream.read(buffer)) != -1) {
//      bufferedOutputStream.write(buffer, 0, len);
//    }
//
//    bufferedOutputStream.close();
//    bufferedInputStream.close();

    readAndWrite(multipartFile.getInputStream(), Files.newOutputStream(newFileBody.getPath()));

    return newFileBody;
  }

  /** 以缓冲区的形式读取文件，适配读取大文件数据，避免内存溢出 */
  public void readAndWrite(InputStream inputStream, OutputStream outputStream) throws IOException {
    byte[] buffer = new byte[10];
    int len = 0;

    try (
     BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
      BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
   ) {
     while ((len = bufferedInputStream.read(buffer)) != -1) {
       bufferedOutputStream.write(buffer, 0, len);
     }
    }
  }
}
