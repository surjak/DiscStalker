package com.frx.disc.stalker.service;

import dorkbox.notify.Notify;
import dorkbox.notify.Pos;

// TODO: take advantage of Notify's onAction method
// TODO: add support for custom notification images
public class Notification implements INotification {
    private static final int DEFAULT_DURATION = 5000;
    private static final Pos DEFAULT_POSITION = Pos.BOTTOM_RIGHT;

    Notify notification;

    private String title;
    private String text;
    private int duration = DEFAULT_DURATION;
    private Pos position = DEFAULT_POSITION;

    public Notification(String title, String text) {
        this.title = title;
        this.text = text;
        setNotification();
    }

    public Notification(String title, String text, int duration, Pos position) {
        this.title = title;
        this.text = text;
        this.duration = duration;
        this.position = position;
        setNotification();
    }

   private void setNotification() {
        Notify notification = Notify.create()
                .title(title)
                .text(text)
                .hideAfter(duration)
                .position(position)
                .darkStyle();

        this.notification = notification;
   }

   @Override
   public void show() {
        notification.show();
   }

   @Override
   public void showInformation() {
       notification.showInformation();
   }

   @Override
   public void showConfirm() {
       notification.showConfirm();
   }

   @Override
   public void showWarning() {
       notification.showWarning();
   }

   @Override
   public void showError() {
       notification.showError();
   }
}
