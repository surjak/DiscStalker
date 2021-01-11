package com.frx.discstalker.statistics.concreteStatistics.filesStatistics;

import com.frx.discstalker.model.FileNode;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by surjak on 22.12.2020
 */
class NumberOfFilesTest {
  @Test
  void given4FileNodesWhenCalculateValueThenProperValueIsSet() {
    //given/when
    NumberOfFiles numberOfFiles = new NumberOfFiles();
    List<FileNode> nodes = createMockedFileNodes();
    numberOfFiles.calculateValue(nodes);

    //then
    assertThat(numberOfFiles.getValue().getValue().toString()).isIn("text/pdf : 2\ntext/plain : 2\n", "text/plain : 2\ntext/pdf : 2\n");
  }


  private List<FileNode> createMockedFileNodes() {
    FileNode fileNode1 = mock(FileNode.class);
    FileNode fileNode2 = mock(FileNode.class);
    FileNode fileNode3 = mock(FileNode.class);
    FileNode fileNode4 = mock(FileNode.class);

    //when
    when(fileNode1.getExtension()).thenReturn("text/plain");
    when(fileNode2.getExtension()).thenReturn("text/plain");
    when(fileNode3.getExtension()).thenReturn("text/pdf");
    when(fileNode4.getExtension()).thenReturn("text/pdf");

    return List.of(fileNode1, fileNode2, fileNode3, fileNode4);
  }
}
