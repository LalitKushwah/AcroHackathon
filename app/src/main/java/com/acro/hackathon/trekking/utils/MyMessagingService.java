package com.acro.hackathon.trekking.utils;

import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import com.acro.hackathon.trekking.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import static com.google.android.gms.wearable.DataMap.TAG;

/**
 * Created by ps11 on 02/04/17.
 */

public class MyMessagingService  extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            sendNotification( remoteMessage);
        }
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param remoteMessage FCM message message received.
     */
    private void sendNotification(RemoteMessage remoteMessage) {
        Intent intent = new Intent(this, MainActivity.class);

        Map<String, String> hmap ;
        hmap = remoteMessage.getData();
        hmap.get("data_info");
        intent.putExtra("data_info", hmap.get("data_info"));
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);


        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

    }

}
