package com.vast.vl_tool.file.entity;

import lombok.*;

/**
 * @author vastlan
 * @description
 * @created 2022/7/29 8:55
 */

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ExifFileBody {

  private FileBody fileBody;

  private Double latitude;

  private Double longitude;

  public static ExifFileBody create(FileBody fileBody, Double latitude, Double longitude) {
    return new ExifFileBody(fileBody, latitude, longitude);
  }

  public static ExifFileBody create(FileBody fileBody) {
    return create(fileBody, null, null);
  }
}
