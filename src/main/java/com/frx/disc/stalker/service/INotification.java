package com.frx.disc.stalker.service;

import dorkbox.notify.Notify;

public interface INotification {
    void show();
    void showInformation();
    void showConfirm();
    void showWarning();
    void showError();
}
