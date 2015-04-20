package com.globant.eventscorelib.baseComponents;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.globant.eventscorelib.R;
import com.globant.eventscorelib.fragments.SubscriberFragment;
import com.globant.eventscorelib.utils.CoreConstants;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public abstract class BaseEventListFragment extends BaseFragment {

    private static final String TAG = "EventListFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;
//    private static final int DATASET_COUNT = 60;
    protected enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }
    private LayoutManagerType mCurrentLayoutManagerType;
    protected abstract int getFragmentLayout();
    protected abstract int getEventListRecyclerView();
    private RecyclerView mRecyclerView;
//    private EventsListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
//    private String[] mDataset;

    public BaseEventListFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        initDataset();
    }

    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(getFragmentLayout(), container, false);
        rootView.setTag(TAG);

        mRecyclerView = (RecyclerView) rootView.findViewById(getEventListRecyclerView());
        mLayoutManager = new LinearLayoutManager(getActivity());
        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);
        hideUtilsAndShowContentOverlay();
        setHasOptionsMenu(true);

        return rootView;
    }

    @Override
    public String getTitle() {
        return getString(R.string.title_fragment_events_stream);
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

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
        super.onSaveInstanceState(savedInstanceState);
    }

//    protected void initDataset() {
//        mDataset = new String[DATASET_COUNT];
//        for (int i = 0; i < DATASET_COUNT; i++) {
//            mDataset[i] = "La Fiesta del Chorizo";
//        }
//    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_event_list_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_credits) {// Do Fragment menu item stuff here
            Intent intentCredits = new Intent(getActivity(), CreditsActivity.class);
            startActivity(intentCredits);
            return true;
        } else {
            if (id == R.id.action_checkin){
                Intent intentScan = new Intent(CoreConstants.INTENT_SCAN);
                startActivityForResult(intentScan,0);
                return true;
            } else {
                if (id == R.id.action_profile){
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, new SubscriberFragment())
                            .commit();
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == -1) {
                String contents = data.getStringExtra(CoreConstants.SCAN_RESULT);
                showCheckinOverlay();
            }

        }
    }
}
