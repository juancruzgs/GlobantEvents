package com.globant.eventmanager.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.globant.eventmanager.adapters.EventParticipantsListAdapterManager;
import com.globant.eventmanager.R;
import com.globant.eventmanager.adapters.ParticipantsListViewHolderManager;
import com.globant.eventscorelib.baseActivities.BasePagerActivity;
import com.globant.eventscorelib.baseFragments.BaseFragment;
import com.globant.eventscorelib.baseComponents.BaseService;
import com.globant.eventscorelib.domainObjects.Subscriber;

import java.util.List;

public class EventParticipantsManagerFragment extends BaseFragment implements BasePagerActivity.FragmentLifecycle, BaseService.ActionListener{

    private static final String TAG = "EventParticipantsFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int DATASET_COUNT = 9;
    private List<Subscriber> mSubscribers;
    protected LayoutManagerType mCurrentLayoutManagerType;
    protected RecyclerView mRecyclerView;
    protected EventParticipantsListAdapterManager mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected String[] mDataset;
    protected Boolean scrolling = false;
    private RelativeLayout mViewButtonsAddDeclineAll;
    private TextView mTextViewAcceptAll;
    private TextView mTextViewDeclineAll;
    private Boolean mAddAll = false;
    private Boolean mDeclineAll = false;

    public Boolean isDeclineAll() {
        return mDeclineAll;
    }

    public Boolean isAddAll() {
        return mAddAll;
    }

    @Override
    public Activity getBindingActivity() {
        return getActivity();
    }

    @Override
    public String getBindingKey() {
        return "EventParticipantsManagerFragment";
    }

    @Override
    public void onStartAction(BaseService.ACTIONS theAction) {
        showProgressOverlay();
    }

    @Override
    public void onFinishAction(BaseService.ACTIONS theAction, Object result) {
        switch ( theAction ) {
            case PARTICIPANT_LIST:
                mSubscribers = (List<Subscriber>) result;
                mAdapter = new EventParticipantsListAdapterManager(getActivity(), mSubscribers, this);
                mRecyclerView.setAdapter(mAdapter);
                hideUtilsAndShowContentOverlay();
        }
    }

    @Override
    public void onFailAction(BaseService.ACTIONS theAction, Exception e) {
        showErrorOverlay();
    }

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    public EventParticipantsManagerFragment() {
        // Required empty public constructor
    }

    public Boolean getScrolling() {
        return scrolling;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public BaseService.ActionListener getActionListener() {
        return this;
    }

    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_event_participants, container, false);
        rootView.setTag(TAG);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.event_participants_recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        if (savedInstanceState != null) {
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);
        setOnScrollListener();
        mViewButtonsAddDeclineAll = (RelativeLayout) rootView.findViewById(R.id.relative_layout_buttons_add_and_decline);
        mTextViewAcceptAll = (TextView) rootView.findViewById(R.id.text_view_accept_all);
        mTextViewDeclineAll = (TextView) rootView.findViewById(R.id.text_view_decline_all);
        setAddDeclineAllListener();
        return rootView;
    }

    private void setAddDeclineAllListener() {
        View.OnClickListener addDeclineAllListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                int initPosition = linearLayoutManager.findFirstVisibleItemPosition();
                int cont = 0;
                switch (v.getId()){
                    case R.id.text_view_accept_all:
                        for (int i = initPosition; i <= linearLayoutManager.findLastVisibleItemPosition(); i++){
                            linearLayoutManager.findViewByPosition(i);
                            ParticipantsListViewHolderManager current = (ParticipantsListViewHolderManager) mRecyclerView.findViewHolderForPosition(i);
                            if (current.getFrameLayoutLeft().getVisibility() == View.VISIBLE){
                                current.acceptAnimation();
                                cont += 1;
                            }
                        }
                        for (Subscriber sub : mSubscribers){
                            sub.setAccepted(true);
                        }
                        mAddAll = true;
                        break;
                    case R.id.text_view_decline_all:
                        for (int i = initPosition; i <= linearLayoutManager.findLastVisibleItemPosition(); i++){
                            linearLayoutManager.findViewByPosition(i);
                            ParticipantsListViewHolderManager current = (ParticipantsListViewHolderManager) mRecyclerView.findViewHolderForPosition(i);
                            if (current.getFrameLayoutLeft().getVisibility() == View.INVISIBLE){
                                current.declineAnimation();
                                cont += 1;
                            }
                        }
                        for (Subscriber sub : mSubscribers){
                            sub.setAccepted(false);
                        }
                        mDeclineAll = true;
                        break;
                }
                if (cont == 0){
                    notifyAdapter();
                }
                mAdapter.setSubscribers(mSubscribers);
            }
        };
        mTextViewAcceptAll.setOnClickListener(addDeclineAllListener);
        mTextViewDeclineAll.setOnClickListener(addDeclineAllListener);
    }

    private void setOnScrollListener() {
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if ((newState == RecyclerView.SCROLL_STATE_DRAGGING) || (newState == RecyclerView.SCROLL_STATE_SETTLING)) {
                    scrolling = true;
                } else {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        scrolling = false;
                    }
                }
            }

            private static final int HIDE_THRESHOLD = 20;
            private int scrolledDistance = 0;
            private boolean controlsVisible = true;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
                    //hide
                    mViewButtonsAddDeclineAll.animate().translationY(mViewButtonsAddDeclineAll.getHeight()).setInterpolator(new AccelerateInterpolator(2)).start();
                    controlsVisible = false;
                    scrolledDistance = 0;
                } else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
                    //show
                    mViewButtonsAddDeclineAll.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
                    controlsVisible = true;
                    scrolledDistance = 0;
                }

                if ((controlsVisible && dy > 0) || (!controlsVisible && dy < 0)) {
                    scrolledDistance += dy;
                }
            }
        });
    }

    @Override
    public String getTitle() {
        return "Participants";
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
        super.onSaveInstanceState(savedInstanceState);
    }

    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        mLayoutManager = new LinearLayoutManager(getActivity());
        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }

    private void initDataset() {

        mDataset = new String[DATASET_COUNT];
        for (int i = 0; i < DATASET_COUNT; i++) {
            mDataset[i] = "Hermione Granger #" + i;
        }
    }

    @Override
    public void onPauseFragment() {
        if (mAdapter.getCurrentParticipant() != null) {
            mAdapter.getCurrentParticipant().cancelAnimations();
        }
    }

    @Override
    public void onResumeFragment(){
        mService.executeAction(BaseService.ACTIONS.PARTICIPANT_LIST, "5vs7DC2RnQ", getBindingKey());
    }

    public void acceptSubscriber(int position){
        mSubscribers.get(position).setAccepted(true);
    }

    public void declineSubscriber(int position){
        mSubscribers.get(position).setAccepted(false);
    }

    public void notifyAdapter(){
        mAdapter.notifyDataSetChanged();
        mAddAll = false;
        mDeclineAll = false;
    }
}
