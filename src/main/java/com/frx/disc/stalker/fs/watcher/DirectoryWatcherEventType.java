package com.frx.disc.stalker.fs.watcher;

import java.nio.file.Path;
import java.nio.file.WatchEvent;

import static java.nio.file.StandardWatchEventKinds.*;

public enum DirectoryWatcherEventType {
  CREATED,
  DELETED,
  MODIFIED;

  public static DirectoryWatcherEventType fromWatchEventKind(WatchEvent.Kind<Path> kind) {
    if (kind == ENTRY_CREATE) return CREATED;
    if (kind == ENTRY_DELETE) return DELETED;
    if (kind == ENTRY_MODIFY) return MODIFIED;
    return null;
  }
}
