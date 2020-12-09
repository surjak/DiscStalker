package com.frx.discstalker.controller;

import com.frx.discstalker.ioc.DIModule;
import com.google.inject.Guice;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

/**
 * Created by nazkord on 09.12.2020
 */
public class DirectoryTabsController {

  private final static String DIRECTORY_VIEW_FXML = "/view/liveDirectoryView.fxml";

  @FXML
  TabPane directoriesTabPane;

  @FXML
  Button addButton;

  @FXML
  public void initialize() throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader();
    final var injector = Guice.createInjector(new DIModule());
    fxmlLoader.setControllerFactory(injector::getInstance);
    fxmlLoader.setLocation(getClass().getResource(DIRECTORY_VIEW_FXML));
    Node tabNode = fxmlLoader.load();
    LiveDirectoryController controller = fxmlLoader.getController();

    directoriesTabPane.getSelectionModel().clearSelection();
    // Add Tab ChangeListener
    directoriesTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
      System.out.println("Tab selected: " + newTab.getText());
      if (oldTab != null) {
        oldTab.setContent(null);
      }
      newTab.setContent(tabNode);

      controller.setPath(newTab.getText());
      try {
        controller.renderLiveTree();
      } catch (IOException e) {
        e.printStackTrace();
      }

    });
  }

  @FXML
  private void handleAddAction(ActionEvent event) {
    DirectoryChooser chooser = new DirectoryChooser();
    chooser.setTitle("Open File");
    chooser.setInitialDirectory(new File(System.getProperty("user.home")));
    File file = chooser.showDialog(new Stage());
    if (file != null) {
      String fileAsString = file.toString();
      directoriesTabPane.getTabs().add(new Tab(fileAsString));
      directoriesTabPane.getSelectionModel().selectLast();
    }
  }
}
