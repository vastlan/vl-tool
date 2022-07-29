package com.vast.vl_tool.file.config.annotation.create;

import com.vast.vl_tool.file.entity.FileBody;
import com.vast.vl_tool.file.config.annotation.IOExecutor;

import java.io.IOException;

/**
 * @author vastlan
 * @description
 * @created 2022/7/26 11:04
 */
public interface IOCreationExecutor<T> extends IOExecutor {
  T create(FileBody fileBody) throws IOException;
}
