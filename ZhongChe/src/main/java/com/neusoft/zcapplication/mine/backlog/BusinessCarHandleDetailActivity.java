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
import com.neusoft.zcapplication.entity.GetAllSuppliers;
import com.neusoft.zcapplication.entity.GetApplyCarsDeal;
import com.neusoft.zcapplication.event.Events;
import com.neusoft.zcapplication.http.now.CallBack;
import com.neusoft.zcapplication.http.now.RetrofitFactory;
import com.neusoft.zcapplication.tools.AppUtils;
import com.neusoft.zcapplication.tools.DateUtils;
import com.neusoft.zcapplication.tools.StringUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 我的办理
 */
public class BusinessCarHandleDetailActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_supplier,tv_supplier_contact,tv_supplier_car_type;
    private TextView tv_apply_name,tv_apply_mobile,
            tv_apply_time,tv_car_type,tv_people_number,tv_start_time,tv_end_time,tv_car_range,
            tv_apply_reason,tv_user_name,tv_user_mobile,tv_car_number,tv_client_level,
            tv_apply_accounting_subject,tv_apply_appartment;
    private EditText et_handler_suggestion;
    private LinearLayout ll_user_name,ll_user_mobile,ll_is_important_client,ll_client_level;
    private ImageView iv_is_important_client;
    private LinearLayout ll_handler_record;
    private LinearLayout ll__handler_record_manager;
    private TextView tv_handler_record_department,tv_handler_record_manager;

    private GetApplyCarsDeal mGetApplyCarsDeal;
    private GetAllSuppliers mGetAllSuppliers;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_car_handle_detail);
        EventBus.getDefault().register(this);
        initView();
        initData();
    }

    private void initView(){
        AppUtils.setStateBar(BusinessCarHandleDetailActivity.this,findViewById(R.id.frg_status_bar));
        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.ll_supplier).setOnClickListener(this);
        findViewById(R.id.tv_agree).setOnClickListener(this);
        findViewById(R.id.tv_refuse).setOnClickListener(this);
        tv_supplier = (TextView) findViewById(R.id.tv_supplier);
        tv_apply_accounting_subject = (TextView) findViewById(R.id.tv_apply_accounting_subject);
        tv_apply_appartment = (TextView) findViewById(R.id.tv_apply_appartment);
        tv_supplier_contact = (TextView) findViewById(R.id.tv_supplier_contact);
        tv_supplier_car_type = (TextView) findViewById(R.id.tv_supplier_car_type);
        tv_apply_name = (TextView) findViewById(R.id.tv_apply_name);
        tv_apply_mobile = (TextView) findViewById(R.id.tv_apply_mobile);
        tv_apply_time = (TextView) findViewById(R.id.tv_apply_time);
        tv_car_type = (TextView) findViewById(R.id.tv_car_type);
        tv_people_number = (TextView) findViewById(R.id.tv_people_number);
        tv_start_time = (TextView) findViewById(R.id.tv_start_time);
        tv_end_time = (TextView) findViewById(R.id.tv_end_time);
        tv_car_range = (TextView) findViewById(R.id.tv_car_range);
        tv_apply_reason = (TextView) findViewById(R.id.tv_apply_reason);
        tv_user_name = (TextView) findViewById(R.id.tv_user_name);
        tv_user_mobile = (TextView) findViewById(R.id.tv_user_mobile);
        tv_car_number = (TextView) findViewById(R.id.tv_car_number);
        et_handler_suggestion = (EditText) findViewById(R.id.et_handler_suggestion);
        ll_user_name = (LinearLayout) findViewById(R.id.ll_user_name);
        ll_user_mobile = (LinearLayout) findViewById(R.id.ll_user_mobile);
        ll_is_important_client = (LinearLayout) findViewById(R.id.ll_is_important_client);
        ll_client_level = (LinearLayout) findViewById(R.id.ll_client_level);
        iv_is_important_client = (ImageView) findViewById(R.id.iv_is_important_client);
        tv_client_level = (TextView) findViewById(R.id.tv_client_level);

        ll_handler_record = (LinearLayout) findViewById(R.id.ll_handler_record);
        ll__handler_record_manager = (LinearLayout) findViewById(R.id.ll__handler_record_manager);
        tv_handler_record_department = (TextView) findViewById(R.id.tv_handler_record_department);
        tv_handler_record_manager = (TextView) findViewById(R.id.tv_handler_record_manager);

    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            mGetApplyCarsDeal = (GetApplyCarsDeal) bundle.getSerializable("getApplyCarsDeal");

            tv_apply_name.setText(mGetApplyCarsDeal.getApplicatName());
            tv_apply_mobile.setText(mGetApplyCarsDeal.getMobile());
            tv_apply_appartment.setText(StringUtil.isEmpty(mGetApplyCarsDeal.getUnitName())?"无":mGetApplyCarsDeal.getUnitName());
            tv_apply_accounting_subject.setText(StringUtil.isEmpty(mGetApplyCarsDeal.getAccountingSubjectName())?
                    "无":mGetApplyCarsDeal.getAccountingSubjectName());
            tv_apply_time.setText(DateUtils.DateToDayStr(new Date(mGetApplyCarsDeal.getApplicationDate())));
            tv_car_type.setText(mGetApplyCarsDeal.getCarType());
            tv_people_number.setText(""+mGetApplyCarsDeal.getNumberPeople());
            tv_start_time.setText(DateUtils.DateToMinuteStr(new Date(mGetApplyCarsDeal.getStartTime())));
            tv_end_time.setText(DateUtils.DateToMinuteStr(new Date(mGetApplyCarsDeal.getEndTime())));
            tv_car_range.setText(mGetApplyCarsDeal.getCarRange());
            tv_apply_reason.setText(mGetApplyCarsDeal.getApplyReason());
            if(StringUtil.isEmpty(mGetApplyCarsDeal.getUserName())){
                ll_user_name.setVisibility(View.GONE);
            }else{
                tv_user_name.setText(mGetApplyCarsDeal.getUserName());
            }
            if(StringUtil.isEmpty(mGetApplyCarsDeal.getUserMobile())){
                ll_user_mobile.setVisibility(View.GONE);
            }else{
                tv_user_mobile.setText(mGetApplyCarsDeal.getUserMobile());
            }
            if(mGetApplyCarsDeal.getImportantCustomer() == 1){
                iv_is_important_client.setImageResource(R.mipmap.btn_checkbox_pressed);
            }
            tv_client_level.setText(mGetApplyCarsDeal.getCustomerLevelJob());
            if(StringUtil.isEmpty(mGetApplyCarsDeal.getUserName())
                    &&StringUtil.isEmpty(mGetApplyCarsDeal.getUserMobile())){
                ll_is_important_client.setVisibility(View.GONE);
                ll_client_level.setVisibility(View.GONE);
            }
            tv_car_number.setText(""+mGetApplyCarsDeal.getCarNumber());
            //0：待审批 11:审批中（部门审批同意） 12：审批拒绝（部门审批拒绝） 21：待办理（总经办审批同意） 22：审批拒绝（总经办审批拒绝） 3/31：已办理  32：办理拒绝
            if(mGetApplyCarsDeal.getCarAuditEntities()!=null && mGetApplyCarsDeal.getCarAuditEntities().size()>0){
                ll_handler_record.setVisibility(View.VISIBLE);
                String departmentAdvice = mGetApplyCarsDeal.getCarAuditEntities().get(0).getAuditAdvice();
                tv_handler_record_department.setText(DateUtils.DateToMinuteStr(new Date(mGetApplyCarsDeal.getCarAuditEntities().get(0).getAuditTime()))
                        + "  " + mGetApplyCarsDeal.getCarAuditEntities().get(0).getAuditorName()
                        + "  通过" + ("同意".equals(departmentAdvice)?"":"  " + departmentAdvice));
                if(mGetApplyCarsDeal.getCarAuditEntities().size()==1){
                    ll__handler_record_manager.setVisibility(View.GONE);
                }else if(mGetApplyCarsDeal.getCarAuditEntities().size()==2){
                    ll__handler_record_manager.setVisibility(View.VISIBLE);
                    String managerAdvice = mGetApplyCarsDeal.getCarAuditEntities().get(1).getAuditAdvice();
                    tv_handler_record_manager.setText(DateUtils.DateToMinuteStr(new Date(mGetApplyCarsDeal.getCarAuditEntities().get(1).getAuditTime()))
                            + "  " + mGetApplyCarsDeal.getCarAuditEntities().get(1).getAuditorName()
                            + "  通过" + ("同意".equals(managerAdvice)?"":"  " + managerAdvice));
                }
            }else{
                ll_handler_record.setVisibility(View.GONE);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Events.BusinessCarChoseSupplier businessCarChoseSupplier){
        mGetAllSuppliers = businessCarChoseSupplier.getGetAllSuppliers();
        tv_supplier.setText(mGetAllSuppliers.getSupplierName());
        tv_supplier_contact.setText(mGetAllSuppliers.getContactName()+"/"+mGetAllSuppliers.getMobile());
        tv_supplier_car_type.setText(mGetAllSuppliers.getGetAllCarTypeBySupplierId()==null?
                "":mGetAllSuppliers.getGetAllCarTypeBySupplierId().getTypeName());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.ll_supplier:
                //选择供应商
                if(!StringUtil.isEmpty(mGetApplyCarsDeal.getSupplierName())){
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable("mGetAllSuppliers",mGetAllSuppliers);
                startActivity(BusinessCarChoseSupplierActivity.class,bundle);
                break;
            case R.id.tv_agree:
                //发送短信（同意办理）
                if(mGetApplyCarsDeal.getAuditStatus() == 3){
                    showToast("请勿重复办理！");
                    return;
                }
                if(mGetAllSuppliers == null){
                    showToast("请选择供应商");
                }else{
                    sendMessage("31");
                }
                break;
            case R.id.tv_refuse:
                //拒绝办理
                if(mGetApplyCarsDeal.getAuditStatus() == 3){
                    showToast("请勿重复办理！");
                    return;
                }
                sendMessage("32");
                break;
        }
    }

    /**
     * 发送短信
     */
    private void sendMessage(final String conductStatus){
        User userInfo = AppUtils.getUserInfo(mContext);
        String conductAdvice = et_handler_suggestion.getText().toString().trim();
        if("31".equals(conductStatus)){
            conductAdvice = "同意办理";
        }else{
            if(StringUtil.isEmpty(conductAdvice)){
                showToast("请填写办理意见");
                return;
            }
        }
        Map<String,Object> params = new HashMap<>();
        params.put("id",""+mGetApplyCarsDeal.getId());
        params.put("conductorId",""+mGetApplyCarsDeal.getConductorId());
        params.put("conductorName",""+userInfo.getEmployeeName());
        params.put("supplierId",mGetAllSuppliers==null?"":""+mGetAllSuppliers.getId());
        params.put("supplierCarTypeId",mGetAllSuppliers==null||mGetAllSuppliers.getGetAllCarTypeBySupplierId()==null?
                "":""+mGetAllSuppliers.getGetAllCarTypeBySupplierId().getTypeCode());
        params.put("conductAdvice",conductAdvice);
        params.put("conductStatus",conductStatus);

        showLoading();
        RetrofitFactory.getInstance().createApi(CarApi.class).sendMessage(params)
                .enqueue(new CallBack<Object>() {
                    @Override
                    public void success(Object object) {
                        dismissLoading();
                        if("31".equals(conductStatus)){
                            showToast("发送短信成功");
                        }else{
                            showToast("拒绝办理成功");
                        }
                        EventBus.getDefault().post(new Events.BusinessCarHandleChange());
                        finish();
                    }

                    @Override
                    public void fail(String code) {
                        dismissLoading();
                    }
                });
    }

}
