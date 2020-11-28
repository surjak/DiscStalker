package com.frx.disc.stalker.fs;

import com.frx.disc.stalker.model.DirectoryNode;
import com.frx.disc.stalker.model.FileNode;
import com.frx.disc.stalker.model.FileSystemNode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * A utility for loading directory trees into their object representation.
 */
public class FileSystemScanner {
  private final Consumer<FileSystemNode> onNewNode;

  /**
   * @param onNewNode A callback called whenever a new node is scanned.
   */
  public FileSystemScanner(Consumer<FileSystemNode> onNewNode) {
    this.onNewNode = onNewNode;
  }

  /**
   * Recursively traverses the filesystem starting at the given path
   * and builds an object representation of the directory tree.
   *
   * Calls {@link #onNewNode} immediately for every new node.
   *
   * @return The root of the built object structure or none
   *         if the given path doesn't point to a valid directory or file.
   */
  public Optional<FileSystemNode> scanRecursively(Path path) throws IOException {
    if (Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
      return Optional.of(scanDirectory(path));
    } else if (Files.isRegularFile(path, LinkOption.NOFOLLOW_LINKS)) {
      return Optional.of(scanFile(path));
    } else {
      return Optional.empty();
    }
  }

  private DirectoryNode scanDirectory(Path path) throws IOException {
    final var liveDir = new DirectoryNode(path);

    final var childPaths = Files.list(path).toArray(Path[]::new);
    for (final var childPath : childPaths) {
      scanRecursively(childPath).ifPresent(liveDir::addChild);
    }

    onNewNode.accept(liveDir);

    return liveDir;
  }

  private FileNode scanFile(Path path) throws IOException {
    final var size = Files.size(path);
    final var fileNode = new FileNode(path, size);

    onNewNode.accept(fileNode);

    return fileNode;
  }
}
