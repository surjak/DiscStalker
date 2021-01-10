package com.frx.discstalker.statistics.concreteStatistics.directoryStatistics;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by surjak on 20.12.2020
 */
public abstract class BaseDirectoryStatistics implements DirectoryStatistics {

  private final String STATISTIC_NAME;
  protected SimpleObjectProperty content;

  public BaseDirectoryStatistics(String statisticName) {
    this.content = new SimpleObjectProperty("");
    this.STATISTIC_NAME = statisticName;
  }

  @Override
  public StringProperty getName() {
    return new SimpleStringProperty(STATISTIC_NAME);
  }

  @Override
  public ObjectProperty getValue() {
    return content;
  }

  protected void setContent(String content){
    this.content.set(content);
  }
}
