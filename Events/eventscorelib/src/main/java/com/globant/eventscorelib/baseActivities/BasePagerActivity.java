package com.globant.eventscorelib.baseActivities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;

import com.ToxicBakery.viewpager.transforms.ZoomOutSlideTransformer;
import com.globant.eventscorelib.R;
import com.globant.eventscorelib.utils.CoreConstants;

import java.util.List;

abstract public class BasePagerActivity extends BaseActivity {

    PageAdapter pageAdapter;
    int mCurrentFragmentPosition = 0;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CoreConstants.CURRENT_FRAGMENT_INTENT, mCurrentFragmentPosition);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mCurrentFragmentPosition = savedInstanceState.getInt(CoreConstants.CURRENT_FRAGMENT_INTENT);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_pager);
        List<Fragment> fragments = getFragments();
        prepareAdapter(fragments);
        initialResumeFragment();
        prepareTitleStrip();
    }

    private void prepareTitleStrip() {
        PagerTitleStrip titleStrip = (PagerTitleStrip) findViewById(R.id.pager_title_strip);
        titleStrip.setTextColor(getResources().getColor(R.color.white));
    }

    private void initialResumeFragment() {
        //onResume first fragment
        if (mCurrentFragmentPosition == 0) {
            FragmentLifecycle fragmentToShow = (FragmentLifecycle) pageAdapter.getItem(0);
            fragmentToShow.onResumeFragment();
        }
    }

    private void prepareAdapter(List<Fragment> fragments) {
        pageAdapter = new PageAdapter(getSupportFragmentManager(), fragments);
        ViewPager pager = (ViewPager)findViewById(R.id.viewpager);
        pager.setAdapter(pageAdapter);
        pager.setPageTransformer(true, new ZoomOutSlideTransformer());
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int newPosition) {
                FragmentLifecycle fragmentToHide = (FragmentLifecycle)pageAdapter.getItem(mCurrentFragmentPosition);
                fragmentToHide.onPauseFragment();

                FragmentLifecycle fragmentToShow = (FragmentLifecycle)pageAdapter.getItem(newPosition);
                fragmentToShow.onResumeFragment();

                mCurrentFragmentPosition = newPosition;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    protected abstract List<Fragment> getFragments();
    protected abstract List<String> getTitlesList();

    public class PageAdapter extends FragmentPagerAdapter {

        private List<Fragment> mFragments;

        public PageAdapter(FragmentManager mFragmentManager, List<Fragment> fragments) {
            super(mFragmentManager);
            this.mFragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return this.mFragments.get(position);
        }

        @Override
        public int getCount() {
            return this.mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getTitlesList().get(position).toUpperCase();
        }
    }

    public interface FragmentLifecycle {

        public void onPauseFragment();
        public void onResumeFragment();

    }
}
