package com.frx.discstalker.statistics.concreteStatistics;

import com.frx.discstalker.model.IFileSystemNode;
import com.frx.discstalker.statistics.Statistic;

import java.util.*;

/**
 * Created by surjak on 19.12.2020
 */
public interface StatisticCalculator {
  void calculate(IFileSystemNode root);
  List<? extends Statistic> getStatistics();
}
