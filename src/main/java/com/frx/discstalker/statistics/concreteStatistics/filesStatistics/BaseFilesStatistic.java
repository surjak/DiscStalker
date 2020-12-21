package com.frx.discstalker.statistics.concreteStatistics.filesStatistics;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by surjak on 19.12.2020
 */
public abstract class BaseFilesStatistic implements FilesStatistic {
  private final String STATISTIC_NAME;
  private SimpleObjectProperty content;

  public BaseFilesStatistic(String statisticName) {
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
