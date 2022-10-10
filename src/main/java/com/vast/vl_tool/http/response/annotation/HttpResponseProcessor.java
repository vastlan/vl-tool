package com.vast.vl_tool.http.response.annotation;

import com.vast.vl_tool.exception.AssertTool;
import com.vast.vl_tool.format.JsonTool;
import org.springframework.core.io.AbstractResource;
import org.springframework.core.io.PathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author vastlan
 * @description
 * @use
 *  HttpTool.createResponse(xxx)
 *  [.mediaType(xxx)]
 *  [.status(HttpStatus)]
 *  .write(xxx)
 * @created 2022/6/8 17:58
 */
public class HttpResponseProcessor {

  private String mediaTypeValue = String.format("%s;charset=UTF-8", MediaType.APPLICATION_JSON_VALUE);

  private HttpStatus status = HttpStatus.OK;

  private ServletResponse servletResponse;

  public HttpResponseProcessor(ServletResponse response) {
    this.servletResponse = response;
  }

  public HttpResponseProcessor status(HttpStatus httpStatus) {
    ((HttpServletResponse) servletResponse).setStatus(status.value());
    this.status = httpStatus;

    return this;
  }

  public HttpResponseProcessor mediaType(String mediaType) {
    this.mediaTypeValue = mediaType;
    return this;
  }

  public void write(Object object) throws IOException {
    AssertTool.isNull(servletResponse, new NullPointerException("ServletResponse 不能为空"));
    servletResponse.setContentType(mediaTypeValue);
    JsonTool.OBJECT_MAPPER.writeValue(servletResponse.getWriter(), object);
  }

  /**
   * 将文件源内容以流的形式返回
   * @param pathResource
   * @throws IOException
   */
  public ResponseEntity<AbstractResource> writeFileStream(PathResource pathResource) throws IOException {
    AssertTool.isNull(servletResponse, new NullPointerException("ServletResponse 不能为空"));

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
