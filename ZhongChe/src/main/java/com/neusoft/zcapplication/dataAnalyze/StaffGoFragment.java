package com.neusoft.zcapplication.dataAnalyze;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.utils.LogUtils;
import com.neusoft.zcapplication.Bean.User;
import com.neusoft.zcapplication.city.AirportActivity;
import com.neusoft.zcapplication.Constant.Constant;
import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.api.StaffGoApi;
import com.neusoft.zcapplication.base.BaseFragment;
import com.neusoft.zcapplication.calendarList.CalendarListActivity;
import com.neusoft.zcapplication.entity.SearchFlightTicketCity;
import com.neusoft.zcapplication.entity.StaffGoModel;
import com.neusoft.zcapplication.http.now.CallBack;
import com.neusoft.zcapplication.http.now.RetrofitFactory;
import com.neusoft.zcapplication.tools.AppUtils;
import com.neusoft.zcapplication.tools.DateUtils;
import com.neusoft.zcapplication.tools.SPAppUtil;
import com.neusoft.zcapplication.tools.StringUtil;
import com.neusoft.zcapplication.tools.ToastUtil;
import com.neusoft.zcapplication.widget.ClearIconEditText;
import com.neusoft.zcapplication.widget.CustomHeader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.util.ConvertUtils;

public class StaffGoFragment extends BaseFragment implements View.OnClickListener{
    private View fragmentView;
    private ArrayList<StaffGoModel> list = new ArrayList<>();
    private ListView staffGoListView;
    private StaffGoListAdapter adapter;

    private TextView tv_city_from,tv_city_to,tv_time_start,tv_time_end,tv_btn_search;
    ClearIconEditText tv_plan;
    private View tv_btn_exchange;

    private XRefreshView refreshView;

    private final int mPinnedTime = 1000;

    private int currentPage = 1;
    private boolean canLoadMore = true;
    private String toCityCode,fromCityCode;

    public StaffGoFragment() {

    }


    public static StaffGoFragment getInstance() {
        StaffGoFragment fragment = new StaffGoFragment();

        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(null == fragmentView){

            fragmentView = inflater.inflate(R.layout.fragment_staff_go, container, false);
            initView();
        }else{
            ViewGroup parent = (ViewGroup) fragmentView.getParent();
            if(parent != null){
                parent.removeView(fragmentView);
            }
        }
        return fragmentView;
    }

    protected void initView(){

        tv_city_from = (TextView) fragmentView.findViewById(R.id.city_from);
        tv_city_to = (TextView) fragmentView.findViewById(R.id.city_to);
        tv_time_start = (TextView) fragmentView.findViewById(R.id.time_start);
        tv_time_end = (TextView) fragmentView.findViewById(R.id.time_end);
        tv_btn_search = (TextView) fragmentView.findViewById(R.id.btn_search);
        tv_btn_exchange = (View) fragmentView.findViewById(R.id.btn_exchange);
        tv_plan = (ClearIconEditText) fragmentView.findViewById(R.id.tv_plan);

        tv_city_from.setOnClickListener(this);
        tv_city_to.setOnClickListener(this);
        tv_time_start.setOnClickListener(this);
        tv_time_end.setOnClickListener(this);
        tv_btn_search.setOnClickListener(this);
        tv_btn_exchange.setOnClickListener(this);
        fragmentView.findViewById(R.id.btn_reset).setOnClickListener(this);

        refreshView = (XRefreshView) fragmentView.findViewById(R.id.custom_view);
        refreshView.setPullRefreshEnable(true);
        // 设置是否可以上拉加载
        refreshView.setPullLoadEnable(true);
        refreshView.setCustomHeaderView(new CustomHeader(getActivity(),mPinnedTime));
//        refreshView.setCustomFooterView(new CustomFooterView(getActivity()));
        // 设置上次刷新的时间
        //当下拉刷新被禁用时，调用这个方法并传入false可以不让头部被下拉
        refreshView.setMoveHeadWhenDisablePullRefresh(true);
        // 设置时候可以自动刷新
        refreshView.setAutoRefresh(false);
        refreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh(boolean isPullDown) {
                getData();
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                loadMore();
            }

            @Override
            public void onRelease(float direction) {
                super.onRelease(direction);
                if (direction > 0) {
                } else {
                }
            }
        });
        refreshView.setOnAbsListViewScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                LogUtils.i("onScrollStateChanged");
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                LogUtils.i("onScroll");
            }
        });

        adapter = new StaffGoListAdapter(getActivity(),list);
        staffGoListView = (ListView) fragmentView.findViewById(R.id.staffGoListView);
        staffGoListView.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        super.initData();

    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.city_from:
                intent = new Intent(getActivity(), AirportActivity.class);
                intent.putExtra("inCity","yes");
                startActivityForResult(intent,3001);
                break;
            case R.id.city_to:
                intent = new Intent(getActivity(), AirportActivity.class);
                intent.putExtra("inCity","yes");
                startActivityForResult(intent,3002);
                break;
            case R.id.time_start:
                String checkIn = ((TextView)view).getText().toString();
//                intent = new Intent(getActivity(), CalendarActivity.class);
//                intent.putExtra("selectType",1);//1选择国际机票日期
//                intent.putExtra("days",1);
//                intent.putExtra("firstDay",checkIn);
//                startActivityForResult(intent,3003);
//                onYearMonthDayPicker(view);

                intent = new Intent(getActivity(), CalendarListActivity.class);

                String startTime = ((TextView)view).getText().toString().trim().length() > 0?((TextView)view).getText().toString().trim():DateUtils.getDate(0) ;
                intent.putExtra("firstDay",startTime);
                startActivityForResult(intent,3003);
                break;
            case R.id.time_end:
                String checkout = ((TextView)view).getText().toString();
//                intent = new Intent(getActivity(), CalendarActivity.class);
//                intent.putExtra("selectType",1);//1选择国际机票日期
//                intent.putExtra("days",1);
//                intent.putExtra("firstDay",checkout);
//                startActivityForResult(intent,3004);
//                onYearMonthDayPicker(view);
                String endTime = ((TextView)view).getText().toString().trim().length() > 0?((TextView)view).getText().toString().trim():DateUtils.getDate(0) ;
                intent = new Intent(getActivity(), CalendarListActivity.class);
                intent.putExtra("firstDay", endTime);
                startActivityForResult(intent,3004);
                break;
            case R.id.btn_search:
                String fromcityStr = tv_city_from.getText().toString();
                String toCityStr = tv_city_to.getText().toString();
                String startTimeStr = tv_time_start.getText().toString();
                String endTimeStr = tv_time_end.getText().toString();
                String flightStr = tv_plan.getText().toString();
                boolean allNull = StringUtil.isEmpty(fromcityStr) && StringUtil.isEmpty(toCityStr)
                                 && StringUtil.isEmpty(startTimeStr) && StringUtil.isEmpty(endTimeStr)
                                 && StringUtil.isEmpty(flightStr);

                if (allNull){
                    ToastUtil.toast("请至少输入一个查询条件");
                    return;
                }
                tv_plan.clearFocus();
                refreshView.startRefresh();
                break;
            case R.id.btn_exchange:
                TextView fromCityTv = (TextView)fragmentView.findViewById(R.id.city_from);
                TextView toCityTv = (TextView)fragmentView.findViewById(R.id.city_to);
                String fromCityName = fromCityTv.getText().toString();
                String toCityName = toCityTv.getText().toString();
                fromCityTv.setText(toCityName);
                toCityTv.setText(fromCityName);
                String tempCode = fromCityCode;
                fromCityCode  = toCityCode;
                toCityCode = tempCode;
                break;
            case R.id.btn_reset:
                tv_city_from.setText("");
                tv_city_to.setText("");
                tv_time_start.setText("");
                tv_time_end.setText("");
                EditText tv_plan = (EditText) fragmentView.findViewById(R.id.tv_plan);
                tv_plan.setText("");
                fromCityCode = "";
                toCityCode = "";
                list.clear();
                adapter.notifyDataSetChanged();
                tv_plan.clearFocus();
                ToastUtil.toast("已重置");
                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(null != data){
            if(requestCode == 3001){
                TextView fromCityTv = (TextView)fragmentView.findViewById(R.id.city_from);
                fromCityCode = data.getStringExtra("code").toString();
                fromCityTv.setText(data.getStringExtra("name").toString());
                SPAppUtil.setSearchFlightTicketStartCity(mContext,new SearchFlightTicketCity(
                        data.getStringExtra("code").toString(),
                        data.getStringExtra("name").toString()));
            }else if(requestCode == 3002){
                //到达城市
                TextView toCityTv = (TextView)fragmentView.findViewById(R.id.city_to);
                toCityCode = data.getStringExtra("code").toString();
                toCityTv.setText(data.getStringExtra("name").toString());
                SPAppUtil.setSearchFlightTicketEndCity(mContext,new SearchFlightTicketCity(
                        data.getStringExtra("code").toString(),
                        data.getStringExtra("name").toString()));
            }else if(requestCode == 3003){
                //选择时间的回调方法
                String firstDay = data.getStringExtra("firstDay");
                TextView timeTv = (TextView)fragmentView.findViewById(R.id.time_start);
                timeTv.setText(firstDay);
                SPAppUtil.setSearchFlightTicketTime(mContext,firstDay);
            }else if(requestCode == 3004){
                //选择时间的回调方法
                String firstDay = data.getStringExtra("firstDay");
                TextView timeTv = (TextView)fragmentView.findViewById(R.id.time_end);
                timeTv.setText(firstDay);
                SPAppUtil.setSearchFlightTicketTime(mContext,firstDay);
            }
        }
    }
    private void  getData(){
        currentPage = 1;
        canLoadMore = true;
        User user = AppUtils.getUserInfo(mContext);
        String startTimeStr = StringUtil.isEmpty(tv_time_start.getText().toString())?"":tv_time_start.getText().toString();
        String endTimeStr = StringUtil.isEmpty(tv_time_end.getText().toString())?"":tv_time_end.getText().toString();

        String flightStr = StringUtil.isEmpty(tv_plan.getText().toString())?"":tv_plan.getText().toString();
        Map<String,Object> params = new HashMap<>();
        params.put("ciphertext","test");
        params.put("loginType", Constant.APP_TYPE);
        params.put("employeeCode", user.getEmployeeCode());
        params.put("flightNo",flightStr);
        params.put("carrierName","");
        params.put("fromCityCode",fromCityCode);
        params.put("toCityCode",toCityCode);
        params.put("endDate",endTimeStr);
        params.put("startDate",startTimeStr);
        params.put("page",currentPage);
        params.put("size",10);
        RetrofitFactory.getInstance().createApi(StaffGoApi.class).getUnusedDomesticOrder(params)
                .enqueue(new CallBack<List<StaffGoModel>>() {
                    @Override
                    public void success(List<StaffGoModel> response) {
                        refreshView.stopRefresh();
                        list.clear();
                        list.addAll(response);
//                        if (list.size() == 0){
//                            ToastUtil.toast("暂无数据");
//                        }
                        adapter.setList(list);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void fail(String code) {
                        refreshView.stopRefresh();
                        list.clear();
                        adapter.setList(list);
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    private void  loadMore(){
        if (canLoadMore){
            ++currentPage;
        }
        User user = AppUtils.getUserInfo(mContext);
        String startTimeStr = StringUtil.isEmpty(tv_time_start.getText().toString())?"":tv_time_start.getText().toString();
        String endTimeStr = StringUtil.isEmpty(tv_time_end.getText().toString())?"":tv_time_end.getText().toString();

        String flightStr = StringUtil.isEmpty(tv_plan.getText().toString())?"":tv_plan.getText().toString();
        Map<String,Object> params = new HashMap<>();
        params.put("ciphertext","test");
        params.put("loginType", Constant.APP_TYPE);
        params.put("employeeCode", user.getEmployeeCode());
        params.put("flightNo",flightStr);
        params.put("carrierName","");
        params.put("fromCityCode",fromCityCode);
        params.put("toCityCode",toCityCode);
        params.put("endDate",endTimeStr);
        params.put("startDate",startTimeStr);
        params.put("page",currentPage);
        params.put("size",10);
        RetrofitFactory.getInstance().createApi(StaffGoApi.class).getUnusedDomesticOrder(params)
                .enqueue(new CallBack<List<StaffGoModel>>() {
                    @Override
                    public void success(List<StaffGoModel> response) {
                        refreshView.stopLoadMore();
                        if (response.size() == 0){
                            canLoadMore = false;
                        }else {
                            list.addAll(response);
                            canLoadMore = true;
                        }

                        adapter.setList(list);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void fail(String code) {
                        refreshView.stopLoadMore();

                    }
                });
    }

    public void onYearMonthDayPicker(View view) {
        final DatePicker picker = new DatePicker(getActivity());
        picker.setCanceledOnTouchOutside(true);
        picker.setUseWeight(true);
        picker.setTopPadding(ConvertUtils.toPx(getActivity(), 10));
        picker.setTextColor(getResources().getColor(R.color.colorPrimary),getResources().getColor(R.color.WeakDarkTextColor));
        picker.setResetWhileWheel(false);
        picker.setGravity(Gravity.CENTER);//弹框居中
        picker.setLabelTextColor(getResources().getColor(R.color.colorPrimary));
        picker.setDividerColor(getResources().getColor(R.color.colorPrimary));
        picker.setCancelTextColor(getResources().getColor(R.color.WeakDarkTextColor));
        picker.setSubmitTextColor(getResources().getColor(R.color.colorPrimary));
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                ((TextView)view).setText(year + "-" + month + "-" + day );
            }
        });
        picker.setOnWheelListener(new DatePicker.OnWheelListener() {
            @Override
            public void onYearWheeled(int index, String year) {
                picker.setTitleText(year + "-" + picker.getSelectedMonth() + "-" + picker.getSelectedDay());
            }

            @Override
            public void onMonthWheeled(int index, String month) {
                picker.setTitleText(picker.getSelectedYear() + "-" + month + "-" + picker.getSelectedDay());
            }

            @Override
            public void onDayWheeled(int index, String day) {
                picker.setTitleText(picker.getSelectedYear() + "-" + picker.getSelectedMonth() + "-" + day);
            }
        });
        picker.show();
    }

}
