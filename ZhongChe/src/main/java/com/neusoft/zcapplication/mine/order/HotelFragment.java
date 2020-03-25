package com.neusoft.zcapplication.mine.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.INaviInfoCallback;
import com.amap.api.navi.model.AMapNaviLocation;
import com.neusoft.zcapplication.Bean.User;
import com.neusoft.zcapplication.Constant.Constant;
import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.ShowViewActivity;
import com.neusoft.zcapplication.base.BaseFragment;
import com.neusoft.zcapplication.http.NetWorkRequest;
import com.neusoft.zcapplication.http.RequestCallback;
import com.neusoft.zcapplication.http.RequestUtil;
import com.neusoft.zcapplication.http.URL;
import com.neusoft.zcapplication.tools.AMapUtil;
import com.neusoft.zcapplication.tools.AlertUtil;
import com.neusoft.zcapplication.tools.AppUtils;
import com.neusoft.zcapplication.tools.DateUtils;
import com.neusoft.zcapplication.tools.ToastUtil;
import com.neusoft.zcapplication.widget.BottomDialog;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;


/**
 * 酒店
 *
 */

public class HotelFragment extends BaseFragment implements View.OnClickListener,RequestCallback,HotelOrderListAdapter.HotelItemClick{
    private List<Map<String,Object>> dataList;
    private HotelOrderListAdapter adapter;
    private BottomDialog bottomDialog;
    private int type = 0,timeSlot = 5;//查询订单参数，订单类型、时间
    private int sortType = 2;//排序方式，1-按时间升序，2-按时间降序
    private TextView tv_sort_type;
    private View frgView;
    private int hotelItemPosition = -1;// 更新酒店列表状态的下标
    public static HotelFragment getInstance(){
        HotelFragment fragment = new HotelFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(null == frgView){
            frgView = inflater.inflate(R.layout.frg_hotel_order_list, container, false);
            initView();

        }else{
            ViewGroup parent = (ViewGroup) frgView.getParent();
            if(parent != null){
                parent.removeView(frgView);
            }
        }
        return frgView;
    }

    private void initView(){
        tv_sort_type = (TextView)frgView.findViewById(R.id.tv_sort_type);
        frgView.findViewById(R.id.btn_filter).setOnClickListener(this);
        frgView.findViewById(R.id.btn_sort).setOnClickListener(this);
        dataList = new ArrayList<>();
        ListView listView  =  (ListView)frgView.findViewById(R.id.order_list);
        adapter = new HotelOrderListAdapter(getActivity(),dataList,2);
        adapter.setItemBtnClick(this);//设置item按钮点击事件
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String,Object> map = adapter.getItem(position);
                Intent intent = new Intent(getActivity(),HotelOrderDetailActivity.class);
                intent.putExtra("data",(Serializable) map);
                String stateName = null == map.get("stateName") ? "" : map.get("stateName").toString();
                intent.putExtra("stateName",stateName);
                startActivity(intent);
            }
        });

        bottomDialog = new BottomDialog(getActivity(),this,3);
    }

    @Override
    protected void initData() {
        super.initData();
        getHotelOrderList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_filter:
                bottomDialog.showBottomDialog();
                break;
            case R.id.btn_sort:
                if (sortType == 1){
                    tv_sort_type.setText("按日期升序");
                    sortType = 2;
                }
                else if (sortType == 2){
                    tv_sort_type.setText("按日期降序");
                    sortType = 1;
                }
                sortByDate();
                adapter.setList(dataList);
                adapter.notifyDataSetChanged();
                break;
            case R.id.radio_order0:
                type = 0;
                bottomDialog.checkItem(1,v.getId());
                break;
            case R.id.radio_order1:
                type = 1;
                bottomDialog.checkItem(1,v.getId());
                break;
            case R.id.radio_order2:
                type = 2;
                bottomDialog.checkItem(1,v.getId());
                break;
            case R.id.radio_order3:
                type = 3;
                bottomDialog.checkItem(1,v.getId());
                break;
            case R.id.radio_time0:
                timeSlot = 0;
                bottomDialog.checkItem(2,v.getId());
                break;
            case R.id.radio_time1:
                timeSlot = 1;
                bottomDialog.checkItem(2,v.getId());
                break;
            case R.id.radio_time2:
                timeSlot = 2;
                bottomDialog.checkItem(2,v.getId());
                break;
            case R.id.radio_time3:
                timeSlot = 3;
                bottomDialog.checkItem(2,v.getId());
                break;
            case R.id.radio_time4:
                timeSlot = 4;
                bottomDialog.checkItem(2,v.getId());
                break;
            case R.id.radio_time5:
                timeSlot = 5;
                bottomDialog.checkItem(2,v.getId());
                break;
            case R.id.btn_ok:
                bottomDialog.dismiss();
                getHotelOrderList();
                break;
            case R.id.btn_cancel:
                bottomDialog.dismiss();
                break;
        }
    }

    /**
     * 取消酒店订单按钮点击事件
     * @param position
     */
    @Override
    public void cancelHotel(int position) {
        hotelItemPosition = position;
        Map<String,Object> item = adapter.getItem(position);
        double supplierId = null == item.get("supplierId") ? 0.0 : (double)(item.get("supplierId"));
        int supplierIdInt = (int) supplierId;
        if(supplierIdInt == 4){
            //退订携程订单
            AlertUtil.show(getActivity(), "很抱歉，该酒店订单来自携程，请在【酒店服务】的【携程订单】栏目中进行退订。", "确定", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            },"取消", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            },"温馨提示");
        }else if(supplierIdInt == 2){
            //退订慧通订单
            AlertUtil.show(getActivity(), "慧通月结酒店，公司已做担保，不可退订~", "确定", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            },"取消", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            },"温馨提示");
        }else{
            String processPassword = null == item.get("processPassword") ? "" : item.get("processPassword").toString();
            Map<String,Object> params = new HashMap<>();
            params.put("ciphertext","test");
            params.put("employeeCode",new AppUtils().getUserInfo(getActivity()).getEmployeeCode());
            params.put("loginType", Constant.APP_TYPE);
            params.put("orderID",item.get("orderId"));//订单id
            params.put("style",supplierIdInt);//渠道id
            params.put("processPassword",processPassword);//订单密码 ,退订hrs的酒店必传
    //        Log.i("--->","酒店退订参数：" + params);
            Retrofit retrofit = new RequestUtil().getRequestClient(URL.ZHONGCHE_93);
            NetWorkRequest request = retrofit.create(NetWorkRequest.class);
            Call<Map<String,Object>> call = request.cancelHotelOrder(params);
            new RequestUtil().requestData(call,this,2,"正在提交数据...",false,getActivity());
        }
    }

    @Override
    public void hotelNav(int position) {
        hotelItemPosition = position;
        Map<String,Object> item = adapter.getItem(position);

        //出发地为当前位置
        AMapUtil mAMapUtil = AMapUtil.getInstance();
        //定位获取当前位置
        mAMapUtil.initLocation();
        mAMapUtil.setAMapLocationHandler(new AMapUtil.AMapLocationHandler() {
            @Override
            public void locateSuccess(double mLongitude, double mLatitude, String province, String city,
                                      String cityCode, String district, String address) {
                String addr = "";
//                double lat = 0.0;
//                double lng = 0.0;
                String hotelName =  item.get("hotelName") == null ? "酒店位置":item.get("hotelName") + "";
                String lat = item.get("latitude") == null ? "0.0":item.get("latitude") + "";
                String lng = item.get("longitude") == null ? "0.0":item.get("longitude") + "";
                //导航开始位置
                Poi startPoi = new Poi("我的位置", new LatLng(mLatitude,mLongitude), "");
                //导航结束位置
                Poi endPoi = new Poi(hotelName, new LatLng(Double.valueOf(lat), Double.valueOf(lng)), "");
                /**
                 * 启动导航
                 */
                AmapNaviPage.getInstance().showRouteActivity(getActivity(),
                        new AmapNaviParams(startPoi, null, endPoi, AmapNaviType.DRIVER),
                        new INaviInfoCallback() {
                            @Override
                            public void onInitNaviFailure() {
                                //导航初始化失败时的回调函数
                                ToastUtil.toast("导航初始化失败");
                            }

                            @Override
                            public void onGetNavigationText(String s) {
                                //导航播报信息回调函数

                            }

                            @Override
                            public void onLocationChange(AMapNaviLocation aMapNaviLocation) {
                                //当GPS位置有更新时的回调函数

                            }

                            @Override
                            public void onArriveDestination(boolean b) {
                                //到达目的地后回调函数

                            }

                            @Override
                            public void onStartNavi(int i) {
                                //启动导航后的回调函数

                            }

                            @Override
                            public void onCalculateRouteSuccess(int[] ints) {
                                //算路成功回调

                            }

                            @Override
                            public void onCalculateRouteFailure(int i) {
                                //步行或者驾车路径规划失败后的回调函数

                            }

                            @Override
                            public void onStopSpeaking() {
                                //停止语音回调，收到此回调后用户可以停止播放语音

                            }

                            @Override
                            public void onReCalculateRoute(int i) {

                            }

                            @Override
                            public void onExitPage(int i) {

                            }

                            @Override
                            public void onStrategyChanged(int i) {

                            }

                            @Override
                            public View getCustomNaviBottomView() {
                                return null;
                            }

                            @Override
                            public View getCustomNaviView() {
                                return null;
                            }

                            @Override
                            public void onArrivedWayPoint(int i) {

                            }
                        });
            }

            @Override
            public void locateFailed(String errorMessage) {
                ToastUtil.toast(errorMessage);
            }
        });
        mAMapUtil.startLocation();
    }

    /**
     * 获取酒店订单列表
     */
    private void getHotelOrderList() {
        Map<String,Object> params = new HashMap<>();
        User user = new AppUtils().getUserInfo(getActivity());
        params.put("ciphertext","test");
        params.put("employeeCode",user.getEmployeeCode());
        params.put("loginType", URL.LOGIN_TYPE);
        params.put("timeSlot",timeSlot);
        params.put("type",type);
        Retrofit retrofit = new RequestUtil().getRequestClient(URL.ZHONGCHE_93);
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        Call<Map<String,Object>> call = request.getHotelOrderList(params);
        new RequestUtil().requestData(call,this,1,getContext());
    }

    private void sortByDate(){
        Collections.sort(dataList,new SortByDate());
//        for (Map<String,Object> map: dataList){
//            Log.i("*******",map.get("fromTime")+"");
//        }
    }

    @Override
    public void requestSuccess(Object map, int type) {
        Map<String,Object> result = (Map<String, Object>) map;
        if(type == 1){
            if(null != result){
                String code = null == result.get("code") ? "" : result.get("code").toString();
                String codeMsg = null == result.get("codeMsg") ? "" : result.get("codeMsg").toString();
                List<Map<String,Object>> data = (ArrayList) result.get("data");
                if(code.equals("00000")) {
                    dataList = data;
                    sortByDate();
                    adapter.setList(dataList);
                    adapter.notifyDataSetChanged();
                }else {
                    if(null != getActivity()){
                        ToastUtil.toastError(getActivity());
                    }
                }
            }else{
                //请求失败
                if(null != getActivity()){
                    ToastUtil.toastError(getActivity());
                }
            }
        }else if(type  == 2){
            String code = null == result.get("code") ? "" : result.get("code").toString();
            String codeMsg = null == result.get("codeMsg") ? "" : result.get("codeMsg").toString();
            if(code.equals("00000")) {
                List<Map<String,Object>> hotelList = adapter.getList();
                for (int i = 0 ; i < hotelList.size(); i++){
                    if(i == hotelItemPosition){
                        hotelList.get(i).put("stateName","已退订");
                        adapter.notifyDataSetChanged();
                        break;
                    }
                }
                ToastUtil.toastNeedData(getActivity(),"退订成功！");
            }else{
                if(null != getActivity()){
                    ToastUtil.toastNeedData(getActivity(),"退订失败，" + codeMsg);
                }
            }
        }
    }

    @Override
    public void requestFail(int type) {
        if(null != getActivity()){
            ToastUtil.toastFail(getActivity());
        }
    }

    @Override
    public void requestCancel(int type) {

    }

    class SortByDate implements Comparator {
        public int compare(Object o1, Object o2) {
            Map<String,Object> m1 = (Map<String,Object>) o1;
            Map<String,Object> m2 = (Map<String,Object>) o2;

            String firstDay = (String)m1.get("fromTime");
            String secondDay = (String)m2.get("fromTime");
            if (firstDay == null && secondDay == null)
                return 0;
            if (firstDay == null)
                return -1;
            if (secondDay == null)
                return 1;
            if(firstDay.equals("")){
                return -1;
            }
            if(secondDay.equals("")){
                return 1;
            }
            //boolean result = DateUtils.isFirstDayBeforeSecondDay((String)m1.get("fromTime"),(String)m2.get("fromTime"));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar =  Calendar.getInstance();
            try {
                Date d1 = sdf.parse(firstDay);
                calendar.setTime(d1);
                long time1 = calendar.getTimeInMillis();
                Date d2 = sdf.parse(secondDay);
                calendar.setTime(d2);
                long time2 = calendar.getTimeInMillis();
                if(sortType == 1){
                    return Long.valueOf(time1).compareTo(Long.valueOf(time2));
                }else{
                    return Long.valueOf(time2).compareTo(Long.valueOf(time1));
                }
            }catch (ParseException e){
                if (sortType == 1) {
                    return 1;
                } else {
                    return -1;
                }
            }
            /*if(result) {
                if (sortType == 1) {
                    return -1;
                } else {
                    return 1;
                }
            } else {
                if (sortType == 1) {
                    return 1;
                } else {
                    return -1;
                }
            }*/
        }
    }
}
