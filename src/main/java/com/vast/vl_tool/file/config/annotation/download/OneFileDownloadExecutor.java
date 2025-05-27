package com.vast.vl_tool.file.config.annotation.download;

import com.vast.vl_tool.file.entity.FileBody;
import org.springframework.util.StringUtils;

import jakarta.servlet.http.HttpServletResponse;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author vastlan
 * @description
 * @created 2022/7/26 15:23
 */

public class OneFileDownloadExecutor extends AbstractIODownloadExecutor {

  public OneFileDownloadExecutor(HttpServletResponse response, FileBody fileBody) {
    super(response, fileBody, fileBody.getFileName());
  }

  public OneFileDownloadExecutor(HttpServletResponse response, FileBody fileBody, boolean withLocation) {
    super(response, fileBody, fileBody.getFileName());
    this.withLocation = withLocation;
  }

  @Override
  public void execute() throws IOException{
    FileBody fileBody = (FileBody) content;

    if (!fileBody.existFile()) {
      throw new FileNotFoundException("找不到指定文件");
    }

    try (InputStream inputStream= new FileInputStream(fileBody.getFile())) {
      if (withLocation) {
        try {
          // 读取图片
          BufferedImage image = ImageIO.read(inputStream);

          // 处理水印
          Graphics2D g2d = image.createGraphics();
          Font font = new Font("Arial", Font.BOLD, 10);
          g2d.setFont(font);
          g2d.setColor(new Color(255, 0, 0, 150));
          g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

          FontMetrics fontMetrics = g2d.getFontMetrics();

          String locationStr = "";

          if (StringUtils.hasLength(fileBody.getLongitude()) && StringUtils.hasLength(fileBody.getLatitude()) && StringUtils.hasLength(fileBody.getAltitude())) {
            locationStr = fileBody.getLongitude() + " " + fileBody.getLatitude() + " " + fileBody.getAltitude();
          }

          int textWidth = fontMetrics.stringWidth(locationStr);
          int x = image.getWidth() - textWidth - 10;
          int y = image.getHeight() - 20;

          g2d.drawString(locationStr, x, y);
          g2d.dispose();

          ImageIO.write(image, fileNameSuffix, response.getOutputStream());
        } catch (IOException e) {
          e.printStackTrace();
          inputStream.transferTo(response.getOutputStream());
        }
      } else {
        inputStream.transferTo(response.getOutputStream());
      }
    }
  }
}
