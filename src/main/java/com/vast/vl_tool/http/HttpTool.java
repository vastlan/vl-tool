package com.vast.vl_tool.http;

import com.vast.vl_tool.format.JsonTool;
import com.vast.vl_tool.http.request.annotaion.HttpRequestProcessor;
import com.vast.vl_tool.http.request.annotaion.RestTemplateHttpRequestSender;
import com.vast.vl_tool.http.request.entity.HttpRequestContent;
import com.vast.vl_tool.http.response.annotation.HttpResponseProcessor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.ServletResponse;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
}