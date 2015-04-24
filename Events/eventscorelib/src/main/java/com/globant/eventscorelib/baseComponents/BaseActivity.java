package com.globant.eventscorelib.baseComponents;

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
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.globant.eventscorelib.R;
import com.globant.eventscorelib.utils.Logger;

import java.util.ArrayList;

import static com.globant.eventscorelib.baseComponents.BaseFragment.TitleChangeable;

/**
 * Created by ignaciopena on 4/1/15.
 */
public abstract class BaseActivity extends ActionBarActivity implements TitleChangeable{

    BroadcastReceiver mReceiver;
    TextView mConnectionRibbon;
    TextView mFragmentTitle;
    Toolbar mToolbar;
    ArrayList<BaseFragment> mFragments = new ArrayList<>();

    BaseService mService = null;
    protected Class<? extends BaseService> mServiceClass;
    boolean mIsBound = false;

    public BaseService getService() {
        return mService;
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with the service has been
            // established, giving us the service object we can use to
            // interact with the service.  Because we have bound to a explicit
            // service that we know is running in our own process, we can
            // cast its IBinder to a concrete class and directly access it.
            mService = ((BaseService.BaseBinder)service).getService();

            for (BaseFragment fragment : mFragments) {
                fragment.setService(mService);
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            // Because it is running in our same process, we should never
            // see this happen.
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
            // Detach our existing connection.
            unbindService(mConnection);
            mIsBound = false;
        }
    }

    // TODO: This function is used to set the service (a subclass of BaseService)
    private Class<? extends BaseService> getServiceClass() {
        return ((BaseApplication)getApplication()).getServiceClass();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setConnectionReceiver();
        mServiceClass = getServiceClass();
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

/*
        if (mServiceClass != null) {
            doUnbindService();
        }
*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mIsBound) {
            doUnbindService();
        }
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
//        setActivityTitle();
    }

//    private void setActivityTitle(){
//        String title = getActivityTitle();
//        if (title != null && !title.isEmpty()){
//            mToolbar.setTitle(title);
//        }
//    }

    private void setFragmentTitle(BaseFragment fragment){
        String title = fragment.getTitle();
        if (title != null && !title.isEmpty() && mFragmentTitle != null){
            mFragmentTitle.setText(title);
        }
    }

    protected void showErrorOverlay(){
        for (BaseFragment f : mFragments) {
            f.showErrorOverlay();
        }
    }
    protected void showErrorOverlay(String messageError){
        for (BaseFragment f : mFragments) {
            f.showErrorOverlay(messageError);
        }
    }

    protected void showProgressOverlay(){
        for (BaseFragment f : mFragments) {
            f.showProgressOverlay();
        }
    }
    protected void showProgressOverlay(String messageProgress){
        for (BaseFragment f : mFragments) {
            f.showProgressOverlay(messageProgress);
        }
    }

    protected void hideUtilsAndShowContentOverlay(){
        for (BaseFragment f : mFragments) {
            f.hideUtilsAndShowContentOverlay();
        }
    }

    @Override
    public void changeFragmentTitle(String title) {
        if (title != null && mFragmentTitle != null){
             mFragmentTitle.setText(title);
        }
    }

//    public abstract String getActivityTitle();
}