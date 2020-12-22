package com.frx.discstalker.statistics.concreteStatistics.directoryStatistics;

import com.frx.discstalker.model.DirectoryNode;
import com.frx.discstalker.model.IFileSystemNode;
import com.frx.discstalker.statistics.Statistic;
import com.frx.discstalker.statistics.concreteStatistics.StatisticCalculator;
import com.google.common.collect.ImmutableList;

import java.util.List;

/**
 * Created by surjak on 20.12.2020
 */
public class DirectoryStatisticsCalculator implements StatisticCalculator {

  private final ImmutableList<DirectoryStatistics> directoryStatistics = ImmutableList.of(new PercentageUsageOfAllowedSpace(200L));

  @Override
  public void calculate(IFileSystemNode root) {
    DirectoryNode directoryRootNode = (DirectoryNode) root;
    directoryStatistics.forEach(statisticByType -> statisticByType.calculateValue(ImmutableList.of(directoryRootNode)));
  }

  @Override
  public List<? extends Statistic> getStatistics() {
    return directoryStatistics;
  }
}
