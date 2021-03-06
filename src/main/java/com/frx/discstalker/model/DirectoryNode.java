package com.frx.discstalker.model;

import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.nio.file.Path;
import java.util.Optional;

public class DirectoryNode implements IFileSystemNode {
  private final ObjectProperty<Path> pathProperty;
  private final ObservableList<IFileSystemNode> childNodes;
  private final LongProperty sizeProperty;
  private final LongProperty numberOfFilesProperty;
  private final StringProperty directoryName;

  public DirectoryNode(Path path, Long size) {
    this.pathProperty = new SimpleObjectProperty<>(path);
    this.childNodes = FXCollections.observableArrayList();
    this.sizeProperty = new SimpleLongProperty(size);
    this.numberOfFilesProperty = new SimpleLongProperty(0);
    this.directoryName = new SimpleStringProperty(path.getFileName().toString());
  }

  public void addChild(IFileSystemNode child) {
    childNodes.add(child);

    increaseSizeBy(child.getSize());
    child.getSizeProperty().addListener(this::handleChildSizeChange);

    increaseNumberOfFilesBy(child.getNumberOfFiles());
    child.getNumberOfFilesProperty().addListener(this::handleChildNumberOfFilesChange);
  }

  public void removeChild(IFileSystemNode child) {
    childNodes.remove(child);

    increaseSizeBy(-child.getSize());
    child.getSizeProperty().removeListener(this::handleChildSizeChange);

    increaseNumberOfFilesBy(-child.getNumberOfFiles());
    child.getNumberOfFilesProperty().removeListener(this::handleChildNumberOfFilesChange);
  }

  private void handleChildSizeChange(Observable observable, Number oldSize, Number newSize) {
    increaseSizeBy(newSize.longValue() - oldSize.longValue());
  }

  private void increaseSizeBy(Long diff) {
    sizeProperty.set(sizeProperty.get() + diff);
  }

  private void handleChildNumberOfFilesChange(Observable observable, Number oldNumberOfFiles, Number newNumberOfFiles) {
    increaseNumberOfFilesBy(newNumberOfFiles.longValue() - oldNumberOfFiles.longValue());
  }

  private void increaseNumberOfFilesBy(Long diff) {
    numberOfFilesProperty.set(numberOfFilesProperty.get() + diff);
  }

  public ObservableList<IFileSystemNode> getChildNodes() {
    return childNodes;
  }

  public Optional<IFileSystemNode> getChildByPath(Path path) {
    return childNodes.stream()
      .filter(child -> child.getPath().equals(path))
      .findAny();
  }

  @Override
  public boolean isDirectory() {
    return true;
  }

  @Override
  public Path getPath() {
    return pathProperty.getValue();
  }

  @Override
  public StringProperty getNodeNameProperty() {
    return directoryName;
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
