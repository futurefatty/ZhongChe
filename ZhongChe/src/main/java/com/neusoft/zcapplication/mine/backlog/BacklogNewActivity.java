package com.neusoft.zcapplication.mine.backlog;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.base.BaseActivity;
import com.neusoft.zcapplication.event.Events;
import com.neusoft.zcapplication.tools.AppUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的待办
 */
public class BacklogNewActivity extends BaseActivity implements View.OnClickListener {

    private TabLayout tl_backlog;
    private ViewPager vp_backlog;

    private BackLogViewPagerAdapter mBackLogViewPagerAdapter;
    private List<Fragment> mFragmentList;
    private List<String> mTitleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backlog_new);
        initView();
        initData();
    }

    private void initView(){
        AppUtils.setStateBar(BacklogNewActivity.this,findViewById(R.id.frg_status_bar));
        findViewById(R.id.btn_back).setOnClickListener(this);
        tl_backlog = (TabLayout) findViewById(R.id.tl_backlog);
        vp_backlog = (ViewPager) findViewById(R.id.vp_backlog);
        tl_backlog.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        EventBus.getDefault().post(new Events.ApplyVerifyChange());
                        break;
                    case 1:
                        EventBus.getDefault().post(new Events.BusinessCarVerifyChange());
                        break;
                    case 2:
                        EventBus.getDefault().post(new Events.BusinessCarHandleChange());
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initData() {
        mFragmentList = new ArrayList<>();
        mTitleList = new ArrayList<>();
        mFragmentList.add(ApplyVerifyFragment.getInstance());
        mFragmentList.add(BusinessCarVerifyFragment.getInstance());
        mFragmentList.add(BusinessCarHandleFragment.getInstance());
        mTitleList.add("预定申请审核");
        mTitleList.add("公务用车审核");
        mTitleList.add("我的办理");
        //tablayout获取集合中的名称
        tl_backlog.addTab(tl_backlog.newTab().setText(mTitleList.get(0)));
        tl_backlog.addTab(tl_backlog.newTab().setText(mTitleList.get(1)));
        tl_backlog.addTab(tl_backlog.newTab().setText(mTitleList.get(2)));
        mBackLogViewPagerAdapter = new BackLogViewPagerAdapter(getSupportFragmentManager(),mFragmentList,mTitleList);
        vp_backlog.setAdapter(mBackLogViewPagerAdapter);
        tl_backlog.setupWithViewPager(vp_backlog);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
        }
    }

}
