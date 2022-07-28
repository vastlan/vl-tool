package com.vast.vl_tool.file.config.annotation;
import java.io.IOException;

/**
 * @author vastlan
 * @description
 * @created 2022/7/25 17:36
 */
public interface IOHandler<T> {

  T handle() throws IOException;
}
