package com.globant.eventmanager.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.globant.eventmanager.R;
import com.globant.eventmanager.activities.EventHistoryManagerActivity;
import com.globant.eventmanager.activities.EventsManagerPagerActivity;
import com.globant.eventmanager.adapters.EventListAdapterManager;
import com.globant.eventscorelib.baseAdapters.BaseEventsListAdapter;
import com.globant.eventscorelib.baseComponents.BaseService;
import com.globant.eventscorelib.baseFragments.BaseEventListFragment;
import com.software.shell.fab.ActionButton;

public class EventListManagerFragment extends BaseEventListFragment {

    private ActionButton mActionButton;

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_manager_event_list;
    }

    @Override
    protected boolean getIsGlober() {
        return true;
    }

    @Override
    protected BaseEventsListAdapter getAdapter() {
        return new EventListAdapterManager(getEventList(), getActivity(), this);
    }

    public EventListManagerFragment() {
    }

    @Override
    public BaseService.ActionListener getActionListener() {
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateEventView(inflater, container, savedInstanceState);
        prepareRecyclerView();
        wireUpFAB(rootView);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_manager_event_list, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        boolean handled = false;

        if (id == R.id.action_history) {
            Intent intentHistory = new Intent(getActivity(), EventHistoryManagerActivity.class);
            startActivity(intentHistory);
            handled = true;
        } else {
            if (id == R.id.action_notifications){
                MaterialDialog materialDialog = new MaterialDialog.Builder(getActivity())
                        .title("Push Notifications")
                        .customView(R.layout.dialog_push_notification, false)
                        .positiveText("Send")
                        .negativeText("Cancel")
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                super.onPositive(dialog);
                                Toast.makeText(dialog.getContext(), "Message send", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onNegative(MaterialDialog dialog) {
                                super.onNegative(dialog);
                            }
                        }).build();
                materialDialog.findViewById(R.id.title_destination).setVisibility(View.GONE);
                materialDialog.findViewById(R.id.spinner_users_filter).setVisibility(View.GONE);
                materialDialog.show();
            }
        }
        if (!handled) {
            handled = super.onOptionsItemSelected(item);
        }
        return handled;
    }

    private void prepareRecyclerView() {
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if ((newState == RecyclerView.SCROLL_STATE_DRAGGING) || (newState == RecyclerView.SCROLL_STATE_SETTLING)) {
                    mActionButton.hide();
                } else {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        mActionButton.show();
                    }
                }
            }
        });
    }

    private void wireUpFAB(View rootView) {
        mActionButton = (ActionButton) rootView.findViewById(R.id.action_button);
        mActionButton.setShowAnimation(ActionButton.Animations.ROLL_FROM_RIGHT);
        mActionButton.setHideAnimation(ActionButton.Animations.ROLL_TO_DOWN);
        mActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EventsManagerPagerActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void getEvent(String eventId) {
    }
}