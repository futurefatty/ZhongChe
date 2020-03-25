package com.neusoft.zcapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.neusoft.zcapplication.base.BaseActivity;
import com.neusoft.zcapplication.tools.AppUtils;
import com.neusoft.zcapplication.tools.StringUtil;

import java.util.Map;

/**
 * 申请单详情
 */
public class ApplyAdvanceOrderDetailActivity extends BaseActivity implements View.OnClickListener {

    private TextView trip_mode, tv_start_place, tv_end_place, tv_time, tv_apply_name, tv_apply_id_card_type, tv_apply_id_card,
            tv_apply_employeeCode, tv_apply_mobile, tv_accounting_subject, tv_out_reason,
            tv_hotel_start_time, tv_hotel_end_time, tv_accounting_depart;
    private LinearLayout ll_hotel_info;
    private CheckBox cb_book_hotel;
    //3月新增
    private LinearLayout is_ipd_ll;
    private Switch is_ipd_switch;//是否是ipd
    private TextView ipd_project_name, ipd_project_mark, ipd_task_name, ipd_task_coding, ipd_approval_personnel, ipd_approval_personnel_number;
    public TextView ipd_manager_number;//项目经理工号
    public TextView ipd_manager_name;//项目经理名称

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_advance_order_detail);
        initView();
        initData();
    }

    private void initView() {
        AppUtils.setStateBar(ApplyAdvanceOrderDetailActivity.this, findViewById(R.id.frg_status_bar));
        findViewById(R.id.btn_back).setOnClickListener(this);
        trip_mode = (TextView) findViewById(R.id.trip_mode);
        tv_start_place = (TextView) findViewById(R.id.tv_start_place);
        tv_end_place = (TextView) findViewById(R.id.tv_end_place);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_apply_name = (TextView) findViewById(R.id.tv_apply_name);
        tv_apply_id_card_type = (TextView) findViewById(R.id.tv_apply_id_card_type);
        tv_apply_id_card = (TextView) findViewById(R.id.tv_apply_id_card);
        tv_apply_employeeCode = (TextView) findViewById(R.id.tv_apply_employeeCode);
        tv_apply_mobile = (TextView) findViewById(R.id.tv_apply_mobile);
        tv_accounting_subject = (TextView) findViewById(R.id.tv_accounting_subject);
        tv_out_reason = (TextView) findViewById(R.id.tv_out_reason);
        tv_hotel_start_time = (TextView) findViewById(R.id.tv_hotel_start_time);
        tv_hotel_end_time = (TextView) findViewById(R.id.tv_hotel_end_time);
        tv_accounting_depart = (TextView) findViewById(R.id.tv_accounting_depart);
        ll_hotel_info = (LinearLayout) findViewById(R.id.ll_hotel_info);
        cb_book_hotel = (CheckBox) findViewById(R.id.cb_book_hotel);
        //3月新增
        is_ipd_switch = (Switch) this.findViewById(R.id.is_ipd_switch);
        is_ipd_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //Todo
                    is_ipd_ll.setVisibility(View.VISIBLE);
                } else {
                    //Todo
                    is_ipd_ll.setVisibility(View.GONE);
                }
            }
        });
        is_ipd_ll = (LinearLayout) findViewById(R.id.is_ipd_ll);
        ipd_project_name = (TextView) findViewById(R.id.ipd_project_name);
        ipd_project_mark = (TextView) findViewById(R.id.ipd_project_mark);
        ipd_task_name = (TextView) findViewById(R.id.ipd_task_name);
        ipd_task_coding = (TextView) findViewById(R.id.ipd_task_coding);
        ipd_manager_name = (TextView) findViewById(R.id.ipd_manager_name);
        ipd_manager_number = (TextView) findViewById(R.id.ipd_manager_number);
        ipd_approval_personnel = (TextView) findViewById(R.id.ipd_approval_personnel);
        ipd_approval_personnel_number = (TextView) findViewById(R.id.ipd_approval_personnel_number);
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Map<String, Object> map = (Map<String, Object>) bundle.getSerializable("map");
            trip_mode.setText(map.get("travelType") != null ? map.get("travelType").toString() : "");
            String fromCity = map.get("fromCity") != null ? map.get("fromCity").toString() : "";
            tv_start_place.setText("出发地：" + fromCity);
            String toCity = map.get("toCity") != null ? map.get("toCity").toString() : "";
            tv_end_place.setText("目的地：" + toCity);
            String fromDate = map.get("fromDate") != null ? map.get("fromDate").toString() : "";
            tv_time.setText("出发时间：" + fromDate);
            if (map.get("checkInTime") != null && map.get("checkOutTime") != null
                    && !StringUtil.isEmpty(map.get("checkInTime").toString())
                    && !StringUtil.isEmpty(map.get("checkOutTime").toString())) {
                ll_hotel_info.setVisibility(View.VISIBLE);
                cb_book_hotel.setChecked(true);
                tv_hotel_start_time.setText("入住日期：" + map.get("checkInTime").toString());
                tv_hotel_end_time.setText("离店日期：" + map.get("checkOutTime").toString());
            } else {
                ll_hotel_info.setVisibility(View.GONE);
            }
            tv_apply_name.setText(map.get("empName") != null ? map.get("empName").toString() : "");
            String documentId = map.get("documentId") != null ? map.get("documentId").toString() : "";
            if (StringUtil.isEmpty(documentId)) {
                //证件类型是身份证
                tv_apply_id_card_type.setText("身份证：");
                tv_apply_id_card.setText(map.get("idcard") != null ? map.get("idcard").toString() : "");
            } else {
                tv_apply_id_card_type.setText(map.get("documentName") != null ? map.get("documentName").toString() + "：" : "身份证：");
                tv_apply_id_card.setText(map.get("documentInfo") != null ? map.get("documentInfo").toString() : "");
            }
            tv_apply_employeeCode.setText(map.get("empCode") != null ? map.get("empCode").toString() : "");
            tv_apply_mobile.setText(map.get("mobil") != null ? map.get("mobil").toString() : "");
            tv_accounting_subject.setText(map.get("accountEntity") != null ? map.get("accountEntity").toString() : "");
            tv_out_reason.setText(map.get("reaseon") != null ? map.get("reaseon").toString() : "");
            tv_accounting_depart.setText(map.get("unitName") != null ? map.get("unitName").toString() : "");
            if (map.get("unitName") == null) {
                findViewById(R.id.view_depart).setVisibility(View.GONE);
            }
            //3月新增
            if (StringUtil.isEmpty(map.get("isIPD") + "")) {
                is_ipd_ll.setVisibility(View.GONE);
                is_ipd_switch.setChecked(false);
            } else {
                if ((map.get("isIPD") + "").equals("1")) {
                    is_ipd_switch.setChecked(true);
                    is_ipd_ll.setVisibility(View.VISIBLE);
                    ipd_project_name.setText(map.get("projName") + "");
                    ipd_project_mark.setText(map.get("costCode") + "");
                    ipd_task_name.setText(map.get("taskName") + "");
                    ipd_task_coding.setText(map.get("taskId") + "");
                    ipd_manager_number.setText(map.get("pmCode") + "");
                    ipd_manager_name.setText(map.get("pmName") + "");
                    ipd_approval_personnel_number.setText(map.get("approver") + "");
                    ipd_approval_personnel.setText(map.get("approverName") + "");
                } else {
                    is_ipd_switch.setChecked(false);
                    is_ipd_ll.setVisibility(View.GONE);
                }
            }

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
        }
    }

}
