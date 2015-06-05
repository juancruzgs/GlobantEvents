package com.globant.eventmanager;

import android.content.Context;
import android.test.AndroidTestCase;
import android.test.InstrumentationTestCase;
import android.test.mock.MockContext;

import com.globant.eventscorelib.utils.CustomDateFormat;

import junit.framework.TestCase;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by juan.soler on 5/29/2015.
 */
public class CustomDateFormatTest extends InstrumentationTestCase {

    private Context mContext;
    private Date mDate;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mContext = getInstrumentation().getTargetContext().getApplicationContext();
        mDate = new Date();
    }

    @Override
    protected void tearDown() throws Exception {
        mContext = null;
        mDate = null;
        super.tearDown();
    }

    public void testGetCompleteDate() throws Exception{
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2014);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 10);
        calendar.set(Calendar.SECOND, 0);
        mDate.setTime(calendar.getTimeInMillis());

        String customDate = CustomDateFormat.getCompleteDate(mDate, mContext);
        assertEquals("Jan, 01 2014 10:10 UTC", customDate);
    }

    public void testGetDate(){
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2014);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 4);
        calendar.set(Calendar.MINUTE, 10);
        calendar.set(Calendar.SECOND, 0);
        mDate.setTime(calendar.getTimeInMillis());

        String customDate = CustomDateFormat.getDate(mDate, mContext);
        assertEquals("Jan, 01 2014\n04:10 UTC", customDate);
    }
}