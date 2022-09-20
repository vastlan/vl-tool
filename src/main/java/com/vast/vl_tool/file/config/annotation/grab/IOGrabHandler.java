package com.vast.vl_tool.file.config.annotation.grab;

import com.vast.vl_tool.file.FileTool;
import com.vast.vl_tool.file.entity.FileBody;
import com.vast.vl_tool.file.config.annotation.AbstractIOHandler;

import java.io.IOException;

/**
 * @author vastlan
 * @description
 * @use
 *  tool
 *    .grab()
 *    .content(xxx)
 *    .target(xxx)
 *    .picture()
 *      .scale(x)
 *     .and()
 *     .invoke()
 * @created 2022/7/28 10:39
 */
public class IOGrabHandler extends AbstractIOHandler<Object> {

  private IOGrabExecutor ioGrabExecutor;

  private String targetPath;

  public PictureGrabExecutor picture() {
    ioGrabExecutor = new PictureGrabExecutor((FileBody) body, targetPath);
    createAndApplyAdapter();
    return (PictureGrabExecutor) ioGrabExecutor;
  }

  public PictureGrabExecutor picture(FileBody fileBody, String targetPath) {
    content(fileBody);
    target(targetPath);
    return picture();
  }

  public VideoGrabExecutor video() {
    ioGrabExecutor = new VideoGrabExecutor((FileBody) body, targetPath);
    createAndApplyAdapter();
    return (VideoGrabExecutor) ioGrabExecutor;
  }

  public VideoGrabExecutor video(FileBody fileBody, String targetPath) {
    content(fileBody);
    target(targetPath);
    return video();
  }

  public PanoramaGrabExecutor panorama() {
    ioGrabExecutor = new PanoramaGrabExecutor(body.toString(), targetPath);
    createAndApplyAdapter();
    return (PanoramaGrabExecutor) ioGrabExecutor;
  }

  public PanoramaGrabExecutor panorama(String uri, String targetPath) {
    content(uri);
    target(targetPath);
    return panorama();
  }

  public IOGrabHandler content(Object content) {
    setBody(content);
    return this;
  }

  public IOGrabHandler target(String targetPath) {
    this.targetPath = targetPath;
    return this;
  }

  @Override
  public Object handle() throws IOException {
    ioGrabExecutor.grab();
    return ioGrabExecutor.grabResult();
  }

  public void createAndApplyAdapter() {
    ioGrabExecutor.setIOHandler(this);
  }
}
