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
public class CreditsFragment extends BaseFragment {

    public CreditsFragment() {
        // Required empty public constructor
    }

    @Override
    public BaseService.ActionListener getActionListener() {
        return null;
    }

    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_credits, container, false);
        hideUtilsAndShowContentOverlay();
        return rootView;
    }

    @Override
    public String getTitle() {
        return " ";
    }
}