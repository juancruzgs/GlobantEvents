package com.example.user.myapplication;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;


/**
 * The main activity for the Jumping Jack application. This activity registers itself to receive
 * sensor values. Since on wearable devices a full screen activity is very short-lived, we set the
 * FLAG_KEEP_SCREEN_ON to give user adequate time for taking actions but since we don't want to
 * keep screen on for an extended period of time, there is a SCREEN_ON_TIMEOUT_MS that is enforced
 * if no interaction is discovered.
 *
 * This activity includes a {@link android.support.v4.view.ViewPager} with two pages, one that
 * shows the current count and one that allows user to reset the counter. the current value of the
 * counter is persisted so that upon re-launch, the counter picks up from the last value. At any
 * stage, user can set this counter to 0.
 */
public class MainActivity extends Activity
        implements SensorEventListener {

    final String[] Phrases = {"Cada hombre quiere ser el primer amor de una mujer, cada mujer quiere ser el último amor de un hombre.",
    "Hay estudiantes que les apena ir al hipódromo y ver que hasta los caballos logran terminar su carrera.",
    "Tener la conciencia limpia, solo es síntoma de mala memoria.",
    "Algunos matrimonios acaban bien, otros duran toda la vida.",
    "Se bueno con tus hijos. Ellos elegirán tu residencia.",
    "Solo los genios somos modestos.",
    "Es mejor callar y que piensen que eres idiota a hablar y demostrarlo."};
    private static final String TAG = "JJMainActivity";

    /** How long to keep the screen on when no activity is happening **/
    private static final long SCREEN_ON_TIMEOUT_MS = 20000; // in milliseconds

    /** an up-down movement that takes more than this will not be registered as such **/
    private static final long TIME_THRESHOLD_NS = 2000000000; // in nanoseconds (= 2sec)

    /**
     * Earth gravity is around 9.8 m/s^2 but user may not completely direct his/her hand vertical
     * during the exercise so we leave some room. Basically if the x-component of gravity, as
     * measured by the Gravity sensor, changes with a variation (delta) > GRAVITY_THRESHOLD,
     * we consider that a successful count.
     */
    private static final float GRAVITY_THRESHOLD = 7.0f;

    private SensorManager mSensorManager;
    private Sensor mSensor;
    private long mLastTime = 0;
    private boolean mUp = false;
    private int mJumpCounter = 0;
    private ImageView mSecondIndicator;
    private ImageView mFirstIndicator;
    private Timer mTimer;
    private TextView textView,textView2;
    private Handler mHandler;
    private Vibrator vibrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jj_layout);
        setupViews();

        vibrar = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//        mHandler = new Handler();
        mJumpCounter = 0;Utils.getCounterFromPreference(this);
       // getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        renewTimer();
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    private void setupViews() {
        textView = (TextView)findViewById(R.id.textView);
        textView2 = (TextView)findViewById(R.id.textView2);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mSensorManager.registerListener(this, mSensor,
                SensorManager.SENSOR_DELAY_NORMAL)) {
            if (Log.isLoggable(TAG, Log.DEBUG))
                Log.d(TAG, "Successfully registered for the sensor updates");

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
        Toast.makeText(this, "unregisterListener",Toast.LENGTH_SHORT).show();
        if (Log.isLoggable(TAG, Log.DEBUG)) {
            Log.d(TAG, "Unregistered for sensor events");
        }
    }
float min=0,max=0;
    @Override
    public void onSensorChanged(SensorEvent event) {detectJump(event.values[0], event.timestamp);


        if (event.values[0]<min)min=event.values[0];
        if (event.values[0]>max)max=event.values[0];
        textView2.setText("max:" +Float.toString(max)+ "\n" + "min:"+Float.toString(min));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    /**
     * A simple algorithm to detect a successful up-down movement of hand(s). The algorithm is
     * based on the assumption that when a person is wearing the watch, the x-component of gravity
     * as measured by the Gravity Sensor is +9.8 when the hand is downward and -9.8 when the hand
     * is upward (signs are reversed if the watch is worn on the right hand). Since the upward or
     * downward may not be completely accurate, we leave some room and instead of 9.8, we use
     * GRAVITY_THRESHOLD. We also consider the up <-> down movement successful if it takes less than
     * TIME_THRESHOLD_NS.
     */
    private void detectJump(float xValue, long timestamp) {

       // textView2.setText("Math.abs(xValue):"+Math.abs(xValue));

        if(xValue > 0){

            if ((xValue > GRAVITY_THRESHOLD)){
                mUp = true;
            }else{
                mUp = false;
            }
        }else{
            if ((timestamp - mLastTime < TIME_THRESHOLD_NS) && mUp)
                onJumpDetected(!mUp);
            mUp = false;
        }
            mLastTime = timestamp;

        /*
        if ((Math.abs(xValue) > GRAVITY_THRESHOLD)) {
            if((timestamp - mLastTime < TIME_THRESHOLD_NS) && mUp != (xValue > 0)) {

                onJumpDetected(!mUp);
            }
            mUp = xValue > 0;
            mLastTime = timestamp;
        }*/
    }

    /**
     * Called on detection of a successful down -> up or up -> down movement of hand.
     */
    private void onJumpDetected(boolean up) {
        // we only count a pair of up and down as one successful movement
       // if (up) {return;}
        mJumpCounter++;
        setCounter(mJumpCounter);
        min=0;
        max=0;
        renewTimer();
        textView.setText("Salto exitoso:"+mJumpCounter);
    }

    /**
     * Updates the counter on UI, saves it to preferences and vibrates the watch when counter
     * reaches a multiple of 3.
     */
    private void setCounter(int i) {
        //Utils.saveCounterToPreference(this, i);
        if (i > 0 ) {//&& i % 3 == 0
            vibrar.vibrate(200);
            //Utils.vibrate(this, 0);
        }
    }

    public void resetCounter() {
        setCounter(0);
        renewTimer();
    }

    /**
     * Starts a timer to clear the flag FLAG_KEEP_SCREEN_ON.
     */
    private void renewTimer() {
        if (null != mTimer) {
            mTimer.cancel();
        }
        TimerTask mTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (Log.isLoggable(TAG, Log.DEBUG)) {
                    Log.d(TAG,
                            "Removing the FLAG_KEEP_SCREEN_ON flag to allow going to background");
                }
             //   resetFlag();
            }
        };
//        mTimer = new Timer();
//        mTimer.schedule(mTimerTask, SCREEN_ON_TIMEOUT_MS);
    }

    /**
     * Resets the FLAG_KEEP_SCREEN_ON flag so activity can go into background.
     */
    private void resetFlag() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (Log.isLoggable(TAG, Log.DEBUG)) {
                    Log.d(TAG, "Resetting FLAG_KEEP_SCREEN_ON flag to allow going to background");
                }
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                finish();
            }
        });
    }


}
