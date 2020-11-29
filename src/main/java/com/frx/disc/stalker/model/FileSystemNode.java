package com.frx.disc.stalker.model;

import javafx.beans.property.LongProperty;

import java.nio.file.Path;

/**
 * Represents a node in a file system directory tree.
 */
public interface FileSystemNode {
  Path getPath();

  boolean isDirectory();

  LongProperty getSizeProperty();

  Long getSize();

  LongProperty getNumberOfFilesProperty();

  Long getNumberOfFiles();
}
