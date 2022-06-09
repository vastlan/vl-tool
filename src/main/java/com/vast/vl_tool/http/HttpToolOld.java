package com.vast.vl_tool.http;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.AbstractResource;
import org.springframework.core.io.PathResource;
import org.springframework.http.*;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 * @author vastlan
 * @description
 * @created 2022/1/21 11:51
 */
public class HttpToolOld {
  public static final RestTemplate REST_TEMPLATE = new RestTemplate();
  public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  public static String request(String url, Object request, HttpMethod httpMethod) {
    return request(url, request, null, httpMethod);
  }

  public static String requestWithToken(String url, Object request, String token, HttpMethod httpMethod) {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
    httpHeaders.add("token", token);

    return request(url, request, httpHeaders, httpMethod);
  }

  public static String request(String url, Object request, HttpHeaders httpHeaders, HttpMethod httpMethod) {
    ResponseEntity responseEntity = null;

    try {
      if (httpHeaders != null) {
        responseEntity = send(url, request, httpHeaders, httpMethod);
      } else {
        responseEntity = send(url, request, httpMethod);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    if (responseEntity == null) {
      throw new HttpServerErrorException(HttpStatus.NOT_FOUND, "连接 " + url + " 服务器异常");
    }

    return (String) responseEntity.getBody();
  }

  public static Map<String, Object> requestForMap(String url, Object obj, HttpMethod httpMethod) {
    String responseBody = request(url, obj, httpMethod);
    return parseToMap(responseBody);
  }

  public static JSONObject requestForJSONObject(String url, Object obj, HttpMethod httpMethod) {
    String responseBody = request(url, obj, httpMethod);
    return parseToJSONObject(responseBody);
  }

  public static JSONObject requestForJSONObject(String url, Object obj, String token, HttpMethod httpMethod) {
    String responseBody = requestWithToken(url, obj, token, httpMethod);
    return parseToJSONObject(responseBody);
  }

  public static HttpEntity<String> getObjectHttpEntity(String data) {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
    return getObjectHttpEntity(data, httpHeaders);
  }

  public static HttpEntity<String> getObjectHttpEntity(String data, HttpHeaders httpHeaders) {
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

  public static JSONObject parseToJSONObject(String str) {
    try {
      return JSONObject.parseObject(str);
    } catch (JSONException e) {
      e.printStackTrace();
      return null;
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
      t = (T) HttpToolOld.OBJECT_MAPPER.readValue(responseData, cls);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }

    return t;
  }

  public static ResponseEntity send(String url, Object request, HttpMethod httpMethod) {
    String data = parseToJSONString(request);
    HttpEntity<String> httpEntity = getObjectHttpEntity(data);
    ResponseEntity<String> responseEntity = REST_TEMPLATE.exchange(url, httpMethod, httpEntity, String.class);

    return responseEntity;
  }

  public static ResponseEntity send(String url, Object request, HttpHeaders httpHeaders, HttpMethod httpMethod) {
    String data = parseToJSONString(request);
    HttpEntity<String> httpEntity;

    if (httpHeaders != null) {
      httpEntity = getObjectHttpEntity(data, httpHeaders);
    } else {
      httpEntity = getObjectHttpEntity(data);
    }

    ResponseEntity<String> responseEntity = REST_TEMPLATE.exchange(url, httpMethod, httpEntity, String.class);

    return responseEntity;
  }

  public static void response(ServletResponse response, Object object) throws IOException {
    response.setContentType(String.format("%s;charset=UTF-8", MediaType.APPLICATION_JSON_VALUE));
    OBJECT_MAPPER.writeValue(response.getWriter(), object);
  }

  /**
   * 将文件源内容以流的形式返回
   * @param pathResource
   * @param request
   * @param response
   * @return
   */
  public static ResponseEntity<AbstractResource> responseFileStream(PathResource pathResource, HttpServletRequest request, HttpServletResponse response) {
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
