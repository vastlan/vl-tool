package com.vast.vl_tool.format;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vast.vl_tool.file.FileTool;

import java.util.Locale;
import java.util.Map;

/**
 * @author vastlan
 * @description
 * @created 2022/1/21 11:48
 */

public class JsonTool {
  public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  public static Map<String, Object> formatToMap(String jsonString) {
    try {
      return OBJECT_MAPPER.readValue(jsonString, Map.class);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("json 格式化成 map 失败", e);
    }
  }

  public static String parseToJSONString(Object obj) {
    try {
      return OBJECT_MAPPER.writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("json 解析失败", e);
    }
  }

}
