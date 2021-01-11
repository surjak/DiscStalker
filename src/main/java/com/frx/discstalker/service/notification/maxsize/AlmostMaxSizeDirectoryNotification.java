package com.frx.discstalker.service.notification.maxsize;

import com.frx.discstalker.service.notification.Notification;

public class AlmostMaxSizeDirectoryNotification extends Notification {

  public static final int ALMOST_MAX_SIZE = 75;
  private static final String TITLE = "Attention! You're close to reach the max size of directory";

  public AlmostMaxSizeDirectoryNotification(Long reachedSize, Long maxSize) {
    super(TITLE, String.format("You filled directory with %d %% of space! Current size of you directory is %d, but maximum was set to %d",
      ALMOST_MAX_SIZE, reachedSize, maxSize));
  }

  @Override
  public void show() {
    notification.showWarning();
  }
}
