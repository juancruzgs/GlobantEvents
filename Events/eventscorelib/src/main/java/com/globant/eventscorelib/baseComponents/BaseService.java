package com.globant.eventscorelib.baseComponents;

import android.app.Activity;
import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.provider.CalendarContract;
import android.support.v4.content.CursorLoader;

import com.globant.eventscorelib.controllers.CloudDatabaseController;
import com.globant.eventscorelib.controllers.GeocoderController;
import com.globant.eventscorelib.controllers.LocalDatabaseController;
import com.globant.eventscorelib.controllers.SelectiveDatabaseController;
import com.globant.eventscorelib.controllers.SharedPreferencesController;
import com.globant.eventscorelib.controllers.TwitterController;
import com.globant.eventscorelib.domainObjects.Event;
import com.globant.eventscorelib.domainObjects.Subscriber;
import com.globant.eventscorelib.utils.CoreConstants;
import com.globant.eventscorelib.utils.Logger;
import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.TimeZone;

import twitter4j.User;

/**
 * Base service class
 *
 * Created by ariel.cattaneo on 09/04/2015.
 */
public abstract class BaseService extends Service {

    private long mNCalendar;

    public long getNCalendar() {
        return mNCalendar;
    }

    public void setNCalendar(long NCalendar) {
        mNCalendar = NCalendar;
    }

    class CalendarInfo {
        public String name;
        public String id;
        public CalendarInfo(String _name, String _id) {
            name = _name;
            id = _id;
        }
        @Override
        public String toString() {
            return name;
        }
    }

    private CalendarInfo mCalendars[];

    private void getCalendars() {
        String[] l_projection = new String[]{
                CalendarContract.Calendars._ID,
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME
        };
        Uri l_calendars;
        if (Build.VERSION.SDK_INT >= 8 ) {
            l_calendars = Uri.parse("content://com.android.calendar/calendars");
        } else {
            l_calendars = Uri.parse("content://calendar/calendars");
        }
        //Cursor l_managedCursor = this.managedQuery(l_calendars, l_projection, null, null, null);    //all calendars
        CursorLoader loader = new CursorLoader(this, l_calendars, l_projection, null, null, null);    //all calendars
        //CursorLoader loader = new CursorLoader(this, l_calendars, l_projection, "selected=1", null, null);   //active calendars
        Cursor l_managedCursor = loader.loadInBackground();
        if (l_managedCursor.moveToFirst()) {
            mCalendars = new CalendarInfo[l_managedCursor.getCount()];
            String l_calName;
            String l_calId;
            int l_cnt = 0;
            int l_nameCol = l_managedCursor.getColumnIndex(l_projection[1]);
            int l_idCol = l_managedCursor.getColumnIndex(l_projection[0]);
            do {
                l_calName = l_managedCursor.getString(l_nameCol);
                l_calId = l_managedCursor.getString(l_idCol);
                mCalendars[l_cnt] = new CalendarInfo(l_calName, l_calId);
                ++l_cnt;
            } while (l_managedCursor.moveToNext());
        }
    }

    public static boolean isRunning = false;
    //protected static List<String> cancelKeys = new ArrayList<>();
    // This is the object that receives interactions from clients.
    private final IBinder mBinder = new BaseBinder();

    private final static int TIMEOUT_MINUTES = 5;
    Handler mHandler = new Handler();
    Runnable mRunnable;

    protected LocalDatabaseController mLocalDatabaseController = null;
    protected CloudDatabaseController mCloudDatabaseController = null;
    protected SelectiveDatabaseController mSelectiveDatabaseController = null;
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
        mLocalDatabaseController = new LocalDatabaseController();
        mCloudDatabaseController = new CloudDatabaseController();
        mSelectiveDatabaseController  = new SelectiveDatabaseController(mLocalDatabaseController, mCloudDatabaseController);
        mGeocoderController = new GeocoderController(getBaseContext());
        mTwitterController = new TwitterController(getTwitterCallbackURL(), getBaseContext());
        mRunnable = new Runnable() {
            @Override
            public void run() {
                isRunning = false;
                stopSelf();
            }
        };
    }

    protected abstract String getTwitterCallbackURL();

    public boolean existsCalendar(long calendarId) {
        getCalendars();

        int pos = 0;
        while (pos < mCalendars.length) {
            if (Long.parseLong(mCalendars[pos].id) == calendarId) {
                return true;
            }
            pos++;
        }

        return false;
    }

    public long getCalendarIdFromOrder(int order) {
        getCalendars();

        return Long.parseLong(mCalendars[order].id);
    }

    protected void updateEvent(Event event) throws ParseException {
        mCloudDatabaseController.updateEvent(event);
        try {
            JSONObject eventsArray = SharedPreferencesController.loadJSONObject(this,
                    getApplicationInfo().name, SharedPreferencesController.KEY_CALENDAR);
            if (eventsArray.has(event.getObjectID())) {
                JSONObject eventObject = eventsArray.getJSONObject(event.getObjectID());
                long rows = updateEventInCalendar(/*eventObject.getInt(CoreConstants.CALENDAR_SELF_ID),*/
                        eventObject.getLong(CoreConstants.CALENDAR_EVENT_ID), event);
                if (rows == -1) {
                    SharedPreferencesController.removeEventJsonInfo(this, event);
                }
            }
        }
        catch (JSONException e) {
            Logger.e("Problems trying to find local info about this event", e);
        }
    }

    protected long addEventToCalendar(Event event) {
        ContentResolver contentResolver = getContentResolver();

        long calID = mNCalendar;
        long startMillis;
        long endMillis;
        Calendar beginTime = Calendar.getInstance();
        beginTime.setTime(event.getStartDate());
        startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.setTime(event.getEndDate());
        endMillis = endTime.getTimeInMillis();

        ContentValues contentValues = new ContentValues();
        contentValues.put(CalendarContract.Events.DTSTART, startMillis);
        contentValues.put(CalendarContract.Events.DTEND, endMillis);
        contentValues.put(CalendarContract.Events.TITLE, event.getTitle());
        contentValues.put(CalendarContract.Events.DESCRIPTION, event.getShortDescription());
        contentValues.put(CalendarContract.Events.CALENDAR_ID, calID);
        // TODO: Get the right timezone. List in TimeZone.getAvailableIDs(). Format: Continent/City
        contentValues.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
        Uri uri = contentResolver.insert(CalendarContract.Events.CONTENT_URI, contentValues);

        // get the event ID that is the last element in the Uri
        long eventID = Long.parseLong(uri.getLastPathSegment());

        ContentValues values = new ContentValues();
        values.put(CalendarContract.Reminders.MINUTES, 1440);
        values.put(CalendarContract.Reminders.EVENT_ID, eventID);
        values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
        uri = contentResolver.insert(CalendarContract.Reminders.CONTENT_URI, values);

        return eventID;
    }

    protected long removeEventFromCalendar(/*Integer calendarID,*/ Long eventID) {
        //ContentResolver cr = getContentResolver();
        //ContentValues values = new ContentValues();
        //Uri deleteUri = null;
        Uri deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID);

        return getContentResolver().delete(deleteUri, null, null);
    }

    protected long updateEventInCalendar(/*Integer calendarID,*/ Long eventID, Event event) {
        ContentResolver contentResolver = getContentResolver();

        if (mNCalendar > mCalendars.length-1) {
            return -1;
        }
        long calID = mNCalendar;
        long startMillis;
        long endMillis;
        Calendar beginTime = Calendar.getInstance();
        beginTime.setTime(event.getStartDate());
        startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.setTime(event.getEndDate());
        endMillis = endTime.getTimeInMillis();

        ContentValues contentValues = new ContentValues();
        contentValues.put(CalendarContract.Events.DTSTART, startMillis);
        contentValues.put(CalendarContract.Events.DTEND, endMillis);
        contentValues.put(CalendarContract.Events.TITLE, event.getTitle());
        contentValues.put(CalendarContract.Events.DESCRIPTION, event.getShortDescription());
        contentValues.put(CalendarContract.Events.CALENDAR_ID, calID);
        // TODO: Get the right timezone. List in TimeZone.getAvailableIDs(). Format: Continent/City
        contentValues.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
        Uri uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID);
        int rows = contentResolver.update(uri, contentValues, null, null);

        return rows;
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

    public enum ACTIONS {
        EVENT_LIST, EVENTS_LIST_REFRESH, EVENT_CREATE, EVENT_UPDATE, EVENT_DELETE, POSITION_COORDINATES, POSITION_ADDRESS,
        TWEET_POST, GET_TWITTER_USER, TWITTER_LOADER, TWITTER_LOADER_RESPONSE, TWEETS_LIST, SUBSCRIBER_CHECKIN, EVENT_SPEAKERS,
        PARTICIPANT_LIST, SUBSCRIBER_EXISTS, SUBSCRIBER_CREATE, EVENTS_TO_SUBSCRIBER_CREATE, IS_SUBSCRIBED, SUBSCRIBER_UPDATE, SET_ACCEPTED,
        GET_EVENT_HISTORY, GET_EVENT, REFRESH_SUBSCRIBERS, ADD_EVENT_TO_CALENDAR, GET_CALENDARS, REMOVE_EVENT_FROM_CALENDAR,
        UPDATE_EVENT_IN_CALENDAR}

    private HashMap<String, ActionWrapper> currentSubscribers = new HashMap<>();

    public void subscribeActor(ActionListener anActionListener){
        String bindingKey = anActionListener.getBindingKey();

        if (!currentSubscribers.containsKey(bindingKey)) {
            ActionWrapper currentSubscriber = new ActionWrapper(anActionListener);
            currentSubscribers.put(bindingKey, currentSubscriber);
        }
//        if (cancelKeys.contains(bindingKey))
//            cancelKeys.remove(bindingKey);

        if (cachedElements.containsKey(bindingKey)){
            HashMap<ACTIONS,Object> cachedElement =cachedElements.remove(bindingKey);
            for (ACTIONS key : cachedElement.keySet()) {
                anActionListener.onFinishAction(key,cachedElement.remove(key));
            }
        }

    }
    
    public void unSubscribeActor(ActionListener anActionListener){
        String bindingKey = anActionListener != null ? anActionListener.getBindingKey() : "";
        if (anActionListener != null && currentSubscribers.containsKey(bindingKey)) {
            currentSubscribers.remove(bindingKey);
//            cancelKeys.add(bindingKey);
        }
    }

    public ActionListener getActionListener(String bindingKey) {
        ActionWrapper subscriber = currentSubscribers.get(bindingKey);
        return subscriber == null ? null : subscriber.theListener;
    }

    public void executeAction(final ACTIONS theAction, final String bindingKey, final Object ... arguments) {
        if ((theAction == ACTIONS.GET_CALENDARS) || (theAction == ACTIONS.UPDATE_EVENT_IN_CALENDAR &&
            (mCalendars == null || mCalendars.length == 0))) {
            getCalendars();
        }
        Runnable r = new Runnable() {
            @Override
            public void run() {
                HashSet<ActionWrapper> subscribers = new HashSet<>(currentSubscribers.values());
                for (ActionWrapper currentSubscriber : subscribers) {
                    if (currentSubscriber.getBindingKey().equals(bindingKey)) {
                        try {
                            currentSubscriber.startAction(theAction);
                            Object result = null;
                            switch (theAction) {
                                case EVENT_SPEAKERS:
                                    result = mCloudDatabaseController.getEventSpeakers((String) arguments[0]);
                                    break;
                                case EVENT_CREATE:
                                    mCloudDatabaseController.createEvent((Event) arguments[0]);
                                    break;
                                case EVENT_UPDATE:
                                    updateEvent((Event) arguments[0]);
                                    break;
                                case EVENT_DELETE:
                                    mCloudDatabaseController.deleteEvent((Event) arguments[0]);
                                    break;
                                case EVENTS_LIST_REFRESH:
                                    result = mCloudDatabaseController.getEvents((boolean) arguments[0]);
                                    break;
                                case EVENT_LIST:
                                    result = mSelectiveDatabaseController.getEvents((boolean) arguments[0], (boolean) arguments[1]);
                                    break;
//                                case EVENT_DETAIL:
//                                    result = mCloudDatabaseController.getEvent((String) arguments[0]);
//                                    break;
                                case POSITION_ADDRESS:
                                    result = mGeocoderController.getAddressFromCoordinates((LatLng) arguments[0]);
                                    break;
                                case POSITION_COORDINATES:
                                    result = mGeocoderController.getCoordinatesFromAddress((String) arguments[0]);
                                    break;
                                case GET_TWITTER_USER:
                                    User user;
                                    try {
                                        user = mTwitterController.getUser();
                                    } catch (Exception e) {
                                        user = null;
                                    }
                                    result = user;
                                    break;
                                case TWEETS_LIST:
                                    result = mTwitterController.getTweetList((String) arguments[0]);
                                    break;
                                case TWITTER_LOADER:
                                    result = mTwitterController.loginToTwitter();
                                    break;
                                case TWITTER_LOADER_RESPONSE:
                                    result = mTwitterController.getLoginResponse((Uri) arguments[0]);
                                    break;
                                case TWEET_POST:
                                    result = mTwitterController.publishPost((String) arguments[0]);
                                    break;
                                case SUBSCRIBER_CHECKIN:
                                    //Object[] arguments  = (Object[]) arguments;
                                    result = mCloudDatabaseController.setCheckIn((String) arguments[0], (String) arguments[1]);
                                    break;
                                case PARTICIPANT_LIST:
                                    result = mCloudDatabaseController.getEventSubscribers((String) arguments[0]);
                                    break;
                                case SET_ACCEPTED:
                                    //Object[] objects = (Object[]) arguments;
                                    mCloudDatabaseController.setAccepted((String) arguments[0], (List<String>) arguments[1]);
                                    break;
                                case SUBSCRIBER_UPDATE:
                                    result = mCloudDatabaseController.updateSubscriber((Subscriber) arguments[0]);
                                    break;
                                case SUBSCRIBER_EXISTS:
                                    result = mCloudDatabaseController.getSubscriberId((String) arguments[0]);
                                    break;
                                case IS_SUBSCRIBED:
                                    //Object[] object = (Object[])arguments;
                                    result = mCloudDatabaseController.isSubscribed((String) arguments[0], (String) arguments[1]);
                                    break;
                                case SUBSCRIBER_CREATE:
                                    result = mCloudDatabaseController.createSubscriber((Subscriber) arguments[0]);
                                    break;
                                case EVENTS_TO_SUBSCRIBER_CREATE:
                                    //Object[] obj = (Object[])arguments;
                                    mCloudDatabaseController.createEventToSubscriber((Subscriber) arguments[0], (String) arguments[1]);
                                    break;
                                case GET_EVENT_HISTORY:
                                    result = mCloudDatabaseController.getEventHistory();
                                    break;
                                case GET_EVENT:
                                    result = mCloudDatabaseController.getEvent((String)arguments[0]);
                                    break;
                                case REFRESH_SUBSCRIBERS:
                                    result = mCloudDatabaseController.refreshSubscribers((String) arguments[0], (Date) arguments[1]);
                                    break;
                                case ADD_EVENT_TO_CALENDAR:
                                    result = addEventToCalendar((Event) arguments[0]);
                                    break;
                                case GET_CALENDARS:
                                    String[] calendarNames = new String[mCalendars.length];
                                    for (int counter = 0; counter < mCalendars.length; counter++) {
                                        calendarNames[counter] = mCalendars[counter].name;
                                    }
                                    result = calendarNames;
                                    break;
                                case REMOVE_EVENT_FROM_CALENDAR:
                                    result = removeEventFromCalendar(/*(Integer)arguments[0],*/ (Long)arguments[0]);
                                    break;
                                case UPDATE_EVENT_IN_CALENDAR:
                                    setNCalendar((Integer)arguments[0]);
                                    long rows = updateEventInCalendar(/*(Integer)arguments[0],*/ (Long)arguments[1],
                                            (Event)arguments[2]);
                                    result = new CalendarResponse((Event)arguments[2], rows);
                                    break;
                            }

//                            if (!cancelKeys.contains(bindingKey)) {
                                currentSubscriber.finishAction(theAction, result);
//                            }
//                            else {
//                                cancelKeys.remove(bindingKey);
//                            }
                        } catch (Exception e) {
                            currentSubscriber.failAction(theAction, e);
                            Logger.e("executeAction", e);
                        }
                    }
                }

                //cancelKeys.clear();
            }
        };
        new Thread(r).start();
    }

    private HashMap<Object,HashMap<ACTIONS,Object>> cachedElements = new HashMap<>();

    public interface ActionListener {
        Activity getBindingActivity();
        String getBindingKey();
        void onStartAction(ACTIONS theAction);
        void onFinishAction(ACTIONS theAction, Object result);
        void onFailAction(ACTIONS theAction, Exception e);
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
                HashMap<ACTIONS,Object> cachedElement = new HashMap<>();
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

    public void disengage(String key) {
        cachedElements.remove(key);
//        cancelKeys.add(key);
    }

    public class CalendarResponse {
        Event mEvent;
        Long mRows;

        public CalendarResponse(Event event, long rows) {
            mEvent = event;
            mRows = rows;
        }

        public Event getEvent() {
            return mEvent;
        }

        public void setEvent(Event event) {
            mEvent = event;
        }

        public Long getRows() {
            return mRows;
        }

        public void setRows(Long rows) {
            mRows = rows;
        }
    }
}
