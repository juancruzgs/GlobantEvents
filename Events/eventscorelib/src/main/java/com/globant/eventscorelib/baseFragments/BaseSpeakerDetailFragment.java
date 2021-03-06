package com.globant.eventscorelib.baseFragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.globant.eventscorelib.R;
import com.globant.eventscorelib.baseComponents.BaseService;
import com.globant.eventscorelib.domainObjects.Speaker;

/**
 * Created by juan.ramirez on 16/04/2015.
 */
public class BaseSpeakerDetailFragment extends BaseFragment {

    private Speaker speaker;
    private AppCompatTextView fullName, speakerTitle, speakerBiography;
    private ImageView speakerImage;

     public BaseSpeakerDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public BaseService.ActionListener getActionListener() {
        return null;
    }

    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_speaker_detail, container, false);
        hideUtilsAndShowContentOverlay();
        speaker = getActivity().getIntent().getExtras().getParcelable("speaker");
        wireSpeakerView(rootView);
        return rootView;
    }

    private void wireSpeakerView(View rootView) {
        fullName = (AppCompatTextView) rootView.findViewById(R.id.speaker_fullName);
        speakerTitle = (AppCompatTextView) rootView.findViewById(R.id.speaker_title);
        speakerBiography = (AppCompatTextView) rootView.findViewById(R.id.speaker_biography);
        speakerImage = (ImageView)rootView.findViewById(R.id.speaker_picture);
        fullName.setText(speaker.getName()+" "+speaker.getLastName());
        speakerTitle.setText(speaker.getTitle());
        speakerBiography.setText(speaker.getBiography());
        if (speaker.getPicture()!= null)
            speakerImage.setImageBitmap(BitmapFactory.decodeByteArray(speaker.getPicture(),0, speaker.getPicture().length));
            speakerImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    @Override
    public String getTitle() {
        return getString(R.string.speaker_detail_fragment);
    }

}
