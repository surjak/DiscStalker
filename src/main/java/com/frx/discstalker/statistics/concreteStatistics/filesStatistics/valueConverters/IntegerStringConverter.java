package com.frx.discstalker.statistics.concreteStatistics.filesStatistics.valueConverters;

import javafx.util.StringConverter;

/**
 * Created by surjak on 11.01.2021
 */
public class IntegerStringConverter extends StringConverter<Number> {

  public IntegerStringConverter() {
  }

  @Override
  public String toString(Number object) {
    if(object.intValue()!=object.doubleValue())
      return "";
    return ""+(object.intValue());
  }

  @Override
  public Number fromString(String string) {
    Number val = Double.parseDouble(string);
    return val.intValue();
  }
}
