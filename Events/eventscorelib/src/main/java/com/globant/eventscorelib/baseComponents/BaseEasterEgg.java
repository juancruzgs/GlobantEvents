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

    protected List<EasterEggListener> eggListeners = new ArrayList<>();

    public void subscribeListener(EasterEggListener listener) {
        eggListeners.add(listener);
    }

    public void unsubscribeListener(EasterEggListener listener) {
        eggListeners.remove(listener);
    }
}
