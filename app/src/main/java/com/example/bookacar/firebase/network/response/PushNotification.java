package com.example.bookacar.firebase.network.response;

public class PushNotification {

    public NotificationData data;
    public String to;

    public PushNotification(NotificationData data, String to) {
        this.data = data;
        this.to = to;
    }
}
