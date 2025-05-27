package com.vast.vl_tool.format;

import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author vastlan
 * @description
 * @created 2024/9/25 17:11
 */
public class StringTool {

  private static final Pattern CAMEL_PATTERN = Pattern.compile("_([a-z])");

  public static String toCamelCase(String input) {
    if (!StringUtils.hasLength(input)) {
      return null;
    }

    Matcher matcher = CAMEL_PATTERN.matcher(input.toLowerCase());

    StringBuffer result = new StringBuffer();

    // 找到每个下划线后的字母，并将其替换为大写
    while (matcher.find()) {
      matcher.appendReplacement(result, matcher.group(1).toUpperCase());
    }

    matcher.appendTail(result);

    return result.toString();
  }
}
