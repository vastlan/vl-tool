package com.vast.vl_tool.http.config.annotation;

import com.vast.vl_tool.exception.AssertTool;
import com.vast.vl_tool.http.HttpTool;
import com.vast.vl_tool.http.request.FormDataHttpRequestBody;
import com.vast.vl_tool.http.request.HttpRequestBody;
import com.vast.vl_tool.http.request.HttpRequestHeader;
import com.vast.vl_tool.http.request.JsonHttpRequestBody;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.http.*;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpServerErrorException;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author vastlan
 * @description
 * @created 2022/6/8 11:19
 */

public class HttpRequestProcessor {

  private MediaType mediaType = MediaType.APPLICATION_JSON;

  private Class responseType = String.class;

  private HttpRequestHeader httpRequestHeader = new HttpRequestHeader();

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

  public HttpRequestHeader headers() {
    createAndApply(httpRequestHeader);
    return httpRequestHeader;
  }

  public HttpRequestHeader headers(HttpHeaders headers) {
    createAndApply(httpRequestHeader);
    httpRequestHeader.setHeaders(headers);
    return httpRequestHeader;
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

    HttpHeaders headers = httpRequestHeader.value();
    headers.setContentType(mediaType);

    if (requestBody != null) {
      httpEntity = requestBody.body(headers);
    } else {
      httpEntity = new HttpEntity<>(null, headers);
    }

    ResponseEntity<T> responseEntity = null;

    try {
      responseEntity = HttpTool.REST_TEMPLATE.exchange(url, method, httpEntity, responseType);
    } catch (HttpServerErrorException e) {
      e.printStackTrace();
      throw new HttpServerErrorException(e.getStatusCode());
    }

    AssertTool.isNull(responseEntity, new HttpServerErrorException(HttpStatus.BAD_REQUEST, "连接 " + url + " 服务器异常"));

    return responseEntity.getBody();
  }

  /**
   * 请求媒体数据地址获取其流
   * @return
   */
  public ResponseBody sendForInputStream() throws IOException {
    Set<Map.Entry<String, List<String>>> headers = httpRequestHeader.value().entrySet();

    Request.Builder requestBuilder = new Request.Builder().url(url);

    if (!headers.isEmpty()) {
      Iterator<Map.Entry<String, List<String>>> iterator = headers.iterator();

      while (iterator.hasNext()) {
        Map.Entry<String, List<String>> next = iterator.next();
        requestBuilder.addHeader(next.getKey(), String.valueOf(next.getValue()));
      }
    }

    Request request = requestBuilder.build();
    ResponseBody body = null;
    InputStream stream = null;

    Response response = HttpTool.OK_HTTP_CLIENT.newCall(request).execute();
    body = response.body();

    stream = body.byteStream();

    return body;
  }

  private HttpProcessorAdepter createAndApply(HttpProcessorAdepter adepter) {
    adepter.setProcessor(this);
    return adepter;
  }

}
