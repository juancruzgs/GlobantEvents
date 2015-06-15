package com.globant.eventscorelib.baseFragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.globant.eventscorelib.R;
import com.globant.eventscorelib.baseActivities.BaseCalendarListActivity;
import com.globant.eventscorelib.baseComponents.BaseService;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BaseCalendarListFragment extends BaseFragment {

    ListView mListViewCalendars;

    public BaseCalendarListFragment() {
        // Required empty public constructor
    }


    @Override
    public BaseService.ActionListener getActionListener() {
        return null;
    }

    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_base_calendar_list, container, false);

        showProgressOverlay();

        mListViewCalendars = (ListView)rootView.findViewById(R.id.list_view_calendars);
        mListViewCalendars.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra(BaseCalendarListActivity.KEY_CALENDAR_POS, position);
                getActivity().setResult(Activity.RESULT_OK, resultIntent);
                getActivity().finish();
            }
        });

        rootView.findViewById(R.id.button_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        List<String> calendarList = getActivity().getIntent().getStringArrayListExtra(
                BaseEventDescriptionFragment.KEY_CALENDAR_LIST);

        setCalendarList(calendarList);
    }

    private void setCalendarList(List<String> calendars) {
        mListViewCalendars.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.item_calendar,
                R.id.text_view_calendar, calendars));

        hideUtilsAndShowContentOverlay();
    }

    @Override
    public String getTitle() {
        return "Choose calendar";
    }


}
