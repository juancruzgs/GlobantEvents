package com.globant.eventmanager.fragments;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.globant.eventscorelib.R;

import com.globant.eventmanager.activities.EventSpeakerListActivity;
import com.globant.eventmanager.activities.EventsManagerPagerActivity;
import com.globant.eventmanager.activities.TestActivity;
import com.globant.eventscorelib.baseActivities.BaseEventDetailPagerActivity;
import com.globant.eventscorelib.baseActivities.BaseSpeakerDetailActivity;
import com.globant.eventscorelib.baseAdapters.BaseSpeakersListAdapter;
import com.globant.eventscorelib.baseAdapters.RecyclerItemClickListener;
import com.globant.eventscorelib.baseComponents.BaseService;
import com.globant.eventscorelib.baseFragments.BaseSpeakersListFragment;
import com.globant.eventscorelib.domainObjects.Event;
import com.globant.eventscorelib.domainObjects.Speaker;
import com.globant.eventscorelib.utils.CoreConstants;

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
    private String LOG_TAG = getClass().getSimpleName();

    @Override
    public BaseService.ActionListener getActionListener() {
        return super.getActionListener();
    }

    @Override
    public String getBindingKey() {
        return super.getBindingKey();
    }

    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(LOG_TAG, Thread.currentThread().getStackTrace()[2].getMethodName());
        View rootView =    super.onCreateEventView(inflater, container, savedInstanceState);
        mActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            // add new speaker to current event
            public void onClick(View v) {
                Intent intentSpeakerCreate = new Intent(getActivity(), EventSpeakerListActivity.class);
                startActivityForResult(intentSpeakerCreate, REQ_CODE_SPEAKER);
                }
        });
        mActionButton.show();
        return  rootView;
    }



    @Override
    protected void prepareRecyclerView(View rootView) {
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
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intentSpeakerEdit = new Intent(getActivity(), EventSpeakerListActivity.class);
                        intentSpeakerEdit.putExtra("speaker", mSpeakers.get(position));
                        intentSpeakerEdit.putExtra("position", position);
                        Log.d(LOG_TAG,"ITEM A EDITAR-> "+Integer.toString(position));

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            //ImageView cardImage = (ImageView) view.findViewById(R.id.image_view_profile_speaker);
                            //ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), view, "cardImage");
                            startActivityForResult(intentSpeakerEdit,REQ_CODE_SPEAKER);
                        }
                        else {
                            startActivityForResult(intentSpeakerEdit, REQ_CODE_SPEAKER);
                        }
                    }
                })
        );
        mSpeakers = ((EventsManagerPagerActivity) getActivity()).getEvent().getSpeakers();
        eventId = ((EventsManagerPagerActivity) getActivity()).getEvent().getObjectID();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(LOG_TAG,Thread.currentThread().getStackTrace()[2].getMethodName().toString());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.d(LOG_TAG,Thread.currentThread().getStackTrace()[2].getMethodName().toString());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQ_CODE_SPEAKER)
        {
            if (resultCode == RESULT_OK) {
                Speaker newSpeaker =  data.getParcelableExtra("newSpeaker");
                Speaker editedSpeaker = data.getParcelableExtra("editedSpeaker");
                Speaker deletedSpeaker = data.getParcelableExtra("deletedSpeaker");

                if(newSpeaker!=null)
                {
                    if(mSpeakers == null) {
                        mSpeakers = new ArrayList<>();
                        mSpeakers.add(newSpeaker);
                        mAdapter = new BaseSpeakersListAdapter(getActivity(), mSpeakers);
                        mRecyclerView.setAdapter(mAdapter);
                        EventsManagerPagerActivity.getInstance().setSpeakersList(mSpeakers);
                        mAdapter.notifyDataSetChanged();
                    }
                    else
                    {
                        mAdapter.addSpeaker(newSpeaker);
                        mAdapter.notifyDataSetChanged();
                        mRecyclerView.setVisibility(View.VISIBLE);
                        mTextViewNoSpeakers.setVisibility(View.VISIBLE);
                        EventsManagerPagerActivity.getInstance().setSpeakersList(mSpeakers);
                    }
                }
                if(editedSpeaker!= null)
                {
                    int position = data.getIntExtra("position",0);
                    Log.d(LOG_TAG,"ITEM TO REPLACE-> "+Integer.toString(position));
                    mSpeakers.set(position, editedSpeaker);
                    mAdapter.notifyItemChanged(position);
                    EventsManagerPagerActivity.getInstance().setSpeakersList(mSpeakers);
                }
                if(deletedSpeaker != null)
                {
                    int position = data.getIntExtra("position",0);
                    Log.d(LOG_TAG,"ITEM TO DELETE-> "+Integer.toString(position));
                    mSpeakers.remove(position);
                    mAdapter.notifyItemRemoved(position);
                    EventsManagerPagerActivity.getInstance().setSpeakersList(mSpeakers);
                    setNotSpeakerOnListMessage();
                }
            }
        }
    }


    @Override
    public void onResumeFragment() {
        Log.d(LOG_TAG,Thread.currentThread().getStackTrace()[2].getMethodName());
        if(mSpeakers==null) {
            if (eventId != null) {
                mService.executeAction(BaseService.ACTIONS.EVENT_SPEAKERS, getBindingKey(), eventId);
            }
        }
        else {
            setRecyclerViewAdapter();
        }
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

    private void setRecyclerViewAdapter() {
        mAdapter = new BaseSpeakersListAdapter(getActivity(), mSpeakers);
        mRecyclerView.setAdapter(mAdapter);
        setNotSpeakerOnListMessage();
    }

    private void setNotSpeakerOnListMessage() {
        if (mSpeakers !=null)
            if (mSpeakers.size() <1){
                mRecyclerView.setVisibility(View.GONE);
                mTextViewNoSpeakers.setVisibility(View.VISIBLE);
            }
        hideUtilsAndShowContentOverlay();
    }

}
