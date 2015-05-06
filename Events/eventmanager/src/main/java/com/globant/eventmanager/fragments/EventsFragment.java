package com.globant.eventmanager.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.text.InputType;
import android.text.format.DateFormat;
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
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.globant.eventmanager.R;
import com.globant.eventmanager.activities.MapManagerActivity;
import com.globant.eventscorelib.baseActivities.BasePagerActivity;
import com.globant.eventscorelib.baseComponents.BaseService;
import com.globant.eventscorelib.baseFragments.BaseFragment;
import com.globant.eventscorelib.domainObjects.Event;
import com.globant.eventscorelib.utils.CoreConstants;
import com.globant.eventscorelib.utils.ErrorLabelLayout;
import com.software.shell.fab.ActionButton;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by david.burgos on 24/04/2015.
 */
public class EventsFragment extends BaseFragment implements BasePagerActivity.FragmentLifecycle {

    private Boolean isEditing = false; // true = edit, false = create
    private Event mEvent;

    ImageView mPhotoEvent;
    ActionButton mFloatingActionButtonPhoto;
    AppCompatEditText mEditTextTitle;
    AppCompatEditText mEditTextFullDescription;
    AppCompatEditText mEditTextShortDescription;
    AppCompatEditText mEditTextAdditionalInfo;
    AppCompatSpinner mSpinnerCategory;
    AppCompatSpinner mSpinnerPublic;
    AppCompatEditText mEditTextHashtag;
    AppCompatEditText mEditTextLanguage;
    AppCompatEditText mEditTextStartDate;
    AppCompatEditText mEditTextStartTime;
    AppCompatEditText mEditTextEndDate;
    AppCompatEditText mEditTextEndTime;
    AppCompatEditText mEditTextMap;
    AppCompatEditText mEditTextAddress;
    AppCompatEditText mEditTextCountry;
    AppCompatEditText mEditTextCity;

    ImageView mIconTitle;
    ImageView mIconFullDescription;
    ImageView mIconShortDescription;
    ImageView mIconAdditionalInfo;
    ImageView mIconCategory;
    ImageView mIconPublic;
    ImageView mIconHashtag;
    ImageView mIconLanguage;
    ImageView mIconStartDate;
    ImageView mIconEndDate;
    ImageView mIconStartTime;
    ImageView mIconEndTime;
    ImageView mIconMap;
    ImageView mIconAddress;
    ImageView mIconCountry;
    ImageView mIconCity;
    ImageView mIconToChange;
    Drawable mDrawableToApply;

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
    ErrorLabelLayout mErrorLabelLayoutMap;
    ErrorLabelLayout mErrorLabelLayoutAddress;
    ErrorLabelLayout mErrorLabelLayoutCity;
    ErrorLabelLayout mErrorLabelLayoutCountry;
    ErrorLabelLayout mErrorLabelLayout;

    public EventsFragment() {
        // Required empty public constructor
        this.isEditing = false;
    }

    public EventsFragment Edit(Event event){
        this.isEditing = true;
        this.mEvent = event;
        return this;
    }

    @Override
    public BaseService.ActionListener getActionListener() {
        return null;
    }

    @Override
    public View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_events, container, false);
        wireUpViews(rootView);
        setUpSpinners();
        prepareImageButton();
        setOnFocusListeners();
        setDateTimeField();
        if(isEditing)populateInfo(mEvent);
        hideUtilsAndShowContentOverlay();
        return rootView;
    }

    private void populateInfo(Event event){
        if (event != null){
            mEditTextTitle.setText(event.getTitle());
            mEditTextFullDescription.setText(event.getFullDescription());
            mEditTextShortDescription.setText(event.getShortDescription());
            mEditTextAdditionalInfo.setText(event.getAdditionalInfo());
            mSpinnerCategory.setSelection(0);
            mSpinnerPublic.setSelection(0);
            mEditTextHashtag.setText(event.getHashtag());
            mEditTextLanguage.setText(event.getLanguage());
            mEditTextStartDate.setText(dateFormatter.format(event.getStartDate()));
            mEditTextStartTime.setText(TimeFormatter.format(event.getStartDate()));
            mEditTextEndDate.setText(dateFormatter.format(event.getEndDate()));
            mEditTextEndTime.setText(TimeFormatter.format(event.getEndDate()));
            mEditTextAddress.setText(event.getAddress());
            mEditTextCountry.setText(event.getCountry());
            mEditTextCity.setText(event.getCity());
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
        else if (id== (R.id.CategorySpinner)){
            mIconToChange= mIconCategory;
            mDrawableToApply=getResources().getDrawable(R.mipmap.ic_category);
            mErrorLabelLayout= mErrorLabelLayoutCategory;
        }
        else if (id== (R.id.PublicSpinner)){
            mIconToChange= mIconPublic;
            mDrawableToApply=getResources().getDrawable(R.mipmap.ic_public);
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
        else if (id== (R.id.edit_text_map)){
            mIconToChange= mIconMap;
            mDrawableToApply=getResources().getDrawable(R.mipmap.ic_map);
            mErrorLabelLayout=mErrorLabelLayoutMap;
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
        mEditTextTitle =(AppCompatEditText)rootView.findViewById(R.id.edit_text_title);
        mEditTextFullDescription =(AppCompatEditText)rootView.findViewById(R.id.edit_text_full_description);
        mEditTextShortDescription =(AppCompatEditText)rootView.findViewById(R.id.edit_text_short_description);
        mEditTextAdditionalInfo =(AppCompatEditText)rootView.findViewById(R.id.edit_text_additional_info);
        mSpinnerCategory =(AppCompatSpinner)rootView.findViewById(R.id.CategorySpinner);
        mSpinnerPublic =(AppCompatSpinner)rootView.findViewById(R.id.PublicSpinner);
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
        mEditTextMap =(AppCompatEditText)rootView.findViewById(R.id.edit_text_map);
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
        mIconMap =(ImageView)rootView.findViewById(R.id.icon_map);
        mIconAddress =(ImageView)rootView.findViewById(R.id.icon_address);
        mIconCountry=(ImageView)rootView.findViewById(R.id.icon_country);
        mIconCity=(ImageView)rootView.findViewById(R.id.icon_city);

        mErrorLabelLayoutTitle = (ErrorLabelLayout) rootView.findViewById(R.id.nameErrorLayoutTitle);
        mErrorLabelLayoutFullDescription = (ErrorLabelLayout) rootView.findViewById(R.id.nameErrorLayoutFullDescription);
        mErrorLabelLayoutShortDescription = (ErrorLabelLayout) rootView.findViewById(R.id.nameErrorLayoutShortDescription);
        mErrorLabelLayoutAdditionalInfo = (ErrorLabelLayout) rootView.findViewById(R.id.nameErrorLayoutAdditionalInfo);
        mErrorLabelLayoutCategory = (ErrorLabelLayout) rootView.findViewById(R.id.nameErrorLayoutCategory);
        mErrorLabelLayoutPublic = (ErrorLabelLayout) rootView.findViewById(R.id.nameErrorLayoutPublic);
        mErrorLabelLayoutHashtag = (ErrorLabelLayout) rootView.findViewById(R.id.nameErrorLayoutHashtag);
        mErrorLabelLayoutLanguage = (ErrorLabelLayout) rootView.findViewById(R.id.nameErrorLayoutLanguage);
        mErrorLabelLayoutStartDate = (ErrorLabelLayout) rootView.findViewById(R.id.nameErrorLayoutStartDate);
        mErrorLabelLayoutEndDate = (ErrorLabelLayout) rootView.findViewById(R.id.nameErrorLayoutEndDate);
        mErrorLabelLayoutStartTime = (ErrorLabelLayout) rootView.findViewById(R.id.nameErrorLayoutStartTime);
        mErrorLabelLayoutEndTime = (ErrorLabelLayout) rootView.findViewById(R.id.nameErrorLayoutEndTime);
        mErrorLabelLayoutMap = (ErrorLabelLayout) rootView.findViewById(R.id.nameErrorMap);
        mErrorLabelLayoutAddress = (ErrorLabelLayout) rootView.findViewById(R.id.nameErrorAddress);
        mErrorLabelLayoutCity = (ErrorLabelLayout) rootView.findViewById(R.id.nameErrorCity);
        mErrorLabelLayoutCountry = (ErrorLabelLayout) rootView.findViewById(R.id.nameErrorCountry);
    }

    private void setUpSpinners(){

        String[] categoryInfo = getResources().getStringArray(R.array.category_entries);
        ArrayAdapter<String> category_adapter = new ArrayAdapter<String>(getActivity(),  R.layout.simple_spinner_item, categoryInfo);
        category_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerCategory.setAdapter(category_adapter);
        mSpinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSpinnerCategory.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mSpinnerCategory.setFocusableInTouchMode(true);
        mSpinnerPublic.setFocusableInTouchMode(true);


        String[] publicInfo = getResources().getStringArray(R.array.public_entries);
        ArrayAdapter<String> public_adapter = new ArrayAdapter<String>(getActivity(),  R.layout.simple_spinner_item, publicInfo);

        public_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerPublic.setAdapter(public_adapter);
        mSpinnerPublic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSpinnerPublic.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mSpinnerPublic.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                onViewFocusChange(v, hasFocus);
                if(hasFocus) mSpinnerPublic.performClick();
            }
        });
        mSpinnerCategory.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                onViewFocusChange(v, hasFocus);
                if(hasFocus) mSpinnerCategory.performClick();
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
                    mStartDate.set(mStartDate.get(Calendar.YEAR),
                                   mStartDate.get(Calendar.MONTH),
                                   mStartDate.get(Calendar.DAY_OF_MONTH),
                                   hourOfDay,
                                   minute);

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

                    mEndDate.set(mEndDate.get(Calendar.YEAR),
                            mEndDate.get(Calendar.MONTH),
                            mEndDate.get(Calendar.DAY_OF_MONTH),
                            hourOfDay,
                            minute);

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
        return getResources().getString(R.string.credits_fragment_title);
        //TODO change hardcoded string
        // return "Create Event";
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
                            mEvent.setEventLogo(convertBitmapImageToByteArray(bitmap));
                        }
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                break;
            case CoreConstants.MAP_MANAGER_REQUEST:
                if (resultCode == getActivity().RESULT_OK){
                    Address address = data.getExtras().getParcelable(CoreConstants.MAP_ADDRESS_INTENT);
                    if (address != null){
                        if(mEditTextCity.getText().toString().isEmpty())mEditTextCity.setText(address.getLocality());
                        if(mEditTextCountry.getText().toString().isEmpty())mEditTextCountry.setText(address.getCountryName());
                        if(mEditTextAddress.getText().toString().isEmpty() && address.getMaxAddressLineIndex()>0)mEditTextAddress.setText(address.getAddressLine(0));
                        mEvent.setLatitude(address.getLatitude());
                        mEvent.setLongitude(address.getLongitude());
                    }
                }
                else {
                    //TODO No seteo ninguno punto
                }
                break;
        }

    }

    private void prepareImageButton() {

        mFloatingActionButtonPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, CoreConstants.PICTURE_SELECTION_REQUEST);
            }
        });

        mEditTextMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapManagerActivity.class);
                startActivityForResult(intent, CoreConstants.MAP_MANAGER_REQUEST);
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_events, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.events_action_done) {
            Boolean savePreferences = true;
            savePreferences &= tintRequiredIconsAndShowError(mEditTextTitle);
            savePreferences &= tintRequiredIconsAndShowError(mEditTextFullDescription);
            savePreferences &= tintRequiredIconsAndShowError(mEditTextShortDescription);
            savePreferences &= tintRequiredIconsAndShowError(mEditTextHashtag);
            savePreferences &= tintRequiredIconsAndShowError(mEditTextLanguage);
            savePreferences &= tintRequiredIconsAndShowError(mEditTextStartDate);
            savePreferences &= tintRequiredIconsAndShowError(mEditTextStartTime);
            savePreferences &= tintRequiredIconsAndShowError(mEditTextEndDate);
            savePreferences &= tintRequiredIconsAndShowError(mEditTextAddress);
            savePreferences &= tintRequiredIconsAndShowError(mEditTextCountry);
            savePreferences &= tintRequiredIconsAndShowError(mEditTextCity);

            if (savePreferences){
                Toast.makeText(getActivity(),"Saved!",Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getActivity(),"All fields need to be filled!",Toast.LENGTH_SHORT).show();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Boolean tintRequiredIconsAndShowError(EditText requiredField){
        
        getIconToTint(requiredField);

        if (requiredField.getText().toString().trim().length() == 0) {
            mErrorLabelLayout.setError("*Required");
            mDrawableToApply= DrawableCompat.wrap(mDrawableToApply);
            DrawableCompat.setTint(mDrawableToApply, getResources().getColor(com.globant.eventscorelib.R.color.red_error));
            mDrawableToApply= DrawableCompat.unwrap(mDrawableToApply);
            mIconToChange.setImageDrawable(mDrawableToApply);
            return false;
        }
        else{
            mDrawableToApply= DrawableCompat.wrap(mDrawableToApply);
            DrawableCompat.setTint(mDrawableToApply, getResources().getColor(com.globant.eventscorelib.R.color.grey_icon));
            mDrawableToApply= DrawableCompat.unwrap(mDrawableToApply);
            mIconToChange.setImageDrawable(mDrawableToApply);
            return true;
        }
    }

    private byte[] convertBitmapImageToByteArray(Bitmap Photo) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    @Override
    public void onPauseFragment() {

    }

    @Override
    public void onResumeFragment() {
       // mService.executeAction(BaseService.ACTIONS.EVENT_CREATE, "5vs7DC2RnQ", getBindingKey());
    }
}
