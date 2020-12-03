package com.frx.discstalker.fs;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Created by surja on 29.11.2020
 */
public interface ILiveDirectoryTreeFactory {
  LiveDirectoryTree createAndRegister(Path path) throws IOException;
}
