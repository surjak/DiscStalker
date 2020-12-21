package com.frx.discstalker.statistics.concreteStatistics.filesStatistics;

import com.frx.discstalker.model.FileNode;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;

/**
 * Created by surjak on 19.12.2020
 */
public class NumberOfFiles extends BaseFilesStatistic {

  private static final String STATISTIC_NAME = "Number of files grouped by file type";

  public NumberOfFiles() {
    super(STATISTIC_NAME);
  }

  @Override
  public void calculateValue(List<FileNode> fileSystemNodes) {
    Map<String, Long> stringLongMap = fileSystemNodes.stream().collect(groupingBy(FileNode::getMimeType, counting()));
    writeIntoValue(stringLongMap);
  }

  private void writeIntoValue(Map<String, Long> statisticByType) {
    StringBuffer buffer = new StringBuffer();
    statisticByType.entrySet()
      .forEach(stringLongEntry -> buffer.append(stringLongEntry.getKey() + " : " + stringLongEntry.getValue() + "\n"));

    setContent(buffer.toString());
  }
}
