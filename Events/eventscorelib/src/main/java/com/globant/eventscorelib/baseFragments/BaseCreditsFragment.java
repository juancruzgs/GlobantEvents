package com.globant.eventscorelib.baseFragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.globant.eventscorelib.R;
import com.globant.eventscorelib.baseComponents.BaseEasterEgg;
import com.globant.eventscorelib.baseComponents.BaseService;

/**
 * A simple {@link Fragment} subclass.
 */
public class BaseCreditsFragment extends BaseFragment implements BaseEasterEgg.EasterEggListener {

    public BaseCreditsFragment() {
        // Required empty public constructor
    }

    @Override
    public BaseService.ActionListener getActionListener() {
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        subscribeEggListener(this);
    }

    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_credits, container, false);
        hideUtilsAndShowContentOverlay();
        rootView.findViewById(R.id.text_view_ready).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                passEventToEgg(event);
                return true;
            }
        });
        return rootView;
    }

    @Override
    public String getTitle() {
        return " ";
    }

    @Override
    public void onEasterEgg() {
        Toast.makeText(getActivity(), "Egging", Toast.LENGTH_SHORT).show();
    }
}
