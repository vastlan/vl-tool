package com.vast.vl_tool.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

/**
 * @author vastlan
 * @description
 * @created 2022/1/21 11:48
 */

public class JsonTool {
  public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  public static String toJSONString(Object object) {
    try {
      return OBJECT_MAPPER.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("json解析失败", e);
    }
  }

  public static Map<String, Object> pares(String jsonString) {
    try {
      return OBJECT_MAPPER.readValue(jsonString, Map.class);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("json解析失败", e);
    }
  }

  public static String toJsonString(Map<?, ?> map) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      return objectMapper.writeValueAsString(map);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("json解析失败", e);
    }
  }
}
