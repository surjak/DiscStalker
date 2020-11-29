package com.frx.disc.stalker.service.notification;

import dorkbox.notify.Pos;

public class ConfirmNotification extends Notification {
  public ConfirmNotification(String title, String text) {
    super(title, text);
  }

  public ConfirmNotification(String title, String text, int duration, Pos position) {
    super(title, text, duration, position);
  }

  @Override
  public void show() {
    notification.showConfirm();
  }
}
