package com.neusoft.zcapplication.CarService;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.neusoft.zcapplication.Bean.User;
import com.neusoft.zcapplication.Constant.Constant;
import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.api.CarApi;
import com.neusoft.zcapplication.api.OrderApi;
import com.neusoft.zcapplication.base.BaseActivity;
import com.neusoft.zcapplication.entity.GetAllApplyUseCars;
import com.neusoft.zcapplication.entity.GetAllCarRange;
import com.neusoft.zcapplication.entity.GetAllCarType;
import com.neusoft.zcapplication.entity.GetSelectEmployee;
import com.neusoft.zcapplication.event.Events;
import com.neusoft.zcapplication.http.now.CallBack;
import com.neusoft.zcapplication.http.now.RetrofitFactory;
import com.neusoft.zcapplication.tools.AppUtils;
import com.neusoft.zcapplication.tools.DateUtils;
import com.neusoft.zcapplication.tools.DisplayUtil;
import com.neusoft.zcapplication.tools.StringUtil;
import com.neusoft.zcapplication.widget.TimeSelector;
import com.neusoft.zcapplication.widget.popuwindow.CommonPopupWindow;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 公务用车我的申请
 */
public class BusinessCarApplyDetailActivity extends BaseActivity implements View.OnClickListener,
        CommonPopupWindow.ViewInterface {

    private TextView tv_apply_name,tv_apply_apartment,tv_apply_time,
            tv_car_type,tv_start_time,tv_end_time,tv_car_range,
            tv_apply_status,tv_department_advice,tv_officer_manager_advice,tv_handler_advice,tv_save,
            tv_add_car_user_add,tv_add_car_user_tips,tv_apply_accounting_subject;
    private EditText et_apply_mobile,et_user_name,et_user_mobile,et_car_number,et_people_number,
            et_apply_reason,et_client_level;
    private LinearLayout ll_container,ll_car_type,ll_car_range;
    private LinearLayout ll_add_car_user,ll_car_user,ll_is_important_client;
    private ImageView iv_is_important_client;

    private GetAllApplyUseCars getAllApplyUseCars;

    private CommonPopupWindow carTypePopupWindow,carRangePopupWindow;
    private TimeSelector startTimeSelector,endTimeSelector;//时间选择器

    private List<GetAllCarType> mGetAllCarTypeList = new ArrayList<>();
    private List<GetAllCarRange> mGetAllCarRangeList = new ArrayList<>();
    private BusinessCarApplyPopuCarTypeAdapter mBusinessCarApplyPopuCarTypeAdapter;
    private BusinessCarApplyPopuCarRangeAdapter mBusinessCarApplyPopuCarRangeAdapter;
    private GetAllCarType mChoseGetAllCarType;
    private GetAllCarRange mChoseGetAllCarRange;
    private GetSelectEmployee.CompanyBean mChoseCompanyBean;

    private boolean isAddCarUser;
    private boolean isImprotantClient;

    private PopupWindow window = new PopupWindow();
    private View popupView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_car_apply_detail);
        initView();
        initData();
    }

    private void initView(){
        AppUtils.setStateBar(BusinessCarApplyDetailActivity.this,findViewById(R.id.frg_status_bar));
        findViewById(R.id.btn_back).setOnClickListener(this);
        ll_container = (LinearLayout) findViewById(R.id.ll_container);
        tv_apply_name = (TextView) findViewById(R.id.tv_apply_name);
        et_apply_mobile = (EditText) findViewById(R.id.et_apply_mobile);
        et_user_name = (EditText) findViewById(R.id.et_user_name);
        et_user_mobile = (EditText) findViewById(R.id.et_user_mobile);
        tv_apply_apartment = (TextView) findViewById(R.id.tv_apply_apartment);
        tv_apply_time = (TextView) findViewById(R.id.tv_apply_time);
        tv_car_type = (TextView) findViewById(R.id.tv_car_type);
        tv_apply_accounting_subject = (TextView) findViewById(R.id.tv_apply_accounting_subject);
        et_people_number = (EditText) findViewById(R.id.et_people_number);
        tv_start_time = (TextView) findViewById(R.id.tv_start_time);
        tv_end_time = (TextView) findViewById(R.id.tv_end_time);
        tv_car_range = (TextView) findViewById(R.id.tv_car_range);
        et_car_number = (EditText) findViewById(R.id.et_car_number);
        et_apply_reason = (EditText) findViewById(R.id.et_apply_reason);
        et_client_level = (EditText) findViewById(R.id.et_client_level);
        tv_apply_status = (TextView) findViewById(R.id.tv_apply_status);
        tv_department_advice = (TextView) findViewById(R.id.tv_department_advice);
        tv_officer_manager_advice = (TextView) findViewById(R.id.tv_officer_manager_advice);
        tv_handler_advice = (TextView) findViewById(R.id.tv_handler_advice);
        ll_car_type = (LinearLayout) findViewById(R.id.ll_car_type);
        ll_car_range = (LinearLayout) findViewById(R.id.ll_car_range);
        tv_car_range = (TextView) findViewById(R.id.tv_car_range);
        ll_add_car_user = (LinearLayout) findViewById(R.id.ll_add_car_user);
        ll_is_important_client = (LinearLayout) findViewById(R.id.ll_is_important_client);
        iv_is_important_client = (ImageView) findViewById(R.id.iv_is_important_client);
        tv_add_car_user_add = (TextView) findViewById(R.id.tv_add_car_user_add);
        tv_add_car_user_tips = (TextView) findViewById(R.id.tv_add_car_user_tips);
        ll_car_user = (LinearLayout) findViewById(R.id.ll_car_user);
        tv_save = (TextView) findViewById(R.id.tv_save);
        tv_save.setVisibility(View.INVISIBLE);
        tv_apply_accounting_subject.setOnClickListener(this);
        tv_save.setOnClickListener(this);
        ll_car_type.setOnClickListener(this);
        tv_start_time.setOnClickListener(this);
        tv_end_time.setOnClickListener(this);
        ll_car_range.setOnClickListener(this);
        ll_add_car_user.setOnClickListener(this);
        ll_is_important_client.setOnClickListener(this);
        DisplayUtil.measureWidthAndHeight(ll_car_type);
        DisplayUtil.measureWidthAndHeight(ll_car_range);
        //时间选择器
        startTimeSelector = new TimeSelector(mContext, new TimeSelector.ResultHandler() {
            @Override
            public void handle(String time) {
                if(!"选择用车结束时间".equals(tv_end_time.getText().toString().trim())){
                    if(DateUtils.StrToDate(time).getTime()>=DateUtils.StrToDate(tv_end_time.getText().toString().trim()).getTime()){
                        showToast("出发时间不能晚于到达时间");
                        return;
                    }
                }
                tv_start_time.setText(time);
            }
        }, DateUtils.generalBeginDate(), DateUtils.generalEndDate(50));
        startTimeSelector.setMode(TimeSelector.MODE.YMDHM);
        endTimeSelector = new TimeSelector(mContext, new TimeSelector.ResultHandler() {
            @Override
            public void handle(String time) {
                if(!"选择用车开始时间".equals(tv_start_time.getText().toString().trim())){
                    if(DateUtils.StrToDate(tv_start_time.getText().toString().trim()).getTime()>=DateUtils.StrToDate(time).getTime()){
                        showToast("出发时间不能晚于到达时间");
                        return;
                    }
                }
                tv_end_time.setText(time);
            }
        }, DateUtils.generalBeginDate(), DateUtils.generalEndDate(50));
        endTimeSelector.setMode(TimeSelector.MODE.YMDHM);
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            getAllApplyUseCars = (GetAllApplyUseCars) bundle.getSerializable("getAllApplyUseCars");
            et_user_name.setText(getAllApplyUseCars.getUserName());
            et_user_mobile.setText(getAllApplyUseCars.getUserMobile());
            if(StringUtil.isEmpty(getAllApplyUseCars.getUserName())
                    ||StringUtil.isEmpty(getAllApplyUseCars.getUserMobile())){
                ll_car_user.setVisibility(View.GONE);
                tv_add_car_user_add.setText("＋");
                tv_add_car_user_tips.setText("添加用车人");
                isAddCarUser = false;
            }else{
                ll_car_user.setVisibility(View.VISIBLE);
                tv_add_car_user_add.setText("－");
                tv_add_car_user_tips.setText("删除用车人");
                isAddCarUser = true;
            }
            if(getAllApplyUseCars.getImportantCustomer() == 1){
                isImprotantClient = true;
                iv_is_important_client.setImageResource(R.mipmap.btn_checkbox_pressed);
            }
            et_client_level.setText(getAllApplyUseCars.getCustomerLevelJob());
            if(getAllApplyUseCars.getAuditStatus() == 0){
                //待审批才可以保存操作
                tv_save.setVisibility(View.VISIBLE);
            }else{
                et_apply_mobile.setEnabled(false);
                et_people_number.setEnabled(false);
                et_apply_reason.setEnabled(false);
                et_user_name.setEnabled(false);
                et_user_mobile.setEnabled(false);
                et_client_level.setEnabled(false);
                et_client_level.setHint("");
                et_car_number.setEnabled(false);
                ll_add_car_user.setVisibility(View.GONE);
            }
            mChoseGetAllCarType = new GetAllCarType(getAllApplyUseCars.getTypeCode(),getAllApplyUseCars.getCarType());
            mChoseGetAllCarRange = new GetAllCarRange(getAllApplyUseCars.getRangeCode(),getAllApplyUseCars.getCarRange());
            mChoseCompanyBean = new GetSelectEmployee.CompanyBean();
            mChoseCompanyBean.setUnitName(getAllApplyUseCars.getUnitName());
            mChoseCompanyBean.setCompanyCode(getAllApplyUseCars.getAccountingSubjectCode());
            mChoseCompanyBean.setCompanyName(getAllApplyUseCars.getAccountingSubjectName());
            tv_apply_name.setText(getAllApplyUseCars.getApplicatName());
            et_apply_mobile.setText(getAllApplyUseCars.getMobile());
            tv_apply_apartment.setText(StringUtil.isEmpty(getAllApplyUseCars.getUnitName())?"无":getAllApplyUseCars.getUnitName());
            tv_apply_accounting_subject.setText(StringUtil.isEmpty(getAllApplyUseCars.getAccountingSubjectName())?
                    "无":getAllApplyUseCars.getAccountingSubjectName());
            tv_apply_time.setText(DateUtils.DateToDayStr(new Date(getAllApplyUseCars.getApplicationDate())));
            tv_car_type.setText(getAllApplyUseCars.getCarType());
            et_car_number.setText(""+getAllApplyUseCars.getCarNumber());
            et_people_number.setText(getAllApplyUseCars.getNumberPeople());
            tv_start_time.setText(DateUtils.DateToMinuteStr(new Date(getAllApplyUseCars.getStartTime())));
            tv_end_time.setText(DateUtils.DateToMinuteStr(new Date(getAllApplyUseCars.getEndTime())));
            tv_car_range.setText(getAllApplyUseCars.getCarRange());
            et_apply_reason.setText(getAllApplyUseCars.getApplyReason());

            tv_department_advice.setText(getAllApplyUseCars.getUnitAdvice());
            tv_officer_manager_advice.setText(getAllApplyUseCars.getGmAdvice());
            tv_handler_advice.setText(getAllApplyUseCars.getConductAdvice());

            //0:待审批  1：已审批  2：审批未通过
            if(getAllApplyUseCars.getCarAuditEntities()!=null && getAllApplyUseCars.getCarAuditEntities().size()>0){
                if(getAllApplyUseCars.getCarAuditEntities().size()==1){
                    String departmentAdvice = getAllApplyUseCars.getCarAuditEntities().get(0).getAuditAdvice();
                    int departmentStatus = getAllApplyUseCars.getCarAuditEntities().get(0).getAuditStatus();
                    if(departmentStatus == 2){
                        tv_department_advice.setText(DateUtils.DateToMinuteStr(new Date(getAllApplyUseCars.getCarAuditEntities().get(0).getAuditTime()))
                                + "  " + getAllApplyUseCars.getCarAuditEntities().get(0).getAuditorName()
                                + "  拒绝" + ("拒绝".equals(departmentAdvice)?"":"  " + departmentAdvice));
                    }else{
                        tv_department_advice.setText(DateUtils.DateToMinuteStr(new Date(getAllApplyUseCars.getCarAuditEntities().get(0).getAuditTime()))
                                + "  " + getAllApplyUseCars.getCarAuditEntities().get(0).getAuditorName()
                                + "  通过" + ("通过".equals(departmentAdvice)?"":"  " + departmentAdvice));
                    }
                }else if(getAllApplyUseCars.getCarAuditEntities().size()==2){
                    String departmentAdvice = getAllApplyUseCars.getCarAuditEntities().get(0).getAuditAdvice();
                    int departmentStatus = getAllApplyUseCars.getCarAuditEntities().get(0).getAuditStatus();
                    if(departmentStatus == 2){
                        tv_department_advice.setText(DateUtils.DateToMinuteStr(new Date(getAllApplyUseCars.getCarAuditEntities().get(0).getAuditTime()))
                                + "  " + getAllApplyUseCars.getCarAuditEntities().get(0).getAuditorName()
                                + "  拒绝" + ("拒绝".equals(departmentAdvice)?"":"  " + departmentAdvice));
                    }else{
                        tv_department_advice.setText(DateUtils.DateToMinuteStr(new Date(getAllApplyUseCars.getCarAuditEntities().get(0).getAuditTime()))
                                + "  " + getAllApplyUseCars.getCarAuditEntities().get(0).getAuditorName()
                                + "  通过" + ("通过".equals(departmentAdvice)?"":"  " + departmentAdvice));
                    }
                    String managerAdvice = getAllApplyUseCars.getCarAuditEntities().get(1).getAuditAdvice();
                    int managerStatus = getAllApplyUseCars.getCarAuditEntities().get(1).getAuditStatus();
                    if(managerStatus == 2){
                        tv_officer_manager_advice.setText(DateUtils.DateToMinuteStr(new Date(getAllApplyUseCars.getCarAuditEntities().get(1).getAuditTime()))
                                + "  " + getAllApplyUseCars.getCarAuditEntities().get(1).getAuditorName()
                                + "  拒绝" + ("拒绝".equals(managerAdvice)?"":"  " + managerAdvice));
                    }else{
                        tv_officer_manager_advice.setText(DateUtils.DateToMinuteStr(new Date(getAllApplyUseCars.getCarAuditEntities().get(1).getAuditTime()))
                                + "  " + getAllApplyUseCars.getCarAuditEntities().get(1).getAuditorName()
                                + "  通过" + ("通过".equals(managerAdvice)?"":"  " + managerAdvice));
                    }
                }else{
                    String departmentAdvice = getAllApplyUseCars.getCarAuditEntities().get(0).getAuditAdvice();
                    int departmentStatus = getAllApplyUseCars.getCarAuditEntities().get(0).getAuditStatus();
                    if(departmentStatus == 2){
                        tv_department_advice.setText(DateUtils.DateToMinuteStr(new Date(getAllApplyUseCars.getCarAuditEntities().get(0).getAuditTime()))
                                + "  " + getAllApplyUseCars.getCarAuditEntities().get(0).getAuditorName()
                                + "  拒绝" + ("拒绝".equals(departmentAdvice)?"":"  " + departmentAdvice));
                    }else{
                        tv_department_advice.setText(DateUtils.DateToMinuteStr(new Date(getAllApplyUseCars.getCarAuditEntities().get(0).getAuditTime()))
                                + "  " + getAllApplyUseCars.getCarAuditEntities().get(0).getAuditorName()
                                + "  通过" + ("通过".equals(departmentAdvice)?"":"  " + departmentAdvice));
                    }
                    String managerAdvice = getAllApplyUseCars.getCarAuditEntities().get(1).getAuditAdvice();
                    int managerStatus = getAllApplyUseCars.getCarAuditEntities().get(1).getAuditStatus();
                    if(managerStatus == 2){
                        tv_officer_manager_advice.setText(DateUtils.DateToMinuteStr(new Date(getAllApplyUseCars.getCarAuditEntities().get(1).getAuditTime()))
                                + "  " + getAllApplyUseCars.getCarAuditEntities().get(1).getAuditorName()
                                + "  拒绝" + ("拒绝".equals(managerAdvice)?"":"  " + managerAdvice));
                    }else{
                        tv_officer_manager_advice.setText(DateUtils.DateToMinuteStr(new Date(getAllApplyUseCars.getCarAuditEntities().get(1).getAuditTime()))
                                + "  " + getAllApplyUseCars.getCarAuditEntities().get(1).getAuditorName()
                                + "  通过" + ("通过".equals(managerAdvice)?"":"  " + managerAdvice));
                    }
                    String handlerAdvice = getAllApplyUseCars.getCarAuditEntities().get(2).getAuditAdvice();
                    int handlerStatus = getAllApplyUseCars.getCarAuditEntities().get(2).getAuditStatus();
                    if(handlerStatus == 2){
                        tv_handler_advice.setText(DateUtils.DateToMinuteStr(new Date(getAllApplyUseCars.getCarAuditEntities().get(2).getAuditTime()))
                                + "  " + getAllApplyUseCars.getCarAuditEntities().get(2).getAuditorName()
                                + "  拒绝" + ("拒绝".equals(handlerAdvice)?"":"  " + handlerAdvice));
                    }else{
                        tv_handler_advice.setText(DateUtils.DateToMinuteStr(new Date(getAllApplyUseCars.getCarAuditEntities().get(2).getAuditTime()))
                                + "  " + getAllApplyUseCars.getCarAuditEntities().get(2).getAuditorName()
                                + "  通过" + ("通过".equals(handlerAdvice)?"":"  " + handlerAdvice));
                    }
                }
            }
            //0：待审批 11:审批中（部门审批同意） 12：审批拒绝（部门审批拒绝） 21：待办理（总经办审批同意） 22：审批拒绝（总经办审批拒绝） 3/31：已办理  32：办理拒绝
            switch (getAllApplyUseCars.getAuditStatus()){
                case 0:
                    tv_apply_status.setText("待审批");
                    break;
                case 11:
                    tv_apply_status.setText("审批中");
                    break;
                case 12:
                    tv_apply_status.setText("审批拒绝");
                    break;
                case 21:
                    tv_apply_status.setText("待办理");
                    break;
                case 22:
                    tv_apply_status.setText("审批拒绝");
                    break;
                case 3:
                    tv_apply_status.setText("已办理");
                    break;
                case 31:
                    tv_apply_status.setText("已办理");
                    break;
                case 32:
                    tv_apply_status.setText("办理拒绝");
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.tv_apply_accounting_subject:
                if(getAllApplyUseCars.getAuditStatus() != 0){
                    return;
                }
                //selectEmployee();
                break;
            case R.id.ll_add_car_user:
                if(!isAddCarUser){
                    ll_car_user.setVisibility(View.VISIBLE);
                    tv_add_car_user_add.setText("－");
                    tv_add_car_user_tips.setText("删除用车人");
                }else{
                    ll_car_user.setVisibility(View.GONE);
                    tv_add_car_user_add.setText("＋");
                    tv_add_car_user_tips.setText("添加用车人");
                    et_user_name.setText("");
                    et_user_mobile.setText("");
                }
                isAddCarUser = !isAddCarUser;
                break;
            case R.id.ll_is_important_client:
                //重要客户
                if(getAllApplyUseCars == null || getAllApplyUseCars.getAuditStatus() != 0){
                    return;
                }
                if(!isImprotantClient){
                    iv_is_important_client.setImageResource(R.mipmap.btn_checkbox_pressed);
                }else{
                    iv_is_important_client.setImageResource(R.mipmap.btn_checkbox_nor);
                }
                isImprotantClient = !isImprotantClient;
                break;
            case R.id.tv_save:
                editApplyCar();
                break;
            case R.id.ll_car_type:
                //用车类型
                if(getAllApplyUseCars == null || getAllApplyUseCars.getAuditStatus() != 0){
                    return;
                }
                if(mGetAllCarTypeList.size() == 0){
                    getAllCarType();
                }else{
                    carTypePopupWindow = new CommonPopupWindow.Builder(mContext)
                            .setView(R.layout.popu_businesscar_apply_car_type)
                            .setBackGroundLevel(0.9f)
                            .setOutsideTouchable(true)
                            .setWidthAndHeight(ll_car_type.getWidth()-DisplayUtil.dpTopx(mContext,10), ViewGroup.LayoutParams.WRAP_CONTENT)
                            .setViewOnclickListener(this)
                            .create();
                    carTypePopupWindow.showAsDropDown(tv_car_type);
                }
                break;
            case R.id.tv_start_time:
                //开始时间
                if(getAllApplyUseCars == null || getAllApplyUseCars.getAuditStatus() != 0){
                    return;
                }
                startTimeSelector.show();
                break;
            case R.id.tv_end_time:
                //结束时间
                if(getAllApplyUseCars == null || getAllApplyUseCars.getAuditStatus() != 0){
                    return;
                }
                endTimeSelector.show();
                break;
            case R.id.ll_car_range:
                //用车范围
                if(getAllApplyUseCars == null || getAllApplyUseCars.getAuditStatus() != 0){
                    return;
                }
                if(mGetAllCarRangeList.size() == 0){
                    getAllCarRange();
                }else{
                    carRangePopupWindow = new CommonPopupWindow.Builder(mContext)
                            .setView(R.layout.popu_businesscar_apply_car_range)
                            .setBackGroundLevel(0.9f)
                            .setOutsideTouchable(true)
                            .setWidthAndHeight(ll_car_range.getWidth()-DisplayUtil.dpTopx(mContext,10), ViewGroup.LayoutParams.WRAP_CONTENT)
                            .setViewOnclickListener(this)
                            .create();
                    carRangePopupWindow.showAsDropDown(tv_car_range);
                }
                break;
        }
    }

    /**
     * 查询已授权人员列表（默认第一个自己）
     */
    private void selectEmployee() {
        User user = AppUtils.getUserInfo(mContext);
        Map<String,Object> params = new HashMap<>();
        params.put("ciphertext","test");
        params.put("employeeCode", user.getEmployeeCode());
        params.put("loginType", Constant.APP_TYPE);

        showLoading();
        RetrofitFactory.getInstance().createApi(OrderApi.class).selectEmployee(params)
                .enqueue(new CallBack<List<GetSelectEmployee>>() {
                    @Override
                    public void success(List<GetSelectEmployee> getSelectEmployeeList) {
                        dismissLoading();
                        if(getSelectEmployeeList.size() > 0){
                            if(getSelectEmployeeList.get(0).getCompany().size() > 0){
                                showPopupWindows(getSelectEmployeeList.get(0).getCompany());
                            }else{
                                showToast("暂无数据");
                            }
                        }else{
                            showToast("暂无数据");
                        }
                    }

                    @Override
                    public void fail(String code) {
                        dismissLoading();
                    }
                });
    }

    /**
     * 获取用车类型列表
     */
    private void getAllCarType() {
        Map<String,Object> params = new HashMap<>();

        showLoading();
        RetrofitFactory.getInstance().createApi(CarApi.class).getAllCarType(params)
                .enqueue(new CallBack<List<GetAllCarType>>() {
                    @Override
                    public void success(List<GetAllCarType> getAllCarTypeList) {
                        dismissLoading();
                        if(getAllCarTypeList.size() == 0){
                            showToast("列表数据为空");
                            return;
                        }
                        mGetAllCarTypeList.clear();
                        mGetAllCarTypeList.addAll(getAllCarTypeList);
                        carTypePopupWindow = new CommonPopupWindow.Builder(mContext)
                                .setView(R.layout.popu_businesscar_apply_car_type)
                                .setBackGroundLevel(0.9f)
                                .setOutsideTouchable(true)
                                .setWidthAndHeight(ll_car_type.getWidth()-DisplayUtil.dpTopx(mContext,10),
                                        ViewGroup.LayoutParams.WRAP_CONTENT)
                                .setViewOnclickListener(BusinessCarApplyDetailActivity.this)
                                .create();
                        carTypePopupWindow.showAsDropDown(tv_car_type);
                    }

                    @Override
                    public void fail(String code) {
                        dismissLoading();
                    }
                });
    }

    /**
     * 获取用车范围列表
     */
    private void getAllCarRange() {
        Map<String,Object> params = new HashMap<>();

        showLoading();
        RetrofitFactory.getInstance().createApi(CarApi.class).getAllCarRange(params)
                .enqueue(new CallBack<List<GetAllCarRange>>() {
                    @Override
                    public void success(List<GetAllCarRange> getAllCarRangeList) {
                        dismissLoading();
                        if(getAllCarRangeList.size() == 0){
                            showToast("列表数据为空");
                            return;
                        }
                        mGetAllCarRangeList.clear();
                        mGetAllCarRangeList.addAll(getAllCarRangeList);
                        carRangePopupWindow = new CommonPopupWindow.Builder(mContext)
                                .setView(R.layout.popu_businesscar_apply_car_range)
                                .setBackGroundLevel(0.9f)
                                .setOutsideTouchable(true)
                                .setWidthAndHeight(ll_car_range.getWidth()-DisplayUtil.dpTopx(mContext,10),
                                        ViewGroup.LayoutParams.WRAP_CONTENT)
                                .setViewOnclickListener(BusinessCarApplyDetailActivity.this)
                                .create();
                        carRangePopupWindow.showAsDropDown(tv_car_range);
                    }

                    @Override
                    public void fail(String code) {
                        dismissLoading();
                    }
                });
    }

    /**
     * 申请用车
     */
    private void editApplyCar() {
        String mobile = et_apply_mobile.getText().toString().trim();
        String carType = tv_car_type.getText().toString().trim();
        String peopleNumber = et_people_number.getText().toString().trim();
        String startTime = tv_start_time.getText().toString().trim();
        String endTime = tv_end_time.getText().toString().trim();
        String carRange = tv_car_range.getText().toString().trim();
        String applyReason = et_apply_reason.getText().toString().trim();
        String userName = et_user_name.getText().toString().trim();
        String userMobile = et_user_mobile.getText().toString().trim();
        String carNumber = et_car_number.getText().toString().trim();
        String importantCustomer = isImprotantClient?"1":"0";
        String customerLevelJob = et_client_level.getText().toString().trim();
        if(getAllApplyUseCars == null){
            showToast("获取数据失败");
            return;
        }
        if(StringUtil.isEmpty(mobile)){
            showToast("请填写联系方式");
            return;
        }
        if(!StringUtil.isEmpty(mobile)&&mobile.length()!=11){
            showToast("请填写正确的联系方式");
            return;
        }
        if(StringUtil.isEmpty(userName)){
            userName = "";
        }
        if(StringUtil.isEmpty(userMobile)){
            userMobile = "";
        }
        if(StringUtil.isEmpty(userName)&&!StringUtil.isEmpty(userMobile)){
            showToast("请填写用车人姓名");
            return;
        }
        if(!StringUtil.isEmpty(userName)&&StringUtil.isEmpty(userMobile)){
            showToast("请填写用车人电话");
            return;
        }
        if(!StringUtil.isEmpty(userMobile)&&userMobile.length()!=11){
            showToast("请填写正确的用车人电话");
            return;
        }
        if(isImprotantClient || !StringUtil.isEmpty(customerLevelJob)){
            if(StringUtil.isEmpty(userName) || StringUtil.isEmpty(userMobile)){
                showToast("请填写用车人姓名与电话");
                return;
            }
        }
        if(null == mChoseCompanyBean){
            showToast("请选择核算主体");
            return;
        }
        if(mChoseGetAllCarType == null){
            showToast("请选择用车类型");
            return;
        }
        if(StringUtil.isEmpty(carNumber)){
            showToast("请填写用车数量");
            return;
        }
        if(StringUtil.isEmpty(peopleNumber)){
            showToast("请填写用车人数");
            return;
        }
        if(mChoseGetAllCarRange == null){
            showToast("请选择用车范围");
            return;
        }
        if(StringUtil.isEmpty(applyReason)){
            showToast("请填写申请事由");
            return;
        }
        User user = AppUtils.getUserInfo(mContext);
        Map<String,Object> params = new HashMap<>();
        params.put("id", getAllApplyUseCars.getId());
        params.put("mobile", mobile);
        params.put("userName", userName);
        params.put("userMobile", userMobile);
        params.put("carNumber", carNumber);
        params.put("importantCustomer", importantCustomer);
        params.put("customerLevelJob", customerLevelJob);
        params.put("unitCode", null==mChoseCompanyBean.getCompanyCode()?"":mChoseCompanyBean.getCompanyCode());
        params.put("unitName", null==mChoseCompanyBean.getUnitName()?"":mChoseCompanyBean.getUnitName());
        params.put("accountingSubjectCode", null==mChoseCompanyBean.getCompanyCode()?"":mChoseCompanyBean.getCompanyCode());
        params.put("accountingSubjectName", null==mChoseCompanyBean.getCompanyName()?"":mChoseCompanyBean.getCompanyName());
        params.put("carType", mChoseGetAllCarType.getTypeCode());
        params.put("carRange", mChoseGetAllCarRange.getRangeCode());
        params.put("numberPeople", peopleNumber);
        params.put("startTime", DateUtils.StrToDate(startTime).getTime());
        params.put("endTime", DateUtils.StrToDate(endTime).getTime());
        params.put("applyReason", applyReason);

        showLoading();
        RetrofitFactory.getInstance().createApi(CarApi.class).editApplyCar(params)
                .enqueue(new CallBack<Object>() {
                    @Override
                    public void success(Object object) {
                        dismissLoading();
                        showToast("保存成功");
                        EventBus.getDefault().post(new Events.BusinessCarApplyChange());
                        finish();
                    }

                    @Override
                    public void fail(String code) {
                        dismissLoading();
                    }
                });
    }

    @Override
    public void getChildView(View view, int layoutResId) {
        if(layoutResId == R.layout.popu_businesscar_apply_car_type){
            ListView lv_car_type = (ListView) view.findViewById(R.id.lv_car_type);
            mBusinessCarApplyPopuCarTypeAdapter = new BusinessCarApplyPopuCarTypeAdapter(this,mGetAllCarTypeList);
            lv_car_type.setAdapter(mBusinessCarApplyPopuCarTypeAdapter);
            lv_car_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mChoseGetAllCarType = mGetAllCarTypeList.get(position);
                    tv_car_type.setText(mChoseGetAllCarType.getTypeName());
                    carTypePopupWindow.dismiss();
                }
            });
        }else if(layoutResId == R.layout.popu_businesscar_apply_car_range){
            ListView lv_car_range = (ListView) view.findViewById(R.id.lv_car_range);
            mBusinessCarApplyPopuCarRangeAdapter = new BusinessCarApplyPopuCarRangeAdapter(this,mGetAllCarRangeList);
            lv_car_range.setAdapter(mBusinessCarApplyPopuCarRangeAdapter);
            lv_car_range.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mChoseGetAllCarRange = mGetAllCarRangeList.get(position);
                    tv_car_range.setText(mChoseGetAllCarRange.getRangeName());
                    carRangePopupWindow.dismiss();
                }
            });
        }
    }

    /**
     * 显示弹窗（选择核算主体界面）
     * @param
     */
    private void showPopupWindows(final List<GetSelectEmployee.CompanyBean> datalist) {
        //构建一个popupwindow的布局
        popupView = getLayoutInflater().inflate(R.layout.popupwindow_select_person, null);
        final PopupWinBusinessCarListAdapter popAdapter = new PopupWinBusinessCarListAdapter(this,datalist);
        //弹出类型选择窗口时，默认设置第一个为选择项
        for(int i = 0 ;i < datalist.size();i++){
            if(i == 0){
                datalist.get(i).setCheck(true);
            }else{
                datalist.get(i).setCheck(false);
            }
        }
        ListView listView = (ListView) popupView.findViewById(R.id.popupwin_list);
        listView.setAdapter(popAdapter);
        //类型列表点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for(int i=0;i<popAdapter.getCount();i++) {
                    datalist.get(i).setCheck(false);
                }
                datalist.get(position).setCheck(true);
                popAdapter.notifyDataSetChanged();
            }
        });

        TextView popuptitle = (TextView) popupView.findViewById(R.id.popup_title);
        //核算主体
        popupView.findViewById(R.id.select_person).setVisibility(View.GONE);
        popupView.findViewById(R.id.popupwin_list).setVisibility(View.VISIBLE);
        popuptitle.setText("选择核算主体");
        TextView tv_ok = (TextView) popupView.findViewById(R.id.select_person_ok);
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                //遍历出选择的核算主体
                for(int i = 0 ; i < datalist.size() ; i++){
                    if(datalist.get(i).isCheck()){
                        mChoseCompanyBean = datalist.get(i);
                        tv_apply_accounting_subject.setText(mChoseCompanyBean.getCompanyName());
                        tv_apply_apartment.setText(mChoseCompanyBean.getUnitName());
                        break;
                    }
                }

            }
        });

        popupView.findViewById(R.id.select_person_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });

        //创建PopupWindow对象，指定宽度和高度
        int pop_width = (int)(AppUtils.getDeviceWidth(this)*0.8);
        int pop_height = (int)(AppUtils.getDeviceWidth(this)*1.1);
        window = new PopupWindow(popupView, pop_width, pop_height);
//                window.setAnimationStyle(R.style.popup_window_anim);
        //设置背景颜色
        window.setBackgroundDrawable(new ColorDrawable(90000000));
        //设置可以获取焦点
        window.setFocusable(true);
        // 设置可以触摸弹出框以外的区域
        window.setOutsideTouchable(true);
        // TODO：更新popupwindow的状态
        window.update();
        popOutShadow(window);
        window.showAtLocation(ll_container, Gravity.CENTER,0,0);
    }

    /**
     * 弹窗外阴影
     * @param popupWindow
     */
    private void popOutShadow(PopupWindow popupWindow) {
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.alpha = 0.3f;//设置阴影透明度
        getWindow().setAttributes(lp);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            /**当关闭弹窗给筛选条件赋值*/
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
    }

}
