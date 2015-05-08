package com.globant.eventmanager.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.globant.eventmanager.R;
import com.globant.eventscorelib.baseComponents.BaseService;
import com.globant.eventscorelib.baseFragments.BaseFragment;
import com.globant.eventscorelib.utils.PushNotifications;

public class PushNotificationFragment extends BaseFragment {
    public static String SOURCE_TAG = "SOURCE";

    Spinner mSpinner;
    EditText mPushText;
    Button mSendButton, mCancelButton;

    public PushNotificationFragment() {
    }


    @Override
    public BaseService.ActionListener getActionListener() {
        return null;
    }

    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.fragment_push_notification, container, false);
        wireUpViews(rootView);
        hideUtilsAndShowContentOverlay();
        if (isBroadcast()){
            mSpinner.setVisibility(View.GONE);
        }
        return rootView;
    }

    private boolean isBroadcast() {
        return getActivity().getIntent().getStringExtra(SOURCE_TAG).equals(EventListManagerFragment.class.getSimpleName());
    }

    private void wireUpViews(View rootView) {
        mSpinner = (Spinner)rootView.findViewById(R.id.spinner_users_filter);
        mPushText = (EditText)rootView.findViewById(R.id.editText_notification_text);
        mSendButton = (Button)rootView.findViewById(R.id.button_send_notification);
        mCancelButton = (Button)rootView.findViewById(R.id.button_cancel_notification);
        setButtonsAction();
    }

    private void setButtonsAction() {
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPushText.getText() != null){
                    if (isBroadcast()){
                        PushNotifications
                                .sendNotification(mPushText.getText().toString());
                    } else {
                        PushNotifications
                                .sendNotification(mPushText.getText().toString(),
                                        String.valueOf(mSpinner.getSelectedItem()),
                                        getActivity().getIntent().getStringExtra(SOURCE_TAG));
                    }
                }
            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    @Override
    public String getTitle() {
        return "Notifications";
    }
}
