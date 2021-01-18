package com.frx.discstalker.service.notification.filetree;

import com.frx.discstalker.service.notification.Notification;

public class ReachMaxNumberOfFilesNotification extends Notification {
  private static final String TITLE = "Number of files";

  public ReachMaxNumberOfFilesNotification(Long numberOfFiles, Long maxNumberOfFiles) {
    super(TITLE, String.format("Current number of files is %d, but maximum was set to %d", numberOfFiles, maxNumberOfFiles));
  }

  @Override
  public void show() {
    notification.showWarning();
  }
}
