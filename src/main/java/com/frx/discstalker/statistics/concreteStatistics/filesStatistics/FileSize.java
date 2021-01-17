package com.frx.discstalker.statistics.concreteStatistics.filesStatistics;

import com.frx.discstalker.model.FileNode;
import com.frx.discstalker.statistics.concreteStatistics.filesStatistics.valueConverters.IntegerStringConverter;
import com.google.inject.internal.cglib.core.$AbstractClassGenerator;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;

import java.util.*;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingLong;

/**
 * Created by surjak on 19.12.2020
 */
public class FileSize extends BaseFilesStatistic<Map<String, Long>> {

  private static final String STATISTIC_NAME = "File size grouped by file type";
  private static final String X_LABEL = "File Type/Extension";
  private static final String Y_LABEL = "Grouped file size [MB]";
  private DoubleProperty doubleProperty = new SimpleDoubleProperty(400);
  private ObservableList<XYChart.Data<String, Number>> chartData = FXCollections.observableArrayList();
  private ObservableList<String> keys = FXCollections.observableArrayList();
  private ObjectProperty<Map<String, Long>> value = new SimpleObjectProperty<>(Map.of());

  public FileSize() {
    super(STATISTIC_NAME);
  }

  @Override
  public ObjectProperty<Map<String, Long>> getValue() {
    return value;
  }

  @Override
  public void calculateValue(List<FileNode> fileSystemNodes) {
    Map<String, Long> statisticByType = fileSystemNodes.stream()
      .collect(groupingBy(FileNode::getExtension, summingLong(FileNode::getSize)));

    writeIntoValue(statisticByType);
    fillChartData(statisticByType);
  }

  private void fillChartData(Map<String, Long> statisticByType) {
    removeAllElementsFromDataList();
    removeAllElementsFromKeysList();
    fillListsWithData(statisticByType);
  }

  private void fillListsWithData(Map<String, Long> statisticByType) {
    statisticByType.entrySet()
      .stream()
      .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
      .forEach(stringLongEntry -> {
        keys.add(stringLongEntry.getKey());
        chartData.add(new XYChart.Data<>(stringLongEntry.getKey(), convertToMB(stringLongEntry.getValue())));
        doubleProperty.setValue(chartData.size() * 100);
      });
  }

  private void writeIntoValue(Map<String, Long> statisticByType) {
    value.set(statisticByType);

    StringBuffer buffer = new StringBuffer();
    statisticByType.entrySet()
      .forEach(stringLongEntry -> buffer.append(stringLongEntry.getKey() + " : " + stringLongEntry.getValue() + "\n"));
    setContent(buffer.toString());
  }

  private void removeAllElementsFromKeysList() {
    keys.removeIf(key -> true);
  }

  private void removeAllElementsFromDataList() {
    chartData.removeIf(data -> true);
  }

  @Override
  public Node getValueAsNode() {
    ScrollPane scrollPane = createHorizontalScrollPane();

    CategoryAxis xAxis = createXAxis();
    NumberAxis yAxis = createYAxis();

    XYChart.Series<String, Number> series = new XYChart.Series<>();
    series.dataProperty().setValue(chartData);

    BarChart<String, Number> barChart = createBarChart(xAxis, yAxis, series);
    barChart.setAnimated(false);
    scrollPane.setContent(barChart);

    return scrollPane;
  }

  private BarChart<String, Number> createBarChart(CategoryAxis xAxis, NumberAxis yAxis, XYChart.Series<String, Number> series) {
    BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
    barChart.getData().add(series);
    barChart.minWidthProperty().bind(doubleProperty);
    barChart.prefWidthProperty().bind(xAxis.widthProperty());
    return barChart;
  }

  private NumberAxis createYAxis() {
    NumberAxis yAxis = new NumberAxis();
    yAxis.setLabel(Y_LABEL);
    yAxis.setTickLabelFormatter(new IntegerStringConverter());
    yAxis.setMinorTickVisible(false);
    return yAxis;
  }

  private CategoryAxis createXAxis() {
    CategoryAxis xAxis = new CategoryAxis();
    xAxis.setCategories(keys);
    xAxis.setLabel(X_LABEL);
    xAxis.setSide(Side.TOP);
    return xAxis;
  }

  private ScrollPane createHorizontalScrollPane() {
    ScrollPane scrollPane = new ScrollPane();
    scrollPane.setFitToHeight(true);
    scrollPane.setFitToWidth(true);
    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
    scrollPane.fitToWidthProperty();
    return scrollPane;
  }

  private static double convertToMB(Long sizeInBytes) {
    return (double) sizeInBytes / (1e6);
  }
}
