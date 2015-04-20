package com.globant.eventscorelib.fragments;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.globant.eventscorelib.R;
import com.globant.eventscorelib.baseComponents.BaseFragment;
import com.globant.eventscorelib.baseComponents.SharedPreferenceManager;
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

    final int CAMERA_CAPTURE = 1;
    final int CROP_PIC = 2;

    public SubscriberFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateEventView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_subscriber, container, false);
        wireUpViews(rootView);
        prepareImageButton();
        File f = new File("/data/data/" + this.getActivity().getPackageName() +  "/shared_prefs/" +  this.getActivity().getPackageName()+ "_preferences.xml");
        if(f.exists())
        {mEditTextFirstName.setText(SharedPreferenceManager.getUserFirstName(this.getActivity()));
            mEditTextLastName.setText(SharedPreferenceManager.getUserLastName(this.getActivity()));
            mEditTextPhone.setText(SharedPreferenceManager.getUserPhone(this.getActivity()));
            mEditTextEmail.setText(SharedPreferenceManager.getUserEmail(this.getActivity()));
            mEditTextOccupation.setText(SharedPreferenceManager.getUserOccupation(this.getActivity()));
            mEditTextTwitter.setText(SharedPreferenceManager.getUserTwitter(this.getActivity()));
            mEditTextCity.setText(SharedPreferenceManager.getUserCity(this.getActivity()));
            mEditTextCountry.setText(SharedPreferenceManager.getUserCountry(this.getActivity()));
            mCheckBoxEnglishKnowledge.setChecked(SharedPreferenceManager.getUserEnglishKnowledge(this.getActivity()));
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
            String value = sharedPreferences.getString(this.getActivity().getString(R.string.preference_user_picture),null);
            if (value != null) {
                byte[] preferencePhoto = SharedPreferenceManager.getUserImage(this.getActivity());
                mPhotoProfile.setImageBitmap(BitmapFactory.decodeByteArray(preferencePhoto, 0, preferencePhoto.length));
            }
        }
        hideUtilsAndShowContentOverlay();
        return rootView;
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
            SharedPreferenceManager.setUserFirstName(mEditTextFirstName.getText().toString(), this.getActivity());
            SharedPreferenceManager.setUserLastName(mEditTextLastName.getText().toString(), this.getActivity());
            SharedPreferenceManager.setUserPhone(mEditTextPhone.getText().toString(), this.getActivity());
            SharedPreferenceManager.setUserOccupation(mEditTextOccupation.getText().toString(), this.getActivity());
            SharedPreferenceManager.setUserEmail(mEditTextEmail.getText().toString(), this.getActivity());
            SharedPreferenceManager.setUserCountry(mEditTextCountry.getText().toString(), this.getActivity());
            SharedPreferenceManager.setUserCity(mEditTextCity.getText().toString(), this.getActivity());
            SharedPreferenceManager.setUserTwitter(mEditTextTwitter.getText().toString(), this.getActivity());
            Bitmap photoToPreference = ((BitmapDrawable)mPhotoProfile.getDrawable()).getBitmap();
            SharedPreferenceManager.setUserImage(convertBitmapImageToByteArray(photoToPreference),this.getActivity());
            SharedPreferenceManager.setUserEnglishKnowledge(mCheckBoxEnglishKnowledge.isChecked(),this.getActivity());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

   private byte[] convertBitmapImageToByteArray(Bitmap Photo) {
       ByteArrayOutputStream stream = new ByteArrayOutputStream();
       Photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
       return stream.toByteArray();
   }

}

