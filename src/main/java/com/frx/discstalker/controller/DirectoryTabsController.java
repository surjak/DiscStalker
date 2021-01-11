package com.frx.discstalker.controller;

import com.frx.discstalker.model.FileInfo;
import com.frx.discstalker.statistics.concreteStatistics.directoryStatistics.PercentageUsageOfAllowedSpace;
import com.frx.discstalker.utils.FileUtil;
import com.frx.discstalker.view.ViewLoader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import io.reactivex.rxjavafx.sources.Flag;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Created by nazkord on 09.12.2020
 */
public class DirectoryTabsController {

  private final static String DIRECTORY_VIEW_FXML = "/view/liveDirectoryView.fxml";
  private final ViewLoader viewLoader;
  private final FileUtil fileUtil;
  /*
   * This map tracks opened tabs and allows to withdraw specific information
   * (e.g. size quota) to serialize them in handleSaveAction().
   */
  private final HashMap<Tab, LiveDirectoryController> tabControllers = new HashMap<>();
  @FXML
  TabPane directoriesTabPane;
  @FXML
  Button addButton;
  @FXML
  Button saveButton;
  @FXML
  Button loadButton;

  @Inject
  public DirectoryTabsController(ViewLoader viewLoader, FileUtil fileUtil) {
    this.viewLoader = viewLoader;
    this.fileUtil = fileUtil;
  }

  @FXML
  private void handleAddAction(ActionEvent event) {
    Optional<File> file = fileUtil.chooseDirectoryFromFS();
    file.ifPresent(this::createAndDisplayNewTabWithLiveDirectoryTree);
  }

  @FXML
  private void handleSaveAction(ActionEvent event) {
    List<FileInfo> fileInfos = new ArrayList<>();
    for (LiveDirectoryController controller : tabControllers.values()) {
      String tabPath = controller.getPathString();
      Long maxSize = controller
        .findConcreteStatistic(PercentageUsageOfAllowedSpace.class)
        .map(PercentageUsageOfAllowedSpace.class::cast)
        .map(PercentageUsageOfAllowedSpace::getMaxSizeInMB)
        .orElse(0L);
      fileInfos.add(new FileInfo(tabPath, maxSize, true));
    }

    fileUtil.chooseSaveFilePath().ifPresent(file -> {
      try (var writer = new FileWriter(file)) {
        var gson = new GsonBuilder().setPrettyPrinting().create();
        gson.toJson(fileInfos, writer);
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    });
  }

  @FXML
  private void handleLoadAction() {
    fileUtil.chooseFileFromFS()
      .ifPresentOrElse(file -> {
        try {
          readFileInfosFromJson(file)
            .stream()
            .map(FileInfo::getPath)
            .map(File::new)
            .forEach(this::createAndDisplayNewTabWithLiveDirectoryTree);
        } catch (FileNotFoundException e) {
          System.out.println("User configuration file " + file.toString() + " not found");
        }
      }, () -> {
      });
  }

  private Collection<FileInfo> readFileInfosFromJson(File file) throws FileNotFoundException {
    BufferedReader br = new BufferedReader(new FileReader(file));
    Type listType = new TypeToken<Collection<FileInfo>>() {
    }.getType();
    return new Gson().fromJson(br, listType);
  }

  @FXML
  private void initialize() {
    JavaFxObservable.changesOf(directoriesTabPane.getTabs())
      .subscribe(event -> {
        if (event.getFlag() == Flag.REMOVED) {
          tabControllers.remove(event.getValue());
        }
      });
  }

  private void createAndDisplayNewTabWithLiveDirectoryTree(File file) {
    String fileAsString = file.toString();
    try {
      ViewLoader.ViewLoaderResponse viewLoaderResponse = viewLoader.loadView(DIRECTORY_VIEW_FXML);
      registerLiveDirectoryTree(fileAsString, viewLoaderResponse.getLoader());
      addTabAndDisplayPaneInside(fileAsString, viewLoaderResponse);
    } catch (IOException e) {
      //ignore
      e.printStackTrace();
      System.out.println("Unexpected file " + fileAsString + " or " + DIRECTORY_VIEW_FXML);
    }
  }

  private void addTabAndDisplayPaneInside(String fileAsString, ViewLoader.ViewLoaderResponse viewLoaderResponse) {
    Tab tab = new Tab(fileAsString, viewLoaderResponse.getPane());
    tabControllers.put(tab, viewLoaderResponse.getLoader().getController());

    directoriesTabPane.getTabs().add(tab);
    directoriesTabPane.getSelectionModel().selectLast();
  }

  private void registerLiveDirectoryTree(String fileAsString, FXMLLoader loader) throws IOException {
    LiveDirectoryController controller = loader.getController();
    controller.setPath(fileAsString);
    controller.renderLiveTree();
  }
}
