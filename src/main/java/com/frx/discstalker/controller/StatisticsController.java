package com.frx.discstalker.controller;

import com.frx.discstalker.statistics.StatisticsProvider;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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

  public void registerStatisticModel(StatisticsProvider statisticsProvider) {
    Objects.requireNonNull(statisticsProvider);
    this.statisticsProvider = statisticsProvider;

    statisticsProvider
      .getStatisticList()
      .stream()
      .forEach(statistic -> {
        Tab tab = new Tab(statistic.getName().get(), statistic.getValueAsNode());
        statisticTabs.getTabs().add(tab);
      });

    notificationController.registerStatisticModel(statisticsProvider);
  }
}
