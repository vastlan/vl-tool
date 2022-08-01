package com.vast.vl_tool.http.response.annotation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vast.vl_tool.http.response.entity.ResponseResult;
import com.vast.vl_tool.json.JsonTool;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author vastlan
 * @description
 * @created 2022/3/24 15:29
 */

public abstract class AbstractResponseBodyAdvice implements ResponseBodyAdvice<Object> {

  private ResponseBodyConfigure responseBodyConfigure;

  void initConfigure(ResponseBodyConfigure responseBodyConfigure) {
    this.responseBodyConfigure = responseBodyConfigure;
    responseBodyConfigure.addIgnoreClass(ResponseResult.class);
    responseBodyConfigure.addIgnoreClass(Byte[].class);

    configure(responseBodyConfigure);
  }

  public abstract void configure(ResponseBodyConfigure responseBodyConfigure);

  @Override
  public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
    if (responseBodyConfigure == null || responseBodyConfigure.isEmpty()) {
      return true;
    }

    Class<?> parameterType = returnType.getParameterType();

    Class ignoreClass = responseBodyConfigure
      .getIgnoreClassList()
      .stream()
      .filter(cls -> parameterType.equals(cls))
      .findAny()
      .orElse(null);

    boolean isIgnoreClass = ignoreClass != null;

    return !isIgnoreClass;
  }

  @Override
  public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                           Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
                           ServerHttpResponse response) {

    Class finalReturnType = returnType.getParameterType();

    if (finalReturnType.equals(String.class)) {
      try {
        return JsonTool.OBJECT_MAPPER.writeValueAsString(new ResponseResult(body));
      } catch (JsonProcessingException e) {
        e.printStackTrace();
      }
    }

    return new ResponseResult(body);
  }
}
