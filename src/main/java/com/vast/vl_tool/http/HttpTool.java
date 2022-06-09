package com.vast.vl_tool.http;

import com.vast.vl_tool.http.config.annotation.HttpRequestProcessor;
import com.vast.vl_tool.http.config.annotation.HttpResponseProcessor;
import com.vast.vl_tool.json.JsonTool;
import org.springframework.core.io.AbstractResource;
import org.springframework.core.io.PathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

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
 * @created 2022/6/8 9:01
 */
public class HttpTool {

  public static final RestTemplate REST_TEMPLATE = new RestTemplate();

  /**
   * HttpTool.createRequest()
   *  .url(xxx)
   *  .method(xxx)
   *  .headers()
   *  .body()
   *    .json().set(xxx) || .formData().add(xxx, xxx)
   *  .and()
   *  .send()
   * @return
   */
  public static HttpRequestProcessor createRequest() {
    return new HttpRequestProcessor();
  }

  /**
   *  HttpTool.createResponse(xxx)
   *  [.mediaType(xxx)]
   *  .write(xxx)
   * @param response
   * @return
   */
  public static HttpResponseProcessor createResponse(ServletResponse response) {
    return new HttpResponseProcessor(response);
  }

}
