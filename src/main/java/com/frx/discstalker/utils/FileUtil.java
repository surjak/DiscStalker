package com.frx.discstalker.utils;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.inject.Singleton;
import java.io.File;
import java.util.Optional;

@Singleton
public class FileUtil {

  public Optional<File> chooseDirectoryFromFS() {
    DirectoryChooser chooser = new DirectoryChooser();
    chooser.setTitle("Choose Directory");
    chooser.setInitialDirectory(new File(System.getProperty("user.home")));
    File file = chooser.showDialog(new Stage());
    return Optional.ofNullable(file);
  }

  public Optional<File> chooseFileFromFS() {
    FileChooser chooser = new FileChooser();
    chooser.setTitle("Load Configuration File");
    chooser.setInitialDirectory(new File(System.getProperty("user.home")));
    chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
    File file = chooser.showOpenDialog(new Stage());
    return Optional.ofNullable(file);
  }

  public Optional<File> saveFileToFS() {
    FileChooser chooser = new FileChooser();
    chooser.setTitle("Save Configuration File");
    chooser.setInitialDirectory(new File(System.getProperty("user.home")));
    chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
    chooser.setInitialFileName("config.json");
    File chosenFile = chooser.showSaveDialog((new Stage()));
    return Optional.ofNullable(chosenFile)
      .map(file -> {
        if(file.getName().toLowerCase().endsWith(".json")) return file;
        return new File(file.toString() + ".json");
      });
  }
}
