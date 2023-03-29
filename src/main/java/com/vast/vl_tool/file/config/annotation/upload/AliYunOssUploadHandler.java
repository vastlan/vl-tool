package com.vast.vl_tool.file.config.annotation.upload;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.comm.ResponseMessage;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.vast.vl_tool.exception.AssertTool;
import com.vast.vl_tool.file.config.annotation.AbstractIOHandler;
import com.vast.vl_tool.file.constant.AliYunOssClient;
import com.vast.vl_tool.file.entity.FileBody;
import com.vast.vl_tool.time.DateTool;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author vastlan
 * @description
 * @created 2023/3/21 9:06
 */

public class AliYunOssUploadHandler extends AbstractIOHandler<FileBody> {

  private Boolean onCover = true;

  private AliYunOssClient clientType = AliYunOssClient.DOMAIN;

  private InputStream fileSource;

  private OSS ossClient;

  /**
   * Endpoint以华东1（杭州）为例，其它Region请按实际情况填写，或者自己生成的域名
   * eg. https://oss-cn-beijing.aliyuncs.com
   */
  private String endpoint;

  /** 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。 */
  private String accessKeyId;
  private String accessKeySecret;
  private String securityToken;

  /** 填写Bucket名称，例如examplebucket。 */
  private String bucketName;

  /**
   * 填写Object完整路径，完整路径中不能包含Bucket名称.
   * 存储路径以及文件名
   * eg. "images/pic/mamba.jpg"
   */
  private String objectName;

  public AliYunOssUploadHandler clientType(AliYunOssClient clientType) {
    this.clientType = clientType;
    return this;
  }

  public AliYunOssUploadHandler source(InputStream fileSource) {
    this.fileSource = fileSource;
    return this;
  }

  public AliYunOssUploadHandler cover(Boolean onCover) {
    this.onCover = onCover;
    return this;
  }

  public AliYunOssUploadHandler endpoint(String endpoint) {
    this.endpoint = endpoint;
    return this;
  }

  public AliYunOssUploadHandler accessKeyId(String accessKeyId) {
    this.accessKeyId = accessKeyId;
    return this;
  }

  public AliYunOssUploadHandler accessKeySecret(String accessKeySecret) {
    this.accessKeySecret = accessKeySecret;
    return this;
  }

  public AliYunOssUploadHandler securityToken(String securityToken) {
    this.securityToken = securityToken;
    return this;
  }

  public AliYunOssUploadHandler bucketName(String bucketName) {
    this.bucketName = bucketName;
    return this;
  }

  public AliYunOssUploadHandler objectName(String objectName) {
    this.objectName = objectName;
    return this;
  }

  public Boolean exist() {
    checkOssBucketParam();
    return getCurrentClient().doesObjectExist(bucketName, objectName);
  }

  @Override
  public FileBody handle() throws IOException {
    // 创建OSSClient实例。
    OSS ossClient = getCurrentClient();

    FileBody fileBody = new FileBody();

    try {
      if (!onCover) {
        if (exist()) {
          String suffix = objectName.substring(objectName.lastIndexOf("."));
          objectName = objectName.replace(suffix, "_" + DateTool.getFormattedCurrentDateTime("yyyyMMddHHmmss") + suffix);
        }
      }

       // 如果需要上传时设置存储类型和访问权限，请参考以下示例代码。
//       ObjectMetadata metadata = new ObjectMetadata();
//       metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
//       metadata.setObjectAcl(CannedAccessControlList.Private);
//       putObjectRequest.setMetadata(metadata);

      // 创建PutObjectRequest对象。
      PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, fileSource);

      String contentType = Files.probeContentType(Path.of(objectName));

      if (StringUtils.hasLength(contentType)) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(contentType);
        objectMetadata.setContentDisposition("inline");
      }

      // 设置该属性可以返回response。如果不设置，则返回的response为空。
      putObjectRequest.setProcess("true");

      // 上传文件。
      PutObjectResult result = ossClient.putObject(putObjectRequest);
      ResponseMessage response = result.getResponse();

      // 如果上传成功，则返回200。
      if (response.getStatusCode() == HttpStatus.OK.value()) {
        String fileRootUri = response.getUri();
        fileBody.setFileName(objectName);
        fileBody.setFileAbsolutePath(fileRootUri);
        fileBody.setFileRelativePath(fileRootUri);

        return fileBody;
      }

    } catch (OSSException oe) {
      System.out.println("Caught an OSSException, which means your request made it to OSS, "
        + "but was rejected with an error response for some reason.");
      System.out.println("Error Message:" + oe.getErrorMessage());
      System.out.println("Error Code:" + oe.getErrorCode());
      System.out.println("Request ID:" + oe.getRequestId());
      System.out.println("Host ID:" + oe.getHostId());
    } catch (ClientException ce) {
      System.out.println("Caught an ClientException, which means the client encountered "
        + "a serious internal problem while trying to communicate with OSS, "
        + "such as not being able to access the network.");
      System.out.println("Error Message:" + ce.getMessage());
    } finally {
      if (ossClient != null) {
        ossClient.shutdown();
      }
    }

    return null;
  }

  public OSS getCurrentClient() {
    if (ossClient != null) {
      return ossClient;
    }

    checkOssIdentityParam();

    switch (clientType) {
      case STS -> {
        AssertTool.isTrue(!StringUtils.hasLength(securityToken), new IllegalArgumentException("sts 模式下 securityToken 不能为空"));
        this.ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret, securityToken);
      }
      default -> this.ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }

    return ossClient;
  }

  @Override
  protected void checkParamValidity() {
    Assert.notNull(fileSource, "file source 不能为空");
    checkOssIdentityParam();
    checkOssBucketParam();
  }

  private void checkOssIdentityParam() {
    AssertTool.isTrue(!StringUtils.hasLength(endpoint), new IllegalArgumentException("endpoint 不能为空"));
    AssertTool.isTrue(!StringUtils.hasLength(accessKeyId), new IllegalArgumentException("accessKeyId 不能为空"));
    AssertTool.isTrue(!StringUtils.hasLength(accessKeySecret), new IllegalArgumentException("accessKeySecret 不能为空"));
  }

  private void checkOssBucketParam() {
    AssertTool.isTrue(!StringUtils.hasLength(bucketName), new IllegalArgumentException("bucketName 不能为空"));
    AssertTool.isTrue(!StringUtils.hasLength(objectName), new IllegalArgumentException("objectName 不能为空"));
  }
}
