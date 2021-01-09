package com.frx.discstalker.controller;

import com.frx.discstalker.statistics.StatisticsProvider;
import com.frx.discstalker.statistics.Statistic;
import com.frx.discstalker.view.ViewUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.util.Objects;

/**
 * Created by surjak on 19.12.2020
 */
public class StatisticsController {

  @FXML
  private TableView tableView;

  @FXML
  private NotificationController notificationController;

  private StatisticsProvider statisticsProvider;

  private static final String STAT_DESCRIPTION_COLUMN_TITLE = "Description";
  private static final String STAT_VALUE_COLUMN_TITLE = "Value";

  @FXML
  public void initialize() {
    tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    TableColumn<Statistic, String> statDescription = new TableColumn<>(STAT_DESCRIPTION_COLUMN_TITLE);
    TableColumn<Statistic, Object> statValue = new TableColumn<>(STAT_VALUE_COLUMN_TITLE);

    statDescription.setCellValueFactory(param -> param.getValue().getName());
    statValue.setCellValueFactory(param -> param.getValue().getValue());
    statValue.setCellFactory(ViewUtils.wrapTextInCellFactory());

    tableView.getColumns().addAll(statDescription, statValue);

  }

  public void registerStatisticModel(StatisticsProvider statisticsProvider) {
    Objects.requireNonNull(statisticsProvider);
    this.statisticsProvider = statisticsProvider;
    tableView.setItems(statisticsProvider.getStatisticList());
    notificationController.registerStatisticModel(statisticsProvider);
  }
}
