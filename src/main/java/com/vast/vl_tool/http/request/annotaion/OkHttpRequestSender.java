package com.vast.vl_tool.http.request.annotaion;

import com.vast.vl_tool.exception.AssertTool;
import com.vast.vl_tool.http.HttpTool;
import com.vast.vl_tool.http.request.entity.HttpRequestContent;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpServerErrorException;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

/**
 * @author vastlan
 * @description
 * @created 2022/8/1 10:31
 */
public class OkHttpRequestSender extends AbstractHttpRequestSender<HttpRequestContent> {

  public static final OkHttpClient OK_HTTP_CLIENT = new OkHttpClient();

  private HttpRequestContent httpRequestContent;

  public OkHttpRequestSender(HttpRequestContent httpRequestContent) {
    this.httpRequestContent = httpRequestContent;
  }

  /**
   * 请求媒体数据地址获取其流
   * @return
   */
  public ResponseBody sendForInputStream() throws Exception {
    checkParamValidity();

    Request.Builder requestBuilder = new Request.Builder().url(httpRequestContent.getUrl());

    if (httpRequestContent.getHeader() != null) {
      Map<String, Object> headers = httpRequestContent.getHeader().getHeaders();

      Iterator<Map.Entry<String, Object>> iterator = headers.entrySet().iterator();

      while (iterator.hasNext()) {
        Map.Entry<String, Object> next = iterator.next();
        requestBuilder.addHeader(next.getKey(), String.valueOf(next.getValue()));
      }
    }

    Request request = requestBuilder.build();
    ResponseBody body = null;
    InputStream stream = null;

    Response response = OK_HTTP_CLIENT.newCall(request).execute();
    body = response.body();

    stream = body.byteStream();

    return body;
  }

  @Override
  public boolean checkParamValidity() throws Exception {
    AssertTool.isTrue(!StringUtils.hasLength(httpRequestContent.getMethod()), new HttpServerErrorException(HttpStatus.METHOD_NOT_ALLOWED, "请求方法不能为空"));
    AssertTool.isTrue(!StringUtils.hasLength(httpRequestContent.getUrl()), new HttpServerErrorException(HttpStatus.BAD_REQUEST, "请求地址不能为空"));

    return true;
  }

  @Override
  public HttpRequestContent handle() throws Exception {
    throw new NullPointerException("OkHttpRequestSender send() 未开发");
  }
}
