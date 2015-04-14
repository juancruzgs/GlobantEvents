package com.globant.eventmanager;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

import com.globant.eventscorelib.TweetFragment;
import com.globant.eventscorelib.baseComponents.BaseActivity;
import com.globant.eventscorelib.baseComponents.BaseApplication;
import com.globant.eventscorelib.baseComponents.BaseFragment;


public class TestActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        BaseFragment fragment = new TweetFragment();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
    }
    @Override
    public String getActivityTitle() {
        return "Test Activty";
    }

    public String getFragmentTitle(BaseFragment fragment) {
        return fragment.getTitle();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Uri uri = getIntent().getData();
        new TwitterLoaderResponse().execute(uri);
    }


    private class TwitterLoaderResponse extends AsyncTask<Uri, Integer, Boolean>{

        @Override
        protected Boolean doInBackground(Uri... params) {
            boolean response = BaseApplication.getInstance().getTwitterManager().getLoginResponse(params[0]);
            if (response){
//                String eventname = BaseApplication.getInstance().getCacheObjectsManager().event.title;
//                String aditionalMsg = getString(R.string.initialtweetcomplement);
//                String tweet = "#FlipThinking "+ TwitterManager.HashtagString(eventname)+" "+aditionalMsg;
                String tweet = "ksjfksnfknfksfns";
                BaseApplication.getInstance().getTwitterManager().publishPost(tweet);
                return true;
            }else{
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(result){
                // mViewPager.setCurrentItem(mViewPager.getChildCount()-1);
            }
        }

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends BaseFragment {

        public PlaceholderFragment() {
        }

        @Override
        protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_test, container, false);
            return rootView;
        }

        @Override
        public String getTitle() {
            return "FragmentTest";
        }
    }
}
