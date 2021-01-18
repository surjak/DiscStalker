package com.frx.discstalker.statistics;

import com.frx.discstalker.fs.LiveDirectoryTree;
import com.frx.discstalker.statistics.concreteStatistics.StatisticCalculator;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Created by surjak on 19.12.2020
 */
public class StatisticsProvider {
  private final LiveDirectoryTree directoryTree;
  private final ObservableList<Statistic> statisticList = FXCollections.observableArrayList();
  private final List<StatisticCalculator> calculators = new ArrayList<>();

  public StatisticsProvider(LiveDirectoryTree directoryTree, List<StatisticCalculator> statisticCalculators) {
    this.directoryTree = directoryTree;
    this.registerCalculators(statisticCalculators);

    bufferEventsFor5SecondsAndRecalculateStatsIfEventOccurred(directoryTree);
  }

  public ObservableList<Statistic> getStatisticList() {
    return statisticList;
  }

  public <T extends Statistic> Optional<T> findConcreteStatisticBy(Class<T> concreteStatisticClass) {
    return statisticList.stream()
      .filter(concreteStatisticClass::isInstance)
      .map(concreteStatisticClass::cast)
      .findFirst();
  }

  private void bufferEventsFor5SecondsAndRecalculateStatsIfEventOccurred(LiveDirectoryTree directoryTree) {
    directoryTree.getEventSubject()
      .toFlowable(BackpressureStrategy.LATEST)
      .buffer(5, TimeUnit.SECONDS)
      .filter(directoryWatcherEvents -> !directoryWatcherEvents.isEmpty())
      .subscribeOn(Schedulers.io())
      .observeOn(JavaFxScheduler.platform())
      .subscribe(directoryWatcherEvent -> calculateStatistics());
  }

  public void registerCalculator(StatisticCalculator statisticCalculator) {
    calculators.add(statisticCalculator);
    fillStatisticsListWith(statisticCalculator);
    calculateStatisticsFor(statisticCalculator);
  }

  public void registerCalculators(List<StatisticCalculator> statisticCalculators) {
    statisticCalculators.forEach(this::registerCalculator);
  }

  private void fillStatisticsListWith(StatisticCalculator statisticCalculator) {
    statisticList.addAll(statisticCalculator.getStatistics());
  }

  private void calculateStatisticsFor(StatisticCalculator statisticCalculator) {
    statisticCalculator.calculate(directoryTree.getRoot());
  }

  public void calculateStatistics() {
    calculators.forEach(this::calculateStatisticsFor);
  }

  public LiveDirectoryTree getLiveDirectoryTree() {
    return directoryTree;
  }
}
