package com.example.dimot_bekalot.SendNotificationPack;

/**
 * This class represent the notification that the business sent to the customer
 */

public class MyNotification {

    private String title;
    private String body;

    public MyNotification(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}