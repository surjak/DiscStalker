package com.frx.discstalker.fs.watcher;

import java.nio.file.Path;
import java.util.Objects;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    DirectoryWatcherEvent that = (DirectoryWatcherEvent) o;
    return type == that.type &&
      watchedPath.equals(that.watchedPath) &&
      path.equals(that.path);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, watchedPath, path);
  }
}
