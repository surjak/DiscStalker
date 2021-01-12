package com.frx.discstalker.model;

public class TabConfig {
  private String path;
  private long size;
  private boolean isDirectory;

  public TabConfig() {
  }

  public TabConfig(String path, long size) {
    this(path, size, false);
  }

  public TabConfig(String path, long size, boolean isDirectory) {
    this.path = path;
    this.size = size;
    this.isDirectory = isDirectory;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public long getSize() {
    return size;
  }

  public void setSize(long size) {
    this.size = size;
  }

  public void setDirectory(boolean directory) {
    isDirectory = directory;
  }

  public boolean isDirectory() {
    return isDirectory;
  }
}
