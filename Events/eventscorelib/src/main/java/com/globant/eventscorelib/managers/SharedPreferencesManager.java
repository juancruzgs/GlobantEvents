package com.globant.eventscorelib.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.globant.eventscorelib.R;
import com.globant.eventscorelib.utils.CoreConstants;

public class SharedPreferencesManager {

    private Context mContext;
    private SharedPreferences mSharedPreferences;


    public SharedPreferencesManager(Context ctx){
        this.mContext = ctx;
        mSharedPreferences =  mContext.getSharedPreferences("Globant", Context.MODE_PRIVATE);
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

    public void setTwitterStatusResponse(String token, String tokenSecret) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(CoreConstants.TWITTER_PREF_KEY_OAUTH_TOKEN, token);
        editor.putString(CoreConstants.TWITTER_PREF_KEY_OAUTH_SECRET,tokenSecret);
        editor.putBoolean(CoreConstants.TWITTER_IS_LOGGED_IN, true);
        editor.commit();
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


