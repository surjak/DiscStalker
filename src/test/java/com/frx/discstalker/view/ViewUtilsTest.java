package com.frx.discstalker.view;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ViewUtilsTest {
  @Test
  public void givenSizeUnder1000BytesReturnsIntegerString() {
    assertThat(ViewUtils.formatSize(0L)).isEqualTo("0 B");
    assertThat(ViewUtils.formatSize(1L)).isEqualTo("1 B");
    assertThat(ViewUtils.formatSize(500L)).isEqualTo("500 B");
    assertThat(ViewUtils.formatSize(999L)).isEqualTo("999 B");
  }

  @Test
  public void givenSizeOver1000BytesReturnsAppropriateSuffix() {
    assertThat(ViewUtils.formatSize(1000L)).isEqualTo("1.0 kB");
    assertThat(ViewUtils.formatSize(999000L)).isEqualTo("999.0 kB");
    assertThat(ViewUtils.formatSize(1000000L)).isEqualTo("1.0 MB");
    assertThat(ViewUtils.formatSize(1000000000L)).isEqualTo("1.0 GB");
    assertThat(ViewUtils.formatSize(1000000000000L)).isEqualTo("1.0 TB");
  }

  @Test
  public void givenSizeOver1000BytesRoundsSizeToOneDecimalPlace() {
    assertThat(ViewUtils.formatSize(1549L)).isEqualTo("1.5 kB");
    assertThat(ViewUtils.formatSize(1550L)).isEqualTo("1.6 kB");

    assertThat(ViewUtils.formatSize(999949L)).isEqualTo("999.9 kB");
    assertThat(ViewUtils.formatSize(999950L)).isEqualTo("1.0 MB");
  }
}
