package com.frx.discstalker.controller;

import com.frx.discstalker.Utils;
import com.frx.discstalker.fs.ILiveDirectoryTreeFactory;
import com.frx.discstalker.fs.LiveDirectoryTree;
import com.frx.discstalker.model.DirectoryNode;
import com.frx.discstalker.model.IFileSystemNode;
import com.frx.discstalker.statistics.Statistic;
import com.frx.discstalker.statistics.StatisticsProvider;
import com.frx.discstalker.statistics.concreteStatistics.directoryStatistics.DirectoryStatisticsCalculator;
import com.frx.discstalker.statistics.concreteStatistics.filesStatistics.FileStatisticsCalculator;
import com.frx.discstalker.view.ViewUtils;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import io.reactivex.rxjavafx.sources.Flag;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

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

  public String getPathString() {
    return this.path.toString();
  }

  @FXML
  public void initialize() {
    treeTableView.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);

    treeTableView.setRowFactory(this::rowFactory);

    final var pathColumn = new TreeTableColumn<IFileSystemNode, String>("Path");
    pathColumn.setCellValueFactory(param -> param.getValue().getValue().getNodeNameProperty());
    treeTableView.getColumns().add(pathColumn);

    final var sizeColumn = new TreeTableColumn<IFileSystemNode, String>("Size");
    sizeColumn.setCellValueFactory(param -> {
      final var sizeProperty = param.getValue().getValue().getSizeProperty();
      return Bindings.createStringBinding(() -> ViewUtils.formatSize(sizeProperty.get()), sizeProperty);
    });
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

  public TreeTableRow<IFileSystemNode> rowFactory(TreeTableView<IFileSystemNode> ttw) {
    final var row = new TreeTableRow<IFileSystemNode>();
    final var deleteMenuItem = new MenuItem("Delete");
    final var contextMenu = new ContextMenu(deleteMenuItem);
    row.setContextMenu(contextMenu);

    setDeleteMenuItemHandler(row, deleteMenuItem);

    return row;
  }

  public Optional<Statistic> findConcreteStatistic(Class<? extends Statistic> statisticName) {
    return statisticsController.getStatisticsProvider().findConcreteStatisticBy(statisticName);
  }

  private void setDeleteMenuItemHandler(TreeTableRow<IFileSystemNode> row, MenuItem deleteMenuItem) {
    deleteMenuItem.setOnAction(event -> {
      final var node = row.getTreeItem().getValue();

      Alert alert = Utils.createJavaFXAlert(
        Alert.AlertType.CONFIRMATION,
        "Remove file",
        "Are you sure?",
        "This will irreversibly remove " + node.getPath().toString()
      );

      Optional<ButtonType> result = alert.showAndWait();
      result.ifPresent(response -> {
        if (response == ButtonType.OK) {
          try {
            Utils.deleteRecursively(node.getPath());
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      });
    });
  }

  private void fillTree(TreeItem<IFileSystemNode> treeItem, IFileSystemNode node) {
    if (node.isDirectory()) {
      final var directoryNode = (DirectoryNode) node;

      directoryNode.getChildNodes().forEach(child -> addNodeToTree(treeItem, child));

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
    StatisticsProvider statisticsProvider = new StatisticsProvider(liveDirectoryTree,
      List.of(new FileStatisticsCalculator(), new DirectoryStatisticsCalculator()));
    statisticsController.registerStatisticModel(statisticsProvider);
  }
}
