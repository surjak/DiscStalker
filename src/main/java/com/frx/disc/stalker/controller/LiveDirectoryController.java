package com.frx.disc.stalker.controller;

import com.frx.disc.stalker.fs.LiveDirectoryTree;
import com.frx.disc.stalker.model.DirectoryNode;
import com.frx.disc.stalker.model.FileSystemNode;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.*;

/**
 * Created by surja on 29.11.2020
 */
public class LiveDirectoryController {

  private DirectoryNode root;

  @FXML
  private TreeTableView<FileSystemNode> treeTableView;

  @FXML
  public void initialize() {

    TreeTableColumn<FileSystemNode, String> pathColumn = new TreeTableColumn<>("Path");
    TreeTableColumn<FileSystemNode, Long> sizeColumn = new TreeTableColumn<>("Size");

    pathColumn.setCellValueFactory(param -> param.getValue().getValue().getNodeNameProperty());
    sizeColumn.setCellValueFactory(param -> param.getValue().getValue().getSizeProperty().asObject());
    pathColumn.setEditable(true);
    sizeColumn.setEditable(true);
    treeTableView.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
    treeTableView.setEditable(true);
    treeTableView.getColumns().add(pathColumn);
    treeTableView.getColumns().add(sizeColumn);


  }

  public void registerDirectoryTree(LiveDirectoryTree directoryTree) {
    this.root = (DirectoryNode) directoryTree.getRoot();

    TreeItem rootItem = new TreeItem(root);
    fillTreeTable(rootItem, root);
    treeTableView.setRoot(rootItem);
  }


  private void fillTreeTable(TreeItem treeItem, FileSystemNode systemNode) {
    if (systemNode.isDirectory()) {
      DirectoryNode directoryNode = (DirectoryNode) systemNode;
      fillSubTree(treeItem, directoryNode);
      directoryNode.getChildNodes().addListener((ListChangeListener<FileSystemNode>) change -> {
        while (change.next()) {
          if (change.wasAdded()) {
            handleCreateChange(treeItem, change);
          }
          if (change.wasRemoved()) {
            handleRemoveChange(treeItem, change);
          }
        }
      });
    }
  }

  private void fillSubTree(TreeItem treeItem, DirectoryNode directoryNode) {
    directoryNode.getChildNodes().forEach(child -> {
      TreeItem subTree = new TreeItem(child);

      treeItem.getChildren().add(subTree);
      fillTreeTable(subTree, child);
    });
  }

  private void handleCreateChange(TreeItem treeItem, ListChangeListener.Change<? extends FileSystemNode> change) {
    change.getAddedSubList().forEach(node -> {
      TreeItem subTree = new TreeItem(node);
      treeItem.getChildren().add(subTree);
      fillTreeTable(subTree, node);
    });
  }

  private void handleRemoveChange(TreeItem treeItem, ListChangeListener.Change<? extends FileSystemNode> change) {
    change.getRemoved().forEach(node -> {
      final List<TreeItem<FileSystemNode>> itemsToRemove = new ArrayList<>();

      for (Object child : treeItem.getChildren()) {
        TreeItem treeItem1 = (TreeItem) child;
        if (treeItem1.getValue().equals(node)) {
          itemsToRemove.add(treeItem1);
        }
      }

      treeItem.getChildren().removeAll(itemsToRemove);
    });
  }

}
