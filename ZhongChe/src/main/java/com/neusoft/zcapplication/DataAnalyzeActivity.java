package com.neusoft.zcapplication;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.neusoft.zcapplication.dataAnalyze.CompanyDataFragment;
import com.neusoft.zcapplication.dataAnalyze.StaffGoFragment;
import com.neusoft.zcapplication.dataAnalyze.personDataFragment;
import com.neusoft.zcapplication.event.Events;
import com.neusoft.zcapplication.mine.backlog.BackLogViewPagerAdapter;
import com.neusoft.zcapplication.tools.AppUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class DataAnalyzeActivity extends AppCompatActivity implements View.OnClickListener{

    private TabLayout tl_data;
    private ViewPager vp_data;

    private BackLogViewPagerAdapter mBackLogViewPagerAdapter;
    private List<Fragment> mFragmentList;
    private List<String> mTitleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_analyze);
        initView();
        initData();
    }

    private void initView(){
        AppUtils.setStateBar(DataAnalyzeActivity.this,findViewById(R.id.frg_status_bar));
        findViewById(R.id.btn_back).setOnClickListener(this);
        tl_data = (TabLayout) findViewById(R.id.tl_dataAnalyze);
        vp_data = (ViewPager) findViewById(R.id.vp_dataAnalyze);
        tl_data.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        EventBus.getDefault().post(new Events.PersonDataHandleChange());
                        break;
                    case 1:
                        EventBus.getDefault().post(new Events.CompanyDataHandleChange());
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
        mFragmentList.add(personDataFragment.getInstance());
        mFragmentList.add(CompanyDataFragment.getInstance());
        mFragmentList.add(StaffGoFragment.getInstance());
        mTitleList.add("个人");
        mTitleList.add("组织");
        mTitleList.add("员工去向");
        //tablayout获取集合中的名称
        tl_data.addTab(tl_data.newTab().setText(mTitleList.get(0)));
        tl_data.addTab(tl_data.newTab().setText(mTitleList.get(1)));
        tl_data.addTab(tl_data.newTab().setText(mTitleList.get(2)));
        mBackLogViewPagerAdapter = new BackLogViewPagerAdapter(getSupportFragmentManager(), mFragmentList, mTitleList);
        vp_data.setAdapter(mBackLogViewPagerAdapter);
        tl_data.setupWithViewPager(vp_data);
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
