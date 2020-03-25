package com.neusoft.zcapplication.mine.order;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.neusoft.zcapplication.Constant.Constant;
import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.base.BaseActivity;
import com.neusoft.zcapplication.tools.AppUtils;
import com.neusoft.zcapplication.tools.DateUtils;
import com.squareup.picasso.Picasso;

import java.util.Map;

/**
 * 国内机票订单详情
 */

public class InternalOrderDetailActivity extends BaseActivity implements View.OnClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internal_order_detail);
        initView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
        }
    }

    private void initView() {
        findViewById(R.id.btn_back).setOnClickListener(this);
        AppUtils.setStateBar(InternalOrderDetailActivity.this, findViewById(R.id.frg_status_bar));//设置状态栏高度
        TextView fromCityTv = (TextView) findViewById(R.id.internal_order_detail_from_city);
        TextView toCityTv = (TextView) findViewById(R.id.internal_order_detail_to_city);
        TextView startDate = (TextView) findViewById(R.id.internal_order_detail_start_date);

        TextView startTimeTv = (TextView) findViewById(R.id.internal_order_detail_start_time);
        TextView fromAirportTv = (TextView) findViewById(R.id.internal_order_detail_from_airport);
        TextView endTimeTv = (TextView) findViewById(R.id.internal_order_detail_end_time);
        TextView toAirportTv = (TextView) findViewById(R.id.internal_order_detail_to_airport);

        TextView channelTv = (TextView) findViewById(R.id.internal_order_detail_channel);
        TextView flightInfoTv = (TextView) findViewById(R.id.internal_order_detail_flight_info);
        TextView cabinTv = (TextView) findViewById(R.id.internal_order_detail_cabin);//舱位

        ImageView companyImg = (ImageView) findViewById(R.id.internal_order_detail_airline_company);//航司logo

        TextView ticketPriceTv = (TextView) findViewById(R.id.internal_order_detail_ticket_price);
        TextView costTv = (TextView) findViewById(R.id.internal_order_detail_ticket_cost);//燃油、机建费
        TextView servicePriceTv = (TextView) findViewById(R.id.internal_order_detail_service_price);//服务费
        TextView allPriceTv = (TextView) findViewById(R.id.internal_order_detail_all_price);//总价格


        Map<String, Object> data = (Map<String, Object>) getIntent().getSerializableExtra("detailData");

        String channelName = null == data.get("SUPPLIERNAME") ? "" : data.get("SUPPLIERNAME").toString();
        channelTv.setText(channelName);
        String fromCity = null == data.get("FROMCITYNAME") ? "" : data.get("FROMCITYNAME").toString();
        String toCity = null == data.get("TOCITYNAME") ? "--" : data.get("TOCITYNAME").toString();
        fromCityTv.setText(fromCity);
        toCityTv.setText(toCity);

        String fromDate = null == data.get("FROMDATE") ? "" : data.get("FROMDATE").toString();//出发日期
        startDate.setText(fromDate);
        String cabin = null == data.get("CABIN") ? "" : data.get("CABIN").toString();
        cabinTv.setText(cabin);
        String companyName = null == data.get("CARRIERNAME") ? "" : data.get("CARRIERNAME").toString();
        String flightNo = null == data.get("FLIGHTNO") ? "" : data.get("FLIGHTNO").toString();
        flightInfoTv.setText(companyName + flightNo);

        double construction = null == data.get("COSTCONSTRUCTION") ? 0 : (double) data.get("COSTCONSTRUCTION");
        double servicePrice = null == data.get("SERVICEPRICE") ? 0 : (double) data.get("SERVICEPRICE");
        double fuelCost = null == data.get("FUELCOST") ? 0 : (double) data.get("FUELCOST");
        double price = null == data.get("TICKETPRICE") ? 0 : (double) data.get("TICKETPRICE");
        double fulAndCstPrice = construction + fuelCost;//燃油、机建费
//        double allPrice = fulAndCstPrice + servicePrice + price;//燃油、机建费
        double allPrice = null == data.get("TOTALPRICE") ? 0 : (double) data.get("TOTALPRICE");
        ticketPriceTv.setText("￥" + price);
        costTv.setText("￥" + fulAndCstPrice);
        servicePriceTv.setText("￥" + servicePrice);
        allPriceTv.setText("￥" + allPrice);

        String startTime = null == data.get("FROMPLANDATE") ? "--" : data.get("FROMPLANDATE").toString();
        String endTime = null == data.get("TOPLANDATE") ? "--" : data.get("TOPLANDATE").toString();
        if (startTime.length() > 10) {
            startTimeTv.setText(startTime.substring(11, startTime.length() - 3));
        } else {
            startTimeTv.setText(startTime);
        }
        if (endTime.length() > 10) {
            endTimeTv.setText(endTime.substring(11, endTime.length() - 3));
        } else {
            endTimeTv.setText(endTime);
        }
        String differStr = DateUtils.differTimeWithDays(startTime, endTime);
        TextView differTv = (TextView) findViewById(R.id.internal_order_detail_differ_time);
        differTv.setText(differStr);
        //出发到达航站楼
        String fromTerminal = null == data.get("FROMTERMINAL") ? "" : data.get("FROMTERMINAL").toString();
        String toTerminal = null == data.get("TOTERMINAL") ? "" : data.get("TOTERMINAL").toString();
        //出发、到达机场
        String fromAirport = null == data.get("FROMAIRPORT") ? "" : data.get("FROMAIRPORT").toString();
        String toAirport = null == data.get("TOAIRPORT") ? "" : data.get("TOAIRPORT").toString();

        String toStr = toAirport + toTerminal;
        if (toStr.length() > 0) {
            toAirportTv.setText(toStr);
        } else {
            toAirportTv.setText("--");
        }
        String fromStr = fromAirport + fromTerminal;
//        fromAirportTv.setText(fromTerminal);
        if (fromStr.length() > 0) {
            fromAirportTv.setText(fromStr);
        } else {
            fromAirportTv.setText("--");
        }

        if (flightNo.length() > 2) {
            String logo = flightNo.substring(0, 2).toUpperCase();
//            String iconSrc = "http://58.20.212.75:8001/travel_app-web/image/logo/" + logo + ".png";
            String iconSrc = Constant.FLIGHT_LOGO + logo + ".png";
            Picasso.with(InternalOrderDetailActivity.this).load(iconSrc).into(companyImg);
        }
        //预定申请单
        TextView billTv = (TextView) findViewById(R.id.internal_order_detail_bill_no);
        String billNo = null == data.get("ORDERAPPLYID") ? "--" : data.get("ORDERAPPLYID").toString();
        billTv.setText(billNo);
        //乘机人
        TextView psgTv = (TextView) findViewById(R.id.internal_order_detail_psg);
        String psg = null == data.get("EMPLOYEENAME") ? "--" : data.get("EMPLOYEENAME").toString();
        psgTv.setText(psg);

        //改签费用
        TextView changeTv = (TextView) findViewById(R.id.internal_order_detail_change_price);
        //退票费用
        TextView returnTv = (TextView) findViewById(R.id.internal_order_detail_return_price);
        double returnFee = null == data.get("RETURNFEE") ? 0 : (double) data.get("RETURNFEE");//退票费
        LinearLayout priceLayout = (LinearLayout) findViewById(R.id.internal_order_detail_price_layout);

        double changeFee = null == data.get("CHANGEFEE") ? 0 : (double) data.get("CHANGEFEE");//改签费
        changeTv.setText("￥" + changeFee);
        double changePrice = null == data.get("CHANGEPRICE") ? 0 : (double) data.get("CHANGEPRICE");//退票中的改签费
        LinearLayout costLayout = (LinearLayout) findViewById(R.id.internal_order_detail_cost_layout);//燃油、机建
        /**
         * 退票中、已退票：显示退票费
         */
        String statusStr = null == data.get("ORDERSTATE") ? "" : data.get("ORDERSTATE").toString();
        if (statusStr.equals("已退票")) {//3
            //个人票价
            priceLayout.setVisibility(View.GONE);//已退票中，不显示票价
            costLayout.setVisibility(View.GONE);
            changeTv.setText("￥" + changePrice);
            returnTv.setText("￥" + returnFee);
        } else if (statusStr.equals("退票中")) {
            priceLayout.setVisibility(View.GONE);//已退票中，不显示票价
            costLayout.setVisibility(View.GONE);
            changeTv.setText("￥" + changePrice);
            returnTv.setText("￥" + returnFee);
        }
        //已改签，改签中的燃油、机建费
//        if (statusStr.equals("改签中") || statusStr.equals("已改签")) {
//            double fuelFee = null == data.get("FUELCOST") ? 0 : (double) data.get("FUELCOST");
//            costTv.setText("￥" + fuelFee);
//        }
//        退票中，已退票，改签中，已改签 ：显示退改签理由
        if (statusStr.equals("退票中") || statusStr.equals("已退票")
                || statusStr.equals("改签中") || statusStr.equals("已改签")) {
            findViewById(R.id.internal_order_detail_ticket_return_layout).setVisibility(View.VISIBLE);
            TextView returnReasonTv = (TextView) findViewById(R.id.internal_order_detail_ticket_return);
            String opTxt = null == data.get("OPREMARK") ? "--" : data.get("OPREMARK").toString();
            returnReasonTv.setText(opTxt);
        }

        //退改规则
        TextView ruleTv = (TextView) findViewById(R.id.internal_order_detail_ticket_rule);
        String ruleTxt = null == data.get("RETURNCHANGERULE") ? "--" : data.get("RETURNCHANGERULE").toString();
        ruleTv.setText(ruleTxt);
        //票号
        TextView ticketNumTv = (TextView) findViewById(R.id.internal_order_detail_ticket_num);
        String ticketId = null == data.get("TICKETID") ? "" : data.get("TICKETID").toString();
        ticketNumTv.setText(ticketId);
        //出票时间
        TextView ticketTimeTv = (TextView) findViewById(R.id.internal_order_detail_get_ticket_time);
        String ticketTime = null == data.get("TICKETTIME") ? "--" : data.get("TICKETTIME").toString();
        ticketTimeTv.setText(ticketTime);

    }
}
