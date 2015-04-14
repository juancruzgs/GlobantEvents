package com.globant.eventscorelib.baseComponents;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;

import com.globant.eventscorelib.domainObjects.BaseObject;
import com.globant.eventscorelib.domainObjects.Event;
import com.globant.eventscorelib.utils.Logger;

import java.util.HashMap;
import java.util.List;

/**
 * Created by ariel.cattaneo on 09/04/2015.
 */
public class BaseService extends Service {

    public static boolean isRunning = false;

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
    protected CloudDataController mCloudDataController = null;



    @Override
    public void onCreate() {
        mRunnable = new Runnable() {
            @Override
            public void run() {
                isRunning = false;
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

        mCloudDataController = new CloudDataController();

        startCountdown();

        isRunning = true;

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

    public DatabaseController getDatabaseController() {
        if (mDatabaseController == null) {
            // TODO: Make a new DatabaseController with the right subclass
            // TODO: Init it if needed
        }
        return mDatabaseController;
    }

    public CloudDataController getCloudDataController() {
        if (mCloudDataController == null) {
            // TODO: Make a new CloudDataController with the right subclass
            // TODO: Init it if needed
        }
        return mCloudDataController;
    }


    ///HERE THE BARDO BEGINS

    public enum ACTIONS {EVENT_LIST, EVENT_DETAIL, EVENT_CREATE, EVENT_DELETE}

    private ActionWrapper currentSubscriber;

    public void subscribeActor(ActionListener anActionListener){
          currentSubscriber = new ActionWrapper(anActionListener);

           if (cachedElements.containsKey(anActionListener.getBindingKey())){
              HashMap<ACTIONS,Object> cachedElement =cachedElements.remove(anActionListener.getBindingKey());
              for (ACTIONS key : cachedElement.keySet()) {
                  anActionListener.onFinishAction(key,cachedElement.remove(key));

              }

          }
    }

    public void unSubscribeActor(ActionListener anActionListener){
            currentSubscriber = null;
//        if(){}
    }

    private HashMap<Object,HashMap<ACTIONS,Object>> cachedElements = new HashMap();


    public abstract class ActionListener {

        abstract protected Activity getActivity();

        abstract protected Object getBindingKey();

        abstract void onStartAction(ACTIONS theAction);

        abstract void onFinishAction(ACTIONS theAction, Object result);

        abstract void onFailAction(ACTIONS theAction, Exception e);

    }

    public  class ActionWrapper {

        private Activity anActivity;
        private Activity getActivity(){return anActivity;}

        ActionListener theListener;

        public ActionWrapper(ActionListener aListener) {
            this.anActivity = aListener.getActivity();
            theListener = aListener;
        }

        void startAction(final ACTIONS theAction){


             if (getActivity()!=null&& !getActivity().isFinishing()) {
                 getActivity().runOnUiThread(new Runnable() {
                     @Override
                     public void run() {
                         theListener.onStartAction(theAction);
                     }
                 });
             }else{

                 // TODO: Decide what to do with an started action when the Activity isn't available

             }


         };

         void finishAction(final ACTIONS theAction, final Object result){

             if (getActivity()!=null&& !getActivity().isFinishing()) {
                 getActivity().runOnUiThread(new Runnable() {
                     @Override
                     public void run() {
                         theListener.onFinishAction(theAction, result);
                     }
                 });
             }else{
                 HashMap<ACTIONS,Object> cachedElement = new HashMap();
                 cachedElement.put(theAction,result);
                 cachedElements.put(theListener.getBindingKey(),cachedElement);
             }

         };

         void failAction(final ACTIONS theAction, final Exception e){

             if (getActivity()!=null&& !getActivity().isFinishing()) {
                 getActivity().runOnUiThread(new Runnable() {
                     @Override
                     public void run() {
                         theListener.onFailAction(theAction, e);
                     }
                 });
             }else{
                 // TODO: Decide what to do with an failed action when the Activity isn't available
//                 HashMap<ACTIONS,Object> cachedElement = new HashMap();
//                 cachedElement.put(theAction,e);
//                 cachedElements.put(getBindingKey(), cachedElement);
             }

         };



    }

    private void executeAction(final ACTIONS theAction, final BaseObject argument) {
        Runnable r = new Runnable() {
            @Override
            public void run() {

                try {
                    currentSubscriber.startAction(theAction);
                    switch (theAction) {
                        case EVENT_CREATE:
                            mCloudDataController.createEvent((Event)argument);
                            currentSubscriber.finishAction(theAction, null);
                            break;
                        case EVENT_DELETE:
                            break;
                        case EVENT_LIST:
                           List<Event> theEvents = mCloudDataController.getEvents();
                            currentSubscriber.finishAction(theAction, theEvents);
                            break;
                        case EVENT_DETAIL:
                            break;


                    }
                } catch (Exception e) {

                    currentSubscriber.failAction(theAction, e);
                    Logger.e("executeAction", e);
                }

            }
        };
        new Thread(r).start();


    }
}
