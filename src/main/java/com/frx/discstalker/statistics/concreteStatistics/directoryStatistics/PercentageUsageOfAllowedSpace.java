package com.frx.discstalker.statistics.concreteStatistics.directoryStatistics;

import com.frx.discstalker.model.DirectoryNode;

import java.util.List;
/**
 * Created by surjak on 20.12.2020
 */
public class PercentageUsageOfAllowedSpace extends BaseDirectoryStatistics {

  private static final String STATISTIC_NAME = "Percentage usage of allowed space";

  private Long maxSizeInGB;

  public PercentageUsageOfAllowedSpace(Long maxSizeInGB) {
    super(STATISTIC_NAME);
    this.maxSizeInGB = maxSizeInGB;
  }

  @Override
  public void calculateValue(List<DirectoryNode> listWithRootElementAsFirstIndex) {
    DirectoryNode directoryNode = listWithRootElementAsFirstIndex.get(0);
    Long rootSize = directoryNode.getSize();
    double rootSizeInGB = convertToGB(rootSize);
    double percentageSize = rootSizeInGB / maxSizeInGB;
    int val = (int) (percentageSize * 100);
    setContent(val + "%");
  }

  private double convertToGB(Long rootSize) {
    // TODO: 20.12.2020 impl
    //hardcoded
    return 10.3423;
  }
}
