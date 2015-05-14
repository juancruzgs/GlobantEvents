package com.globant.eventscorelib.utils;

import android.content.Context;

import com.globant.eventscorelib.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CustomDateFormat {

    public static String getDateWithTimeZone(Date date, Context context) {
        SimpleDateFormat dateFormat = new java.text.SimpleDateFormat(context.getString(R.string.simple_date_format_with_time_zone), Locale.US);
        return  dateFormat.format(date);
    }

    public static String getDate (Date date, Context context) {
        SimpleDateFormat dateFormat = new java.text.SimpleDateFormat(context.getString(R.string.simple_date_format), Locale.US);
        return  dateFormat.format(date);
    }
}
