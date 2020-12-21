package com.frx.discstalker.view;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.util.stream.Stream;

public class ViewUtils {
  public static String formatSize(Long bytes) {
    if (bytes < 1000) {
      return bytes + " B";
    }

    final var chars = Stream.of("kB", "MB", "GB", "TB", "PB").iterator();
    var suffix = chars.next();
    while (chars.hasNext() && bytes >= 999_950) {
      bytes /= 1000;
      suffix = chars.next();
    }

    String formattedValue = String.format("%.1f %s", bytes / 1000.0, suffix);
    return formattedValue.replace(",", ".");
  }

  public static <T, V> Callback<TableColumn<T, V>, TableCell<T, V>> wrapTextInCellFactory() {
    return new Callback<>() {
      @Override
      public TableCell<T, V> call(TableColumn<T, V> param) {
        TableCell<T, V> tableCell = new TableCell<>() {

          @Override
          protected void updateItem(V item, boolean empty) {
            if (item == getItem()) return;

            super.updateItem(item, empty);

            if (item == null) {
              super.setText(null);
              super.setGraphic(null);
            } else {
              super.setText(null);
              Label l = new Label(item.toString());
              l.setWrapText(true);
              VBox box = new VBox(l);
              l.heightProperty().addListener((observable, oldValue, newValue) -> {
                box.setPrefHeight(newValue.doubleValue() + 7);
                Platform.runLater(() -> this.getTableRow().requestLayout());
              });
              super.setGraphic(box);
            }
          }
        };
        return tableCell;
      }
    };
  }
}
