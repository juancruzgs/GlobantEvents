package com.globant.eventscorelib.baseFragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.globant.eventscorelib.R;
import com.globant.eventscorelib.baseActivities.BaseEventDetailPagerActivity;
import com.globant.eventscorelib.baseActivities.BasePagerActivity;
import com.globant.eventscorelib.baseAdapters.BaseSpeakersListAdapter;
import com.globant.eventscorelib.baseComponents.BaseApplication;
import com.globant.eventscorelib.baseComponents.BaseService;
import com.globant.eventscorelib.baseActivities.BaseSpeakerDetailActivity;
import com.globant.eventscorelib.domainObjects.Event;
import com.globant.eventscorelib.domainObjects.Speaker;
import com.globant.eventscorelib.baseAdapters.RecyclerItemClickListener;
import com.globant.eventscorelib.utils.CoreConstants;
import com.globant.eventscorelib.utils.Logger;

import java.util.ArrayList;
import java.util.List;

/**
        * Created by agustin.gugliotta on 15/04/2015.
        */
public class BaseSpeakersListFragment extends BaseFragment implements BaseService.ActionListener, BasePagerActivity.FragmentLifecycle{

    private List<Speaker> mSpeakers;
    protected RecyclerView mRecyclerView;
    protected BaseSpeakersListAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    private Event mEvent;

    @Override
    public BaseService.ActionListener getActionListener() {
        return this;
    }

    @Override
    public Activity getBindingActivity() {
        return getActivity();
    }

    @Override
    public String getBindingKey() {
        return BaseSpeakersListFragment.class.getSimpleName();
    }

    @Override
    public void onStartAction(BaseService.ACTIONS theAction) {
        showProgressOverlay();
    }

    @Override
    public void onFinishAction(BaseService.ACTIONS theAction, Object result) {
        if (theAction == BaseService.ACTIONS.EVENT_SPEAKERS) {
            mSpeakers = (List<Speaker>) result;
            if (mSpeakers!=null)
            setRecyclerViewAdapter();
            hideUtilsAndShowContentOverlay();
            }
        else{

        }

        }


    private void setRecyclerViewAdapter() {
        mAdapter = new BaseSpeakersListAdapter(getActivity(), mSpeakers);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onFailAction(BaseService.ACTIONS theAction, Exception e) {
        showErrorOverlay();
    }

    public BaseSpeakersListFragment() {
    }

    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_speaker_list, container, false);
        hideUtilsAndShowContentOverlay();
        prepareRecyclerView(rootView);
        setRetainInstance(true);
        return rootView;
    }

    private void prepareRecyclerView(View rootView) {
        int scrollPosition = 0;
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.speaker_list_recycler_view);
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
                    @Override public void onItemClick(View view, int position) {
                        //TODO Transition
                        Intent intentSpeakerDetail = new Intent(getActivity(), BaseSpeakerDetailActivity.class);
                        intentSpeakerDetail.putExtra(CoreConstants.SPEAKER_SELECTED,mSpeakers.get(position));
                        startActivity(intentSpeakerDetail);
                    }
                })
        );
    }

    @Override
    public String getTitle() {
        return getString(R.string.title_base_speaker_list_fragment);
    }

    @Override
    public void onPauseFragment() {

    }

    @Override
    public void onResumeFragment() {
        if (mSpeakers == null) {
            mEvent=BaseApplication.getInstance().getEvent();
            String eventId=mEvent.getObjectID();
            mService.executeAction(BaseService.ACTIONS.EVENT_SPEAKERS, eventId, getBindingKey());
        }
        else {
            setRecyclerViewAdapter();
        }

    }


}
