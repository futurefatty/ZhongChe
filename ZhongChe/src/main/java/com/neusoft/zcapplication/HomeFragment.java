package com.neusoft.zcapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.crcc.commonlib.utils.UsefulToast;
import com.neusoft.zcapplication.BannerView.Banner;
import com.neusoft.zcapplication.BannerView.listener.OnBannerListener;
import com.neusoft.zcapplication.Bean.FlightItem;
import com.neusoft.zcapplication.Bean.User;
import com.neusoft.zcapplication.Calendar.CalendarActivity;
import com.neusoft.zcapplication.CarService.CarServiceActivity;
import com.neusoft.zcapplication.Constant.Constant;
import com.neusoft.zcapplication.HotelService.HotelServiceActivity;
import com.neusoft.zcapplication.OtherService.OtherServiceActivity;
import com.neusoft.zcapplication.TicketService.TicketServiceActivity;
import com.neusoft.zcapplication.api.NoticeApi;
import com.neusoft.zcapplication.api.OrderApi;
import com.neusoft.zcapplication.base.BaseFragment;
import com.neusoft.zcapplication.city.AirportActivity;
import com.neusoft.zcapplication.entity.GetNotice;
import com.neusoft.zcapplication.entity.GetScheduleData;
import com.neusoft.zcapplication.event.Events;
import com.neusoft.zcapplication.flight.internation.fragment.HomeInternationJourneyFragment;
import com.neusoft.zcapplication.home.NoticeListActivity;
import com.neusoft.zcapplication.home.PersonalTripActivity;
import com.neusoft.zcapplication.home.PoiActivity;
import com.neusoft.zcapplication.home.QuestionActivity;
import com.neusoft.zcapplication.http.now.CallBack;
import com.neusoft.zcapplication.http.now.RetrofitFactory;
import com.neusoft.zcapplication.tools.AlertUtil;
import com.neusoft.zcapplication.tools.AppUtils;
import com.neusoft.zcapplication.tools.DateUtils;
import com.neusoft.zcapplication.tools.FontSizeUtils;
import com.neusoft.zcapplication.tools.GlideImageLoader;
import com.neusoft.zcapplication.tools.StringUtil;
import com.neusoft.zcapplication.tools.ToastUtil;
import com.neusoft.zcapplication.widget.DefinedGridView;
import com.neusoft.zcapplication.widget.MarqueeDownTextView;
import com.neusoft.zcapplication.widget.MarqueeTextView;
import com.neusoft.zcapplication.widget.MarqueeTextViewClickListener;
import com.neusoft.zcapplication.widget.SlideTabBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 首页
 */

public class HomeFragment extends BaseFragment implements SlideTabBar.OnTabBarChangeListener,
        View.OnClickListener {

    private LinearLayout ly_flight, ly_hotel, ly_flight_hotel, select_apply_order;
    private RadioGroup radioGroup_reason;
    private TextView applyOrderId, applyOrder;
    private View homeFrgView;
    private TextView tv_city_from, tv_city_to, city_hotel1, time_start, time_end, btn_search;
    private Map<String, Object> selectOrder;
    //飞机舱位
    private int travelType;
    private String hotelCityName, hotelCityCode;
    //true 查询酒店，用的酒店编码，false 三字码
    private boolean useHotelCode;
    //酒店区域的经纬度信息
    private double lat, lng;
    //酒店区域的地址
    private String hotelAddress;
    //1-因公出行;0-因私出行
    private int reason = 1;
    //0-机票；1-酒店 2国际
    private int flightOrHotel = 0;
    private MarqueeDownTextView marqueeTv;
    private String[] textArrays = null;
    //因公因私
    private View llPublicOrPrivate;
    //预定审批单
    private View llapplyOrder;

    public static HomeFragment getInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        FragmentManager fm = getChildFragmentManager();
        Fragment internationFragment = fm.findFragmentByTag(HomeInternationJourneyFragment.TAG);
        if (internationFragment != null) {
            fm.beginTransaction().hide(internationFragment).commit();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (marqueeTv != null) {
            marqueeTv.releaseResources();
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (null == homeFrgView) {
            homeFrgView = inflater.inflate(R.layout.frg_home, container, false);
            initView();
        } else {
            ViewGroup parent = (ViewGroup) homeFrgView.getParent();
            if (parent != null) {
                parent.removeView(homeFrgView);
            }
        }
        return homeFrgView;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Events.DeleteOrderIds deleteOrderIds) {
        if (deleteOrderIds != null
                && deleteOrderIds.getOrderApplyId() != null
                && deleteOrderIds.getOrderApplyId().size() > 0) {
            for (int i = 0; i < deleteOrderIds.getOrderApplyId().size(); i++) {
                String applyOrderIds = applyOrder.getText().toString().trim();
                if (!StringUtil.isEmpty(applyOrderIds)) {
                    String[] splitApplyOrder = applyOrderIds.split(",");
                    for (int j = 0; j < splitApplyOrder.length; j++) {
                        if (splitApplyOrder[j].equals(deleteOrderIds.getOrderApplyId().get(i))) {
                            applyOrder.setText("");
                        }
                    }
                }
                String applyOrderIdIds = applyOrderId.getText().toString().trim();
                if (!StringUtil.isEmpty(applyOrderIdIds)) {
                    String[] splitApplyOrderIdIds = applyOrderIdIds.split(",");
                    for (int k = 0; k < splitApplyOrderIdIds.length; k++) {
                        if (splitApplyOrderIdIds[k].equals(deleteOrderIds.getOrderApplyId().get(i))) {
                            applyOrderId.setText("");
                        }
                    }
                }
            }
        }
    }

    int[] btnBookIds = new int[]{
            R.id.btn_flight_book,
            R.id.btn_outer_flight_book,
            R.id.btn_hotel_book,
    };

    int[] textBookIds = new int[]{
            R.id.text_fight_book,
            R.id.text_outer_flight_book,
            R.id.text_hotel_book,
    };

    private void initView() {
        DefinedGridView gridView = (DefinedGridView) homeFrgView.findViewById(R.id.home_gridView);
        ly_flight = (LinearLayout) homeFrgView.findViewById(R.id.ly_flight_content);
        ly_hotel = (LinearLayout) homeFrgView.findViewById(R.id.ly_hotel_content);
        homeFrgView.findViewById(R.id.city_hotel1).setOnClickListener(this);//选酒店城市
        ly_flight_hotel = (LinearLayout) homeFrgView.findViewById(R.id.ly_flight_hotel);
        radioGroup_reason = (RadioGroup) homeFrgView.findViewById(R.id.radioG_B_P);
        radioGroup_reason.check(R.id.nav_for_business);

        select_apply_order = (LinearLayout) homeFrgView.findViewById(R.id.select_apply_order);
        select_apply_order.setOnClickListener(this);
        //机票预定
        View btnFlightBook = homeFrgView.findViewById(R.id.btn_flight_book);
        btnFlightBook.setOnClickListener(this);
        //国际机票
        View btnOuterFlightBook = homeFrgView.findViewById(R.id.btn_outer_flight_book);
        btnOuterFlightBook.setOnClickListener(this);

        Fragment fragment = HomeInternationJourneyFragment.newInstance();
        getChildFragmentManager().
                beginTransaction().add(R.id.fl_content, fragment, HomeInternationJourneyFragment.TAG)
                .hide(fragment)
                .commit();

        //酒店预定
        View btnHotelBook = homeFrgView.findViewById(R.id.btn_hotel_book);
        btnHotelBook.setOnClickListener(this);
        //因公因私
        llPublicOrPrivate = homeFrgView.findViewById(R.id.ll_public_or_private);
        //预定审批
        llapplyOrder = homeFrgView.findViewById(R.id.select_apply_order);

        homeFrgView.findViewById(R.id.nav_for_business).setOnClickListener(this);
        homeFrgView.findViewById(R.id.nav_for_personal).setOnClickListener(this);
        homeFrgView.findViewById(R.id.city_hotel_latLng).setOnClickListener(this);//选酒店经纬度
        btn_search = (TextView) homeFrgView.findViewById(R.id.btn_search);
        btn_search.setOnClickListener(this);

        applyOrder = (TextView) homeFrgView.findViewById(R.id.apply_order);//机票预定申请单
        applyOrderId = (TextView) homeFrgView.findViewById(R.id.apply_order_id);//酒店预定申请单
        //出发、到达城市
        tv_city_from = (TextView) homeFrgView.findViewById(R.id.city_from);
        tv_city_from.setTextColor(Color.parseColor("#d3d3d3"));
        tv_city_from.setOnClickListener(this);
        tv_city_to = (TextView) homeFrgView.findViewById(R.id.city_to);
        tv_city_to.setTextColor(Color.parseColor("#d3d3d3"));
        tv_city_to.setOnClickListener(this);
        city_hotel1 = (TextView) homeFrgView.findViewById(R.id.city_hotel1);
        city_hotel1.setOnClickListener(this);

        time_start = (TextView) homeFrgView.findViewById(R.id.time_start);
        time_start.setText(DateUtils.getDate(0));
        time_start.setOnClickListener(this);
        homeFrgView.findViewById(R.id.home_hotel_date_layout).setOnClickListener(this);
        TextView startDateH = (TextView) homeFrgView.findViewById(R.id.start_date_H);
        TextView endDateH = (TextView) homeFrgView.findViewById(R.id.end_date_H);
        startDateH.setText(DateUtils.getDate(0));
        endDateH.setText(DateUtils.getDate(1));

        time_end = (TextView) homeFrgView.findViewById(R.id.time_end);
        time_end.setOnClickListener(this);
        homeFrgView.findViewById(R.id.btn_exchange).setOnClickListener(this);
        TextView noticeMoreBtn = (TextView) homeFrgView.findViewById(R.id.noticeMoreBtn);
        noticeMoreBtn.setOnClickListener(this);
        List<Map<String, String>> gridList = new ArrayList<>();
        String[] name = {"机票服务", "酒店服务", "车辆服务", "旅游服务"};
        String[] ids = {"icon_ticketservice", "icon_hotel", "icon_carservice", "icon_other"};
        for (int i = 0; i < name.length; i++) {
            Map<String, String> m = new HashMap<>();
            m.put("name", name[i]);
            m.put("ids", ids[i]);
            gridList.add(m);
        }
        HomeGridAdapter adapter = new HomeGridAdapter(getActivity(), gridList);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 2) {
                    //跳转滴滴
                    HashMap<String, String> map = ((MainActivity) getActivity()).getLocation();
                    map.put("maptype", "baidu");
                    Intent intent = new Intent(getActivity(), CarServiceActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("map", map);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    Intent intent = null;
                    if (position == 3) {
                        intent = new Intent(getActivity(), OtherServiceActivity.class);
                    } else if (position == 1) {
                        intent = new Intent(getActivity(), HotelServiceActivity.class);
                    } else if (position == 0) {
                        intent = new Intent(getActivity(), TicketServiceActivity.class);
                    }
                    startActivity(intent);
                }

            }
        });
        /**
         * 计算出banner可用高度
         */
        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        ly_flight_hotel.measure(width, height);
        int ly_flight_hotel_H = ly_flight_hotel.getMeasuredHeight();//ly_flight_hotel 的高度

        //计算gridView的高度
        int gridH = FontSizeUtils.dp2px(getActivity(), 60) + FontSizeUtils.sp2px(getActivity(), 12);
        int pic_height = getDeviceHeight() - MainActivity.radioGruop_H - gridH - ly_flight_hotel_H;

        //设置首页跑马灯文字背景透明度
        View home_text_ly = homeFrgView.findViewById(R.id.home_text_ly);
        home_text_ly.getBackground().setAlpha(100);//范围：0-255

        Banner banner = (Banner) homeFrgView.findViewById(R.id.home_banner);
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                if (position == 4) {
                    Intent intent = new Intent(getActivity(), QuestionActivity.class);
                    startActivity(intent);
                }
            }
        });
//        int sw = getDeviceWidth();
//        int height = (int)(sw * 0.54);
//        int banner_height = getDeviceHeight() - MainActivity.radioGruop_H - gridH;
//        banner_height = pic_height;
//        Log.i("--->","banner：高：" +";"+banner_height+";   "+getDeviceHeight()+"---"+MainActivity.radioGruop_H+"---"+gridH);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, pic_height);
//        banner.setLayoutParams(lp);
        List<Integer> list = new ArrayList<>();
        list.add(R.mipmap.banner_01);
        list.add(R.mipmap.banner_02);
        list.add(R.mipmap.banner_03);
        list.add(R.mipmap.banner_04);
        list.add(R.mipmap.banner_05);
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(list);
        //banner设置方法全部调用完毕时最后调用
        banner.start();

//        timeSelector = new TimeSelector(getActivity(), new TimeSelector.ResultHandler() {
//            @Override
//            public void handle(String time) {
//
//                String timeStr = time.substring(0,10);
//                    if(timeTypeIndex == 0){
//                        //设置出发时间
//                        time_start.setText(timeStr);
//                    }
//                    else if(timeTypeIndex == 1){
//                        //设置到达时间
//                        time_end.setText(timeStr);
//                    }
//                    else if(timeTypeIndex == 2){
//                        //设置入住时间
//                        /**判断当前选择的入住时间是否在离店时间之前，
//                         * 若不是，则设置离店时间为当前时间的第二天的日期
//                         * 否则，直接设置离店时间
//                         */
//                        TextView endDateH = (TextView) homeFrgView.findViewById(R.id.end_date_H);
//                        String leftDay = endDateH.getText().toString().trim();
//                        TextView startDateH = (TextView) homeFrgView.findViewById(R.id.start_date_H);
//                        boolean bool = DateUtils.checkinDayUsable(timeStr,leftDay);
//                        if(!bool){
//                            startDateH.setText(timeStr);
//                            String nextDay = DateUtils.generalNextDayOfOneDay(timeStr,1);
//                            endDateH.setText(nextDay);
//                        }else{
//                            startDateH.setText(timeStr);
//                        }
//
//
//                    }else if(timeTypeIndex == 3){
//                        //设置离店时间
//                        //判断离店时间是否在入住时间之前
//                        TextView startDateH = (TextView) homeFrgView.findViewById(R.id.start_date_H);
//                        String inDay = startDateH.getText().toString().trim();
//                        boolean bool = DateUtils.isFirstDayBeforeSecondDay(timeStr,inDay);
//                        if(bool){
//                            Toast.makeText(getActivity(),"请选择" + inDay + "之后的日期",Toast.LENGTH_SHORT).show();
//                        }else{
//                            TextView endDateH = (TextView) homeFrgView.findViewById(R.id.end_date_H);
//                            endDateH.setText(timeStr);
//                        }
//                    }
//            }
//        }, DateUtils.generalBeginDate(), DateUtils.generalEndDate());
//        timeSelector.setMode(TimeSelector.MODE.YMD);

        SlideTabBar slideTabBar = (SlideTabBar) homeFrgView.findViewById(R.id.frg_price_slide_bar);
        slideTabBar.setOnTabBarChangeListener(this);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null != data) {
            if (requestCode == 101) {
                //选择了预定申请单后的返回事件
//                Bundle bundle = data.getExtras();
//                BunbleParam bunbleParam = (BunbleParam) bundle.getSerializable("bunbleParam");
//                selectOrder = bunbleParam.getMap();
                selectOrder = (Map<String, Object>) (data.getSerializableExtra("bunbleParam"));
                String isHotelCity = null == selectOrder.get("isHotelCity") ? "0.0" : selectOrder.get("isHotelCity").toString();
                //切换酒店后，设置编码为使用城市三字码
//                useHotelCode = false;
                if (isHotelCity.equals("0.0")) {
                    hotelCityCode = selectOrder.get("toCityCode").toString();
                    useHotelCode = true;
                } else {
                    useHotelCode = false;
                }
                useHotelCode = isHotelCity.equals("0.0") ? true : false;
                if (selectOrder.get("travelType") != null && selectOrder.get("travelType").toString().contains("商务")) {
                    travelType = 2;
                } else {
                    travelType = 1;
                }
                applyOrder.setText(selectOrder.get("orderApplyId") + "");
                applyOrderId.setText(selectOrder.get("id") + "");
//                applyOrder.setText(selectOrder.get("orderApplyId")+"");
//                applyOrderId.setText(selectOrder.get("id")+"");
//                tv_city_from.setText(selectOrder.get("fromCity")+"");
//
//                tv_city_to.setText(selectOrder.get("toCity")+"");
//                time_start.setText(selectOrder.get("fromDate")+"");
                if (flightOrHotel == 1) {
//                    city_hotel1.setClickable(false);
                    if (selectOrder.get("checkInTime") == null || selectOrder.get("checkOutTime") == null) {
                        AlertUtil.show2(getActivity(), "该预订申请单不可用", "确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        });
                        applyOrder.setText("");
                        applyOrderId.setText("");
                        tv_city_from.setText("");
                        tv_city_to.setText("");
                        return;
                    } else {
                        city_hotel1.setText(selectOrder.get("toCity") + "");
                        TextView checkInTime = (TextView) homeFrgView.findViewById(R.id.start_date_H);
                        checkInTime.setText(selectOrder.get("checkInTime") + "");
                        TextView checkOutTime = (TextView) homeFrgView.findViewById(R.id.end_date_H);
                        checkOutTime.setText(selectOrder.get("checkOutTime") + "");
                    }
                    resetData();
                } else {
                    //
                    tv_city_from.setText(selectOrder.get("fromCity") + "");
                    tv_city_to.setText(selectOrder.get("toCity") + "");
                    time_start.setText(selectOrder.get("fromDate") + "");
                }

            } else if (requestCode == 3001) {
                //选择了出发城市
                String cityCode = data.getStringExtra("code");
                String cityName = data.getStringExtra("name");
                FlightItem flightItem = new FlightItem();
                flightItem.setFromCity(cityName);
                flightItem.setFromCityCode(cityCode);
                tv_city_from.setTag(flightItem);
                tv_city_from.setText(cityName);
                int type = data.getIntExtra("type", 0);//0 国内城市  1  国际城市
                if (type == 1) {
                    btn_search.setText("填写方案");
                } else {
                    btn_search.setText("查询");
                }
            } else if (requestCode == 3002) {

                String cityCode = data.getStringExtra("code");
                String cityName = data.getStringExtra("name");
                FlightItem flightItem = new FlightItem();
                flightItem.setToCity(cityName);
                flightItem.setToCityCode(cityCode);
                tv_city_to.setTag(flightItem);
                tv_city_to.setText(cityName);
                //选择了到达城市
                tv_city_to.setText(data.getStringExtra("name"));
                int type = data.getIntExtra("type", 0);//0 国内城市  1  国际城市
                if (type == 1) {
                    btn_search.setText("填写方案");
                } else {
                    btn_search.setText("查询");
                }
            } else if (requestCode == 3003) {
                //选择了酒店目的地
                hotelCityName = data.getStringExtra("name");
                if (selectOrder != null) {
                    selectOrder.put("toCity", hotelCityName);
                }
                hotelCityCode = data.getStringExtra("code");
                city_hotel1.setText(data.getStringExtra("name"));
                int type = data.getIntExtra("type", 0);//0 国内城市  1  国际城市
//                useHotelCode = true;
                //切换酒店后，设置编码为使用酒店编码
                useHotelCode = true;
                resetData();
            } else if (requestCode == 1010) {
                //因公机票预定成功后返回
                resetFlightData();
            } else if (requestCode == 1020) {
                //因公酒店预定成功后返回
                resetFlightData();
            } else if (requestCode == 1001) {
                //选择航班出行日期
                String firstDay = data.getStringExtra("firstDay");
                TextView dateTv = (TextView) homeFrgView.findViewById(R.id.time_start);
                dateTv.setText(firstDay);
            } else if (requestCode == 1002) {
                //选择酒店的入住和离店日期后的返回
                String firstDay = data.getStringExtra("firstDay");
                String secondDay = data.getStringExtra("secondDay");
                TextView checkInTv = (TextView) homeFrgView.findViewById(R.id.start_date_H);
                TextView checkOutTv = (TextView) homeFrgView.findViewById(R.id.end_date_H);
                checkInTv.setText(firstDay);
                checkOutTv.setText(secondDay);
            } else if (requestCode == 1019) {
                TextView latLngTv = (TextView) homeFrgView.findViewById(R.id.city_hotel_latLng);
                hotelAddress = data.getStringExtra("address");
                double initLat = data.getDoubleExtra("lat", 0);
                double initLng = data.getDoubleExtra("lng", 0);
                boolean changeLngLat = data.getBooleanExtra("change", false);
//                if(changeLngLat){
//                    if(initLat != 0 || initLng != 0){
//                        double[] latLngAry = MapUtil.bd_gdecrypt(initLat,initLng);
//                        lat = latLngAry[0];
//                        lng = latLngAry[1];
//                    }
//                }else{
                lat = initLat;
                lng = initLng;

//                }
                latLngTv.setText(hotelAddress);
            }

            if (2 == flightOrHotel) {
                btn_search.setText("填写方案");
            } else {
                btn_search.setText("查询");
            }
        }
    }

    private void resetFlightData() {
        applyOrder.setText("");
        applyOrderId.setText("");
        tv_city_from.setText("");
        tv_city_to.setText("");
    }

    @Override
    public void onResume() {
        super.onResume();
        lazyLoadData();
//        new Thread(new MyThread()).start();
    }

    @Override
    protected void initData() {
        super.initData();
        getNowTripData();//获取当前行程信息
        getNoticeList();
    }

    /**
     * 获取当前公告
     */
    private void getNoticeList() {

        Map<String, Object> params = new HashMap<>();
        RetrofitFactory.getInstance().createApi(NoticeApi.class).getNoticeList()
                .enqueue(new CallBack<List<GetNotice>>() {
                    @Override
                    public void success(List<GetNotice> response) {
                        boolean hasData = false;
                        if (response.size() > 0) {
                            Integer size = response.size() > 3 ? 3 : response.size();
                            textArrays = new String[size];
                            for (int i = 0; i < response.size(); i++) {
                                textArrays[i] = response.get(i).getNOTICE_TITLE();
                                if (i == 2) {
                                    break;
                                }
                            }
                            hasData = true;
                        } else {
                            textArrays = new String[]{"暂无公告"};
                            hasData = false;
                        }


                        marqueeTv = (MarqueeDownTextView) homeFrgView.findViewById(R.id.notice_text_marquee);

                        marqueeTv.setTextArraysAndClickListener(textArrays, new MarqueeTextViewClickListener() {
                            @Override
                            public void onClick(View view) {
                                String text = ((TextView) view).getText().toString();
                                if (text.equals("暂无公告")) return;
                                if (textArrays.length <= 0) return;
                                for (int i = 0; i < response.size(); i++) {
                                    GetNotice tmpNotice = response.get(i);
                                    if (tmpNotice.getNOTICE_TITLE().equals(text) && textArrays[i].equals(text)) {
                                        Intent it = new Intent(getActivity(), ShowViewActivity.class);
                                        it.putExtra("url", tmpNotice.getURL());
                                        startActivity(it);
                                    }
                                }
                            }
                        });
                        if (!hasData) {
                            marqueeTv.stop();
                        }
                    }

                    @Override
                    public void fail(String code) {
                        textArrays = new String[]{"暂无公告"};
                        marqueeTv = (MarqueeDownTextView) homeFrgView.findViewById(R.id.notice_text_marquee);

                        marqueeTv.setTextArraysAndClickListener(textArrays, new MarqueeTextViewClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        });
                        marqueeTv.stop();
                    }
                });
    }

    /**
     * 获取当前行程
     */
    private void getNowTripData() {
        Map<String, Object> params = new HashMap<>();
        params.put("ciphertext", "test");
        params.put("employeeCode", AppUtils.getUserInfo(getActivity()).getEmployeeCode());
        params.put("loginType", Constant.APP_TYPE);
        params.put("timeSlot", 5);//0：一个月内，1：三个月内，2：半年内，3：一年内，4：三年内，5：全部数据 ,
        params.put("type", 1);//0：全部，1：国内，2：国际，3：酒店

//        showLoading();
        RetrofitFactory.getInstance().createApi(OrderApi.class).getScheduleData(params)
                .enqueue(new CallBack<List<GetScheduleData>>() {
                    @Override
                    public void success(List<GetScheduleData> getScheduleDataList) {
//                        dismissLoading();
                        if (getScheduleDataList.size() > 0) {
                            GetScheduleData getScheduleData = getScheduleDataList.get(getScheduleDataList.size() - 1);
                            //当前航班:FM123 广州--上海  起飞时间:2018-01-08 11:30:00 落地时间:2018-01-08 13:25:00  飞行时间:1h40m
                            String flightNo = getScheduleData.getFlightNo();
                            String fromCity = getScheduleData.getFromCity();
                            String toCity = getScheduleData.getToCity();
                            String backDate = getScheduleData.getBackDate();
                            String fromDate = getScheduleData.getFromDate();
                            String timeStr = DateUtils.differTimeWithDays(backDate, fromDate);
                            String startTime = fromDate.length() > 3 ? fromDate.substring(0, fromDate.length() - 3) : "";
                            String endTime = backDate.length() > 3 ? backDate.substring(0, backDate.length() - 3) : "";
                            StringBuilder sb = new StringBuilder();
                            sb.append("当前航班:").append(flightNo)
                                    .append("  ")
                                    .append(fromCity).append("--").append(toCity)
                                    .append("  起飞时间：").append(startTime)
                                    .append("  落地时间：").append(endTime)
                                    .append("  飞行时间：").append(timeStr);
                            String text = sb.toString();
                            MarqueeTextView marqueeTextView = (MarqueeTextView) homeFrgView.findViewById(R.id.home_text_marquee);
                            marqueeTextView.setText(text);
                        }

                    }

                    @Override
                    public void fail(String code) {
//                        dismissLoading();
                    }
                });
    }

    public int getDeviceHeight() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int h_screen = dm.heightPixels;
        return h_screen;
    }

    @Override
    public void onTabBarChange(int position) {
//        Log.i("----------<","position: " + position);
        if (position == 0) {
            time_end.setVisibility(View.INVISIBLE);
        } else {
            time_end.setVisibility(View.VISIBLE);
        }
    }


    public void onTabClick(int pos) {
        for (int index = 0; index < btnBookIds.length; index++) {
            View btnBookView = homeFrgView.findViewById(btnBookIds[index]);
            TextView textBookView = (TextView) homeFrgView.findViewById(textBookIds[index]);
            if (pos == index) {
                textBookView.setTextColor(ContextCompat.getColor(mContext, R.color.CC70019));
                btnBookView.setBackgroundResource(R.drawable.tab_type_bg_checked);
            } else {
                textBookView.setTextColor(ContextCompat.getColor(mContext, R.color.C999999));
                btnBookView.setBackgroundResource(R.drawable.tab_type_bg_unchecked);
            }
        }
        switch (pos) {
            //国内机票
            case 0:
                ly_flight.setVisibility(View.VISIBLE);
                ly_hotel.setVisibility(View.GONE);
                if (flightOrHotel != 0) {
                    applyOrder.setText("");
                    applyOrderId.setText("");
                    tv_city_from.setText("");
                    tv_city_to.setText("");
                    selectOrder = null;
                    ((TextView) homeFrgView.findViewById(R.id.city_hotel1)).setText("");
                }
                flightOrHotel = 0;
                //切换到机票tab时，设置酒店使用编码标志为使用城市三字码
                useHotelCode = false;
                llapplyOrder.setVisibility(View.VISIBLE);
                llPublicOrPrivate.setVisibility(View.VISIBLE);
                isShowInternationFlightFragment(false);
                break;
            //国际机票
            case 1:
                if (flightOrHotel != 2) {
                    //清除国内机票和酒店相关数据
                    applyOrder.setText("");
                    applyOrderId.setText("");
                    tv_city_from.setText("");
                    tv_city_to.setText("");
                    tv_city_from.setTag(null);
                    tv_city_to.setTag(null);
                    ((TextView) homeFrgView.findViewById(R.id.city_hotel1)).setText("");
                    selectOrder = null;
                }
                flightOrHotel = 2;
                //切换到机票tab时，设置酒店使用编码标志为使用城市三字码
                useHotelCode = false;
                llapplyOrder.setVisibility(View.GONE);
                llPublicOrPrivate.setVisibility(View.GONE);
                ly_flight.setVisibility(View.GONE);
                ly_hotel.setVisibility(View.GONE);
                isShowInternationFlightFragment(true);
                break;
            //酒店
            case 2:
                ly_flight.setVisibility(View.GONE);
                ly_hotel.setVisibility(View.VISIBLE);
                if (flightOrHotel != 1) {
                    applyOrder.setText("");
                    applyOrderId.setText("");
                    tv_city_from.setText("");
                    tv_city_to.setText("");
                    tv_city_from.setTag(null);
                    tv_city_to.setTag(null);
                    ((TextView) homeFrgView.findViewById(R.id.city_hotel1)).setText("");
                    selectOrder = null;
                }
                flightOrHotel = 1;
                llapplyOrder.setVisibility(View.VISIBLE);
                llPublicOrPrivate.setVisibility(View.VISIBLE);
                isShowInternationFlightFragment(false);
                break;
        }
    }

    private void isShowInternationFlightFragment(boolean isShow) {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        Fragment fragment = fm.findFragmentByTag(HomeInternationJourneyFragment.TAG);
        if (isShow) {
            fragmentTransaction.show(fragment).commitNow();
        } else {
            fragmentTransaction.hide(fragment).commitNow();
        }
    }


    @Override
    public void onClick(View v) {
        Intent intentCity = new Intent(getActivity(), AirportActivity.class);
        switch (v.getId()) {
            case R.id.btn_flight_book:
                onTabClick(0);
                break;
            case R.id.btn_outer_flight_book:
                onTabClick(1);
                break;
            case R.id.btn_hotel_book:
                onTabClick(2);
                break;
            case R.id.nav_for_business:
//                select_adult_child.setVisibility(View.GONE);
//                select_person.setVisibility(View.VISIBLE);
                select_apply_order.setVisibility(View.VISIBLE);
                radioGroup_reason.check(R.id.nav_for_business);
                reason = 1;
                setFlightCityTvStatus(true);
                break;
            case R.id.nav_for_personal:
//                select_person.setVisibility(View.GONE);
                select_apply_order.setVisibility(View.GONE);
//                select_adult_child.setVisibility(View.VISIBLE);
                radioGroup_reason.check(R.id.nav_for_personal);
                reason = 0;
                setFlightCityTvStatus(false);
                break;
            case R.id.select_apply_order:
                //选择预定申请
                User user = new AppUtils().getUserInfo(getActivity());
                String userCode = user.getEmployeeCode();//查询预定申请单列表数据，查询当前登录用户的数据
                Intent intent = new Intent(getActivity(), SelectOrderActivity.class);
//                intent.putExtra("employeeCode",selectPerson.get("employeeCode")+"");
                intent.putExtra("employeeCode", userCode);
                intent.putExtra("orderApplyId", applyOrder.getText().toString());
                intent.putExtra("id", applyOrderId.getText().toString());

                if (flightOrHotel == 0) {
                    intent.putExtra("type", 1);
                } else {
                    intent.putExtra("type", 3);
                }
                startActivityForResult(intent, 101);
                break;
            case R.id.city_from:
                //因私出行，不跳转选城市
                if (reason == 1) {
                    if (!"".equals(applyOrder.getText().toString()) && reason == 1) {
                        break;
                    }
                    intentCity.putExtra("inCity", "yes");
                    startActivityForResult(intentCity, 3001);
                }
                break;
            case R.id.city_to:
                //因私出行，不跳转选城市
                if (reason == 1) {
                    if (!"".equals(applyOrder.getText().toString()) && reason == 1) {
                        break;
                    }
                    intentCity.putExtra("inCity", "yes");
                    startActivityForResult(intentCity, 3002);
                }
                break;
            case R.id.city_hotel1:
                intentCity.putExtra("dataType", 1);
                startActivityForResult(intentCity, 3003);
                break;
            case R.id.btn_search:
                switch (flightOrHotel) {
                    //国内机票
                    case 0:
                        handleInnearFlightSearch();
                        break;
                    //酒店
                    case 1:
                        handleHotelSearch();
                        break;
                    //国际机票
                    case 2:
                        handleOutterFlightSearch();
                        break;
                }
                break;
            case R.id.time_end:
                if (!"".equals(applyOrder.getText().toString())) {
                    break;
                }
//                timeTypeIndex = 1;
//                timeSelector.show();
                break;
            case R.id.time_start:
//                if(!"".equals(applyOrder.getText().toString())){
//                    break;
//                }
                //选择机票出行的出行日期
//                timeTypeIndex = 0;
//                timeSelector.show();

                intent = new Intent(getActivity(), CalendarActivity.class);
                intent.putExtra("selectType", 1);//2选择酒店日期
                intent.putExtra("days", 1);//需要选择多个日期
                intent.putExtra("firstDay", ((TextView) v).getText().toString().trim());
                startActivityForResult(intent, 1001);

                break;
            case R.id.home_hotel_date_layout:
                intent = new Intent(getActivity(), CalendarActivity.class);

                String checkInDate = ((TextView) homeFrgView.findViewById(R.id.start_date_H)).getText().toString().trim();
                String checkOutDate = ((TextView) homeFrgView.findViewById(R.id.end_date_H)).getText().toString().trim();
                intent.putExtra("selectType", 2);//2选择酒店日期
                intent.putExtra("days", 2);//需要选择多个日期
                intent.putExtra("firstDay", checkInDate);
                intent.putExtra("secondDay", checkOutDate);
                startActivityForResult(intent, 1002);
                break;
            case R.id.start_date_H:
                //选择入住酒店的入住日期
//                if(!"".equals(applyOrder.getText().toString())&&reason==1){
//                    break;
//                }
//                timeTypeIndex = 2;
//                timeSelector.show();
                break;
            case R.id.end_date_H:
//                if(!"".equals(applyOrder.getText().toString())&&reason==1){
//                    break;
//                }
                //选择入住酒店的离店日期
//                timeTypeIndex = 3;
//                timeSelector.show();
                break;
            case R.id.btn_exchange:
                //因公出行才可以切换城市，否则不切换
                if (reason == 1) {
                    //如果有选择预定申请单，则不可切换城市
                    if (!"".equals(applyOrder.getText().toString())) {
                        break;
                    }
                    String tempFrom = tv_city_from.getText().toString();
                    FlightItem formTag = (FlightItem) tv_city_from.getTag();
                    FlightItem flightItemFrom = new FlightItem();
                    flightItemFrom.setToCity(formTag.getFromCity());
                    flightItemFrom.setToCityCode(formTag.getFromCityCode());

                    String tempTo = tv_city_to.getText().toString();
                    FlightItem toTag = (FlightItem) tv_city_to.getTag();
                    FlightItem flightItemTo = new FlightItem();
                    flightItemTo.setFromCity(toTag.getToCity());
                    flightItemTo.setFromCityCode(toTag.getToCityCode());

                    tv_city_to.setTag(flightItemFrom);
                    tv_city_to.setText(tempFrom);
                    tv_city_from.setTag(flightItemTo);
                    tv_city_from.setText(tempTo);


                }
                break;
            case R.id.city_hotel_latLng:
                TextView hotelCityTv = (TextView) homeFrgView.findViewById(R.id.city_hotel1);
                String hotelCityStr = hotelCityTv.getText().toString().trim();
                if (hotelCityStr.equals("")) {
                    ToastUtil.toastNeedData(getActivity(), "请先选择目的地城市~");
                } else {
                    Intent intent1 = new Intent(getActivity(), PoiActivity.class);
                    String linkCode = useHotelCode ? hotelCityCode : selectOrder.get("toCityCode").toString();
                    if (useHotelCode) {
                        intent1.putExtra("style", 1);
                    } else {
                        intent1.putExtra("style", 2);
                    }
                    intent1.putExtra("cityId", linkCode);
                    intent1.putExtra("cityName", hotelCityStr);
                    startActivityForResult(intent1, 1019);
                }
                break;
            case R.id.noticeMoreBtn:
                intent = new Intent(getActivity(), NoticeListActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 因公、因私切换时，设置机票城市的背景
     *
     * @param usable
     */
    private void setFlightCityTvStatus(boolean usable) {
        //显示直接点击搜索按钮进行跳转
        TextView searchDirectTextTv = (TextView) homeFrgView.findViewById(R.id.search_direct_txt);
        TextView startTimeTv = (TextView) homeFrgView.findViewById(R.id.time_start);
        if (usable) {
//            tv_city_from.setTextColor(Color.parseColor("#333333"));
//            tv_city_to.setTextColor(Color.parseColor("#333333"));
            searchDirectTextTv.setVisibility(View.GONE);
            startTimeTv.setVisibility(View.VISIBLE);
        } else {
//            tv_city_from.setTextColor(Color.parseColor("#d3d3d3"));
//            tv_city_to.setTextColor(Color.parseColor("#d3d3d3"));
            searchDirectTextTv.setVisibility(View.VISIBLE);
            startTimeTv.setVisibility(View.GONE);
        }
    }

    private void resetData() {
        TextView latLngTv = (TextView) homeFrgView.findViewById(R.id.city_hotel_latLng);
        latLngTv.setText("");
        lat = 0;
        lng = 0;
    }

    private void handleOutterFlightSearch() {
//        Intent it = new Intent(getActivity(), ShowViewActivity.class);
//        String url = "";
//        it.putExtra("url", url);
//        it.putExtra("showMoreBtn", true);
//        //因公机票
//        startActivityForResult(it, 1010);

        HomeInternationJourneyFragment homeInternationJourneyFragment =
                (HomeInternationJourneyFragment) getChildFragmentManager().
                        findFragmentByTag(HomeInternationJourneyFragment.TAG);
        homeInternationJourneyFragment.queryInternationFlightJourney();

    }

    private void handleHotelSearch() {
        String applyOrderStr = applyOrder.getText().toString();
        Intent it = new Intent(getActivity(), PersonalTripActivity.class);
        it.putExtra("isHotel", true);//false机票 ，true 酒店
        it.putExtra("showMoreBtn", true);
        User loginUser = AppUtils.getUserInfo(getActivity());
        EditText hotel_keyword = (EditText) homeFrgView.findViewById(R.id.hotel_keyword);
        HashMap<String, String> location = ((MainActivity) getActivity()).getLocation();
        int codeType = useHotelCode ? 2 : 1;//1机场3字码，2酒店三字码
        switch (reason) {
            //因公
            case 1:
                if (TextUtils.isEmpty(applyOrderStr)) {
                    //用户没有选择预定申请单是，自动跳转到选择预定申请单页面
                    homeFrgView.findViewById(R.id.select_apply_order).performClick();
                    return;
                }
                //因公
                String bills = null == selectOrder.get("orderApplyId") ? "" : selectOrder.get("orderApplyId").toString();
                //酒店下单不需要担保时，目前考虑一次只能最多5个人同时下单；
                if (bills.split(",").length > 5) {
                    ToastUtil.toastNeedData(getActivity(), "最多只支持5人同时预定酒店~");
                    return;
                }
                TextView startDateH = (TextView) homeFrgView.findViewById(R.id.start_date_H);
                TextView endDateH = (TextView) homeFrgView.findViewById(R.id.end_date_H);
                it.putExtra("bills", bills);
                String employeeCodes = null == selectOrder.get("employeeCodes") ? "" : selectOrder.get("employeeCodes").toString();
                it.putExtra("employeeCodes", employeeCodes);
                String linkCode = useHotelCode ? hotelCityCode : selectOrder.get("toCityCode").toString();
                String url = Constant.ZHONGCHE_H5 + //"http://192.168.0.56:8081/travel_app-web/pages/phone/" +
                        "hotelList.html?" +
                        "reachCity=" + selectOrder.get("toCity") +
                        "&reachCityCode=" + linkCode +
                        "&startDate=" + startDateH.getText().toString().trim() +
                        "&endDate=" + endDateH.getText().toString().trim() +
                        "&tripType=1" +
                        "&billNo=" + bills +
                        "&price=" + selectOrder.get("price") +
                        "&userNo=" + loginUser.getEmployeeCode() +
                        "&employeeCodes=" + employeeCodes +//预定申请单里面的员工编号字符串，多个编号之间用逗号隔开
                        "&userName=" + loginUser.getEmployeeName() +
                        "&userTel=" + loginUser.getMobil() +
                        "&userEmail=" + loginUser.getMail() +
                        "&userId=" + loginUser.getIdCard() +
                        "&keyword=" + hotel_keyword.getText().toString() +
                        "&cptxt=string&codeType=" + codeType +
                        "&sex=" + loginUser.getGender();
                if ("0.0".equals(location.get("fromlng")) || "0.0".equals(location.get("fromlat"))) {
                    url = url +
                            "&lont=" + lng +
                            "&lat=" + lat;
                } else {
                    url = url +
                            "&lont=" + location.get("fromlng") +
                            "&lat=" + location.get("fromlat");
                }
                if (lat != 0 || lng != 0) {
                    url = url + "&plng=" + lng + "&plat=" + lat;
                    it.putExtra("plng", lng);
                    it.putExtra("plat", lat);
                    it.putExtra("hotelAddress", hotelAddress);
                }

                //因公酒店
                it.putExtra("dataType", 1);//1因公 ，2 因私
                it.putExtra("url", url);
                startActivityForResult(it, 1020);
                break;
            //因私
            case 0:
                if ("".equals(city_hotel1.getText().toString()) || "目的地酒店".equals(city_hotel1.getText().toString())) {
                    AlertUtil.show2(getActivity(), "请选择目的地", "确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    });
                    return;
                }

                startDateH = (TextView) homeFrgView.findViewById(R.id.start_date_H);
                endDateH = (TextView) homeFrgView.findViewById(R.id.end_date_H);
                url = Constant.ZHONGCHE_H5 + //"http://192.168.0.56:8081/travel_app-web/pages/phone/" +
                        "hotelList.html?" +
                        "reachCity=" + hotelCityName +
                        "&reachCityCode=" + hotelCityCode +
                        "&startDate=" + startDateH.getText().toString() +
                        "&endDate=" + endDateH.getText().toString() +
                        "&tripType=0" +
                        "&billNo=" +
                        "&price=" +
                        "&userName=" + loginUser.getEmployeeName() +
                        "&userNo=" + loginUser.getEmployeeCode() +
                        "&userTel=" + loginUser.getMobil() +
                        "&userEmail=" + loginUser.getMail() +
                        "&userId=" + loginUser.getIdCard() +
                        "&keyword=" + hotel_keyword.getText().toString() +
                        "&cptxt=string&codeType=" + codeType +
                        "&sex=" + loginUser.getGender();
                if ("0.0".equals(location.get("fromlng")) || "0.0".equals(location.get("fromlat"))) {
                    url = url +
                            "&lont=" + lng +
                            "&lat=" + lat;
                } else {
                    url = url +
                            "&lont=" + location.get("fromlng") +
                            "&lat=" + location.get("fromlat");
                }
                if (lat != 0 || lng != 0) {
                    url = url + "&plng=" + lng + "&plat=" + lat;
                    it.putExtra("plng", lng);
                    it.putExtra("plat", lat);
                    it.putExtra("hotelAddress", hotelAddress);
                }
                it.putExtra("url", url);
                it.putExtra("dataType", 2);//1因公 ，2 因私
                startActivityForResult(it, 1021);
                break;
        }

    }

    private void handleInnearFlightSearch() {
        //预定申请单号
        String applyOrderStr = applyOrder.getText().toString();
        switch (reason) {
            //因公
            case 1:
                Object fromCityObj = tv_city_from.getTag();
                Object toCityObj = tv_city_to.getTag();
                String flightFromCityName = "";
                String flightToCityName = "";
                String flightFromCityCode = "";
                String flightToCityCode = "";
                //单程
                //机票下单目前考虑一次只能最多9个人同时下单；
                if (!TextUtils.isEmpty(applyOrderStr) && applyOrderStr.split(",").length > 9) {
                    UsefulToast.showToast(mContext, "最多只支持9人同时预定机票~");
                    return;
                }
                //当申请单为空时,判断城市是否为空
                if (TextUtils.isEmpty(applyOrderStr)) {
                    if (fromCityObj instanceof FlightItem) {
                        flightFromCityName = ((FlightItem) fromCityObj).getFromCity();
                        flightFromCityCode = ((FlightItem) fromCityObj).getFromCityCode();
                    }
                    if (toCityObj instanceof FlightItem) {
                        flightToCityName = ((FlightItem) toCityObj).getToCity();
                        flightToCityCode = ((FlightItem) toCityObj).getToCityCode();
                    }
                    if (TextUtils.isEmpty(flightFromCityName) || TextUtils.isEmpty(flightToCityName)) {
                        UsefulToast.showToast(mContext, "请选择出发/到达城市");
                        return;
                    }
                }
                Intent it = new Intent(getActivity(), ShowViewActivity.class);
                it.putExtra("hasPay", true);
                it.putExtra("payType", "payMent");
                User loginUser = AppUtils.getUserInfo(getActivity());
                //因公出行
                String timeStart = time_start.getText().toString();
                if (timeStart.equals("")) {
                    AlertUtil.show2(getActivity(), "请选择出发时间", "确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    });
                    return;
                }
                int paytype = 0;
                String empCode = loginUser.getEmployeeCode();
                String empName = loginUser.getEmployeeName();
                String loginUserNo = loginUser.getEmployeeCode();
                String loginUserName = loginUser.getEmployeeName();
                String accountCode = "";
                if (selectOrder != null) {
                    flightFromCityName = null == selectOrder.get("fromCity") ? "" : selectOrder.get("fromCity").toString();
                    flightToCityName = null == selectOrder.get("toCity") ?
                            "" : selectOrder.get("toCity").toString();
                    flightFromCityCode = null == selectOrder.get("fromCityCode") ?
                            "" : selectOrder.get("fromCityCode").toString();
                    flightToCityCode = null == selectOrder.get("toCityCode") ?
                            "" : selectOrder.get("toCityCode").toString();
                    paytype = (null == selectOrder.get("paytype") || "".equals(selectOrder.get("paytype").toString())) ?
                            0 : Double.valueOf(selectOrder.get("paytype").toString()).intValue();
                    empCode = null == selectOrder.get("empCode") ? "" : selectOrder.get("empCode").toString();
                    empName = null == selectOrder.get("empName") ? "" : selectOrder.get("empName").toString();
                    accountCode = null == selectOrder.get("accountCode") ? "" : selectOrder.get("accountCode").toString();
                }
                String url = Constant.ZHONGCHE_H5 +
                        "oneWayFlightList.html?departCity=" + flightFromCityName +
                        "&departCityCode=" + flightFromCityCode +
                        "&reachCity=" + flightToCityName +
                        "&reachCityCode=" + flightToCityCode +
                        "&startDate=" + timeStart +
                        "&billNo=" + applyOrderStr +
                        "&tripType=0&cptxt=string&userNo=" + empCode +
                        "&userName=" + empName +
                        "&loginUserNo=" + loginUserNo +
                        "&loginUserName=" + loginUserName +
                        "&companyId=" + accountCode +
                        "&seatType=" + travelType +
                        "&payType=" + paytype;
                it.putExtra("url", url);
                it.putExtra("showMoreBtn", true);
                //因公机票
                startActivityForResult(it, 1010);
                break;
            //因私
            case 0:
                //因私机票
                Intent skipIntent = new Intent(getActivity(), ShowViewForNonBusinessActivity.class);
                skipIntent.putExtra("url", "ctrip");
                skipIntent.putExtra("loginType", 1);//1机票 ，2 酒店 3 携程订单
                skipIntent.putExtra("dataType", 2);//1因公 ，2 因私
                startActivity(skipIntent);
                break;
        }
    }

}
