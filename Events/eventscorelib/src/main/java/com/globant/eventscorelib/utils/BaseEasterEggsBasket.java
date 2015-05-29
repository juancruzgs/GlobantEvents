package com.globant.eventscorelib.utils;

import com.globant.eventscorelib.baseActivities.BaseActivity;
import com.globant.eventscorelib.baseComponents.BaseEasterEgg;

import java.lang.reflect.Constructor;
import java.util.HashMap;

/**
 * Created by ariel.cattaneo on 21/05/2015.
 */
public class BaseEasterEggsBasket {
    public enum EASTEREGGS {HANDSHAKE, TWO_FINGER_DOUBLE_TAP}

    private static HashMap<EASTEREGGS, Class<? extends BaseEasterEgg> > mEggsMap = new HashMap<>();
    static {
        mEggsMap.put(EASTEREGGS.HANDSHAKE, HandshakeEasterEgg.class);
        mEggsMap.put(EASTEREGGS.TWO_FINGER_DOUBLE_TAP, TwoFingerDoubleTapEgg.class);
    }

    public static BaseEasterEgg initEgg(EASTEREGGS egg, BaseActivity activity) {
        Constructor<? extends BaseEasterEgg> constructor = null;
        try {
            constructor = mEggsMap.get(egg).getConstructor();
        } catch (NoSuchMethodException e) {
            Logger.e("Can't get the constructor of the desired Easter Egg", e);
            return null;
        }
        BaseEasterEgg eggMade = null;
        try {
            eggMade = constructor.newInstance();
            eggMade.setActivity(activity);
        } catch (Exception e) {
            Logger.e("Can't make a new instance of the desired Easter Egg", e);
        }

        return eggMade;
    }

}
