package com.vast.vl_tool.file.config.annotation.grab;

import com.vast.vl_tool.exception.AssertTool;
import com.vast.vl_tool.file.entity.FileBody;
import com.vast.vl_tool.file.FileTool;
import com.vast.vl_tool.http.HttpTool;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import okhttp3.ResponseBody;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;

/**
 * @author vastlan
 * @description
 * @created 2022/7/28 17:07
 */
public class PanoramaGrabExecutor extends AbstractIOGrabExecutor {

  public final static ScriptEngine SCRIPT_ENGINE = new ScriptEngineManager().getEngineByName("js");
  public static final String IMAGE_HOST = "https://ssl-thumb.720static.com/";

  private String url;

  public PanoramaGrabExecutor(String url, String targetPath) {
    super(url, targetPath);
    this.url = url;
  }

  @Override
  public void execute() throws IOException {
    String thumbUrl;
    Integer sceneId;

    try {
      Document doc = Jsoup.connect(url).get();

      String scriptContent =
        doc
          .getElementsByClass("script").get(0)
          .getElementsByTag("script").get(0)
          .childNodes().get(0)
          .toString()
          .replace("window.", "var");

      String query = new URL(url).getQuery();
      sceneId = query != null ? Integer.valueOf(query.split("=")[1]) : 0;

      ScriptObjectMirror scriptObjectMirror = (ScriptObjectMirror) SCRIPT_ENGINE.eval(scriptContent);
      ScriptObjectMirror data = (ScriptObjectMirror) scriptObjectMirror.get("data");
      ScriptObjectMirror product = (ScriptObjectMirror) data.get("product");
      ScriptObjectMirror config = (ScriptObjectMirror) product.get("config");
      ScriptObjectMirror category = (ScriptObjectMirror) config.get("category");
      ScriptObjectMirror groups = (ScriptObjectMirror) category.get("groups");
      ScriptObjectMirror property = (ScriptObjectMirror) product.get("property");

      thumbUrl = IMAGE_HOST + property.get("thumbUrl");

      String panoramaName = "";

      Collection<Object> groupList = groups == null ? category.values() : groups.values();

      for (Object group : groupList) {

        ScriptObjectMirror scenes = (ScriptObjectMirror) ((ScriptObjectMirror) group).get("scenes");
        Collection<Object> sceneList = scenes.values();

        for (Object scene : sceneList) {

          Integer id = (Integer) ((ScriptObjectMirror) scene).get("id");

          if (id.equals(sceneId)) {
            thumbUrl = IMAGE_HOST + ((ScriptObjectMirror) scene).get("thumb");
            panoramaName = (String) ((ScriptObjectMirror) scene).get("name");
          }

        }
      }

      ResponseBody mediaResponseBody = null;

      try {
        // @formatter:off
        mediaResponseBody =
          HttpTool.createRequest()
            .url(thumbUrl)
            .method()
              .get()
          .and()
            .okHttp()
            .sendForInputStream();
        // @formatter:on

        if (mediaResponseBody == null) {
          throw new IOException("获取截取源失败");
        }

        FileBody fileBody = FileBody.create(targetPath + File.separator + grabbedFileName());

        if (fileBody.notExistAndIsFile()) {
          FileTool.create().createFile(fileBody);
        }

        BufferedImage bufferedImage = generateThumbnail(mediaResponseBody.byteStream(), 660, 280);
        ImageIO.write(bufferedImage, "jpg", fileBody.getFile());

        setGrabResult(fileBody);
      } finally {
        if (mediaResponseBody != null) {
          mediaResponseBody.close();
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public boolean checkParamValidity() {
    super.checkParamValidity();
    AssertTool.isTrue(!StringUtils.hasLength(fileContent.toString()), new IllegalArgumentException("url 不能为空"));
    return true;
  }

  public static BufferedImage generateThumbnail(InputStream inputStream, int width, int height) throws IOException {
    BufferedImage srcImage = ImageIO.read(inputStream);

    BufferedImage targetImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    Graphics graphics = targetImage.getGraphics();
    graphics.drawImage(srcImage, 0, 0, width, height, null);
    graphics.dispose();

    return targetImage;
  }
}
