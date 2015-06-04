package com.globant.eventscorelib.baseActivities;

import android.os.Bundle;

import com.globant.eventscorelib.R;
import com.globant.eventscorelib.baseFragments.BaseSpeakerFragment;
import com.globant.eventscorelib.utils.easterEggs.BaseEasterEggsBasket;


public class BaseSpeakerActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speaker);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new BaseSpeakerFragment())
                    .commit();
        }
        setContentView(R.layout.activity_base_speaker);
    }

    @Override
    protected boolean usesEgg() {
        return false;
    }

    @Override
    protected BaseEasterEggsBasket.EASTEREGGS whichEgg() {
        return null;
    }
}
