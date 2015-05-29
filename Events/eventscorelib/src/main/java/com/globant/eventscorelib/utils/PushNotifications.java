package com.globant.eventscorelib.utils;

import android.content.Context;
import android.content.res.Resources;

import com.globant.eventscorelib.R;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.parse.SendCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by agustin.gugliotta on 05/05/2015.
 */
public class PushNotifications {

    public static void subscribeToChannel(final String channel){
        ParsePush.subscribeInBackground(channel, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Logger.d("Successfully subscribed to the " + channel + " channel.");
                } else {
                    Logger.e("Failed to subscribe for push", e);
                }
            }
        });
    }

    public static  void unsubscribeToChannel(final String channel){
        ParsePush.unsubscribeInBackground(channel, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Logger.d("Successfully unsubscribed to the " + channel + " channel.");
                } else {
                    Logger.e("Failed to unsubscribe for push", e);
                }
            }
        });
    }

    public static List<String> getSuscribedChannelsList(){
        return ParseInstallation.getCurrentInstallation().getList("channels");
    }

    public static void sendBroadcastNotification(Context context, String message){
        JSONObject data =  new JSONObject();
        try {
            data.put(context.getString(R.string.notification_attribute_title),
                    context.getString(R.string.notification_broadcast_title));
            data.put(context.getString(R.string.notification_attribute_alert), message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ParsePush parsePush = new ParsePush();
        parsePush.setData(data);
        parsePush.setQuery(ParseInstallation.getQuery());
        parsePush.sendInBackground(new SendCallback() {
            @Override
            public void done(ParseException e) {
                Logger.d("Notification sended");
            }
        });
    }

    public static void sendNotification(Context context, String message,String channel,String eventId){
        JSONObject data =  new JSONObject();
        ParsePush parsePush = new ParsePush();
        try {
            data.put(context.getString(R.string.notification_attribute_title),
                    context.getString(R.string.notification_event_title));
            data.put(context.getString(R.string.notification_attribute_alert), message);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (!(channel.equals(context.getString(R.string.channel_everyone)))){
            if (channel.equals(context.getString(R.string.channel_subscriber))){
                channel = context.getString(R.string.prefix_subscriber)+eventId;
            } else {
                channel = context.getString(R.string.prefix_participants)+eventId;
            }
        } else {
            channel = context.getString(R.string.prefix_channel)+eventId;
        }
        parsePush.setData(data);
        parsePush.setQuery(ParseInstallation.getQuery()
                .whereEqualTo(context.getString(R.string.query_attribute_channel), channel));
        parsePush.sendInBackground(new SendCallback() {
            @Override
            public void done(ParseException e) {
                Logger.d("Notification sended");
            }
        });
    }

    public static void sendNotificationToSubscriber(Context context, String message, String event, String idSubscriber){
        JSONObject data =  new JSONObject();
        try {
            data.put(context.getString(R.string.notification_attribute_title),
                    context.getString(R.string.subs_to_part_title));
            data.put(context.getString(R.string.notification_attribute_alert), message);
            data.put(context.getString(R.string.notification_attribute_event), event);
            data.put(context.getString(R.string.notification_attribute_id),idSubscriber);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ParsePush parsePush = new ParsePush();
        parsePush.setData(data);
        parsePush.setQuery(ParseInstallation.getQuery()
                .whereEqualTo(context.getString(R.string.query_attribute_channel),
                        context.getString(R.string.prefix_subscriber) + event + "-" + idSubscriber));
        parsePush.sendInBackground(new SendCallback() {
            @Override
            public void done(ParseException e) {
                Logger.d("Notification sended");
            }
        });
    }
}
