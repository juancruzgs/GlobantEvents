package com.globant.eventscorelib.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.globant.eventscorelib.R;
import com.globant.eventscorelib.adapters.SpeakersListAdapter;
import com.globant.eventscorelib.baseComponents.BaseFragment;

/**
 * Created by agustin.gugliotta on 15/04/2015.
 */
public class SpeakersListFragment extends BaseFragment {

    //TODO delete this two attr
    private static final int DATASET_COUNT = 60;
    protected String[] mDataset;

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    protected LayoutManagerType mCurrentLayoutManagerType;
    protected RecyclerView mRecyclerView;
    protected SpeakersListAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;

    public SpeakersListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataset();
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

        mAdapter = new SpeakersListAdapter(mDataset);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setHasFixedSize(true);

        hideUtilsAndShowContentOverlay();

        return rootView;
    }

    @Override
    public String getFragmentTitle() {
        //TODO change hardcoded string
        return "Speaker List";
    }

    //TODO delete method
    private void initDataset() {
        mDataset = new String[DATASET_COUNT];
        for (int i = 0; i < DATASET_COUNT; i++) {
            mDataset[i] = "This is element #" + i;
        }
    }
}
