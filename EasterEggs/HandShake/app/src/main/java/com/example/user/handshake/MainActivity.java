package com.example.user.handshake;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity implements SensorEventListener {
    private final String HANDSHAKE = "detecté HandShake";
    private SensorManager sensorManager;
    private Sensor senAcelerometer;

    private long lastUpdate = 0;
    private float last_x,last_y,last_z;
    private static final int SHAKE_THRESHOLD = 2500;

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;
        if (mySensor.getType()==Sensor.TYPE_ACCELEROMETER)
        {
            checkHandShake(event);
        }

    }

    private void checkHandShake(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        long curTime  = System.currentTimeMillis();
        if(curTime-lastUpdate > 100)
        {
            long diffTime = (curTime - lastUpdate);
            lastUpdate = curTime;
            float speed = Math.abs(x + y + z - last_x - last_y - last_z)/ diffTime * 10000;
            if(speed > SHAKE_THRESHOLD)
            {
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(400);
                Toast.makeText(getBaseContext(), HANDSHAKE, Toast.LENGTH_SHORT).show();
            }

            last_x = x;
            last_y=y;
            last_z=z;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this,senAcelerometer,sensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAcelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this,senAcelerometer,sensorManager.SENSOR_DELAY_NORMAL);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
