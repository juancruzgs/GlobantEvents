package com.globant.eventscorelib.utils;

import com.globant.eventscorelib.R;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.parse.SendCallback;

import java.util.HashMap;
import java.util.List;

/**
 * Created by agustin.gugliotta on 05/05/2015.
 */
public class PushNotifications {

    public static void suscribeToChannel(final String channel){
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

    public static  void unsuscribeToChannel(final String channel){
        ParsePush.unsubscribeInBackground("Giants", new SaveCallback() {
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

    public static void sendNotification(String message){
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
}
