package com.globant.eventmanager;

import android.support.test.espresso.assertion.ViewAssertions;
import android.test.ActivityInstrumentationTestCase2;

import com.globant.eventscorelib.baseActivities.BaseSubscriberActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.not;

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

    public void testHideCamera(){
        onView(withId(R.id.fab)).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.card_view_contact)).perform(swipeUp());
        onView(withId(R.id.fab)).check(ViewAssertions.matches(not(isDisplayed())));
    }
}
