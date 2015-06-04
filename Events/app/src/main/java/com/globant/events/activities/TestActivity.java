package com.globant.events.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.globant.events.fragments.EventListClientFragment;
import com.globant.events.R;
import com.globant.eventscorelib.baseActivities.BaseActivity;
import com.globant.eventscorelib.baseFragments.BaseFragment;
import com.globant.eventscorelib.baseComponents.BaseService;
import com.globant.eventscorelib.utils.easterEggs.BaseEasterEggsBasket;


public class TestActivity extends BaseActivity {

    @Override
    protected boolean usesEgg() {
        return false;
    }

    @Override
    protected BaseEasterEggsBasket.EASTEREGGS whichEgg() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new EventListClientFragment())
                       .commit();
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends BaseFragment {

        public PlaceholderFragment() {
        }

        @Override
        public BaseService.ActionListener getActionListener() {
            return null;
        }

        @Override
        public View onCreateEventView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_test, container, false);
            hideUtilsAndShowContentOverlay();
            return rootView;
        }
        @Override
        public String getTitle(){ return "Fragment";
        }
    }
}
