package com.frx.discstalker.fs.watcher;

import java.nio.file.Path;

/**
 * Carries information about a file change within an observed directory.
 */
public class DirectoryWatcherEvent {
  private final DirectoryWatcherEventType type;
  private final Path watchedPath;
  private final Path path;

  public DirectoryWatcherEvent(DirectoryWatcherEventType type, Path watchedPath, Path path) {
    this.watchedPath = watchedPath;
    this.path = path;
    this.type = type;
  }

  public DirectoryWatcherEventType getType() {
    return type;
  }

  public Path getWatchedPath() {
    return watchedPath;
  }

  public Path getPath() {
    return path;
  }
}
