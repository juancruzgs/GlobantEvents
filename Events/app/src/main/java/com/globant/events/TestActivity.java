package com.globant.events;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.globant.eventscorelib.baseComponents.BaseActivity;
import com.globant.eventscorelib.baseComponents.BaseFragment;
import com.globant.eventscorelib.baseComponents.BaseService;


public class TestActivity extends BaseActivity {

    @Override
    protected Class<? extends BaseService> getServiceClass() {
        return ClientDataService.class;
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
