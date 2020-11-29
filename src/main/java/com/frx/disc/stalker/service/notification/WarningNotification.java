package com.frx.disc.stalker.service.notification;

import dorkbox.notify.Pos;

public class WarningNotification extends Notification {
  public WarningNotification(String title, String text) {
    super(title, text);
  }

  public WarningNotification(String title, String text, int duration, Pos position) {
    super(title, text, duration, position);
  }

  @Override
  public void show() {
    notification.showWarning();
  }
}
