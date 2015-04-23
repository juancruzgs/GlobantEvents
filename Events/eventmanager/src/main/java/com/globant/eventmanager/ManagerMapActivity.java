package com.globant.eventmanager;


import android.app.Activity;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Address;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.globant.eventscorelib.baseComponents.BaseMapActivity;
import com.globant.eventscorelib.baseComponents.BaseService;
import com.globant.eventscorelib.utils.CoreConstants;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;


public class ManagerMapActivity extends BaseMapActivity implements BaseService.ActionListener{

    private Marker mMarker;
    private long mBackPressedTime;
    private long mUpPressedTime;

    BaseService mService = null;

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mService = ((BaseService.BaseBinder)service).getService();
            mService.subscribeActor(ManagerMapActivity.this);
        }

        public void onServiceDisconnected(ComponentName className) {
            mService = null;
        }
    };

    private void doStartService() {
        startService(new Intent(this, ManagerDataService.class));
    }

    protected void doBindService() {
        bindService(new Intent(this, ManagerDataService.class), mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public Activity getBindingActivity() {
        return this;
    }

    @Override
    public Object getBindingKey() {
        return null;
    }

    @Override
    public void onStartAction(BaseService.ACTIONS theAction) {

    }

    @Override
    public void onFinishAction(BaseService.ACTIONS theAction, Object result) {
        if (theAction == BaseService.ACTIONS.POSITION_ADDRESS) {
            Address address = (Address)result;
            if (address != null) {
                setMapActivityResult(address);
            }
            else {
                setResult(ManagerMapActivity.RESULT_CANCELED);
            }
            finish();
        }
        else if (theAction == BaseService.ACTIONS.POSITION_COORDINATES) {
            LatLng latLng = (LatLng)result;
            if (latLng != null) {
                mMarker = addMarkerToMap(latLng);
            }
        }
    }

    @Override
    public void onFailAction(BaseService.ACTIONS theAction, Exception e) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doStartService();
        doBindService();
    }

    @Override
    protected int getMapLayout() {
        return R.layout.activity_manager_map;
    }

    @Override
    protected int getMapContainer() {
        return R.id.container;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        super.onMapReady(googleMap);
        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                mMarker = addMarkerToMap(latLng);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_manager_map, menu);
        prepareSearchView(menu);
        return true;
    }

    private void prepareSearchView(Menu menu) {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setQueryHint(getString(R.string.search_hint));
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                mService.executeAction(BaseService.ACTIONS.POSITION_COORDINATES, s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        finishActivityWithResult(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        boolean handled = false;

        if (id == android.R.id.home) {
            finishActivityWithResult(false);
            handled = true;
        }

        if (!handled) {
            handled =  super.onOptionsItemSelected(item);
        }

        return handled;
    }

    private void finishActivityWithResult(boolean backButton) {
        if (mMarker != null) {
            LatLng latLng = mMarker.getPosition();
            mService.executeAction(BaseService.ACTIONS.POSITION_ADDRESS, latLng);
        }
        else {
            finishActivityWithoutMarkerData(backButton);
        }
    }

    private void setMapActivityResult(Address address) {
        Intent intent = new Intent();
        intent.putExtra(CoreConstants.MAP_ADDRESS_INTENT, address);
        setResult(ManagerMapActivity.RESULT_OK, intent);
    }

    private void finishActivityWithoutMarkerData(boolean backButton) {
        if (isDoubleTapToExitDone(backButton)) {
            setResult(ManagerMapActivity.RESULT_CANCELED);
            finish();
        }
    }

    private boolean isDoubleTapToExitDone(boolean backButton) {
        long time;
        if (backButton){
            time = mBackPressedTime;
        }
        else {
            time = mUpPressedTime;
        }

        if (time + CoreConstants.DOUBLE_TAP_TIME_INTERVAL > System.currentTimeMillis())
        {
            return true;
        }
        else {
            Toast.makeText(getBaseContext(), getString(R.string.toast_double_tap_to_cancel), Toast.LENGTH_SHORT).show();
        }

        if (backButton){
            mBackPressedTime = System.currentTimeMillis();
        }
        else {
            mUpPressedTime = System.currentTimeMillis();
        }

        return false;
    }
}
