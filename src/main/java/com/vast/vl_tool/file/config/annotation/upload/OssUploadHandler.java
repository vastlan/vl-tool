package com.vast.vl_tool.file.config.annotation.upload;

/**
 * @author vastlan
 * @description
 * @created 2023/3/21 9:43
 */

/**
 * FileTool
 *  .uploadToOss()
 *    .aliyun()
 *      .source(File)
 *      .endpoint("")
 *      .accessKeyId("")
 *      .accessKeySecret("")
 *      .bucketName("")
 *      .objectName("")
 *      .invoke();
 */
public class OssUploadHandler {

  public AliYunOssUploadHandler aliyun() {
    return new AliYunOssUploadHandler();
  }
}
