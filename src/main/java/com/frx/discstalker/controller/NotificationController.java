package com.frx.discstalker.controller;

import com.frx.discstalker.service.notification.ErrorNotification;
import com.frx.discstalker.service.notification.maxsize.AlmostMaxSizeDirectoryNotification;
import com.frx.discstalker.service.notification.maxsize.ReachMaxSizeDirectoryNotification;
import com.frx.discstalker.statistics.StatisticsProvider;
import com.frx.discstalker.statistics.concreteStatistics.directoryStatistics.PercentageUsageOfAllowedSpace;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
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

  private static final String NOTIFICATION_TITLE = "Alert for maximum size in MB of the chosen directory";
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
    setNewMaximumSize(getNumericValueFromTextField());
  }

  public void setNewMaximumSize(long newMaximumSize) {
    statisticsProvider.findConcreteStatisticBy(PercentageUsageOfAllowedSpace.class)
      .ifPresent(statistic -> ((PercentageUsageOfAllowedSpace) statistic).setMaxSizeInMB(newMaximumSize));
    statisticsProvider.calculateStatistics();
  }

  public void registerStatisticModel(StatisticsProvider statisticsProvider) {
    Objects.requireNonNull(statisticsProvider);
    this.statisticsProvider = statisticsProvider;
    // Notification observers depend on statistics, so we have to wait for the provider to be registered
    this.initializeNotifications();
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
    } finally {
      maximumSizeField.clear();
    }
  }

  private void initializeNotifications() {
    statisticsProvider.findConcreteStatisticBy(PercentageUsageOfAllowedSpace.class)
      .ifPresent(statistic -> {
        JavaFxObservable.valuesOf(statistic.getValue())
          .subscribe(usage -> {
            this.checkForNotifications(usage, statistic);
          });
      });
  }

  private void checkForNotifications(int percentageUsage, PercentageUsageOfAllowedSpace statistic) {
    final var maxSizeInMB = statistic.getMaxSizeInMB();

    if (percentageUsage >= 100) {
      new ReachMaxSizeDirectoryNotification(maxSizeInMB * percentageUsage / 100, maxSizeInMB).show();
    } else if (percentageUsage >= AlmostMaxSizeDirectoryNotification.ALMOST_MAX_SIZE) {
      new AlmostMaxSizeDirectoryNotification(maxSizeInMB * percentageUsage / 100, maxSizeInMB).show();
    }
  }
}
