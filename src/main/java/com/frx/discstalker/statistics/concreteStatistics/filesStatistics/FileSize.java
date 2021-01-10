package com.frx.discstalker.statistics.concreteStatistics.filesStatistics;

import com.frx.discstalker.model.FileNode;
import javafx.scene.Node;
import javafx.scene.control.Label;

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
  }
  @Override
  public Node getValueAsNode() {
    return new Label("aaa");
  }
}
