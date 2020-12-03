package com.frx.disc.stalker.fs;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Created by surja on 29.11.2020
 */
public class LiveDirectoryTreeCreator implements LiveDirectoryTreeFactory {

  @Override
  public LiveDirectoryTree createAndRegister(Path path) throws IOException {
    return new LiveDirectoryTree(path);
  }
}
