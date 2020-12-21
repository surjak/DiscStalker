package com.frx.discstalker.statistics.concreteStatistics.filesStatistics;

import com.frx.discstalker.model.FileNode;

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
      .collect(Collectors.toMap(fileNode -> fileNode.getNodeNameProperty().getValue(), FileNode::getSize));

    writeIntoValue(statisticByType);
  }

  private void writeIntoValue(Map<String, Long> statisticByType) {
    StringBuffer buffer = new StringBuffer();
    statisticByType.entrySet()
      .forEach(stringLongEntry -> buffer.append(stringLongEntry.getKey() + " : " + stringLongEntry.getValue() + "\n"));
    System.out.println("alamakota");
    setContent(buffer.toString());
  }
}
