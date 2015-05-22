package com.globant.eventscorelib.baseFragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatEditText;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.globant.eventscorelib.R;
import com.globant.eventscorelib.baseActivities.BaseEventDetailPagerActivity;
import com.globant.eventscorelib.baseComponents.BaseService;
import com.globant.eventscorelib.controllers.SharedPreferencesController;
import com.globant.eventscorelib.domainObjects.Subscriber;
import com.globant.eventscorelib.utils.ConvertImage;
import com.globant.eventscorelib.utils.CoreConstants;
import com.globant.eventscorelib.utils.ErrorLabelLayout;
import com.software.shell.fab.ActionButton;

import java.io.File;
import java.util.Date;
import java.util.regex.Pattern;


/**
 * A simple {@link Fragment} subclass.
 */
public class BaseSubscriberFragment extends BaseFragment implements BaseService.ActionListener, SensorEventListener {


    Bitmap mPhoto;
    ImageView mPhotoProfile;
    ActionButton mFloatingActionButtonPhoto;
    AppCompatEditText mEditTextFirstName;
    AppCompatEditText mEditTextLastName;
    AppCompatEditText mEditTextPhone;
    AppCompatEditText mEditTextOccupation;
    AppCompatEditText mEditTextEmail;
    AppCompatEditText mEditTextTwitter;
    AppCompatEditText mEditTextCountry;
    AppCompatEditText mEditTextCity;
    AppCompatEditText mEditTextToChangeHint;
    AppCompatCheckBox mCheckBoxEnglishKnowledge;
    String mHintToReturn;
    ImageView mIconOccupation;
    ImageView mIconLastName;
    ImageView mIconCountry;
    ImageView mIconCity;
    ImageView mIconTwitter;
    ImageView mIconPhone;
    ImageView mIconEmail;
    ImageView mIconEnglishKnowledge;
    ImageView mIconFirstName;
    ImageView mIconToChange;
    Drawable mDrawableToApply;
    final int CAMERA_CAPTURE = 1;
    final int CROP_PIC = 2;
    ErrorLabelLayout mErrorLabelLayoutFirstName;
    ErrorLabelLayout mErrorLabelLayoutLastName;
    ErrorLabelLayout mErrorLabelLayoutEmail;
    ErrorLabelLayout mErrorLabelLayoutPhone;
    ErrorLabelLayout mErrorLabelLayoutOccupation;
    ErrorLabelLayout mErrorLabelLayoutCity;
    ErrorLabelLayout mErrorLabelLayoutCountry;
    ErrorLabelLayout mErrorLabelLayoutTwitter;
    ErrorLabelLayout mErrorLabelLayout;
    final Pattern emailPattern = Patterns.EMAIL_ADDRESS;
    Boolean mSavePreferences;
    LinearLayout mLayoutToFocus;
    Boolean mDoneClicked;
    Boolean mPhotoTaken;

    Subscriber mSubscriber;
    String mEventId;
    
    private SensorManager sensorManager;
    private Sensor senAcelerometer;
    private long lastUpdate = 0;
    private float last_x,last_y,last_z;
    private static final int N_SHAKES = 3;
    private static final int SHAKE_THRESHOLD = 2500;
    private static final int ONE_SHAKE_TIME_MILLIS = 80;
    private static final String HANDSHAKE_MESSAGE = "Glober detected";
    private int mShakes = 0;
    private boolean globerDetected = false;


    public BaseSubscriberFragment() {
        // Required empty public constructor
    }


    @Override
    public BaseService.ActionListener getActionListener() {
        return this;
    }

    @Override
    public View onCreateEventView(LayoutInflater inflater, ViewGroup container,
                                  Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_subscriber, container, false);
        wireUpViews(rootView);
        prepareImageButton();
        mPhotoTaken = false;
        mSubscriber = new Subscriber();
        checkPreferences();
        setOnFocusListeners();
        hideUtilsAndShowContentOverlay();
        mDoneClicked = false;
        return rootView;
    }
    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            {
                if (!(savedInstanceState.getString(CoreConstants.DONE_CLICKED).equals("false")))
                    doneClick();
            }
                Bitmap bitmapToSave=savedInstanceState.getParcelable(CoreConstants.PHOTO_ROTATE);
                mPhotoProfile.setImageBitmap(bitmapToSave);
                mPhotoTaken=Boolean.parseBoolean(savedInstanceState.getString(CoreConstants.PHOTO_TAKEN));
//            if (mPhotoTaken){
//                mPhotoProfile.setScaleType(ImageView.ScaleType.FIT_XY);


             }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(CoreConstants.DONE_CLICKED, mDoneClicked.toString());
        outState.putString(CoreConstants.PHOTO_TAKEN, mPhotoTaken.toString());
        BitmapDrawable drawable = (BitmapDrawable) mPhotoProfile.getDrawable();
        Bitmap bitmapToSave = drawable.getBitmap();
        outState.putParcelable(CoreConstants.PHOTO_ROTATE, bitmapToSave);
    }

    private void setOnFocusListeners() {
        mEditTextFirstName.setOnFocusChangeListener(editTextFocus);
        mEditTextLastName.setOnFocusChangeListener(editTextFocus);
        mEditTextCountry.setOnFocusChangeListener(editTextFocus);
        mEditTextCity.setOnFocusChangeListener(editTextFocus);
        mEditTextTwitter.setOnFocusChangeListener(editTextFocus);
        mEditTextPhone.setOnFocusChangeListener(editTextFocus);
        mEditTextEmail.setOnFocusChangeListener(editTextFocus);
        mEditTextOccupation.setOnFocusChangeListener(editTextFocus);
        mCheckBoxEnglishKnowledge.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mIconEnglishKnowledge.setImageDrawable(getResources().getDrawable(R.mipmap.ic_language_ambar));
                if (mIconToChange != null) {
                    tintGrey();
                }
            }
        });
    }

    private View.OnFocusChangeListener editTextFocus = new View.OnFocusChangeListener() {
        public void onFocusChange(View view, boolean gainFocus) {

            getIconToTint(view);
            mEditTextToChangeHint = (AppCompatEditText) view;
            //onFocus
            if (gainFocus) {
                mDrawableToApply = DrawableCompat.wrap(mDrawableToApply);
                DrawableCompat.setTint(mDrawableToApply, getResources().getColor(R.color.ambar));
                mDrawableToApply = DrawableCompat.unwrap(mDrawableToApply);
                mIconToChange.setImageDrawable(mDrawableToApply);
                mErrorLabelLayout.clearError();
                mEditTextToChangeHint.setHint("");
            }
            //onBlur
            else {
                tintGrey();
                mIconEnglishKnowledge.setImageDrawable(getResources().getDrawable(R.mipmap.ic_language));
                mEditTextToChangeHint.setHint(mHintToReturn);
            }
        }
    };

    private void getIconToTint(View view) {
        int id = view.getId();
        //noinspection SimplifiableIfStatement

        if (id == (R.id.edit_text_first_name)) {
            mIconToChange = mIconFirstName;
            mDrawableToApply = getResources().getDrawable(R.mipmap.ic_first_name);
            mErrorLabelLayout = mErrorLabelLayoutFirstName;
            mHintToReturn = getResources().getString(R.string.edit_text_first_name_hint);

        } else if (id == (R.id.edit_text_last_name)) {
            mIconToChange = mIconLastName;
            mDrawableToApply = getResources().getDrawable(R.mipmap.ic_last_name);
            mErrorLabelLayout = mErrorLabelLayoutLastName;
            mHintToReturn = getResources().getString(R.string.edit_text_last_name_hint);


        } else if (id == (R.id.edit_text_phone)) {
            mIconToChange = mIconPhone;
            mDrawableToApply = getResources().getDrawable(R.mipmap.ic_phone);
            mErrorLabelLayout = mErrorLabelLayoutPhone;
            mHintToReturn = getResources().getString(R.string.edit_text_phone_hint);

        } else if (id == (R.id.edit_text_occupation)) {
            mIconToChange = mIconOccupation;
            mDrawableToApply = getResources().getDrawable(R.mipmap.ic_occupation);
            mErrorLabelLayout = mErrorLabelLayoutOccupation;
            mHintToReturn = getResources().getString(R.string.edit_text_occupation_hint);


        } else if (id == (R.id.edit_text_email)) {
            mIconToChange = mIconEmail;
            mDrawableToApply = getResources().getDrawable(R.mipmap.ic_email);
            mErrorLabelLayout = mErrorLabelLayoutEmail;
            mHintToReturn = getResources().getString(R.string.edit_text_email_hint);


        } else if (id == (R.id.edit_text_country)) {
            mIconToChange = mIconCountry;
            mDrawableToApply = getResources().getDrawable(R.mipmap.ic_country);
            mErrorLabelLayout = mErrorLabelLayoutCountry;
            mHintToReturn = getResources().getString(R.string.edit_text_country_hint);


        } else if (id == (R.id.edit_text_city)) {
            mIconToChange = mIconCity;
            mDrawableToApply = getResources().getDrawable(R.mipmap.ic_city);
            mErrorLabelLayout = mErrorLabelLayoutCity;
            mHintToReturn = getResources().getString(R.string.edit_text_city_hint);


        } else if (id == (R.id.edit_text_twitter)) {
            mHintToReturn = getResources().getString(R.string.edit_text_twitter_hint);

        }
    }


    private void checkPreferences() {
        File f = new File(CoreConstants.SHARED_PREF_ROOT + this.getActivity().getPackageName() + CoreConstants.SHARED_PREF_DIR + this.getActivity().getPackageName() + CoreConstants.SHARED_PREF_FILE);
        if (f.exists()) {
            retrieveSubscriberPreferences();
        }
    }

    private void retrieveSubscriberPreferences() {
        mEditTextFirstName.setText(SharedPreferencesController.getUserFirstName(this.getActivity()));
        mEditTextLastName.setText(SharedPreferencesController.getUserLastName(this.getActivity()));
        mEditTextPhone.setText(SharedPreferencesController.getUserPhone(this.getActivity()));
        mEditTextEmail.setText(SharedPreferencesController.getUserEmail(this.getActivity()));
        mEditTextOccupation.setText(SharedPreferencesController.getUserOccupation(this.getActivity()));
        mEditTextTwitter.setText(SharedPreferencesController.getUserTwitter(this.getActivity()));
        mEditTextCity.setText(SharedPreferencesController.getUserCity(this.getActivity()));
        mEditTextCountry.setText(SharedPreferencesController.getUserCountry(this.getActivity()));
        mCheckBoxEnglishKnowledge.setChecked(SharedPreferencesController.getUserEnglishKnowledge(this.getActivity()));
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        String value = sharedPreferences.getString(this.getActivity().getString(R.string.preference_user_picture), null);
        if (value != null) {
            byte[] preferencePhoto = SharedPreferencesController.getUserImage(this.getActivity());
            mPhotoProfile.setImageBitmap(BitmapFactory.decodeByteArray(preferencePhoto, 0, preferencePhoto.length));
            mPhotoTaken=true;
//            mPhotoProfile.setScaleType(ImageView.ScaleType.FIT_XY);
        }

    }

    public void wireUpViews(View rootView) {
        mEditTextFirstName = (AppCompatEditText) rootView.findViewById(R.id.edit_text_first_name);
        mEditTextLastName = (AppCompatEditText) rootView.findViewById(R.id.edit_text_last_name);
        mEditTextPhone = (AppCompatEditText) rootView.findViewById(R.id.edit_text_phone);
        mEditTextOccupation = (AppCompatEditText) rootView.findViewById(R.id.edit_text_occupation);
        mEditTextTwitter = (AppCompatEditText) rootView.findViewById(R.id.edit_text_twitter);
        mEditTextEmail = (AppCompatEditText) rootView.findViewById(R.id.edit_text_email);
        mEditTextCountry = (AppCompatEditText) rootView.findViewById(R.id.edit_text_country);
        mEditTextCity = (AppCompatEditText) rootView.findViewById(R.id.edit_text_city);
        mCheckBoxEnglishKnowledge = (AppCompatCheckBox) rootView.findViewById(R.id.check_box_english_knowledge);
        mFloatingActionButtonPhoto = (ActionButton) rootView.findViewById(R.id.fab);
        mPhotoProfile = (ImageView) rootView.findViewById(R.id.header);
        mIconFirstName = (ImageView) rootView.findViewById(R.id.icon_first_name);
        mIconLastName = (ImageView) rootView.findViewById(R.id.icon_last_name);
        mIconOccupation = (ImageView) rootView.findViewById(R.id.icon_occupation);
        mIconPhone = (ImageView) rootView.findViewById(R.id.icon_phone);
        mIconEmail = (ImageView) rootView.findViewById(R.id.icon_email);
        mIconCountry = (ImageView) rootView.findViewById(R.id.icon_country);
        mIconCity = (ImageView) rootView.findViewById(R.id.icon_city);
        mIconEnglishKnowledge = (ImageView) rootView.findViewById(R.id.icon_language);
        mIconTwitter = (ImageView) rootView.findViewById(R.id.icon_twitter);
        mErrorLabelLayoutFirstName = (ErrorLabelLayout) rootView.findViewById(R.id.nameErrorLayoutFirstName);
        mErrorLabelLayoutLastName = (ErrorLabelLayout) rootView.findViewById(R.id.name_error_layout_last_name);
        mErrorLabelLayoutPhone = (ErrorLabelLayout) rootView.findViewById(R.id.nameErrorLayoutPhone);
        mErrorLabelLayoutEmail = (ErrorLabelLayout) rootView.findViewById(R.id.nameErrorLayoutEmail);
        mErrorLabelLayoutTwitter = (ErrorLabelLayout) rootView.findViewById(R.id.nameErrorTwitter);
        mErrorLabelLayoutOccupation = (ErrorLabelLayout) rootView.findViewById(R.id.nameErrorOccupation);
        mErrorLabelLayoutCity = (ErrorLabelLayout) rootView.findViewById(R.id.name_error_city);
        mErrorLabelLayoutCountry = (ErrorLabelLayout) rootView.findViewById(R.id.name_error_country);
        mLayoutToFocus = (LinearLayout) rootView.findViewById(R.id.autoFocusable);
    }

    @Override
    public String getTitle() {
        return getString(R.string.title_fragment_subscriber);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA_CAPTURE) {
                File file = new File(Environment.getExternalStorageDirectory() + File.separator + CoreConstants.SHARED_PREF_IMG);
                performCrop(Uri.fromFile(file));
            } else if (requestCode == CROP_PIC) {
                // get the returned data
                Bundle extras = data.getExtras();
                // get the cropped bitmap
                mPhoto = extras.getParcelable(CoreConstants.DATA);
                mPhotoProfile.setImageBitmap(mPhoto);
                mPhotoTaken=true;
//                mPhotoProfile.setScaleType(ImageView.ScaleType.FIT_XY);
            }
        }
    }

    private void prepareImageButton() {

        mFloatingActionButtonPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(CoreConstants.IMAGE_CAPTURE);
    /*create instance of File with name img.jpg*/
                File file = new File(Environment.getExternalStorageDirectory() + File.separator + CoreConstants.SHARED_PREF_IMG);
    /*put uri as extra in intent object*/
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(cameraIntent, CAMERA_CAPTURE);

            }
        });
    }

    private void performCrop(Uri picUri) {
        // take care of exceptions
        // call the standard crop action intent (the user device may not
        // support it)
        Intent cropIntent = new Intent(CoreConstants.IMAGE_CROP);
        // indicate image type and Uri
        cropIntent.setDataAndType(picUri, CoreConstants.URI_NAME);
        // set crop properties
        cropIntent.putExtra(CoreConstants.EXTRA_CROP, CoreConstants.EXTRA_TRUE);
        // indicate aspect of desired crop
        cropIntent.putExtra(CoreConstants.EXTRA_ASPECTX, 720);
        cropIntent.putExtra(CoreConstants.EXTRA_ASPECTY, 360);
        // indicate output X and Y
        cropIntent.putExtra(CoreConstants.EXTRA_OUTPUTX, 720);
        cropIntent.putExtra(CoreConstants.EXTRA_OUTPUTY,360);
        // retrieve data on return
        cropIntent.putExtra(CoreConstants.EXTRA_RETURN_DATA, true);
        // start the activity - we handle returning in onActivityResult
        startActivityForResult(cropIntent, CROP_PIC);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (!globerDetected) {
            sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
            senAcelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, senAcelerometer, sensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_subscriber, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        mLayoutToFocus.requestFocus();
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_done) {
            doneClick();
            if ((mSavePreferences) && (mPhotoTaken)) {
                saveSubscriberObject();
                SharedPreferencesController.setSubscriberInformation(mSubscriber, getActivity());
                if (getActivity().getIntent().getBooleanExtra(CoreConstants.FIELD_CHECK_IN, false)) {
                    mEventId = BaseEventDetailPagerActivity.getInstance().getEvent().getObjectID();}
                mService.executeAction(BaseService.ACTIONS.SUBSCRIBER_EXISTS, getBindingKey(), mEditTextEmail.getText().toString());

            } else if (!(mPhotoTaken)) {
                Toast.makeText(getActivity(), getResources().getString(R.string.missing_photo),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.missing_fields),
                        Toast.LENGTH_SHORT).show();
            }
            mLayoutToFocus.requestFocus();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    public void doneClick() {
        mSavePreferences = true;
        mDoneClicked = true;
        tintRequiredIconsAndShowError(mEditTextFirstName);
        tintRequiredIconsAndShowError(mEditTextLastName);
        tintRequiredIconsAndShowError(mEditTextPhone);
        tintRequiredIconsAndShowError(mEditTextEmail);
        tintRequiredIconsAndShowError(mEditTextOccupation);
        tintRequiredIconsAndShowError(mEditTextCity);
        tintRequiredIconsAndShowError(mEditTextCountry);

    }

    private void tintRequiredIconsAndShowError(EditText requiredField) {
        getIconToTint(requiredField);
        if (requiredField.getText().toString().trim().length() == 0) {
            mErrorLabelLayout.setError(getResources().getString(R.string.field_required));
            mDrawableToApply = DrawableCompat.wrap(mDrawableToApply);
            DrawableCompat.setTint(mDrawableToApply, getResources().getColor(R.color.red_error));
            mDrawableToApply = DrawableCompat.unwrap(mDrawableToApply);
            mIconToChange.setImageDrawable(mDrawableToApply);
            mSavePreferences = false;
        } else {
            tintGrey();
        }

        if (requiredField == mEditTextEmail) {
            if ((!emailPattern.matcher(mEditTextEmail.getText().toString()).matches()) && (!(requiredField.getText().toString().trim().length() == 0))) {
                mErrorLabelLayout.setError(getResources().getString(R.string.email_required));
                mDrawableToApply = DrawableCompat.wrap(mDrawableToApply);
                DrawableCompat.setTint(mDrawableToApply, getResources().getColor(R.color.red_error));
                mDrawableToApply = DrawableCompat.unwrap(mDrawableToApply);
                mIconToChange.setImageDrawable(mDrawableToApply);
                mSavePreferences = false;
            }
        }
    }

    private void tintGrey() {

        mDrawableToApply = DrawableCompat.wrap(mDrawableToApply);
        DrawableCompat.setTint(mDrawableToApply, getResources().getColor(R.color.grey_icon));
        mDrawableToApply = DrawableCompat.unwrap(mDrawableToApply);
        mIconToChange.setImageDrawable(mDrawableToApply);
    }



    public void tintAllGrey(){
        mDrawableToApply = getResources().getDrawable(R.mipmap.ic_first_name);
        tintGrey();
        mDrawableToApply = getResources().getDrawable(R.mipmap.ic_last_name);
        tintGrey();
        mDrawableToApply = getResources().getDrawable(R.mipmap.ic_phone);
        tintGrey();
        mDrawableToApply = getResources().getDrawable(R.mipmap.ic_email);
        tintGrey();
        mDrawableToApply = getResources().getDrawable(R.mipmap.ic_country);
        tintGrey();
        mDrawableToApply = getResources().getDrawable(R.mipmap.ic_city);
        tintGrey();
        mDrawableToApply = getResources().getDrawable(R.mipmap.ic_occupation);
        tintGrey();

    }






    private void saveSubscriberObject() {
        mSubscriber.setName(mEditTextFirstName.getText().toString());
        mSubscriber.setLastName(mEditTextLastName.getText().toString());
        mSubscriber.setPhone(mEditTextPhone.getText().toString());
        mSubscriber.setOccupation(mEditTextOccupation.getText().toString());
        mSubscriber.setEmail(mEditTextEmail.getText().toString());
        mSubscriber.setCountry(mEditTextCountry.getText().toString());
        mSubscriber.setCity(mEditTextCity.getText().toString());
        if (!mEditTextTwitter.getText().toString().isEmpty()) {
            mSubscriber.setTwitterUser(mEditTextTwitter.getText().toString());
        } else {
            mSubscriber.setTwitterUser(null);
        }
        Bitmap photo = ((BitmapDrawable) mPhotoProfile.getDrawable()).getBitmap();
        mSubscriber.setPicture(ConvertImage.convertBitmapImageToByteArray(photo));
        mSubscriber.setEnglish(mCheckBoxEnglishKnowledge.isChecked());

    }

    @Override
    public Activity getBindingActivity() {
        return getActivity();
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
    @Override
    public String getBindingKey() {
        return this.getClass().getSimpleName() ;//+ new Date().toString();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!globerDetected) {
            sensorManager.registerListener(this, senAcelerometer, sensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;
        if (mySensor.getType()==Sensor.TYPE_ACCELEROMETER)
        {
            checkHandShake(event);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void checkHandShake(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        long curTime  = System.currentTimeMillis();
        if(curTime-lastUpdate > ONE_SHAKE_TIME_MILLIS)
        {
            long diffTime = (curTime - lastUpdate);
            lastUpdate = curTime;
            float speed = Math.abs(x + y + z - last_x - last_y - last_z)/ diffTime * 10000;
            if(speed > SHAKE_THRESHOLD)
            {
                mShakes++;
                // 5 shakes: 3 forward with 2 backward
                if (mShakes >= 2 * N_SHAKES - 1) {
                    // TODO: Use this to identify the owner as glober
                    // NOTE: Here lies a pseudo bug: if you shake, exit, back and shake again, getActivity() returns null.
/*
                    Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(400);
*/
                    globerDetected = true;
                    Toast.makeText(getActivity(), HANDSHAKE_MESSAGE, Toast.LENGTH_SHORT).show();
                    sensorManager.unregisterListener(this);
                }
            }
            else {
                mShakes = 0;
            }

            last_x = x;
            last_y=y;
            last_z=z;
        }
    }
    @Override
    public void onStartAction(BaseService.ACTIONS theAction) {
        showProgressOverlay();
    }

    @Override
    public void onFinishAction(BaseService.ACTIONS theAction, Object result) {
        switch (theAction) {
            case SUBSCRIBER_EXISTS:
                    if (result.equals("")) {
                    mService.executeAction(BaseService.ACTIONS.SUBSCRIBER_CREATE, getBindingKey(), mSubscriber);
                    } else {
                        mSubscriber.setObjectID((String) result);
                        mService.executeAction(BaseService.ACTIONS.SUBSCRIBER_UPDATE, getBindingKey(), mSubscriber);
                    }
                break;
            case IS_SUBSCRIBED:
                if ((Boolean) result) {
                     hideUtilsAndShowContentOverlay();
                    Toast.makeText(getActivity(), getString(R.string.already_subscribed), Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                } else {
                    mService.executeAction(BaseService.ACTIONS.EVENTS_TO_SUBSCRIBER_CREATE, getBindingKey(),mSubscriber, mEventId);
                }
                break;
            case SUBSCRIBER_CREATE:
                 mSubscriber.setObjectID((String)result);
                 mService.executeAction(BaseService.ACTIONS.IS_SUBSCRIBED, getBindingKey(),result, mEventId);
                break;
            case EVENTS_TO_SUBSCRIBER_CREATE:
                hideUtilsAndShowContentOverlay();
                Toast.makeText(getActivity(), getString(R.string.have_been_subscribed), Toast.LENGTH_SHORT).show();
                getActivity().finish();
                break;
            case SUBSCRIBER_UPDATE:
               if (getActivity().getIntent().getBooleanExtra(CoreConstants.FIELD_CHECK_IN, false)) {
                   mService.executeAction(BaseService.ACTIONS.IS_SUBSCRIBED, getBindingKey(),result, mEventId);}
                else {
                   hideUtilsAndShowContentOverlay();
                   Toast.makeText(getActivity(), getResources().getString(R.string.profile_saved), Toast.LENGTH_SHORT).show();
                   getActivity().finish();
                   break;
               }
        }

    }

    @Override
    public void onFailAction(BaseService.ACTIONS theAction, Exception e) {
        showErrorOverlay();
    }
}
