package com.globant.eventmanager;

import android.support.test.espresso.assertion.ViewAssertions;
import android.test.ActivityInstrumentationTestCase2;

import com.globant.eventscorelib.baseActivities.BaseSubscriberActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by juan.soler on 6/16/2015.
 */
public class SubscriberActivityTest extends ActivityInstrumentationTestCase2<BaseSubscriberActivity> {

    BaseSubscriberActivity mSubscriberActivity;

    public SubscriberActivityTest() {
        super(BaseSubscriberActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        mSubscriberActivity = getActivity();
        super.setUp();
    }

    public void testNameIsDisplayed() throws Exception {
        onView(withId(R.id.edit_text_first_name)).check(ViewAssertions.matches(isDisplayed()));
    }
}
