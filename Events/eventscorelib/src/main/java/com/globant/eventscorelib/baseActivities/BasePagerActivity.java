package com.globant.eventscorelib.baseActivities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;

import com.ToxicBakery.viewpager.transforms.ZoomOutSlideTransformer;
import com.globant.eventscorelib.R;
import com.globant.eventscorelib.baseComponents.BaseService;
import com.globant.eventscorelib.baseFragments.BaseFragment;
import com.globant.eventscorelib.utils.CoreConstants;

import java.util.List;

abstract public class BasePagerActivity extends BaseActivity {

    private PageAdapter pageAdapter;
    private int mCurrentFragmentPosition = 0;

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
        prepareAdapter();
        prepareTitleStrip();
    }

    private void prepareAdapter() {
        List<Fragment> fragmentList = getFragments();
        pageAdapter = new PageAdapter(getSupportFragmentManager(), fragmentList);
        final ViewPager pager = (ViewPager) findViewById(R.id.viewpager);
        pager.setAdapter(pageAdapter);
        pager.setPageTransformer(true, new ZoomOutSlideTransformer());
        pager.setOffscreenPageLimit(fragmentList.size());
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int newPosition) {
                FragmentLifecycle fragmentToHide = (FragmentLifecycle) pageAdapter.getItem(mCurrentFragmentPosition);
                fragmentToHide.onPauseFragment();

                FragmentLifecycle fragmentToShow = (FragmentLifecycle) pageAdapter.getItem(newPosition);
                fragmentToShow.onResumeFragment();

                mCurrentFragmentPosition = newPosition;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                    if (pageAdapter.getItem(mCurrentFragmentPosition) instanceof OnPageScrollStateChangedCancelAnimation) {
                        OnPageScrollStateChangedCancelAnimation fragmentToCancel = (OnPageScrollStateChangedCancelAnimation) pageAdapter.getItem(mCurrentFragmentPosition);
                        fragmentToCancel.cancelAnimations();
                    }
                }
            }
        });
    }

    public interface OnPageScrollStateChangedCancelAnimation{
        void cancelAnimations();
    }

    private void prepareTitleStrip() {
        PagerTitleStrip titleStrip = (PagerTitleStrip) findViewById(R.id.pager_title_strip);
        titleStrip.setTextColor(getResources().getColor(R.color.white));
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialResumeFragment();
    }

    private void initialResumeFragment() {
        //onResume first fragment
        if (mCurrentFragmentPosition == 0) {
            FragmentLifecycle fragmentToShow = (FragmentLifecycle) pageAdapter.getItem(mCurrentFragmentPosition);
            fragmentToShow.onResumeFragment();
        }
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

//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) {
//        }
    }

    public interface FragmentLifecycle {
        void onPauseFragment();

        void onResumeFragment();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }
}
