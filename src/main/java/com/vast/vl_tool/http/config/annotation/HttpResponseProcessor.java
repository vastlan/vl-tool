package com.vast.vl_tool.http.config.annotation;

import com.vast.vl_tool.exception.AssertTool;
import com.vast.vl_tool.json.JsonTool;
import org.springframework.core.io.AbstractResource;
import org.springframework.core.io.PathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author vastlan
 * @description
 * @created 2022/6/8 17:58
 */
public class HttpResponseProcessor {

  private String mediaTypeValue = String.format("%s;charset=UTF-8", MediaType.APPLICATION_JSON_VALUE);

  private ServletResponse response;

  public HttpResponseProcessor(ServletResponse response) {
    this.response = response;
  }

  public HttpResponseProcessor mediaType(String mediaType) {
    this.mediaTypeValue = mediaType;
    return this;
  }

  /**
   * 将文件源内容以流的形式返回
   * @param object
   * @throws IOException
   */
  public void write(Object object) throws IOException {
    AssertTool.isNull(response, new NullPointerException("ServletResponse 不能为空"));
    JsonTool.OBJECT_MAPPER.writeValue(response.getWriter(), object);
  }

  public ResponseEntity<AbstractResource> writeStream(PathResource pathResource) {
    if (!pathResource.exists()) {
      return ResponseEntity.notFound().build();
    }

    HttpHeaders httpHeaders = new HttpHeaders();

    String fileName = pathResource.getFilename();
    String encodeFileName = null;

    try {
      encodeFileName = URLEncoder.encode(fileName, "UTF-8");

      httpHeaders.add("Content-Disposition", "inline; fileName=" + encodeFileName);
      httpHeaders.add("Content-Type", Files.probeContentType(Paths.get(fileName)));

      return ResponseEntity.ok().lastModified(pathResource.lastModified()).headers(httpHeaders).body(pathResource);

    } catch (Exception e) {
      e.printStackTrace();
    }

    return ResponseEntity.noContent().build();
  }


}
