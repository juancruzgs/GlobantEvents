package com.globant.eventscorelib.baseComponents;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.ToxicBakery.viewpager.transforms.ZoomOutSlideTransformer;
import com.globant.eventscorelib.R;

import java.util.ArrayList;
import java.util.List;

abstract public class BasePagerActivity extends BaseActivity{

    PageAdapter pageAdapter;
    ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_pager);

        List<Fragment> fragments = getFragments();

        pageAdapter = new PageAdapter(getSupportFragmentManager(), fragments);

        ViewPager pager = (ViewPager)findViewById(R.id.viewpager);
        pager.setAdapter(pageAdapter);
        pager.setPageTransformer(true, new ZoomOutSlideTransformer());
        PagerTitleStrip titleStrip = (PagerTitleStrip) findViewById(R.id.pager_title_strip);
        titleStrip.setTextColor(getResources().getColor(R.color.white));
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
