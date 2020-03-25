package com.neusoft.zcapplication.mine.order;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.base.BaseActivity;
import com.neusoft.zcapplication.http.NetWorkRequest;
import com.neusoft.zcapplication.http.RequestCallback;
import com.neusoft.zcapplication.http.RequestUtil;
import com.neusoft.zcapplication.http.URL;
import com.neusoft.zcapplication.tools.AppUtils;
import com.neusoft.zcapplication.tools.ToastUtil;

import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * 酒店订单详情
 */

public class HotelOrderDetailActivity extends BaseActivity implements View.OnClickListener,RequestCallback{
    private Call<Map<String,Object>> detailCall;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_order_detail);
        Map<String,Object> dataMap = (Map<String, Object>) getIntent().getSerializableExtra("data");
        initView(dataMap);
        String orderId = dataMap.get("orderId").toString();
        TextView billIdTv = (TextView)findViewById(R.id.ht_order_detail_bill_id);
        billIdTv.setText(orderId);
//        Log.i("--->","" + orderId);
        getDetailData(orderId);
    }
    @Override
    protected void onStop() {
        super.onStop();
        if(null != detailCall && detailCall.isExecuted()){
            detailCall.isCanceled();
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
        }
    }

    @Override
    public void requestSuccess(Object map, int type) {
        if(type == 1){
            Map<String,Object> result = (Map<String, Object>) map;
            String code = null == result.get("code") ? "" :result.get("code").toString();
            if(code.equals("00000")){
                Object listObj = result.get("data");
                if(null == listObj){
                    ToastUtil.toastFail(HotelOrderDetailActivity.this);
                }else{
                    Map<String,Object> dataMap = (Map<String,Object>) listObj;
                    Map<String,Object> psgMap = (Map<String,Object>) dataMap.get("passenger");
                    Map<String,Object> hotelMap = (Map<String,Object>) dataMap.get("hotelDetail");
                    String indayStr = null ==  hotelMap.get("inday") ? "0": hotelMap.get("inday").toString();
                    String priceStr = null ==  hotelMap.get("price") ? "0": hotelMap.get("price").toString();
                    String servicePriceStr = null ==  hotelMap.get("servicePrice") ? "0": hotelMap.get("servicePrice").toString();
                    double inday = Double.parseDouble(indayStr);
                    double price = Double.parseDouble(priceStr);
                    double servicePrice = servicePriceStr.equals("") ? 0: Double.parseDouble(servicePriceStr) / inday;
//                    double price = null == hotelMap.get("price") ? 0: (double) hotelMap.get("price");
//                    double servicePrice = null == hotelMap.get("servicePrice") ? 0: (double) hotelMap.get("servicePrice");
                    //1已确认，0 未确认
                    String isConfirmIn = null ==  hotelMap.get("isConfirmIn") ? "0": hotelMap.get("isConfirmIn").toString();
                    TextView inDayTv = (TextView)findViewById(R.id.ht_order_detail_inday_num);//入住天数
                    if(isConfirmIn.equals("1")){
                        inDayTv.setText("实际入住:" + indayStr + "天");
                        inDayTv.setVisibility(View.VISIBLE);
                    }
                    TextView roomPriceTv = (TextView)findViewById(R.id.ht_order_detail_room_price);
                    roomPriceTv.setText("￥" + price + "/天");
                    TextView svPriceTv = (TextView)findViewById(R.id.ht_order_detail_sv_price);
                    DecimalFormat  df = new DecimalFormat("#.00");
                    if(servicePrice > 0){
                        svPriceTv.setText("￥" + df.format(servicePrice) + "/天");
                    }else{
                        svPriceTv.setText("￥0/天");
                    }
                    TextView roomPricePerDayTv = (TextView)findViewById(R.id.ht_order_detail_price_per_day);
                    double perDayPrice = price + servicePrice;
                    roomPricePerDayTv.setText("￥" + perDayPrice  + "/天");

                    double allPrice = perDayPrice * inday;
                    TextView allPriceTv = (TextView)findViewById(R.id.ht_order_detail_total_price);
                    allPriceTv.setText("￥" +  df.format(allPrice));

                    //联系人信息
                    Map<String,Object> applicantMap = (Map<String,Object>) dataMap.get("applicant");
                    String contactTel = applicantMap.get("phone").toString();
                    String contactName = applicantMap.get("name").toString();
                    TextView contactNameTv = (TextView)findViewById(R.id.ht_order_detail_arrive_contact_man);
                    TextView contactTelTv = (TextView)findViewById(R.id.ht_order_detail_arrive_contact_man_tel);
                    contactNameTv.setText(contactName);
                    contactTelTv.setText(contactTel);
                    //入住人信息
                    String cstTel = psgMap.get("phone").toString();
                    String cstName = psgMap.get("name").toString();
                    TextView cstNameTv = (TextView)findViewById(R.id.ht_order_detail_arrive_in_man);
                    TextView cstTelTv = (TextView)findViewById(R.id.ht_order_detail_arrive_in_man_tel);
                    cstNameTv.setText(cstName);
                    cstTelTv.setText(cstTel);
                }
            }else{
                ToastUtil.toastFail(HotelOrderDetailActivity.this);
            }
        }
    }

    @Override
    public void requestFail(int type) {
        if(type == 1){
            ToastUtil.toastFail(HotelOrderDetailActivity.this);
        }
    }

    @Override
    public void requestCancel(int type) {

    }
    private void initView(Map<String,Object> map){
        findViewById(R.id.btn_back).setOnClickListener(this);
        AppUtils.setStateBar(HotelOrderDetailActivity.this,findViewById(R.id.frg_status_bar));
        //酒店名称
        String hotelName = map.get("hotelName").toString();
        TextView hotelNameTv = (TextView)findViewById(R.id.ht_order_detail_ht_name);
        hotelNameTv.setText(hotelName);
        //预定申请单号
        String orderApplyId = map.get("orderApplyId").toString();
        TextView billTv = (TextView)findViewById(R.id.ht_order_detail_bill);
        billTv.setText(orderApplyId);

        String fromTime = map.get("fromTime").toString();
        String toTime = map.get("toTime").toString();
        String timeStr = "入住：" + fromTime + " 离店：" + toTime;
        TextView timeTv = (TextView)findViewById(R.id.ht_order_detail_ht_room_time);
        timeTv.setText(timeStr);
        //渠道名称
        String supplierName = map.get("supplierName").toString();
        TextView channelTv = (TextView)findViewById(R.id.ht_order_detail_channel);
        channelTv.setText("预定渠道：" + supplierName);

//        //已完成订单才显示实际入住天数
//        TextView inDayTv = (TextView)findViewById(R.id.ht_order_detail_inday_num);//入住天数
//        String stateName = getIntent().getStringExtra("stateName");
//        if(stateName.equals("已完成")){
//            inDayTv.setVisibility(View.VISIBLE);
//        }
    }
    private void getDetailData(String orderId) {
        Map<String,Object> params = new HashMap<>();
//        params.put("ciphertext","test");
//        params.put("loginType", URL.LOGIN_TYPE);
        params.put("orderId", orderId);
        Retrofit retrofit = new RequestUtil().getRequestClient(URL.ZHONGCHE_93);
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        detailCall = request.getHotelDetail(params);
        new RequestUtil().requestData(detailCall,this,1,"加载中...",true,HotelOrderDetailActivity.this);
    }
}
