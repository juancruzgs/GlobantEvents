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

/**
 * Created by ignaciopena on 4/1/15.
 */
public abstract class BaseActivity extends ActionBarActivity {

    BroadcastReceiver mReceiver;
    TextView mConnectionRibbon;
    TextView mFragmentTitle;
    Toolbar mToolbar;
    ArrayList<BaseFragment> mFragments;

    BaseService mService = null;
    protected Class<? extends BaseService> mServiceClass;
    boolean mIsBound = false;
    BaseService.ActionListener mActionListener = null;
    ServiceReadyListener mReadyListener = null;

    public BaseService getService() {
        return mService;
    }

    public void setActionListener(BaseService.ActionListener listener) {
        mActionListener = listener;
    }

    public void setReadyListener(ServiceReadyListener listener) {
        mReadyListener = listener;
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with the service has been
            // established, giving us the service object we can use to
            // interact with the service.  Because we have bound to a explicit
            // service that we know is running in our own process, we can
            // cast its IBinder to a concrete class and directly access it.
            mService = ((BaseService.BaseBinder)service).getService();

            if (mActionListener != null) {
                mService.subscribeActor(mActionListener);
            }
            if (mReadyListener != null) {
                mReadyListener.onServiceReady(mService);
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            // Because it is running in our same process, we should never
            // see this happen.
            mService = null;
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

    // TODO: This function will be used to set the service (a subclass of BaseService)
    // No more "abstract" to not force use it in subclasses (there will be time for that)
    abstract protected void setServiceInternally();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setConnectionReceiver();
        mFragments = new ArrayList<>();

        setServiceInternally();

        if (mServiceClass == null) {
            // TODO: This will become an exception
            Logger.d("Service not defined");
        }
        else {
            doStartService();
        }
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

        if (!BaseService.isRunning) {
            doStartService();
        }

        doBindService();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(mReceiver);
        super.onPause();

        doUnbindService();
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        mFragments.add((BaseFragment)fragment);
        setFragmentTitle((BaseFragment)fragment);
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
        String title = fragment.getFragmentTitle();
        if (title != null && !title.isEmpty()){
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

    // Anstract methods
    public abstract String getActivityTitle();
//    public abstract String getFragmentTitle(BaseFragment fragment);
}
