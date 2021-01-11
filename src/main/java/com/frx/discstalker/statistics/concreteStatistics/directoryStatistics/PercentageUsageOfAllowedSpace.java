package com.frx.discstalker.statistics.concreteStatistics.directoryStatistics;

import com.frx.discstalker.model.DirectoryNode;
import com.frx.discstalker.service.notification.maxsize.AlmostMaxSizeDirectoryNotification;
import com.frx.discstalker.service.notification.maxsize.ReachMaxSizeDirectoryNotification;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.LongBinding;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;

import java.util.List;

/**
 * Created by surjak on 20.12.2020
 */
public class PercentageUsageOfAllowedSpace extends BaseDirectoryStatistics {

  private static final String STATISTIC_NAME = "Percentage usage of allowed space";
  private final static Long DEFAULT_MAX_DIRECTORY_SIZE = 2000000L;
  private final SimpleDoubleProperty freeSpaceProperty = new SimpleDoubleProperty(1);
  private final SimpleDoubleProperty usedSpaceProperty = new SimpleDoubleProperty(0);
  private Long maxSizeInMB;

  public PercentageUsageOfAllowedSpace() {
    this(DEFAULT_MAX_DIRECTORY_SIZE);
  }

  public PercentageUsageOfAllowedSpace(Long maxSizeInMB) {
    super(STATISTIC_NAME);
    this.maxSizeInMB = maxSizeInMB;
    addListeners();
  }

  public void setMaxSizeInMB(Long maxSizeInMB) {
    this.maxSizeInMB = maxSizeInMB;
  }

  @Override
  public void calculateValue(List<DirectoryNode> listWithRootElementAsFirstIndex) {
    DirectoryNode directoryNode = listWithRootElementAsFirstIndex.get(0);
    Long rootSize = directoryNode.getSize();
    double rootSizeInMB = convertToMB(rootSize);
    double percentageSize = rootSizeInMB / maxSizeInMB;
    double roundedPercentage = Math.round(percentageSize * 100);
    setContent(String.valueOf(roundedPercentage));
    setChartContent(rootSizeInMB);
  }

  @Override
  public Node getValueAsNode() {
    PieChart pieChart = new PieChart(FXCollections.observableArrayList(
      prepareFreeSpaceData(),
      prepareUsedSpaceData()
    ));
    pieChart.setAnimated(false);
    return pieChart;
  }

  private PieChart.Data prepareFreeSpaceData() {
    PieChart.Data free = new PieChart.Data("Free", maxSizeInMB);
    free.pieValueProperty().bind(freeSpaceProperty);
    LongBinding longBinding = Bindings.createLongBinding(
      () -> Math.round(freeSpaceProperty.get() / maxSizeInMB * 100),
      freeSpaceProperty);
    free.nameProperty().bind(Bindings.concat(free.getName(), " ", longBinding, " %"));
    return free;
  }

  private PieChart.Data prepareUsedSpaceData() {
    PieChart.Data used = new PieChart.Data("Used", 0);
    used.pieValueProperty().bind(usedSpaceProperty);
    used.nameProperty().bind(Bindings.concat(used.getName(), " ", getValue(), " %"));
    return used;
  }

  private void addListeners() {
    getValue().addListener((observable, oldValue, newValue) -> {
      long percentageUsage = Math.round(Double.parseDouble(newValue.toString()));
      if (percentageUsage > AlmostMaxSizeDirectoryNotification.ALMOST_MAX_SIZE) {
        new AlmostMaxSizeDirectoryNotification(maxSizeInMB * percentageUsage / 100, maxSizeInMB).show();
      }
      if (percentageUsage > 100) {
        new ReachMaxSizeDirectoryNotification(maxSizeInMB * percentageUsage / 100, maxSizeInMB).show();
      }
    });
  }

  private void setChartContent(double rootSizeInMB) {
    if (rootSizeInMB < maxSizeInMB) {
      this.freeSpaceProperty.set(maxSizeInMB - rootSizeInMB);
      this.usedSpaceProperty.set(rootSizeInMB);
    } else {
      this.freeSpaceProperty.set(0);
      this.usedSpaceProperty.set(1);
    }
  }

  private double convertToMB(Long rootSize) {
    return (double) rootSize / (1e6);
  }
}
