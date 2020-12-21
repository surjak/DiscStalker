package com.frx.discstalker.statistics.concreteStatistics.filesStatistics;

import com.frx.discstalker.model.DirectoryNode;
import com.frx.discstalker.model.FileNode;
import com.frx.discstalker.model.IFileSystemNode;
import com.frx.discstalker.statistics.Statistic;
import com.frx.discstalker.statistics.concreteStatistics.StatisticCalculator;
import com.google.common.collect.ImmutableList;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;

import java.util.*;

/**
 * Created by surjak on 19.12.2020
 */
public class FileStatisticsCalculator implements StatisticCalculator {

  private final ImmutableList<FilesStatistic> filesStatistics = ImmutableList.of(new FileSize(), new NumberOfFiles(), new NLargestFiles());
  private boolean firstCalculation = true;

  @Override
  public void calculate(IFileSystemNode root) {
    List<FileNode> fileSystemNodes = new ArrayList<>();
    fillListWithAllFilesInTree(fileSystemNodes, root);
    if (firstCalculation) {
      setFileExtensionAsPartOfFirstCalculation(fileSystemNodes);
      return;
    }
    calculateStatistics(fileSystemNodes);

  }

  private void calculateStatistics(List<FileNode> fileSystemNodes) {
    filesStatistics.forEach(statisticByType -> statisticByType.calculateValue(fileSystemNodes));
  }

  private void setFileExtensionAsPartOfFirstCalculation(List<FileNode> fileSystemNodes) {
    Observable.fromIterable(fileSystemNodes)
      .doOnNext(FileNode::setFileExtension)
      .subscribeOn(Schedulers.io())
      .observeOn(JavaFxScheduler.platform())
      .doOnComplete(() -> filesStatistics.forEach(statisticByType -> statisticByType.calculateValue(fileSystemNodes)))
      .subscribe();
    firstCalculation = false;
  }

  private void fillListWithAllFilesInTree(List<FileNode> fileSystemNodes, IFileSystemNode root) {
    if (root.isDirectory()) {
      DirectoryNode rootNode = (DirectoryNode) root;
      rootNode.getChildNodes().forEach(iFileSystemNode -> addChildrenToList(fileSystemNodes, iFileSystemNode)
      );
    }
  }

  private void addChildrenToList(List<FileNode> fileSystemNodes, IFileSystemNode iFileSystemNode) {
    if (iFileSystemNode.isDirectory()) {
      fillListWithAllFilesInTree(fileSystemNodes, iFileSystemNode);
    } else {
      fileSystemNodes.add((FileNode) iFileSystemNode);
    }
  }

  @Override
  public List<? extends Statistic> getStatistics() {
    return filesStatistics;
  }
}
