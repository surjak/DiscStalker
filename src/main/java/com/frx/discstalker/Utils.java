package com.frx.discstalker;

import javafx.scene.control.Alert;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Utils {
  /**
   * Recursively removes all files starting from the given path.
   */
  public static void deleteRecursively(Path path) throws IOException {
    Files.walk(path)
      .map(Path::toFile)
      .sorted((o1, o2) -> -o1.compareTo(o2))
      .forEach(File::delete);
  }

  public static Alert createJavaFXAlert(Alert.AlertType type, String title, String headerText, String content) {
    Alert alert = new Alert(type);
    alert.setTitle(title);
    alert.setHeaderText(headerText);
    alert.setContentText(content);

    return alert;
  }

  public static Long convertToMB(Long bytes) {
    return bytes / 1_000_000;
  }
}
