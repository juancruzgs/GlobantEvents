package com.globant.events.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.globant.events.adapters.EventsListAdapterClient;
import com.globant.events.R;
import com.globant.eventscorelib.baseAdapters.BaseEventsListAdapter;
import com.globant.eventscorelib.baseFragments.BaseEventListFragment;
import com.globant.eventscorelib.baseComponents.BaseService;
import com.globant.eventscorelib.controllers.SharedPreferencesController;
import com.globant.eventscorelib.domainObjects.Event;
import com.globant.eventscorelib.utils.PushNotifications;

import java.util.Date;
import java.util.List;

public class EventListClientFragment extends BaseEventListFragment implements BaseService.ActionListener {
    private String mBindingKey;
    private Date mNow =  new Date();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBindingKey = this.getClass().getSimpleName() + new Date().toString();
    }

    @Override
    public void setService(BaseService service) {
        super.setService(service);
        if (checkDateToCleanChannelList()) {
            mService.executeAction(BaseService.ACTIONS.GET_EVENT_HISTORY, getBindingKey(), null);
        }
    }

    private boolean checkDateToCleanChannelList(){
        Date now = new Date();
        Date clean_date  =  new Date(SharedPreferencesController.getCleanChannelDate(getActivity()));
        if (now.compareTo(clean_date) >= 0){
            SharedPreferencesController.setCleanChannelDate(getActivity(), now.getTime() + 604800000L); // 7 * 24 * 60 * 60 * 1000
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_client_event_list;
    }

    @Override
    protected boolean getIsGlober() {
        return SharedPreferencesController.isGlober(getActivity());
    }

    @Override
    protected BaseEventsListAdapter getAdapter() {
        return new EventsListAdapterClient(getEventList(), getActivity(), this);
    }

    public EventListClientFragment() {
    }

    @Override
    public BaseService.ActionListener getActionListener() {
        return this;
    }

    @Override
    public void getEvent(String eventId) {

    }

    @Override
    public Activity getBindingActivity() {
        return getActivity();
    }

    @Override
    public String getBindingKey() {
        return mBindingKey;
    }

    @Override
    public void onStartAction(BaseService.ACTIONS theAction) {

    }

    @Override
    public void onFinishAction(BaseService.ACTIONS theAction, Object result) {
        if (theAction == BaseService.ACTIONS.GET_EVENT_HISTORY){
            List<Event> events = (List<Event>) result;
            List<String> channels = PushNotifications.getSuscribedChannelsList();
            for (Event event: events){
                if (mNow.compareTo(event.getEndDate()) > 0){
                    if (channels.contains(getString(R.string.prefix_channel) + event.getObjectID())){
                        PushNotifications.unsubscribeToChannel(getString(R.string.prefix_channel) + event.getObjectID());
                    }
                    if (channels.contains(getString(R.string.prefix_participants) + event.getObjectID())){
                        PushNotifications.unsubscribeToChannel(getString(R.string.prefix_participants) + event.getObjectID());
                    }
                    if (channels.contains(getString(R.string.prefix_checkin) + event.getObjectID())){
                        PushNotifications.unsubscribeToChannel(getString(R.string.prefix_checkin) + event.getObjectID());
                    }
                    if (channels.contains(getString(R.string.prefix_subscriber) + event.getObjectID())){
                        PushNotifications.unsubscribeToChannel(getString(R.string.prefix_subscriber) + event.getObjectID());
                        for(String channel : channels){
                            if (channel.contains(getString(R.string.prefix_subscriber) + event.getObjectID())){
                                PushNotifications.unsubscribeToChannel(channel);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onFailAction(BaseService.ACTIONS theAction, Exception e) {

    }
}
