package com.globant.eventscorelib.baseFragments;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.software.shell.fab.ActionButton;

import java.util.Date;
import java.util.List;

/**
        * Created by agustin.gugliotta on 15/04/2015.
        */
public class BaseSpeakersListFragment extends BaseFragment implements BaseService.ActionListener, BasePagerActivity.FragmentLifecycle{

    protected List<Speaker> mSpeakers;
    protected RecyclerView mRecyclerView;
    protected BaseSpeakersListAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected AppCompatTextView mTextViewNoSpeakers;
    protected ActionButton mActionButton;

    private String LOG_TAG = getClass().getSimpleName();

    private String mBindingKey;

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
        return mBindingKey;
    }

    @Override
    public void onStartAction(BaseService.ACTIONS theAction) {
        Log.d(LOG_TAG,Thread.currentThread().getStackTrace()[2].getMethodName());
        showProgressOverlay();
    }

    @Override
    public void onFinishAction(BaseService.ACTIONS theAction, Object result) {
        Log.d(LOG_TAG,Thread.currentThread().getStackTrace()[2].getMethodName());
        if (theAction == BaseService.ACTIONS.EVENT_SPEAKERS) {
            mSpeakers = (List<Speaker>) result;
            if ((mSpeakers.size()) >= 1) {
                ((BaseEventDetailPagerActivity) getActivity()).setSpeakersList(mSpeakers);
                setRecyclerViewAdapter();
            } else {
                mRecyclerView.setVisibility(View.GONE);
                mTextViewNoSpeakers.setVisibility(View.VISIBLE);
            }
            hideUtilsAndShowContentOverlay();
        }
    }


    private void setRecyclerViewAdapter() {
        Log.d(LOG_TAG,Thread.currentThread().getStackTrace()[2].getMethodName());
        mAdapter = new BaseSpeakersListAdapter(getActivity(), mSpeakers);
        mRecyclerView.setAdapter(mAdapter);
        if (mSpeakers.size() <1){
            mRecyclerView.setVisibility(View.GONE);
            mTextViewNoSpeakers.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onFailAction(BaseService.ACTIONS theAction, Exception e) {
        showErrorOverlay();
    }

    public BaseSpeakersListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG,Thread.currentThread().getStackTrace()[2].getMethodName());
        super.onCreate(savedInstanceState);
        mBindingKey = this.getClass().getSimpleName() + new Date().toString();
    }

    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(LOG_TAG,Thread.currentThread().getStackTrace()[2].getMethodName());
        View rootView = inflater.inflate(R.layout.fragment_speaker_list, container, false);
        hideUtilsAndShowContentOverlay();
        mTextViewNoSpeakers=(AppCompatTextView)rootView.findViewById(R.id.text_view_no_speakers);
        prepareRecyclerView(rootView);
        setRetainInstance(true);
        wireUpFAB(rootView);

        return rootView;
    }

    protected void prepareRecyclerView(View rootView) {
        Log.d(LOG_TAG,Thread.currentThread().getStackTrace()[2].getMethodName());
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
                    @Override
                    public void onItemClick(View view, int position) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            ImageView cardImage = (ImageView) view.findViewById(R.id.image_view_profile_speaker);
                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), view, "cardImage");
                            Intent intentSpeakerDetail = new Intent(getActivity(), BaseSpeakerDetailActivity.class);
                            intentSpeakerDetail.putExtra(CoreConstants.SPEAKER_SELECTED, mSpeakers.get(position));
                            getActivity().startActivity(intentSpeakerDetail, options.toBundle());
                        } else {
                            Intent intentSpeakerDetail = new Intent(getActivity(), BaseSpeakerDetailActivity.class);
                            intentSpeakerDetail.putExtra(CoreConstants.SPEAKER_SELECTED, mSpeakers.get(position));
                            startActivity(intentSpeakerDetail);
                        }


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
        Log.d(LOG_TAG,Thread.currentThread().getStackTrace()[2].getMethodName());
        Activity activity = getActivity();
        mSpeakers = ((BaseEventDetailPagerActivity) getActivity()).getEvent().getSpeakers();
        if (mSpeakers == null) {
                Event event = ((BaseEventDetailPagerActivity) getActivity()).getEvent();
                String eventId = event.getObjectID();
                mService.executeAction(BaseService.ACTIONS.EVENT_SPEAKERS, getBindingKey(), eventId);
        } else {
            setRecyclerViewAdapter();
        }
    }
    private void wireUpFAB(View rootView) {
        Log.d(LOG_TAG,Thread.currentThread().getStackTrace()[2].getMethodName());
        mActionButton = (ActionButton) rootView.findViewById(com.globant.eventscorelib.R.id.action_button);
        mActionButton.setShowAnimation(ActionButton.Animations.ROLL_FROM_RIGHT);
        mActionButton.setHideAnimation(ActionButton.Animations.ROLL_TO_DOWN);
        mActionButton.hide();
    }
}
