package com.globant.eventscorelib.utils.easterEggs;

import com.globant.eventscorelib.baseActivities.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ariel.cattaneo on 06/05/2015.
 */
public abstract class BaseEasterEgg {
    public interface EasterEggListener {
        void onEasterEgg();
    }

    protected BaseActivity mActivity;

    public void setActivity(BaseActivity activity) {
        mActivity = activity;
    }

    protected List<EasterEggListener> mEggListeners = new ArrayList<>();

    public void subscribeListener(EasterEggListener listener) {
        mEggListeners.add(listener);
    }

    public void unsubscribeListener(EasterEggListener listener) {
        mEggListeners.remove(listener);
    }

    public abstract void uninit();

    protected void triggerEgg() {
        for (EasterEggListener eggListener : mEggListeners) {
            eggListener.onEasterEgg();
        }
    }
}
