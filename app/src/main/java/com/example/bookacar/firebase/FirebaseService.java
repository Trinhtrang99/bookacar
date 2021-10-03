package com.example.bookacar.firebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.bookacar.MainActivity;
import com.example.bookacar.R;
import com.example.bookacar.admin.HomAd;
import com.example.bookacar.bookcar.ActivityBookCar;
import com.example.bookacar.driver.MainDriver;
import com.example.bookacar.util.Constants;
import com.example.bookacar.util.PreferenceManager;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class FirebaseService extends FirebaseMessagingService {

    private String CHANNEL_ID = "my_chanel";
    private PreferenceManager preferenceManager;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        preferenceManager = new PreferenceManager(getApplicationContext());

        Intent intent;
        if (preferenceManager.getString(Constants.KEY_TYPE_USER).equals(Constants.TYPE_DRIVER)) {
            intent = new Intent(this, MainDriver.class);
        } else if (preferenceManager.getString(Constants.KEY_TYPE_USER).equals(Constants.TYPE_ADMIN)) {
            intent = new Intent(this, HomAd.class);
        } else {
            intent = new Intent(this, ActivityBookCar.class);
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationId = new Random().nextInt();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(remoteMessage.getData().get("title"))
                .setContentText(remoteMessage.getData().get("message"))
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();

        notificationManager.notify(notificationId, notification);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel (NotificationManager notificationManager) {
        String channelName = "channelName";
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription("My channel description");
        channel.enableLights(true);
        channel.setLightColor(Color.GREEN);

        notificationManager.createNotificationChannel(channel);
    }
}
