package com.vast.vl_tool.regex;

import java.util.regex.Pattern;

/**
 * @author vastlan
 * @description
 * @created 2022/5/21 11:22
 */
public class RegexTool {

  /** yyyy-MM-dd HH:mm:ss、yyyy/MM/dd HH:mm:ss */
  public static final String DATE_REGEX = "\\d{4}(\\-|\\/)\\d{2}\\1\\d{2}(\\s)\\d{2}(\\:)\\d{2}(\\:)\\d{2}";

  /** yyyy-MM-dd、yyyy/MM/dd */
  public static final String DATE_REGEX_TML = "\\d{4}(\\-|\\/)\\d{2}\\1\\d{2}";

  public static Boolean isMatches(String regex, String matchingSource) {
    return Pattern.compile(regex).matcher(matchingSource).matches();
  }

  public static Boolean isMatchesDate(String dateStr) {
    return Pattern.compile(DATE_REGEX).matcher(dateStr).matches();
  }

  public static Boolean isMatchesDateOfYML(String dateStr) {
    return Pattern.compile(DATE_REGEX_TML).matcher(dateStr).matches();
  }

//  public static void main(String[] args) {
//    String str = "/***";
//    String regex = "\\/(\\*|\\*\\*|\\*\\*\\*)";
//    System.out.println(Pattern.compile(regex).matcher(str).matches());
//
//    String apiOne = "/user";
//    String apiTwo = "/user/qfasfaf";
//
//    String regex = "\\/user\\/.*?";
//    System.out.println(Pattern.compile(regex).matcher(apiOne).matches());
//    System.out.println(Pattern.compile(regex).matcher(apiTwo).matches());
//  }
}
