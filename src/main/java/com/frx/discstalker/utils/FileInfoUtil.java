package com.frx.discstalker.utils;

import com.frx.discstalker.controller.LiveDirectoryController;
import com.frx.discstalker.model.FileInfo;
import com.frx.discstalker.statistics.concreteStatistics.directoryStatistics.PercentageUsageOfAllowedSpace;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import javax.inject.Singleton;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Singleton
public class FileInfoUtil {

  public Collection<FileInfo> readFileInfosFromJson(File file) throws FileNotFoundException {
    BufferedReader br = new BufferedReader(new FileReader(file));
    Type listType = new TypeToken<Collection<FileInfo>>(){}.getType();
    return new Gson().fromJson(br, listType);
  }

  public List<FileInfo> getFileInfosFrom(Collection<LiveDirectoryController> liveDirectoryControllers) {
    List<FileInfo> fileInfos = new ArrayList<>();
    for (LiveDirectoryController controller : liveDirectoryControllers) {
      String tabPath = controller.getPathString();
      Long maxSize = controller
        .getStatisticsController()
        .getStatisticsProvider()
        .findConcreteStatisticBy(PercentageUsageOfAllowedSpace.class)
        .map(PercentageUsageOfAllowedSpace.class::cast)
        .map(PercentageUsageOfAllowedSpace::getMaxSizeInMB)
        .orElse(PercentageUsageOfAllowedSpace.DEFAULT_MAX_DIRECTORY_SIZE);
      fileInfos.add(new FileInfo(tabPath, maxSize, true));
    }
    return fileInfos;
  }

  public void writeFileInfosTo(List<FileInfo> fileInfos, File file) {
      try (var writer = new FileWriter(file)) {
        var gson = new GsonBuilder().setPrettyPrinting().create();
        gson.toJson(fileInfos, writer);
      } catch (IOException ex) {
        ex.printStackTrace();
      }
  }
}
