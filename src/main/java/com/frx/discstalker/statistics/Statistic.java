package com.frx.discstalker.statistics;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;

/**
 * Created by surjak on 19.12.2020
 */
public interface Statistic<T> {
  StringProperty getName();
  StringProperty getTextValue();
  Node getValueAsNode();
  ObjectProperty<T> getValue();
}
