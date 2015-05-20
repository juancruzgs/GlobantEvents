package com.globant.eventscorelib.baseComponents;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ariel.cattaneo on 06/05/2015.
 */
public class BaseEasterEgg {
    public interface EasterEggListener {
        void onEasterEgg();
    }

    protected List<EasterEggListener> mEggListeners = new ArrayList<>();

    public void subscribeListener(EasterEggListener listener) {
        mEggListeners.add(listener);
    }

    public void unsubscribeListener(EasterEggListener listener) {
        mEggListeners.remove(listener);
    }
}
