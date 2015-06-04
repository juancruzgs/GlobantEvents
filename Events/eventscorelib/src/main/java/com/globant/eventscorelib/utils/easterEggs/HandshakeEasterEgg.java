package com.globant.eventscorelib.utils.easterEggs;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.widget.Toast;

import com.globant.eventscorelib.utils.easterEggs.BaseSensorEasterEgg;

/**
 * Created by ariel.cattaneo on 07/05/2015.
 */
public class HandshakeEasterEgg extends BaseSensorEasterEgg {
    private Sensor senAcelerometer = null;

    private long lastUpdate = 0;
    private float last_x,last_y,last_z;
    private static int mNShakesLimit = 5;
    private static final int SHAKE_THRESHOLD = 2500;
    private static final int ONE_SHAKE_TIME_MILLIS = 80;
    private static final String HANDSHAKE_MESSAGE = "Glober detected";
    private int mShakes = 0;
    private boolean mGloberDetected = false;

    @Override
    public void subscribeListener(EasterEggListener listener) {
        super.subscribeListener(listener);

        if (mSensorManager != null && senAcelerometer == null) {
            senAcelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            mSensorManager.registerListener(this, senAcelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void unsubscribeListener(EasterEggListener listener) {
        super.unsubscribeListener(listener);

        if (mSensorManager != null) {
            mSensorManager.unregisterListener(this);
        }
    }

    public void uninit() {
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;
        if (mySensor.getType()==Sensor.TYPE_ACCELEROMETER)
        {
            checkHandShake(event);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void setOneSideShakes(int shakesNumber) {
        mNShakesLimit = 2 * shakesNumber + 1;
    }

    public void setBothDirectionsShakes(int shakesNumber) {
        mNShakesLimit = shakesNumber;
    }

    private void checkHandShake(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        long curTime  = System.currentTimeMillis();
        if(curTime-lastUpdate > ONE_SHAKE_TIME_MILLIS)
        {
            long diffTime = (curTime - lastUpdate);
            lastUpdate = curTime;
            float speed = Math.abs(x + y + z - last_x - last_y - last_z)/ diffTime * 10000;
            if(speed > SHAKE_THRESHOLD)
            {
                mShakes++;
                // 5 shakes: 3 forward with 2 backward
                if (mShakes >= mNShakesLimit) {
                    // TODO: Use this to identify the owner as glober
                    // NOTE: Here lies a pseudo bug: if you shake, exit, back and shake again, getActivity() returns null.
                    // NOTE2: Perhaps that pseudo-bug is fixed now.
/*
                    Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(400);
*/
                    mGloberDetected = true;
                    Toast.makeText(mActivity, HANDSHAKE_MESSAGE, Toast.LENGTH_SHORT).show();
                    // TODO: This needs to be managed from the listener (when it unsubscribes), but let's let it here for testing
                    mSensorManager.unregisterListener(this);
                    triggerEgg();
                }
            }
            else {
                mShakes = 0;
            }

            last_x = x;
            last_y=y;
            last_z=z;
        }
    }
}
