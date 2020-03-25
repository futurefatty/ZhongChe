package com.neusoft.zcapplication.mine.journey;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.neusoft.zcapplication.BannerView.Banner;
import com.neusoft.zcapplication.MainViewPagerAdapter;
import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.base.BaseActivity;
import com.neusoft.zcapplication.tools.AppUtils;
import com.neusoft.zcapplication.tools.GlideImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * 行程
 */
public class JourneyActivity extends BaseActivity implements View.OnClickListener{
    private static final int TAG_COUNTS = 2;//选项卡数量
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

        setContentView(R.layout.activity_journey);
        initView();
//        initImageView();
    }


    /**
     *
     */
    private void initView(){
        findViewById(R.id.left_tv).setOnClickListener(this);
        findViewById(R.id.right_tv).setOnClickListener(this);
        findViewById(R.id.btn_back).setOnClickListener(this);
        //初始化Fragment
        List<Fragment> frgList = new ArrayList<>();
        NowJourneyFragment powerFrg = NowJourneyFragment.getInstance();
        HistoryJourneyFragment newsFrg = HistoryJourneyFragment.getInstance();
        frgList.add(powerFrg);
        frgList.add(newsFrg);

        viewPagerAdapter = new MainViewPagerAdapter(frgList,getSupportFragmentManager());
        viewPager = (ViewPager)findViewById(R.id.journey_view_pager);
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
                        break;
                    case 1:
                        setLineView(1);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        Banner banner = (Banner)findViewById(R.id.journey_banner);
        int sw = getDeviceWidth();
        int height = (int)(sw * 0.54);
//        Log.i("--->","banner：宽高："+sw +";"+height);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,height);
        banner.setLayoutParams(lp);
        List<Integer> list = new ArrayList<>();
        list.add(R.mipmap.banner_01);
        list.add(R.mipmap.banner_02);
        banner.setImages(list);

        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(list);
        //banner设置方法全部调用完毕时最后调用
        banner.start();

        AppUtils.setStateBar(JourneyActivity.this,findViewById(R.id.frg_status_bar));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.left_tv:
                //主页
                viewPager.setCurrentItem(0);
                break;
            case R.id.right_tv:
                //比价
                viewPager.setCurrentItem(1);
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
        LinearLayout rightLayout = (LinearLayout)findViewById(R.id.act_list_cursor1);
        TextView leftText = (TextView)findViewById(R.id.left_tv);
        TextView rightText = (TextView)findViewById(R.id.right_tv);

        if(position  == 1){
            leftLayout.setVisibility(View.INVISIBLE);
            rightLayout.setVisibility(View.VISIBLE);
            leftText.setTextColor(getResources().getColor(R.color.tab_unpressed));
            rightText.setTextColor(getResources().getColor(R.color.colorAccent));
        }else{
            leftLayout.setVisibility(View.VISIBLE);
            rightLayout.setVisibility(View.INVISIBLE);
            leftText.setTextColor(getResources().getColor(R.color.colorAccent));
            rightText.setTextColor(getResources().getColor(R.color.tab_unpressed));
        }
    }
}
