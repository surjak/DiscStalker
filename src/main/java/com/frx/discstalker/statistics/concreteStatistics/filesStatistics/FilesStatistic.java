package com.frx.discstalker.stats.filesByTypeStatistics;

import com.frx.discstalker.model.FileNode;
import com.frx.discstalker.stats.Statistic;

import java.util.List;
import java.util.Map;

/**
 * Created by surjak on 19.12.2020
 */
public interface StatisticByType extends Statistic {
  void calculateValue(List<FileNode> fileSystemNodes);
}
