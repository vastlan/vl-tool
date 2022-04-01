package com.vast.vl_tool.http.response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author vastlan
 * @description
 * @created 2022/3/24 15:42
 */
public class ResponseBodyConfigure {

  private List<Class> ignoreList = new ArrayList<>();

  public void addIgnoreClass(Class... clsArr) {
    Arrays.stream(clsArr).forEach(clazz -> ignoreList.add(clazz));
  }

  public List<Class> getIgnoreClassList() {
    return ignoreList;
  }

  public Boolean isEmpty() {
    return ignoreList.isEmpty();
  }
}
