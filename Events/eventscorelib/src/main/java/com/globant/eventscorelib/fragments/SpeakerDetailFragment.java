package com.globant.eventscorelib.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.globant.eventscorelib.R;
import com.globant.eventscorelib.baseComponents.BaseFragment;
import com.globant.eventscorelib.domainObjects.Speaker;

/**
 * Created by juan.ramirez on 16/04/2015.
 */
public class SpeakerDetailFragment extends BaseFragment {

    //TODO delete this  dummy object
    private Speaker speakerDemo  = new Speaker("Aerospace Engineer",
            "Alejandra",
            "Buitrago",
            "She became an aviation enthusiast early in life. She gained BA in mechanical engineering from the University of California, Berkeley, MA in aeronautical engineering from Princeton University, and Ph.D. in engineering from the Tokyo University of Science. Alejandra joined the Boeing company in 1965 as an aerodynamics engineer. Later, she became a lead engineer on the Boeing 747 high-speed configuration.  She advanced into management within a year and became manager of the Boeing 727 marketing. In 1996, She was elected president of Boeing, and a member of the board of directors. She remained in this position until resigning on 1 December 2003."
            ,null);

    private TextView fullName, speakerTitle, speakerBiography;

     public SpeakerDetailFragment() {
        // Required empty public constructor
    }

    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_speaker_detail, container, false);
        hideUtilsAndShowContentOverlay();
        wireSpeakerView(rootView);
        return rootView;
    }

    private void wireSpeakerView(View rootView) {
        fullName = (TextView) rootView.findViewById(R.id.speaker_fullName);
        speakerTitle = (TextView) rootView.findViewById(R.id.speaker_title);
        speakerBiography = (TextView) rootView.findViewById(R.id.speaker_biography);
        fullName.setText(speakerDemo.getName()+" "+speakerDemo.getLastName());
        speakerTitle.setText(speakerDemo.getTitle());
        speakerBiography.setText(speakerDemo.getBiography());
    }

    @Override
    public String getTitle() {
        return getString(R.string.speaker_detail_fragment);
    }

}
