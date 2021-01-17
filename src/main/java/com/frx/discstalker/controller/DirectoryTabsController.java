package com.frx.discstalker.controller;

import com.frx.discstalker.model.TabConfig;
import com.frx.discstalker.service.notification.ErrorNotification;
import com.frx.discstalker.statistics.concreteStatistics.directoryStatistics.PercentageUsageOfAllowedSpace;
import com.frx.discstalker.utils.TabConfigUtil;
import com.frx.discstalker.utils.FileUtil;
import com.frx.discstalker.view.ViewLoader;
import com.google.inject.Inject;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import io.reactivex.rxjavafx.sources.Flag;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * Created by nazkord on 09.12.2020
 */
public class DirectoryTabsController {

  private final static String DIRECTORY_VIEW_FXML = "/view/liveDirectoryView.fxml";
  private final ViewLoader viewLoader;
  private final FileUtil fileUtil;
  private final TabConfigUtil tabConfigUtil;
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
  public DirectoryTabsController(ViewLoader viewLoader, FileUtil fileUtil, TabConfigUtil tabConfigUtil) {
    this.viewLoader = viewLoader;
    this.fileUtil = fileUtil;
    this.tabConfigUtil = tabConfigUtil;
  }

  @FXML
  private void handleAddAction(ActionEvent event) {
    Optional<File> file = fileUtil.chooseDirectoryFromFS();
    file.ifPresent(this::createAndDisplayNewTabWithLiveDirectoryTree);
  }

  @FXML
  private void handleSaveAction(ActionEvent event) {
    List<TabConfig> tabConfigs = tabConfigUtil.getFileInfosFrom(tabControllers.values());
    fileUtil.chooseSaveFilePath().ifPresent(file -> tabConfigUtil.writeFileInfosTo(tabConfigs, file));
  }

  @FXML
  private void handleLoadAction() {
    fileUtil.chooseFileFromFS().ifPresent(this::loadConfigDataFrom);
  }

  private void loadConfigDataFrom(File file) {
    try {
      Collection<TabConfig> tabConfigs = tabConfigUtil.readFileInfosFromJson(file);
      loadTabsFrom(tabConfigs);
      loadStatsFrom(tabConfigs);
    } catch (FileNotFoundException e) {
      String errorMessage = "User configuration file " + file.toString() + " not found";
      new ErrorNotification(errorMessage, "Choose other json file than " + file.toString()).show();
      System.out.println(errorMessage);
    }
  }

  private void loadStatsFrom(Collection<TabConfig> tabConfigs) {
    for (Tab tab : tabControllers.keySet()) {
      final var tabConfig = tabConfigs.stream()
        .filter(config -> config.getPath().equals(tab.getText()))
        .findFirst()
        .orElseThrow();

      final var notificationController = tabControllers.get(tab).getStatisticsController().getNotificationController();
      notificationController.setNewMaximumSize(tabConfig.getMaximumSize());
      notificationController.setNewMaximumNumberOfFiles(tabConfig.getMaximumNumberOfFiles());
      notificationController.setNewMaximumFileSize(tabConfig.getMaximumFileSize());
    }
  }

  private void loadTabsFrom(Collection<TabConfig> tabConfigs) {
    tabConfigs
      .stream()
      .map(TabConfig::getPath)
      .map(File::new)
      .forEach(this::createAndDisplayNewTabWithLiveDirectoryTree);
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
