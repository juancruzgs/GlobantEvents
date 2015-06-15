package com.globant.eventscorelib.baseFragments;


import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.globant.eventscorelib.R;
import com.globant.eventscorelib.baseActivities.BaseEventDetailPagerActivity;
import com.globant.eventscorelib.baseActivities.BaseMapEventDescriptionActivity;
import com.globant.eventscorelib.baseActivities.BasePagerActivity;
import com.globant.eventscorelib.baseActivities.BaseSubscriberActivity;
import com.globant.eventscorelib.baseComponents.BaseService;
import com.globant.eventscorelib.domainObjects.Event;
import com.globant.eventscorelib.utils.ConvertImage;
import com.globant.eventscorelib.utils.CoreConstants;
import com.globant.eventscorelib.utils.CustomDateFormat;
import com.globant.eventscorelib.utils.JSONSharedPreferences;
import com.globant.eventscorelib.utils.Logger;
import com.globant.eventscorelib.utils.ScrollChangeCallbacks;
import com.globant.eventscorelib.utils.PushNotifications;
import com.software.shell.fab.ActionButton;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class BaseEventDescriptionFragment extends BaseFragment implements BaseService.ActionListener, BasePagerActivity.FragmentLifecycle {

    private View mToolbar;
    private ImageView mEventImage;
    private AppCompatTextView mEventTitle;
    private AppCompatTextView mEventStartDate;
    private AppCompatTextView mEventEndDate;
    private AppCompatTextView mEventAddress;
    private AppCompatTextView mEventCity;
    private AppCompatTextView mEventCountry;
    private AppCompatTextView mEventLanguage;
    private AppCompatTextView mEventAdditionalInfo;
    private AppCompatTextView mEventFullDescription;
    protected ImageView mMapIcon;
    private View mOverlayView;
    private ObservableScrollView mScrollView;
    protected ActionButton mFab;
    protected Event mEvent;
    private String mBindingKey;

    private AppCompatTextView mButtonAddToCalendar;
    private boolean mAddedToCalendar = false;

    public BaseEventDescriptionFragment() {
    }

    @Override
    public BaseService.ActionListener getActionListener() {
        return this;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBindingKey = this.getClass().getSimpleName(); // + new Date().toString();
    }

    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_event_description, container, false);
        hideUtilsAndShowContentOverlay();
        wireUpViews(rootView);
        mEvent = BaseEventDetailPagerActivity.getInstance().getEvent();
        if (mEvent != null) {
            loadEventDescription();
        }
        initializeViewParameters();
        setCalendarButtonText();
        setHasOptionsMenu(true);
        setRetainInstance(true);
        return rootView;
    }

    private void changeIconColor() {
        Drawable drawableToApply = mMapIcon.getDrawable();
        drawableToApply = DrawableCompat.wrap(drawableToApply);
        DrawableCompat.setTint(drawableToApply, getActivity().getResources().getColor(R.color.grey));
        drawableToApply = DrawableCompat.unwrap(drawableToApply);
    }

    private void prepareMapIconButton() {
        mMapIcon.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), BaseMapEventDescriptionActivity.class);
                        intent.putExtra(CoreConstants.MAP_MARKER_POSITION_INTENT, mEvent.getCoordinates());
                        startActivity(intent);
                    }
                }
        );
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.menu_event_description, menu);
    }


    protected void initializeViewParameters() {
        int actionBarSize = getActionBarSize();
        int flexibleSpaceImageHeight = getResources().getDimensionPixelSize(com.globant.eventscorelib.R.dimen.flexible_space_image_height);
        int toolbarColor = getResources().getColor(com.globant.eventscorelib.R.color.globant_green);
        int flexibleSpaceShowFabOffset = getResources().getDimensionPixelSize(com.globant.eventscorelib.R.dimen.flexible_space_show_fab_offset);
        int fabMargin = getResources().getDimensionPixelSize(com.globant.eventscorelib.R.dimen.activity_horizontal_margin);
        ScrollChangeCallbacks scrollChangeCallbacks = new ScrollChangeCallbacks(actionBarSize, flexibleSpaceImageHeight, toolbarColor, flexibleSpaceShowFabOffset,
                fabMargin, mToolbar, mOverlayView, mEventTitle, mEventImage, mFab , false, getActivity());
        mScrollView.setScrollViewCallbacks(scrollChangeCallbacks);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PushNotifications.subscribeToChannel("CH-" + mEvent.getObjectID());
                prepareBaseSubscriberActivity();
            }
        });

        ScrollUtils.addOnGlobalLayoutListener(mScrollView, new Runnable() {
            @Override
            public void run() {
                mScrollView.scrollTo(0, 1);
                mScrollView.scrollTo(0, 0);
            }
        });
    }

    private void prepareBaseSubscriberActivity() {
        Intent intent = new Intent(getActivity(), BaseSubscriberActivity.class);
        intent.putExtra(CoreConstants.FIELD_CHECK_IN, true);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == 0) {
//            if (resultCode == Activity.RESULT_OK) {
//                showProgressOverlay();
//                String eventId = data.getStringExtra(CoreConstants.SCAN_RESULT);
//                mService.executeAction(BaseService.ACTIONS.SUBSCRIBER_CHECKIN, eventId, getBindingKey());
//            }
//        }
    }

    private void wireUpViews(View rootView) {
        mToolbar = rootView.findViewById(R.id.toolbar);
        mEventImage = (ImageView) rootView.findViewById(R.id.image);
        mEventTitle = (AppCompatTextView) rootView.findViewById(R.id.title);
        mEventStartDate = (AppCompatTextView) rootView.findViewById(R.id.textView_Event_Start_Date);
        mEventEndDate = (AppCompatTextView) rootView.findViewById(R.id.textView_Event_End_Date);
        mEventAddress = (AppCompatTextView) rootView.findViewById(R.id.textView_Event_Address);
        mEventCity = (AppCompatTextView) rootView.findViewById(R.id.textView_Event_City);
        mEventCountry = (AppCompatTextView) rootView.findViewById(R.id.textView_Event_Country);
        mEventLanguage = (AppCompatTextView) rootView.findViewById(R.id.textView_Event_Language);
        mEventAdditionalInfo = (AppCompatTextView) rootView.findViewById(R.id.textView_Event_Additional_Info);
        mEventFullDescription = (AppCompatTextView) rootView.findViewById(R.id.textView_Event_Full_Description);
        mOverlayView = rootView.findViewById(R.id.overlay);
        mScrollView = (ObservableScrollView) rootView.findViewById(R.id.scroll);
        mFab = (ActionButton)rootView.findViewById(R.id.fab);
        mMapIcon = (ImageView) rootView.findViewById(R.id.image_view_map_icon);
        changeIconColor();

        mButtonAddToCalendar = (AppCompatTextView) rootView.findViewById(R.id.button_add_to_calendar);
        mButtonAddToCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mAddedToCalendar) {
                    mService.executeAction(BaseService.ACTIONS.GET_CALENDARS, mBindingKey);
                }
                else {
                    try {
                        JSONObject eventArray = JSONSharedPreferences.loadJSONObject(getActivity(),
                                getActivity().getApplicationInfo().name, JSONSharedPreferences.KEY_CALENDAR);
                        JSONObject calendarData = eventArray.getJSONObject(mEvent.getObjectID());
                        mService.executeAction(BaseService.ACTIONS.REMOVE_EVENT_FROM_CALENDAR, getBindingKey(),
                                /*calendarData.getInt(CoreConstants.CALENDAR_SELF_ID),*/
                                calendarData.getLong(CoreConstants.CALENDAR_EVENT_ID));
                    }
                    catch (JSONException e) {
                        Logger.e("Problems with the internal event info while trying to remove the event", e);
                    }
                }
            }
        });

        rootView.findViewById(R.id.button_emergency_shared_preferences_killer).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        JSONSharedPreferences.remove(getActivity(), getActivity().getApplicationInfo().name,
                                JSONSharedPreferences.KEY_CALENDAR);
                    }
                }
        );
    }

    private void setCalendarButtonText() {
        // TODO: Pass this button stuff through somebody with better UI ideas
        try {
            JSONObject eventArray = JSONSharedPreferences.loadJSONObject(getActivity(),
                    getActivity().getApplicationInfo().name, JSONSharedPreferences.KEY_CALENDAR);
            if (eventArray.has(mEvent.getObjectID())) {
                mButtonAddToCalendar.setText(getString(R.string.button_remove_from_calendar));
                mAddedToCalendar = true;
            }
            else {
                mButtonAddToCalendar.setText(getString(R.string.button_add_to_calendar));
                mAddedToCalendar = false;
            }
        }
        catch (JSONException e) {
            Logger.e("Problems trying to get the event local info", e);
            mButtonAddToCalendar.setVisibility(View.GONE);
        }
    }

    @Override
    public String getTitle() {
        return "Description";
    }

    private void loadEventDescription() {
        mEventTitle.setText(mEvent.getTitle());
        if (mEvent.getEventLogo() != null) {
            mEventImage.setImageBitmap(ConvertImage.convertByteToBitmap(mEvent.getEventLogo()));
        } else {
            mEventImage.setImageResource(R.mipmap.placeholder);
        }
        mEventStartDate.setText(CustomDateFormat.getDate(mEvent.getStartDate(), getActivity()));
        mEventEndDate.setText(CustomDateFormat.getDate(mEvent.getEndDate(), getActivity()));
        setCalendarButtonText();
        mEventAddress.setText(mEvent.getAddress());
        mEventCity.setText(mEvent.getCity());
        mEventCountry.setText(mEvent.getCountry());
        mEventLanguage.setText(mEvent.getLanguage());
        if (mEvent.getAdditionalInfo() != null && !mEvent.getAdditionalInfo().equals("")) {
            mEventAdditionalInfo.setText(mEvent.getAdditionalInfo());
        } else {
            mEventAdditionalInfo.setText("-");
        }
        mEventFullDescription.setText(mEvent.getFullDescription());
        if (mEvent.getCoordinates() != null) {
            prepareMapIconButton();
        } else {
            mMapIcon.setVisibility(View.GONE);
        }
    }


    @Override
    public void onStartAction(BaseService.ACTIONS theAction) {
        showProgressOverlay();
    }

    @Override
    public void onFinishAction(BaseService.ACTIONS theAction, Object result) {
        hideUtilsAndShowContentOverlay();
        if (theAction == BaseService.ACTIONS.GET_CALENDARS) {
            showCalendarList(result);
        }
        if (theAction == BaseService.ACTIONS.ADD_EVENT_TO_CALENDAR) {
            JSONObject eventsArray;
            try {
                eventsArray = JSONSharedPreferences.loadJSONObject(getActivity(),
                        getActivity().getApplicationInfo().name, JSONSharedPreferences.KEY_CALENDAR);

                JSONObject calendarData = new JSONObject();
                calendarData.put(CoreConstants.CALENDAR_SELF_ID, mService.getNCalendar());
                calendarData.put(CoreConstants.CALENDAR_EVENT_ID, result);
                calendarData.put(CoreConstants.CALENDAR_EVENT_LAST_UPDATE,
                        CustomDateFormat.getCompleteDate(mEvent.getUpdatedAt(), getActivity()));
                eventsArray.put(mEvent.getObjectID(), calendarData);
                JSONSharedPreferences.saveJSONObject(getActivity(), getActivity().getApplicationInfo().name,
                        JSONSharedPreferences.KEY_CALENDAR, eventsArray);

                setCalendarButtonText();

                //mAddedToCalendar = true;
            } catch (JSONException e) {
                Logger.e("Error trying to get this event's calendar id", e);
                mButtonAddToCalendar.setEnabled(false);
            }
        }
        if (theAction == BaseService.ACTIONS.REMOVE_EVENT_FROM_CALENDAR) {
            JSONObject eventsArray;
            try {
                eventsArray = JSONSharedPreferences.loadJSONObject(getActivity(),
                        getActivity().getApplicationInfo().name, JSONSharedPreferences.KEY_CALENDAR);

                eventsArray.remove(mEvent.getObjectID());
                JSONSharedPreferences.saveJSONObject(getActivity(), getActivity().getApplicationInfo().name,
                        JSONSharedPreferences.KEY_CALENDAR, eventsArray);

                setCalendarButtonText();

                //mAddedToCalendar = false;
            } catch (JSONException e) {
                Logger.e("Error trying to get this event's calendar id", e);
                mButtonAddToCalendar.setEnabled(false);
            }
        }
    }

    private void showCalendarList(Object result) {
        new MaterialDialog.Builder(getActivity())
                .title("Choose calendar")
                .titleColorRes(R.color.globant_green_dark)
                .items((CharSequence[]) result)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                        mService.setNCalendar(i);
                        mService.executeAction(BaseService.ACTIONS.ADD_EVENT_TO_CALENDAR, getBindingKey(), mEvent);
                    }
                })
                .show();
    }

    @Override
    public void onFailAction(BaseService.ACTIONS theAction, Exception e) {
        showErrorOverlay();
    }

    @Override
    public void onPauseFragment() {
    }

    @Override
    public void onResumeFragment() {
    }
}
