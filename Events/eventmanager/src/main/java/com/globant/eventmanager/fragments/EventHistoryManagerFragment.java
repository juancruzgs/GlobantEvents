package com.globant.eventmanager.fragments;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.globant.eventmanager.R;
import com.globant.eventmanager.activities.EventDetailManagerActivity;
import com.globant.eventmanager.activities.EventHistoryManagerActivity;
import com.globant.eventmanager.adapters.EventHistoryListAdapterManager;
import com.globant.eventscorelib.baseActivities.BaseEventDetailPagerActivity;
import com.globant.eventscorelib.baseAdapters.GetEventInformation;
import com.globant.eventscorelib.baseComponents.BaseService;
import com.globant.eventscorelib.baseFragments.BaseFragment;
import com.globant.eventscorelib.domainObjects.Event;
import com.globant.eventscorelib.utils.CoreConstants;

import java.util.Collection;
import java.util.Date;
import java.util.List;


public class EventHistoryManagerFragment extends BaseFragment implements BaseService.ActionListener, GetEventInformation {

    private SearchView mSearchView;
    private String mBindingKey;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Event> mEventList;
    private RecyclerView mRecyclerView;
    private AppCompatTextView mTextViewNoEvents;
    private EventHistoryListAdapterManager mAdapter;

    public EventHistoryManagerFragment() {
    }

    @Override
    public BaseService.ActionListener getActionListener() {
        return this;
    }

    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_manager_event_history, container, false);
        hideUtilsAndShowContentOverlay();
        prepareRecyclerView(rootView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mTextViewNoEvents = (AppCompatTextView) rootView.findViewById(R.id.text_view_no_events);
        setRecyclerViewLayoutManager();
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBindingKey = this.getClass().getSimpleName() + new Date().toString();
    }

    @Override
    public String getTitle() {
        return "";
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_manager_event_history, menu);
        prepareSearchView(menu);
    }

    public void setRecyclerViewLayoutManager() {
        int scrollPosition = 0;
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }


    private void setRecyclerViewAdapter() {
        mAdapter = new EventHistoryListAdapterManager(mEventList, getActivity(), this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void prepareSearchView(Menu menu) {
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        mSearchView = (SearchView) menu.findItem(R.id.search).getActionView();
        mSearchView.setQueryHint(getString(R.string.filter_hint));
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (TextUtils.isEmpty(s)) {
                    mAdapter.getFilter().filter("");
                } else {
                    mAdapter.getFilter().filter(s);
                }
                return true;
            }
        });
    }

    private void prepareRecyclerView(View rootView) {
        mRecyclerView = (RecyclerView) rootView.findViewById(com.globant.eventscorelib.R.id.list_recycler_view);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
    }

    @Override
    public void setService(BaseService service) {
        super.setService(service);
        mEventList = ((EventHistoryManagerActivity) getActivity()).getEventHistory();
        if (mEventList == null) {
            mService.executeAction(BaseService.ACTIONS.GET_EVENT_HISTORY, getBindingKey(), null);
        } else {
            setRecyclerViewAdapter();
        }
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
        showProgressOverlay();
    }

    @Override
    public void onFinishAction(BaseService.ACTIONS theAction, Object result) {
        switch (theAction) {
            case GET_EVENT_HISTORY:
                if (result != null) {
                    mEventList = (List<Event>) result;
                    ((EventHistoryManagerActivity) getActivity()).setEventHistory(mEventList);
                    setRecyclerViewAdapter();
                    setHasOptionsMenu(true);
                } else {
                    showErrorOverlay();
                }

                hideUtilsAndShowContentOverlay();
                break;
            case GET_EVENT:
                Intent intent = new Intent(getActivity(), EventDetailManagerActivity.class);
                intent.putExtra(CoreConstants.FIELD_EVENTS, (Event) result);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onFailAction(BaseService.ACTIONS theAction, Exception e) {
        showErrorOverlay();
    }

    @Override
    public Event getEvent(int position) {
        return null;
    }

    @Override
    public void getEvent(String eventId) {
        mService.executeAction(BaseService.ACTIONS.GET_EVENT, getBindingKey(), eventId);
    }
}
