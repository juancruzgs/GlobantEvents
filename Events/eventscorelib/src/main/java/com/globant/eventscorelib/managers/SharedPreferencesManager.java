package com.globant.eventscorelib.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Base64;

import com.globant.eventscorelib.R;
import com.globant.eventscorelib.utils.CoreConstants;

public class SharedPreferencesManager {

    private Context mContext;
    private SharedPreferences mSharedPreferences;


    public SharedPreferencesManager(Context ctx) {
        this.mContext = ctx;
        mSharedPreferences = mContext.getSharedPreferences("Globant", Context.MODE_PRIVATE);
    }


    public static String getUserFirstName(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(
                context.getString(R.string.preference_user_first_name), null);
    }

    public static String getUserLastName(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(
                context.getString(R.string.preference_user_last_name), null);

    }

    public static String getUserEmail(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(
                context.getString(R.string.preference_user_email), null);
    }

    public static String getUserOccupation(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(
                context.getString(R.string.preference_user_occupation_name), null);
    }

    public static String getUserPhone(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(
                context.getString(R.string.preference_user_phone), null);
    }

    public static String getUserCountry(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(
                context.getString(R.string.preference_user_country), null);
    }

    public static String getUserCity(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(
                context.getString(R.string.preference_user_city), null);
    }

    public static Boolean getUserEnglishKnowledge(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return Boolean.parseBoolean(sharedPreferences.getString(
                context.getString(R.string.preference_user_english_knowledge), context.getResources().getStringArray(R.array.english_knowledge_values)[0]));


    }

    public static String getUserTwitter(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(
                context.getString(R.string.preference_user_twitter), null);
    }


    public static void setUserTwitter(String value, Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.preference_user_twitter), value);
        editor.commit();
    }

    public static void setUserFirstName(String value, Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.preference_user_first_name), value);
        editor.commit();
    }

    public static void setUserLastName(String value, Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.preference_user_last_name), value);
        editor.commit();
    }

    public static void setUserEmail(String value, Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.preference_user_email), value);
        editor.commit();
    }

    public static void setUserOccupation(String value, Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.preference_user_occupation_name), value);
        editor.commit();
    }

    public static void setUserPhone(String value, Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.preference_user_phone), value);
        editor.commit();
    }

    public static void setUserEnglishKnowledge(Boolean value, Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.preference_user_english_knowledge), value.toString());
        editor.commit();
    }

    public static void setUserCountry(String value, Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.preference_user_country), value);
        editor.commit();
    }

    public static void setUserCity(String value, Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.preference_user_city), value);
        editor.commit();
    }

    public static void clearPreferences(Context context) {
        SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.commit();
    }

    public static void setUserImage(byte[] image, Context context) {                     //Converts Bytearray  into String
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String encoded = Base64.encodeToString(image, Base64.DEFAULT);
        editor.putString(context.getString(R.string.preference_user_picture), encoded);
        editor.commit();
    }

    public static byte[] getUserImage(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String encoded = sharedPreferences.getString(context.getString(R.string.preference_user_picture), null);
        byte[] image = Base64.decode(encoded, Base64.DEFAULT);
        return image;
    }


    public void setTwitterStatusResponse(String token, String tokenSecret) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(CoreConstants.TWITTER_PREF_KEY_OAUTH_TOKEN, token);
        editor.putString(CoreConstants.TWITTER_PREF_KEY_OAUTH_SECRET, tokenSecret);
        editor.putBoolean(CoreConstants.TWITTER_IS_LOGGED_IN, true);
        editor.apply();
    }

    public String getAccessToken() {
        return mSharedPreferences.getString(CoreConstants.TWITTER_PREF_KEY_OAUTH_TOKEN, null);
    }

    public String getAccessTokenSecret() {
        return mSharedPreferences.getString(CoreConstants.TWITTER_PREF_KEY_OAUTH_SECRET, null);
    }

    public boolean isAlreadyTwitterLogged() {
        return mSharedPreferences.getBoolean(CoreConstants.TWITTER_IS_LOGGED_IN, false);
    }


}


