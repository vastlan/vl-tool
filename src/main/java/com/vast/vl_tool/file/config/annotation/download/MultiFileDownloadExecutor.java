package com.vast.vl_tool.file.config.annotation.download;

import com.vast.vl_tool.file.entity.FileBody;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import jakarta.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipOutputStream;

/**
 * @author vastlan
 * @description
 * @created 2022/7/26 16:33
 */
public class MultiFileDownloadExecutor extends AbstractIODownloadExecutor {

  public MultiFileDownloadExecutor(HttpServletResponse httpServletResponse, List<FileBody> fileBodyList) {
    super(httpServletResponse, fileBodyList, "DataPackage", "zip");
  }

  public MultiFileDownloadExecutor(HttpServletResponse httpServletResponse, List<FileBody> fileBodyList, boolean withLocation) {
    super(httpServletResponse, fileBodyList, "DataPackage", "zip");
    this.withLocation = withLocation;
  }

  @Override
  public void execute() throws IOException {
    List<FileBody> fileBodyList = (List<FileBody>) content;

    try (ZipOutputStream zipOutputStream = new ZipOutputStream(new BufferedOutputStream(response.getOutputStream()), StandardCharsets.UTF_8)) {
      for (FileBody fileBody : fileBodyList) {
        try {
          if (!fileBody.existFile()) {
            System.err.println("找不到文件" + fileBody.getFileName());
            continue;
          }

          ZipEntry zipEntry = new ZipEntry(fileBody.getFileName());
          zipOutputStream.putNextEntry(zipEntry);

          try (InputStream inputStream = Files.newInputStream(fileBody.getPath())) {
//            inputStream.transferTo(zipOutputStream);
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

                ImageIO.write(image, fileNameSuffix, zipOutputStream);
              } catch (IOException e) {
                e.printStackTrace();
                inputStream.transferTo(zipOutputStream);
              }
            } else {
              inputStream.transferTo(zipOutputStream);
            }
          }
        } catch (ZipException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
