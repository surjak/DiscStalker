package com.frx.disc.stalker.model;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;

import java.nio.file.Path;

public class FileNode implements FileSystemNode {
  private final Path path;
  private final LongProperty sizeProperty;
  private final LongProperty numberOfFilesProperty;

  public FileNode(Path path, Long size) {
    this.path = path;
    this.sizeProperty = new SimpleLongProperty(size);
    this.numberOfFilesProperty = new SimpleLongProperty(1);
  }

  @Override
  public boolean isDirectory() {
    return false;
  }

  @Override
  public Path getPath() {
    return path;
  }

  @Override
  public LongProperty getSizeProperty() {
    return sizeProperty;
  }

  @Override
  public Long getSize()  {
    return sizeProperty.get();
  }

  @Override
  public LongProperty getNumberOfFilesProperty() {
    return numberOfFilesProperty;
  }

  @Override
  public Long getNumberOfFiles() {
    return numberOfFilesProperty.get();
  }
}
