package com.frx.discstalker.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

/**
 * Created by surja on 02.12.2020
 */
class FileSystemNodeTest {

  DirectoryNode directoryNode;

  @BeforeEach
  public void initDirectory() {
    Path path = Paths.get("PATH");
    directoryNode = new DirectoryNode(path);
  }

  @Test
  public void givenDirectoryNodeWhenAddDirectoryAsChildThenChildNumberIncreases() {
    //given
    Path path = Paths.get("PATH");
    DirectoryNode childNode = new DirectoryNode(path);

    //when
    directoryNode.addChild(childNode);

    //then
    assertThat(directoryNode.getChildNodes().size()).isEqualTo(1);
  }

  @Test
  public void givenDirectoryNodeWhenAddDirectoryAsChildThenDirectorySizeIncreases() {
    //given
    Long EXPECTED_SIZE = 50L;
    Path path = Paths.get("PATH");
    DirectoryNode childNode = Mockito.spy(new DirectoryNode(path));

    //when
    when(childNode.getSize()).thenReturn(EXPECTED_SIZE);
    directoryNode.addChild(childNode);

    //then
    assertThat(directoryNode.getSize()).isEqualTo(EXPECTED_SIZE);
  }

  @Test
  public void givenDirectoryNodeWhenAddDirectoryAsChildThenNumberOfFilesDoesNotIncrease() {
    //given
    Path path = Paths.get("PATH");
    DirectoryNode childNode = new DirectoryNode(path);

    //when
    directoryNode.addChild(childNode);

    //then
    assertThat(directoryNode.getNumberOfFiles()).isEqualTo(0L);
  }

  @Test
  public void givenFileNodeWhenAddFileAsChildThenChildNumberIncreases() {
    //given
    Path path = Paths.get("PATH");
    FileNode childNode = new FileNode(path, 10L);

    //when
    directoryNode.addChild(childNode);

    //then
    assertThat(directoryNode.getChildNodes().size()).isEqualTo(1);
  }

  @Test
  public void givenFileNodeWhenAddFileAsChildThenDirectorySizeIncreases() {
    //given
    Long EXPECTED_SIZE = 50L;
    Path path = Paths.get("PATH");
    FileNode childNode = Mockito.spy(new FileNode(path, EXPECTED_SIZE));

    //when
    directoryNode.addChild(childNode);

    //then
    assertThat(directoryNode.getSize()).isEqualTo(EXPECTED_SIZE);
  }

  @Test
  public void givenFileNodeWhenAddFileAsChildThenNumberOfFilesIncreases() {
    //given
    Path path = Paths.get("PATH");
    FileNode childNode = new FileNode(path, 10L);

    //when
    directoryNode.addChild(childNode);

    //then
    assertThat(directoryNode.getNumberOfFiles()).isEqualTo(1L);
  }
}
