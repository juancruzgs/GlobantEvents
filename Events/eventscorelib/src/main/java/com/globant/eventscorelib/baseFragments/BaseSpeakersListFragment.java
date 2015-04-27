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
import com.globant.eventscorelib.baseAdapters.BaseSpeakersListAdapter;
import com.globant.eventscorelib.baseComponents.BaseService;
import com.globant.eventscorelib.baseActivities.BaseSpeakerDetailActivity;
import com.globant.eventscorelib.domainObjects.Speaker;
import com.globant.eventscorelib.baseAdapters.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
        * Created by agustin.gugliotta on 15/04/2015.
        */
public class BaseSpeakersListFragment extends BaseFragment implements BaseService.ActionListener{

    private List<Speaker> mSpeakers = new ArrayList();

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    protected LayoutManagerType mCurrentLayoutManagerType;
    protected RecyclerView mRecyclerView;
    protected BaseSpeakersListAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;

    @Override
    public Activity getBindingActivity() {
        return getActivity();
    }

    @Override
    public String getBindingKey() {
        // TODO: Return an appropriated key
        return "BaseSpeakersListFragment";
    }

    @Override
    public void onStartAction(BaseService.ACTIONS theAction) {

    }

    @Override
    public void onFinishAction(BaseService.ACTIONS theAction, Object result) {
        if (theAction == BaseService.ACTIONS.EVENT_SPEAKERS) {
            mSpeakers = (List<Speaker>) result;
            mAdapter = new BaseSpeakersListAdapter(getActivity(), mSpeakers);
            mRecyclerView.setAdapter(mAdapter);
        }
        hideUtilsAndShowContentOverlay();
    }

    @Override
    public void onFailAction(BaseService.ACTIONS theAction, Exception e) {
        showErrorOverlay();
    }

    public BaseSpeakersListFragment() {
    }



    @Override
    public void setService(BaseService service) {
        super.setService(service);
        mService.executeAction(BaseService.ACTIONS.EVENT_SPEAKERS, "5vs7DC2RnQ", getBindingKey());
    }

    @Override
    public BaseService.ActionListener getActionListener() {
        return this;
    }

    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int scrollPosition = 0;
        View rootView = inflater.inflate(R.layout.fragment_speaker_list, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.speaker_list_recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);

        mAdapter = new BaseSpeakersListAdapter(getActivity(),mSpeakers);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setHasFixedSize(true);
        if (mAdapter.getItemCount() == 0){
            showProgressOverlay();
        } else {
            hideUtilsAndShowContentOverlay();
        }
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        //TODO juan.ramirez,  send speaker id or speaker object from backend.
                        Intent intentSpeakerDetail = new Intent(getActivity(), BaseSpeakerDetailActivity.class);
                        startActivity(intentSpeakerDetail);
                    }
                })
        );

        return rootView;
    }

    @Override
    public String getTitle() {
        //TODO change hardcoded string
        return "Speakers";
    }


}
