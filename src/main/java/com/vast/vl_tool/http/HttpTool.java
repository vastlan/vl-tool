package com.vast.vl_tool.http;

import com.vast.vl_tool.http.request.annotaion.HttpRequestProcessor;
import com.vast.vl_tool.http.response.annotation.HttpResponseProcessor;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletResponse;
import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * @author vastlan
 * @description
 * @created 2022/7/29 11:32
 */
public class HttpTool {

  public static HttpRequestProcessor createRequest() {
    return new HttpRequestProcessor();
  }

  public static HttpResponseProcessor createResponse(ServletResponse response) {
    return new HttpResponseProcessor(response);
  }

  public static void main(String[] args) {
//    String url = "http://192.168.0.136:8081/record/confg/list"; // 替换为你的 SSE 接口
//
//    RestTemplate restTemplate = new RestTemplate();
//
//    String firstSseLine = restTemplate.execute(url,
//            org.springframework.http.HttpMethod.GET,
//            null,
//            (ResponseExtractor<String>) response -> {
//              try (BufferedReader reader = new BufferedReader(
//                      new InputStreamReader(response.getBody()))) {
//                String line;
//                while ((line = reader.readLine()) != null) {
//                  if (line.startsWith("data:")) {
//                    return line.substring(5).trim(); // 去除前缀 "data:"
//                  }
//                }
//              }
//              return null;
//            });
//
//    System.out.println("第一条 SSE 消息为: " + firstSseLine);

  }
}