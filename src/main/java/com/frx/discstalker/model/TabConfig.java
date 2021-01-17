package com.frx.discstalker.model;

public class TabConfig {
  private String path;
  private Long maximumSize;
  private Long maximumNumberOfFiles;
  private Long maximumFileSize;

  public TabConfig() {}

  public TabConfig(String path, Long maximumSize, Long maximumNumberOfFiles, Long maximumFileSize) {
    this.path = path;
    this.maximumSize = maximumSize;
    this.maximumNumberOfFiles = maximumNumberOfFiles;
    this.maximumFileSize = maximumFileSize;
  }

  public String getPath() {
    return path;
  }

  public Long getMaximumSize() {
    return maximumSize;
  }

  public Long getMaximumNumberOfFiles() {
    return maximumNumberOfFiles;
  }

  public Long getMaximumFileSize() {
    return maximumFileSize;
  }
}
