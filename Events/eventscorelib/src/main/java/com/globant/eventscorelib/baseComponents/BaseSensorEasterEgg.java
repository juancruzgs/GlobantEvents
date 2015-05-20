package com.globant.eventscorelib.baseComponents;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.globant.eventscorelib.baseActivities.BaseActivity;

/**
 * Created by ariel.cattaneo on 07/05/2015.
 */
public abstract class BaseSensorEasterEgg extends BaseEasterEgg implements SensorEventListener {
    protected BaseActivity mActivity;
    protected SensorManager sensorManager = null;

    public void setActivity(BaseActivity activity) {
        mActivity = activity;
    }

    @Override
    public void subscribeListener(EasterEggListener listener) {
        super.subscribeListener(listener);
        // TODO: Start sensor
        if (sensorManager == null) {
            sensorManager = (SensorManager) mActivity.getSystemService(Context.SENSOR_SERVICE);
        }
    }

    @Override
    public void unsubscribeListener(EasterEggListener listener) {
        super.unsubscribeListener(listener);
        // TODO: Stop sensor
    }

}
