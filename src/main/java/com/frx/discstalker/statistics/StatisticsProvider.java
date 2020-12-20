package com.frx.discstalker.statistics;

import com.frx.discstalker.fs.LiveDirectoryTree;
import com.frx.discstalker.statistics.concreteStatistics.filesStatistics.FileStatisticsCalculator;
import com.frx.discstalker.statistics.concreteStatistics.StatisticCalculator;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;

/**
 * Created by surjak on 19.12.2020
 */
public class StatisticsProvider {
  private LiveDirectoryTree directoryTree;
  private ObservableList<Statistic> statisticList = FXCollections.observableArrayList();
  private List<StatisticCalculator> calculators = new ArrayList<>();

  public StatisticsProvider(LiveDirectoryTree directoryTree) {
    this.directoryTree = directoryTree;

    registerCalculator(new FileStatisticsCalculator());

    fillStatisticsList();
    calculateStatistics();

    directoryTree.getEventSubject()
      .subscribeOn(Schedulers.io())
      .observeOn(JavaFxScheduler.platform())
      .subscribe(directoryWatcherEvent -> calculateStatistics());
  }

  public ObservableList<Statistic> getStatisticList() {
    return statisticList;
  }

  private void registerCalculator(StatisticCalculator statisticCalculator){
    this.calculators.add(statisticCalculator);
  }

  private void fillStatisticsList() {
    calculators.stream().flatMap(statisticCalculator -> statisticCalculator.getStatistics().stream()).forEach(statisticList::add);
  }

  private void calculateStatistics() {
    calculators.forEach(statisticCalculator -> statisticCalculator.calculate(directoryTree.getRoot()));
  }
}
