package com.globant.eventscorelib.baseComponents;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.globant.eventscorelib.R;

import java.io.ByteArrayOutputStream;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends BaseFragment {

    public final static Integer REQUEST_CODE = 0;
    Bitmap mPhoto;
    byte[] mImage;
    ImageButton mPhotoProfile;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateEventView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_profile, container, false);
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
        if (requestCode == ProfileFragment.REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            mPhoto = (Bitmap) data.getExtras().get("data");
            mPhotoProfile.setImageBitmap(mPhoto);
        }
    }

    private void convertBitmapImageToByteArray() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        mPhoto.compress(Bitmap.CompressFormat.PNG, 100, stream);
        mImage = stream.toByteArray();
    }

    private void prepareImageButton() {

        mPhoto = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.profile_placeholder);

        mPhotoProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, ProfileFragment.REQUEST_CODE);
            }
        });
    }
}
