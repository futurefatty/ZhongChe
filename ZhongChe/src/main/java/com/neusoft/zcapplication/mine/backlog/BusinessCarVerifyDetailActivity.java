package com.neusoft.zcapplication.mine.backlog;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.neusoft.zcapplication.Bean.User;
import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.api.CarApi;
import com.neusoft.zcapplication.base.BaseActivity;
import com.neusoft.zcapplication.entity.GetApplyCarsAudit;
import com.neusoft.zcapplication.event.Events;
import com.neusoft.zcapplication.http.now.CallBack;
import com.neusoft.zcapplication.http.now.RetrofitFactory;
import com.neusoft.zcapplication.tools.AppUtils;
import com.neusoft.zcapplication.tools.DateUtils;
import com.neusoft.zcapplication.tools.StringUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 我的审批
 */
public class BusinessCarVerifyDetailActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_name,tv_apply_time,tv_car_type,tv_people_number,tv_start_time,tv_end_time,
            tv_car_range,tv_car_reason,tv_user_name,tv_user_mobile,tv_car_number,tv_client_level,
            tv_apply_accounting_subject,tv_apply_appartment;
    private EditText et_verify_suggestion;
    private LinearLayout ll_user_name,ll_user_mobile,ll_is_important_client,ll_client_level;
    private ImageView iv_is_important_client;
    private LinearLayout ll_verify_record;
    private LinearLayout ll_verify_record_manager;
    private TextView tv_verify_record_department,tv_verify_record_manager;

    private GetApplyCarsAudit mGetApplyCarsAudit;
    private String suggestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_car_verify_detail);
        initView();
        initData();
    }

    private void initView(){
        AppUtils.setStateBar(BusinessCarVerifyDetailActivity.this,findViewById(R.id.frg_status_bar));
        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.tv_agree).setOnClickListener(this);
        findViewById(R.id.tv_refuse).setOnClickListener(this);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_apply_accounting_subject = (TextView) findViewById(R.id.tv_apply_accounting_subject);
        tv_apply_appartment = (TextView) findViewById(R.id.tv_apply_appartment);
        tv_apply_time = (TextView) findViewById(R.id.tv_apply_time);
        tv_car_type = (TextView) findViewById(R.id.tv_car_type);
        tv_people_number = (TextView) findViewById(R.id.tv_people_number);
        tv_start_time = (TextView) findViewById(R.id.tv_start_time);
        tv_end_time = (TextView) findViewById(R.id.tv_end_time);
        tv_car_range = (TextView) findViewById(R.id.tv_car_range);
        tv_car_reason = (TextView) findViewById(R.id.tv_car_reason);
        et_verify_suggestion = (EditText) findViewById(R.id.et_verify_suggestion);
        tv_user_name = (TextView) findViewById(R.id.tv_user_name);
        tv_user_mobile = (TextView) findViewById(R.id.tv_user_mobile);
        tv_car_number = (TextView) findViewById(R.id.tv_car_number);
        ll_user_name = (LinearLayout) findViewById(R.id.ll_user_name);
        ll_user_mobile = (LinearLayout) findViewById(R.id.ll_user_mobile);
        ll_is_important_client = (LinearLayout) findViewById(R.id.ll_is_important_client);
        ll_client_level = (LinearLayout) findViewById(R.id.ll_client_level);
        iv_is_important_client = (ImageView) findViewById(R.id.iv_is_important_client);
        tv_client_level = (TextView) findViewById(R.id.tv_client_level);

        ll_verify_record = (LinearLayout) findViewById(R.id.ll_verify_record);
        ll_verify_record_manager = (LinearLayout) findViewById(R.id.ll_verify_record_manager);
        tv_verify_record_department = (TextView) findViewById(R.id.tv_verify_record_department);
        tv_verify_record_manager = (TextView) findViewById(R.id.tv_verify_record_manager);

    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            mGetApplyCarsAudit = (GetApplyCarsAudit) bundle.getSerializable("getApplyCarsAudit");
            tv_name.setText(mGetApplyCarsAudit.getApplicatName());
            tv_apply_appartment.setText(StringUtil.isEmpty(mGetApplyCarsAudit.getUnitName())?"无":mGetApplyCarsAudit.getUnitName());
            tv_apply_accounting_subject.setText(StringUtil.isEmpty(mGetApplyCarsAudit.getAccountingSubjectName())?
                    "无":mGetApplyCarsAudit.getAccountingSubjectName());
            tv_apply_time.setText(""+ DateUtils.DateToDayStr(new Date(mGetApplyCarsAudit.getApplicationDate())));
            tv_car_type.setText(mGetApplyCarsAudit.getCarType());
            tv_people_number.setText(mGetApplyCarsAudit.getNumberPeople());
            tv_start_time.setText(""+ DateUtils.DateToMinuteStr(new Date(mGetApplyCarsAudit.getStartTime())));
            tv_end_time.setText(""+ DateUtils.DateToMinuteStr(new Date(mGetApplyCarsAudit.getEndTime())));
            tv_car_range.setText(mGetApplyCarsAudit.getCarRange());
            tv_car_reason.setText(mGetApplyCarsAudit.getApplyReason());

            if(StringUtil.isEmpty(mGetApplyCarsAudit.getUserName())){
                ll_user_name.setVisibility(View.GONE);
            }else{
                tv_user_name.setText(mGetApplyCarsAudit.getUserName());
            }
            if(StringUtil.isEmpty(mGetApplyCarsAudit.getUserMobile())){
                ll_user_mobile.setVisibility(View.GONE);
            }else{
                tv_user_mobile.setText(mGetApplyCarsAudit.getUserMobile());
            }
            if(mGetApplyCarsAudit.getImportantCustomer() == 1){
                iv_is_important_client.setImageResource(R.mipmap.btn_checkbox_pressed);
            }
            tv_client_level.setText(mGetApplyCarsAudit.getCustomerLevelJob());
            if(StringUtil.isEmpty(mGetApplyCarsAudit.getUserName())
                    &&StringUtil.isEmpty(mGetApplyCarsAudit.getUserMobile())){
                ll_is_important_client.setVisibility(View.GONE);
                ll_client_level.setVisibility(View.GONE);
            }
            tv_car_number.setText(""+mGetApplyCarsAudit.getCarNumber());
            if(mGetApplyCarsAudit.getCarAuditEntities()!=null && mGetApplyCarsAudit.getCarAuditEntities().size()>0){
                ll_verify_record.setVisibility(View.VISIBLE);
                String departmentAdvice = mGetApplyCarsAudit.getCarAuditEntities().get(0).getAuditAdvice();
                tv_verify_record_department.setText(DateUtils.DateToMinuteStr(new Date(mGetApplyCarsAudit.getCarAuditEntities().get(0).getAuditTime()))
                        + "  " + mGetApplyCarsAudit.getCarAuditEntities().get(0).getAuditorName()
                        + "  通过" + ("同意".equals(departmentAdvice)?"":"  " + departmentAdvice));
                if(mGetApplyCarsAudit.getCarAuditEntities().size()==1){
                    ll_verify_record_manager.setVisibility(View.GONE);
                }else if(mGetApplyCarsAudit.getCarAuditEntities().size()==2){
                    ll_verify_record_manager.setVisibility(View.VISIBLE);
                    String managerAdvice = mGetApplyCarsAudit.getCarAuditEntities().get(1).getAuditAdvice();
                    tv_verify_record_manager.setText(DateUtils.DateToMinuteStr(new Date(mGetApplyCarsAudit.getCarAuditEntities().get(1).getAuditTime()))
                            + "  " + mGetApplyCarsAudit.getCarAuditEntities().get(1).getAuditorName()
                            + "  通过" + ("同意".equals(managerAdvice)?"":"  " + managerAdvice));
                }
            }else{
                ll_verify_record.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.tv_agree:
                //同意
                if(mGetApplyCarsAudit == null){
                    showToast("获取数据失败");
                    return;
                }
                suggestion = et_verify_suggestion.getText().toString().trim();
                if(StringUtil.isEmpty(suggestion)){
                    suggestion = "同意";
                }
                examineApplyCar("1");
                break;
            case R.id.tv_refuse:
                //拒绝
                if(mGetApplyCarsAudit == null){
                    showToast("获取数据失败");
                    return;
                }
                suggestion = et_verify_suggestion.getText().toString().trim();
                if(StringUtil.isEmpty(suggestion)){
                    showToast("请填写审批意见");
                    return;
                }
                examineApplyCar("2");
                break;
        }
    }

    /**
     * 审批用车
     */
    private void examineApplyCar(String auditStatus){
        User userInfo = AppUtils.getUserInfo(mContext);
        Map<String,Object> params = new HashMap<>();
        params.put("id",mGetApplyCarsAudit.getAuditId());
        params.put("applicatId",mGetApplyCarsAudit.getApplicatId());
        params.put("applicatName",mGetApplyCarsAudit.getApplicatName());
        params.put("unitCode",mGetApplyCarsAudit.getUnitCode());
        params.put("auditorId",mGetApplyCarsAudit.getAuditorId());
        params.put("publicAuditId",mGetApplyCarsAudit.getId());
        params.put("auditorName",mGetApplyCarsAudit.getAuditorName());
        params.put("auditStatus",auditStatus);
        params.put("auditAdvice",suggestion);
        params.put("applyReason",mGetApplyCarsAudit.getApplyReason());
        params.put("carApplyId",mGetApplyCarsAudit.getCarApplyId());
        params.put("createDate",mGetApplyCarsAudit.getCreateDate());
        params.put("applicatorEmail",mGetApplyCarsAudit.getEmail());
        params.put("auditorEmail",userInfo.getMail());

        showLoading();
        RetrofitFactory.getInstance().createApi(CarApi.class).examineApplyCar(params)
                .enqueue(new CallBack<Object>() {
                    @Override
                    public void success(Object object) {
                        dismissLoading();
                        showToast("操作成功");
                        EventBus.getDefault().post(new Events.BusinessCarVerifyChange());
                        finish();
                    }

                    @Override
                    public void fail(String code) {
                        dismissLoading();
                    }
                });
    }

}
