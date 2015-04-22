package com.globant.eventscorelib.baseComponents;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.ToxicBakery.viewpager.transforms.ZoomOutSlideTransformer;
import com.globant.eventscorelib.R;
import com.globant.eventscorelib.utils.Logger;

import java.util.ArrayList;
import java.util.List;

import static android.support.v4.view.ViewPager.OnPageChangeListener;
import static com.globant.eventscorelib.baseComponents.BaseFragment.TitleChangeable;

abstract public class BasePagerActivity extends BaseActivity  implements OnPageChangeListener {

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
        mPager.setOnPageChangeListener(this);
        mPager.setPageTransformer(true, new ZoomOutSlideTransformer());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_base_pager_acivity, menu);
        return true;
    }

    protected abstract List<Fragment> getFragments();

    public class PageAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments;

        public PageAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return this.fragments.get(position);
        }

        @Override
        public int getCount() {
            return this.fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return ((BaseFragment)fragments.get(position)).getTitle().toUpperCase();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        Logger.d("onPageScrollStateChanged");
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Logger.d("onPageScrolled");
//        mPager = (ViewPager)findViewById(R.id.viewpager);
//        CharSequence pageTitle = mPager.getAdapter().getPageTitle(position);
//        if (pageTitle != null){
//            changeFragmentTitle(pageTitle.toString());
//        } else {
//            changeFragmentTitle("");
//        }
    }

    @Override
    public void onPageSelected(int position) {
        Logger.d("onPageSelected");
    }
}
