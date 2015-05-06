package com.globant.eventscorelib.baseFragments;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.globant.eventscorelib.R;
import com.globant.eventscorelib.baseComponents.BaseService;
import com.globant.eventscorelib.domainObjects.Speaker;
import com.globant.eventscorelib.utils.CoreConstants;

/**
 * Created by juan.ramirez on 16/04/2015.
 */
public class BaseSpeakerDetailFragment extends BaseFragment {


    private TextView mFullNameTextView, mSpeakerTitleTextView, mSpeakerBiographyTextView;
    private Speaker mSpeaker;
    private ImageView mSpeakerImage;

     public BaseSpeakerDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public BaseService.ActionListener getActionListener() {
        return null;
    }

    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_speaker_detail, container, false);
        hideUtilsAndShowContentOverlay();
        mSpeaker = (Speaker) getActivity().getIntent().getExtras().getSerializable(CoreConstants.SPEAKER_SELECTED);
        wireSpeakerView(rootView);
        return rootView;
    }

    private void wireSpeakerView(View rootView) {
            mFullNameTextView = (TextView) rootView.findViewById(R.id.speaker_fullName);
            mSpeakerTitleTextView = (TextView) rootView.findViewById(R.id.speaker_title);
            mSpeakerBiographyTextView = (TextView) rootView.findViewById(R.id.speaker_biography);
            mSpeakerImage = (ImageView)rootView.findViewById(R.id.speaker_picture);
            mFullNameTextView.setText(mSpeaker.getName() + " " + mSpeaker.getLastName());
            mSpeakerTitleTextView.setText(mSpeaker.getTitle());
            mSpeakerBiographyTextView.setText(mSpeaker.getBiography());
            mSpeakerImage.setImageBitmap(BitmapFactory.decodeByteArray(mSpeaker.getPicture(), 0, mSpeaker.getPicture().length));
    }

    @Override
    public String getTitle() {
        return getString(R.string.speaker_detail_fragment);
    }

}
