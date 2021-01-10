package com.frx.discstalker.statistics.concreteStatistics.directoryStatistics;

import com.frx.discstalker.model.DirectoryNode;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import java.util.List;

/**
 * Created by surjak on 20.12.2020
 */
public class PercentageUsageOfAllowedSpace extends BaseDirectoryStatistics {

  private static final String STATISTIC_NAME = "Percentage usage of allowed space";
  private final static Long DEFAULT_MAX_DIRECTORY_SIZE = 200L;
  private Long maxSizeInMB;
  private SimpleDoubleProperty freeSpaceProperty = new SimpleDoubleProperty(1);
  private SimpleDoubleProperty usedSpaceProperty = new SimpleDoubleProperty(0);

  public PercentageUsageOfAllowedSpace() {
    super(STATISTIC_NAME);
    this.maxSizeInMB = DEFAULT_MAX_DIRECTORY_SIZE;
  }

  public PercentageUsageOfAllowedSpace(Long maxSizeInMB) {
    super(STATISTIC_NAME);
    this.maxSizeInMB = maxSizeInMB;
  }

  @Override
  public void calculateValue(List<DirectoryNode> listWithRootElementAsFirstIndex) {
    DirectoryNode directoryNode = listWithRootElementAsFirstIndex.get(0);
    Long rootSize = directoryNode.getSize();
    double rootSizeInMB = convertToMB(rootSize);
    double percentageSize = rootSizeInMB / maxSizeInMB;
    int val = (int) (percentageSize * 100);
    setContent(val + "%");
    setChartContent(rootSizeInMB);
  }

  private void setChartContent(double rootSizeInMB) {
    if(rootSizeInMB < maxSizeInMB){
      this.freeSpaceProperty.set(maxSizeInMB - rootSizeInMB);
      this.usedSpaceProperty.set(rootSizeInMB);
    }else {
      this.freeSpaceProperty.set(0);
      this.usedSpaceProperty.set(1);
    }

  }

  private double convertToMB(Long rootSize) {
    return (double) rootSize / (1e6);
  }

  @Override
  public Node getValueAsNode() {
    PieChart.Data free = new PieChart.Data("Free", maxSizeInMB);
    free.pieValueProperty().bind(freeSpaceProperty);
    PieChart.Data used = new PieChart.Data("Used", 0);
    used.pieValueProperty().bind(usedSpaceProperty);
    ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
      free,
      used
    );
    PieChart pieChart = new PieChart();
    pieChart.dataProperty().setValue(pieChartData);
    return pieChart;
  }

  public void setMaxSizeInMB(Long maxSizeInMB) {
    this.maxSizeInMB = maxSizeInMB;
  }
}
