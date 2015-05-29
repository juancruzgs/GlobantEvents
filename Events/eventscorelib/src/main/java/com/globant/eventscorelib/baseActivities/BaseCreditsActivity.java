package com.globant.eventscorelib.baseActivities;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.globant.eventscorelib.R;
import com.globant.eventscorelib.baseFragments.BaseCreditsFragment;
import com.globant.eventscorelib.controllers.SharedPreferencesController;
import com.globant.eventscorelib.utils.BaseEasterEggsBasket;


public class BaseCreditsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);
        prepareToolbar();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new BaseCreditsFragment())
                    .commit();
        }
    }

    private void prepareToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (SharedPreferencesController.getUserFirstName(this) != null) {
            getSupportActionBar().setTitle(SharedPreferencesController.getUserFirstName(this) + " " + SharedPreferencesController.getUserLastName(this));
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
    }

    @Override
    protected boolean usesEgg() {
        return true;
    }

    @Override
    protected BaseEasterEggsBasket.EASTEREGGS whichEgg() {
        return BaseEasterEggsBasket.EASTEREGGS.TWO_FINGER_DOUBLE_TAP;
    }
}
