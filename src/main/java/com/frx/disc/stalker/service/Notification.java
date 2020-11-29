package com.frx.disc.stalker.service;

import dorkbox.notify.Notify;
import dorkbox.notify.Pos;

// TODO: take advantage of Notify's onAction method in concrete classes such as ErrorNotification
// TODO: alter concrete classes when more specific use cases come out
public abstract class Notification implements INotification {
    private static final int DEFAULT_DURATION = 5000;
    private static final Pos DEFAULT_POSITION = Pos.BOTTOM_RIGHT;

    protected Notify notification;

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
        notification = Notify.create()
                .title(title)
                .text(text)
                .hideAfter(duration)
                .position(position)
                .darkStyle();
    }

    @Override
    public void show() {
        notification.show();
    }
}
