package com.frx.discstalker.statistics.concreteStatistics.filesStatistics;

import com.frx.discstalker.model.FileNode;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
/**
 * Created by surjak on 22.12.2020
 */
class FileSizeTest {

  @Test
  void givenFileNodesWhenCalculateThenProperValueIsSet() {
    //given
    FileSize fileSizeStatistic = new FileSize();
    FileNode fileNode1 = mock(FileNode.class);
    FileNode fileNode2 = mock(FileNode.class);

    //when
    when(fileNode1.getMimeType()).thenReturn("text/plain");
    when(fileNode1.getSize()).thenReturn(10L);
    when(fileNode2.getMimeType()).thenReturn("text/pdf");
    when(fileNode2.getSize()).thenReturn(20L);
    fileSizeStatistic.calculateValue(List.of(fileNode1, fileNode2));

    //then
    assertThat(fileSizeStatistic.getValue().getValue().toString()).isIn("text/plain : 10\ntext/pdf : 20\n", "text/pdf : 20\ntext/plain : 10\n");
  }

  @Test
  void givenFileNodesWithTheSameMimeTypeWhenCalculateThenProperValueIsSet() {
    //given
    FileSize fileSizeStatistic = new FileSize();
    FileNode fileNode1 = mock(FileNode.class);
    FileNode fileNode2 = mock(FileNode.class);

    //when
    when(fileNode1.getMimeType()).thenReturn("text/plain");
    when(fileNode1.getSize()).thenReturn(10L);
    when(fileNode2.getMimeType()).thenReturn("text/plain");
    when(fileNode2.getSize()).thenReturn(20L);
    fileSizeStatistic.calculateValue(List.of(fileNode1, fileNode2));

    //then
    assertThat(fileSizeStatistic.getValue().getValue().toString()).isEqualTo("text/plain : 30\n");
  }
}
