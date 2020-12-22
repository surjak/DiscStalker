package com.frx.discstalker.statistics.concreteStatistics.directoryStatistics;

import com.frx.discstalker.model.DirectoryNode;

import java.util.List;
/**
 * Created by surjak on 20.12.2020
 */
public class PercentageUsageOfAllowedSpace extends BaseDirectoryStatistics {

  private static final String STATISTIC_NAME = "Percentage usage of allowed space";

  private Long maxSizeInMB;

  public PercentageUsageOfAllowedSpace(Long maxSizeInMB) {
    super(STATISTIC_NAME);
    this.maxSizeInMB = maxSizeInMB;
  }

  @Override
  public void calculateValue(List<DirectoryNode> listWithRootElementAsFirstIndex) {
    DirectoryNode directoryNode = listWithRootElementAsFirstIndex.get(0);
    Long rootSize = directoryNode.getSize();
    double rootSizeInGB = convertToMB(rootSize);
    double percentageSize = rootSizeInGB / maxSizeInMB;
    int val = (int) (percentageSize * 100);
    setContent(val + "%");
  }

  private double convertToMB(Long rootSize) {
    return (double) rootSize / (1e6);
  }
}
