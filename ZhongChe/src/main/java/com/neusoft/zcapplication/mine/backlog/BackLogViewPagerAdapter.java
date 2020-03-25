package com.neusoft.zcapplication.mine.backlog;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Author: TenzLiu
 * Time: 2018/6/14 11:29
 * Desc:
 */

public class BackLogViewPagerAdapter extends FragmentPagerAdapter {

    //添加fragment的集合
    private List<Fragment> mFragmentList;
    //添加标题的集合
    private List<String> mTilteList;

    public BackLogViewPagerAdapter(FragmentManager fm, List<Fragment> fragmentList, List<String> tilteList) {
        super(fm);
        mFragmentList = fragmentList;
        mTilteList = tilteList;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    //获取标题
    @Override
    public CharSequence getPageTitle(int position) {
        return mTilteList.get(position);
    }

}
