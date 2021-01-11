package com.frx.discstalker.controller;

import com.frx.discstalker.statistics.StatisticsProvider;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.util.Objects;

/**
 * Created by surjak on 19.12.2020
 */
public class StatisticsController {

  @FXML
  private TabPane statisticTabs;

  @FXML
  private NotificationController notificationController;

  private StatisticsProvider statisticsProvider;

  @FXML
  public void initialize() {
    statisticTabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
  }

  public StatisticsProvider getStatisticsProvider() {
    return statisticsProvider;
  }

  public void registerStatisticModel(StatisticsProvider statisticsProvider) {
    Objects.requireNonNull(statisticsProvider);
    this.statisticsProvider = statisticsProvider;

    statisticsProvider
      .getStatisticList()
      .forEach(statistic -> statisticTabs.getTabs().add(new Tab(statistic.getName().get(), statistic.getValueAsNode())));

    notificationController.registerStatisticModel(statisticsProvider);
  }
}
