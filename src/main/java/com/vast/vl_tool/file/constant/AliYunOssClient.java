package com.vast.vl_tool.file.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author vastlan
 * @description
 * @created 2023/3/24 11:10
 */

@Getter
public enum AliYunOssClient {

  DOMAIN("使用OSS域名或自定义域名新建OSSClient, 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。"),
  PROFESSIONAL("专有云或专有域环境新建OSSClient, 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户"),
  IP("使用IP新建OSSClient, 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户"),
  STS("使用STS新建OSSClient"),
  STSAssumeRole("使用STSAssumeRole新建OSSClient"),
  EcsRamRole("使用EcsRamRole新建OSSClient")
  ;

  private String desc;

  AliYunOssClient(String desc) {
    this.desc = desc;
  }
}
