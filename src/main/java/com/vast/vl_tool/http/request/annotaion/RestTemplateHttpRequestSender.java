package com.vast.vl_tool.http.request.annotaion;

import com.vast.vl_tool.exception.AssertTool;
import com.vast.vl_tool.http.HttpTool;
import com.vast.vl_tool.http.request.entity.HttpRequestContent;
import org.springframework.http.*;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Iterator;
import java.util.Map;

/**
 * @author vastlan
 * @description
 * @created 2022/7/29 15:55
 */
public class RestTemplateHttpRequestSender extends AbstractHttpRequestSender<HttpRequestContent> {

  public static final RestTemplate REST_TEMPLATE = new RestTemplate();

  private HttpRequestContent httpRequestContent;

  public RestTemplateHttpRequestSender(HttpRequestContent httpRequestContent) {
    this.httpRequestContent = httpRequestContent;
  }

  @Override
  public boolean checkParamValidity() throws Exception {
    AssertTool.isTrue(!StringUtils.hasLength(httpRequestContent.getMethod()), new HttpServerErrorException(HttpStatus.METHOD_NOT_ALLOWED, "请求方法不能为空"));
    AssertTool.isTrue(!StringUtils.hasLength(httpRequestContent.getUrl()), new HttpServerErrorException(HttpStatus.BAD_REQUEST, "请求地址不能为空"));

    return true;
  }

  @Override
  public HttpRequestContent handle() throws Exception {

    HttpRequestContent.Response response = httpRequestContent.getResponse();

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.valueOf(httpRequestContent.getMediaType()));

    if (httpRequestContent.getHeader() != null) {
      Map<String, Object> httpReqHeaderMap = httpRequestContent.getHeader().getHeaders();
      Iterator<Map.Entry<String, Object>> iterator = httpReqHeaderMap.entrySet().iterator();

      while (iterator.hasNext()) {
        Map.Entry<String, Object> next = iterator.next();

        headers.add(next.getKey(), next.getValue().toString());
      }
    }

    Object bodyObj = null;

    if (httpRequestContent.getBody() != null) {
      bodyObj = httpRequestContent.getBody().body();
    }

    HttpEntity httpEntity = new HttpEntity<>(bodyObj, headers);

    ResponseEntity responseEntity = null;

    try {
      responseEntity = REST_TEMPLATE.exchange(httpRequestContent.getUrl(), HttpMethod.valueOf(httpRequestContent.getMethod().toUpperCase()), httpEntity, response.getType());
    } catch (HttpServerErrorException e) {
      e.printStackTrace();
      throw new HttpServerErrorException(e.getStatusCode());
    }

    AssertTool.isNull(responseEntity, new HttpServerErrorException(HttpStatus.BAD_REQUEST, "连接 " + httpRequestContent.getUrl() + " 服务器异常"));

    response.setStatusCode(responseEntity.getStatusCodeValue());
    response.setResult(responseEntity.getBody());

    return httpRequestContent;
  }

  public HttpRequestContent send(Class clz) throws Exception {
    httpRequestContent.getResponse().setType(clz);
    return send();
  }
}
