package com.frx.discstalker.statistics.concreteStatistics.filesStatistics;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by surjak on 19.12.2020
 */
public abstract class BaseFilesStatistic<T> implements FilesStatistic<T> {
  private final String STATISTIC_NAME;
  private StringProperty content;

  public BaseFilesStatistic(String statisticName) {
    this.content = new SimpleStringProperty("");
    this.STATISTIC_NAME = statisticName;
  }

  @Override
  public StringProperty getName() {
    return new SimpleStringProperty(STATISTIC_NAME);
  }

  @Override
  public StringProperty getTextValue() {
    return content;
  }

  protected void setContent(String content){
    this.content.set(content);
  }
}
