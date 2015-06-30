package com.globant.eventscorelib.baseFragments;


import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.globant.eventscorelib.R;
import com.globant.eventscorelib.baseActivities.BaseEventDetailPagerActivity;
import com.globant.eventscorelib.baseActivities.BaseMapEventDescriptionActivity;
import com.globant.eventscorelib.baseActivities.BasePagerActivity;
import com.globant.eventscorelib.baseActivities.BaseSubscriberActivity;
import com.globant.eventscorelib.baseComponents.BaseService;
import com.globant.eventscorelib.controllers.SharedPreferencesController;
import com.globant.eventscorelib.domainObjects.Event;
import com.globant.eventscorelib.domainObjects.Subscriber;
import com.globant.eventscorelib.utils.CoreConstants;
import com.globant.eventscorelib.utils.CustomDateFormat;
import com.globant.eventscorelib.utils.Logger;
import com.globant.eventscorelib.utils.ScrollChangeCallbacks;
import com.globant.eventscorelib.utils.PushNotifications;
import com.globant.eventscorelib.utils.SharingIntent;
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

    private final static int CODE_REQUEST_SUBSCRIBER = 1;

    private Subscriber mSubscriber;

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
                fabMargin, mToolbar, mOverlayView, mEventTitle, mEventImage, mFab, false, getActivity());
        mScrollView.setScrollViewCallbacks(scrollChangeCallbacks);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String subscriberEmail = SharedPreferencesController.getUserEmail(getActivity());
                mSubscriber = SharedPreferencesController.getSubscriberInformation(getActivity());
                //if (subscriberEmail.isEmpty()) {
                // TODO: See if it would be better to check the name
                if (mSubscriber.getEmail() == null) {
                //if (mSubscriber.getObjectID() == null) {
                    PushNotifications.subscribeToChannel("CH-" + mEvent.getObjectID());
                    Toast.makeText(getActivity(), R.string.need_info_for_subscription, Toast.LENGTH_LONG)
                            .show();
                    prepareBaseSubscriberActivity();
                } else

                {
                    checkPrevSubscription();
                    //subscribeToEvent();
                }
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
        //startActivity(intent);
        startActivityForResult(intent, CODE_REQUEST_SUBSCRIBER);
        getActivity().overridePendingTransition(R.anim.right_in, R.anim.nothing);
    }

    private void checkPrevSubscription() {
        String subscriberId = SharedPreferencesController.getUserId(getActivity());
        //String subscriberEmail = SharedPreferencesController.getUserEmail(getActivity());
        mService.executeAction(BaseService.ACTIONS.IS_SUBSCRIBED, getBindingKey(), subscriberId, mEvent.getObjectID());
    }

    private void subscribeToEvent() {
        new MaterialDialog.Builder(getActivity())
                .title(R.string.dialog_question_subscribe_event)
                .titleColorRes(R.color.globant_green_dark)
                .positiveText(R.string.yes)
                .negativeText(R.string.no)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        // TODO: First save the subscriber
                        //mService.executeAction(BaseService.ACTIONS.SUBSCRIBER_EXISTS, getBindingKey(), mSubscriber.getEmail());
                        mService.executeAction(BaseService.ACTIONS.SUBSCRIBER_EXISTS, getBindingKey(), mSubscriber.getObjectID());
//                        mService.executeAction(BaseService.ACTIONS.EVENTS_TO_SUBSCRIBER_CREATE, getBindingKey(),
//                                mSubscriber, mEvent.getObjectID());
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                    }
                }).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_REQUEST_SUBSCRIBER) {
            if (resultCode == Activity.RESULT_OK) {
                mSubscriber = data.getParcelableExtra(CoreConstants.EXTRA_DATA_SUBSCRIBER);
                checkPrevSubscription();
                //subscribeToEvent();
            }
        }
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
        mFab = (ActionButton) rootView.findViewById(R.id.fab);
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
                        JSONObject eventArray = SharedPreferencesController.loadJSONObject(getActivity(),
                                getActivity().getApplicationInfo().name, SharedPreferencesController.KEY_CALENDAR);
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
                        SharedPreferencesController.removeJSON(getActivity(), getActivity().getApplicationInfo().name,
                                SharedPreferencesController.KEY_CALENDAR);
                    }
                }
        );
    }

    private void setCalendarButtonText() {
        try {
            JSONObject eventArray = SharedPreferencesController.loadJSONObject(getActivity(),
                    getActivity().getApplicationInfo().name, SharedPreferencesController.KEY_CALENDAR);
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

    protected void loadEventDescription() {
        mEventTitle.setText(mEvent.getTitle());
        if (mEvent.getEventLogo() != null) {
            mEventImage.setImageBitmap(mEvent.getEventLogo());
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        boolean handled = false;

        if (id == R.id.action_share) {
            String shortDescription = mEvent.getShortDescription() +
                    " - " + CustomDateFormat.getCompleteDate(mEvent.getStartDate(), getActivity()) + " - " + mEvent.getCity() + ", " + mEvent.getCountry();
            SharingIntent.showList(getActivity(), mEvent.getTitle(), shortDescription);
            handled = true;
        }
        if (!handled) {
            handled = super.onOptionsItemSelected(item);
        }
        return handled;
    }


    @Override
    public void onStartAction(BaseService.ACTIONS theAction) {
        showProgressOverlay();
    }

    @Override
    public void onFinishAction(BaseService.ACTIONS theAction, Object result) {
        //Intent intent;
        hideUtilsAndShowContentOverlay();
        switch (theAction) {
            case GET_CALENDARS: {
                showCalendarList(result);
                break;
            }
            case ADD_EVENT_TO_CALENDAR: {
                if (SharedPreferencesController.addEventJsonInfo(getActivity(), mService.getNCalendar(), (Long) result, mEvent)) {
                    setCalendarButtonText();
                } else {
                    mButtonAddToCalendar.setEnabled(false);
                }
                break;
            }
            case REMOVE_EVENT_FROM_CALENDAR: {
                if (SharedPreferencesController.removeEventJsonInfo(getActivity(), mEvent)) {
                    setCalendarButtonText();
                } else {
                    mButtonAddToCalendar.setEnabled(false);
                }
                break;
            }
            case IS_SUBSCRIBED: {
                if ((Boolean) result) {
                    Toast.makeText(getActivity(), getString(R.string.already_subscribed), Toast.LENGTH_SHORT).show();
                } else {
                    subscribeToEvent();
                }
                break;
            }
            case SUBSCRIBER_EXISTS:
                //if (result.equals("")) {
                if (!(Boolean)result) {
                    mService.executeAction(BaseService.ACTIONS.SUBSCRIBER_CREATE, getBindingKey(), mSubscriber);
                } else {
                    //mSubscriber.setObjectID((String) result);
                    mService.executeAction(BaseService.ACTIONS.SUBSCRIBER_UPDATE, getBindingKey(), mSubscriber);
                }
                break;
            case SUBSCRIBER_CREATE:
                mSubscriber.setObjectID((String) result);
                SharedPreferencesController.setSubscriberInformation(mSubscriber, getActivity());
                //mService.executeAction(BaseService.ACTIONS.IS_SUBSCRIBED, getBindingKey(), result, mEventId);
//                intent = new Intent();
//                intent.putExtra(CoreConstants.EXTRA_DATA_SUBSCRIBER, mSubscriber);
//                getActivity().setResult(Activity.RESULT_OK, intent);
//                getActivity().finish();
                mService.executeAction(BaseService.ACTIONS.EVENTS_TO_SUBSCRIBER_CREATE, getBindingKey(),
                        mSubscriber, mEvent.getObjectID());
                break;
            case SUBSCRIBER_UPDATE:
//                if (getActivity().getIntent().getBooleanExtra(CoreConstants.FIELD_CHECK_IN, false)) {
//                    mService.executeAction(BaseService.ACTIONS.IS_SUBSCRIBED, getBindingKey(), result, mEventId);
//                } else {
                //Toast.makeText(getActivity(), getResources().getString(R.string.profile_saved), Toast.LENGTH_SHORT).show();
//                intent = new Intent();
//                intent.putExtra(CoreConstants.EXTRA_DATA_SUBSCRIBER, mSubscriber);
//                getActivity().setResult(Activity.RESULT_OK, intent);
//                getActivity().finish();
                mService.executeAction(BaseService.ACTIONS.EVENTS_TO_SUBSCRIBER_CREATE, getBindingKey(),
                        mSubscriber, mEvent.getObjectID());
                break;
//                }
            case EVENTS_TO_SUBSCRIBER_CREATE:
                Toast.makeText(getActivity(), getString(R.string.have_been_subscribed), Toast.LENGTH_SHORT).show();
                PushNotifications.subscribeToChannel("SUB-" + mEvent.getObjectID());
                PushNotifications.subscribeToChannel("SUB-" + mEvent.getObjectID() + "-" + mSubscriber.getObjectID());
                //getActivity().finish();
                break;
        }
    }

    private void showCalendarList(Object result) {
        new MaterialDialog.Builder(getActivity())
                .title(R.string.choose_calendar)
                .titleColorRes(R.color.globant_green_dark)
                .items((CharSequence[]) result)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                        mService.setNCalendar(mService.getCalendarIdFromOrder(i));
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
