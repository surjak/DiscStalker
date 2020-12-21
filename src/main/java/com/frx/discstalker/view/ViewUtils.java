package com.frx.discstalker.view;

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

    return String.format("%.1f %s", bytes / 1000.0, suffix);
  }
}
