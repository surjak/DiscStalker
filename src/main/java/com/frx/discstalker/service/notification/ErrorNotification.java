package com.frx.discstalker.service.notification;

import dorkbox.notify.Pos;

public class ErrorNotification extends Notification {
  public ErrorNotification(String title, String text) {
    super(title, text);
  }

  public ErrorNotification(String title, String text, int duration, Pos position) {
    super(title, text, duration, position);
  }

  @Override
  public void show() {
    notification.showError();
  }
}
