package com.frx.discstalker.statistics.concreteStatistics.directoryStatistics;

import com.frx.discstalker.model.DirectoryNode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by surjak on 22.12.2020
 */
class PercentageUsageOfAllowedSpaceTest {

  @Test
  void name() {
    //given
    PercentageUsageOfAllowedSpace percentageUsageOfAllowedSpace = new PercentageUsageOfAllowedSpace(100L);
    DirectoryNode rootNode = mock(DirectoryNode.class);

    //when
    when(rootNode.getSize()).thenReturn(40000000L); //bytes
    percentageUsageOfAllowedSpace.calculateValue(java.util.List.of(rootNode));

    //then
    assertEquals(percentageUsageOfAllowedSpace.getTextValue().getValue().toString(), "40");
  }
}
