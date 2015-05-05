package com.globant.eventscorelib.baseFragments;


import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.globant.eventscorelib.R;
import com.globant.eventscorelib.baseActivities.BasePagerActivity;
import com.globant.eventscorelib.baseActivities.BaseSpeakerActivity;
import com.globant.eventscorelib.baseActivities.BaseSpeakerDetailActivity;
import com.globant.eventscorelib.baseAdapters.BaseEventsListAdapter;
import com.globant.eventscorelib.baseAdapters.BaseSpeakersListAdapter;
import com.globant.eventscorelib.baseAdapters.RecyclerItemClickListener;
import com.globant.eventscorelib.baseComponents.BaseService;
import com.globant.eventscorelib.domainObjects.Speaker;
import com.software.shell.fab.ActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by juan.ramirez on 04/05/2015.
 */
public class BaseSpeakersListAllFragment extends BaseFragment implements BaseService.ActionListener, BasePagerActivity.FragmentLifecycle {
    private List<Speaker> mAllSpeakers = new ArrayList();
    private ActionButton mActionButton;

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
            mAllSpeakers = (List<Speaker>) result;
            mAdapter = new BaseSpeakersListAdapter(getActivity(), mAllSpeakers);
            mRecyclerView.setAdapter(mAdapter);
        }
        hideUtilsAndShowContentOverlay();
    }

    @Override
    public void onFailAction(BaseService.ACTIONS theAction, Exception e) {
        showErrorOverlay();
    }

    public BaseSpeakersListAllFragment() {
    }



    @Override
    public void setService(BaseService service) {
        super.setService(service);
        //TODO  juan.ramirez make a getallspeakers request
        mService.executeAction(BaseService.ACTIONS.EVENT_SPEAKERS, "5vs7DC2RnQ", getBindingKey());
    }

    @Override
    public BaseService.ActionListener getActionListener() {
        return this;
    }

    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int scrollPosition = 0;
        final View rootView = inflater.inflate(R.layout.fragment_base_speakers_list_all, container, false);
        wireUpFAB(rootView);

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

        mAdapter = new BaseSpeakersListAdapter(getActivity(),mAllSpeakers);
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
                        //TODO juan.ramirez,  ask to user if want edit this speaker.
                        //Log.d("fasdfa", "click en "+Integer.toString(position));
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            ImageView cardImage = (ImageView) rootView.findViewById(R.id.image_view_profile_speaker);
                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(),view, "cardImage");
                            Intent intentSpeaker = new Intent(getActivity(), BaseSpeakerActivity.class);
                            intentSpeaker.putExtra("speaker",mAllSpeakers.get(position));
                            intentSpeaker.putExtra("eventId","event1");
                            getActivity().startActivity(intentSpeaker,options.toBundle());
                        }
                    }
                })
        );

        return rootView;
    }

    @Override
    public String getTitle() {
        //TODO change hardcoded string
        return "All Speakers";
    }

    @Override
    public void onPauseFragment() {
        Log.i("asd", "onPauseFragment()");
    }

    @Override
    public void onResumeFragment() {
        Log.i("asd", "onResumeFragment()");
    }

    private void wireUpFAB(View rootView) {
        mActionButton = (ActionButton) rootView.findViewById(R.id.action_button);
        mActionButton.setShowAnimation(ActionButton.Animations.ROLL_FROM_RIGHT);
        mActionButton.setHideAnimation(ActionButton.Animations.ROLL_TO_DOWN);
    }




}
