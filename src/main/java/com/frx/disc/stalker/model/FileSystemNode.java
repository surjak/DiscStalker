package com.frx.disc.stalker.model;

import javafx.beans.property.LongProperty;
import javafx.beans.property.StringProperty;

import java.nio.file.Path;

/**
 * Represents a node in a file system directory tree.
 */
public interface FileSystemNode {
  Path getPath();

  StringProperty getNodeNameProperty();

  boolean isDirectory();

  LongProperty getSizeProperty();

  Long getSize();

  LongProperty getNumberOfFilesProperty();

  Long getNumberOfFiles();
}
