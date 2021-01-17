package com.frx.discstalker.statistics.concreteStatistics.filesStatistics;

import com.frx.discstalker.Utils;
import com.frx.discstalker.model.FileNode;
import com.frx.discstalker.statistics.Statistic;
import com.google.common.base.Strings;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by surjak on 20.12.2020
 */
public class NLargestFiles extends BaseFilesStatistic {

  private static final String STATISTIC_NAME = "Largest files";
  private final Integer N;
  private ObservableList<FileNode> filesToDisplay = FXCollections.observableArrayList();

  public NLargestFiles(Integer N) {
    super(STATISTIC_NAME);
    this.N = N;
  }

  public NLargestFiles() {
    this(3);
  }

  @Override
  public void calculateValue(List<FileNode> fileSystemNodes) {
    removeAllElementsFromDisplayList();

    Map<String, Long> statisticByType = fileSystemNodes.stream()
      .sorted(Comparator.comparingLong(FileNode::getSize).reversed())
      .limit(N)
      .map(this::addFileToDisplayList)
      .collect(Collectors.toMap(fileNode -> fileNode.getPath().toString(), FileNode::getSize));

    writeIntoValue(statisticByType);
  }

  private void removeAllElementsFromDisplayList() {
    filesToDisplay.removeIf(data -> true);
  }

  private FileNode addFileToDisplayList(FileNode fileNode) {
    filesToDisplay.add(fileNode);
    return fileNode;
  }

  private void writeIntoValue(Map<String, Long> statisticByType) {
    StringBuffer buffer = new StringBuffer();
    statisticByType.entrySet()
      .stream()
      .sorted((o1, o2) -> -1 * (int) (o1.getValue() - o2.getValue())) //workaround Comparator.comparingLong(Map.Entry::getValue).reversed()
      .forEach(stringLongEntry -> buffer.append(stringLongEntry.getKey() + " : " + stringLongEntry.getValue() + "\n"));
    setContent(buffer.toString());
  }

  @Override
  public Node getValueAsNode() {
    ListView<FileNode> listView = new ListView<>();
    listView.setItems(filesToDisplay);
    listView.setCellFactory(param -> listViewCellFactory());
    listView.setMaxHeight(150);
    listView.prefHeight(150);
    listView.setFixedCellSize(50);

    return listView;
  }

  private ListCell<FileNode> listViewCellFactory() {
    ListCell<FileNode> cell = displayPathInTextFieldCell();

    ContextMenu contextMenu = new ContextMenu();

    MenuItem deleteItem = new MenuItem();
    deleteItem.setText("Delete");

    deleteItem.setOnAction(event -> deleteSelectedItem(cell));
    contextMenu.getItems().add(deleteItem);

    cell.setContextMenu(contextMenu);
    return cell;
  }

  private void deleteSelectedItem(ListCell<FileNode> cell) {
    final var node = cell.getItem();

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
  }

  private ListCell<FileNode> displayPathInTextFieldCell() {
    return new ListCell<>() {
      @Override
      protected void updateItem(FileNode item, boolean empty) {
        super.updateItem(item, empty);
        if (null != item) {
          setText(item.getPath().toString());
        } else {
          setText("");
        }
      }
    };
  }
}
