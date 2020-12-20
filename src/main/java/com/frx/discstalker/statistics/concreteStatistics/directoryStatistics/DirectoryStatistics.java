package com.frx.discstalker.statistics.concreteStatistics.directoryStatistics;

import com.frx.discstalker.model.DirectoryNode;
import com.frx.discstalker.statistics.Statistic;

import java.util.List;

/**
 * Created by surjak on 20.12.2020
 */
public interface DirectoryStatistics extends Statistic {
  void calculateValue(List<DirectoryNode> directoryNodes);
}
