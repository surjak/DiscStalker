package com.frx.disc.stalker.service.notification;

import dorkbox.notify.Pos;
import dorkbox.util.ImageUtil;
import dorkbox.util.LocationResolver;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.InputStream;

public class CustomNotification extends Notification {
    public CustomNotification(String title, String text, String imagePath) {
        super(title, text);
        setImage(imagePath);
    }

    public CustomNotification(String title, String text, int duration, Pos position, String imagePath) {
        super(title, text, duration, position);
        setImage(imagePath);
    }

    private void setImage(String imagePath) {
        try {
            InputStream resourceAsStream = LocationResolver.getResourceAsStream(imagePath);
            Image image = ImageUtil.getImageImmediate(ImageIO.read(resourceAsStream))
                .getScaledInstance(48, 48, Image.SCALE_SMOOTH);

            addImageToNotification(image);
        } catch (Exception e) {
            System.err.println("Could not read the image from " + imagePath);
        }
    }

    private void addImageToNotification(Image image) {
        notification.image(image);
    }
}
