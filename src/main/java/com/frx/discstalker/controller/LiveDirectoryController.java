package com.frx.discstalker.controller;

import com.frx.discstalker.fs.ILiveDirectoryTreeFactory;
import com.frx.discstalker.fs.LiveDirectoryTree;
import com.frx.discstalker.model.DirectoryNode;
import com.frx.discstalker.model.IFileSystemNode;
import com.frx.discstalker.stats.StatisticsProvider;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import io.reactivex.rxjavafx.sources.Flag;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by surja on 29.11.2020
 */
public class LiveDirectoryController {
  @Inject
  private ILiveDirectoryTreeFactory liveDirectoryTreeFactory;

  @FXML
  private StatisticsController statisticsController;

  @FXML
  private TreeTableView<IFileSystemNode> treeTableView;

  private Path path;

  public void setPath(String path) {
    this.path = Paths.get(path);
  }

  @FXML
  public void initialize() {
    treeTableView.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);

    final var pathColumn = new TreeTableColumn<IFileSystemNode, String>("Path");
    pathColumn.setCellValueFactory(param -> param.getValue().getValue().getNodeNameProperty());
    treeTableView.getColumns().add(pathColumn);

    final var sizeColumn = new TreeTableColumn<IFileSystemNode, Long>("Size");
    sizeColumn.setCellValueFactory(param -> param.getValue().getValue().getSizeProperty().asObject());
    treeTableView.getColumns().add(sizeColumn);
  }

  public void renderLiveTree() throws IOException {
    final var liveDirectoryTree = liveDirectoryTreeFactory.createAndRegister(path);
    registerStatisticModel(liveDirectoryTree);
    final var root = (DirectoryNode) liveDirectoryTree.getRoot();
    final var rootItem = new TreeItem<IFileSystemNode>(root);
    fillTree(rootItem, root);
    treeTableView.setRoot(rootItem);
    rootItem.setExpanded(true);
  }

  private void fillTree(TreeItem<IFileSystemNode> treeItem, IFileSystemNode node) {
    if (node.isDirectory()) {
      final var directoryNode = (DirectoryNode) node;

      directoryNode.getChildNodes().forEach(child -> {
        addNodeToTree(treeItem, child);
      });

      JavaFxObservable.changesOf(directoryNode.getChildNodes())
        .subscribe(event -> {
          if (event.getFlag() == Flag.ADDED) {
            addNodeToTree(treeItem, event.getValue());
          } else if (event.getFlag() == Flag.REMOVED) {
            removeNodeFromTree(treeItem, event.getValue());
          }
        });
    }
  }

  private void addNodeToTree(TreeItem<IFileSystemNode> parentTreeItem, IFileSystemNode node) {
    final var childTreeItem = new TreeItem<>(node);
    parentTreeItem.getChildren().add(childTreeItem);
    fillTree(childTreeItem, node);
  }

  private void removeNodeFromTree(TreeItem<IFileSystemNode> parentTreeItem, IFileSystemNode node) {
    parentTreeItem.getChildren().removeIf(childTreeItem -> childTreeItem.getValue().equals(node));
  }

  private void registerStatisticModel(LiveDirectoryTree liveDirectoryTree) {
    StatisticsProvider model = new StatisticsProvider(liveDirectoryTree);
    statisticsController.registerStatisticModel(model);
  }
}
