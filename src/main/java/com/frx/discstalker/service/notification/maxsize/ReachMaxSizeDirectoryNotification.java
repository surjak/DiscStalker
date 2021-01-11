package com.frx.discstalker.service.notification.maxsize;

import com.frx.discstalker.service.notification.Notification;

public class ReachMaxSizeDirectoryNotification extends Notification {

  private static final String TITLE = "The size of directory has reached max size";

  public ReachMaxSizeDirectoryNotification(Long reachedSize, Long maxSize) {
    super(TITLE, String.format("Current size of you directory is %d, but maximum was set to %d", reachedSize, maxSize));
  }

  @Override
  public void show() {
    notification.showWarning();
  }
}
