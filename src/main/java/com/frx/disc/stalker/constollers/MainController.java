package com.frx.disc.stalker.constollers;

import com.frx.disc.stalker.fs.LiveDirectoryTree;
import com.frx.disc.stalker.model.DirectoryNode;
import com.frx.disc.stalker.model.FileSystemNode;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.*;

/**
 * Created by surja on 29.11.2020
 */
public class MainController {

    private DirectoryNode root;

    @FXML
    private TreeTableView<FileSystemNode> treeTableView;

    @FXML
    public void initialize() {

        TreeTableColumn<FileSystemNode, String> treeTableColumn1 = new TreeTableColumn<>("Path");
        TreeTableColumn<FileSystemNode, String> treeTableColumn2 = new TreeTableColumn<>("Size");

        treeTableColumn1.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getPath().getFileName().toString()));
        treeTableColumn2.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getSize().toString()));
        treeTableColumn1.setEditable(true);
        treeTableColumn2.setEditable(true);
        treeTableView.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
        treeTableView.setEditable(true);
        treeTableView.getColumns().add(treeTableColumn1);
        treeTableView.getColumns().add(treeTableColumn2);


    }

    public void registerDirectoryTree(LiveDirectoryTree directoryTree) {
        this.root = (DirectoryNode) directoryTree.getRoot();

        TreeItem rootItem = new TreeItem(root);
        fillTreeTable(rootItem, root);
        treeTableView.setRoot(rootItem);
        refreshTreeOnChangeEvent(directoryTree);
    }

    private void refreshTreeOnChangeEvent(LiveDirectoryTree directoryTree) {
        directoryTree.getEventSubject()
                .subscribeOn(Schedulers.io())
                .observeOn(JavaFxScheduler.platform())
                .subscribe(directoryWatcherEvent -> {
                    treeTableView.refresh();
                });
    }


    private void fillTreeTable(TreeItem treeItem, FileSystemNode systemNode) {
        if (systemNode.isDirectory()) {
            DirectoryNode directoryNode = (DirectoryNode) systemNode;
            fillSubTree(treeItem, directoryNode);
            directoryNode.getChildNodes().addListener((ListChangeListener<FileSystemNode>) change -> {
                while (change.next()){
                    if(change.wasAdded()){
                        handleCreateChange(treeItem, change);
                    }
                    if(change.wasRemoved()){
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
        change.getAddedSubList().forEach(systemNode1 -> {
            TreeItem subTree = new TreeItem(systemNode1);
            treeItem.getChildren().add(subTree);
            fillTreeTable(subTree, systemNode1);
        });
    }

    private void handleRemoveChange(TreeItem treeItem, ListChangeListener.Change<? extends FileSystemNode> change) {
        change.getRemoved().forEach(t->{
            final List<TreeItem<FileSystemNode>> itemsToRemove = new ArrayList<>();

            for (Object child : treeItem.getChildren()) {
                TreeItem treeItem1 = (TreeItem) child;
                if (treeItem1.getValue().equals(t)){
                    itemsToRemove.add(treeItem1);
                }
            }

            treeItem.getChildren().removeAll(itemsToRemove);
        });
    }

}
