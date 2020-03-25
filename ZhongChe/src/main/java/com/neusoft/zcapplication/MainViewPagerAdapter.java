package com.neusoft.zcapplication;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/3/7.
 * 主页viewpager
 */

public class MainViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> list;

    public MainViewPagerAdapter(List<Fragment> list, FragmentManager fm) {
        super(fm);
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
