package com.globant.eventmanager.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.globant.eventmanager.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class SpeakerManagerFragment extends Fragment {


    public SpeakerManagerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_speaker, container, false);
    }


}
