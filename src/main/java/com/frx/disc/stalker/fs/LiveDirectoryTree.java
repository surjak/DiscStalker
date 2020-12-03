package com.frx.disc.stalker.fs;

import com.frx.disc.stalker.fs.watcher.DirectoryWatcher;
import com.frx.disc.stalker.fs.watcher.DirectoryWatcherEvent;
import com.frx.disc.stalker.fs.watcher.DirectoryWatcherEventType;
import com.frx.disc.stalker.model.DirectoryNode;
import com.frx.disc.stalker.model.FileSystemNode;
import com.google.inject.assistedinject.Assisted;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;

import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Combines the functionality of loading directory structure into the memory
 * and updating it, so that it stays synchronized with the underlying filesystem.
 */
public class LiveDirectoryTree {
  private final FileSystemNode root;
  private final DirectoryWatcher directoryWatcher;
  private final Map<Path, DirectoryNode> directoryByPath;
  private final FileSystemScanner fsScanner;

  public LiveDirectoryTree(@Assisted Path rootPath) throws IOException {
    this.directoryWatcher = new DirectoryWatcher();
    this.directoryByPath = new HashMap<>();
    this.fsScanner = new FileSystemScanner(this::registerNode);

    directoryWatcher.getEventSubject()
      .subscribeOn(Schedulers.io())
      .observeOn(JavaFxScheduler.platform())
      .subscribe(this::handleWatcherEvent);

    this.root = fsScanner.scanRecursively(rootPath)
      .orElseThrow(() -> new IllegalArgumentException("invalid path"));
  }

  private void registerNode(FileSystemNode node) {
    if (node.isDirectory()) {
      final var dirNode = (DirectoryNode) node;

      try {
        directoryWatcher.watchDirectory(dirNode.getPath());
      } catch (IOException ex) { /* TODO: log error (?) */ }

      directoryByPath.put(node.getPath(), dirNode);
    }
  }

  private void handleWatcherEvent(DirectoryWatcherEvent event) throws IOException {
    final var type = event.getType();
    final var dirPath = event.getWatchedPath();
    final var dirNode = directoryByPath.get(dirPath);
    final var childPath = event.getPath();

    if (type == DirectoryWatcherEventType.MODIFIED) {
      dirNode.getChildByPath(childPath).ifPresent(this::synchronizeNode);
    } else if (type == DirectoryWatcherEventType.CREATED) {
      fsScanner.scanRecursively(childPath).ifPresent(dirNode::addChild);
    } else if (type == DirectoryWatcherEventType.DELETED) {
      dirNode.getChildByPath(childPath).ifPresent(dirNode::removeChild);
    }
  }

  private void synchronizeNode(FileSystemNode node) {
    if (!node.isDirectory()) {
      try {
        final var size = Files.size(node.getPath());
        node.getSizeProperty().set(size);
      } catch (IOException e) {
        //ignore for now
      }
    }
  }

  /**
   * Returns the root node of the watched directory tree.
   * <p>
   * All the nested nodes are updated whenever a change to the corresponding files occurs.
   */
  public FileSystemNode getRoot() {
    return root;
  }

  /**
   * Returns the stream of individual file change events.
   */
  public PublishSubject<DirectoryWatcherEvent> getEventSubject() {
    return directoryWatcher.getEventSubject();
  }

  /**
   * Terminates the watcher.
   */
  public void terminate() {
    directoryWatcher.terminate();
  }
}
