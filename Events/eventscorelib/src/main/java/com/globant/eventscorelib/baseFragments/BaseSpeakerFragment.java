package com.globant.eventscorelib.baseFragments;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.globant.eventscorelib.R;
import com.globant.eventscorelib.baseComponents.BaseService;
import com.globant.eventscorelib.domainObjects.Speaker;
import com.globant.eventscorelib.utils.ConvertImage;
import com.globant.eventscorelib.utils.ErrorLabelLayout;
import com.software.shell.fab.ActionButton;

/**
 * A simple {@link Fragment} subclass.
 */
public class BaseSpeakerFragment extends BaseFragment {

    Bitmap mPhoto;
    ImageView mPhotoProfile;
    ActionButton mFloatingActionButtonPhoto;
    AppCompatEditText mEditTextFirstName;
    AppCompatEditText mEditTextLastName;
    AppCompatEditText mEditTextTitle;
    AppCompatEditText mEditTextBiography;
    AppCompatEditText mEditTextToChangeHint;

    String mHintToReturn;
    ImageView mIconFirstName;
    ImageView mIconLastName;
    ImageView mIconTitle;
    ImageView mIconBiography;

    ImageView mIconToChange;
    Drawable mDrawableToApply;
    final int GET_SPEAKER_IMAGE = 1;
    final int CROP_PIC = 2;
    final  int REQ_CODE_SPEAKER = 999;
    ErrorLabelLayout mErrorLabelLayoutFirstName;
    ErrorLabelLayout mErrorLabelLayoutLastName;
    ErrorLabelLayout mErrorLabelLayoutTitle;
    ErrorLabelLayout mErrorLabelLayoutBiography;
    ErrorLabelLayout mErrorLabelLayout;

    public static final String EXTRA_CROP = "crop";
    public static final String EXTRA_TRUE = "true";
    public static final String EXTRA_ASPECTX = "aspectX";
    public static final String EXTRA_ASPECTY = "aspectY";
    public static final String EXTRA_OUTPUTX = "outputX";
    public static final String EXTRA_OUTPUTY= "outputY";
    public static final String EXTRA_RETURN_DATA= "return-data";
    public static final String DATA= "data";
    public static final String IMAGE_CROP = "com.android.camera.action.CROP";
    public static final String URI_NAME = "image/*";

    public final String EDIT_MODE= "EDIT_MODE";
    public final String CREATE_MODE= "CREATE_MODE";
    private static final int RESULT_OK = 1;

    public Speaker speakerEdit;
    public String fragmentMode = CREATE_MODE;
    public String eventId;
    public int position;



    public BaseSpeakerFragment() {
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
        View rootView= inflater.inflate(R.layout.fragment_base_speaker, container, false);
        wireUpViews(rootView);
        prepareImageButton();
        setOnFocusListeners();
        hideUtilsAndShowContentOverlay();
        setScreenMode();
        return rootView;
    }

    private void setScreenMode() {
        if (getActivity().getIntent().getExtras()!=null) {
            if (getActivity().getIntent().getParcelableExtra("speaker")!=null)
            {
                  position = getActivity().getIntent().getExtras().getInt("position", -1);
                  speakerEdit =  getActivity().getIntent().getExtras().getParcelable("speaker");
                  mEditTextFirstName.setText(speakerEdit.getName());
                  mEditTextLastName.setText(speakerEdit.getLastName());
                  mEditTextTitle.setText(speakerEdit.getTitle());
                  mEditTextBiography.setText(speakerEdit.getBiography());
                  Bitmap photo = BitmapFactory.decodeByteArray(speakerEdit.getPicture(), 0, speakerEdit.getPicture().length);
                  mPhotoProfile.setImageBitmap(photo);
                  fragmentMode= EDIT_MODE;
             }
            if (getActivity().getIntent().getExtras().getSerializable("eventId") != null)
                eventId = getActivity().getIntent().getExtras().getString("eventId");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_speaker, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_done) {
            Boolean savePreferences=true;
            tintRequiredIconsAndShowError(mEditTextFirstName,  savePreferences);
            tintRequiredIconsAndShowError(mEditTextLastName,  savePreferences);
            tintRequiredIconsAndShowError(mEditTextTitle,  savePreferences);
            tintRequiredIconsAndShowError(mEditTextBiography,  savePreferences);

            if (savePreferences=true){
               Intent resultIntent = new Intent();
               switch (fragmentMode)
               {
                   case EDIT_MODE  :
                       resultIntent.putExtra("editedSpeaker",fillSpeakerObject());
                       //resultIntent.putExtra("position",position);
                       break;
                   case CREATE_MODE:
                       resultIntent.putExtra("newSpeaker",fillSpeakerObject());
                       break;
               }
                getActivity().setResult(RESULT_OK, resultIntent);
                getActivity().finish();
            }
            else{

                Toast.makeText(getActivity(), "Required fields missing!",
                        Toast.LENGTH_LONG).show();
            }
            return true;
        }

        return true;
    }

    private Speaker fillSpeakerObject() {
        Speaker sp = new Speaker();
        sp.setName(mEditTextFirstName.getText().toString());
        sp.setLastName(mEditTextLastName.getText().toString());
        sp.setTitle(mEditTextTitle.getText().toString());
        sp.setBiography(mEditTextBiography.getText().toString());
        Bitmap photo = ((BitmapDrawable) mPhotoProfile.getDrawable()).getBitmap();
        sp.setPicture(ConvertImage.convertBitmapImageToByteArray(photo));
        return sp;
    }

    @Override
    public String getTitle() {
        return "speaker";
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GET_SPEAKER_IMAGE) {
                //File file = new File(Environment.getExternalStorageDirectory() + File.separator + SHARED_PREF_IMG);
                performCrop(data.getData());
            } else if (requestCode == CROP_PIC) {
                // get the returned data
                Bundle extras = data.getExtras();
                // get the cropped bitmap
                mPhoto = extras.getParcelable(DATA);
                mPhotoProfile.setImageBitmap(mPhoto);
            }
        }
    }

    private void setOnFocusListeners() {
        mEditTextFirstName.setOnFocusChangeListener(editTextFocus);
        mEditTextLastName.setOnFocusChangeListener(editTextFocus);
        mEditTextTitle.setOnFocusChangeListener(editTextFocus);
        mEditTextBiography.setOnFocusChangeListener(editTextFocus);
    }

    private View.OnFocusChangeListener editTextFocus =  new View.OnFocusChangeListener() {
        public void onFocusChange(View view, boolean gainFocus) {

            getIconToTint(view);
            mEditTextToChangeHint=(AppCompatEditText) view;
            //onFocus
            if (gainFocus) {
                mDrawableToApply= DrawableCompat.wrap(mDrawableToApply);
                DrawableCompat.setTint(mDrawableToApply,getResources().getColor(R.color.ambar));
                mDrawableToApply=DrawableCompat.unwrap(mDrawableToApply);
                mIconToChange.setImageDrawable(mDrawableToApply);
                mErrorLabelLayout.clearError();
                mEditTextToChangeHint.setHint("");
            }
            //onBlur
            else {
                tintGrey();
            }
        };
    };

    private void getIconToTint(View view) {
        int id = view.getId();
        //noinspection SimplifiableIfStatement

        if (id== (R.id.edit_text_first_name)) {
            mIconToChange=mIconFirstName;
            mDrawableToApply=getResources().getDrawable(R.mipmap.ic_first_name);
            mErrorLabelLayout=mErrorLabelLayoutFirstName;
            mHintToReturn=getResources().getString(R.string.edit_text_first_name_hint);

        } else if (id== (R.id.edit_text_last_name)){
            mIconToChange=mIconLastName;
            mDrawableToApply=getResources().getDrawable(R.mipmap.ic_last_name);
            mErrorLabelLayout=mErrorLabelLayoutLastName;
            mHintToReturn=getResources().getString(R.string.edit_text_last_name_hint);


        }   else if (id== (R.id.edit_text_title)){
            mIconToChange=mIconTitle;
            mDrawableToApply=getResources().getDrawable(R.mipmap.ic_diploma);
            mErrorLabelLayout=mErrorLabelLayoutTitle;
            mHintToReturn=getResources().getString(R.string.edit_text_title_hint);
        }

        else if (id== (R.id.edit_text_biography)){
            mIconToChange=mIconBiography;
            mDrawableToApply=getResources().getDrawable(R.mipmap.ic_biography);
            mErrorLabelLayout=mErrorLabelLayoutBiography;
            mHintToReturn=getResources().getString(R.string.edit_text_biography_hint);
        }

    }

    private void tintGrey() {
        mDrawableToApply= DrawableCompat.wrap(mDrawableToApply);
        DrawableCompat.setTint(mDrawableToApply,getResources().getColor(R.color.grey_icon));
        mDrawableToApply=DrawableCompat.unwrap(mDrawableToApply);
        mIconToChange.setImageDrawable(mDrawableToApply);
    }

    private void wireUpViews(View rootView) {
        mEditTextFirstName=(AppCompatEditText)rootView.findViewById(R.id.edit_text_first_name);
        mEditTextLastName=(AppCompatEditText)rootView.findViewById(R.id.edit_text_last_name);
        mEditTextTitle=(AppCompatEditText)rootView.findViewById(R.id.edit_text_title);
        mEditTextBiography=(AppCompatEditText)rootView.findViewById(R.id.edit_text_biography);
        mFloatingActionButtonPhoto=(ActionButton)rootView.findViewById(R.id.fab);
        mPhotoProfile=(ImageView)rootView.findViewById(R.id.header);
        mIconFirstName=(ImageView)rootView.findViewById(R.id.icon_first_name);
        mIconLastName=(ImageView)rootView.findViewById(R.id.icon_last_name);
        mIconTitle=(ImageView)rootView.findViewById(R.id.icon_title);
        mIconBiography=(ImageView)rootView.findViewById(R.id.icon_biography);
        mErrorLabelLayoutFirstName = (ErrorLabelLayout) rootView.findViewById(R.id.nameErrorLayoutFirstName);
        mErrorLabelLayoutLastName = (ErrorLabelLayout) rootView.findViewById(R.id.nameErrorLayoutLastName);
        mErrorLabelLayoutTitle = (ErrorLabelLayout) rootView.findViewById(R.id.nameErrorLayoutTitle);
        mErrorLabelLayoutBiography = (ErrorLabelLayout) rootView.findViewById(R.id.nameErrorLayoutBiography);
    }

    private void prepareImageButton() {

        mFloatingActionButtonPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), GET_SPEAKER_IMAGE);
            }
        });
    }

    private void performCrop(Uri picUri) {
        // take care of exceptions
        // call the standard crop action intent (the user device may not
        // support it)
        try {
            Intent cropIntent = new Intent(IMAGE_CROP);
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, URI_NAME);
            // set crop properties
            cropIntent.putExtra(EXTRA_CROP, EXTRA_TRUE);
            // indicate aspect of desired crop
            cropIntent.putExtra(EXTRA_ASPECTX, mPhotoProfile.getWidth());
            cropIntent.putExtra(EXTRA_ASPECTY,  mPhotoProfile.getHeight());
            // indicate output X and Y
            cropIntent.putExtra(EXTRA_OUTPUTX, mPhotoProfile.getWidth());
            cropIntent.putExtra(EXTRA_OUTPUTY, mPhotoProfile.getHeight());
            // retrieve data on return
            cropIntent.putExtra(EXTRA_RETURN_DATA, true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, CROP_PIC);
        }
        catch (Exception ex)
        {
            Toast.makeText(getActivity().getBaseContext(), "fds", Toast.LENGTH_SHORT).show();
        }

    }

    private void tintRequiredIconsAndShowError(EditText requiredField,  Boolean savePreferences){
        getIconToTint(requiredField);

        if (requiredField.getText().toString().trim().length() == 0) {
            mErrorLabelLayout.setError(getResources().getString(R.string.field_required));
            mDrawableToApply=DrawableCompat.wrap(mDrawableToApply);
            DrawableCompat.setTint(mDrawableToApply,getResources().getColor(R.color.red_error));
            mDrawableToApply=DrawableCompat.unwrap(mDrawableToApply);
            mIconToChange.setImageDrawable(mDrawableToApply);
        }
        else{
            tintGrey();
        }
    }


}
