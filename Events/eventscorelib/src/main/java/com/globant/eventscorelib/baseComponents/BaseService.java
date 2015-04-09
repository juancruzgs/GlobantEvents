package com.globant.eventscorelib.baseComponents;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

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

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

}
