package com.frx.discstalker.statistics.concreteStatistics.filesStatistics;

import com.frx.discstalker.model.FileNode;
import com.frx.discstalker.statistics.Statistic;

import java.util.List;

/**
 * Created by surjak on 19.12.2020
 */
public interface FilesStatistic extends Statistic {
  void calculateValue(List<FileNode> fileSystemNodes);
}
