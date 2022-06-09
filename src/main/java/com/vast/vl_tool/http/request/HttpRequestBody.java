package com.vast.vl_tool.http.request;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

/**
 * @author vastlan
 * @description
 * @created 2022/6/8 9:58
 */
public interface HttpRequestBody {

  HttpEntity body(HttpHeaders headers);

}
