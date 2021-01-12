package com.frx.discstalker.utils;

import com.frx.discstalker.controller.LiveDirectoryController;
import com.frx.discstalker.model.TabConfig;
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
public class TabConfigUtil {

  public Collection<TabConfig> readFileInfosFromJson(File file) throws FileNotFoundException {
    BufferedReader br = new BufferedReader(new FileReader(file));
    Type listType = new TypeToken<Collection<TabConfig>>(){}.getType();
    return new Gson().fromJson(br, listType);
  }

  public List<TabConfig> getFileInfosFrom(Collection<LiveDirectoryController> liveDirectoryControllers) {
    List<TabConfig> tabConfigs = new ArrayList<>();
    for (LiveDirectoryController controller : liveDirectoryControllers) {
      String tabPath = controller.getPathString();
      Long maxSize = controller
        .getStatisticsController()
        .getStatisticsProvider()
        .findConcreteStatisticBy(PercentageUsageOfAllowedSpace.class)
        .map(PercentageUsageOfAllowedSpace.class::cast)
        .map(PercentageUsageOfAllowedSpace::getMaxSizeInMB)
        .orElse(PercentageUsageOfAllowedSpace.DEFAULT_MAX_DIRECTORY_SIZE);
      tabConfigs.add(new TabConfig(tabPath, maxSize, true));
    }
    return tabConfigs;
  }

  public void writeFileInfosTo(List<TabConfig> tabConfigs, File file) {
      try (var writer = new FileWriter(file)) {
        var gson = new GsonBuilder().setPrettyPrinting().create();
        gson.toJson(tabConfigs, writer);
      } catch (IOException ex) {
        ex.printStackTrace();
      }
  }
}
