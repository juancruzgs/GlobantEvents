package com.globant.eventscorelib.utils.easterEggs;

import android.graphics.PointF;
import android.util.SparseArray;
import android.view.MotionEvent;

import com.globant.eventscorelib.utils.easterEggs.BaseTouchEasterEgg;

/**
 * Created by ariel.cattaneo on 26/05/2015.
 */
public class MultitouchEasterEgg extends BaseTouchEasterEgg {

    private int mFingersNumberLast = 0;
    private int mFingersNumberCheck = 4;

    @Override
    public void uninit() {
        // TODO: Well... uninit this multitouch easter egg (if needed)
    }

    public void setFingersNumberCheck(int fingersNumberCheck) {
        mFingersNumberCheck = fingersNumberCheck;
    }

    @Override
    public void touchEvent(MotionEvent event) {
        SparseArray<PointF> activePointers = new SparseArray<>();

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
                activePointers.put(pointerId, f);
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                for (int size = event.getPointerCount(), i = 0; i < size; i++) {
                    PointF point = activePointers.get(event.getPointerId(i));
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
                activePointers.remove(pointerId);
                break;
            }
        }

        int fingersNumberNow = activePointers.size();
        if (mFingersNumberLast != fingersNumberNow) {
            mFingersNumberLast = fingersNumberNow;
            if (mFingersNumberLast == mFingersNumberCheck) {
                triggerEgg();
            }
        }
    }
}
