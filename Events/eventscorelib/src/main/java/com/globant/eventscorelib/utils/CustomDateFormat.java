package com.globant.eventscorelib.utils;

import android.content.Context;

import com.globant.eventscorelib.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CustomDateFormat {

    public static String getCompleteDate(Date date, Context context) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(context.getString(R.string.simple_date_format_complete_date), Locale.US);
        return  dateFormat.format(date);
    }

    public static String getDate(Date date, Context context) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(context.getString(R.string.simple_date_format_date), Locale.US);
        return  dateFormat.format(date) + "\n" + getHour(date, context);
    }

    private static String getHour (Date date, Context context) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(context.getString(R.string.simple_date_format_hour), Locale.US);
        return  dateFormat.format(date);
    }

}
