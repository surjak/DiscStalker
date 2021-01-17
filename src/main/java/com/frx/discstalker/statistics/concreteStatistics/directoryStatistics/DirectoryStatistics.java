package com.frx.discstalker.statistics.concreteStatistics.directoryStatistics;

import com.frx.discstalker.model.DirectoryNode;
import com.frx.discstalker.statistics.Statistic;

import java.util.List;

/**
 * Created by surjak on 20.12.2020
 */
public interface DirectoryStatistics<T> extends Statistic<T> {
  void calculateValue(List<DirectoryNode> directoryNodes);
}
