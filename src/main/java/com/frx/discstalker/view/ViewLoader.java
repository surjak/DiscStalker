package com.frx.discstalker.view;

import com.google.inject.Inject;
import com.google.inject.Injector;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;

/**
 * Created by surjak on 11.12.2020
 */
public class ViewLoader {

  Injector injector;

  @Inject
  public ViewLoader(Injector injector) {
    this.injector = injector;
  }

  public ViewLoaderResponse loadView(String viewFilePath) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader();
    fxmlLoader.setControllerFactory(injector::getInstance);
    fxmlLoader.setLocation(getClass().getResource(viewFilePath));
    Pane pane = fxmlLoader.load();
    return new ViewLoaderResponse(fxmlLoader, pane);
  }

  public final static class ViewLoaderResponse{
    private final FXMLLoader loader;
    private final Pane pane;

    public ViewLoaderResponse(FXMLLoader loader, Pane pane) {
      this.loader = loader;
      this.pane = pane;
    }

    public FXMLLoader getLoader() {
      return loader;
    }

    public Pane getPane() {
      return pane;
    }
  }

}
