package com.example.admin.mysvvvappbeta;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static android.content.ContentValues.TAG;

/**
 * Created by Asad Mirza on 01-01-2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "M_CH_ID");

            notificationBuilder.setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.logo_cn_new)
                    .setColor(getResources().getColor(R.color.colorPrimary))
                  //  .setLargeIcon(Bitmap.createBitmap(getResources().getDrawable(R.drawable.cn_logo)))
                    //.setTicker("Hearty365")
                    .setPriority(Notification.PRIORITY_MAX) // this is deprecated in API 26 but you can still use for below 26. check below update for 26 API
                    .setContentTitle(remoteMessage.getNotification().getTitle())

                    .setContentText(remoteMessage.getNotification().getBody());
                    //.setContentInfo("Info");

            NotificationManager notificationManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);


            if (notificationManager != null) {

                notificationManager.notify(1, notificationBuilder.build());
            }
         /*   else FirebaseDatabase.getInstance().getReference().child("notifA").push().setValue(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
            FirebaseDatabase.getInstance().getReference().child("notifA").push().setValue("Got but error"+FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

            */















          /*  if (*//* Check if data needs to be processed by long running job *//* true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
               // scheduleJob();
            } else {
                // Handle message within 10 seconds
                //handleNow();
            }*/

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }


}
