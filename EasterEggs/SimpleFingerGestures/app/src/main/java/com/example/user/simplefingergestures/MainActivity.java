package com.example.user.simplefingergestures;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import in.championswimmer.sfg.lib.SimpleFingerGestures;

// tomado de https://android-arsenal.com/details/1/777

public class MainActivity extends ActionBarActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SimpleFingerGestures mySfg = new SimpleFingerGestures();

        mySfg.setDebug(true);
        mySfg.setConsumeTouchEvents(true);

        mySfg.setOnFingerGestureListener(new SimpleFingerGestures.OnFingerGestureListener() {
            @Override
            public boolean onSwipeUp(int fingers, long gestureDuration) {
                Toast.makeText(MainActivity.this,"swiped " + fingers + " up",Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onSwipeDown(int fingers, long gestureDuration) {
                Toast.makeText(MainActivity.this,"swiped " + fingers + " down",Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onSwipeLeft(int fingers, long gestureDuration) {
                Toast.makeText(MainActivity.this,"swiped " + fingers + " left",Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onSwipeRight(int fingers, long gestureDuration) {
                Toast.makeText(MainActivity.this,"swiped " + fingers + " right",Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onPinch(int fingers, long gestureDuration) {
                Toast.makeText(MainActivity.this,"pinch",Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onUnpinch(int fingers, long gestureDuration) {
                Toast.makeText(MainActivity.this,"unpinch",Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        RelativeLayout view =(RelativeLayout) findViewById(R.id.layout_for_test);

        view.setOnTouchListener(mySfg);
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
