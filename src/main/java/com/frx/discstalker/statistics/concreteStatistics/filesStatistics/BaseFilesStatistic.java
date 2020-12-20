package com.frx.discstalker.stats.filesByTypeStatistics;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by surjak on 19.12.2020
 */
public abstract class BaseByTypeStatistic implements StatisticByType {
  private final String STATISTIC_NAME;
  private SimpleObjectProperty content;

  public BaseByTypeStatistic(String statisticName) {
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
