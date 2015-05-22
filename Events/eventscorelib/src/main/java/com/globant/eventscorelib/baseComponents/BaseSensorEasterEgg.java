package com.globant.eventscorelib.baseComponents;

import android.content.Context;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.globant.eventscorelib.baseActivities.BaseActivity;

/**
 * Created by ariel.cattaneo on 07/05/2015.
 */
public abstract class BaseSensorEasterEgg extends BaseEasterEgg implements SensorEventListener {
    protected SensorManager mSensorManager = null;
    @Override
    public void subscribeListener(EasterEggListener listener) {
        super.subscribeListener(listener);
        // TODO: Start sensor
        if (mSensorManager == null) {
            mSensorManager = (SensorManager) mActivity.getSystemService(Context.SENSOR_SERVICE);
        }
    }

    @Override
    public void unsubscribeListener(EasterEggListener listener) {
        super.unsubscribeListener(listener);
        // TODO: Stop sensor
    }

}
