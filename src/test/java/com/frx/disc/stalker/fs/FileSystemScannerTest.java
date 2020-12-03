package com.frx.disc.stalker.fs;

import com.frx.disc.stalker.model.FileSystemNode;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Created by surja on 02.12.2020
 */
class FileSystemScannerTest {

  FileSystemScanner scanner;
  Consumer consumer;

  private static MockedStatic<Files> filesMockedStatic;

  @BeforeAll
  static void beforeAll() {
    filesMockedStatic = mockStatic(Files.class);
  }

  @AfterAll
  static void afterAll() {
    filesMockedStatic.close();
  }

  @BeforeEach
  void setUp() {
    consumer = mock(Consumer.class);
    scanner = new FileSystemScanner(consumer);
  }

  @Test
  void givenDirectoryPathWhenScanDirectoryThenConsumerAcceptIsCalled() throws IOException {
    //given
    Path path = Paths.get("TEST_PATH");

    //when
    when(Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)).thenReturn(true);
    when(Files.list(path)).thenReturn(Stream.empty());
    scanner.scanRecursively(path);

    //then
    verify(consumer, times(1)).accept(any());
  }

  @Test
  void givenFilePathWhenScanDirectoryThenConsumerAcceptIsCalled() throws IOException {
    //given
    Path path = Paths.get("TEST_PATH");

    //when
    when(Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)).thenReturn(false);
    when(Files.isRegularFile(path, LinkOption.NOFOLLOW_LINKS)).thenReturn(true);
    scanner.scanRecursively(path);

    //then
    verify(consumer, times(1)).accept(any());
  }

  @Test
  void givenDirectoryPathWhenScanDirectoryThenFileNodeIsReturnedAndHasProperSize() throws IOException {
    //given
    Path path = Paths.get("TEST_PATH");

    //when
    when(Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)).thenReturn(true);
    Optional<FileSystemNode> fileSystemNode = scanner.scanRecursively(path);

    //then
    assertTrue(fileSystemNode.get().isDirectory());
  }

  @Test
  void givenFilePathWhenScanDirectoryThenFileNodeIsReturnedAndHasProperSize() throws IOException {
    //given
    Path path = Paths.get("TEST_PATH");

    //when
    when(Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)).thenReturn(false);
    when(Files.isRegularFile(path, LinkOption.NOFOLLOW_LINKS)).thenReturn(true);
    when(Files.size(path)).thenReturn(5L);
    Optional<FileSystemNode> fileSystemNode = scanner.scanRecursively(path);

    //then
    assertFalse(fileSystemNode.get().isDirectory());
  }

  @Test
  void givenUnexpectedInputPathWhenScanDirectoryThenEmptyOptionalIsReturned() throws IOException {
    //given
    Path path = Paths.get("TEST_PATH");

    //when
    when(Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)).thenReturn(false);
    when(Files.isRegularFile(path, LinkOption.NOFOLLOW_LINKS)).thenReturn(false);
    Optional<FileSystemNode> fileSystemNode = scanner.scanRecursively(path);

    //then
    assertTrue(fileSystemNode.isEmpty());
  }

}
