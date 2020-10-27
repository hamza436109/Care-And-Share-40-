package com.example.careandshare;

import android.app.NotificationManager;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyMessagingService extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
showNotificaion(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());

    }



        public void showNotificaion(String title, String message){

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"MyNotifications")
                    .setContentTitle(title)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setAutoCancel(true)
                    .setContentText(message);

            NotificationManagerCompat manager = NotificationManagerCompat.from(this);
            manager.notify(999,builder.build());






        }


    }

