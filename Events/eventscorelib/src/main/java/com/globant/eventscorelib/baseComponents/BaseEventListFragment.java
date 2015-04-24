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
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.globant.eventscorelib.R;
import com.globant.eventscorelib.fragments.SubscriberFragment;
import com.globant.eventscorelib.utils.CoreConstants;
import com.nineoldandroids.view.ViewHelper;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public abstract class BaseEventListFragment extends BaseFragment implements ObservableScrollViewCallbacks {

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
    private ObservableRecyclerView mRecyclerView;
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

        mRecyclerView = (ObservableRecyclerView) rootView.findViewById(getEventListRecyclerView());
        mRecyclerView.setScrollViewCallbacks(this);

        ScrollUtils.addOnGlobalLayoutListener(mRecyclerView, new Runnable() {
            @Override
            public void run() {
                ((LinearLayoutManager)mRecyclerView.getLayoutManager()).scrollToPositionWithOffset(0,1);
                ((LinearLayoutManager)mRecyclerView.getLayoutManager()).scrollToPositionWithOffset(0,0);

            }
        });

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
        boolean handled = false;

        if (id == R.id.action_credits) {// Do Fragment menu item stuff here
            Intent intentCredits = new Intent(getActivity(), CreditsActivity.class);
            startActivity(intentCredits);
            handled = true;
        } else {
            if (id == R.id.action_checkin) {
                Intent intentScan = new Intent(CoreConstants.INTENT_SCAN);
                startActivityForResult(intentScan, 0);
                handled = true;
            } else {
                if (id == R.id.action_profile) {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, new SubscriberFragment())
                            .addToBackStack(null).commit();
                    handled = true;
                }
            }
        }

        if (!handled) {
            handled = super.onOptionsItemSelected(item);
        }
        return handled;
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
    @Override
    public void onScrollChanged(int i, boolean b, boolean b2) {

        float height = mRecyclerView.getChildAt(0).getHeight();
        float childHeight = mRecyclerView.getChildAt(0).findViewById(R.id.event_title_text_view).getHeight();

        float z = (childHeight+30) / height;
        float movementY , movementX;

        for (int n = 0; n < mRecyclerView.getChildCount(); n++){

            View cardView = mRecyclerView.getChildAt(n);
            View titleView = mRecyclerView.getChildAt(n).findViewById(R.id.event_title_text_view);
            View dateView = mRecyclerView.getChildAt(n).findViewById(R.id.event_date_text_view);
            View locationView = mRecyclerView.getChildAt(n).findViewById(R.id.event_location_text_view);

            // Set translation movement
            float cardY = cardView.getY();
            movementY = ScrollUtils.getFloat( (cardY - 230) * (-z*2) , -(childHeight * 2) , 30);
            movementX = ScrollUtils.getFloat( (cardY - 200) * (-z*2) , -(childHeight * 2) , 0);

            // Translate Title
            ViewHelper.setTranslationY(titleView, movementY);
            ViewHelper.setTranslationX(titleView, (-movementX)/2.5f );

            //Translate Date
            ViewHelper.setTranslationY(dateView, movementY/2);
            ViewHelper.setTranslationX(dateView, -(movementX*1.5f));

            // Alpha of Date
            float alpha = ScrollUtils.getFloat( cardY * z, 0, 255);
            ViewHelper.setAlpha(mRecyclerView.getChildAt(n).findViewById(R.id.event_date_text_view), 1 - (alpha / 128));

            // Translate Location
            ViewHelper.setTranslationX(locationView, -(movementX*3));

            //Alpha of Location
            ViewHelper.setAlpha(locationView, 1 - (alpha / 128));

            //((TextView)mRecyclerView.getChildAt(n).findViewById(R.id.event_title_text_view)).setText(String.format("%.02f", movementY) + " | " + String.format("%.02f", cardY));

        }

    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }
}
