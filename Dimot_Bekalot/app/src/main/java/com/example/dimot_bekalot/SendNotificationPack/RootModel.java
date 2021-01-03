
package com.example.dimot_bekalot.SendNotificationPack;

import com.google.gson.annotations.SerializedName;

/**
 * class that send on HTTP POST contains the data, the token of the receiver and the notification
 */

public class RootModel {

    @SerializedName("to") //  "to" changed to token
    private String token;

    @SerializedName("notification")
    private MyNotification notification;

    @SerializedName("data")
    private Data data;

    public RootModel(String token, MyNotification notification, Data data) {
        this.token = token;
        this.notification = notification;
        this.data = data;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public MyNotification getNotification() {
        return notification;
    }

    public void setNotification(MyNotification notification) {
        this.notification = notification;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}