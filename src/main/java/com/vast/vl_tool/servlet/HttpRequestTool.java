package com.vast.vl_tool.servlet;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author vastlan
 * @description
 * @created 2022/1/21 11:55
 */
public class HttpRequestTool {

  public static String getRequestBodyToString(HttpServletRequest request) {

    StringBuilder params = new StringBuilder("");

    String temp = null;

    try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(request.getInputStream(), Charset.forName("UTF-8")))) {

      while ((temp = bufferedReader.readLine()) != null) {
        params.append(temp);
      }

    } catch (IOException e) {
      e.printStackTrace();
    }

    return params.toString();

  }

  public static byte[] getRequestBodyToBytes(HttpServletRequest request) {
    String rbString = getRequestBodyToString(request);
    return "".equals(rbString) ? new byte[]{} : rbString.getBytes(StandardCharsets.UTF_8);
  }

  public static JSONObject getRequestBodyToJsonObject(HttpServletRequest request) {
    String paramStr = getRequestBodyToString(request);
    return paramStr != null ? (JSONObject) JSONObject.parse(paramStr) : null;
  }

  public static Object getRequestBodyParam(HttpServletRequest request, String paramName) {
    JSONObject requestBodyToJsonObject = getRequestBodyToJsonObject(request);
    return requestBodyToJsonObject != null ? requestBodyToJsonObject.get(paramName) : null;
  }

  public static String getRequestBodyParamString(HttpServletRequest request, String paramName) {
    JSONObject requestBodyToJsonObject = getRequestBodyToJsonObject(request);
    return requestBodyToJsonObject != null ? requestBodyToJsonObject.getString(paramName) :null;
  }

  public static Integer getRequestBodyParamInteger(HttpServletRequest request, String paramName) {
    JSONObject requestBodyToJsonObject = getRequestBodyToJsonObject(request);
    return requestBodyToJsonObject != null ? requestBodyToJsonObject.getInteger(paramName) :null;
  }

  public static JSONArray getRequestBodyToJSONArray(HttpServletRequest request) {
    String paramStr = getRequestBodyToString(request);
    return paramStr != null ? (JSONArray) JSONArray.parse(getRequestBodyToString(request)) : null;
  }

}
