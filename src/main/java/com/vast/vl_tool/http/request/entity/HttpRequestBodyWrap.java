package com.vast.vl_tool.http.request.entity;

import com.vast.vl_tool.http.request.annotaion.HttpProcessorAdepter;
import com.vast.vl_tool.http.request.annotaion.HttpRequestProcessor;
import org.springframework.http.MediaType;

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
