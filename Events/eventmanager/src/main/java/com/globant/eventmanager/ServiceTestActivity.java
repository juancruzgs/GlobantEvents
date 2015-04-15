package com.globant.eventmanager;

import android.os.Bundle;

import com.globant.eventscorelib.baseComponents.BaseActivity;
import com.globant.eventscorelib.baseComponents.BaseFragment;

/**
 * Created by ariel.cattaneo on 14/04/2015.
 */
public class ServiceTestActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        BaseFragment fragment = new ServiceTestFragment();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
    }

    @Override
    protected void setServiceInternally() {
        mServiceClass = ManagerDataService.class;
    }

    @Override
    public String getActivityTitle() {
        return "Test";
    }

}
