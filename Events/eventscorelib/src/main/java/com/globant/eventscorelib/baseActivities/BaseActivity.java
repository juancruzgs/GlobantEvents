package com.globant.eventscorelib.baseActivities;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.globant.eventscorelib.R;
import com.globant.eventscorelib.baseComponents.BaseApplication;
import com.globant.eventscorelib.baseFragments.BaseFragment;
import com.globant.eventscorelib.baseComponents.BaseService;
import com.globant.eventscorelib.utils.CoreConstants;

import java.util.ArrayList;

/**
 * Created by ignaciopena on 4/1/15.
 */
public abstract class BaseActivity extends AppCompatActivity{

    private BroadcastReceiver mReceiver;
    private TextView mConnectionRibbon;
    private TextView mFragmentTitle;
    protected ArrayList<BaseFragment> mFragments = new ArrayList<>();
    private BaseService mService = null;
    protected Class<? extends BaseService> mServiceClass;
    boolean mIsBound = false;
    boolean mIsOnline;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(CoreConstants.ACTIVITY_TITLE_INTENT, mFragmentTitle.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mFragmentTitle.setText(savedInstanceState.getString(CoreConstants.ACTIVITY_TITLE_INTENT));
    }

    public BaseService getService() {
        return mService;
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mService = ((BaseService.BaseBinder)service).getService();
            for (BaseFragment fragment : mFragments) {
                fragment.setService(mService);
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            mService = null;
            for (BaseFragment fragment : mFragments) {
                fragment.setService(null);
            }
        }
    };

    private void doStartService() {
        startService(new Intent(this, mServiceClass));
    }

    protected void doBindService() {
        bindService(new Intent(this, mServiceClass), mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    protected void doUnbindService() {
        if (mIsBound) {
            unbindService(mConnection);
            mIsBound = false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setConnectionReceiver();
        mServiceClass = getServiceClass();
    }

    private void setConnectionReceiver() {
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mIsOnline = isOnline(context);
                if (mIsOnline){
                    mConnectionRibbon.setVisibility(View.GONE);
                }else {
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

    public boolean isOnline() {
        return mIsOnline;
    }

    private Class<? extends BaseService> getServiceClass() {
        return ((BaseApplication)getApplication()).getServiceClass();
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

    private void setToolbar(ViewGroup mainContainer) {
        Toolbar toolbar = (Toolbar) mainContainer.findViewById(R.id.toolbar);
        mFragmentTitle = (TextView) toolbar.findViewById(R.id.toolbar_fragment_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (mService != null){
            ((BaseFragment)fragment).setService(mService);
        }
        mFragments.add((BaseFragment)fragment);
        if (mFragments.size() == 1) {
            //Sets the first fragment title
            setFragmentTitle((BaseFragment) fragment);
        }
    }

    private void setFragmentTitle(BaseFragment fragment){
        String title = fragment.getTitle();
        if (title != null && mFragmentTitle != null){
            mFragmentTitle.setText(title);
        }
    }

    @Override
    protected void onResume() {
        registerReceiver(mReceiver, new IntentFilter(CoreConstants.INTENT_FILTER_CONNECTIVITY));
        super.onResume();
        if (mServiceClass != null) {
            if (!BaseService.isRunning) {
                doStartService();
            }
            if (!mIsBound) {
                doBindService();
            }
        }
    }

    @Override
    protected void onPause() {
        unregisterReceiver(mReceiver);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mIsBound) {
            doUnbindService();
        }
    }

    protected void showErrorOverlay(){
        for (BaseFragment fragment : mFragments) {
            fragment.showErrorOverlay();
        }
    }

    protected void showErrorOverlay(String messageError){
        for (BaseFragment fragment : mFragments) {
            fragment.showErrorOverlay(messageError);
        }
    }

    protected void showProgressOverlay(){
        for (BaseFragment fragment : mFragments) {
            fragment.showProgressOverlay();
        }
    }

    protected void showProgressOverlay(String messageProgress){
        for (BaseFragment fragment : mFragments) {
            fragment.showProgressOverlay(messageProgress);
        }
    }

    protected void hideUtilsAndShowContentOverlay(){
        for (BaseFragment fragment : mFragments) {
            fragment.hideUtilsAndShowContentOverlay();
        }
    }

    public void changeFragmentTitle(String title) {
        if (title != null && mFragmentTitle != null){
             mFragmentTitle.setText(title);
        }
    }
}