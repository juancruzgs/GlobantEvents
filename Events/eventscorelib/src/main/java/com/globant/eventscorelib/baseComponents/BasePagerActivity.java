package com.globant.eventscorelib.baseComponents;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;

import com.ToxicBakery.viewpager.transforms.ZoomOutSlideTransformer;
import com.globant.eventscorelib.R;

import java.util.List;

abstract public class BasePagerActivity extends BaseActivity{

    PageAdapter pageAdapter;
    ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_pager_acivity);

        List<Fragment> fragments = getFragments();

        pageAdapter = new PageAdapter(getSupportFragmentManager(), fragments);

        mPager = (ViewPager)findViewById(R.id.viewpager);
        mPager.setAdapter(pageAdapter);
        mPager.setPageTransformer(true, new ZoomOutSlideTransformer());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_base_pager_acivity, menu);
        return true;
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
}
