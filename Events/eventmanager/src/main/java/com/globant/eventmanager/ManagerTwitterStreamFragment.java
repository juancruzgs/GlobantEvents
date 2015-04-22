package com.globant.eventmanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.globant.eventscorelib.BaseTweetActivity;
import com.globant.eventscorelib.baseComponents.BaseService;
import com.globant.eventscorelib.fragments.BaseTwitterStreamFragment;


public class ManagerTwitterStreamFragment extends BaseTwitterStreamFragment {

    @Override
    protected Class<? extends BaseTweetActivity> getTweetActivityClass() {
        return TweetManagerActivity.class;
    }
}
