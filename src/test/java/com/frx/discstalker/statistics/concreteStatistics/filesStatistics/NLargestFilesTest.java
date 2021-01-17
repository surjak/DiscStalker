package com.frx.discstalker.statistics.concreteStatistics.filesStatistics;

import com.frx.discstalker.model.FileNode;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by surjak on 22.12.2020
 */
class NLargestFilesTest {

  @Test
  void given4FileNodesWhenCalculateValueThenProperValueIsSet() {
    //given/when
    NLargestFiles nLargestFilesTest = new NLargestFiles();
    List<FileNode> nodes = createMockedFileNodes();
    nLargestFilesTest.calculateValue(nodes);

    //then
    assertThat(nLargestFilesTest.getTextValue().getValue().toString()).isEqualTo("fileNode4 : 42\nfileNode3 : 32\nfileNode2 : 22\n");
  }

  @Test
  void given4FileNodesWhenNIs2AndCalculateValueThenProperValueIsSet() {
    //given/when
    NLargestFiles nLargestFilesTest = new NLargestFiles(2);
    List<FileNode> nodes = createMockedFileNodes();
    nLargestFilesTest.calculateValue(nodes);

    //then
    assertThat(nLargestFilesTest.getTextValue().getValue().toString()).isEqualTo("fileNode4 : 42\nfileNode3 : 32\n");
  }

  private List<FileNode> createMockedFileNodes() {
    FileNode fileNode1 = mock(FileNode.class);
    FileNode fileNode2 = mock(FileNode.class);
    FileNode fileNode3 = mock(FileNode.class);
    FileNode fileNode4 = mock(FileNode.class);

    //when
    when(fileNode1.getPath()).thenReturn(Path.of("fileNode1"));
    when(fileNode2.getPath()).thenReturn(Path.of("fileNode2"));
    when(fileNode3.getPath()).thenReturn(Path.of("fileNode3"));
    when(fileNode4.getPath()).thenReturn(Path.of("fileNode4"));

    when(fileNode1.getSize()).thenReturn(12L);
    when(fileNode2.getSize()).thenReturn(22L);
    when(fileNode3.getSize()).thenReturn(32L);
    when(fileNode4.getSize()).thenReturn(42L);

    return List.of(fileNode1, fileNode2, fileNode3, fileNode4);
  }

}
