package com.globant.eventscorelib.utils.easterEggs;

import android.view.MotionEvent;

/**
 * Created by ariel.cattaneo on 07/05/2015.
 */
public abstract class BaseTouchEasterEgg extends BaseEasterEgg {
    /**
     * Call this method from the touch event belonging to the View or Activity you want to check the Easter Egg.
     *
     * @param event The event parameter from the touch event from the View or Activity.
     */
    abstract public void touchEvent(MotionEvent event);
}
