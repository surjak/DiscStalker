package com.frx.discstalker.fs.watcher;

import io.reactivex.rxjava3.core.Observer;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class DirectoryWatcherTest {
  DirectoryWatcher watcher;
  private static final String TEST_PATH = "TEST_PATH";

  private static final String LOCAL_TEST_PATH = "src/test/resources/dirA";

  private static MockedStatic<Files> filesMockedStatic;

  static void mockFiles() {
    filesMockedStatic = mockStatic(Files.class);
  }

  static void unmockFiles() {
    filesMockedStatic.close();
  }

  @BeforeEach
  void setUp() throws IOException {
    watcher = new DirectoryWatcher();
    Files.createDirectories(Paths.get(LOCAL_TEST_PATH));
  }

  @AfterEach
  void cleanUp() throws IOException {
    Files.walk(Paths.get(LOCAL_TEST_PATH))
        .map(Path::toFile)
        .sorted((o1, o2) -> -o1.compareTo(o2))
        .forEach(File::delete);
  }

  @Test
  void givenFilePathWhenWatchDirectoryThenExceptionIsThrown() {
    mockFiles();

    // given
    Path path = Paths.get(TEST_PATH);

    // when
    when(Files.isRegularFile(path)).thenReturn(true);

    // then
    assertThrows(NotDirectoryException.class, () -> watcher.watchDirectory(path));

    unmockFiles();
  }

  @Test
  void givenLocalDirectoryWhenAddDirectoryAsChildThenCreatedEventIsEmitted() throws IOException {
    // given
    Path path = Paths.get(LOCAL_TEST_PATH);
    watcher.watchDirectory(path);

    Observer<DirectoryWatcherEvent> observer = mock(Observer.class);
    watcher.getEventSubject().subscribe(observer);

    // when
    Path newDir = Files.createDirectory(Paths.get(LOCAL_TEST_PATH + "/newDir"));
    DirectoryWatcherEvent expectedEvent = new DirectoryWatcherEvent(
      DirectoryWatcherEventType.CREATED,
      path,
      newDir
    );

    // then
    verify(observer, timeout(100).times(1))
      .onNext(expectedEvent);
  }

  @Test
  void givenLocalDirectoryWhenAddFileAsChildThenCreatedEventIsEmitted() throws IOException {
    // given
    Path path = Paths.get(LOCAL_TEST_PATH);
    watcher.watchDirectory(path);

    Observer<DirectoryWatcherEvent> observer = mock(Observer.class);
    watcher.getEventSubject().subscribe(observer);

    // when
    Path newFile = Files.createFile(Paths.get(LOCAL_TEST_PATH + "/newFile.md"));
    DirectoryWatcherEvent expectedEvent = new DirectoryWatcherEvent(
      DirectoryWatcherEventType.CREATED,
      path,
      newFile
    );

    // then
    verify(observer, timeout(100).times(1))
      .onNext(expectedEvent);
  }

  @Test
  void givenLocalFileWhenModifyFileThenModifiedEventIsEmitted() throws IOException {
    // given
    Path file = Files.createFile(Paths.get(LOCAL_TEST_PATH + "/testFile.md"));

    Observer<DirectoryWatcherEvent> observer = mock(Observer.class);
    watcher.getEventSubject().subscribe(observer);

    Path path = Paths.get(LOCAL_TEST_PATH);
    watcher.watchDirectory(path);

    // when
    Files.writeString(file, "test_line");
    DirectoryWatcherEvent expectedEvent = new DirectoryWatcherEvent(
      DirectoryWatcherEventType.MODIFIED,
      path,
      file
    );

    // then
    // It turns out that modifying triggers Java's Watch API ENTRY_MODIFY event twice
    verify(observer, timeout(100).times(2))
      .onNext(expectedEvent);
  }

  @Test
  void givenLocalFileWhenDeleteFileThenDeletedEventIsEmitted() throws IOException {
    // given
    Path file = Files.createFile(Paths.get(LOCAL_TEST_PATH + "/testFile.md"));

    Observer<DirectoryWatcherEvent> observer = mock(Observer.class);
    watcher.getEventSubject().subscribe(observer);

    Path path = Paths.get(LOCAL_TEST_PATH);
    watcher.watchDirectory(path);

    // when
    Files.delete(file);
    DirectoryWatcherEvent expectedEvent = new DirectoryWatcherEvent(
      DirectoryWatcherEventType.DELETED,
      path,
      file
    );

    // then
    verify(observer, timeout(100).times(1))
      .onNext(expectedEvent);
  }
}
