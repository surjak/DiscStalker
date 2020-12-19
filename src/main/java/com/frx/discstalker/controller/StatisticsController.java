package com.frx.discstalker.controller;

import com.frx.discstalker.stats.StatisticsProvider;
import com.frx.discstalker.stats.Statistic;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.Objects;

/**
 * Created by surjak on 19.12.2020
 */
public class StatisticsController {

  @FXML
  private TableView tableView;

  private StatisticsProvider statisticsProvider;

  @FXML
  public void initialize() {
    tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    TableColumn<Statistic, String> statDescription = new TableColumn<>("Description");
    TableColumn<Statistic, Object> statValue = new TableColumn<>("Value");

    statDescription.setCellValueFactory(param -> param.getValue().getName());
    statValue.setCellValueFactory(param -> param.getValue().getValue());

    tableView.getColumns().addAll(statDescription, statValue);

  }

  public void registerStatisticModel(StatisticsProvider statisticsProvider) {
    Objects.requireNonNull(statisticsProvider);
    this.statisticsProvider = statisticsProvider;
    tableView.setItems(statisticsProvider.getStatisticList());
  }
}
