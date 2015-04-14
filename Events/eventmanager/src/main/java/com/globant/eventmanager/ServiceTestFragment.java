package com.globant.eventmanager;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.globant.eventscorelib.baseComponents.BaseActivity;
import com.globant.eventscorelib.baseComponents.BaseFragment;
import com.globant.eventscorelib.baseComponents.BaseService;
import com.globant.eventscorelib.utils.Logger;

/**
 * A placeholder fragment containing a simple view.
 */
public class ServiceTestFragment extends BaseFragment {

    BaseService.ActionListener mActionListener;

    public ServiceTestFragment() {
    }

    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_service_test, container, false);
        hideUtilsAndShowContentOverlay();

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mActionListener = new BaseService.ActionListener() {
            @Override
            protected Activity getBindingActivity() {
                return getActivity();
            }

            @Override
            protected Object getBindingKey() {
                return getActivity().hashCode();
            }

            @Override
            protected void onStartAction(BaseService.ACTIONS theAction) {
                Logger.d("Action " + theAction.toString() + " started");
            }

            @Override
            protected void onFinishAction(BaseService.ACTIONS theAction, Object result) {
                Logger.d("Action " + theAction.toString() + " finished with result " + result.toString());
            }

            @Override
            protected void onFailAction(BaseService.ACTIONS theAction, Exception e) {
                Logger.d("Action " + theAction.toString() + " failed, throwing " + e.toString());
            }
        };

        final BaseService service = ((BaseActivity)getActivity()).getService();
        service.subscribeActor(mActionListener);

        getActivity().findViewById(R.id.button_execute_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                service.executeAction(BaseService.ACTIONS.EVENT_LIST, null);
            }
        });

    }

    @Override
    public String getFragmentTitle() {
        return "Service";
    }

}
