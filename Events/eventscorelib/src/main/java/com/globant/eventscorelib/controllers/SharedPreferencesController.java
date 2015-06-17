package com.globant.eventscorelib.controllers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.globant.eventscorelib.domainObjects.Subscriber;
import com.globant.eventscorelib.utils.ConvertImage;
import com.globant.eventscorelib.utils.CoreConstants;

public class SharedPreferencesController {

    public static String getUserFirstName(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CoreConstants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(CoreConstants.PREFERENCE_USER_FIRST_NAME, null);
    }

    public static String getUserLastName(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CoreConstants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(CoreConstants.PREFERENCE_USER_LAST_NAME, null);
    }

    public static String getUserEmail(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CoreConstants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(CoreConstants.PREFERENCE_USER_EMAIL, null);
    }

    public static String getUserOccupation(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CoreConstants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(CoreConstants.PREFERENCE_USER_OCCUPATION_NAME, null);
    }

    public static String getUserPhone(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CoreConstants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(CoreConstants.PREFERENCE_USER_PHONE, null);
    }

    public static String getUserCountry(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CoreConstants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(CoreConstants.PREFERENCE_USER_COUNTRY, null);
    }

    public static String getUserCity(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CoreConstants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(CoreConstants.PREFERENCE_USER_CITY, null);
    }

    public static Boolean getUserEnglishKnowledge(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CoreConstants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(CoreConstants.PREFERENCE_USER_ENGLISH_KNOWLEDGE, false);
    }

    public static String getUserTwitter(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CoreConstants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(CoreConstants.PREFERENCE_USER_TWITTER, null);
    }

    public static void setSubscriberInformation(Subscriber subscriber, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CoreConstants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CoreConstants.PREFERENCE_USER_FIRST_NAME, subscriber.getName());
        editor.putString(CoreConstants.PREFERENCE_USER_LAST_NAME, subscriber.getLastName());
        editor.putString(CoreConstants.PREFERENCE_USER_EMAIL, subscriber.getEmail());
        editor.putString(CoreConstants.PREFERENCE_USER_OCCUPATION_NAME, subscriber.getOccupation());
        editor.putString(CoreConstants.PREFERENCE_USER_PHONE, subscriber.getPhone());
        editor.putBoolean(CoreConstants.PREFERENCE_USER_ENGLISH_KNOWLEDGE, subscriber.speaksEnglish());
        editor.putString(CoreConstants.PREFERENCE_USER_TWITTER, subscriber.getTwitterUser());
        editor.putString(CoreConstants.PREFERENCE_USER_COUNTRY, subscriber.getCountry());
        editor.putString(CoreConstants.PREFERENCE_USER_CITY, subscriber.getCity());
        byte[] image = ConvertImage.convertBitmapToByteArrayAndCompress(subscriber.getPicture());
        String encoded = Base64.encodeToString(image, Base64.DEFAULT);
        editor.putString(CoreConstants.PREFERENCE_USER_PICTURE, encoded);
        editor.putBoolean(CoreConstants.PREFERENCE_USER_IS_GLOBER, subscriber.isGlober());
        //TODO add field is public

        editor.apply();
    }

    public static void clearPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CoreConstants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public static byte[] getUserImage(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CoreConstants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        String encoded = sharedPreferences.getString(CoreConstants.PREFERENCE_USER_PICTURE, null);
        byte[] image = null;
        if (encoded != null) {
            image = Base64.decode(encoded, Base64.DEFAULT);
        }
        return image;
    }

    public static boolean isGlober(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CoreConstants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(CoreConstants.PREFERENCE_USER_IS_GLOBER, false);
    }

    public static void setTwitterStatusResponse(String token, String tokenSecret, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CoreConstants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CoreConstants.TWITTER_PREF_KEY_OAUTH_TOKEN, token);
        editor.putString(CoreConstants.TWITTER_PREF_KEY_OAUTH_SECRET, tokenSecret);
        editor.putBoolean(CoreConstants.TWITTER_IS_LOGGED_IN, true);
        editor.apply();
    }

    public static void setHintParticipantsShowed(boolean showed, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CoreConstants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(CoreConstants.PREFERENCE_PARTICIPANT_HINT_SHOWED, showed);
        editor.apply();
    }

    public static boolean isHintParticipantsShowed(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CoreConstants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(CoreConstants.PREFERENCE_PARTICIPANT_HINT_SHOWED, false);
    }

    public static String getAccessToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CoreConstants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(CoreConstants.TWITTER_PREF_KEY_OAUTH_TOKEN, null);
    }

    public static String getAccessTokenSecret(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CoreConstants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(CoreConstants.TWITTER_PREF_KEY_OAUTH_SECRET, null);
    }

    public static boolean isAlreadyTwitterLogged(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CoreConstants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(CoreConstants.TWITTER_IS_LOGGED_IN, false);
    }

    public static long getCleanChannelDate(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CoreConstants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getLong(CoreConstants.PREFERENCE_TIME, 0);
    }

    public static void setCleanChannelDate(Context context, Long time) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CoreConstants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(CoreConstants.PREFERENCE_TIME, time);
        editor.apply();
    }
}


