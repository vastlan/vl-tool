package com.vast.vl_tool.http.request.entity;

import com.vast.vl_tool.http.request.annotaion.HttpProcessorAdepter;
import com.vast.vl_tool.http.request.annotaion.HttpRequestProcessor;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.Set;

/**
 * @author vastlan
 * @description
 * @created 2022/8/1 10:10
 */
public class HttpRequestBodyWrap {

  private HttpRequestContent httpRequestContent;

  private HttpRequestProcessor httpRequestProcessor;

  public HttpRequestBodyWrap(HttpRequestContent httpRequestContent, HttpRequestProcessor httpRequestProcessor) {
    this.httpRequestContent = httpRequestContent;
    this.httpRequestProcessor = httpRequestProcessor;
  }

  public JsonHttpRequestBody json() {
    JsonHttpRequestBody jsonHttpRequestBody = new JsonHttpRequestBody();
    createAndApply(jsonHttpRequestBody);
    httpRequestContent.setBody(jsonHttpRequestBody);
    return jsonHttpRequestBody;
  }

  public JsonHttpRequestBody json(Map<String, Object> param) {
    Assert.notNull(param, "param must not be null");
    JsonHttpRequestBody jsonHttpRequestBody = new JsonHttpRequestBody();
    createAndApply(jsonHttpRequestBody);

    param.keySet().stream().forEach(key -> jsonHttpRequestBody.put(key, param.get(key)));
    return jsonHttpRequestBody;
  }

  public JsonHttpRequestObjectBody json(Object param) {
    JsonHttpRequestObjectBody jsonHttpRequestBody = new JsonHttpRequestObjectBody(param);
    return jsonHttpRequestBody;
  }

  public FormDataHttpRequestBody formData() {
    FormDataHttpRequestBody formDataHttpRequestBody = new FormDataHttpRequestBody();
    createAndApply(formDataHttpRequestBody);
    httpRequestContent.setBody(formDataHttpRequestBody);
    httpRequestContent.setMediaType(String.valueOf(MediaType.MULTIPART_FORM_DATA));
    return formDataHttpRequestBody;
  }

  public void createAndApply(HttpProcessorAdepter adepter) {
    adepter.setProcessor(httpRequestProcessor);
  }
}
