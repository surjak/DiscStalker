import com.frx.discstalker.ioc.DIModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.IOException;

public class App extends Application {
  public static void main(String[] args) {
    App.launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    final var injector = Guice.createInjector(new DIModule());

    final var borderPane = loadMainView(injector);
    configureStage(primaryStage, borderPane);
    primaryStage.setOnCloseRequest(event -> {
      Platform.exit();
      System.exit(0);
    });

    borderPane.requestFocus();
    primaryStage.show();
  }

  private TabPane loadMainView(Injector injector) throws IOException {
    final var fxmlLoader = new FXMLLoader();
//    fxmlLoader.setControllerFactory(injector::getInstance);
    fxmlLoader.setLocation(getClass().getResource("view/directoryTabs.fxml"));
    return fxmlLoader.load();
  }

  private void configureStage(Stage primaryStage, TabPane rootLayout) {
    final var scene = new Scene(rootLayout);
    primaryStage.setScene(scene);
    primaryStage.setTitle("Disc Stalker");
    primaryStage.minWidthProperty().bind(rootLayout.minWidthProperty());
    primaryStage.minHeightProperty().bind(rootLayout.minHeightProperty());
  }
}
