package com.frx.discstalker.statistics.concreteStatistics.directoryStatistics;

import com.frx.discstalker.Utils;
import com.frx.discstalker.model.DirectoryNode;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.LongBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;

import java.util.List;
import java.util.Objects;

/**
 * Created by surjak on 20.12.2020
 */
public class PercentageUsageOfAllowedSpace extends BaseDirectoryStatistics<Integer> {

  private static final String STATISTIC_NAME = "Percentage usage of allowed space";
  public final static Long DEFAULT_MAX_DIRECTORY_SIZE = 100L;
  private final SimpleDoubleProperty freeSpaceProperty = new SimpleDoubleProperty(1);
  private final SimpleDoubleProperty usedSpaceProperty = new SimpleDoubleProperty(0);
  private final SimpleLongProperty maxSizeInMB = new SimpleLongProperty(DEFAULT_MAX_DIRECTORY_SIZE);
  private final ObjectProperty<Integer> value = new SimpleObjectProperty<>(0);

  public PercentageUsageOfAllowedSpace() {
    this(DEFAULT_MAX_DIRECTORY_SIZE);
  }

  public PercentageUsageOfAllowedSpace(Long maxSizeInMB) {
    super(STATISTIC_NAME);
    this.setMaxSizeInMB(maxSizeInMB);
  }

  public void setMaxSizeInMB(Long maxSizeInMB) {
    this.maxSizeInMB.set(Objects.requireNonNullElse(maxSizeInMB, DEFAULT_MAX_DIRECTORY_SIZE));
  }
  public long getMaxSizeInMB() {
    return maxSizeInMB.get();
  }

  @Override
  public ObjectProperty<Integer> getValue() {
    return value;
  }

  @Override
  public void calculateValue(List<DirectoryNode> listWithRootElementAsFirstIndex) {
    DirectoryNode directoryNode = listWithRootElementAsFirstIndex.get(0);
    Long rootSize = directoryNode.getSize();
    double rootSizeInMB = Utils.convertToMB(rootSize);
    double percentageSize = rootSizeInMB / maxSizeInMB.getValue();
    int roundedPercentage = (int) Math.round(percentageSize * 100);
    value.set(roundedPercentage);
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
    PieChart.Data free = new PieChart.Data("Free", maxSizeInMB.getValue());
    free.pieValueProperty().bind(freeSpaceProperty);
    LongBinding longBinding = Bindings.createLongBinding(
      () -> Math.round(freeSpaceProperty.get() / maxSizeInMB.getValue() * 100),
      freeSpaceProperty);
    free.nameProperty().bind(Bindings.concat(free.getName(), " ", longBinding, " %"));
    return free;
  }

  private PieChart.Data prepareUsedSpaceData() {
    PieChart.Data used = new PieChart.Data("Used", 0);
    used.pieValueProperty().bind(usedSpaceProperty);
    used.nameProperty().bind(Bindings.concat(used.getName(), " ", getTextValue(), " %"));
    return used;
  }

  private void setChartContent(double rootSizeInMB) {
    if (rootSizeInMB < maxSizeInMB.getValue()) {
      this.freeSpaceProperty.set(maxSizeInMB.getValue() - rootSizeInMB);
      this.usedSpaceProperty.set(rootSizeInMB);
    } else {
      this.freeSpaceProperty.set(0);
      this.usedSpaceProperty.set(1);
    }
  }
}
