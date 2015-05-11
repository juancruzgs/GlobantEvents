package com.globant.eventmanager.fragments;

import android.content.Intent;
import android.view.MenuItem;

import com.globant.eventmanager.activities.PushNotificationActivity;
import com.globant.eventscorelib.baseActivities.BaseEventDetailPagerActivity;
import com.globant.eventscorelib.baseComponents.BaseApplication;
import com.globant.eventscorelib.baseFragments.BaseEventDescriptionFragment;

/**
 * Created by juan.soler on 21/04/2015.
 */
public class EventDescriptionManagerFragment extends BaseEventDescriptionFragment {

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == com.globant.eventscorelib.R.id.action_notifications){
            Intent intentNotifications = new Intent(getActivity(), PushNotificationActivity.class);
            intentNotifications.putExtra(PushNotificationFragment.SOURCE_TAG,
                    BaseEventDetailPagerActivity.getInstance().getEvent().getObjectID());
            startActivity(intentNotifications);
            return  true;
        }
        return false;
    }
}
