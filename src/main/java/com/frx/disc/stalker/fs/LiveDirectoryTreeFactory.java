package com.frx.disc.stalker.fs;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Created by surja on 29.11.2020
 */
public interface LiveDirectoryTreeFactory {
    LiveDirectoryTree createAndRegister(Path path) throws IOException;
}
