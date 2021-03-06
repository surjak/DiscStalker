package com.frx.discstalker.fs;

import com.frx.discstalker.model.DirectoryNode;
import com.frx.discstalker.model.FileNode;
import com.frx.discstalker.model.IFileSystemNode;

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
  private final Consumer<IFileSystemNode> onNewNode;

  /**
   * @param onNewNode A callback called whenever a new node is scanned.
   */
  public FileSystemScanner(Consumer<IFileSystemNode> onNewNode) {
    this.onNewNode = onNewNode;
  }

  /**
   * Recursively traverses the filesystem starting at the given path
   * and builds an object representation of the directory tree.
   * <p>
   * Calls {@link #onNewNode} immediately for every new node.
   *
   * @return The root of the built object structure or none
   * if the given path doesn't point to a valid directory or file.
   */
  public Optional<IFileSystemNode> scanRecursively(Path path) throws IOException {
    if (Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
      return Optional.of(scanDirectory(path));
    } else if (Files.isRegularFile(path, LinkOption.NOFOLLOW_LINKS)) {
      return Optional.of(scanFile(path));
    } else {
      return Optional.empty();
    }
  }

  private DirectoryNode scanDirectory(Path path) throws IOException {
    final var size = Files.size(path);
    final var liveDir = new DirectoryNode(path, size);

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
