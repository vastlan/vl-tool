package com.vast.vl_tool.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * @author vastlan
 * @description
 * @created 2022/1/21 11:51
 */
public class HttpTool {
  public static final RestTemplate REST_TEMPLATE = new RestTemplate();
  public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  public static String request(String url, Object request, HttpMethod httpMethod) {
    ResponseEntity responseEntity = null;

    try {
      responseEntity = send(url, request, httpMethod);
    } catch (Exception e) {
      e.printStackTrace();
    }

    if (responseEntity == null) {
      throw new HttpServerErrorException(HttpStatus.NOT_FOUND, "连接 GPS服务器 异常");
    }

    return (String) responseEntity.getBody();
  }

  public static Map<String, Object> requestForMap(String url, Object obj, HttpMethod httpMethod) {
    String responseBody = request(url, obj, httpMethod);
    return parseToMap(responseBody);
  }

  public static HttpEntity<String> getObjectHttpEntity(String data) {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
    return new HttpEntity<>(data, httpHeaders);
  }

  public static String parseToJSONString(Object obj) {
    try {
      return OBJECT_MAPPER.writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("json解析失败", e);
    }
  }

  public static Map<String, Object> parseToMap(String str) {
    try {
      return OBJECT_MAPPER.readValue(str, Map.class);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("json解析失败", e);
    }
  }

  public static List<Object> parseToList(String str) {
    try {
      return OBJECT_MAPPER.readValue(str, List.class);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("json解析失败", e);
    }
  }

  public static <T> T parseToClass(String responseData, Class<T> cls) {
    T t = null;

    try {
      t = (T) HttpTool.OBJECT_MAPPER.readValue(responseData, cls);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }

    return t;
  }

  private static ResponseEntity send(String url, Object request, HttpMethod httpMethod) {
    String data = parseToJSONString(request);
    HttpEntity<String> httpEntity = getObjectHttpEntity(data);
    ResponseEntity<String> responseEntity = REST_TEMPLATE.exchange(url, httpMethod, httpEntity, String.class);

    return responseEntity;
  }
}
