package com.vast.vl_tool.file.config.annotation.format;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.lang.GeoLocation;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.GpsDirectory;
import com.drew.metadata.xmp.XmpDirectory;
import com.vast.vl_tool.file.constant.FileType;
import com.vast.vl_tool.file.entity.ExifFileBody;
import com.vast.vl_tool.file.entity.FileBody;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;

/**
 * @author vastlan
 * @description
 * @use
 *  tool
 *    .format()
 *    .content(xxx)
 *    .toEXIF()
 *    .invoke()
 * @created 2022/7/28 18:28
 */
public class FormatToEXIFExecutor extends AbstractIOFormatExecutor {

  public FormatToEXIFExecutor(Object body) {
    super(body);
  }

  @Override
  public void execute() throws IOException {
    ExifFileBody exifFileBody = (ExifFileBody) body;

    try (FileBody fileBody = exifFileBody.getFileBody()) {
      Assert.notNull(fileBody.getInputStream(), "stream must not be null");

      Metadata metadata = JpegMetadataReader.readMetadata(fileBody.getInputStream());

      Iterable<Directory> directories = metadata.getDirectories();

      for (Object d : directories) {
        if (d instanceof GpsDirectory) {
          GeoLocation geoLocation = ((GpsDirectory) d).getGeoLocation();

          if (geoLocation != null) {
            exifFileBody.setLatitude(geoLocation.getLatitude());
            exifFileBody.setLongitude(geoLocation.getLongitude());
          }
        }

        if (d instanceof XmpDirectory) {
          Map<String, String> xmpProperties = ((XmpDirectory) d).getXmpProperties();

          if (xmpProperties != null) {
            final String usePanoramaViewer = "GPano:UsePanoramaViewer";

            if (xmpProperties.containsKey(usePanoramaViewer)) {
              boolean isTrue = Boolean.parseBoolean(xmpProperties.get(usePanoramaViewer));
              exifFileBody.setFileType(isTrue ? FileType.PICTURE_PANORAMA : FileType.PICTURE);
            }
          }
        }
      }

      setFormattedResult(exifFileBody);

    } catch (JpegProcessingException e) {
      throw new IOException(e.getMessage());
    }
  }
}
