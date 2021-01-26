package com.frx.discstalker.controller;

import com.frx.discstalker.fs.LiveDirectoryTree;
import com.frx.discstalker.statistics.StatisticsProvider;
import com.frx.discstalker.statistics.concreteStatistics.StatisticCalculator;
import com.frx.discstalker.statistics.concreteStatistics.directoryStatistics.DirectoryStatisticsCalculator;
import com.frx.discstalker.statistics.concreteStatistics.filesStatistics.FileStatisticsCalculator;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.util.List;

/**
 * Created by surjak on 19.12.2020
 */
public class StatisticsController {

  @FXML
  private TabPane statisticTabs;

  @FXML
  private NotificationController notificationController;

  private StatisticsProvider statisticsProvider;

  public NotificationController getNotificationController() {
    return notificationController;
  }

  @FXML
  public void initialize() {
    statisticTabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
  }

  public StatisticsProvider getStatisticsProvider() {
    return statisticsProvider;
  }

  public void registerStatisticModel(LiveDirectoryTree liveDirectoryTree) {
    List<StatisticCalculator> statisticCalculators = List.of(new FileStatisticsCalculator());
    this.statisticsProvider = new StatisticsProvider(liveDirectoryTree, statisticCalculators);
    statisticCalculators.forEach(this::addTabsForStatistics);

    notificationController.registerStatisticModel(statisticsProvider);
    addListener();
  }

  private void addListener() {
    notificationController.isMaximumSizeSet.addListener((observable, oldValue, newValue) -> {
      if(newValue) {
        registerCalculator(new DirectoryStatisticsCalculator());
      }
    });
  }

  public void registerCalculator(StatisticCalculator statisticCalculator) {
    statisticsProvider.registerCalculator(statisticCalculator);
    addTabsForStatistics(statisticCalculator);
  }

  private void addTabsForStatistics(StatisticCalculator statisticCalculator) {
    statisticCalculator
      .getStatistics()
      .forEach(statistic -> statisticTabs.getTabs().add(new Tab(statistic.getName().get(), statistic.getValueAsNode())));
  }
}
