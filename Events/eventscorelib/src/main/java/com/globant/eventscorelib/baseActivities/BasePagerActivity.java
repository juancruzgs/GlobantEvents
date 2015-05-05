package com.globant.eventscorelib.baseActivities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import com.ToxicBakery.viewpager.transforms.ZoomOutSlideTransformer;
import com.globant.eventscorelib.R;
import com.globant.eventscorelib.baseComponents.BaseService;
import com.globant.eventscorelib.baseFragments.BaseFragment;
import com.globant.eventscorelib.utils.CoreConstants;

import java.util.List;

abstract public class BasePagerActivity extends BaseActivity {

    private PageAdapter pageAdapter;
    private int mCurrentFragmentPosition = 0;
    //private List<Fragment> mFragments;

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
//        prepareAdapter();
//        initialResumeFragment();
        prepareTitleStrip();
    }

    private void prepareTitleStrip() {
        PagerTitleStrip titleStrip = (PagerTitleStrip) findViewById(R.id.pager_title_strip);
        titleStrip.setTextColor(getResources().getColor(R.color.white));
    }

    protected void initialResumeFragment() {
        //onResume first fragment
        if (mCurrentFragmentPosition == 0) {
            FragmentLifecycle fragmentToShow = (FragmentLifecycle) pageAdapter.getItem(0);
            fragmentToShow.onResumeFragment();
        }
    }

    protected void prepareAdapter() {
        final List<Fragment> fragments = getFragments();
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
                FragmentLifecycle fragmentToHide = (FragmentLifecycle)fragments.get(mCurrentFragmentPosition);
                fragmentToHide.onPauseFragment();

                FragmentLifecycle fragmentToShow = (FragmentLifecycle)fragments.get(newPosition);
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

    public class PageAdapter extends FragmentStatePagerAdapter {

        private List<Fragment> mAdapterFragments;

        public PageAdapter(FragmentManager mFragmentManager, List<Fragment> fragments) {
            super(mFragmentManager);
            this.mAdapterFragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return mAdapterFragments.get(position);
        }

        @Override
        public int getCount() {
            return mAdapterFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getTitlesList().get(position).toUpperCase();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
        }

        //        @Override
//        public Object instantiateItem(ViewGroup container, int position) {
//            Fragment fragment = (Fragment) super.instantiateItem(container, position);
//            mAdapterFragments.set(position, fragment);
//            return fragment;
//        }
    }

    public interface FragmentLifecycle {
        public void onPauseFragment();
        public void onResumeFragment();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        for (BaseFragment fragment : mFragments) {
            BaseService.ActionListener listener = fragment.getActionListener();
            if (listener != null) {
                getService().disengage(listener.getBindingKey());
            }
        }
    }
}
