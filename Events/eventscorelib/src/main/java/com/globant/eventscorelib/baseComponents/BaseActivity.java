package com.globant.eventscorelib.baseComponents;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.globant.eventscorelib.R;

import java.util.ArrayList;

/**
 * Created by ignaciopena on 4/1/15.
 */
public abstract class BaseActivity extends ActionBarActivity {

    BroadcastReceiver mReceiver;
    TextView mConnectionRibbon;
    TextView mFragmentTitle;
    Toolbar mToolbar;
    ArrayList<BaseFragment> mFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setConnectionReceiver();
        mFragments = new ArrayList<>();
    }

    @Override
    public void setContentView(int layoutResID) {
        ViewGroup mainContainer = (ViewGroup) getLayoutInflater().inflate(R.layout.activity_base, null);
        FrameLayout frameLayout = (FrameLayout) mainContainer.findViewById(R.id.container);
        ViewGroup content = (ViewGroup) getLayoutInflater().inflate(layoutResID, frameLayout, false);
        frameLayout.addView(content);

        mConnectionRibbon = (TextView) mainContainer.findViewById(R.id.connection_ribbon);

        setToolbar(mainContainer);
        super.setContentView(mainContainer);
    }

    @Override
    protected void onResume() {
        registerReceiver(mReceiver,
                         new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(mReceiver);
        super.onPause();
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        mFragments.add((BaseFragment)fragment);
        setFragmentTitle((BaseFragment)fragment);
        //If there are more than 1 fragment title is overrided
    }

    private void setConnectionReceiver() {
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (isOnline(context)){
                    //TextView Gone
                    mConnectionRibbon.setVisibility(View.GONE);
                }else {
                    //TextView Enable
                    mConnectionRibbon.setVisibility(View.VISIBLE);
                }
            }

            private boolean isOnline(Context context) {
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = cm.getActiveNetworkInfo();
                return (netInfo != null && netInfo.isConnected());
            }
        };
    }

    private void setToolbar(ViewGroup mainContainer) {
        mToolbar =  (Toolbar) mainContainer.findViewById(R.id.toolbar);
        mFragmentTitle = (TextView) mToolbar.findViewById(R.id.toolbar_fragment_title);
        setSupportActionBar(mToolbar);
        setActivityTitle();
    }

    private final void setActivityTitle(){
        String title = getActivityTitle();
        if (title != null && !title.isEmpty()){
            mToolbar.setTitle(title);
        }
    }

    private final void setFragmentTitle(BaseFragment fragment){
        String title = getFragmentTitle(fragment);
        if (title != null && !title.isEmpty()){
            mFragmentTitle.setText(title);
        }
    }

    private void showErrorOverlay(){
        for (BaseFragment f : mFragments) {
            f.showErrorOverlay();
        }
    }
    private void showErrorOverlay(String messageError){
        for (BaseFragment f : mFragments) {
            f.showErrorOverlay(messageError);
        }
    }

    private void showProgressOverlay(){
        for (BaseFragment f : mFragments) {
            f.showProgressOverlay();
        }
    }
    private void showProgressOverlay(String messageProgress){
        for (BaseFragment f : mFragments) {
            f.showProgressOverlay(messageProgress);
        }
    }

    private void hideUtilsAndShowContentOverlay(){
        for (BaseFragment f : mFragments) {
            f.hideUtilsAndShowContentOverlay();
        }
    }

    // Anstract methods
    public abstract String getActivityTitle();
    public abstract String getFragmentTitle(BaseFragment fragment);
}
