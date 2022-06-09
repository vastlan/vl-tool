package com.vast.vl_tool.http.config.annotation;

import com.vast.vl_tool.exception.AssertTool;
import com.vast.vl_tool.http.HttpTool;
import com.vast.vl_tool.http.request.FormDataHttpRequestBody;
import com.vast.vl_tool.http.request.HttpRequestBody;
import com.vast.vl_tool.http.request.JsonHttpRequestBody;
import org.springframework.http.*;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpServerErrorException;

/**
 * @author vastlan
 * @description
 * @created 2022/6/8 11:19
 */

public class HttpRequestProcessor {

  private MediaType mediaType = MediaType.APPLICATION_JSON;

  private Class responseType = String.class;

  private HttpHeaders headers = new HttpHeaders();

  private HttpRequestBody requestBody;

  private HttpMethod method;

  private HttpEntity httpEntity;

  private String url;

  public HttpRequestProcessor url(String url) {
    this.url = url;
    return this;
  }

  public JsonHttpRequestBody requestBodyOfJson() {
    requestBody = (JsonHttpRequestBody) createAndApply(new JsonHttpRequestBody());
    return (JsonHttpRequestBody) requestBody;
  }

  public FormDataHttpRequestBody requestBodyOfFormData() {
    mediaType = MediaType.MULTIPART_FORM_DATA;
    requestBody = (FormDataHttpRequestBody) createAndApply(new FormDataHttpRequestBody());
    return (FormDataHttpRequestBody) requestBody;
  }

  public HttpRequestProcessor headers(HttpHeaders headers) {
    this.headers = headers;
    return this;
  }

  public HttpRequestProcessor method(HttpMethod method) {
    this.method = method;
    return this;
  }

  public String send() {
    return send(String.class);
  }

  public <T> T send(Class<T> responseType) {
    AssertTool.isNull(method, new HttpServerErrorException(HttpStatus.METHOD_NOT_ALLOWED, "HttpMethod 不能为空"));
    AssertTool.isTrue(!StringUtils.hasLength(url), new HttpServerErrorException(HttpStatus.BAD_REQUEST, "url 不能为空"));

    this.responseType = responseType;
    headers.setContentType(mediaType);

    if (requestBody != null) {
      httpEntity = requestBody.body(headers);
    } else {
      httpEntity = new HttpEntity<>(null, headers);
    }

    ResponseEntity<T> responseEntity = null;

    try {
      responseEntity = HttpTool.REST_TEMPLATE.exchange(url, method, httpEntity, responseType);
    } catch (Exception e) {

    }

    AssertTool.isNull(responseEntity, new HttpServerErrorException(HttpStatus.NOT_FOUND, "连接 " + url + " 服务器异常"));

    return responseEntity.getBody();
  }

  private HttpProcessorAdepter createAndApply(HttpProcessorAdepter adepter) {
    adepter.setProcessor(this);
    return adepter;
  }

}
