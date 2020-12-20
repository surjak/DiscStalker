package com.frx.discstalker.controller;

import com.frx.discstalker.view.ViewLoader;
import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

/**
 * Created by nazkord on 09.12.2020
 */
public class DirectoryTabsController {

  private final static String DIRECTORY_VIEW_FXML = "/view/liveDirectoryView.fxml";

  @FXML
  TabPane directoriesTabPane;

  @FXML
  Button addButton;

  private final ViewLoader viewLoader;

  @Inject
  public DirectoryTabsController(ViewLoader viewLoader) {
    this.viewLoader = viewLoader;
  }

  @FXML
  private void handleAddAction(ActionEvent event) {
    Optional<File> file = chooseDirectoryFromFS();
    file.ifPresent(this::createAndDisplayNewTabWithLiveDirectoryTree);
  }

  private void createAndDisplayNewTabWithLiveDirectoryTree(File file) {
    String fileAsString = file.toString();
    try {
      ViewLoader.ViewLoaderResponse viewLoaderResponse = viewLoader.loadView(DIRECTORY_VIEW_FXML);
      registerLiveDirectoryTree(fileAsString, viewLoaderResponse.getLoader());
      addTabAndDisplayPaneInside(fileAsString, viewLoaderResponse.getPane());
    } catch (IOException e) {
      //ignore
      e.printStackTrace();
      System.out.println("Unexpected file " + fileAsString + " or " + DIRECTORY_VIEW_FXML);
    }
  }

  private void addTabAndDisplayPaneInside(String fileAsString, Pane pane) {
    directoriesTabPane.getTabs().add(new Tab(fileAsString, pane));
    directoriesTabPane.getSelectionModel().selectLast();
  }

  private void registerLiveDirectoryTree(String fileAsString, FXMLLoader loader) throws IOException {
    LiveDirectoryController controller = loader.getController();
    controller.setPath(fileAsString);
    controller.renderLiveTree();
  }

  private Optional<File> chooseDirectoryFromFS() {
    DirectoryChooser chooser = new DirectoryChooser();
    chooser.setTitle("Open File");
    chooser.setInitialDirectory(new File(System.getProperty("user.home")));
    File file = chooser.showDialog(new Stage());
    return Optional.ofNullable(file);
  }
}
