package com.globant.events.components;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.globant.events.activities.EventListClientActivity;
import com.globant.eventscorelib.utils.PushNotifications;
import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

public class PushReceiver extends ParsePushBroadcastReceiver {
    private static final String TAG = "PushReceiver";
    private static final int NOTIFICATION_ID = 1;
    public static int numMessages = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        String message = extras != null ? extras.getString("com.parse.Data") : "";
        try {
            generateNotification(context, new JSONObject(message));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void generateNotification(Context context,JSONObject json) throws JSONException {
        Intent intent = new Intent(context, EventListClientActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, 0);

        numMessages = 0;
        NotificationManager notifM = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = null;
        mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(com.globant.eventscorelib.R.mipmap.ic_launcher)
                .setContentTitle(json.getString("title"))
                .setContentText(json.getString("alert"))
                .setAutoCancel(true)
                .setNumber(++numMessages);
        mBuilder.setContentIntent(contentIntent);

        notifM.notify(NOTIFICATION_ID, mBuilder.build());

        PushNotifications.unsubscribeToChannel("SUB-" + json.getString("event"));
        PushNotifications.unsubscribeToChannel("SUB-" + json.getString("event") + "-" + json.getString("id"));
        PushNotifications.subscribeToChannel("PAR-" + json.getString("event"));
    }
}
