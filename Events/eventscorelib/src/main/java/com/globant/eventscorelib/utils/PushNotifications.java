package com.globant.eventscorelib.utils;

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

    public static void sendBroadcastNotification(String message){
        ParsePush parsePush = new ParsePush();
        parsePush.setMessage(message);
        parsePush.setQuery(ParseInstallation.getQuery());
        parsePush.sendInBackground(new SendCallback() {
            @Override
            public void done(ParseException e) {
                Logger.d("Notification sended");
            }
        });
    }

    public static void sendNotification(String message,String channel,String eventId){
        ParsePush parsePush = new ParsePush();
        parsePush.setMessage(message);
        //TODO change hardcoded string
        if (!(channel.equals("Everyone"))){
            if (channel.equals("Subscribers")){
                channel = "SUB-"+eventId;
            } else {
                channel = "PAR-"+eventId;
            }
            parsePush.setChannel(channel);
        }
        parsePush.setQuery(ParseInstallation.getQuery());
        parsePush.sendInBackground(new SendCallback() {
            @Override
            public void done(ParseException e) {
                Logger.d("Notification sended");
            }
        });
    }

    public static void sendNotificationToSubscriber(String message, String event, String idSubscriber){
        JSONObject data =  new JSONObject();
        try {
            //TODO change hardcoded string
            data.put("title", "Hello Participant!");
            data.put("alert", message);
            data.put("event", event);
            data.put("id",idSubscriber);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ParsePush parsePush = new ParsePush();
        parsePush.setChannel("SUB-"+event+"-"+idSubscriber);
        parsePush.setData(data);
        parsePush.setQuery(ParseInstallation.getQuery());
        parsePush.sendInBackground(new SendCallback() {
            @Override
            public void done(ParseException e) {
                Logger.d("Notification sended");
            }
        });
    }
}
