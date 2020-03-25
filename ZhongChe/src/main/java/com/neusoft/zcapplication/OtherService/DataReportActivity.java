package com.neusoft.zcapplication.OtherService;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.neusoft.zcapplication.Bean.User;
import com.neusoft.zcapplication.Constant.Constant;
import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.api.OrderApi;
import com.neusoft.zcapplication.base.BaseActivity;
import com.neusoft.zcapplication.entity.GetPersonalData;
import com.neusoft.zcapplication.http.now.CallBack;
import com.neusoft.zcapplication.http.now.RetrofitFactory;
import com.neusoft.zcapplication.tools.AppUtils;
import com.neusoft.zcapplication.widget.SinglePickSelector;
import com.neusoft.zcapplication.widget.pieview.PieData;
import com.neusoft.zcapplication.widget.pieview.PieView;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: TenzLiu
 * Time: 2018/8/24 9:58
 * Desc: 数据报表
 */

public class DataReportActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_year,tv_quarter,tv_month;
    //饼状图
    private TextView tv_total_cost,tv_domestic_flight_ticket_order_count,tv_domestic_flight_ticket_total_cost,
            tv_domestic_flight_ticket_per_total_cost,tv_endorse_flight_ticket_order_count,
            tv_endorse_flight_ticket_total_cost,tv_endorse_flight_ticket_per_total_cost,
            tv_hotel_order_count,tv_hotel_total_cost,tv_hotel_per_total_cost;
    private LinearLayout ll_domestic_flight_ticket_cost,ll_international_flight_ticket_cost,
            ll_hotel_cost;
    //提前预定表
    private TextView tv_advance_domestic,tv_advance_international,tv_advance_hotel,
            tv_advance_rank_domestic,tv_advance_rank_international,tv_advance_rank_hotel;
    //机票改签表
    private TextView tv_endorse_ticket_total,tv_endorse_ticket_domestic,tv_endorse_ticket_international,
            tv_endorse_rank_per_total,tv_endorse_rank_per_domestic,tv_endorse_rank_per_international,
            tv_endorse_rank_total,tv_endorse_rank_domestic,tv_endorse_rank_international;
    //机票退订表
    private TextView tv_unsubscribe_ticket_total,tv_unsubscribe_ticket_domestic,tv_unsubscribe_ticket_international,
            tv_unsubscribe_rank_per_total,tv_unsubscribe_rank_per_domestic,tv_unsubscribe_rank_per_international,
            tv_unsubscribe_rank_total,tv_unsubscribe_rank_domestic,tv_unsubscribe_rank_international;
    //酒店退订表
    private TextView tv_unsubscribe_hotel_number,tv_unsubscribe_hotel_percent,tv_unsubscribe_hotel_rank;
    //月结酒店订购表
    private TextView tv_order_hotel_number,tv_order_hotel_percent,tv_order_hotel_rank;
    //违规订票表
    private TextView tv_breach_number_domestic,tv_breach_number_international,tv_breach_number_hotel,
            tv_breach_percent_domestic,tv_breach_percent_international,tv_breach_percent_hotel,
            tv_breach_rank_domestic,tv_breach_rank_international,tv_breach_rank_hotel;
    private PieView pv_cost;

    private ArrayList<PieData> mPieDataList;

    private SinglePickSelector yearSelector,quarterSelector,monthSelector;//选择器
    private List<String> yearDataList = new ArrayList<>();
    private List<String> quarterDataList = new ArrayList<>();
    private List<String> monthDataList = new ArrayList<>();

    private int year = -1;
    private int quarter = -1;
    private int month = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_report);
        initView();
        initData();
    }

    private void initView() {
        AppUtils.setStateBar(DataReportActivity.this,findViewById(R.id.frg_status_bar));
        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.ll_yeaer).setOnClickListener(this);
        findViewById(R.id.ll_quarter).setOnClickListener(this);
        findViewById(R.id.ll_month).setOnClickListener(this);
        pv_cost = (PieView) findViewById(R.id.pv_cost);
        ll_domestic_flight_ticket_cost = (LinearLayout) findViewById(R.id.ll_domestic_flight_ticket_cost);
        ll_international_flight_ticket_cost = (LinearLayout) findViewById(R.id.ll_international_flight_ticket_cost);
        ll_hotel_cost = (LinearLayout) findViewById(R.id.ll_hotel_cost);

        tv_year = (TextView) findViewById(R.id.tv_year);
        tv_quarter = (TextView) findViewById(R.id.tv_quarter);
        tv_month = (TextView) findViewById(R.id.tv_month);

        tv_total_cost = (TextView) findViewById(R.id.tv_total_cost);
        tv_domestic_flight_ticket_order_count = (TextView) findViewById(R.id.tv_domestic_flight_ticket_order_count);
        tv_domestic_flight_ticket_total_cost = (TextView) findViewById(R.id.tv_domestic_flight_ticket_total_cost);
        tv_domestic_flight_ticket_per_total_cost = (TextView) findViewById(R.id.tv_domestic_flight_ticket_per_total_cost);
        tv_endorse_flight_ticket_order_count = (TextView) findViewById(R.id.tv_endorse_flight_ticket_order_count);
        tv_endorse_flight_ticket_total_cost = (TextView) findViewById(R.id.tv_endorse_flight_ticket_total_cost);
        tv_endorse_flight_ticket_per_total_cost = (TextView) findViewById(R.id.tv_endorse_flight_ticket_per_total_cost);
        tv_hotel_order_count = (TextView) findViewById(R.id.tv_hotel_order_count);
        tv_hotel_total_cost = (TextView) findViewById(R.id.tv_hotel_total_cost);
        tv_hotel_per_total_cost = (TextView) findViewById(R.id.tv_hotel_per_total_cost);

        tv_advance_domestic = (TextView) findViewById(R.id.tv_advance_domestic);
        tv_advance_international= (TextView) findViewById(R.id.tv_advance_international);
        tv_advance_hotel = (TextView) findViewById(R.id.tv_advance_hotel);
        tv_advance_rank_domestic = (TextView) findViewById(R.id.tv_advance_rank_domestic);
        tv_advance_rank_international = (TextView) findViewById(R.id.tv_advance_rank_international);
        tv_advance_rank_hotel = (TextView) findViewById(R.id.tv_advance_rank_hotel);

        tv_endorse_ticket_total = (TextView) findViewById(R.id.tv_endorse_ticket_total);
        tv_endorse_ticket_domestic = (TextView) findViewById(R.id.tv_endorse_ticket_domestic);
        tv_endorse_ticket_international = (TextView) findViewById(R.id.tv_endorse_ticket_international);
        tv_endorse_rank_per_total = (TextView) findViewById(R.id.tv_endorse_rank_per_total);
        tv_endorse_rank_per_domestic = (TextView) findViewById(R.id.tv_endorse_rank_per_domestic);
        tv_endorse_rank_per_international = (TextView) findViewById(R.id.tv_endorse_rank_per_international);
        tv_endorse_rank_total = (TextView) findViewById(R.id.tv_endorse_rank_total);
        tv_endorse_rank_domestic = (TextView) findViewById(R.id.tv_endorse_rank_domestic);
        tv_endorse_rank_international = (TextView) findViewById(R.id.tv_endorse_rank_international);

        tv_unsubscribe_ticket_total = (TextView) findViewById(R.id.tv_unsubscribe_ticket_total);
        tv_unsubscribe_ticket_domestic = (TextView) findViewById(R.id.tv_unsubscribe_ticket_domestic);
        tv_unsubscribe_ticket_international = (TextView) findViewById(R.id.tv_unsubscribe_ticket_international);
        tv_unsubscribe_rank_per_total = (TextView) findViewById(R.id.tv_unsubscribe_rank_per_total);
        tv_unsubscribe_rank_per_domestic = (TextView) findViewById(R.id.tv_unsubscribe_rank_per_domestic);
        tv_unsubscribe_rank_per_international = (TextView) findViewById(R.id.tv_unsubscribe_rank_per_international);
        tv_unsubscribe_rank_total = (TextView) findViewById(R.id.tv_unsubscribe_rank_total);
        tv_unsubscribe_rank_domestic = (TextView) findViewById(R.id.tv_unsubscribe_rank_domestic);
        tv_unsubscribe_rank_international = (TextView) findViewById(R.id.tv_unsubscribe_rank_international);

        tv_unsubscribe_hotel_number = (TextView) findViewById(R.id.tv_unsubscribe_hotel_number);
        tv_unsubscribe_hotel_percent = (TextView) findViewById(R.id.tv_unsubscribe_hotel_percent);
        tv_unsubscribe_hotel_rank = (TextView) findViewById(R.id.tv_unsubscribe_hotel_rank);

        tv_order_hotel_number = (TextView) findViewById(R.id.tv_order_hotel_number);
        tv_order_hotel_percent = (TextView) findViewById(R.id.tv_order_hotel_percent);
        tv_order_hotel_rank = (TextView) findViewById(R.id.tv_order_hotel_rank);

        tv_breach_number_domestic = (TextView) findViewById(R.id.tv_breach_number_domestic);
        tv_breach_number_international = (TextView) findViewById(R.id.tv_breach_number_international);
        tv_breach_number_hotel = (TextView) findViewById(R.id.tv_breach_number_hotel);
        tv_breach_percent_domestic = (TextView) findViewById(R.id.tv_breach_percent_domestic);
        tv_breach_percent_international = (TextView) findViewById(R.id.tv_breach_percent_international);
        tv_breach_percent_hotel = (TextView) findViewById(R.id.tv_breach_percent_hotel);
        tv_breach_rank_domestic = (TextView) findViewById(R.id.tv_breach_rank_domestic);
        tv_breach_rank_international = (TextView) findViewById(R.id.tv_breach_rank_international);
        tv_breach_rank_hotel = (TextView) findViewById(R.id.tv_breach_rank_hotel);

        yearDataList.clear();
        quarterDataList.clear();
        Calendar c = Calendar.getInstance();
        int currentYear = c.get(Calendar.YEAR); // 获取当前年份
        int startYear = 1770;
        year = currentYear;
        yearDataList.add(currentYear+"年");
        for (int i=startYear; i<currentYear; i++){
            yearDataList.add(i+"年");
        }
        monthDataList.add("全部");
        for (int i=0; i<12; i++){
            monthDataList.add((i+1)+"月");
        }
        quarterDataList.add("全部");
        quarterDataList.add("第一季度");
        quarterDataList.add("第二季度");
        quarterDataList.add("第三季度");
        quarterDataList.add("第四季度");
        yearSelector = new SinglePickSelector(this, new SinglePickSelector.ResultHandler() {
            @Override
            public void handle(String data) {
                year = Integer.valueOf(data.replace("年",""));
                tv_year.setText(""+year);
                getPersonalData();
            }
        },yearDataList);
        quarterSelector = new SinglePickSelector(this, new SinglePickSelector.ResultHandler() {
            @Override
            public void handle(String data) {
                if("全部".equals(data)){
                    quarter = -1;
                    monthDataList.clear();
                    monthDataList.add("全部");
                    for(int i=1; i<=12; i++){
                        monthDataList.add(i+"月");
                    }
                }else if("第一季度".equals(data)){
                    quarter = 1;
                    monthDataList.clear();
                    monthDataList.add("全部");
                    for(int i=1; i<=3; i++){
                        monthDataList.add(i+"月");
                    }
                }else if("第二季度".equals(data)){
                    quarter = 2;
                    monthDataList.clear();
                    monthDataList.add("全部");
                    for(int i=4; i<=6; i++){
                        monthDataList.add(i+"月");
                    }
                }else if("第三季度".equals(data)){
                    quarter = 3;
                    monthDataList.clear();
                    monthDataList.add("全部");
                    for(int i=7; i<=9; i++){
                        monthDataList.add(i+"月");
                    }
                }else{
                    quarter = 4;
                    monthDataList.clear();
                    monthDataList.add("全部");
                    for(int i=10; i<=12; i++){
                        monthDataList.add(i+"月");
                    }
                }
                tv_quarter.setText(data);
                month = -1;
                tv_month.setText("全部");
                getPersonalData();
            }
        },quarterDataList);
        monthSelector = new SinglePickSelector(this, new SinglePickSelector.ResultHandler() {
            @Override
            public void handle(String data) {
                if("全部".equals(data)){
                    month = -1;
                    tv_month.setText("全部");
                }else{
                    month = Integer.valueOf(data.replace("月",""));
                    tv_month.setText(""+month);
                }
                getPersonalData();
            }
        },monthDataList);
    }

    private void initData() {
        mPieDataList = new ArrayList<>();
        getPersonalData();
    }

    /**
     * 获取证件列表
     */
    private void getPersonalData() {
        User user = AppUtils.getUserInfo(mContext);
        Map<String,Object> params = new HashMap<>();
        params.put("ciphertext","test");
        params.put("loginType", Constant.APP_TYPE);
        params.put("employeeCode", user.getEmployeeCode());
        params.put("year", "" + year);
        if(quarter != -1){
            params.put("quarterType", "" + quarter);
        }else{
            params.put("quarterType", "");
        }
        if(month != -1){
            if(month < 10){
                params.put("yearMonth", year + "-0" + month);
            }else{
                params.put("yearMonth", year + "-" + month);
            }
        }else{
            params.put("yearMonth", "");
        }
        showLoading();
        RetrofitFactory.getInstance().createApi(OrderApi.class).getPersonalData(params)
                .enqueue(new CallBack<GetPersonalData>() {
                    @Override
                    public void success(GetPersonalData getPersonalData) {
                        dismissLoading();
                        clearViewData();
                        setVIewData(getPersonalData);
                    }

                    @Override
                    public void fail(String code) {
                        dismissLoading();
                    }
                });
    }

    /**
     * 设置数据
     * @param getPersonalData
     */
    private void setVIewData(GetPersonalData getPersonalData) {
        //饼状图
        float totalCost = 0;
        float domesticCost = 0;
        int domesticTicketCount = 0;
        float internationalCost = 0;
        int internationalTicketCount = 0;
        float hotelCost = 0;
        int hotelTicketCount = 0;
        mPieDataList.clear();
        for(int i=0; i<getPersonalData.getCostList().size(); i++){
            GetPersonalData.CostListBean costListBean = getPersonalData.getCostList().get(i);
            if(costListBean.getResultType()==0){
                domesticCost = Math.abs(costListBean.getCostPirce());
                domesticTicketCount = costListBean.getTicketCount();
            }else if(costListBean.getResultType()==1){
                internationalCost = Math.abs(costListBean.getCostPirce());
                internationalTicketCount = costListBean.getTicketCount();
            }else if(costListBean.getResultType()==2){
                hotelCost = Math.abs(costListBean.getCostPirce());
                hotelTicketCount = costListBean.getTicketCount();
            }
            totalCost += Math.abs(costListBean.getCostPirce());
        }
        mPieDataList.add(new PieData("",domesticCost));
        mPieDataList.add(new PieData("",internationalCost));
        mPieDataList.add(new PieData("",hotelCost));
        pv_cost.setData(mPieDataList);
        if(domesticCost>0f){
            ll_domestic_flight_ticket_cost.setVisibility(View.VISIBLE);
        }else{
            ll_domestic_flight_ticket_cost.setVisibility(View.GONE);
        }
        if(internationalCost>0f){
            ll_international_flight_ticket_cost.setVisibility(View.VISIBLE);
        }else{
            ll_international_flight_ticket_cost.setVisibility(View.GONE);
        }
        if(hotelCost>0f){
            ll_hotel_cost.setVisibility(View.VISIBLE);
        }else{
            ll_hotel_cost.setVisibility(View.GONE);
        }
        tv_total_cost.setText("￥" + Html.fromHtml(parseNumber(""+new BigDecimal(totalCost).setScale(1,BigDecimal.ROUND_HALF_UP).floatValue())));
        if(totalCost==0){
            totalCost = 1;
        }
        tv_domestic_flight_ticket_order_count.setText("" + parseIntNumber("" + domesticTicketCount));
        tv_domestic_flight_ticket_total_cost.setText("" + parseNumber("" + domesticCost));
        tv_domestic_flight_ticket_per_total_cost.setText("" + new BigDecimal(domesticCost/totalCost*100).setScale(1,BigDecimal.ROUND_HALF_UP).floatValue() + "%");
        tv_endorse_flight_ticket_order_count.setText("" + parseIntNumber("" + internationalTicketCount));
        tv_endorse_flight_ticket_total_cost.setText("" + parseNumber("" + internationalCost));
        tv_endorse_flight_ticket_per_total_cost.setText("" + new BigDecimal(internationalCost/totalCost*100).setScale(1,BigDecimal.ROUND_HALF_UP).floatValue() + "%");
        tv_hotel_order_count.setText("" + parseIntNumber("" + hotelTicketCount));
        tv_hotel_total_cost.setText("" + parseNumber("" + hotelCost));
        tv_hotel_per_total_cost.setText("" + new BigDecimal(hotelCost/totalCost*100).setScale(1,BigDecimal.ROUND_HALF_UP).floatValue() + "%");
        //平均提前表
        for(int i=0; i<getPersonalData.getAverageDaysList().size(); i++){
            GetPersonalData.AverageDaysListBean averageDaysListBean = getPersonalData.getAverageDaysList().get(i);
            if(averageDaysListBean.getResultType()==0){
                tv_advance_domestic.setText("" + parseNumber(averageDaysListBean.getDays()));
                tv_advance_rank_domestic.setText("" + averageDaysListBean.getRanks());
            }else if(averageDaysListBean.getResultType()==1){
                tv_advance_international.setText("" + parseNumber(averageDaysListBean.getDays()));
                tv_advance_rank_international.setText("" + averageDaysListBean.getRanks());
            }else if(averageDaysListBean.getResultType()==2){
                tv_advance_hotel.setText("" + parseNumber(averageDaysListBean.getDays()));
                tv_advance_rank_hotel.setText("" + averageDaysListBean.getRanks());
            }
        }
        //机票改签表
        int endorseTicketTotal = 0;
        int endorseTicketDomestic = 0;
        int endorseTicketInternational = 0;
        int endorseTicketDomesticTotal = 0;
        int endorseTicketInternationalTotal = 0;
        int endorseTicketDomesticInternationalTotal = 0;
        for(int i=0; i<getPersonalData.getPlanChangeList().size(); i++){
            GetPersonalData.PlanChangeListBean planChangeListBean = getPersonalData.getPlanChangeList().get(i);
            if(planChangeListBean.getResultType()==0){
                endorseTicketDomesticTotal = planChangeListBean.getTotalCount();
                endorseTicketDomestic = planChangeListBean.getCount();
                tv_endorse_ticket_domestic.setText("" + endorseTicketDomestic);
                tv_endorse_rank_domestic.setText("" + planChangeListBean.getRanks());
            }else if(planChangeListBean.getResultType()==1){
                endorseTicketInternationalTotal = planChangeListBean.getTotalCount();
                endorseTicketInternational = planChangeListBean.getCount();
                tv_endorse_ticket_international.setText("" + endorseTicketInternational);
                tv_endorse_rank_international.setText("" + planChangeListBean.getRanks());
            }
            endorseTicketTotal += planChangeListBean.getCount();
        }
        endorseTicketDomesticInternationalTotal = endorseTicketDomesticTotal + endorseTicketInternationalTotal;
        tv_endorse_ticket_total.setText("" + endorseTicketTotal);
        if(endorseTicketDomesticInternationalTotal==0){
            endorseTicketDomesticInternationalTotal = 1;
        }
        tv_endorse_rank_per_total.setText("" +
                new BigDecimal(endorseTicketTotal/(float)endorseTicketDomesticInternationalTotal*100).setScale(1,BigDecimal.ROUND_HALF_UP).floatValue() + "%");
        //tv_endorse_rank_total.setText("");
        tv_endorse_rank_per_domestic.setText("" +
                new BigDecimal(endorseTicketDomestic/(float)endorseTicketDomesticInternationalTotal*100).setScale(1,BigDecimal.ROUND_HALF_UP).floatValue() + "%");
        tv_endorse_rank_per_international.setText("" +
                new BigDecimal(endorseTicketInternational/(float)endorseTicketDomesticInternationalTotal*100).setScale(1,BigDecimal.ROUND_HALF_UP).floatValue() + "%");
        //机票退订表
        int unsubscribeTicketTotal = 0;
        int unsubscribeTicketDomestic = 0;
        int unsubscribeTicketInternational = 0;
        int unsubscribeTicketDomesticInternationalTotal = 0;
        int unsubscribeTicketDomesticTotal = 0;
        int unsubscribeTicketInternationalTotal = 0;
        for(int i=0; i<getPersonalData.getPlaneReturnList().size(); i++){
            GetPersonalData.PlaneReturnListBean planeReturnListBean = getPersonalData.getPlaneReturnList().get(i);
            if(planeReturnListBean.getResultType()==0){
                unsubscribeTicketDomesticTotal = planeReturnListBean.getTotalCount();
                unsubscribeTicketDomestic = planeReturnListBean.getCount();
                tv_unsubscribe_ticket_domestic.setText("" + unsubscribeTicketDomestic);
                tv_unsubscribe_rank_domestic.setText("" + planeReturnListBean.getRanks());
            }else if(planeReturnListBean.getResultType()==1){
                unsubscribeTicketInternationalTotal = planeReturnListBean.getTotalCount();
                unsubscribeTicketInternational = planeReturnListBean.getCount();
                tv_unsubscribe_ticket_international.setText("" + unsubscribeTicketInternational);
                tv_unsubscribe_rank_international.setText("" + planeReturnListBean.getRanks());
            }
            unsubscribeTicketTotal += planeReturnListBean.getCount();
        }
        unsubscribeTicketDomesticInternationalTotal = unsubscribeTicketDomesticTotal + unsubscribeTicketInternationalTotal;
        tv_unsubscribe_ticket_total.setText("" + unsubscribeTicketTotal);
        if(unsubscribeTicketDomesticInternationalTotal==0){
            unsubscribeTicketDomesticInternationalTotal = 1;
        }
        tv_unsubscribe_rank_per_total.setText("" +
                new BigDecimal(unsubscribeTicketTotal/(float)unsubscribeTicketDomesticInternationalTotal*100).setScale(1,BigDecimal.ROUND_HALF_UP).floatValue() + "%");
        tv_unsubscribe_rank_per_domestic.setText("" +
                new BigDecimal(unsubscribeTicketDomestic/(float)unsubscribeTicketDomesticInternationalTotal*100).setScale(1,BigDecimal.ROUND_HALF_UP).floatValue() + "%");
        tv_unsubscribe_rank_per_international.setText("" +
                new BigDecimal(unsubscribeTicketInternational/(float)unsubscribeTicketDomesticInternationalTotal*100).setScale(1,BigDecimal.ROUND_HALF_UP).floatValue() + "%");
        //tv_unsubscribe_rank_total.setText("");
        //酒店退订表
        for(int i=0; i<getPersonalData.getHotelRetunList().size(); i++){
            GetPersonalData.HotelRetunListBean hotelRetunListBean = getPersonalData.getHotelRetunList().get(i);
            tv_unsubscribe_hotel_number.setText("" + hotelRetunListBean.getCount());
            tv_unsubscribe_hotel_percent.setText("" +
                    new BigDecimal(hotelRetunListBean.getCount()/(float)hotelRetunListBean.getTotalCount()*100).setScale(1,BigDecimal.ROUND_HALF_UP).floatValue() + "%");
            tv_unsubscribe_hotel_rank.setText("" + hotelRetunListBean.getRanks());
        }
        //月结酒店订购表
        for(int i=0; i<getPersonalData.getHotelMonthList().size(); i++){
            GetPersonalData.HotelMonthListBean hotelMonthListBean = getPersonalData.getHotelMonthList().get(i);
            tv_order_hotel_number.setText("" + hotelMonthListBean.getCount());
            tv_order_hotel_percent.setText("" +
                    new BigDecimal(hotelMonthListBean.getCount()/(float)hotelMonthListBean.getTotalCount()*100).setScale(1,BigDecimal.ROUND_HALF_UP).floatValue() + "%");
            tv_order_hotel_rank.setText("" + hotelMonthListBean.getRanks());
        }
        //违规订票
        int breachNumberTotal = 0;
        int breachNumberDomestic = 0;
        int breachNumberInternational = 0;
        int breachNumberHotel = 0;
        int breachNumberDomesticInternationalHotelTotal = 0;
        int breachNumberDomesticTotal = 0;
        int breachNumberInternationalTotal = 0;
        int breachNumberHotelTotal = 0;
        for(int i=0; i<getPersonalData.getViolationsList().size(); i++){
            GetPersonalData.ViolationsListBean violationsListBean = getPersonalData.getViolationsList().get(i);
            if(violationsListBean.getResultType()==0){
                breachNumberDomesticTotal = violationsListBean.getTotalCount();
                breachNumberDomestic = violationsListBean.getCount();
                tv_breach_number_domestic.setText("" + breachNumberDomestic);
                tv_breach_rank_domestic.setText("" + violationsListBean.getRanks());
            }else if(violationsListBean.getResultType()==1){
                breachNumberInternationalTotal = violationsListBean.getTotalCount();
                breachNumberInternational = violationsListBean.getCount();
                tv_breach_number_international.setText("" + breachNumberInternational);
                tv_breach_rank_international.setText("" + violationsListBean.getRanks());
            }else if(violationsListBean.getResultType()==2){
                breachNumberHotelTotal = violationsListBean.getTotalCount();
                breachNumberHotel = violationsListBean.getCount();
                tv_breach_number_hotel.setText("" + breachNumberHotel);
                tv_breach_rank_hotel.setText("" + violationsListBean.getRanks());
            }
            breachNumberTotal += violationsListBean.getCount();
        }
        breachNumberDomesticInternationalHotelTotal = breachNumberDomesticTotal + breachNumberInternationalTotal + breachNumberHotelTotal;
        if(breachNumberDomesticInternationalHotelTotal==0){
            breachNumberDomesticInternationalHotelTotal = 1;
        }
        tv_breach_percent_domestic.setText("" +
                new BigDecimal(breachNumberDomestic/(float)breachNumberDomesticInternationalHotelTotal*100).setScale(1,BigDecimal.ROUND_HALF_UP).floatValue() + "%");
        tv_breach_percent_international.setText("" +
                new BigDecimal(breachNumberInternational/(float)breachNumberDomesticInternationalHotelTotal*100).setScale(1,BigDecimal.ROUND_HALF_UP).floatValue() + "%");
        tv_breach_percent_hotel.setText("" +
                new BigDecimal(breachNumberHotel/(float)breachNumberDomesticInternationalHotelTotal*100).setScale(1,BigDecimal.ROUND_HALF_UP).floatValue() + "%");
        //机票（改签，退票）总计，总排名
        for(int i=0; i<getPersonalData.getTotalRanksList().size(); i++){
            GetPersonalData.TotalRanksListBean totalRanksListBean = getPersonalData.getTotalRanksList().get(i);
            if(totalRanksListBean.getResultType() == 3){
                tv_endorse_rank_total.setText("" + totalRanksListBean.getTotalRanks());
            }else if(totalRanksListBean.getResultType() == 4){
                tv_unsubscribe_rank_total.setText("" + totalRanksListBean.getTotalRanks());
            }
        }
    }

    /**
     * 重置数据
     */
    private void clearViewData(){
        ll_domestic_flight_ticket_cost.setVisibility(View.GONE);
        ll_international_flight_ticket_cost.setVisibility(View.GONE);
        ll_hotel_cost.setVisibility(View.GONE);
        //提前预定表
        tv_advance_domestic.setText("0");
        tv_advance_international.setText("0");
        tv_advance_hotel.setText("0");
        tv_advance_rank_domestic.setText("0");
        tv_advance_rank_international.setText("0");
        tv_advance_rank_hotel.setText("0");
        //机票改签表
        tv_endorse_ticket_total.setText("0");
        tv_endorse_ticket_domestic.setText("0");
        tv_endorse_ticket_international.setText("0");
        tv_endorse_rank_per_total.setText("0%");
        tv_endorse_rank_per_domestic.setText("0%");
        tv_endorse_rank_per_international.setText("0%");
        tv_endorse_rank_total.setText("0");
        tv_endorse_rank_domestic.setText("0");
        tv_endorse_rank_international.setText("0");
        //机票退订表
        tv_unsubscribe_ticket_total.setText("0");
        tv_unsubscribe_ticket_domestic.setText("0");
        tv_unsubscribe_ticket_international.setText("0");
        tv_unsubscribe_rank_per_total.setText("0%");
        tv_unsubscribe_rank_per_domestic.setText("0%");
        tv_unsubscribe_rank_per_international.setText("0%");
        tv_unsubscribe_rank_total.setText("0");
        tv_unsubscribe_rank_domestic.setText("0");
        tv_unsubscribe_rank_international.setText("0");
        //酒店退订表
        tv_unsubscribe_hotel_number.setText("0");
        tv_unsubscribe_hotel_percent.setText("0%");
        tv_unsubscribe_hotel_rank.setText("0");
        //月结酒店订购表
       tv_order_hotel_number.setText("0");
       tv_order_hotel_percent.setText("0%");
        tv_order_hotel_rank.setText("0");
        //违规订票表
        tv_breach_number_domestic.setText("0");
        tv_breach_number_international.setText("0");
        tv_breach_number_hotel.setText("0");
        tv_breach_percent_domestic.setText("0%");
        tv_breach_percent_international.setText("0%");
        tv_breach_percent_hotel.setText("0%");
        tv_breach_rank_domestic.setText("0");
        tv_breach_rank_international.setText("0");
        tv_breach_rank_hotel.setText("0");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.ll_yeaer:
                yearSelector.show();
                break;
            case R.id.ll_quarter:
                quarterSelector.show();
                break;
            case R.id.ll_month:
                monthSelector.show();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pv_cost = null;
    }

    /**
     * 千位符
     * @param number
     * @return
     */
    public static String parseNumber(String number) {
        DecimalFormat df = new DecimalFormat(",###,###.0");
        String format = df.format(new BigDecimal(number));
        if(".0".equals(format)){
            return "0.0";
        }else{
            return format;
        }
    }

    /**
     * 千位符
     * @param number
     * @return
     */
    public static String parseIntNumber(String number) {
        DecimalFormat df = new DecimalFormat(",###,###");
        String format = df.format(new BigDecimal(number));
        if("".equals(format)){
            return "0";
        }else{
            return format;
        }
    }

    /**
     * 遍历设置微软雅黑
     * @param view
     */
    private void findAllTxtView(View view) {
        Typeface mtypeface=Typeface.createFromAsset(getAssets(),"scyahei.ttf");
        if (view instanceof ViewGroup) {
            ViewGroup vp = (ViewGroup) view;
            for (int i = 0; i < vp.getChildCount(); i++) {
            View viewchild = vp.getChildAt(i);
                if(viewchild instanceof TextView){
                    ((TextView)viewchild).setTypeface(mtypeface);
                }else if(viewchild instanceof ViewGroup){
                    findAllTxtView(viewchild);
                }
            }
        }else if(view instanceof TextView){
            ((TextView)view).setTypeface(mtypeface);
        }
    }

}
