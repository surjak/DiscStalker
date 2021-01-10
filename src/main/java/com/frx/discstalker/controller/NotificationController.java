package com.frx.discstalker.controller;

import com.frx.discstalker.service.notification.ErrorNotification;
import com.frx.discstalker.statistics.StatisticsProvider;
import com.frx.discstalker.statistics.concreteStatistics.directoryStatistics.PercentageUsageOfAllowedSpace;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.util.Objects;

/**
 * Created by nazkord on 04.01.2021
 */
public class NotificationController {

  private static final String NOTIFICATION_TITLE = "Maximum size in MB of the chosen directory";
  private StatisticsProvider statisticsProvider;

  @FXML
  private Text notificationTitle;

  @FXML
  private TextField maximumSizeField;

  @FXML
  private Button setButton;

  @FXML
  public void initialize() {
    notificationTitle.setText(NOTIFICATION_TITLE);
    setUpOnlyNumericValuesForTextField();
  }

  @FXML
  private void handleSetAction(ActionEvent event) {
    Long newMaxSizeInMB = getNumericValueFromTextField();
    statisticsProvider.findConcreteStatisticBy(PercentageUsageOfAllowedSpace.class)
      .ifPresent(statistic -> ((PercentageUsageOfAllowedSpace) statistic).setMaxSizeInMB(newMaxSizeInMB));
    statisticsProvider.calculateStatistics();
  }

  public void registerStatisticModel(StatisticsProvider statisticsProvider) {
    Objects.requireNonNull(statisticsProvider);
    this.statisticsProvider = statisticsProvider;
  }

  private void setUpOnlyNumericValuesForTextField() {
    maximumSizeField.textProperty().addListener((observable, oldValue, newValue) -> {
      if (!newValue.matches("\\d{0,9}")) {
        maximumSizeField.setText(oldValue);
      }
    });
  }

  private Long getNumericValueFromTextField() {
    try {
      return Long.parseLong(maximumSizeField.getText());
    } catch (NumberFormatException e) {
      new ErrorNotification("Bad input: " + maximumSizeField.getText(), "You must provide the numeric value without dot")
        .show();
      System.out.println("Error parsing int (" + maximumSizeField.getText() + ") from field." + e);
      return 0L;
    }
  }
}
