package com.globant.eventscorelib.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.globant.eventscorelib.R;
import com.globant.eventscorelib.baseComponents.BaseFragment;
import com.globant.eventscorelib.baseComponents.BaseService;

/**
 * A simple {@link Fragment} subclass.
 */
public class SpeakerFragment extends BaseFragment {


    public SpeakerFragment() {
        // Required empty public constructor
    }




    @Override
    public BaseService.ActionListener getActionListener() {
        return null;
    }

    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_speaker, container, false);
    }

    @Override
    public String getTitle() {
        return "speaker";
    }


}
