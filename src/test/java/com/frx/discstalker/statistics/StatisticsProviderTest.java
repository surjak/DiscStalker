package com.frx.discstalker.statistics;

import com.frx.discstalker.fs.LiveDirectoryTree;
import com.frx.discstalker.model.DirectoryNode;
import com.frx.discstalker.statistics.concreteStatistics.directoryStatistics.PercentageUsageOfAllowedSpace;
import com.frx.discstalker.statistics.concreteStatistics.filesStatistics.FileSize;
import com.frx.discstalker.statistics.concreteStatistics.filesStatistics.NLargestFiles;
import com.frx.discstalker.statistics.concreteStatistics.filesStatistics.NumberOfFiles;
import io.reactivex.rxjava3.subjects.PublishSubject;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Created by surjak on 22.12.2020
 */
class StatisticsProviderTest {

  @Test
  void givenStatisticProviderWhenGetStatisticListThenAllStatisticsAreInList() {
    //given
    LiveDirectoryTree directoryTree = mock(LiveDirectoryTree.class);
    DirectoryNode rootNode = mock(DirectoryNode.class);

    //when
    when(directoryTree.getRoot()).thenReturn(rootNode);
    when(rootNode.isDirectory()).thenReturn(false);
    when(directoryTree.getEventSubject()).thenReturn(PublishSubject.create());
    StatisticsProvider statisticsProvider = new StatisticsProvider(directoryTree);

    //then
    assertThat(statisticsProvider.getStatisticList()).hasSize(4);
    assertThat(statisticsProvider.getStatisticList()).anyMatch(statistic -> statistic.getClass().equals(NLargestFiles.class));
    assertThat(statisticsProvider.getStatisticList()).anyMatch(statistic -> statistic.getClass().equals(FileSize.class));
    assertThat(statisticsProvider.getStatisticList()).anyMatch(statistic -> statistic.getClass().equals(NumberOfFiles.class));
    assertThat(statisticsProvider.getStatisticList()).anyMatch(statistic -> statistic.getClass().equals(PercentageUsageOfAllowedSpace.class));
  }

}
