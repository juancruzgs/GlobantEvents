package com.globant.eventscorelib.baseComponents;

import android.view.MotionEvent;
import android.view.View;

/**
 * Created by ariel.cattaneo on 07/05/2015.
 */
public abstract class BaseTouchListenerEasterEgg extends BaseEasterEgg implements View.OnTouchListener {
    protected View mTouchView;

    public void setTouchView(View touchView) {
        mTouchView = touchView;

        mTouchView.setOnTouchListener(this);
    }

    @Override
    public void uninit() {
        mTouchView.setOnTouchListener(null);
    }
}
