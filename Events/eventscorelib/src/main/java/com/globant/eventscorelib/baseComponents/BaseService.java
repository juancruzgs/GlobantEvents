package com.globant.eventscorelib.baseComponents;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;

import com.globant.eventscorelib.domainObjects.Event;

import java.util.List;

/**
 * Created by ariel.cattaneo on 09/04/2015.
 */
public class BaseService extends Service {

    /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class BaseBinder extends Binder {
        BaseService getService() {
            return BaseService.this;
        }
    }

    // This is the object that receives interactions from clients.
    private final IBinder mBinder = new BaseBinder();

    private final static int TIMEOUT_MINUTES = 5;
    Handler mHandler = new Handler();
    Runnable mRunnable;

    protected DatabaseController mDatabaseController = null;

    @Override
    public void onCreate() {
        mRunnable = new Runnable() {
            @Override
            public void run() {
                stopSelf();
            }
        };
    }

    private void startCountdown() {
        mHandler.postDelayed(mRunnable, 60000 * TIMEOUT_MINUTES);
    }

    private void stopCountdown() {
        mHandler.removeCallbacks(mRunnable);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        stopCountdown();

        // TODO: If mDatabaseController is null, throw an adequate exception
        // (the controller must be set in the subclass' onCreate() method)
        mDatabaseController.init();

        startCountdown();

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        stopCountdown();

        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        super.onUnbind(intent);

        startCountdown();

        return true;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);

        stopCountdown();
    }

    public Event getEvent(String id) {
        return (Event) mDatabaseController.getObject(id, "events");
    }
}
