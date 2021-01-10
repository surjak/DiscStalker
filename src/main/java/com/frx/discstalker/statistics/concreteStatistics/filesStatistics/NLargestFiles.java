package com.frx.discstalker.statistics.concreteStatistics.filesStatistics;

import com.frx.discstalker.model.FileNode;
import javafx.scene.Node;
import javafx.scene.control.Label;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by surjak on 20.12.2020
 */
public class NLargestFiles extends BaseFilesStatistic {

  private static final String STATISTIC_NAME = "Largest files";
  private final Integer N;

  public NLargestFiles(Integer N) {
    super(STATISTIC_NAME);
    this.N = N;
  }

  public NLargestFiles() {
    this(3);
  }

  @Override
  public void calculateValue(List<FileNode> fileSystemNodes) {
    Map<String, Long> statisticByType = fileSystemNodes.stream()
      .sorted(Comparator.comparingLong(FileNode::getSize).reversed())
      .limit(N)
      .collect(Collectors.toMap(fileNode -> fileNode.getPath().toString(), FileNode::getSize));

    writeIntoValue(statisticByType);
  }

  private void writeIntoValue(Map<String, Long> statisticByType) {
    StringBuffer buffer = new StringBuffer();
    statisticByType.entrySet()
      .stream()
      .sorted((o1, o2) -> -1 * (int) (o1.getValue() - o2.getValue())) //workaround Comparator.comparingLong(Map.Entry::getValue).reversed()
      .forEach(stringLongEntry -> buffer.append(stringLongEntry.getKey() + " : " + stringLongEntry.getValue() + "\n"));
    setContent(buffer.toString());
  }
  @Override
  public Node getValueAsNode() {
    return new Label("aaa");
  }
}
