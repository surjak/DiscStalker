package com.frx.disc.stalker.model;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.nio.file.Path;

public class FileNode implements FileSystemNode {
  private final Path path;
  private final LongProperty sizeProperty;
  private final LongProperty numberOfFilesProperty;
  private final StringProperty fileName;

  public FileNode(Path path, Long size) {
    this.path = path;
    this.sizeProperty = new SimpleLongProperty(size);
    this.numberOfFilesProperty = new SimpleLongProperty(1);
    this.fileName = new SimpleStringProperty(path.getFileName().toString());
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
  public StringProperty getNodeNameProperty() {
    return fileName;
  }

  @Override
  public LongProperty getSizeProperty() {
    return sizeProperty;
  }

  @Override
  public Long getSize() {
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
