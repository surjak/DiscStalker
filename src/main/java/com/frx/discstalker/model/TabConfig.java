package com.frx.discstalker.model;

import java.util.Optional;

public class TabConfig {
  private String path;
  private Long maximumSize;
  private Long maximumNumberOfFiles;
  private Long maximumFileSize;

  public TabConfig(String path, Long maximumSize, Long maximumNumberOfFiles, Long maximumFileSize) {
    this.path = path;
    this.maximumSize = maximumSize;
    this.maximumNumberOfFiles = maximumNumberOfFiles;
    this.maximumFileSize = maximumFileSize;
  }

  public String getPath() {
    return path;
  }

  public Optional<Long> getMaximumSize() {
    return Optional.ofNullable(maximumSize);
  }

  public Optional<Long> getMaximumNumberOfFiles() {
    return Optional.ofNullable(maximumNumberOfFiles);
  }

  public Optional<Long> getMaximumFileSize() {
    return Optional.ofNullable(maximumFileSize);
  }
}
