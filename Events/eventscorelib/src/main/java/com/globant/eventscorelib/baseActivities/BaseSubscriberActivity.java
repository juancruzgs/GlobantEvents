package com.globant.eventscorelib.baseActivities;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;

import com.globant.eventscorelib.R;
import com.globant.eventscorelib.baseFragments.BaseSubscriberFragment;
import com.globant.eventscorelib.controllers.SharedPreferencesController;
import com.globant.eventscorelib.utils.BaseEasterEggsBasket;

public class BaseSubscriberActivity extends BaseActivity {

    BaseSubscriberFragment mBaseSubscriberFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscriber);
        getSupportActionBar().hide();
        if (savedInstanceState == null) {
            mBaseSubscriberFragment = new BaseSubscriberFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, mBaseSubscriberFragment)
                    .commit();
        }
        if (savedInstanceState != null) {
            //Restore the fragment's instance
             mBaseSubscriberFragment= (BaseSubscriberFragment)getSupportFragmentManager().getFragment(
                    savedInstanceState, "mContent");

        }


    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mBaseSubscriberFragment.tintAllGrey();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "mContent", mBaseSubscriberFragment);

    }

    @Override
    protected boolean usesEgg() {
        return true;
    }
    @Override
    protected BaseEasterEggsBasket.EASTEREGGS whichEgg() {
        return BaseEasterEggsBasket.EASTEREGGS.HANDSHAKE;
    }
}


