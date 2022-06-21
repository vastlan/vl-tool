package com.vast.vl_tool.file;

import com.vast.vl_tool.exception.AssertTool;
import com.vast.vl_tool.file.config.annotation.FileProcessor;
import com.vast.vl_tool.http.HttpTool;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import okhttp3.ResponseBody;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.bytedeco.libfreenect._freenect_context;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.core.io.PathResource;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;

/**
 * @author vastlan
 * @description
 * @created 2022/6/10 14:17
 */
public class FileTool {

  /***
   * 默认截取视频帧数
   */
  private final static int DEFAULT_CUT_OUT_VIDEO_FRAME_NUMBER = 5;

  private final static ScriptEngine SCRIPT_ENGINE = new ScriptEngineManager().getEngineByName("js");
  private static final String IMAGE_HOST = "https://ssl-thumb.720static.com/";

  public static boolean isPicture(String fileName) {
    String[] pictureSuffixArray = new String[] {
      "jpg", "jpeg", "png", "bmp", "gif", "tif"
    };

    return Arrays.stream(pictureSuffixArray)
      .filter(item -> fileName.indexOf(item) != -1)
      .findAny()
      .orElse(null) != null;
  }

  public static boolean isVideo(String fileName) {
    String[] videoSuffixArray = new String[] {
      "mp4", "flv", "wmv"
    };

    return Arrays.stream(videoSuffixArray)
      .filter(item -> fileName.indexOf(item) != -1)
      .findAny()
      .orElse(null) != null;
  }

  public static boolean isZip(String fileName) {
    return StringUtils.hasLength(fileName) && fileName.endsWith(".zip");
  }

  public static boolean isRar(String fileName) {
    return StringUtils.hasLength(fileName) && fileName.endsWith(".rar");
  }

  public static Boolean isFile(File file) {
    if (file.exists()) {
      return file.isFile();
    }

    String fileName = file.getName();
    return StringUtils.hasLength(fileName) && fileName.indexOf(".") != -1;
  }


  /**
   * FileTool
   *  .createFileProcessor()
   *  .path(xxx)
   *  .upload(xxx)
   * @return
   */
  public static FileProcessor createFileProcessor() {
    return new FileProcessor();
  }

  public static PathResource transformToPathSource(String absolutePath) {
    Path path = Paths.get(absolutePath);
    return new PathResource(path);
  }

  public static FileBody grabThumbnailFor720(String url, String targetFolderPath) {
    return grabThumbnailFor720(url, targetFolderPath, null);
  }

  public static FileBody grabThumbnailFor720(String url, String targetFolderPath, String fileName) {
    AssertTool.isTrue(!StringUtils.hasLength(url), new IllegalArgumentException("url 不能为空"));
    AssertTool.isTrue(!StringUtils.hasLength(targetFolderPath), new IllegalArgumentException("targetFolderPath 不能为空"));

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
      FileBody fileBody = null;

      try {
        // @formatter:off
        mediaResponseBody = HttpTool.createRequest()
          .url(thumbUrl)
          .method(HttpMethod.GET)
          .sendForInputStream();
        // @formatter:on

        if (mediaResponseBody == null) {
          return null;
        }

        if (StringUtils.hasLength(fileName)) {
          fileBody = FileBody.create(targetFolderPath, String.format("%s_%s_%s", panoramaName, sceneId.toString(), fileName));
        } else {
          fileBody = FileBody.create(targetFolderPath, String.format("%s_%s.jpg", panoramaName, sceneId.toString()));
        }

        if (fileBody.notExistAndIsFile()) {
          createFileProcessor().path(fileBody).createFile();
        }

        BufferedImage bufferedImage = generateThumbnail(mediaResponseBody.byteStream(), 660, 280);
        ImageIO.write(bufferedImage, "jpg", fileBody.getFile());
      } finally {
        if (mediaResponseBody != null) {
          mediaResponseBody.close();
        }
      }

      return fileBody;
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
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
