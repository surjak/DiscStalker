package com.frx.discstalker.stats;

import com.frx.discstalker.model.IFileSystemNode;

import java.util.*;

/**
 * Created by surjak on 19.12.2020
 */
public interface StatisticCalculator {
  void calculate(IFileSystemNode root);
  List<? extends Statistic> getStatistics();
}
