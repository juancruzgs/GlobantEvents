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

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetCompleteDate() throws Exception{
        Context context = getInstrumentation().getTargetContext().getApplicationContext();
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2014);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 10);
        calendar.set(Calendar.SECOND, 0);
        date.setTime(calendar.getTimeInMillis());

        String customDate = CustomDateFormat.getCompleteDate(date, context);
        assertEquals("Jan, 01 2014 10:10 UTC", customDate);
    }
}
