package com.frx.discstalker.model;

import io.vavr.control.Try;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.apache.tika.Tika;

import java.nio.file.Path;

public class FileNode implements IFileSystemNode {
  private final Path path;
  private final LongProperty sizeProperty;
  private final LongProperty numberOfFilesProperty;
  private final StringProperty fileName;
  private String mimeType;

  public FileNode(Path path, Long size) {
    this.path = path;
    this.sizeProperty = new SimpleLongProperty(size);
    this.numberOfFilesProperty = new SimpleLongProperty(1);
    this.fileName = new SimpleStringProperty(path.getFileName().toString());
    setFileExtension();
  }

  public String getMimeType() {
    return this.mimeType;
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

  private void setFileExtension() {
    Tika tika = new Tika();
    Try.of(() -> tika.detect(path.toFile()))
      .onSuccess(mimeType -> this.mimeType = mimeType)
      .onFailure(throwable -> this.mimeType = "");

    fileName.addListener((observable, oldValue, newValue) -> setFileExtension());
  }

}
