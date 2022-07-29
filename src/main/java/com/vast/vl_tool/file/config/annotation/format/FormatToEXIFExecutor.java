package com.vast.vl_tool.file.config.annotation.format;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.lang.GeoLocation;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.GpsDirectory;
import com.vast.vl_tool.file.entity.ExifFileBody;
import com.vast.vl_tool.file.entity.FileBody;

import java.io.IOException;

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
    FileBody fileBody = exifFileBody.getFileBody();

    try {
      Metadata metadata = JpegMetadataReader.readMetadata(fileBody.getFile());

      Iterable<Directory> directories = metadata.getDirectories();

      for (Object d : directories) {
        if (!(d instanceof GpsDirectory)) {
          continue;
        }

        GeoLocation geoLocation = ((GpsDirectory) d).getGeoLocation();

        if (geoLocation == null) {
          continue;
        }

        exifFileBody.setLatitude(geoLocation.getLatitude());
        exifFileBody.setLongitude(geoLocation.getLongitude());
        break;
      }

      setFormattedResult(exifFileBody);

    } catch (JpegProcessingException e) {
      throw new IOException(e.getMessage());
    }
  }
}
