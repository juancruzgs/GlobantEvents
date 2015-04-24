package com.globant.eventscorelib.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.globant.eventscorelib.R;
import com.globant.eventscorelib.adapters.SpeakersListAdapter;
import com.globant.eventscorelib.baseComponents.BaseFragment;
import com.globant.eventscorelib.baseComponents.BaseService;
import com.globant.eventscorelib.baseComponents.CloudDataController;
import com.globant.eventscorelib.baseComponents.CreditsActivity;
import com.globant.eventscorelib.baseComponents.SpeakerDetailActivity;
import com.globant.eventscorelib.domainObjects.Speaker;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

/**
        * Created by agustin.gugliotta on 15/04/2015.
        */
public class SpeakersListFragment extends BaseFragment implements BaseService.ActionListener{

    private List<Speaker> mSpeakers = new ArrayList();

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    protected LayoutManagerType mCurrentLayoutManagerType;
    protected RecyclerView mRecyclerView;
    protected SpeakersListAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;

    @Override
    public Activity getBindingActivity() {
        return getActivity();
    }

    @Override
    public Object getBindingKey() {
        return null;
    }

    @Override
    public void onStartAction(BaseService.ACTIONS theAction) {
        showProgressOverlay("Loading Speakers");
    }

    @Override
    public void onFinishAction(BaseService.ACTIONS theAction, Object result) {
        if (theAction == BaseService.ACTIONS.EVENT_SPEAKERS) {
            mSpeakers = (List<Speaker>) result;
            mAdapter = new SpeakersListAdapter(getActivity(), mSpeakers);
            mRecyclerView.setAdapter(mAdapter);
        }
        hideUtilsAndShowContentOverlay();
    }

    @Override
    public void onFailAction(BaseService.ACTIONS theAction, Exception e) {
    }

    public SpeakersListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setService(BaseService service) {
        super.setService(service);
        mService.executeAction(BaseService.ACTIONS.EVENT_SPEAKERS, "5vs7DC2RnQ");
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

        mAdapter = new SpeakersListAdapter(getActivity(),mSpeakers);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setHasFixedSize(true);

        hideUtilsAndShowContentOverlay();

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        //TODO juan.ramirez,  send speaker id or speaker object from backend.
                        Intent intentSpeakerDetail = new Intent(getActivity(), SpeakerDetailActivity.class);
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
