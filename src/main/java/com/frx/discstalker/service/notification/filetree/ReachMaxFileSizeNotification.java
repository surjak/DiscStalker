package com.frx.discstalker.service.notification.filetree;

import com.frx.discstalker.service.notification.Notification;

public class ReachMaxFileSizeNotification extends Notification {

  private static final String TITLE = "File max size";

  public ReachMaxFileSizeNotification(Long reachedSize, Long maxSize) {
    super(TITLE, String.format("Largest file size is %d MB, but maximum was set to %d MB", reachedSize, maxSize));
  }

  @Override
  public void show() {
    notification.showWarning();
  }
}
