package com.globant.eventscorelib.baseComponents;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.globant.eventscorelib.R;
import com.software.shell.fab.ActionButton;
import com.software.shell.fab.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.File;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends BaseFragment {

    public final static Integer REQUEST_CODE = 0;
    Bitmap mPhoto;
    byte[] mImage;
    ImageView mPhotoProfile;
    ActionButton mFloatingActionButtonPhoto;

    final int CAMERA_CAPTURE = 1;
    final int CROP_PIC = 2;
    private Uri picUri;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateEventView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_profile, container, false);
        mFloatingActionButtonPhoto=(ActionButton)rootView.findViewById(R.id.fab);
        mPhotoProfile=(ImageView)rootView.findViewById(R.id.header);
        prepareImageButton();
        hideUtilsAndShowContentOverlay();
        return rootView;
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
                Bitmap mPhoto = extras.getParcelable("data");
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

}

