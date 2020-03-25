package com.neusoft.zcapplication.mine.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.neusoft.zcapplication.MainViewPagerAdapter;
import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.base.BaseActivity;
import com.neusoft.zcapplication.tools.AppUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的订单
 **/
public class OrderActivity extends BaseActivity implements View.OnClickListener {

    private TextView left_tv, middle_tv, right_tv, right2_tv;
    private ViewPager viewPager;
    private MainViewPagerAdapter viewPagerAdapter;
    private int currentItem = 0;// 当前页卡编号
    private List<Fragment> frgList;

    public static final String POSITION = "position";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        initView();
        currentItem = getIntent().getIntExtra(POSITION, 0);
        viewPager.setCurrentItem(currentItem);
    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView interPointTv = (TextView) findViewById(R.id.act_order_inter_point);
        int count = AppUtils.getInterTicketCount(OrderActivity.this);
        if (count > 0) {
            interPointTv.setVisibility(View.VISIBLE);
        } else {
            interPointTv.setVisibility(View.GONE);
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        currentItem = getIntent().getIntExtra(POSITION, 0);
        if (currentItem == 0) {
            InternalTicketFragment internalTicketFragment = (InternalTicketFragment) frgList.get(currentItem);
            internalTicketFragment.refresh();
        }
        viewPager.setCurrentItem(currentItem);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        left_tv = (TextView) findViewById(R.id.left_tv);
        middle_tv = (TextView) findViewById(R.id.middle_tv);
        right_tv = (TextView) findViewById(R.id.right_tv);
        left_tv.setOnClickListener(this);
        middle_tv.setOnClickListener(this);
        right_tv.setOnClickListener(this);
        findViewById(R.id.btn_back).setOnClickListener(this);
        //初始化Fragment
        frgList = new ArrayList<>();
        //国内机票
        InternalTicketFragment internalTicket = InternalTicketFragment.getInstance();
        //国际机票
        ExternalTicketFragment externalTicket = ExternalTicketFragment.getInstance();
        //酒店
        HotelFragment hotel = HotelFragment.getInstance();
        frgList.add(internalTicket);
        frgList.add(externalTicket);
        frgList.add(hotel);

        //设置订单适配器
        viewPagerAdapter = new MainViewPagerAdapter(frgList, getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.order_view_pager);
        viewPager.setAdapter(viewPagerAdapter);

        /**
         * 切换监听事件
         */
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        //国内机票
                        setLineView(0);
                        left_tv.setTextColor(getResources().getColor(R.color.colorAccent));
                        middle_tv.setTextColor(getResources().getColor(R.color.tab_unpressed));
                        right_tv.setTextColor(getResources().getColor(R.color.tab_unpressed));
                        break;
                    case 1:
                        //国际机票
                        setLineView(1);
                        left_tv.setTextColor(getResources().getColor(R.color.tab_unpressed));
                        middle_tv.setTextColor(getResources().getColor(R.color.colorAccent));
                        right_tv.setTextColor(getResources().getColor(R.color.tab_unpressed));
                        break;
                    case 2:
                        //酒店
                        setLineView(2);
                        left_tv.setTextColor(getResources().getColor(R.color.tab_unpressed));
                        middle_tv.setTextColor(getResources().getColor(R.color.tab_unpressed));
                        right_tv.setTextColor(getResources().getColor(R.color.colorAccent));
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        AppUtils.setStateBar(OrderActivity.this, findViewById(R.id.frg_status_bar));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_tv:
                //国内机票
                viewPager.setCurrentItem(0);
                break;
            case R.id.middle_tv:
                //国际机票
                viewPager.setCurrentItem(1);
                break;
            case R.id.right_tv:
                //酒店
                viewPager.setCurrentItem(2);
                break;
            case R.id.btn_back:
                //返回
                finish();
                break;

        }
    }

    /**
     * 设置选中样式
     *
     * @param position
     */
    private void setLineView(int position) {
        LinearLayout leftLayout = (LinearLayout) findViewById(R.id.act_list_cursor);
        LinearLayout middleLayout = (LinearLayout) findViewById(R.id.act_list_cursor1);
        LinearLayout rightLayout = (LinearLayout) findViewById(R.id.act_list_cursor2);
        if (position == 0) {
            leftLayout.setVisibility(View.VISIBLE);
            middleLayout.setVisibility(View.INVISIBLE);
            rightLayout.setVisibility(View.INVISIBLE);
        } else if (position == 1) {
            leftLayout.setVisibility(View.INVISIBLE);
            middleLayout.setVisibility(View.VISIBLE);
            rightLayout.setVisibility(View.INVISIBLE);
        } else if (position == 2) {
            leftLayout.setVisibility(View.INVISIBLE);
            middleLayout.setVisibility(View.INVISIBLE);
            rightLayout.setVisibility(View.VISIBLE);
        } else {
            leftLayout.setVisibility(View.INVISIBLE);
            middleLayout.setVisibility(View.INVISIBLE);
            rightLayout.setVisibility(View.INVISIBLE);
        }
    }
}
