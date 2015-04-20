package com.example.user.multitouchtest;

import android.graphics.PointF;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;


public class Multitouch extends ActionBarActivity {

    private static final int SIZE = 60;
    private SparseArray<PointF> mActivePointers;
    private final String  MSG = "simultaneous touch points :";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multitouch);
        initView();
        View view = findViewById(R.id.mainLayout);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // get pointer index from the event object
                int pointerIndex = event.getActionIndex();

                // get pointer ID
                int pointerId = event.getPointerId(pointerIndex);

                // get masked (not specific to a pointer) action
                int maskedAction = event.getActionMasked();

                switch (maskedAction) {

                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_POINTER_DOWN: {
                        PointF f = new PointF();
                        f.x = event.getX(pointerIndex);
                        f.y = event.getY(pointerIndex);
                        mActivePointers.put(pointerId, f);
                        break;
                    }
                    case MotionEvent.ACTION_MOVE: {
                        for (int size = event.getPointerCount(), i = 0; i < size; i++) {
                            PointF point = mActivePointers.get(event.getPointerId(i));
                            if (point != null) {
                                point.x = event.getX(i);
                                point.y = event.getY(i);
                            }
                        }
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:
                    case MotionEvent.ACTION_CANCEL: {
                        mActivePointers.remove(pointerId);
                        break;
                    }
                }
                Log.d(getClass().getSimpleName(), MSG + Integer.toString(mActivePointers.size()));

                if(mActivePointers.size()==3) {
                    Toast.makeText(getBaseContext(),MSG + '3',Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
    }

    private void initView() {
        mActivePointers = new SparseArray<PointF>();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_multitouch, menu);
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
