package com.globant.eventscorelib.baseComponents;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.location.Address;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;

import com.globant.eventscorelib.controllers.CloudDataController;
import com.globant.eventscorelib.controllers.DatabaseController;
import com.globant.eventscorelib.controllers.GeocoderController;
import com.globant.eventscorelib.controllers.TwitterController;
import com.globant.eventscorelib.domainObjects.Event;
import com.globant.eventscorelib.domainObjects.Speaker;
import com.globant.eventscorelib.utils.Logger;
import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import twitter4j.Status;
import twitter4j.User;

/**
 * Created by ariel.cattaneo on 09/04/2015.
 */
public class BaseService extends Service {

    public static boolean isRunning = false;
    // This is the object that receives interactions from clients.
    private final IBinder mBinder = new BaseBinder();

    private final static int TIMEOUT_MINUTES = 5;
    Handler mHandler = new Handler();
    Runnable mRunnable;

    protected DatabaseController mDatabaseController = null;
    protected CloudDataController mCloudDataController = null;
    protected GeocoderController mGeocoderController = null;
    protected TwitterController mTwitterController = null;

    /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class BaseBinder extends Binder {
        public BaseService getService() {
            return BaseService.this;
        }
    }

    @Override
    public void onCreate() {
        mCloudDataController = new CloudDataController();
        mGeocoderController = new GeocoderController(getBaseContext());
        mTwitterController = new TwitterController();
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

    public enum ACTIONS {EVENT_LIST, EVENT_DETAIL, EVENT_CREATE, EVENT_DELETE, POSITION_COORDINATES, POSITION_ADDRESS
    ,TWEET_POST, GET_TWITTER_USER, TWITTER_LOADER, TWITTER_LOADER_RESPONSE, TWEETS_LIST, SUBSCRIBER_CHECKIN, EVENT_SPEAKERS}

    public TwitterController getTwitterController() {
        return mTwitterController;
    }

    private HashMap<String, ActionWrapper> currentSubscribers = new HashMap<>();

    synchronized public void subscribeActor(ActionListener anActionListener){
        if (!currentSubscribers.containsKey(anActionListener.getBindingKey())) {
            ActionWrapper currentSubscriber = new ActionWrapper(anActionListener);
            currentSubscribers.put(anActionListener.getBindingKey(), currentSubscriber);
        }

        if (cachedElements.containsKey(anActionListener.getBindingKey())){
            HashMap<ACTIONS,Object> cachedElement =cachedElements.remove(anActionListener.getBindingKey());
            for (ACTIONS key : cachedElement.keySet()) {
                anActionListener.onFinishAction(key,cachedElement.remove(key));
            }
        }
    }
    
    synchronized public void unSubscribeActor(ActionListener anActionListener){
        if (anActionListener != null && currentSubscribers.containsKey(anActionListener.getBindingKey())) {
            currentSubscribers.remove(anActionListener.getBindingKey());
        }
    }

    synchronized public void executeAction(final ACTIONS theAction, final Object argument, final String bindingKey) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                HashSet<ActionWrapper> subscribers = new HashSet<>(currentSubscribers.values());
                for (ActionWrapper currentSubscriber : subscribers) {
                    if (currentSubscriber.getBindingKey().equals(bindingKey)) {
                        try {
                            currentSubscriber.startAction(theAction);
                            switch (theAction) {
                                case EVENT_SPEAKERS:
                                    List<Speaker> speakers = mCloudDataController.getEventSpeakers((String) argument);
                                    currentSubscriber.finishAction(theAction, speakers);
                                    break;
                                case EVENT_CREATE:
                                    mCloudDataController.createEvent((Event) argument);
                                    currentSubscriber.finishAction(theAction, null);
                                    break;
                                case EVENT_DELETE:
                                    break;
                                case EVENT_LIST:
                                    List<Event> theEvents = mCloudDataController.getEvents((boolean) argument);
                                    currentSubscriber.finishAction(theAction, theEvents);
                                    break;
                                case EVENT_DETAIL:
                                    Event event = mCloudDataController.getEvent((String) argument);
                                    currentSubscriber.finishAction(theAction, event);
                                    break;
                                case POSITION_ADDRESS:
                                    Address address = mGeocoderController.getAddressFromCoordinates((LatLng) argument);
                                    currentSubscriber.finishAction(theAction, address);
                                    break;
                                case POSITION_COORDINATES:
                                    LatLng latLng = mGeocoderController.getCoordinatesFromAddress((String) argument);
                                    currentSubscriber.finishAction(theAction, latLng);
                                    break;
                                case GET_TWITTER_USER:
                                    User user;
                                    try {
                                        user = mTwitterController.getUser();
                                    } catch (Exception e) {
                                        user = null;
                                    }
                                    currentSubscriber.finishAction(theAction, user);
                                    break;
                                case TWEETS_LIST:
                                    List<Status> tweetList = mTwitterController.getTweetList(getBaseContext(), (String) argument);
                                    currentSubscriber.finishAction(theAction, tweetList);
                                    break;
                                case TWITTER_LOADER:
                                    Boolean login = mTwitterController.loginToTwitter(getBaseContext());
                                    currentSubscriber.finishAction(theAction, login);
                                    break;
                                case TWITTER_LOADER_RESPONSE:
                                    Boolean response = mTwitterController.getLoginResponse((Uri) argument);
                                    currentSubscriber.finishAction(theAction, response);
                                    break;
                                case TWEET_POST:
                                    Boolean post = mTwitterController.publishPost((String) argument);
                                    currentSubscriber.finishAction(theAction, post);
                                    break;
                                case SUBSCRIBER_CHECKIN:
                                    mCloudDataController.setCheckIn((String) argument, getBaseContext());
                                    currentSubscriber.finishAction(theAction, argument);
                                    break;
                            }
                        } catch (Exception e) {
                            currentSubscriber.failAction(theAction, e);
                            Logger.e("executeAction", e);
                        }
                    }
                }
            }
        };
        new Thread(r).start();
    }

    private HashMap<Object,HashMap<ACTIONS,Object>> cachedElements = new HashMap();

    public static interface ActionListener {
        public Activity getBindingActivity();
        public String getBindingKey();
        public void onStartAction(ACTIONS theAction);
        public void onFinishAction(ACTIONS theAction, Object result);
        public void onFailAction(ACTIONS theAction, Exception e);
    }

    public class ActionWrapper {

        private Activity anActivity;
        private Activity getActivity(){return anActivity;}

        ActionListener theListener;

        public String getBindingKey() {return theListener.getBindingKey();}

        public ActionWrapper(ActionListener aListener) {
            this.anActivity = aListener.getBindingActivity();
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
        }

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
        }

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
        }
    }
}
