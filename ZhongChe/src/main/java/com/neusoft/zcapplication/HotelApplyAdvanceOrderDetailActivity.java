package com.neusoft.zcapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.neusoft.zcapplication.base.BaseActivity;
import com.neusoft.zcapplication.tools.AppUtils;
import com.neusoft.zcapplication.tools.StringUtil;

import java.util.Map;

/**
 * 申请单详情
 */
public class HotelApplyAdvanceOrderDetailActivity extends BaseActivity implements View.OnClickListener {

    private TextView item_hotel_trip_to_city,item_hotel_trip_checkIn_date,item_hotel_trip_checkOut_date,
            tv_apply_name,tv_apply_id_card,tv_apply_employeeCode,tv_apply_mobile,tv_accounting_subject,
            tv_apply_id_card_type,tv_out_reason,tv_accounting_depart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_hotel_advance_order_detail);
        initView();
        initData();
    }

    private void initView(){
        AppUtils.setStateBar(HotelApplyAdvanceOrderDetailActivity.this,findViewById(R.id.frg_status_bar));
        findViewById(R.id.btn_back).setOnClickListener(this);
        item_hotel_trip_to_city = (TextView) findViewById(R.id.item_hotel_trip_to_city);
        item_hotel_trip_checkIn_date = (TextView) findViewById(R.id.item_hotel_trip_checkIn_date);
        item_hotel_trip_checkOut_date = (TextView) findViewById(R.id.item_hotel_trip_checkOut_date);
        tv_apply_name = (TextView) findViewById(R.id.tv_apply_name);
        tv_apply_id_card = (TextView) findViewById(R.id.tv_apply_id_card);
        tv_apply_employeeCode = (TextView) findViewById(R.id.tv_apply_employeeCode);
        tv_apply_mobile = (TextView) findViewById(R.id.tv_apply_mobile);
        tv_accounting_subject = (TextView) findViewById(R.id.tv_accounting_subject);
        tv_apply_id_card_type = (TextView) findViewById(R.id.tv_apply_id_card_type);
        tv_out_reason = (TextView) findViewById(R.id.tv_out_reason);
        tv_accounting_depart = (TextView) findViewById(R.id.tv_accounting_depart);
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            Map<String,Object> map = (Map<String, Object>) bundle.getSerializable("map");
            String toCity = map.get("toCity")!=null?map.get("toCity").toString():"";
            String checkInTime = map.get("checkInTime")!=null?map.get("checkInTime").toString():"";
            String checkOutTime = map.get("checkOutTime")!=null?map.get("checkOutTime").toString():"";
            item_hotel_trip_to_city.setText(toCity);
            item_hotel_trip_checkIn_date.setText(checkInTime);
            item_hotel_trip_checkOut_date.setText(checkOutTime);
            tv_apply_name.setText(map.get("empName")!=null?map.get("empName").toString():"");
            String documentId = map.get("documentId")!=null?map.get("documentId").toString():"";
            if(StringUtil.isEmpty(documentId)){
                //证件类型是身份证
                tv_apply_id_card_type.setText("身份证：");
                tv_apply_id_card.setText(map.get("idcard")!=null?map.get("idcard").toString():"");
            }else{
                tv_apply_id_card_type.setText(map.get("documentName")!=null?map.get("documentName").toString() + "：":"身份证：");
                tv_apply_id_card.setText(map.get("documentInfo")!=null?map.get("documentInfo").toString():"");
            }
            tv_apply_employeeCode.setText(map.get("empCode")!=null?map.get("empCode").toString():"");
            tv_apply_mobile.setText(map.get("mobil")!=null?map.get("mobil").toString():"");
            tv_accounting_subject.setText(map.get("accountEntity")!=null?map.get("accountEntity").toString():"");
            tv_out_reason.setText(map.get("reaseon")!=null?map.get("reaseon").toString():"");
            tv_accounting_depart.setText(map.get("unitName")!=null?map.get("unitName").toString():"");
            if (map.get("unitName")==null){
                findViewById(R.id.view_depart).setVisibility(View.GONE);
            }
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

}
