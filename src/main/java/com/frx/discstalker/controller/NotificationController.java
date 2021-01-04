package com.frx.discstalker.controller;

import com.frx.discstalker.service.notification.ErrorNotification;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.util.Optional;

public class NotificationController {

  private static final String NOTIFICATION_TITLE = "Maximum size of the chosen directory";

  @FXML
  private Text notificationTitle;

  @FXML
  private TextField maximumSizeField;

  @FXML
  private Button setButton;

  @FXML
  public void initialize() {
    notificationTitle.setText(NOTIFICATION_TITLE);
    setUpOnlyNumericValuesForTextField();
  }

  @FXML
  private void handleSetAction(ActionEvent event) {
    System.out.println(getNumericValueFromTextField());
  }

  private void setUpOnlyNumericValuesForTextField() {
    maximumSizeField.textProperty().addListener((observable, oldValue, newValue) -> {
      if (!newValue.matches("\\d{0,9}")) {
        maximumSizeField.setText(oldValue);
      }
    });
  }

  private Optional<Long> getNumericValueFromTextField() {
    try {
      return Optional.of(Long.parseLong(maximumSizeField.getText()));
    } catch (NumberFormatException e) {
      new ErrorNotification("Bad input: " + maximumSizeField.getText(), "You must provide the numeric value without dot")
        .show();
      System.out.println("Error parsing int (" + maximumSizeField.getText() + ") from field." + e);
      return Optional.empty();
    }
  }
}
