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
import com.globant.eventscorelib.utils.CoreConstants;
import com.globant.eventscorelib.utils.ErrorLabelLayout;
import com.globant.eventscorelib.controllers.SharedPreferencesController;
import com.globant.eventscorelib.baseComponents.BaseService;
import com.software.shell.fab.ActionButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;


/**
 * A simple {@link Fragment} subclass.
 */
public class BaseSubscriberFragment extends BaseFragment {


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


    public BaseSubscriberFragment() {
        // Required empty public constructor
    }

//    public Boolean getDoneClicked() {
//        return mDoneClicked;
//    }

    @Override
    public BaseService.ActionListener getActionListener() {
        return null;
    }

    @Override
    public View onCreateEventView(LayoutInflater inflater, ViewGroup container,
                                  Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_subscriber, container, false);
        wireUpViews(rootView);
        prepareImageButton();
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
                if (savedInstanceState.getString(CoreConstants.DONE_CLICKED) != null)
                    doneClick();
            }
                Bitmap bitmapToSave=savedInstanceState.getParcelable(CoreConstants.PHOTO_ROTATE);
                mPhotoProfile.setImageBitmap(bitmapToSave);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(CoreConstants.DONE_CLICKED, mDoneClicked.toString());
        BitmapDrawable drawable = (BitmapDrawable) mPhotoProfile.getDrawable();
        Bitmap bitmapToSave = drawable.getBitmap();
        outState.putParcelable(CoreConstants.PHOTO_ROTATE,bitmapToSave);
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

        ;
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
            mIconToChange = mIconTwitter;
            mDrawableToApply = getResources().getDrawable(R.mipmap.ic_twitter1);
            mErrorLabelLayout = mErrorLabelLayoutTwitter;
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
        mErrorLabelLayoutLastName = (ErrorLabelLayout) rootView.findViewById(R.id.nameErrorLayoutLastName);
        mErrorLabelLayoutPhone = (ErrorLabelLayout) rootView.findViewById(R.id.nameErrorLayoutPhone);
        mErrorLabelLayoutEmail = (ErrorLabelLayout) rootView.findViewById(R.id.nameErrorLayoutEmail);
        mErrorLabelLayoutTwitter = (ErrorLabelLayout) rootView.findViewById(R.id.nameErrorTwitter);
        mErrorLabelLayoutOccupation = (ErrorLabelLayout) rootView.findViewById(R.id.nameErrorOccupation);
        mErrorLabelLayoutCity = (ErrorLabelLayout) rootView.findViewById(R.id.nameErrorCity);
        mErrorLabelLayoutCountry = (ErrorLabelLayout) rootView.findViewById(R.id.nameErrorCountry);
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
        cropIntent.putExtra(CoreConstants.EXTRA_ASPECTX, mPhotoProfile.getWidth());
        cropIntent.putExtra(CoreConstants.EXTRA_ASPECTY, mPhotoProfile.getHeight());
        // indicate output X and Y
        cropIntent.putExtra(CoreConstants.EXTRA_OUTPUTX, mPhotoProfile.getWidth());
        cropIntent.putExtra(CoreConstants.EXTRA_OUTPUTY, mPhotoProfile.getHeight());
        // retrieve data on return
        cropIntent.putExtra(CoreConstants.EXTRA_RETURN_DATA, true);
        // start the activity - we handle returning in onActivityResult
        startActivityForResult(cropIntent, CROP_PIC);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

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
            if (mSavePreferences) {
                Toast.makeText(getActivity(), getResources().getString(R.string.profile_saved),
                        Toast.LENGTH_SHORT).show();
                saveSubscriberPreferences();
                getActivity().finish();
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.missing_fields),
                        Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        if (id == android.R.id.home) {
            getActivity().finish();
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
        tintRequiredIconsAndShowError(mEditTextTwitter);
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

    private void saveSubscriberPreferences() {
        SharedPreferencesController.setUserFirstName(mEditTextFirstName.getText().toString(), this.getActivity());
        SharedPreferencesController.setUserLastName(mEditTextLastName.getText().toString(), this.getActivity());
        SharedPreferencesController.setUserPhone(mEditTextPhone.getText().toString(), this.getActivity());
        SharedPreferencesController.setUserOccupation(mEditTextOccupation.getText().toString(), this.getActivity());
        SharedPreferencesController.setUserEmail(mEditTextEmail.getText().toString(), this.getActivity());
        SharedPreferencesController.setUserCountry(mEditTextCountry.getText().toString(), this.getActivity());
        SharedPreferencesController.setUserCity(mEditTextCity.getText().toString(), this.getActivity());
        SharedPreferencesController.setUserTwitter(mEditTextTwitter.getText().toString(), this.getActivity());
        Bitmap photoToPreference = ((BitmapDrawable) mPhotoProfile.getDrawable()).getBitmap();
        SharedPreferencesController.setUserImage(convertBitmapImageToByteArray(photoToPreference), this.getActivity());
        SharedPreferencesController.setUserEnglishKnowledge(mCheckBoxEnglishKnowledge.isChecked(), this.getActivity());
    }

    private byte[] convertBitmapImageToByteArray(Bitmap Photo) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }


}
