package com.neusoft.zcapplication.flight;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.crcc.commonlib.adapter.ViewPagerFragmentAdapter;
import com.crcc.commonlib.base.BaseFragment;
import com.crcc.commonlib.inter.Action;
import com.neusoft.zcapplication.R;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * author:Six
 * Date:2019/5/28
 */
public class FlightCityFragment extends BaseFragment {

    public static final String TAG = FlightCityFragment.class.getSimpleName();
    @BindView(R.id.indicator)
    MagicIndicator indicator;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.search_back)
    ImageView searchBack;
    @BindView(R.id.tv_search_input)
    TextView tvSearchInput;

    public static final String TAB_POSITION = "TAB_POSITION";

    private int tabPosition;

    public static FlightCityFragment newInstance(int tabPosition) {
        FlightCityFragment flightCityFragment = new FlightCityFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TAB_POSITION, tabPosition);
        flightCityFragment.setArguments(bundle);
        return flightCityFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_flight_city;
    }

    private Action searchClickListener, backClickListener;

    public void setSearchClickListener(Action searchClickListener) {
        this.searchClickListener = searchClickListener;
    }

    public void setBackClickListener(Action backClickListener) {
        this.backClickListener = backClickListener;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            tabPosition = bundle.getInt(TAB_POSITION);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvSearchInput.setOnClickListener(v -> searchClickListener.action());
        searchBack.setOnClickListener(v -> backClickListener.action());
        String[] tabs = new String[]{
                "国内", "国际/港澳台"
        };
        List<Fragment> fragments = Arrays.asList(FlightCityListFragment.newInstance(FlightCityListFragment.INLAND),
                FlightCityListFragment.newInstance(FlightCityListFragment.INTERNATION));
        viewPager.setAdapter(new ViewPagerFragmentAdapter(getChildFragmentManager(), fragments));
        CommonNavigator commonNavigator = new CommonNavigator(mContext);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return tabs.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
                colorTransitionPagerTitleView.setNormalColor(ContextCompat.getColor(context, R.color.C333333));
                colorTransitionPagerTitleView.setTextSize(14);
                colorTransitionPagerTitleView.setSelectedColor(ContextCompat.getColor(context, R.color.C333333));
                colorTransitionPagerTitleView.setText(tabs[index]);
                colorTransitionPagerTitleView.setOnClickListener(view -> viewPager.setCurrentItem(index));
                return colorTransitionPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setColors(ContextCompat.getColor(context, R.color.CC70019));
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                return indicator;
            }
        });
        commonNavigator.setAdjustMode(true);
        indicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(indicator, viewPager);
        viewPager.setCurrentItem(tabPosition);
    }


}
