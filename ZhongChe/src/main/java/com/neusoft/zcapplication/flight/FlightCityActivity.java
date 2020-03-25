package com.neusoft.zcapplication.flight;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;

import com.crcc.commonlib.base.SyBaseActivity;
import com.crcc.commonlib.event.Event;
import com.crcc.commonlib.event.EventCode;
import com.crcc.commonlib.utils.StatusBarUtil;
import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.city.CityModel;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;

public class FlightCityActivity extends SyBaseActivity {
    public static final String SELECT_CITY_MODEL = "SELECT_CITY_MODEL";
    /**
     * 偏移位置
     * 0 国内 1国际
     */
    public static final String TAB_POSITION = "TAB_POSITION";
    Map<String, Fragment> fragmentMap = new HashMap<>();

    @Override
    protected void initView(Bundle savedInstanceState) {
        new Builder(this)
                .setTitleModel(Builder.NO_TITLE)
                .isMuliteView(false)
                .build();
        ButterKnife.bind(this);
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.colorPrimary), 0);
        initView();
    }


    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(Event event) {
        switch (event.getCode()) {
            case EventCode.INTERNATION_SELECT_CITY:
                CityModel cityModel = (CityModel) event.getData();
                Intent intent = new Intent();
                intent.putExtra(SELECT_CITY_MODEL, cityModel);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }

    }

    private void initView() {
        FlightCityFragment flightCityFragment = FlightCityFragment.newInstance(getIntent().getIntExtra(TAB_POSITION, 0));
        flightCityFragment.setBackClickListener(() -> finish());
        flightCityFragment.setSearchClickListener(() -> showFragment(SearchCityFragment.TAG, FlightCityFragment.TAG));
        SearchCityFragment searchCityFragment = SearchCityFragment.newInstance();
        searchCityFragment.setOnCancelListener(() -> showFragment(FlightCityFragment.TAG, SearchCityFragment.TAG));
        fragmentMap.put(FlightCityFragment.TAG, flightCityFragment);
        fragmentMap.put(SearchCityFragment.TAG, searchCityFragment);
        showFragment(FlightCityFragment.TAG, SearchCityFragment.TAG);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_flight_city;
    }

    public void showFragment(String showTag, String hideTag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // 设置动画
        transaction.setCustomAnimations(R.anim.alpha_in, R.anim.alpha_out);
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(showTag);
        if (fragment == null) {
            // 没有找到表示没有被创建过
            fragment = fragmentMap.get(showTag);
            // 直接add
            transaction.add(R.id.fl_content, fragment, showTag);
        } else {
            // 找到了，表示已经被add了，所以直接show
            transaction.show(fragment);
        }
        fragment = getSupportFragmentManager().findFragmentByTag(hideTag);
        if (fragment != null) {
            transaction.hide(fragment);
        }
        transaction.commit();
    }

}
