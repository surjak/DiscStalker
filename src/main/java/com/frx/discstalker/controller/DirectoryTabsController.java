package com.frx.discstalker.controller;

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
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.*;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
  Button saveButton;

  private final ViewLoader viewLoader;

  /*
  * This map tracks opened tabs and allows to withdraw specific information
  * (e.g. size quota) to serialize them in handleSaveAction().
  */
  private final HashMap<Tab, LiveDirectoryController> tabControllers = new HashMap<>();

  // TODO: figure out where such file should be placed
  private final String savedTabsPath = "/home/mateusz/stuff/tmp/tabs.json";

  @Inject
  public DirectoryTabsController(ViewLoader viewLoader) {
    this.viewLoader = viewLoader;
  }

  @FXML
  private void handleAddAction(ActionEvent event) {
    Optional<File> file = chooseDirectoryFromFS();
    file.ifPresent(this::createAndDisplayNewTabWithLiveDirectoryTree);
  }

  @FXML
  private void handleSaveAction(ActionEvent event) {
    List<String> tabPaths = tabControllers.values().stream().map(LiveDirectoryController::getPathString).collect(Collectors.toList());

    try (final var writer = new FileWriter(savedTabsPath)) {
      final var gson = new GsonBuilder().setPrettyPrinting().create();
      gson.toJson(tabPaths, writer);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  @FXML
  private void initialize() {
    JavaFxObservable.changesOf(directoriesTabPane.getTabs())
      .subscribe(event -> {
        if (event.getFlag() == Flag.REMOVED) {
          tabControllers.remove(event.getValue());
        }
      });


    try {
      BufferedReader br = new BufferedReader(new FileReader(savedTabsPath));

      Type typeOfT = new TypeToken<Collection<String>>(){}.getType();
      Collection<String> tabPaths = new Gson().fromJson(br, typeOfT);

      tabPaths.stream().map(File::new).forEach(this::createAndDisplayNewTabWithLiveDirectoryTree);
    } catch (FileNotFoundException e) {
      System.out.println("User saved tabs file" + savedTabsPath + " not found");
    }
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

  private Optional<File> chooseDirectoryFromFS() {
    DirectoryChooser chooser = new DirectoryChooser();
    chooser.setTitle("Open File");
    chooser.setInitialDirectory(new File(System.getProperty("user.home")));
    File file = chooser.showDialog(new Stage());
    return Optional.ofNullable(file);
  }
}
