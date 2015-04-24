package com.globant.events;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.globant.eventscorelib.BaseTweetActivity;
import com.globant.eventscorelib.baseComponents.BaseService;
import com.globant.eventscorelib.fragments.BaseTwitterStreamFragment;

public class ClientStreamTwitterFragment extends BaseTwitterStreamFragment {

    @Override
    public BaseService.ActionListener getActionListener() {
        return null;
    }

    @Override
    protected Class<? extends BaseTweetActivity> getTweetActivityClass() {
        return TweetClientActivity.class;
    }

    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateEventView(inflater, container, savedInstanceState);
    }
}
