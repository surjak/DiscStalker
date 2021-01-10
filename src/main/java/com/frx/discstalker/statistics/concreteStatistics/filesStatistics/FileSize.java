package com.frx.discstalker.statistics.concreteStatistics.filesStatistics;

import com.frx.discstalker.model.FileNode;
import com.google.inject.internal.cglib.core.$AbstractClassGenerator;
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
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingLong;

/**
 * Created by surjak on 19.12.2020
 */
public class FileSize extends BaseFilesStatistic {

  private static final String STATISTIC_NAME = "File size grouped by file type";

  public FileSize() {
    super(STATISTIC_NAME);
  }

  private ObservableList<XYChart.Data<String, Number>> chartData = FXCollections.observableArrayList();
  private ObservableList<String> keys = FXCollections.observableArrayList();

  private DoubleProperty doubleProperty = new SimpleDoubleProperty(400);

  @Override
  public void calculateValue(List<FileNode> fileSystemNodes) {
    Map<String, Long> statisticByType = fileSystemNodes.stream()
      .collect(groupingBy(FileNode::getMimeType, summingLong(FileNode::getSize)));

    writeIntoValue(statisticByType);
  }

  private void writeIntoValue(Map<String, Long> statisticByType) {
    StringBuffer buffer = new StringBuffer();
    statisticByType.entrySet()
      .forEach(stringLongEntry -> buffer.append(stringLongEntry.getKey() + " : " + stringLongEntry.getValue() + "\n"));
    setContent(buffer.toString());

    chartData.removeIf(a -> true);
    keys.removeIf(a -> true);

    statisticByType.entrySet()
      .forEach(stringLongEntry -> {
        keys.add(stringLongEntry.getKey());
        chartData.add(new XYChart.Data<>(stringLongEntry.getKey(), stringLongEntry.getValue()));
        doubleProperty.setValue(chartData.size() * 200);
      });

  }

  @Override
  public Node getValueAsNode() {
    ScrollPane scrollPane = new ScrollPane();
    scrollPane.setFitToHeight(true);
    scrollPane.setFitToWidth(true);
    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

    CategoryAxis xAxis = new CategoryAxis();
    xAxis.setCategories(keys);
    xAxis.setLabel("Mime Type");
    xAxis.setSide(Side.TOP);

    scrollPane.fitToWidthProperty();
    NumberAxis yAxis = new NumberAxis();
    yAxis.setLabel("Grouped file size");


    XYChart.Series<String, Number> series = new XYChart.Series<>();
    series.dataProperty().setValue(chartData);
    BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
    barChart.getData().add(series);

    barChart.minWidthProperty().bind(doubleProperty);

    barChart.prefWidthProperty().bind(xAxis.widthProperty());
    scrollPane.setContent(barChart);
    return scrollPane;
  }
}
