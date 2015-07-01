package com.globant.eventscorelib.utils;

import android.app.Activity;
import android.widget.Toast;

import com.globant.eventscorelib.R;
import com.globant.eventscorelib.baseActivities.BaseActivity;
import com.globant.eventscorelib.baseComponents.BaseService;
import com.globant.eventscorelib.baseExceptions.CheckinException;
import com.globant.eventscorelib.baseFragments.BaseEventListFragment;
import com.globant.eventscorelib.controllers.SharedPreferencesController;
import com.globant.eventscorelib.domainObjects.Event;

import java.util.List;

/**
 * Service action listener. Separated to keep it available when the parent fragment is destroyed.
 *
 * Created by ariel.cattaneo on 14/05/2015.
 */
public class BaseEventListActionListener implements BaseService.ActionListener {

    private BaseActivity mActivity;
    private String mBindingKey;
    private BaseEventListFragment mFragment;

    public void setFragment(BaseEventListFragment fragment) {
        mFragment = fragment;
    }

    public void setActivity(BaseActivity activity) {
        mActivity = activity;
    }

    @Override
    public Activity getBindingActivity() {
        return mActivity;
    }

    @Override
    public String getBindingKey() {
        return mBindingKey;
    }

    public void setBindingKey(String bindingKey) {
        this.mBindingKey = bindingKey;
    }

    @Override
    public void onStartAction(BaseService.ACTIONS theAction) {
    }

    @Override
    public void onFinishAction(BaseService.ACTIONS theAction, Object result) {
        switch (theAction) {
            case EVENT_LIST:
            case EVENTS_LIST_REFRESH:
                mFragment.updateEventList((List<Event>) result);
                break;
            case SUBSCRIBER_CHECKIN:
                if (result != null) {
                    mFragment.postCheckinTweet((Event) result);
                }
                else {
                    Toast.makeText(mActivity, R.string.checkin_error_qr_code_no_event, Toast.LENGTH_SHORT).show();
                }
                break;
            case TWEET_POST:
                mFragment.showCheckinOverlay();
                break;
            case UPDATE_EVENT_IN_CALENDAR:
                BaseService.CalendarResponse response = (BaseService.CalendarResponse) result;
                if (response.getRows() != -1) {
                    SharedPreferencesController.updateEventJsonInfo(mActivity, response.getEvent());
                }
                else {
                    SharedPreferencesController.removeEventJsonInfo(mActivity, response.getEvent());
                }
                break;
            default:
                mFragment.hideUtilsAndShowContentOverlay();
                break;
        }
    }

    @Override
    public void onFailAction(BaseService.ACTIONS theAction, Exception e) {
        switch (theAction) {
            case SUBSCRIBER_CHECKIN:
                if (e instanceof CheckinException) {
                    mFragment.hideUtilsAndShowContentOverlay();
                    //Toast.makeText(mActivity, mActivity.getString(R.string.checkin_error), Toast.LENGTH_SHORT).show();
                    if (((CheckinException)e).getExceptionCode() == CheckinException.SUBSCRIBER_NOT_SUBSCRIBED) {
                        Toast.makeText(mActivity, R.string.checkin_error_subscriber_not_subscribed,
                                Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(mActivity, R.string.checkin_error_subscriber_not_accepted,
                                Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    mFragment.showErrorOverlay();
                    Logger.e("Exception during checkin", e);
                }
                break;
            case EVENT_LIST:
            case EVENTS_LIST_REFRESH:
                mFragment.updateEventListFail();
                break;
            default:
                mFragment.showErrorOverlay();
                break;
        }
    }

}
