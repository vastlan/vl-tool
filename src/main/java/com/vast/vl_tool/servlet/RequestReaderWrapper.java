package com.vast.vl_tool.servlet;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * @author vastlan
 * @description
 *  用于解决获取请求流位置不重置的问题处理
 * @created 2021/6/10 17:04
 */

public class RequestReaderWrapper extends HttpServletRequestWrapper {

  private byte[] rbBytes;

  public RequestReaderWrapper(HttpServletRequest request) {
    super(request);
    this.rbBytes = HttpServletRequestTool.getRequestBodyToBytes(request);
  }

  @Override
  public ServletInputStream getInputStream() throws IOException {

    final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.rbBytes);

    return new ServletInputStream() {

      @Override
      public boolean isFinished() {
        return false;
      }

      @Override
      public boolean isReady() {
        return false;
      }

      @Override
      public void setReadListener(ReadListener listener) {

      }

      @Override
      public int read() throws IOException {
        return byteArrayInputStream.read();
      }
    };

  }
}