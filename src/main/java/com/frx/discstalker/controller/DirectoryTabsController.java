package com.frx.discstalker.controller;

import com.google.inject.Inject;
import com.google.inject.Injector;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.io.IOException;

/**
 * Created by nazkord on 09.12.2020
 */
public class DirectoryTabsController {

  private final static String lab1 = "/Users/nazkord/AGH/5_semestr/TO/Lab1";
  private final static String lab2 = "/Users/nazkord/AGH/5_semestr/TO/Lab2";
  private final static String lab3 = "/Users/nazkord/AGH/5_semestr/TO/Lab3";
  private final static String directoryView = "view/directoryTabs.fxml";
  // The FXMLLoader
  private final FXMLLoader fxmlLoader = new FXMLLoader();
  @FXML
  TabPane directoriesTabPane;

  @Inject
  private Injector injector;

  @FXML
  public void initialize() throws IOException {

    fxmlLoader.setControllerFactory(injector::getInstance);
    fxmlLoader.setLocation(getClass().getResource("/view/liveDirectoryView.fxml"));
    Node tabNode = fxmlLoader.load();
    LiveDirectoryController controller = fxmlLoader.getController();

    directoriesTabPane.getTabs().add(new Tab(lab1));
    directoriesTabPane.getTabs().add(new Tab(lab2));
    directoriesTabPane.getTabs().add(new Tab(lab3));

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
    // By default, select 1st tab and load its content.
    directoriesTabPane.getSelectionModel().selectFirst();

  }
}
