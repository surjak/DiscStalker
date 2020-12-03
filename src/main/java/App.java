import com.frx.discstalker.controller.LiveDirectoryController;
import com.frx.discstalker.fs.LiveDirectoryTreeFactory;
import com.frx.discstalker.ioc.DIModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.nio.file.Paths;

public class App extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {
    var loader = new FXMLLoader();
    BorderPane borderPane = loadMainView(loader, primaryStage);
    LiveDirectoryController controller = loader.getController();
    configureStage(primaryStage, borderPane);
    primaryStage.show();

    final var path = Paths.get("C:\\Users\\surja\\Downloads\\PIPEv4.3.0\\PIPEv4.3.0\\Pipe\\tmp");

    Injector injector = Guice.createInjector(new DIModule());
    LiveDirectoryTreeFactory liveDirectoryTreeFactory = injector.getInstance(LiveDirectoryTreeFactory.class);
    final var liveTree = liveDirectoryTreeFactory.createAndRegister(path);

    controller.registerDirectoryTree(liveTree);
    primaryStage.setOnCloseRequest(t -> {
      Platform.exit();
      System.exit(0);
    });

  }

  private BorderPane loadMainView(FXMLLoader loader, Stage primaryStage) throws java.io.IOException {
    loader.setLocation(getClass().getResource("view/liveDirectoryView.fxml"));
    return loader.load();
  }

  private void configureStage(Stage primaryStage, BorderPane rootLayout) {
    var scene = new Scene(rootLayout);
    primaryStage.setScene(scene);
    primaryStage.setTitle("Disc Stalker");
    primaryStage.minWidthProperty().bind(rootLayout.minWidthProperty());
    primaryStage.minHeightProperty().bind(rootLayout.minHeightProperty());
  }
}
