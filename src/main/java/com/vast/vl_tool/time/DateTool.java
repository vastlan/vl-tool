package com.vast.vl_tool.time;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author vastlan
 * @description
 * @created 2022/1/21 11:52
 */
public class DateTool {
  // 1619430384530
  public static long getCurrentTimeMillis() {
    return System.currentTimeMillis();
  }

  // 2021-04-26
  public static LocalDate getCurrentDate() {
    return LocalDate.now();
  }

  // 17:46:24.559300900
  public static LocalTime getCurrentTime() {
    return LocalTime.now();
  }

  // 2021-04-26T17:46:24.559300900
  public static LocalDateTime getCurrentDateTime() {
    return LocalDateTime.now();
  }

  public static Date getCurrentDateTimeOfDate() {
    return parseToDate(getCurrentDateTime());
  }

  public static String getFormattedCurrentDateTime() {
    return getFormattedCurrentDateTime(null);
  }

  public static String getFormattedCurrentDateTime(String pattern) {

    if (pattern == null) {
      pattern = "yyyy-MM-dd HH:mm:ss";
    }

    return getCurrentDateTime().format(DateTimeFormatter.ofPattern(pattern));
  }

  // 2021
  public static int getCurrentYear() {
    return getCurrentDateTime().getYear();
  }

  // APRIL 对象
  public static Month getCurrentMoth() {
    return getCurrentDateTime().getMonth();
  }

  // 4
  public static int getCurrentMothValue() {
    return getCurrentMoth().getValue();
  }

  // 一个月的第几天 - 26
  public static int getCurrentDayOfMonth() {
    return getCurrentDateTime().getDayOfMonth();
  }

  // 一年第几天 - 116
  public static int getCurrentDayOfYear() {
    return getCurrentDateTime().getDayOfYear();
  }

  // MONDAY 对象
  public static DayOfWeek getCurrentDayOfWeek() {
    return getCurrentDateTime().getDayOfWeek();
  }

  // 一星期的第几天 - 1
  public static int getCurrentDayOfWeekValue() {
    return getCurrentDayOfWeek().getValue();
  }

  public static int getCurrentHour() {
    return getCurrentDateTime().getHour();
  }

  public static int getCurrentMinute() {
    return getCurrentDateTime().getMinute();
  }

  public static int getYear(Date date) {
    return parseToLocalDateTime(date).getYear();
  }

  public static Month getMonth(Date date) {
    return parseToLocalDateTime(date).getMonth();
  }

  public static int getMonthValue(Date date) {
    return getMonth(date).getValue();
  }

  public static int getDayOfMonth(Date date) {
    return parseToLocalDateTime(date).getDayOfMonth();
  }

  public static int getDayOfYear(Date date) {
    return parseToLocalDateTime(date).getDayOfYear();
  }

  public static DayOfWeek getDayOfWeek(Date date) {
    return parseToLocalDateTime(date).getDayOfWeek();
  }

  public static int getDayOfWeekValue(Date date) {
    return getDayOfWeek(date).getValue();
  }

  public static int getHour(Date date) {
    return parseToLocalDateTime(date).getHour();
  }

  public static String getStringOfYMD(Date date, String pattern) {
    return parseToLocalDate(date).format(DateTimeFormatter.ofPattern(pattern));
  }

  public static String format(Date date) {
    return format(date, "yyyy-MM-dd hh:mm:dd");
  }

  public static String format(Date date, String pattern) {
    return parseToLocalDateTime(date).format(DateTimeFormatter.ofPattern(pattern));
  }

  /**
   * 转换
   * @param date
   * @return
   */
  public static ZonedDateTime parseToZonedDateTime(Date date) {
    return date.toInstant().atZone(ZoneId.systemDefault());
  }

  public static LocalDate parseToLocalDate(Date date) {
    return parseToZonedDateTime(date).toLocalDate();
  }

  public static LocalDateTime parseToLocalDateTime(Date date) {
    return parseToZonedDateTime(date).toLocalDateTime();
  }

  public static Date parseToDate(LocalDateTime localDateTime) {
    return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
  }
}
