package com.globant.eventmanager.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.globant.eventmanager.activities.EventsManagerPagerActivity;
import com.globant.eventmanager.activities.TestActivity;
import com.globant.eventscorelib.baseAdapters.BaseSpeakersListAdapter;
import com.globant.eventscorelib.baseComponents.BaseService;
import com.globant.eventscorelib.baseFragments.BaseSpeakersListFragment;
import com.globant.eventscorelib.domainObjects.Speaker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by juan.ramirez on 13/05/2015.
 */
public class EventSpeakersList extends BaseSpeakersListFragment{

    public final static int REQ_CODE_SPEAKER = 999;
    private static final int RESULT_OK = 1;
    private String eventId;



    @Override
    public BaseService.ActionListener getActionListener() {
        return super.getActionListener();
    }

    @Override
    public String getBindingKey() {
        return super.getBindingKey();
    }


    @Override
    public void onFinishAction(BaseService.ACTIONS theAction, Object result) {
        if (theAction == BaseService.ACTIONS.EVENT_SPEAKERS) {
            mSpeakers = (List<Speaker>) result;
            if ((mSpeakers.size()) >= 1) {
                ((EventsManagerPagerActivity) getActivity()).setSpeakersList(mSpeakers);
                setRecyclerViewAdapter();
            } else {
                mRecyclerView.setVisibility(View.GONE);
                mTextViewNoSpeakers.setVisibility(View.VISIBLE);
            }
            hideUtilsAndShowContentOverlay();
        }
    }


    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =    super.onCreateEventView(inflater, container, savedInstanceState);
        prepareRecyclerView(rootView);
        return  rootView;
    }

    private void prepareRecyclerView(View rootView) {
        int scrollPosition = 0;
        mRecyclerView = (RecyclerView) rootView.findViewById(com.globant.eventscorelib.R.id.speaker_list_recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
        mRecyclerView.setHasFixedSize(true);
        mActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            // add new speaker to current event
            public void onClick(View v) {
                    Intent intentSpeakerCreate = new Intent(getActivity(), TestActivity.class);
                    startActivityForResult(intentSpeakerCreate, REQ_CODE_SPEAKER);
            }
        });



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQ_CODE_SPEAKER)
        {
            if (resultCode == RESULT_OK) {
                Speaker newSpeaker =  data.getParcelableExtra("newSpeaker");
                    if(mSpeakers == null) {
                        mSpeakers = new ArrayList<>();
                        mSpeakers.add(newSpeaker);
                        mAdapter = new BaseSpeakersListAdapter(getActivity(), mSpeakers);
                        mRecyclerView.setAdapter(mAdapter);
                        EventsManagerPagerActivity.getInstance().setSpeakersList(mSpeakers);
                        mAdapter.notifyDataSetChanged();
                    }
                    else
                    {   mAdapter.addSpeaker(newSpeaker);
                        mAdapter.notifyDataSetChanged();
                    }

                }
        }
    }

    @Override
    public void onResumeFragment() {
        //super.onResumeFragment();
        mSpeakers = ((EventsManagerPagerActivity) getActivity()).getEvent().getSpeakers();
        String eventId = ((EventsManagerPagerActivity) getActivity()).getEvent().getObjectID();
        setRecyclerViewAdapter();
    }



    private void setRecyclerViewAdapter() {
        mAdapter = new BaseSpeakersListAdapter(getActivity(), mSpeakers);
        mRecyclerView.setAdapter(mAdapter);
        if (mSpeakers !=null)
            if (mSpeakers.size() <1){
                mRecyclerView.setVisibility(View.GONE);
                mTextViewNoSpeakers.setVisibility(View.VISIBLE);
            }
            else {
            mRecyclerView.setVisibility(View.GONE);
            mTextViewNoSpeakers.setVisibility(View.VISIBLE);
            }
        else
        {
            mRecyclerView.setVisibility(View.GONE);
            mTextViewNoSpeakers.setVisibility(View.VISIBLE);
        }
        hideUtilsAndShowContentOverlay();
    }

}
