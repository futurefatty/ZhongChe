package com.neusoft.zcapplication.mine.mostusedinfo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.neusoft.zcapplication.MainViewPagerAdapter;
import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.base.BaseActivity;
import com.neusoft.zcapplication.tools.AppUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 常用信息
 **/
public class MostUsedInfoActivity extends BaseActivity implements View.OnClickListener{
    private static final int TAG_COUNTS = 3;//选项卡数量
    TextView left_tv,middle_tv,right_tv;
    private ViewPager viewPager;
    //    private ViewPager viewPager;
    private MainViewPagerAdapter viewPagerAdapter;
    private Animation animation;
    private int offset = 0;// 动画图片偏移量
    private int currentItem = 0;// 当前页卡编号
    private int bmpW;// 动画图片宽度

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_most_used_info);
        initView();
//        initImageView();
    }


    private void initView(){
        left_tv = (TextView) findViewById(R.id.left_tv);
        middle_tv = (TextView) findViewById(R.id.middle_tv);
        right_tv = (TextView) findViewById(R.id.right_tv);
        left_tv.setOnClickListener(this);
        middle_tv.setOnClickListener(this);
        right_tv.setOnClickListener(this);
        findViewById(R.id.btn_back).setOnClickListener(this);
        //初始化Fragment
        List<Fragment> frgList = new ArrayList<>();
        PassengerFragment passengerFrg = PassengerFragment.getInstance();
        ReimburseVoucherFragment reimburseFrg = ReimburseVoucherFragment.getInstance();
        VisaFragment visaFrg = VisaFragment.getInstance();
        frgList.add(passengerFrg);
        frgList.add(reimburseFrg);
        frgList.add(visaFrg);

        viewPagerAdapter = new MainViewPagerAdapter(frgList,getSupportFragmentManager());
        viewPager = (ViewPager)findViewById(R.id.info_view_pager);
        viewPager.setAdapter(viewPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        setLineView(0);
                        left_tv.setTextColor(getResources().getColor(R.color.colorAccent));
                        middle_tv.setTextColor(getResources().getColor(R.color.tab_unpressed));
                        right_tv.setTextColor(getResources().getColor(R.color.tab_unpressed));
                        break;
                    case 1:
                        setLineView(1);
                        left_tv.setTextColor(getResources().getColor(R.color.tab_unpressed));
                        middle_tv.setTextColor(getResources().getColor(R.color.colorAccent));
                        right_tv.setTextColor(getResources().getColor(R.color.tab_unpressed));
                        break;
                    case 2:
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

        AppUtils.setStateBar(MostUsedInfoActivity.this,findViewById(R.id.frg_status_bar));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.left_tv:
                viewPager.setCurrentItem(0);
                break;
            case R.id.middle_tv:
                viewPager.setCurrentItem(1);
                break;
            case R.id.right_tv:
                viewPager.setCurrentItem(2);
                break;
            case R.id.btn_back:
                finish();
                break;

        }
    }
    public int getDeviceWidth(){
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        return w_screen;
    }
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dpTopx(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private void setLineView(int position){
        LinearLayout leftLayout = (LinearLayout)findViewById(R.id.act_list_cursor);
        LinearLayout middleLayout = (LinearLayout)findViewById(R.id.act_list_cursor1);
        LinearLayout rightLayout = (LinearLayout)findViewById(R.id.act_list_cursor2);

        if(position  == 0){
            leftLayout.setVisibility(View.VISIBLE);
            middleLayout.setVisibility(View.INVISIBLE);
            rightLayout.setVisibility(View.INVISIBLE);
        }else if(position  == 1){
            leftLayout.setVisibility(View.INVISIBLE);
            middleLayout.setVisibility(View.VISIBLE);
            rightLayout.setVisibility(View.INVISIBLE);
        }
        else {
            leftLayout.setVisibility(View.INVISIBLE);
            middleLayout.setVisibility(View.INVISIBLE);
            rightLayout.setVisibility(View.VISIBLE);
        }
    }
}
