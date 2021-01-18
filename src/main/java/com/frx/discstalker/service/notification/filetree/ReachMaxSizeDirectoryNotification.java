package com.frx.discstalker.service.notification.filetree;

import com.frx.discstalker.service.notification.Notification;

public class ReachMaxSizeDirectoryNotification extends Notification {

  private static final String TITLE = "Directory max size";

  public ReachMaxSizeDirectoryNotification(Long reachedSize, Long maxSize) {
    super(TITLE, String.format("Current directory size is %d MB, but maximum was set to %d MB", reachedSize, maxSize));
  }

  @Override
  public void show() {
    notification.showWarning();
  }
}
