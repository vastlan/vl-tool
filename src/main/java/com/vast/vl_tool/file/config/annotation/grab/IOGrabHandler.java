package com.vast.vl_tool.file.config.annotation.grab;

import com.vast.vl_tool.file.FileBody;
import com.vast.vl_tool.file.config.annotation.AbstractIOHandler;
import org.springframework.util.Assert;

import java.io.IOException;

/**
 * @author vastlan
 * @description
 * @use
 *  tool
 *    .grab()
 *    .content(xxx)
 *    .target()
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

  public VideoGrabExecutor video() {
    ioGrabExecutor = new VideoGrabExecutor((FileBody) body, targetPath);
    createAndApplyAdapter();
    return (VideoGrabExecutor) ioGrabExecutor;
  }

  public IOGrabHandler panorama() {
    ioGrabExecutor = new PanoramaGrabExecutor(body.toString(), targetPath);
    createAndApplyAdapter();
    return this;
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
