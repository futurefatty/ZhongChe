package com.neusoft.zcapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.crcc.commonlib.event.Event;
import com.crcc.commonlib.event.EventBusUtil;
import com.crcc.commonlib.event.EventCode;
import com.neusoft.zcapplication.api.OrderApi;
import com.neusoft.zcapplication.base.BaseActivity;
import com.neusoft.zcapplication.event.Events;
import com.neusoft.zcapplication.http.URL;
import com.neusoft.zcapplication.http.now.CallBack;
import com.neusoft.zcapplication.http.now.RetrofitFactory;
import com.neusoft.zcapplication.tools.AMapUtil;
import com.neusoft.zcapplication.tools.AppManagerUtil;
import com.neusoft.zcapplication.tools.AppUtils;
import com.neusoft.zcapplication.tools.ToastUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 主页MainActivity
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {

    public static int radioGruop_H;//底部栏高度
    public AMapUtil mAMapUtil;
    private ViewPager viewPager;
    private MainViewPagerAdapter viewPagerAdapter;
    private RadioGroup radioGroup;//底部导航栏
    private long firstPressBackTime;
    private Handler approalHandler;
    private ImageView aprovalImg;
    private double latitude, longitude;   // 纬度信息
    private int pagerIndex;//viewpager下标

    private static final String PAGE = "PAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBusUtil.register(this);
        initView();
        initLocation();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Event event) {
        switch (event.getCode()) {
            case EventCode.INNTEAR_FLIGHT_APPLY_ORDER:
                viewPager.setCurrentItem(1);
                break;
        }
    }


    public static void startMainActivityOfFlight(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(PAGE, 0);
        context.startActivity(intent);
    }

    public static void startMainActivityOfApproval(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(PAGE, 1);
        context.startActivity(intent);
    }

    public static void startMainActivityOfMine(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(PAGE, 2);
        context.startActivity(intent);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int tab = intent.getIntExtra(PAGE, 0);
        switch (tab) {
            case 0:
                findViewById(R.id.main_nav_home).performClick();
                break;
            case 1:
                findViewById(R.id.main_nav_approval).performClick();
                break;
            case 2:
                findViewById(R.id.main_nav_mine).performClick();
                break;
        }
    }

    /**
     * 初始化控件
     */
    private void initView() {
        //导航栏
        radioGroup = (RadioGroup) findViewById(R.id.home_radioGroup);
        //获取导航栏高度，并保存
        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        radioGroup.measure(width, height);
        radioGruop_H = radioGroup.getMeasuredHeight();//radioGroup 的高度

        //导航栏点击事件
        findViewById(R.id.main_nav_home).setOnClickListener(this);
        findViewById(R.id.main_nav_approval).setOnClickListener(this);
        aprovalImg = (ImageView) findViewById(R.id.main_nav_approval_img);
        aprovalImg.setOnClickListener(this);
        findViewById(R.id.main_nav_mine).setOnClickListener(this);
        //初始化Fragment
        List<Fragment> frgList = new ArrayList<>();
        HomeFragment powerFrg = HomeFragment.getInstance();
        AllOrderFragment allOrderFragment = AllOrderFragment.getInstance();
        MineFragment mineFrg = MineFragment.getInstance();

        frgList.add(powerFrg);
        frgList.add(allOrderFragment);
        frgList.add(mineFrg);

        viewPagerAdapter = new MainViewPagerAdapter(frgList, getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.main_view_pager);
        viewPager.setAdapter(viewPagerAdapter);

        /**
         * Fragment切换监听事件
         */
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pagerIndex = position;
                switch (position) {
                    case 0:
                        //首页
                        radioGroup.check(R.id.main_nav_home);
                        aprovalImg.setImageResource(R.drawable.icon_application_nor);
                        break;
                    case 1:
                        //预定申请
                        radioGroup.check(R.id.main_nav_approval);
                        aprovalImg.setImageResource(R.drawable.icon_application_pressed);
                        break;
                    case 2:
                        //我的
                        radioGroup.check(R.id.main_nav_mine);
                        aprovalImg.setImageResource(R.drawable.icon_application_nor);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        long nowPressBackTime = System.currentTimeMillis();
        //如果两次点击时间间隔小于2秒，则退出app
        if (firstPressBackTime > 0 && nowPressBackTime - firstPressBackTime < 2000) {
            //退出app
            AppManagerUtil.getInstance().finishAllActivity();
            android.os.Process.killProcess(android.os.Process.myPid());
        } else {
            Toast.makeText(MainActivity.this, "再按一次退出应用", Toast.LENGTH_SHORT).show();
            firstPressBackTime = System.currentTimeMillis();
        }
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //获取用户是否有待办事项，若有显示待办事项标识，则不获取该数据
        getJobData();
        getInterCount();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mAMapUtil) {
            mAMapUtil.stopLocation();
            mAMapUtil.destroy();
        }
        EventBusUtil.unregister(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_nav_home:
                //主页
                radioGroup.check(v.getId());
                viewPager.setCurrentItem(0);
                pagerIndex = 0;
                aprovalImg.setImageResource(R.drawable.icon_application_nor);
                break;
            case R.id.main_nav_approval_img:
                //预定申请
                radioGroup.check(R.id.main_nav_approval);
                viewPager.setCurrentItem(1);
                pagerIndex = 1;
                aprovalImg.setImageResource(R.drawable.icon_application_pressed);
                break;
            case R.id.main_nav_approval:
                //预定申请
                pagerIndex = 1;
                radioGroup.check(R.id.main_nav_approval);
                viewPager.setCurrentItem(1);
                aprovalImg.setImageResource(R.drawable.icon_application_pressed);
                break;
            case R.id.main_nav_mine:
                //我的
                radioGroup.check(v.getId());
                viewPager.setCurrentItem(2);
                pagerIndex = 2;
                aprovalImg.setImageResource(R.drawable.icon_application_nor);
                break;
        }
    }

    /**
     * 设置我的tab栏提示点的显示状态
     */
    private void changeNavBarPointStatus() {
        TextView minePointTv = (TextView) findViewById(R.id.act_main_mine_point);
        int count = AppUtils.getInterTicketCount(MainActivity.this);
        boolean hasJob = AppUtils.hasJobToDo(MainActivity.this);
        if (count > 0 || hasJob) {
            minePointTv.setVisibility(View.VISIBLE);
        } else {
            minePointTv.setVisibility(View.GONE);
        }
    }

    /**
     * 提交预定申请单成功后，需要跳转到首页
     *
     * @param index
     */
    public void changeViewPagerIndex(int index) {
        viewPager.setCurrentItem(index, true);
    }

    /**
     * 百度定位
     */
    public void initLocation() {
        mAMapUtil = AMapUtil.getInstance();
        mAMapUtil.initLocation();
        mAMapUtil.setAMapLocationHandler(new AMapUtil.AMapLocationHandler() {
            @Override
            public void locateSuccess(double mLongitude, double mLatitude, String province, String city,
                                      String cityCode, String district, String address) {
                latitude = mLatitude;    //获取纬度信息
                longitude = mLongitude;    //获取经度信息
            }

            @Override
            public void locateFailed(String errorMessage) {
                ToastUtil.toast(errorMessage);
            }
        });
        mAMapUtil.startLocation();

    }

    /**
     * 获取经纬度
     *
     * @return
     */
    public HashMap<String, String> getLocation() {
        HashMap<String, String> map = new HashMap<>();
        map.put("fromlat", latitude + "");
        map.put("fromlng", longitude + "");
        return map;
    }

    /**
     * 返回当前选中的fragment下标
     *
     * @return
     */
    public int getPagerIndex() {
        return pagerIndex;
    }


    /**
     * 获取该用户是否有代办事件
     */
    private void getJobData() {
        Map<String, Object> params = new HashMap<>();
        params.put("ciphertext", "test");
        params.put("employeeCode", AppUtils.getUserInfo(MainActivity.this).getEmployeeCode());
        params.put("loginType", URL.LOGIN_TYPE);

        showLoading();
        RetrofitFactory.getInstance().createApi(OrderApi.class).getBacklogCount(params)
                .enqueue(new CallBack<Map<String, Integer>>() {
                    @Override
                    public void success(Map<String, Integer> object) {
                        dismissLoading();
                        Integer totalCount = object.get("total");
                        //获取用户是否有待办事件数据的成功的回调事件
                        if (totalCount > 0) {
                            AppUtils.saveJobToDoStatus(MainActivity.this, true);//有待办事项
                        } else {
                            AppUtils.saveJobToDoStatus(MainActivity.this, false);
                        }
                        changeNavBarPointStatus();
                    }

                    @Override
                    public void fail(String code) {
                        dismissLoading();
                        //我的待办
                        AppUtils.saveJobToDoStatus(MainActivity.this, false);//无待办事项
                    }
                });
    }

    /**
     * 是否有带选择国际机票方案
     */
    private void getInterCount() {
        Map<String, Object> params = new HashMap<>();
        params.put("ciphertext", "test");
        params.put("employeeCode", AppUtils.getUserInfo(MainActivity.this).getEmployeeCode());
        params.put("loginType", URL.LOGIN_TYPE);

        showLoading();
        RetrofitFactory.getInstance().createApi(OrderApi.class).getInterCount(params)
                .enqueue(new CallBack<Integer>() {
                    @Override
                    public void success(Integer integer) {
                        dismissLoading();
                        //获取是否有带选择国际机票方案的事项，返回成功的回调事件
                        AppUtils.saveInterTicketCount(MainActivity.this, integer);
                        changeNavBarPointStatus();
                    }

                    @Override
                    public void fail(String code) {
                        dismissLoading();
                        //待选国际机票方案个数
                        AppUtils.saveInterTicketCount(MainActivity.this, 0);
                    }
                });
    }

}
