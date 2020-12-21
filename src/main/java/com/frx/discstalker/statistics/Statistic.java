package com.frx.discstalker.statistics;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by surjak on 19.12.2020
 */
public interface Statistic {
  StringProperty getName();
  ObjectProperty getValue();
}
