package com.frx.discstalker.model;

public class FileInfo {
  private String path;
  private long size;
  private boolean isDirectory;

  public FileInfo() {
  }

  public FileInfo(String path, long size) {
    this.path = path;
    this.size = size;
    this.isDirectory = false;
  }

  public FileInfo(String path, long size, boolean isDirectory) {
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
