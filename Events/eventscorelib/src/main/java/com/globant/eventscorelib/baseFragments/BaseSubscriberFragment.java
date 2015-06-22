package com.globant.eventscorelib.baseFragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.globant.eventscorelib.R;
import com.globant.eventscorelib.baseActivities.BaseEventDetailPagerActivity;
import com.globant.eventscorelib.utils.TintInformation;
import com.globant.eventscorelib.utils.easterEggs.BaseEasterEgg;
import com.globant.eventscorelib.baseComponents.BaseService;
import com.globant.eventscorelib.controllers.SharedPreferencesController;
import com.globant.eventscorelib.domainObjects.Subscriber;
import com.globant.eventscorelib.utils.ConvertImage;
import com.globant.eventscorelib.utils.CoreConstants;
import com.globant.eventscorelib.utils.ErrorLabelLayout;
import com.globant.eventscorelib.utils.PushNotifications;
import com.software.shell.fab.ActionButton;

import java.io.File;
import java.util.regex.Pattern;


/**
 * A simple {@link Fragment} subclass.
 */
public class BaseSubscriberFragment extends BaseFragment implements BaseService.ActionListener,
        BaseEasterEgg.EasterEggListener {

    private Bitmap mPhoto;
    private ImageView mPhotoProfile;
    private FloatingActionButton mFloatingActionButtonPhoto;
    private AppCompatEditText mEditTextFirstName;
    private AppCompatEditText mEditTextLastName;
    private AppCompatEditText mEditTextPhone;
    private AppCompatEditText mEditTextOccupation;
    private AppCompatEditText mEditTextEmail;
    private AppCompatEditText mEditTextTwitter;
    private AppCompatEditText mEditTextCountry;
    private AppCompatEditText mEditTextCity;
    private AppCompatCheckBox mCheckBoxEnglishKnowledge;
    private ImageView mIconOccupation;
    private ImageView mIconLastName;
    private ImageView mIconCountry;
    private ImageView mIconCity;
    private ImageView mIconTwitter;
    private ImageView mIconPhone;
    private ImageView mIconEmail;
    private ImageView mIconFirstName;
    private final int CAMERA_CAPTURE = 1;

    private ErrorLabelLayout mErrorLabelLayoutFirstName;
    private ErrorLabelLayout mErrorLabelLayoutLastName;
    private ErrorLabelLayout mErrorLabelLayoutEmail;
    private ErrorLabelLayout mErrorLabelLayoutPhone;
    private ErrorLabelLayout mErrorLabelLayoutOccupation;
    private ErrorLabelLayout mErrorLabelLayoutCity;
    private ErrorLabelLayout mErrorLabelLayoutCountry;
    private ErrorLabelLayout mErrorLabelLayoutTwitter;
    private final Pattern emailPattern = Patterns.EMAIL_ADDRESS;
    private Boolean mSavePreferences;
    private LinearLayout mLayoutToFocus;
    private Boolean mDoneClicked;
    private Boolean mPhotoTaken;

    private Subscriber mSubscriber;
    private String mEventId;

    //    private SensorManager sensorManager;
//    private Sensor senAcelerometer;
//    private long lastUpdate = 0;
//    private float last_x, last_y, last_z;
//    private static final int N_SHAKES = 3;
//    private static final int SHAKE_THRESHOLD = 2500;
//    private static final int ONE_SHAKE_TIME_MILLIS = 80;
    private static final String HANDSHAKE_MESSAGE = "Glober detected";
    //    private int mShakes = 0;
    private boolean mGloberDetected = false;

    public BaseSubscriberFragment() {
        // Required empty public constructor
    }

    @Override
    public BaseService.ActionListener getActionListener() {
        return this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (!mGloberDetected) {
            subscribeEggListener(this);
//            sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
//            senAcelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//            sensorManager.registerListener(this, senAcelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public View onCreateEventView(LayoutInflater inflater, ViewGroup container,
                                  Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_subscriber, container, false);
        wireUpViews(rootView);
        prepareImageButton();
        setOnFocusListeners();
        mPhotoTaken = false;
        mSubscriber = new Subscriber();
        populateInfo();
        hideUtilsAndShowContentOverlay();
        mDoneClicked = false;
        return rootView;
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

        mFloatingActionButtonPhoto = (FloatingActionButton) rootView.findViewById(R.id.fab);

        mPhotoProfile = (ImageView) rootView.findViewById(R.id.header);
        mIconFirstName = (ImageView) rootView.findViewById(R.id.icon_first_name);
        mIconLastName = (ImageView) rootView.findViewById(R.id.icon_last_name);
        mIconOccupation = (ImageView) rootView.findViewById(R.id.icon_occupation);
        mIconPhone = (ImageView) rootView.findViewById(R.id.icon_phone);
        mIconEmail = (ImageView) rootView.findViewById(R.id.icon_email);
        mIconCountry = (ImageView) rootView.findViewById(R.id.icon_country);
        mIconCity = (ImageView) rootView.findViewById(R.id.icon_city);
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

        final Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar_subscriber);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) rootView.findViewById(R.id.collapsing_toolbar);
        if (SharedPreferencesController.getUserFirstName(getActivity()) != null) {
            collapsingToolbar.setTitle(SharedPreferencesController.getUserFirstName(getActivity()) + " " + SharedPreferencesController.getUserLastName(getActivity()));
        }    }

    private void prepareImageButton() {
        mFloatingActionButtonPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent cameraIntent = new Intent(CoreConstants.IMAGE_CAPTURE);
//    /*create instance of File with name img.jpg*/
//                File file = new File(Environment.getExternalStorageDirectory() + File.separator + CoreConstants.SHARED_PREF_IMG);
//    /*put uri as extra in intent object*/
//                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
//                startActivityForResult(cameraIntent, CAMERA_CAPTURE);
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_CAPTURE);

            }
        });
    }

    private void setOnFocusListeners() {
        View.OnFocusChangeListener editTextFocus = new View.OnFocusChangeListener() {
            public void onFocusChange(View view, boolean gainFocus) {
                TintInformation tintInformation = getIconToTint(view);
                //onFocus
                if (gainFocus) {
                    tintIcon(tintInformation.getImageView(), tintInformation.getDrawable(), getResources().getColor(R.color.ambar));
                    tintInformation.getErrorLabelLayout().clearError();
                }
                //onBlur
                else {
                    tintIcon(tintInformation.getImageView(), tintInformation.getDrawable(), getResources().getColor(R.color.grey_icon));
                }
            }
        };

        mEditTextFirstName.setOnFocusChangeListener(editTextFocus);
        mEditTextLastName.setOnFocusChangeListener(editTextFocus);
        mEditTextCountry.setOnFocusChangeListener(editTextFocus);
        mEditTextCity.setOnFocusChangeListener(editTextFocus);
        mEditTextTwitter.setOnFocusChangeListener(editTextFocus);
        mEditTextPhone.setOnFocusChangeListener(editTextFocus);
        mEditTextEmail.setOnFocusChangeListener(editTextFocus);
        mEditTextOccupation.setOnFocusChangeListener(editTextFocus);
    }

    private void populateInfo() {
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
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
//        String value = sharedPreferences.getString(CoreConstants.PREFERENCE_USER_PICTURE, null);
        byte[] preferencePhoto = SharedPreferencesController.getUserImage(this.getActivity());
        if (preferencePhoto != null) {
            mPhotoProfile.setImageBitmap(BitmapFactory.decodeByteArray(preferencePhoto, 0, preferencePhoto.length));
            mPhotoTaken = true;
//            mPhotoProfile.setScaleType(ImageView.ScaleType.FIT_XY);
        }
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

    private void tintIcon(ImageView imageView, Drawable drawable, int color) {
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, color);
        drawable = DrawableCompat.unwrap(drawable);
        if (imageView != null) {
            imageView.setImageDrawable(drawable);
        }
    }

    private TintInformation getIconToTint(View view) {
        int id = view.getId();
        TintInformation tintInformation = null;

        if (id == R.id.edit_text_first_name) {
            tintInformation = new TintInformation(mIconFirstName, getResources().getDrawable(R.mipmap.ic_first_name), mErrorLabelLayoutFirstName);
        } else if (id == R.id.edit_text_last_name) {
            tintInformation = new TintInformation(mIconLastName, getResources().getDrawable(R.mipmap.ic_last_name), mErrorLabelLayoutLastName);
        } else if (id == R.id.edit_text_phone) {
            tintInformation = new TintInformation(mIconPhone, getResources().getDrawable(R.mipmap.ic_phone), mErrorLabelLayoutPhone);
        } else if (id == R.id.edit_text_occupation) {
            tintInformation = new TintInformation(mIconOccupation, getResources().getDrawable(R.mipmap.ic_occupation), mErrorLabelLayoutOccupation);
        } else if (id == R.id.edit_text_email) {
            tintInformation = new TintInformation(mIconEmail, getResources().getDrawable(R.mipmap.ic_email), mErrorLabelLayoutEmail);
        } else if (id == R.id.edit_text_country) {
            tintInformation = new TintInformation(mIconCountry, getResources().getDrawable(R.mipmap.ic_country), mErrorLabelLayoutCountry);
        } else if (id == R.id.edit_text_city) {
            tintInformation = new TintInformation(mIconCity, getResources().getDrawable(R.mipmap.ic_city), mErrorLabelLayoutCity);
        } else if (id == R.id.edit_text_twitter) {
            tintInformation = new TintInformation(mIconTwitter, getResources().getDrawable(R.mipmap.ic_twitter1), mErrorLabelLayoutTwitter);
        }
        return tintInformation;
    }

    private void tintRequiredIconsAndShowError(EditText requiredField) {
        TintInformation tintInformation = getIconToTint(requiredField);
        if (requiredField.getText().toString().isEmpty()) {
            tintInformation.getErrorLabelLayout().setError(getResources().getString(R.string.field_required));
            tintIcon(tintInformation.getImageView(), tintInformation.getDrawable(), getResources().getColor(R.color.red_error));
            mSavePreferences = false;
        } else {
            tintIcon(tintInformation.getImageView(), tintInformation.getDrawable(), getResources().getColor(R.color.grey_icon));
        }

        if (requiredField == mEditTextEmail) {
            if ((!emailPattern.matcher(mEditTextEmail.getText().toString()).matches()) && (!(requiredField.getText().toString().isEmpty()))) {
                tintInformation.getErrorLabelLayout().setError(getResources().getString(R.string.email_required));
                tintIcon(tintInformation.getImageView(), tintInformation.getDrawable(), getResources().getColor(R.color.red_error));
                mSavePreferences = false;
            }
        }
    }

    public void tintAllIconsGrey() {
        tintIcon(null, getResources().getDrawable(R.mipmap.ic_first_name), getResources().getColor(R.color.grey_icon));
        tintIcon(null, getResources().getDrawable(R.mipmap.ic_last_name), getResources().getColor(R.color.grey_icon));
        tintIcon(null, getResources().getDrawable(R.mipmap.ic_phone), getResources().getColor(R.color.grey_icon));
        tintIcon(null, getResources().getDrawable(R.mipmap.ic_email), getResources().getColor(R.color.grey_icon));
        tintIcon(null, getResources().getDrawable(R.mipmap.ic_country), getResources().getColor(R.color.grey_icon));
        tintIcon(null, getResources().getDrawable(R.mipmap.ic_city), getResources().getColor(R.color.grey_icon));
        tintIcon(null, getResources().getDrawable(R.mipmap.ic_occupation), getResources().getColor(R.color.grey_icon));
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
        mSubscriber.setPicture(((BitmapDrawable) mPhotoProfile.getDrawable()).getBitmap());
        mSubscriber.setEnglish(mCheckBoxEnglishKnowledge.isChecked());
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
//                performCrop(Uri.fromFile(file));
//            } else if (requestCode == CROP_PIC) {
//                // get the returned data
                Bundle extras = data.getExtras();
                // get the cropped bitmap
                mPhoto = extras.getParcelable(CoreConstants.DATA);
                mPhotoProfile.setImageBitmap(mPhoto);
                mPhotoTaken=true;
//                mPhotoProfile.setScaleType(ImageView.ScaleType.FIT_XY);
            }
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

        if (id == R.id.action_done) {
            doneClick();
            if ((mSavePreferences) && (mPhotoTaken)) {
                saveSubscriberObject();
                SharedPreferencesController.setSubscriberInformation(mSubscriber, getActivity());
                if (getActivity().getIntent().getBooleanExtra(CoreConstants.FIELD_CHECK_IN, false)) {
                    mEventId = BaseEventDetailPagerActivity.getInstance().getEvent().getObjectID();
                }
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

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            if (!(savedInstanceState.getString(CoreConstants.DONE_CLICKED).equals("false"))) {
                doneClick();
            }

            Bitmap bitmapToSave = savedInstanceState.getParcelable(CoreConstants.PHOTO_ROTATE);
            mPhotoProfile.setImageBitmap(bitmapToSave);
            mPhotoTaken = Boolean.parseBoolean(savedInstanceState.getString(CoreConstants.PHOTO_TAKEN));
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

    @Override
    public Activity getBindingActivity() {
        return getActivity();
    }

    @Override
    public void onPause() {
        super.onPause();
        //sensorManager.unregisterListener(this);
    }

    @Override
    public String getBindingKey() {
        return this.getClass().getSimpleName();//+ new Date().toString();
    }

    @Override
    public void onResume() {
        super.onResume();
//        if (!mGloberDetected) {
//            //sensorManager.registerListener(this, senAcelerometer, SensorManager.SENSOR_DELAY_NORMAL);
//        }
    }

    @Override
    public void onStartAction(BaseService.ACTIONS theAction) {
        showProgressOverlay();
    }

    @Override
    public void onFinishAction(BaseService.ACTIONS theAction, Object result) {
        Intent intent;

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
                    Toast.makeText(getActivity(), getString(R.string.already_subscribed), Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                } else {
                    mService.executeAction(BaseService.ACTIONS.EVENTS_TO_SUBSCRIBER_CREATE, getBindingKey(), mSubscriber, mEventId);
                }
                break;
            case SUBSCRIBER_CREATE:
                mSubscriber.setObjectID((String) result);
                SharedPreferencesController.setSubscriberInformation(mSubscriber, getActivity());
                //mService.executeAction(BaseService.ACTIONS.IS_SUBSCRIBED, getBindingKey(), result, mEventId);
                intent = new Intent();
                intent.putExtra(CoreConstants.EXTRA_DATA_SUBSCRIBER, mSubscriber);
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
                break;
            case EVENTS_TO_SUBSCRIBER_CREATE:
                Toast.makeText(getActivity(), getString(R.string.have_been_subscribed), Toast.LENGTH_SHORT).show();
                PushNotifications.subscribeToChannel("SUB-" + mEventId);
                PushNotifications.subscribeToChannel("SUB-" + mEventId + "-" + mSubscriber.getObjectID());
                getActivity().finish();
                break;
            case SUBSCRIBER_UPDATE:
//                if (getActivity().getIntent().getBooleanExtra(CoreConstants.FIELD_CHECK_IN, false)) {
//                    mService.executeAction(BaseService.ACTIONS.IS_SUBSCRIBED, getBindingKey(), result, mEventId);
//                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.profile_saved), Toast.LENGTH_SHORT).show();
                    intent = new Intent();
                    intent.putExtra(CoreConstants.EXTRA_DATA_SUBSCRIBER, mSubscriber);
                    getActivity().setResult(Activity.RESULT_OK, intent);
                    getActivity().finish();
                    break;
//                }
        }
    }

    @Override
    public void onFailAction(BaseService.ACTIONS theAction, Exception e) {
        showErrorOverlay();
    }

    @Override
    public void onEasterEgg() {
        // TODO: The glober have just been detected
        mGloberDetected = true;
        Toast.makeText(getActivity(), HANDSHAKE_MESSAGE, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        tintAllIconsGrey();
        unsubscribeEggListener(this);
        super.onDestroy();
    }
}
