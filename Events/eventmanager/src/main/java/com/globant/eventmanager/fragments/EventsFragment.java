package com.globant.eventmanager.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.globant.eventmanager.R;
import com.globant.eventmanager.activities.EventsManagerPagerActivity;
import com.globant.eventmanager.activities.MapEventCreationActivity;
import com.globant.eventscorelib.baseActivities.BaseActivity;
import com.globant.eventscorelib.baseActivities.BasePagerActivity;
import com.globant.eventscorelib.baseComponents.BaseService;
import com.globant.eventscorelib.baseFragments.BaseFragment;
import com.globant.eventscorelib.domainObjects.Event;
import com.globant.eventscorelib.utils.ConvertImage;
import com.globant.eventscorelib.utils.CoreConstants;
import com.globant.eventscorelib.utils.ErrorLabelLayout;
import com.globant.eventscorelib.utils.ScrollChangeCallbacks;
import com.google.android.gms.maps.model.LatLng;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.software.shell.fab.ActionButton;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by david.burgos
 */
public class EventsFragment extends BaseFragment implements BaseService.ActionListener, BasePagerActivity.FragmentLifecycle {

    private Event mEvent;
    protected LatLng mLatLng;
    private String mBindingKey;

    private View mToolbar;
    private View mOverlayView;
    private AppCompatTextView mEventTitle;
    private ImageView mMapIcon;
    private ObservableScrollView mScrollView;
    private ImageView mPhotoEvent;
    private ActionButton mFloatingActionButtonPhoto;
    private AppCompatEditText mEditTextTitle;
    private AppCompatEditText mEditTextFullDescription;
    private AppCompatEditText mEditTextShortDescription;
    private AppCompatEditText mEditTextAdditionalInfo;
    private AppCompatSpinner mSpinnerCategory;
    private AppCompatSpinner mSpinnerPublic;
    private AppCompatEditText mEditTextHashtag;
    private AppCompatEditText mEditTextLanguage;
    private AppCompatEditText mEditTextStartDate;
    private AppCompatEditText mEditTextStartTime;
    private AppCompatEditText mEditTextEndDate;
    private AppCompatEditText mEditTextEndTime;
    private AppCompatEditText mEditTextAddress;
    private AppCompatEditText mEditTextCountry;
    private AppCompatEditText mEditTextCity;

    private ImageView mIconTitle;
    private ImageView mIconFullDescription;
    private ImageView mIconShortDescription;
    private ImageView mIconAdditionalInfo;
    private ImageView mIconCategory;
    private ImageView mIconPublic;
    private ImageView mIconHashtag;
    private ImageView mIconLanguage;
    private ImageView mIconStartDate;
    private ImageView mIconEndDate;
    private ImageView mIconStartTime;
    private ImageView mIconEndTime;
    private ImageView mIconAddress;
    private ImageView mIconCountry;
    private ImageView mIconCity;
    private ImageView mIconToChange;
    private Drawable mDrawableToApply;

    private TimePickerDialog mStartTimePicker;
    private TimePickerDialog mEndTimePicker;
    private DatePickerDialog mStartDatePicker;
    private DatePickerDialog mEndDatePicker;
    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat TimeFormatter;
    private Calendar mStartDate;
    private Calendar mEndDate;

    ErrorLabelLayout mErrorLabelLayoutTitle;
    ErrorLabelLayout mErrorLabelLayoutFullDescription;
    ErrorLabelLayout mErrorLabelLayoutShortDescription;
    ErrorLabelLayout mErrorLabelLayoutAdditionalInfo;
    ErrorLabelLayout mErrorLabelLayoutCategory;
    ErrorLabelLayout mErrorLabelLayoutPublic;
    ErrorLabelLayout mErrorLabelLayoutHashtag;
    ErrorLabelLayout mErrorLabelLayoutLanguage;
    ErrorLabelLayout mErrorLabelLayoutStartDate;
    ErrorLabelLayout mErrorLabelLayoutStartTime;
    ErrorLabelLayout mErrorLabelLayoutEndDate;
    ErrorLabelLayout mErrorLabelLayoutEndTime;
    ErrorLabelLayout mErrorLabelLayoutAddress;
    ErrorLabelLayout mErrorLabelLayoutCity;
    ErrorLabelLayout mErrorLabelLayoutCountry;
    ErrorLabelLayout mErrorLabelLayout;

    public EventsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_events, container, false);
        wireUpViews(rootView);
        setUpSpinners();
        prepareImageButton();
        setOnFocusListeners();
        setDateTimeField();

        mEvent = EventsManagerPagerActivity.getInstance().getEvent();
        populateInfo(mEvent);

        hideUtilsAndShowContentOverlay();
        setHasOptionsMenu(true);
        setRetainInstance(true);
        return rootView;
    }

    private void populateInfo(Event event){
        mEventTitle.setText(event.getTitle());
        mEditTextTitle.setText(event.getTitle());
        mEditTextFullDescription.setText(event.getFullDescription());
        mEditTextShortDescription.setText(event.getShortDescription());
        mEditTextAdditionalInfo.setText(event.getAdditionalInfo());
        String[] category = getResources().getStringArray(R.array.category_entries);
        int CategoryIndex = Arrays.asList(category).indexOf(event.getCategory());
        if (CategoryIndex>=0){
            mSpinnerCategory.setSelection(CategoryIndex);
        }
        mSpinnerPublic.setSelection(event.isPublic() ? 0 : 1);
        mEditTextHashtag.setText(event.getHashtag());
        mEditTextLanguage.setText(event.getLanguage());
        if(event.getStartDate() != null) {
            mEditTextStartDate.setText(dateFormatter.format(event.getStartDate()));
            mEditTextStartTime.setText(TimeFormatter.format(event.getStartDate()));
            mStartDate.setTime(event.getStartDate());
        }
        if(event.getEndDate() != null) {
            mEditTextEndDate.setText(dateFormatter.format(event.getEndDate()));
            mEditTextEndTime.setText(TimeFormatter.format(event.getEndDate()));
            mEndDate.setTime(event.getEndDate());
        }
        mEditTextAddress.setText(event.getAddress());
        mEditTextCountry.setText(event.getCountry());
        mEditTextCity.setText(event.getCity());
        mLatLng = event.getCoordinates();

        if (event.getEventLogo()!= null){
            mPhotoEvent.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mPhotoEvent.setImageBitmap(ConvertImage.convertByteToBitmap(mEvent.getEventLogo()));
        }else {
            mPhotoEvent.setScaleType(ImageView.ScaleType.CENTER);
            mPhotoEvent.setImageResource(R.mipmap.ic_insert_photo);
        }
    }

    private void retrieveInfo() {
        if (mEvent != null) {
            mEvent.setTitle(mEditTextTitle.getText().toString());
            mEvent.setFullDescription(mEditTextFullDescription.getText().toString());
            mEvent.setShortDescription(mEditTextShortDescription.getText().toString());
            mEvent.setAdditionalInfo(mEditTextAdditionalInfo.getText().toString());
            mEvent.setHashtag(mEditTextHashtag.getText().toString());
            mEvent.setLanguage(mEditTextLanguage.getText().toString());
            if(!mEditTextEndDate.getText().toString().isEmpty())
            mEvent.setEndDate(mEndDate.getTime());
            if(!mEditTextStartDate.getText().toString().isEmpty())
                mEvent.setStartDate(mStartDate.getTime());
            mEvent.setCategory(mSpinnerCategory.getSelectedItem().toString());
            mEvent.setCity(mEditTextCity.getText().toString());
            mEvent.setAddress(mEditTextAddress.getText().toString());
            mEvent.setPublic(mSpinnerPublic.getSelectedItemPosition() == 0);
            mEvent.setCountry(mEditTextCountry.getText().toString());
            mEvent.setEventLogo(ConvertImage.convertDrawableToByteArray(mPhotoEvent.getDrawable()));
            mEvent.setIcon(null);
            mEvent.setCoordinates(mLatLng);

        }
    }

    private void setOnFocusListeners() {
        mEditTextTitle.setOnFocusChangeListener(editTextFocus);
        mEditTextFullDescription.setOnFocusChangeListener(editTextFocus);
        mEditTextShortDescription.setOnFocusChangeListener(editTextFocus);
        mEditTextAdditionalInfo.setOnFocusChangeListener(editTextFocus);
        mEditTextHashtag.setOnFocusChangeListener(editTextFocus);
        mEditTextLanguage.setOnFocusChangeListener(editTextFocus);
        mEditTextStartDate.setOnFocusChangeListener(editTextFocus);
        mEditTextStartTime.setOnFocusChangeListener(editTextFocus);
        mEditTextEndDate.setOnFocusChangeListener(editTextFocus);
        mEditTextEndTime.setOnFocusChangeListener(editTextFocus);
        mEditTextAddress.setOnFocusChangeListener(editTextFocus);
        mEditTextCountry.setOnFocusChangeListener(editTextFocus);
        mEditTextCity.setOnFocusChangeListener(editTextFocus);
    }

    private View.OnFocusChangeListener editTextFocus =  new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            onViewFocusChange(v, hasFocus);
        }
    };

    public void onViewFocusChange(View view, boolean gainFocus) {

        getIconToTint(view);

        //onFocus
        if (gainFocus) {
            mDrawableToApply= DrawableCompat.wrap(mDrawableToApply);
            DrawableCompat.setTint(mDrawableToApply, getResources().getColor(com.globant.eventscorelib.R.color.ambar));
            mDrawableToApply= DrawableCompat.unwrap(mDrawableToApply);
            mIconToChange.setImageDrawable(mDrawableToApply);
            mErrorLabelLayout.clearError();
        }
        //onBlur
        else {
            mDrawableToApply= DrawableCompat.wrap(mDrawableToApply);
            DrawableCompat.setTint(mDrawableToApply, getResources().getColor(com.globant.eventscorelib.R.color.grey_icon));
            mDrawableToApply= DrawableCompat.unwrap(mDrawableToApply);
            mIconToChange.setImageDrawable(mDrawableToApply);
        }
    }

    private void getIconToTint(View view) {
        int id = view.getId();
        //noinspection SimplifiableIfStatement
        if (id == (R.id.edit_text_title)) {
            mIconToChange = mIconTitle;
            mDrawableToApply = getResources().getDrawable(R.mipmap.ic_event_title);
            mErrorLabelLayout = mErrorLabelLayoutTitle;

        }
        else if (id== (R.id.edit_text_full_description)){
            mIconToChange= mIconFullDescription;
            mDrawableToApply=getResources().getDrawable(R.mipmap.ic_description);
            mErrorLabelLayout= mErrorLabelLayoutFullDescription;

        }
        else if (id== (R.id.edit_text_short_description)){
            mIconToChange= mIconShortDescription;
            mDrawableToApply=getResources().getDrawable(R.mipmap.ic_short_description);
            mErrorLabelLayout= mErrorLabelLayoutShortDescription;

        }
        else if (id== (R.id.edit_text_additional_info)){
            mIconToChange= mIconAdditionalInfo;
            mDrawableToApply=getResources().getDrawable(R.mipmap.ic_additional_info);
            mErrorLabelLayout= mErrorLabelLayoutAdditionalInfo;
        }
        else if (id== (R.id.category_spinner)){
            mIconToChange= mIconCategory;
            switch (((Spinner)view).getSelectedItemPosition()){
                case 0:
                    mDrawableToApply=getResources().getDrawable(R.mipmap.ic_social);
                    break;
                case 1:
                    mDrawableToApply=getResources().getDrawable(R.mipmap.ic_informative);
                    break;
                case 2:
                    mDrawableToApply=getResources().getDrawable(R.mipmap.ic_technical);
                    break;
            }

            mErrorLabelLayout= mErrorLabelLayoutCategory;
        }
        else if (id== (R.id.public_spinner)){
            mIconToChange= mIconPublic;
            switch (((Spinner)view).getSelectedItemPosition()){
                case 0:
                    mDrawableToApply=getResources().getDrawable(R.mipmap.ic_public);
                    break;
                case 1:
                    mDrawableToApply=getResources().getDrawable(R.mipmap.ic_private);
                    break;
            }
            mErrorLabelLayout= mErrorLabelLayoutPublic;
        }
        else if (id== (R.id.edit_text_hashtag)){
            mIconToChange= mIconHashtag;
            mDrawableToApply=getResources().getDrawable(R.mipmap.ic_hashtag);
            mErrorLabelLayout= mErrorLabelLayoutHashtag;
        }
        else if (id== (R.id.edit_text_language)){
            mIconToChange= mIconLanguage;
            mDrawableToApply=getResources().getDrawable(R.mipmap.ic_language);
            mErrorLabelLayout=mErrorLabelLayoutLanguage;
        }
        else if (id== (R.id.edit_text_start_date)){
            mIconToChange= mIconStartDate;
            mDrawableToApply=getResources().getDrawable(R.mipmap.ic_event_start_date);
            mErrorLabelLayout= mErrorLabelLayoutStartDate;
        }
        else if (id== (R.id.edit_text_start_time)){
            mIconToChange= mIconStartTime;
            mDrawableToApply=getResources().getDrawable(R.mipmap.ic_start_time);
            mErrorLabelLayout= mErrorLabelLayoutStartTime;
        }
        else if (id== (R.id.edit_text_end_date)){
            mIconToChange= mIconEndDate;
            mDrawableToApply=getResources().getDrawable(R.mipmap.ic_event_end_date);
            mErrorLabelLayout= mErrorLabelLayoutEndDate;
        }
        else if (id== (R.id.edit_text_end_time)){
            mIconToChange= mIconEndTime;
            mDrawableToApply=getResources().getDrawable(R.mipmap.ic_end_time);
            mErrorLabelLayout= mErrorLabelLayoutEndTime;
        }
        else if (id== (R.id.edit_text_address)){
            mIconToChange= mIconAddress;
            mDrawableToApply=getResources().getDrawable(R.mipmap.ic_location);
            mErrorLabelLayout=mErrorLabelLayoutAddress;
        }
        else if (id== (com.globant.eventscorelib.R.id.edit_text_country)){
            mIconToChange=mIconCountry;
            mDrawableToApply=getResources().getDrawable(com.globant.eventscorelib.R.mipmap.ic_country);
            mErrorLabelLayout=mErrorLabelLayoutCountry;
        }
        else if (id== (com.globant.eventscorelib.R.id.edit_text_city)){
            mIconToChange=mIconCity;
            mDrawableToApply=getResources().getDrawable(R.mipmap.ic_city);
            mErrorLabelLayout=mErrorLabelLayoutCity;
        }

    }

    private void wireUpViews(View rootView) {

        mToolbar = rootView.findViewById(R.id.events_toolbar);
        mOverlayView = rootView.findViewById(R.id.events_overlay);
        mScrollView = (ObservableScrollView) rootView.findViewById(R.id.event_scroll);
        mEventTitle = (AppCompatTextView) rootView.findViewById(R.id.title);
        mMapIcon = (ImageView) rootView.findViewById(R.id.image_button_map);

        mEditTextTitle =(AppCompatEditText)rootView.findViewById(R.id.edit_text_title);
        mEditTextFullDescription =(AppCompatEditText)rootView.findViewById(R.id.edit_text_full_description);
        mEditTextShortDescription =(AppCompatEditText)rootView.findViewById(R.id.edit_text_short_description);
        mEditTextAdditionalInfo =(AppCompatEditText)rootView.findViewById(R.id.edit_text_additional_info);
        mSpinnerCategory =(AppCompatSpinner)rootView.findViewById(R.id.category_spinner);
        mSpinnerPublic =(AppCompatSpinner)rootView.findViewById(R.id.public_spinner);
        mEditTextHashtag =(AppCompatEditText)rootView.findViewById(R.id.edit_text_hashtag);
        mEditTextLanguage =(AppCompatEditText)rootView.findViewById(R.id.edit_text_language);
        mEditTextStartDate =(AppCompatEditText)rootView.findViewById(R.id.edit_text_start_date);
        mEditTextStartDate.setInputType(InputType.TYPE_NULL);
        mEditTextStartTime =(AppCompatEditText)rootView.findViewById(R.id.edit_text_start_time);
        mEditTextStartTime.setInputType(InputType.TYPE_NULL);
        mEditTextEndDate =(AppCompatEditText)rootView.findViewById(R.id.edit_text_end_date);
        mEditTextEndDate.setInputType(InputType.TYPE_NULL);
        mEditTextEndTime =(AppCompatEditText)rootView.findViewById(R.id.edit_text_end_time);
        mEditTextEndTime.setInputType(InputType.TYPE_NULL);
        mEditTextAddress =(AppCompatEditText)rootView.findViewById(R.id.edit_text_address);
        mEditTextCountry=(AppCompatEditText)rootView.findViewById(R.id.edit_text_country);
        mEditTextCity=(AppCompatEditText)rootView.findViewById(R.id.edit_text_city);

        mFloatingActionButtonPhoto=(ActionButton)rootView.findViewById(R.id.event_fab);

        mPhotoEvent =(ImageView)rootView.findViewById(R.id.event_header);
        mIconTitle =(ImageView)rootView.findViewById(R.id.icon_title);
        mIconFullDescription =(ImageView)rootView.findViewById(R.id.icon_full_description);
        mIconShortDescription =(ImageView)rootView.findViewById(R.id.icon_short_description);
        mIconAdditionalInfo =(ImageView)rootView.findViewById(R.id.icon_additional_info);
        mIconCategory =(ImageView)rootView.findViewById(R.id.icon_category);
        mIconPublic =(ImageView)rootView.findViewById(R.id.icon_public);
        mIconHashtag =(ImageView)rootView.findViewById(R.id.icon_hashtag);
        mIconLanguage =(ImageView)rootView.findViewById(R.id.icon_language);
        mIconStartDate =(ImageView)rootView.findViewById(R.id.icon_start_date);
        mIconEndDate =(ImageView)rootView.findViewById(R.id.icon_end_date);
        mIconStartTime =(ImageView)rootView.findViewById(R.id.icon_start_time);
        mIconEndTime =(ImageView)rootView.findViewById(R.id.icon_end_time);
        mIconAddress =(ImageView)rootView.findViewById(R.id.icon_address);
        mIconCountry=(ImageView)rootView.findViewById(R.id.icon_country);
        mIconCity=(ImageView)rootView.findViewById(R.id.icon_city);

        mErrorLabelLayoutTitle = (ErrorLabelLayout) rootView.findViewById(R.id.name_error_layout_title);
        mErrorLabelLayoutFullDescription = (ErrorLabelLayout) rootView.findViewById(R.id.name_error_layout_full_description);
        mErrorLabelLayoutShortDescription = (ErrorLabelLayout) rootView.findViewById(R.id.name_error_layout_short_description);
        mErrorLabelLayoutAdditionalInfo = (ErrorLabelLayout) rootView.findViewById(R.id.name_error_layout_additional_info);
        mErrorLabelLayoutCategory = (ErrorLabelLayout) rootView.findViewById(R.id.name_error_layout_category);
        mErrorLabelLayoutPublic = (ErrorLabelLayout) rootView.findViewById(R.id.name_error_layout_public);
        mErrorLabelLayoutHashtag = (ErrorLabelLayout) rootView.findViewById(R.id.name_error_layout_hashtag);
        mErrorLabelLayoutLanguage = (ErrorLabelLayout) rootView.findViewById(R.id.name_error_layout_language);
        mErrorLabelLayoutStartDate = (ErrorLabelLayout) rootView.findViewById(R.id.name_error_layout_start_date);
        mErrorLabelLayoutEndDate = (ErrorLabelLayout) rootView.findViewById(R.id.name_error_layout_end_date);
        mErrorLabelLayoutStartTime = (ErrorLabelLayout) rootView.findViewById(R.id.name_error_layout_start_time);
        mErrorLabelLayoutEndTime = (ErrorLabelLayout) rootView.findViewById(R.id.name_error_layout_end_time);
        mErrorLabelLayoutAddress = (ErrorLabelLayout) rootView.findViewById(R.id.name_error_address);
        mErrorLabelLayoutCity = (ErrorLabelLayout) rootView.findViewById(R.id.name_error_city);
        mErrorLabelLayoutCountry = (ErrorLabelLayout) rootView.findViewById(R.id.name_error_country);
    }

    private void setUpSpinners(){

        String[] categoryInfo = getResources().getStringArray(R.array.category_entries);
        ArrayAdapter<String> category_adapter = new ArrayAdapter<>(getActivity(),  R.layout.simple_spinner_item, categoryInfo);
        category_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerCategory.setAdapter(category_adapter);
        mSpinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        mIconCategory.setImageResource(R.mipmap.ic_social);
                        break;
                    case 1:
                        mIconCategory.setImageResource(R.mipmap.ic_informative);
                        break;
                    case 2:
                        mIconCategory.setImageResource(R.mipmap.ic_technical);
                        break;
                }
                if (!mSpinnerCategory.isDirty())
                onViewFocusChange(mSpinnerCategory, true);
                else
                onViewFocusChange(mSpinnerCategory, false);
                mSpinnerCategory.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        mSpinnerCategory.setFocusableInTouchMode(true);
        mSpinnerPublic.setFocusableInTouchMode(true);

        String[] publicInfo = getResources().getStringArray(R.array.public_entries);
        ArrayAdapter<String> public_adapter = new ArrayAdapter<>(getActivity(), R.layout.simple_spinner_item, publicInfo);

        public_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerPublic.setAdapter(public_adapter);
        mSpinnerPublic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSpinnerPublic.setSelection(position);
                switch (position) {
                    case 0:
                        mIconPublic.setImageResource(R.mipmap.ic_public);
                        break;
                    case 1:
                        mIconPublic.setImageResource(R.mipmap.ic_private);
                        break;
                }
                if (!mSpinnerPublic.isDirty())
                    onViewFocusChange(mSpinnerPublic, true);
                else
                    onViewFocusChange(mSpinnerPublic, false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
            mSpinnerCategory.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    onViewFocusChange(v, hasFocus);
                    if (hasFocus && mSpinnerCategory.isDirty()) mSpinnerCategory.performClick();
                }
            });
            mSpinnerPublic.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    onViewFocusChange(v, hasFocus);
                    if(hasFocus && mSpinnerPublic.isDirty()) mSpinnerPublic.performClick();
                }
            });
    }

    private void setDateTimeField() {
        // date pickers
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        TimeFormatter = new SimpleDateFormat("hh:mm a", Locale.US);

        mStartDate = Calendar.getInstance();
        mEndDate = Calendar.getInstance();

        mStartDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mStartDate.set(year, monthOfYear, dayOfMonth);
                mEditTextStartDate.setText(dateFormatter.format(mStartDate.getTime()));
            }

        },mStartDate.get(Calendar.YEAR), mStartDate.get(Calendar.MONTH), mStartDate.get(Calendar.DAY_OF_MONTH));

        mEndDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mEndDate.set(year, monthOfYear, dayOfMonth);
                mEditTextEndDate.setText(dateFormatter.format(mEndDate.getTime()));
            }

        },mEndDate.get(Calendar.YEAR), mEndDate.get(Calendar.MONTH), mEndDate.get(Calendar.DAY_OF_MONTH));

        mStartDatePicker.setTitle(R.string.edit_text_start_date_hint);
        mStartDatePicker.setIcon(R.mipmap.ic_event_start_date);
        mEndDatePicker.setTitle(R.string.edit_text_end_date_hint);
        mEndDatePicker.setIcon(R.mipmap.ic_event_end_date);

        mEditTextStartDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mEditTextStartDate.requestFocus();
                mStartDatePicker.show();
                return false;
            }
        });
        mEditTextEndDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mEditTextEndDate.requestFocus();
                mEndDatePicker.show();
                return false;
            }
        });

        // time pickers
        int startHour = mStartDate.get(Calendar.HOUR_OF_DAY);
        int StartMinute = mStartDate.get(Calendar.MINUTE);
        int EndHour = mEndDate.get(Calendar.HOUR_OF_DAY);
        int EndMinute = mEndDate.get(Calendar.MINUTE);

         mStartTimePicker = new TimePickerDialog(getActivity(),
                new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                    mStartDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    mStartDate.set(Calendar.MINUTE,minute);

                    mEditTextStartTime.setText(TimeFormatter.format(mStartDate.getTime()));
                }
               },
                startHour, StartMinute,
                DateFormat.is24HourFormat(getActivity()));

        mStartTimePicker.setTitle(R.string.edit_text_start_time_hint);
        mStartTimePicker.setIcon(R.mipmap.ic_start_time);

        mEndTimePicker = new TimePickerDialog(getActivity(),
                new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                    mEndDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    mEndDate.set(Calendar.MINUTE,minute);

                    mEditTextEndTime.setText(TimeFormatter.format(mEndDate.getTime()));
                }
               },
                EndHour, EndMinute,
                DateFormat.is24HourFormat(getActivity()));

        mEndTimePicker.setTitle(R.string.edit_text_end_time_hint);
        mEndTimePicker.setIcon(R.mipmap.ic_end_time);

        mEditTextEndTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mEditTextEndTime.requestFocus();
                mEndTimePicker.show();
                return false;
            }
        });

        mEditTextStartTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mEditTextStartTime.requestFocus();
                mStartTimePicker.show();
                return false;
            }
        });

    }

    @Override
    public String getTitle() {
        return getResources().getString(R.string.title_activity_event_detail);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case CoreConstants.PICTURE_SELECTION_REQUEST:
                if (resultCode == Activity.RESULT_OK){
                    Uri targetUri = data.getData();
                    Bitmap bitmap;
                    try {
                        bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(targetUri));
                        if (bitmap != null) {
                            mPhotoEvent.setImageBitmap(bitmap);
                            mPhotoEvent.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            mEvent.setEventLogo(ConvertImage.convertBitmapImageToByteArray(bitmap));
                        }
                    } catch (FileNotFoundException e) {
                        mPhotoEvent.setImageResource(R.mipmap.ic_insert_photo);
                        mPhotoEvent.setScaleType(ImageView.ScaleType.CENTER);
                        e.printStackTrace();
                    }
                }
                break;
            case CoreConstants.MAP_MANAGER_REQUEST:
                if (resultCode == Activity.RESULT_OK){
                    Address address = data.getExtras().getParcelable(CoreConstants.MAP_ADDRESS_INTENT);
                    if (address != null){
                        mEditTextCity.setText(address.getLocality());
                        mEditTextCountry.setText(address.getCountryName());
                        if(address.getMaxAddressLineIndex()>0)mEditTextAddress.setText(address.getAddressLine(0));
                        mLatLng = new LatLng(address.getLatitude(),address.getLongitude());
                    }
                }
                break;
        }
    }

    protected void prepareImageButton() {

        mMapIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapEventCreationActivity.class);
                intent.putExtra(CoreConstants.MAP_MARKER_POSITION_INTENT, mLatLng);
                startActivityForResult(intent, CoreConstants.MAP_MANAGER_REQUEST);
            }
        });

        mFloatingActionButtonPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), CoreConstants.PICTURE_SELECTION_REQUEST);

//                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(intent, CoreConstants.PICTURE_SELECTION_REQUEST);
            }
        });

        mEditTextTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mEventTitle.setText(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        int actionBarSize = getActionBarSize();
        int flexibleSpaceImageHeight = getResources().getDimensionPixelSize(com.globant.eventscorelib.R.dimen.flexible_space_image_height);
        int toolbarColor = getResources().getColor(com.globant.eventscorelib.R.color.globant_green);
        int flexibleSpaceShowFabOffset = getResources().getDimensionPixelSize(com.globant.eventscorelib.R.dimen.flexible_space_show_fab_offset);
        int fabMargin = getResources().getDimensionPixelSize(com.globant.eventscorelib.R.dimen.activity_horizontal_margin);
        ScrollChangeCallbacks scrollChangeCallbacks = new ScrollChangeCallbacks(actionBarSize, flexibleSpaceImageHeight, toolbarColor, flexibleSpaceShowFabOffset,
                fabMargin, mToolbar, mOverlayView, mEventTitle, mPhotoEvent, mFloatingActionButtonPhoto , false, getActivity());
        mScrollView.setScrollViewCallbacks(scrollChangeCallbacks);

        ViewHelper.setScaleX(mFloatingActionButtonPhoto, 0);
        ViewHelper.setScaleY(mFloatingActionButtonPhoto, 0);

        ScrollUtils.addOnGlobalLayoutListener(mScrollView, new Runnable() {
            @Override
            public void run() {
                mScrollView.scrollTo(0, 1);
                mScrollView.scrollTo(0, 0);
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBindingKey = this.getClass().getSimpleName();
        setHasOptionsMenu(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        retrieveInfo();
        EventsManagerPagerActivity.getInstance().setEvent(mEvent);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_events, menu);

        MenuItem item = menu.findItem(com.globant.eventmanager.R.id.events_action_delete);

        if (EventsManagerPagerActivity.mEventAction == EventsManagerPagerActivity.ActionType.EDIT_EVENT) {
            item.setVisible(true);
        } else {
            item.setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        boolean handled = false;

       // mEvent = new Event(); //TODO: erase after test
/*        mEvent.setTitle("Apero Urbano");
        mEvent.setShortDescription("Picnic, cerveza y comida!");
        mEvent.setFullDescription("evento realizado el ultimo viernes de cada mes para integrar la ciudad en diferentes actividades.");
        mEvent.setAddress("Parque de la presidenta");
        mEvent.setCity("Medellin");
        mEvent.setCountry("Colombia");
        mEvent.setAdditionalInfo("-");
        mEvent.setHashtag("#Apero");
        mEvent.setCategory("Social");
        mEvent.setPublic(true);
        mEvent.setLanguage("Spanglish");
        Calendar fecha = Calendar.getInstance();
        fecha.set(fecha.get(Calendar.YEAR)+1, fecha.get(Calendar.MONTH), fecha.get(Calendar.DAY_OF_MONTH),fecha.get(Calendar.HOUR_OF_DAY),30);
        mEvent.setStartDate(fecha.getTime());
        fecha.set(fecha.get(Calendar.YEAR), fecha.get(Calendar.MONTH), fecha.get(Calendar.DAY_OF_MONTH) + 1, fecha.get(Calendar.HOUR_OF_DAY), 30);
        mEvent.setEndDate(fecha.getTime());
        populateInfo(mEvent);*/

        if (id == com.globant.eventmanager.R.id.events_action_done) {
            Boolean savePreferences;
            savePreferences  = tintRequiredIconsAndShowError(mEditTextTitle);
            savePreferences &= tintRequiredIconsAndShowError(mEditTextFullDescription);
            savePreferences &= tintRequiredIconsAndShowError(mEditTextShortDescription);
            savePreferences &= tintRequiredIconsAndShowError(mEditTextHashtag);
            savePreferences &= tintRequiredIconsAndShowError(mEditTextLanguage);
            savePreferences &= tintRequiredIconsAndShowError(mEditTextStartDate);
            savePreferences &= tintRequiredIconsAndShowError(mEditTextStartTime);
            savePreferences &= tintRequiredIconsAndShowError(mEditTextEndDate);
            savePreferences &= tintRequiredIconsAndShowError(mEditTextEndTime);
            savePreferences &= tintRequiredIconsAndShowError(mEditTextAddress);
            savePreferences &= tintRequiredIconsAndShowError(mEditTextCountry);
            savePreferences &= tintRequiredIconsAndShowError(mEditTextCity);

            if (savePreferences){
                if(mStartDate.compareTo(mEndDate) != -1){
                    Toast.makeText(getActivity(),R.string.error_message_dates,Toast.LENGTH_LONG).show();

                    ImageView iconToChange= mIconEndDate;
                    Drawable mDrawableToApplyChanges=getResources().getDrawable(R.mipmap.ic_event_end_date);

                    mErrorLabelLayoutEndDate.setError(getString(R.string.error_message_change_dates));
                    mDrawableToApplyChanges= DrawableCompat.wrap(mDrawableToApplyChanges);
                    DrawableCompat.setTint(mDrawableToApplyChanges, getResources().getColor(com.globant.eventscorelib.R.color.red_error));
                    mDrawableToApplyChanges= DrawableCompat.unwrap(mDrawableToApplyChanges);
                    iconToChange.setImageDrawable(mDrawableToApplyChanges);

                    iconToChange= mIconEndTime;
                    mDrawableToApplyChanges=getResources().getDrawable(R.mipmap.ic_end_time);

                    mErrorLabelLayoutEndTime.setError("");
                    mDrawableToApplyChanges= DrawableCompat.wrap(mDrawableToApplyChanges);
                    DrawableCompat.setTint(mDrawableToApplyChanges, getResources().getColor(com.globant.eventscorelib.R.color.red_error));
                    mDrawableToApplyChanges= DrawableCompat.unwrap(mDrawableToApplyChanges);
                    iconToChange.setImageDrawable(mDrawableToApplyChanges);
                }
                else if(mPhotoEvent.getScaleType() == ImageView.ScaleType.CENTER) {
                    Toast.makeText(getActivity(),getString(R.string.missing_photo),Toast.LENGTH_SHORT).show();
                }else{
                    retrieveInfo();

                    switch (EventsManagerPagerActivity.mEventAction) {
                        case CREATE_EVENT:
                            mService.executeAction(BaseService.ACTIONS.EVENT_CREATE, getBindingKey(), mEvent);
                            break;
                        case EDIT_EVENT:
                            mService.executeAction(BaseService.ACTIONS.EVENT_UPDATE, getBindingKey(), mEvent);
                            break;
                    }
                }
            }
            else {
                Toast.makeText(getActivity(),getString(R.string.missing_fields),Toast.LENGTH_SHORT).show();
            }

            handled = true;
        }else {
            if (id == R.id.events_action_delete) {
                new AlertDialog.Builder(getActivity())
                        .setTitle(getString(R.string.alert_message_delete_event_title))
                        .setMessage(getString(R.string.alert_message_delete_event))
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                mService.executeAction(BaseService.ACTIONS.EVENT_DELETE, getBindingKey(), mEvent);
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
                handled = true;
            }
        }

        if (!handled) {
            handled =  super.onOptionsItemSelected(item);
        }

        return handled;
    }

    private Boolean tintRequiredIconsAndShowError(EditText requiredField){

        getIconToTint(requiredField);

        if (requiredField.getText().toString().trim().length() == 0) {
            mErrorLabelLayout.setError(getString(R.string.field_required));
            mDrawableToApply= DrawableCompat.wrap(mDrawableToApply);
            DrawableCompat.setTint(mDrawableToApply, getResources().getColor(com.globant.eventscorelib.R.color.red_error));
            mDrawableToApply= DrawableCompat.unwrap(mDrawableToApply);
            mIconToChange.setImageDrawable(mDrawableToApply);
            return false;
        }
        else{
            mErrorLabelLayout.setError("");
            mDrawableToApply= DrawableCompat.wrap(mDrawableToApply);
            DrawableCompat.setTint(mDrawableToApply, getResources().getColor(com.globant.eventscorelib.R.color.grey_icon));
            mDrawableToApply= DrawableCompat.unwrap(mDrawableToApply);
            mIconToChange.setImageDrawable(mDrawableToApply);
            return true;
        }
    }

    // BasePagerActivity.FragmentLifecycle implementation
    @Override
    public void onPauseFragment() {

    }

    @Override
    public void onResumeFragment() {
    }

    // BaseService.ActionListener implementation
    @Override
    public BaseService.ActionListener getActionListener() {
        return this;
    }

    @Override
    public Activity getBindingActivity() {return getActivity();}

    @Override
    public String getBindingKey() {return mBindingKey;}

    @Override
    public void onStartAction(BaseService.ACTIONS theAction) {showProgressOverlay();}

    @Override
    public void onFinishAction(BaseService.ACTIONS theAction, Object result) {

        getActivity().finish();

        switch (theAction){
            case EVENT_CREATE:
                Toast.makeText(getActivity(),getResources().getString(R.string.event_created),Toast.LENGTH_SHORT).show();
                break;
            case EVENT_UPDATE:
                Toast.makeText(getActivity(),getResources().getString(R.string.event_updated),Toast.LENGTH_SHORT).show();
                break;
            case EVENT_DELETE:
                Toast.makeText(getActivity(),getResources().getString(R.string.event_deleted),Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onFailAction(BaseService.ACTIONS theAction, Exception e) {
        showErrorOverlay();}
}
