package com.frx.discstalker.statistics.concreteStatistics.filesStatistics;

import com.frx.discstalker.model.FileNode;
import com.frx.discstalker.statistics.concreteStatistics.filesStatistics.valueConverters.IntegerStringConverter;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ScrollPane;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;

/**
 * Created by surjak on 19.12.2020
 */
public class NumberOfFiles extends BaseFilesStatistic {

  private static final String STATISTIC_NAME = "Number of files grouped by file type";
  private static final String X_LABEL = "File Type/Extension";
  private static final String Y_LABEL = "Number of files";
  private DoubleProperty doubleProperty = new SimpleDoubleProperty(400);
  private ObservableList<XYChart.Data<String, Number>> chartData = FXCollections.observableArrayList();
  private ObservableList<String> keys = FXCollections.observableArrayList();

  public NumberOfFiles() {
    super(STATISTIC_NAME);
  }

  @Override
  public void calculateValue(List<FileNode> fileSystemNodes) {
    Map<String, Long> stringLongMap = fileSystemNodes.stream()
      .collect(groupingBy(FileNode::getExtension, counting()));
    writeIntoValue(stringLongMap);
  }

  private void writeIntoValue(Map<String, Long> statisticByType) {
    StringBuffer buffer = new StringBuffer();
    statisticByType.entrySet()
      .forEach(stringLongEntry -> buffer.append(stringLongEntry.getKey() + " : " + stringLongEntry.getValue() + "\n"));

    setContent(buffer.toString());
    fillChartData(statisticByType);
  }

  private void fillChartData(Map<String, Long> statisticByType) {
    removeAllElementsFromDataList();
    removeAllElementsFromKeysList();
    fillListsWithData(statisticByType);
  }

  private void removeAllElementsFromKeysList() {
    keys.removeIf(key -> true);
  }

  private void removeAllElementsFromDataList() {
    chartData.removeIf(data -> true);
  }

  private void fillListsWithData(Map<String, Long> statisticByType) {
    statisticByType.entrySet()
      .stream()
      .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
      .forEach(stringLongEntry -> {
        keys.add(stringLongEntry.getKey());
        chartData.add(new XYChart.Data<>(stringLongEntry.getKey(), stringLongEntry.getValue()));
        doubleProperty.setValue(chartData.size() * 100);
      });
  }

  @Override
  public Node getValueAsNode() {
    ScrollPane scrollPane = createHorizontalScrollPane();

    CategoryAxis xAxis = createXAxis();
    NumberAxis yAxis = createYAxis();

    XYChart.Series<String, Number> series = new XYChart.Series<>();
    series.dataProperty().setValue(chartData);

    BarChart<String, Number> barChart = createBarChart(xAxis, yAxis, series);
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
}
