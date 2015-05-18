package com.globant.eventscorelib.utils;

import android.app.Activity;
import android.widget.Toast;

import com.globant.eventscorelib.R;
import com.globant.eventscorelib.baseActivities.BaseActivity;
import com.globant.eventscorelib.baseComponents.BaseService;
import com.globant.eventscorelib.baseFragments.BaseEventListFragment;
import com.globant.eventscorelib.domainObjects.Event;

import java.util.List;

/**
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
                mFragment.postCheckinTweet((Event) result);
                break;
            case TWEET_POST:
                mFragment.showCheckinOverlay();
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
                mFragment.hideUtilsAndShowContentOverlay();
                Toast.makeText(mActivity, mActivity.getString(R.string.checkin_error), Toast.LENGTH_SHORT).show();
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
