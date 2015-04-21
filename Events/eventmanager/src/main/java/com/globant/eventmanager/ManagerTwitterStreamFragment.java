package com.globant.eventmanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.globant.eventscorelib.baseComponents.BaseService;
import com.globant.eventscorelib.fragments.BaseTwitterStreamFragment;


public class ManagerTwitterStreamFragment extends BaseTwitterStreamFragment {

    @Override
    public BaseService.ActionListener getActionListener() {
        return null;
    }

    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateEventView(inflater, container, savedInstanceState);
    }
}
