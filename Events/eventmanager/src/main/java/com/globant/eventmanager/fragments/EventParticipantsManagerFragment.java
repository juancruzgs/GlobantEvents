package com.globant.eventmanager.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.globant.eventmanager.R;
import com.globant.eventmanager.adapters.EventParticipantsListAdapterManager;
import com.globant.eventmanager.adapters.ParticipantsListViewHolderManager;
import com.globant.eventscorelib.baseActivities.BaseEventDetailPagerActivity;
import com.globant.eventscorelib.baseActivities.BasePagerActivity;
import com.globant.eventscorelib.baseAdapters.BaseParticipantsListAdapter;
import com.globant.eventscorelib.baseComponents.BaseService;
import com.globant.eventscorelib.baseFragments.BaseFragment;
import com.globant.eventscorelib.baseFragments.BaseParticipantsFragment;
import com.globant.eventscorelib.controllers.SharedPreferencesController;
import com.globant.eventscorelib.domainObjects.Event;
import com.globant.eventscorelib.domainObjects.Subscriber;
import com.globant.eventscorelib.utils.CoreConstants;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventParticipantsManagerFragment extends BaseParticipantsFragment implements BasePagerActivity.FragmentLifecycle, BaseService.ActionListener, BasePagerActivity.OnPageScrollStateChangedCancelAnimation{

    private static final String TAG = "EventParticipantsFragment";
    private List<Subscriber> mSubscribers;
    private List<Subscriber> mAcceptedSubscribers;
    protected LayoutManagerType mCurrentLayoutManagerType;
    protected ObservableRecyclerView mRecyclerView;
    protected EventParticipantsListAdapterManager mAdapter;
    protected Boolean scrolling = false;
    private LinearLayout mViewButtonsAddDeclineAll;
    private AppCompatTextView mTextViewAcceptAll;
    private AppCompatTextView mTextViewDeclineAll;
    private Boolean mLastVisibleItem = false;
    private String mBindingKey;
    private SwipeRefreshLayout mSwipeRefreshLayout;


    public Boolean isLastVisibleItem() {
        return mLastVisibleItem;
    }

    @Override
    public Activity getBindingActivity() {
        return getActivity();
    }

    @Override
    public String getBindingKey() {
        return EventParticipantsManagerFragment.class.getSimpleName();
    }

    @Override
    protected void initializeAcceptedSubscribers() {
        mAcceptedSubscribers = new ArrayList<>();
    }

    @Override
    protected BaseParticipantsListAdapter getAdapter() {
        mSubscribers = getSubscribers();
        mAdapter = new EventParticipantsListAdapterManager(getActivity(), mSubscribers, this);
        return mAdapter;
    }



    @Override
    protected void showHint() {
        if (!SharedPreferencesController.isHintParticipantsShowed(this.getActivity())){
            Toast.makeText(this.getActivity(),R.string.toast_hint_participants_list, Toast.LENGTH_SHORT).show();
            SharedPreferencesController.setHintParticipantsShowed(true, this.getActivity());
        }
    }

    @Override
    public void onFailAction(BaseService.ACTIONS theAction, Exception e) {
        showErrorOverlay();
    }

    @Override
    public void cancelAnimations() {
        if (mAdapter != null && mAdapter.getCurrentParticipant() != null) {
            mAdapter.getCurrentParticipant().cancelAnimations();
        }
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
    public BaseService.ActionListener getActionListener() {
        return this;
    }

    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateEventView(inflater, container, savedInstanceState);
        wireUpAddDeclineButtons(rootView);
        setAddDeclineAllListener();
        setOnScrollListener();
        mSwipeRefreshLayout = getSwipeRefreshLayout();
        return rootView;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_event_participants;
    }

    @Override
    protected void cancelAnimationOnRefresh() {
        if ((mAdapter != null) && (mAdapter.getCurrentParticipant() != null)) {
            mAdapter.getCurrentParticipant().cancelAnimations();
        }
    }

    private void wireUpAddDeclineButtons(View rootView) {
        mViewButtonsAddDeclineAll = (LinearLayout) rootView.findViewById(R.id.linear_layout_buttons_add_and_decline);
        mTextViewAcceptAll = (AppCompatTextView) rootView.findViewById(R.id.text_view_accept_all);
        mTextViewDeclineAll = (AppCompatTextView) rootView.findViewById(R.id.text_view_decline_all);
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
                            if (current.getFrameLayoutRight().getVisibility() != View.VISIBLE){
                                current.acceptAnimation();
                                cont += 1;
                            }
                            if (i == linearLayoutManager.findLastVisibleItemPosition()){
                                mLastVisibleItem = true;
                            }
                        }
                        for (Subscriber sub : mSubscribers){
                            if (!sub.isAccepted()){
                                sub.setAccepted(true);
                                if (!mAcceptedSubscribers.contains(sub)){
                                    mAcceptedSubscribers.add(sub);
                                }
                            }
                        }
                        break;
                    case R.id.text_view_decline_all:
                        for (int i = initPosition; i <= linearLayoutManager.findLastVisibleItemPosition(); i++){
                            linearLayoutManager.findViewByPosition(i);
                            ParticipantsListViewHolderManager current = (ParticipantsListViewHolderManager) mRecyclerView.findViewHolderForPosition(i);
                            if (current.getFrameLayoutLeft().getVisibility() != View.VISIBLE){
                                current.declineAnimation();
                                cont += 1;
                            }
                            if (i == linearLayoutManager.findLastVisibleItemPosition()){
                                mLastVisibleItem = true;
                            }
                        }
                        for (Subscriber sub : mSubscribers){
                            if (mAcceptedSubscribers.contains(sub)){
                                sub.setAccepted(false);
                                mAcceptedSubscribers.remove(sub);
                            }
                        }
                        break;
                }
                if (cont == 0){
                    //notifyAdapter();
                }
                mAdapter.setSubscribers(mSubscribers);
            }
        };
        mTextViewAcceptAll.setOnClickListener(addDeclineAllListener);
        mTextViewDeclineAll.setOnClickListener(addDeclineAllListener);
    }



    private void setOnScrollListener() {
        mRecyclerView = getRecyclerView();
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
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable(CoreConstants.KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onPauseFragment() {
    }

    @Override
    public void onStop() {
        if ((mSubscribers != null) && (mAcceptedSubscribers.size() > 0))
        {
            mService.executeAction(BaseService.ACTIONS.SET_ACCEPTED, getBindingKey(), mEvent.getObjectID(), mAcceptedSubscribers);
        }
        super.onStop();
    }

    public void acceptSubscriber(int position){
        Subscriber subscriber = mSubscribers.get(position);
        subscriber.setAccepted(true);
        if (!mAcceptedSubscribers.contains(subscriber)){
            mAcceptedSubscribers.add(subscriber);
        }
    }

    public void declineSubscriber(int position){
        Subscriber subscriber = mSubscribers.get(position);
        subscriber.setAccepted(false);
        if (mAcceptedSubscribers.contains(subscriber)){
            mAcceptedSubscribers.remove(subscriber);
        }
    }

    public void notifyAdapter(){
        mAdapter.notifyDataSetChanged();
        mLastVisibleItem = false;
    }
}
