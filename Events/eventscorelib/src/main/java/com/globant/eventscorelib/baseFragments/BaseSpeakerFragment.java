package com.globant.eventscorelib.baseFragments;


import android.app.Activity;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
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
import com.globant.eventscorelib.utils.CoreConstants;
import com.globant.eventscorelib.utils.ErrorLabelLayout;
import com.software.shell.fab.ActionButton;

/**
 * A simple {@link Fragment} subclass.
 */
public class BaseSpeakerFragment extends BaseFragment {

    public static final String DATA= "data";
    public static final String EDITED_SPEAKER = "editedSpeaker";
    public static final String POSITION = "position";
    public static final String DELETED_SPEAKER = "deletedSpeaker";
    public static final String NEW_SPEAKER = "newSpeaker";
    public static final String EDIT_MODE= "EDIT_MODE";
    public static final String CREATE_MODE= "CREATE_MODE";
    public static final int RESULT_OK = 1;
    public static final String REQUIRED_FIELDS_MISSING = "Required fields missing!";
    public Speaker speakerEdit;
    public String fragmentMode = CREATE_MODE;
    public String eventId;
    public int position;
    Boolean mDoneClicked = false;
    Boolean mPhotoPicked = false;

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

    ErrorLabelLayout mErrorLabelLayoutFirstName;
    ErrorLabelLayout mErrorLabelLayoutLastName;
    ErrorLabelLayout mErrorLabelLayoutTitle;
    ErrorLabelLayout mErrorLabelLayoutBiography;
    ErrorLabelLayout mErrorLabelLayout;

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
                  position = getActivity().getIntent().getExtras().getInt(POSITION, -1);
                  speakerEdit =  getActivity().getIntent().getExtras().getParcelable("speaker");
                  mEditTextFirstName.setText(speakerEdit.getName());
                  mEditTextLastName.setText(speakerEdit.getLastName());
                  mEditTextTitle.setText(speakerEdit.getTitle());
                  mEditTextBiography.setText(speakerEdit.getBiography());
                  mPhotoProfile.setImageBitmap(speakerEdit.getPicture());
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
        inflater.inflate(R.menu.menu_base_speaker, menu);
        MenuItem item = menu.findItem(R.id.events_action_delete);

        if (fragmentMode.equals(EDIT_MODE)) {
            item.setVisible(true);
        } else {
            item.setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_done) {
            doneClick();
            if(fragmentMode.equals(EDIT_MODE))
                mPhotoPicked=true;

            if (mDoneClicked && mPhotoPicked ){
               Intent resultIntent = new Intent();
               switch (fragmentMode)
               {
                   case EDIT_MODE  :
                       resultIntent.putExtra(EDITED_SPEAKER,fillSpeakerObject());
                       resultIntent.putExtra(POSITION,position);
                       break;
                   case CREATE_MODE:
                       resultIntent.putExtra(NEW_SPEAKER,fillSpeakerObject());
                       break;
               }
                getActivity().setResult(RESULT_OK, resultIntent);
                getActivity().finish();
            }
            else if (!mPhotoPicked) {
                Toast.makeText(getActivity(), getResources().getString(R.string.missing_photo), Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getActivity(),REQUIRED_FIELDS_MISSING,Toast.LENGTH_LONG).show();
            }
            return true;
        }
        if (id == R.id.events_action_delete) {
            new AlertDialog.Builder(getActivity())
                    .setTitle(getString(R.string.alert_message_delete_speaker))
                    .setMessage(getString(R.string.alert_message_delete_speaker))
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            if (fragmentMode.equals(EDIT_MODE)) {
                                Intent resultIntent = new Intent();
                                resultIntent.putExtra(DELETED_SPEAKER, fillSpeakerObject());
                                resultIntent.putExtra(POSITION, position);
                                getActivity().setResult(RESULT_OK, resultIntent);
                                getActivity().finish();
                            }

                        }
                    })
                    .setNegativeButton(android.R.string.no, null).show();
            //handled = true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void doneClick() {
        mDoneClicked = true;
        mDoneClicked &= tintRequiredIconsAndShowError(mEditTextFirstName);
        mDoneClicked &= tintRequiredIconsAndShowError(mEditTextLastName);
        mDoneClicked &= tintRequiredIconsAndShowError(mEditTextTitle);
        mDoneClicked &= tintRequiredIconsAndShowError(mEditTextBiography);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            {
                if (!(savedInstanceState.getString(CoreConstants.DONE_CLICKED).equals("false")))
                    doneClick();
            }
            Bitmap bitmapToSave = savedInstanceState.getParcelable(CoreConstants.PHOTO_ROTATE);
            mPhotoProfile.setImageBitmap(bitmapToSave);
            mPhotoPicked = Boolean.parseBoolean(savedInstanceState.getString(CoreConstants.PHOTO_TAKEN));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(CoreConstants.DONE_CLICKED, mDoneClicked.toString());
        outState.putString(CoreConstants.PHOTO_TAKEN, mPhotoPicked.toString());
        BitmapDrawable drawable = (BitmapDrawable) mPhotoProfile.getDrawable();
        Bitmap bitmapToSave = drawable.getBitmap();
        outState.putParcelable(CoreConstants.PHOTO_ROTATE, bitmapToSave);
    }

    private Speaker fillSpeakerObject() {
        Speaker speaker = new Speaker();
        speaker.setName(mEditTextFirstName.getText().toString());
        speaker.setLastName(mEditTextLastName.getText().toString());
        speaker.setTitle(mEditTextTitle.getText().toString());
        speaker.setBiography(mEditTextBiography.getText().toString());
        speaker.setPicture(((BitmapDrawable)mPhotoProfile.getDrawable()).getBitmap());
        return speaker;
    }

    @Override
    public String getTitle() {
        return "speaker";
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CoreConstants.PICTURE_SELECTION_REQUEST) {

                Intent cropIntent = ConvertImage.performCrop(data.getData());
                startActivityForResult(cropIntent, CoreConstants.PICTURE_CROP_SELECTION_REQUEST);

            } else if (requestCode == CoreConstants.PICTURE_CROP_SELECTION_REQUEST) {
                // get the returned data
                Bundle extras = data.getExtras();
                // get the cropped bitmap
                mPhoto = extras.getParcelable(DATA);
                mPhotoProfile.setImageBitmap(mPhoto);
                mPhotoPicked = true;
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
        }
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
        mErrorLabelLayoutLastName = (ErrorLabelLayout) rootView.findViewById(R.id.name_error_layout_last_name);
        mErrorLabelLayoutTitle = (ErrorLabelLayout) rootView.findViewById(R.id.name_error_layout_title);
        mErrorLabelLayoutBiography = (ErrorLabelLayout) rootView.findViewById(R.id.nameErrorLayoutBiography);
    }

    private void prepareImageButton() {

        mFloatingActionButtonPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                if (Build.VERSION.SDK_INT < 19){
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                } else {
                    intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                }

                startActivityForResult(intent, CoreConstants.PICTURE_SELECTION_REQUEST);
            }
        });
    }

    private boolean tintRequiredIconsAndShowError(EditText requiredField){
         getIconToTint(requiredField);
         if (requiredField.getText().toString().trim().length() == 0) {
                mErrorLabelLayout.setError(getResources().getString(R.string.field_required));
                mDrawableToApply = DrawableCompat.wrap(mDrawableToApply);
                DrawableCompat.setTint(mDrawableToApply, getResources().getColor(R.color.red_error));
                mDrawableToApply = DrawableCompat.unwrap(mDrawableToApply);
                mIconToChange.setImageDrawable(mDrawableToApply);
                return false;
            } else {
                tintGrey();
                return true;

            }
    }
}
