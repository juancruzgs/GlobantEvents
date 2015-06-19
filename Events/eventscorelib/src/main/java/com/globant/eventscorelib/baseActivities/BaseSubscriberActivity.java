package com.globant.eventscorelib.baseActivities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.globant.eventscorelib.R;
import com.globant.eventscorelib.baseFragments.BaseSubscriberFragment;
import com.globant.eventscorelib.controllers.SharedPreferencesController;
import com.globant.eventscorelib.utils.easterEggs.BaseEasterEggsBasket;

public class BaseSubscriberActivity extends BaseActivity {

//    BaseSubscriberFragment mBaseSubscriberFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscriber);
        prepareToolbar();
        if (savedInstanceState == null) {
//            mBaseSubscriberFragment = new BaseSubscriberFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new BaseSubscriberFragment())
                    .commit();
//        } else {
//            //Restore the fragment's instance
//             mBaseSubscriberFragment= (BaseSubscriberFragment)getSupportFragmentManager().getFragment(
//                    savedInstanceState, "mContent");
//        }
        }
    }

    private void prepareToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (SharedPreferencesController.getUserFirstName(this) != null) {
            getSupportActionBar().setTitle(SharedPreferencesController.getUserFirstName(this) + " " + SharedPreferencesController.getUserLastName(this));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        mBaseSubscriberFragment.tintAllIconsGrey();
        finish();
        overridePendingTransition(R.anim.nothing, R.anim.top_out);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        getSupportFragmentManager().putFragment(outState, "mContent", mBaseSubscriberFragment);
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