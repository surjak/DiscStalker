package com.frx.discstalker.controller;

import com.frx.discstalker.Utils;
import com.frx.discstalker.service.notification.ErrorNotification;
import com.frx.discstalker.service.notification.filetree.ReachMaxFileSizeNotification;
import com.frx.discstalker.service.notification.filetree.ReachMaxNumberOfFilesNotification;
import com.frx.discstalker.service.notification.filetree.AlmostMaxSizeDirectoryNotification;
import com.frx.discstalker.service.notification.filetree.ReachMaxSizeDirectoryNotification;
import com.frx.discstalker.statistics.StatisticsProvider;
import com.frx.discstalker.statistics.concreteStatistics.directoryStatistics.PercentageUsageOfAllowedSpace;
import com.frx.discstalker.statistics.concreteStatistics.filesStatistics.NLargestFiles;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Created by nazkord on 04.01.2021
 */
public class NotificationController {

  private StatisticsProvider statisticsProvider;

  @FXML
  private TextField maximumSizeField;

  private Optional<Long> maximumSize = Optional.empty();

  @FXML
  private Text maximumSizeDescription;

  @FXML
  private TextField maximumNumberOfFilesField;

  private Optional<Long> maximumNumberOfFiles = Optional.empty();

  @FXML
  private Text maximumNumberOfFilesDescription;

  @FXML
  private TextField maximumFileSizeField;

  private Optional<Long> maximumFileSize = Optional.empty();

  @FXML
  private Text maximumFileSizeDescription;

  @FXML
  public void initialize() {
    setUpOnlyNumericValuesForTextField();
  }

  @FXML
  private void handleSetMaximumSizeClick(ActionEvent event) {
    final var size = getNumericValueFromTextField(maximumSizeField);
    setNewMaximumSize(size);
  }

  @FXML
  private void handleSetMaximumNumberOfFilesClick(ActionEvent event) {
    final var numberOfFiles = getNumericValueFromTextField(maximumNumberOfFilesField);
    setNewMaximumNumberOfFiles(numberOfFiles);
  }

  @FXML
  private void handleSetMaximumFileSizeClick(ActionEvent event) {
    final var fileSize = getNumericValueFromTextField(maximumFileSizeField);
    setNewMaximumFileSize(fileSize);
  }

  public void setNewMaximumSize(Long newMaximumSize) {
    this.maximumSize = Optional.ofNullable(newMaximumSize);

    this.maximumSize.ifPresentOrElse(maximumSize -> {
      statisticsProvider.findConcreteStatisticBy(PercentageUsageOfAllowedSpace.class)
        .ifPresent(statistic -> statistic.setMaxSizeInMB(maximumSize));
      statisticsProvider.calculateStatistics();

      this.maximumSizeDescription.setText("Set to " + maximumSize.toString() + " MB");
    }, () -> {
      this.maximumSizeDescription.setText("None");
    });
  }

  public void setNewMaximumNumberOfFiles(Long newMaximumNumberOfFiles) {
    this.maximumNumberOfFiles = Optional.ofNullable(newMaximumNumberOfFiles);

    this.maximumNumberOfFiles.ifPresentOrElse(numberOfFiles -> {
      this.maximumNumberOfFilesDescription.setText("Set to " + numberOfFiles.toString());
    }, () -> {
      this.maximumNumberOfFilesDescription.setText("None");
    });
  }

  public void setNewMaximumFileSize(Long newMaximumFileSize) {
    this.maximumFileSize = Optional.ofNullable(newMaximumFileSize);

    this.maximumFileSize.ifPresentOrElse(maximumFileSize -> {
      this.maximumFileSizeDescription.setText("Set to " + maximumFileSize.toString() + " MB");
    }, () -> {
      this.maximumFileSizeDescription.setText("None");
    });
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

  private Long getNumericValueFromTextField(TextField field) {
    try {
      return Long.parseLong(field.getText());
    } catch (NumberFormatException e) {
      new ErrorNotification("Bad input: " + field.getText(), "You must provide the numeric value without dot").show();
      System.out.println("Error parsing int (" + field.getText() + ") from field." + e);
      return 0L;
    } finally {
      field.clear();
    }
  }

  private void initializeNotifications() {
    initializeMaxDirectorySizeNotifications();
    initializeMaxNumberOfFilesNotifications();
    initializeMaxFileSizeNotifications();
  }

  private void initializeMaxDirectorySizeNotifications() {
    final var root = statisticsProvider.getLiveDirectoryTree().getRoot();

    JavaFxObservable.valuesOf(root.getSizeProperty())
      .map(Number::longValue)
      .map(Utils::convertToMB)
      .throttleLatest(5, TimeUnit.SECONDS)
      .subscribe(this::checkForMaxSizeNotifications);
  }

  private void initializeMaxNumberOfFilesNotifications() {
    final var root = statisticsProvider.getLiveDirectoryTree().getRoot();

    JavaFxObservable.valuesOf(root.getNumberOfFilesProperty())
      .map(Number::longValue)
      .throttleLatest(5, TimeUnit.SECONDS)
      .subscribe(this::checkForMaxNumberOfFilesNotifications);
  }

  private void initializeMaxFileSizeNotifications() {
    statisticsProvider.findConcreteStatisticBy(NLargestFiles.class)
      .ifPresent(statistic -> {
        JavaFxObservable.valuesOf(statistic.getValue())
          .filter(files -> !files.isEmpty())
          .map(files -> files.get(0).getSize())
          .map(Utils::convertToMB)
          .subscribe(this::checkForMaxFileSizeNotifications);
      });
  }

  private void checkForMaxSizeNotifications(Long sizeMB) {
    this.maximumSize.ifPresent(maximumSize -> {
      final var percentageUsage = sizeMB.doubleValue() / maximumSize * 100;

      if (percentageUsage >= 100) {
        new ReachMaxSizeDirectoryNotification(sizeMB, maximumSize).show();
      } else if (percentageUsage >= AlmostMaxSizeDirectoryNotification.ALMOST_MAX_SIZE) {
        new AlmostMaxSizeDirectoryNotification(sizeMB, maximumSize).show();
      }
    });
  }

  private void checkForMaxNumberOfFilesNotifications(Long numberOfFiles) {
    this.maximumNumberOfFiles.ifPresent(maximumNumberOfFiles -> {
      if (numberOfFiles > maximumNumberOfFiles) {
        new ReachMaxNumberOfFilesNotification(numberOfFiles, maximumNumberOfFiles).show();
      }
    });
  }

  private void checkForMaxFileSizeNotifications(Long fileSizeMB) {
    this.maximumFileSize.ifPresent(maximumFileSize -> {
      if (fileSizeMB > maximumFileSize) {
        new ReachMaxFileSizeNotification(fileSizeMB, maximumFileSize).show();
      }
    });
  }

  public Optional<Long> getMaximumSize() {
    return maximumSize;
  }

  public Optional<Long> getMaximumNumberOfFiles() {
    return maximumNumberOfFiles;
  }

  public Optional<Long> getMaximumFileSize() {
    return maximumFileSize;
  }
}
