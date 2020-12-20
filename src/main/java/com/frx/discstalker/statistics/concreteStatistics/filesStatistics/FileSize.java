package com.frx.discstalker.stats.filesByTypeStatistics;

import com.frx.discstalker.model.FileNode;
import javafx.beans.property.*;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingLong;

/**
 * Created by surjak on 19.12.2020
 */
public class FileSizeByType extends BaseByTypeStatistic {

  private static final String STATISTIC_NAME = "File size grouped by file type";

  public FileSizeByType() {
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
}
