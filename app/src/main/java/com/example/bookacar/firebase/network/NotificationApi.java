package com.example.bookacar.firebase.network;

import com.example.bookacar.firebase.network.response.PushNotification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface NotificationApi {

    String SERVER_KEY = "AAAAckv6JmM:APA91bGDdcJDBT4ECcV5PaA4_OP7qw17PtcDgdc9Q7Lv5BYPHuSROrh1WhrNFyq9XYsZgS5ENvXyo4lorXOWffUmPvo5Bf6lxDcNaly9YzinG8VO4wQKJV0cfJzzSU_Jmsil99lxiCf3";
    String CONTENT_TYPE = "application/json";

    @Headers({"Authorization: key=" + SERVER_KEY, "Content-Type:" + CONTENT_TYPE})
    @POST("/fcm/send")
    Call<PushNotification> postNotification(@Body PushNotification pushNotification);
}
