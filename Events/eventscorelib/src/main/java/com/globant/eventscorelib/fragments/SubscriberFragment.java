package com.globant.eventscorelib.fragments;


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
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import com.globant.eventscorelib.R;
import com.globant.eventscorelib.baseComponents.ErrorLabelLayout;
import com.globant.eventscorelib.managers.SharedPreferencesManager;
import com.globant.eventscorelib.baseComponents.BaseFragment;
import com.globant.eventscorelib.baseComponents.BaseService;
import com.software.shell.fab.ActionButton;

import java.io.ByteArrayOutputStream;
import java.io.File;


/**
 * A simple {@link Fragment} subclass.
 */
public class SubscriberFragment extends BaseFragment {


    Bitmap mPhoto;
    ImageView mPhotoProfile;
    ActionButton mFloatingActionButtonPhoto;
    EditText mEditTextFirstName;
    EditText mEditTextLastName;
    EditText mEditTextPhone;
    EditText mEditTextOccupation;
    EditText mEditTextEmail;
    EditText mEditTextTwitter;
    EditText mEditTextCountry;
    EditText mEditTextCity;
    CheckBox mCheckBoxEnglishKnowledge;
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
    ErrorLabelLayout errorLabelLayoutFirstName;

    public SubscriberFragment() {
        // Required empty public constructor
    }


    @Override
    public BaseService.ActionListener getActionListener() {
        return null;
    }

    @Override
    public View onCreateEventView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_subscriber, container, false);
        wireUpViews(rootView);
        prepareImageButton();
        checkPreferences();
        setOnFocusListeners();
        hideUtilsAndShowContentOverlay();
        return rootView;
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
                if (mIconToChange!= null){
                    mDrawableToApply=DrawableCompat.wrap(mDrawableToApply);
                    DrawableCompat.setTint(mDrawableToApply,getResources().getColor(R.color.grey_icon));
                    mDrawableToApply=DrawableCompat.unwrap(mDrawableToApply);
                    mIconToChange.setImageDrawable(mDrawableToApply);}
            }
        });
    }


    private View.OnFocusChangeListener editTextFocus =  new View.OnFocusChangeListener() {
        public void onFocusChange(View view, boolean gainFocus) {

            int id = view.getId();
            //noinspection SimplifiableIfStatement

            //HACERLO UN VOID COMUN
            if (id== (R.id.edit_text_first_name)) {
             mIconToChange=mIconFirstName;
             mDrawableToApply=getResources().getDrawable(R.mipmap.ic_first_name);


            } else if (id== (R.id.edit_text_last_name)){
                mIconToChange=mIconLastName;
                mDrawableToApply=getResources().getDrawable(R.mipmap.ic_last_name);

            }   else if (id== (R.id.edit_text_phone)){
                 mIconToChange=mIconPhone;
                 mDrawableToApply=getResources().getDrawable(R.mipmap.ic_phone);

            }
                  else if (id== (R.id.edit_text_occupation)){
                    mIconToChange=mIconOccupation;
                    mDrawableToApply=getResources().getDrawable(R.mipmap.ic_occupation);


            }
                   else if (id== (R.id.edit_text_email)){
                     mIconToChange=mIconEmail;
                     mDrawableToApply=getResources().getDrawable(R.mipmap.ic_email);


            }
                     else if (id== (R.id.edit_text_country)){
                        mIconToChange=mIconCountry;
                        mDrawableToApply=getResources().getDrawable(R.mipmap.ic_country);


            }
                        else if (id== (R.id.edit_text_city)){
                         mIconToChange=mIconCity;
                         mDrawableToApply=getResources().getDrawable(R.mipmap.ic_city);


            }
                           else if (id== (R.id.edit_text_twitter)){
                             mIconToChange=mIconTwitter;
                             mDrawableToApply=getResources().getDrawable(R.mipmap.ic_twitter1);


            }

            //onFocus
            if (gainFocus) {
                mDrawableToApply=DrawableCompat.wrap(mDrawableToApply);
                DrawableCompat.setTint(mDrawableToApply,getResources().getColor(R.color.ambar));
                mDrawableToApply=DrawableCompat.unwrap(mDrawableToApply);
                mIconToChange.setImageDrawable(mDrawableToApply);
            }
            //onBlur
            else {
                mDrawableToApply=DrawableCompat.wrap(mDrawableToApply);
                DrawableCompat.setTint(mDrawableToApply,getResources().getColor(R.color.grey_icon));
                mDrawableToApply=DrawableCompat.unwrap(mDrawableToApply);
                mIconToChange.setImageDrawable(mDrawableToApply);
                mIconEnglishKnowledge.setImageDrawable(getResources().getDrawable(R.mipmap.ic_language));
            }
        };
    };

    private void checkPreferences() {
        File f = new File("/data/data/" + this.getActivity().getPackageName() +  "/shared_prefs/" +  this.getActivity().getPackageName()+ "_preferences.xml");
        if(f.exists())
        {
            retrieveSubscriberPreferences();
        }
    }

    private void retrieveSubscriberPreferences() {
        mEditTextFirstName.setText(SharedPreferencesManager.getUserFirstName(this.getActivity()));
        mEditTextLastName.setText(SharedPreferencesManager.getUserLastName(this.getActivity()));
        mEditTextPhone.setText(SharedPreferencesManager.getUserPhone(this.getActivity()));
        mEditTextEmail.setText(SharedPreferencesManager.getUserEmail(this.getActivity()));
        mEditTextOccupation.setText(SharedPreferencesManager.getUserOccupation(this.getActivity()));
        mEditTextTwitter.setText(SharedPreferencesManager.getUserTwitter(this.getActivity()));
        mEditTextCity.setText(SharedPreferencesManager.getUserCity(this.getActivity()));
        mEditTextCountry.setText(SharedPreferencesManager.getUserCountry(this.getActivity()));
        mCheckBoxEnglishKnowledge.setChecked(SharedPreferencesManager.getUserEnglishKnowledge(this.getActivity()));
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        String value = sharedPreferences.getString(this.getActivity().getString(R.string.preference_user_picture),null);
        if (value != null) {
            byte[] preferencePhoto = SharedPreferencesManager.getUserImage(this.getActivity());
            mPhotoProfile.setImageBitmap(BitmapFactory.decodeByteArray(preferencePhoto, 0, preferencePhoto.length));
        }
    }

    private void wireUpViews(View rootView) {
        mEditTextFirstName=(EditText)rootView.findViewById(R.id.edit_text_first_name);
        mEditTextLastName=(EditText)rootView.findViewById(R.id.edit_text_last_name);
        mEditTextPhone=(EditText)rootView.findViewById(R.id.edit_text_phone);
        mEditTextOccupation=(EditText)rootView.findViewById(R.id.edit_text_occupation);
        mEditTextTwitter=(EditText)rootView.findViewById(R.id.edit_text_twitter);
        mEditTextEmail=(EditText)rootView.findViewById(R.id.edit_text_email);
        mEditTextCountry=(EditText)rootView.findViewById(R.id.edit_text_country);
        mEditTextCity=(EditText)rootView.findViewById(R.id.edit_text_city);
        mCheckBoxEnglishKnowledge=(CheckBox)rootView.findViewById(R.id.check_box_english_knowledge);
        mFloatingActionButtonPhoto=(ActionButton)rootView.findViewById(R.id.fab);
        mPhotoProfile=(ImageView)rootView.findViewById(R.id.header);
        mIconFirstName=(ImageView)rootView.findViewById(R.id.icon_first_name);
        mIconLastName=(ImageView)rootView.findViewById(R.id.icon_last_name);
        mIconOccupation=(ImageView)rootView.findViewById(R.id.icon_occupation);
        mIconPhone=(ImageView)rootView.findViewById(R.id.icon_phone);
        mIconEmail=(ImageView)rootView.findViewById(R.id.icon_email);
        mIconCountry=(ImageView)rootView.findViewById(R.id.icon_country);
        mIconCity=(ImageView)rootView.findViewById(R.id.icon_city);
        mIconEnglishKnowledge=(ImageView)rootView.findViewById(R.id.icon_language);
        mIconTwitter=(ImageView)rootView.findViewById(R.id.icon_twitter);
        errorLabelLayoutFirstName = (ErrorLabelLayout) rootView.findViewById(R.id.nameErrorLayoutFirstName);
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA_CAPTURE) {
                File file = new File(Environment.getExternalStorageDirectory() + File.separator + "img.jpg");
                performCrop(Uri.fromFile(file));
            } else if (requestCode == CROP_PIC) {
                // get the returned data
                Bundle extras = data.getExtras();
                // get the cropped bitmap
                mPhoto = extras.getParcelable("data");
                mPhotoProfile.setImageBitmap(mPhoto);

            }
        }
  }

    private void prepareImageButton() {

        mFloatingActionButtonPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
    /*create instance of File with name img.jpg*/
                File file = new File(Environment.getExternalStorageDirectory()+ File.separator + "img.jpg");
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
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", mPhotoProfile.getWidth());
            cropIntent.putExtra("aspectY",  mPhotoProfile.getHeight());
            // indicate output X and Y
            cropIntent.putExtra("outputX", mPhotoProfile.getWidth());
            cropIntent.putExtra("outputY", mPhotoProfile.getHeight());
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_done) {
                ////CREAR PROCEDIMIENTO HAGA UN IF COMUN, QUE ENTRE UNA VIEW (EDITTEXT) USE EL VOID DE ARRIBA, SAQUE QUE ICONTOCHANGE Y DRAWTOAPLY Y HAGA LAS 4 LINEAS DE ABAJO
            if (mEditTextFirstName.getText().toString().trim().length() == 0) {
                errorLabelLayoutFirstName.setError("*Required");
                mIconToChange=mIconFirstName;
                mDrawableToApply=getResources().getDrawable(R.mipmap.ic_first_name);
                mDrawableToApply=DrawableCompat.wrap(mDrawableToApply);
                DrawableCompat.setTint(mDrawableToApply,getResources().getColor(R.color.red_error));
                mDrawableToApply=DrawableCompat.unwrap(mDrawableToApply);
                mIconToChange.setImageDrawable(mDrawableToApply);
            }

                else {
                    saveSubscriberPreferences();}

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveSubscriberPreferences() {
        SharedPreferencesManager.setUserFirstName(mEditTextFirstName.getText().toString(), this.getActivity());
        SharedPreferencesManager.setUserLastName(mEditTextLastName.getText().toString(), this.getActivity());
        SharedPreferencesManager.setUserPhone(mEditTextPhone.getText().toString(), this.getActivity());
        SharedPreferencesManager.setUserOccupation(mEditTextOccupation.getText().toString(), this.getActivity());
        SharedPreferencesManager.setUserEmail(mEditTextEmail.getText().toString(), this.getActivity());
        SharedPreferencesManager.setUserCountry(mEditTextCountry.getText().toString(), this.getActivity());
        SharedPreferencesManager.setUserCity(mEditTextCity.getText().toString(), this.getActivity());
        SharedPreferencesManager.setUserTwitter(mEditTextTwitter.getText().toString(), this.getActivity());
        Bitmap photoToPreference = ((BitmapDrawable)mPhotoProfile.getDrawable()).getBitmap();
        SharedPreferencesManager.setUserImage(convertBitmapImageToByteArray(photoToPreference),this.getActivity());
        SharedPreferencesManager.setUserEnglishKnowledge(mCheckBoxEnglishKnowledge.isChecked(),this.getActivity());
    }

    private byte[] convertBitmapImageToByteArray(Bitmap Photo) {
       ByteArrayOutputStream stream = new ByteArrayOutputStream();
       Photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
       return stream.toByteArray();
   }

}

