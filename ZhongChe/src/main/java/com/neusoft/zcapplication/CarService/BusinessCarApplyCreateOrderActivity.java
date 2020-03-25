package com.neusoft.zcapplication.CarService;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
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

import com.google.gson.internal.LinkedTreeMap;
import com.neusoft.zcapplication.Bean.FlightItem;
import com.neusoft.zcapplication.Bean.PersonItem;
import com.neusoft.zcapplication.Bean.User;
import com.neusoft.zcapplication.Constant.Constant;
import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.api.CarApi;
import com.neusoft.zcapplication.api.OrderApi;
import com.neusoft.zcapplication.approval.ApprovalActivity;
import com.neusoft.zcapplication.base.BaseActivity;
import com.neusoft.zcapplication.entity.GetAllCarRange;
import com.neusoft.zcapplication.entity.GetAllCarType;
import com.neusoft.zcapplication.entity.GetCredentialsData;
import com.neusoft.zcapplication.entity.GetSelectEmployee;
import com.neusoft.zcapplication.event.Events;
import com.neusoft.zcapplication.http.NetWorkRequest;
import com.neusoft.zcapplication.http.RequestCallback;
import com.neusoft.zcapplication.http.RequestUtil;
import com.neusoft.zcapplication.http.URL;
import com.neusoft.zcapplication.http.now.CallBack;
import com.neusoft.zcapplication.http.now.RetrofitFactory;
import com.neusoft.zcapplication.tools.AlertUtil;
import com.neusoft.zcapplication.tools.AppUtils;
import com.neusoft.zcapplication.tools.DateUtils;
import com.neusoft.zcapplication.tools.DisplayUtil;
import com.neusoft.zcapplication.tools.LogUtil;
import com.neusoft.zcapplication.tools.StringUtil;
import com.neusoft.zcapplication.tools.ToastUtil;
import com.neusoft.zcapplication.widget.PopupWinListAdapter;
import com.neusoft.zcapplication.widget.TimeSelector;
import com.neusoft.zcapplication.widget.popuwindow.CommonPopupWindow;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * 创建公务用车
 */
public class BusinessCarApplyCreateOrderActivity extends BaseActivity implements View.OnClickListener,
        CommonPopupWindow.ViewInterface, RequestCallback {

    private TextView tv_apply_name, tv_apply_apartment,
            tv_car_type, tv_start_time, tv_end_time, tv_car_range,
            tv_add_car_user_add, tv_add_car_user_tips, tv_apply_accounting_subject;
    private EditText et_apply_mobile, et_user_name, et_user_mobile, et_car_number, et_people_number,
            et_apply_reason, et_client_level;
    private LinearLayout ll_container, ll_add_car_user, ll_car_type, ll_car_range, ll_car_user, ll_is_important_client;
    private ImageView iv_is_important_client;

    private CommonPopupWindow carTypePopupWindow, carRangePopupWindow;
    private TimeSelector startTimeSelector, endTimeSelector;//时间选择器

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
    private boolean isFirstLoadData;

    private Map<String, Object> selectAccountEntity;//选择的核算主体
    private Map<String, Object> selectunitEntity;//选择的部门
    private Map<String, Object> auditorEntity;//选择的审批人

    private List<Map<String, Object>> accountEntityList;//核算主体列表
    private List<Map<String, Object>> unitEntityList;//部门主体列表

    private List<Map<String, Object>> auditorList;//审批人列表

    private Map<String, Object> currentPerson;

    private String selectCompanyStr = "";
    private String selectUnitStr = "";

    private String selectCompanyCode = "";
    private String selectUnitCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_car_apply_create_order);
        initView();
        initData();
    }

    private void initView() {
        AppUtils.setStateBar(BusinessCarApplyCreateOrderActivity.this, findViewById(R.id.frg_status_bar));
        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.tv_submit).setOnClickListener(this);
        findViewById(R.id.tv_reset).setOnClickListener(this);
        ll_container = (LinearLayout) findViewById(R.id.ll_container);
        tv_apply_name = (TextView) findViewById(R.id.tv_apply_name);
        et_apply_mobile = (EditText) findViewById(R.id.et_apply_mobile);
        et_user_name = (EditText) findViewById(R.id.et_user_name);
        et_user_mobile = (EditText) findViewById(R.id.et_user_mobile);
        tv_apply_accounting_subject = (TextView) findViewById(R.id.tv_apply_accounting_subject);
        tv_apply_apartment = (TextView) findViewById(R.id.tv_apply_apartment);
        ll_car_type = (LinearLayout) findViewById(R.id.ll_car_type);
        tv_car_type = (TextView) findViewById(R.id.tv_car_type);
        tv_start_time = (TextView) findViewById(R.id.tv_start_time);
        tv_end_time = (TextView) findViewById(R.id.tv_end_time);
        ll_car_range = (LinearLayout) findViewById(R.id.ll_car_range);
        tv_car_range = (TextView) findViewById(R.id.tv_car_range);
        et_car_number = (EditText) findViewById(R.id.et_car_number);
        et_people_number = (EditText) findViewById(R.id.et_people_number);
        et_apply_reason = (EditText) findViewById(R.id.et_apply_reason);
        et_client_level = (EditText) findViewById(R.id.et_client_level);
        ll_add_car_user = (LinearLayout) findViewById(R.id.ll_add_car_user);
        ll_car_user = (LinearLayout) findViewById(R.id.ll_car_user);
        ll_is_important_client = (LinearLayout) findViewById(R.id.ll_is_important_client);
        tv_add_car_user_add = (TextView) findViewById(R.id.tv_add_car_user_add);
        tv_add_car_user_tips = (TextView) findViewById(R.id.tv_add_car_user_tips);
        iv_is_important_client = (ImageView) findViewById(R.id.iv_is_important_client);
        tv_apply_accounting_subject.setOnClickListener(this);
        tv_apply_apartment.setOnClickListener(this);
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
                if (!"选择用车结束时间".equals(tv_end_time.getText().toString().trim())) {
                    if (DateUtils.StrToDate(time).getTime() >= DateUtils.StrToDate(tv_end_time.getText().toString().trim()).getTime()) {
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
                if (!"选择用车开始时间".equals(tv_start_time.getText().toString().trim())) {
                    if (DateUtils.StrToDate(tv_start_time.getText().toString().trim()).getTime() >= DateUtils.StrToDate(time).getTime()) {
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
        User userInfo = AppUtils.getUserInfo(mContext);
        tv_apply_name.setText(userInfo.getEmployeeName());
        et_apply_mobile.setText(userInfo.getMobil());
        isFirstLoadData = true;
        selectEmployee();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.tv_apply_accounting_subject:
//                selectEmployee();
                showPopupWindows(3, accountEntityList);
                break;
            case R.id.tv_apply_apartment:
                showPopupWindows(7, unitEntityList);
                break;
            case R.id.ll_add_car_user:
                if (!isAddCarUser) {
                    ll_car_user.setVisibility(View.VISIBLE);
                    tv_add_car_user_add.setText("－");
                    tv_add_car_user_tips.setText("删除用车人");
                } else {
                    ll_car_user.setVisibility(View.GONE);
                    tv_add_car_user_add.setText("＋");
                    tv_add_car_user_tips.setText("添加用车人");
                    et_user_name.setText("");
                    et_user_mobile.setText("");
                }
                isAddCarUser = !isAddCarUser;
                break;
            case R.id.ll_is_important_client:
                if (!isImprotantClient) {
                    iv_is_important_client.setImageResource(R.mipmap.btn_checkbox_pressed);
                } else {
                    iv_is_important_client.setImageResource(R.mipmap.btn_checkbox_nor);
                }
                isImprotantClient = !isImprotantClient;
                break;
            case R.id.ll_car_type:
                //用车类型
                if (mGetAllCarTypeList.size() == 0) {
                    getAllCarType();
                } else {
                    carTypePopupWindow = new CommonPopupWindow.Builder(mContext)
                            .setView(R.layout.popu_businesscar_apply_car_type)
                            .setBackGroundLevel(0.9f)
                            .setOutsideTouchable(true)
                            .setWidthAndHeight(ll_car_type.getWidth() - DisplayUtil.dpTopx(mContext, 10), ViewGroup.LayoutParams.WRAP_CONTENT)
                            .setViewOnclickListener(this)
                            .create();
                    carTypePopupWindow.showAsDropDown(tv_car_type);
                }
                break;
            case R.id.tv_start_time:
                //开始时间
                startTimeSelector.show();
                break;
            case R.id.tv_end_time:
                //结束时间
                endTimeSelector.show();
                break;
            case R.id.ll_car_range:
                //用车范围
                if (mGetAllCarRangeList.size() == 0) {
                    getAllCarRange();
                } else {
                    carRangePopupWindow = new CommonPopupWindow.Builder(mContext)
                            .setView(R.layout.popu_businesscar_apply_car_range)
                            .setBackGroundLevel(0.9f)
                            .setOutsideTouchable(true)
                            .setWidthAndHeight(ll_car_range.getWidth() - DisplayUtil.dpTopx(mContext, 10), ViewGroup.LayoutParams.WRAP_CONTENT)
                            .setViewOnclickListener(this)
                            .create();
                    carRangePopupWindow.showAsDropDown(tv_car_range);
                }
                break;
            case R.id.tv_submit:
                //提交
//                applyCar();
                createApplyCarRequest();
                break;
            case R.id.tv_reset:
                //重置
                et_people_number.setText("");
                et_apply_reason.setText("");
                et_user_name.setText("");
                et_user_mobile.setText("");
                et_car_number.setText("");
                tv_start_time.setText("选择用车开始时间");
                tv_end_time.setText("选择用车结束时间");
                break;

        }
    }

    /**
     * 查询已授权人员列表（默认第一个自己）
     */
    private void selectEmployee() {
//        User user = AppUtils.getUserInfo(mContext);
//        Map<String,Object> params = new HashMap<>();
//        params.put("ciphertext","test");
//        params.put("employeeCode", user.getEmployeeCode());
//        params.put("loginType", Constant.APP_TYPE);

        showLoading();

        User user = AppUtils.getUserInfo(this);
        String employeeCode = user.getEmployeeCode();
//        showLoading("正在获取数据",true);
        Map<String, Object> params = new HashMap<>();
        params.put("ciphertext", "test");
        params.put("employeeCode", employeeCode);
        params.put("loginType", URL.LOGIN_TYPE);

        Retrofit retrofit = new RequestUtil().getRequestClient(URL.ZHONGCHE_93);
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        Call<Map<String, Object>> call = request.getPersonList(params);
        new RequestUtil().requestDataNoLoading(call, this, 3, this);

//        RetrofitFactory.getInstance().createApi(OrderApi.class).selectEmployee(params)
//                .enqueue(new CallBack<List<GetSelectEmployee>>() {
//                    @Override
//                    public void success(List<GetSelectEmployee> getSelectEmployeeList) {
//                        dismissLoading();
//                        if(getSelectEmployeeList.size() > 0){
//                            if(getSelectEmployeeList.get(0).getCompany().size() > 0){
//                                if(isFirstLoadData){
//                                    if(getSelectEmployeeList.get(0).getCompany().size() == 1){
//                                        mChoseCompanyBean = getSelectEmployeeList.get(0).getCompany().get(0);
//                                        tv_apply_accounting_subject.setText(mChoseCompanyBean.getCompanyName());
//                                        tv_apply_apartment.setText(mChoseCompanyBean.getUnitName());
//                                    }else{
//                                        showPopupWindows(getSelectEmployeeList.get(0).getCompany());
//                                    }
//                                }else{
//                                    showPopupWindows(getSelectEmployeeList.get(0).getCompany());
//                                }
//                            }else{
//                                showToast("暂无数据");
//                            }
//                        }else{
//                            showToast("暂无数据");
//                        }
//                        isFirstLoadData = false;
//                    }
//
//                    @Override
//                    public void fail(String code) {
//                        dismissLoading();
//                        isFirstLoadData = false;
//                    }
//                });
    }

    /**
     *
     */
    private void selectAuditor() {
        showLoading();
        User user = AppUtils.getUserInfo(this);

    }

    @Override
    public void requestSuccess(Object map, int type) {
        dismissLoading();
        Map<String, Object> result = (Map<String, Object>) map;
        if (null != result) {
            String code = null == result.get("code") ? "" : result.get("code").toString();
            String codeMsg = null == result.get("codeMsg") ? "" : result.get("codeMsg").toString();
            if (code.equals("00000")) {
                currentPerson = ((List<Map<String, Object>>) result.get("data")).get(0);
                List<Map<String, Object>> companyList = (List<Map<String, Object>>) currentPerson.get("company");
                accountEntityList = companyList;

                if (companyList.size() == 0) {
                    ToastUtil.toast("核算主体列表为空");
                    return;
                }
                for (int i = 0; i < accountEntityList.size(); i++) {
                    Map<String, Object> company = companyList.get(i);
                    if (i == 0) {
                        company.put("isCheck", "true");
                        selectAccountEntity = company;
                    } else {
                        company.put("isCheck", "false");
                    }
                }
                Map<String, Object> firstCompany = companyList.get(0);

                tv_apply_accounting_subject.setText(firstCompany.get("companyName") + "");
                selectCompanyStr = tv_apply_accounting_subject.getText().toString();
                selectCompanyCode = firstCompany.get("companyCode") + "";

                unitEntityList = new ArrayList<>();
                for (int i = 0; i < (((List<Map<String, Object>>) firstCompany.get("unitInfo"))).size(); i++) {
                    unitEntityList.add((((List<Map<String, Object>>) firstCompany.get("unitInfo"))).get(i));
                }
                if (((List<Map<String, Object>>) firstCompany.get("unitInfo")).size() == 0) {
                    ToastUtil.toast("部门列表为空");
                    return;
                }
                for (int i = 0; i < unitEntityList.size(); i++) {
                    Map<String, Object> unit = unitEntityList.get(i);
                    if (i == 0) {
                        unit.put("isCheck", "true");
                    } else {
                        unit.put("isCheck", "false");
                    }

                }


                selectunitEntity = unitEntityList.get(0);
//                if (((List<Map<String, Object>>) firstCompany.get("unitInfo")) != null || ((List<Map<String, Object>>) firstCompany.get("unitInfo")).size() > 0) {
//                    Map<String, Object> firstUnit = ((List<Map<String, Object>>) firstCompany.get("unitInfo")).get(0);
//
//                    tv_depart.setText(firstUnit.get("unitName") + "");
//                }
                tv_apply_apartment.setText(selectunitEntity.get("unitName") + "");
                selectUnitStr = tv_apply_apartment.getText().toString();
                selectUnitCode = selectunitEntity.get("unitCode") + "";
            } else {
                ToastUtil.toastError(this);
            }
        } else {
            //请求失败
            ToastUtil.toastError(this);
        }
    }

    @Override
    public void requestFail(int type) {
        dismissLoading();
    }

    @Override
    public void requestCancel(int type) {
        dismissLoading();
    }

    /**
     * 获取用车类型列表
     */
    private void getAllCarType() {
        Map<String, Object> params = new HashMap<>();

        showLoading();
        RetrofitFactory.getInstance().createApi(CarApi.class).getAllCarType(params)
                .enqueue(new CallBack<List<GetAllCarType>>() {
                    @Override
                    public void success(List<GetAllCarType> getAllCarTypeList) {
                        dismissLoading();
                        if (getAllCarTypeList.size() == 0) {
                            showToast("列表数据为空");
                            return;
                        }
                        mGetAllCarTypeList.clear();
                        mGetAllCarTypeList.addAll(getAllCarTypeList);
                        carTypePopupWindow = new CommonPopupWindow.Builder(mContext)
                                .setView(R.layout.popu_businesscar_apply_car_type)
                                .setBackGroundLevel(0.9f)
                                .setOutsideTouchable(true)
                                .setWidthAndHeight(ll_car_type.getWidth() - DisplayUtil.dpTopx(mContext, 10),
                                        ViewGroup.LayoutParams.WRAP_CONTENT)
                                .setViewOnclickListener(BusinessCarApplyCreateOrderActivity.this)
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
        Map<String, Object> params = new HashMap<>();

        showLoading();
        RetrofitFactory.getInstance().createApi(CarApi.class).getAllCarRange(params)
                .enqueue(new CallBack<List<GetAllCarRange>>() {
                    @Override
                    public void success(List<GetAllCarRange> getAllCarRangeList) {
                        dismissLoading();
                        if (getAllCarRangeList.size() == 0) {
                            showToast("列表数据为空");
                            return;
                        }
                        mGetAllCarRangeList.clear();
                        mGetAllCarRangeList.addAll(getAllCarRangeList);
                        carRangePopupWindow = new CommonPopupWindow.Builder(mContext)
                                .setView(R.layout.popu_businesscar_apply_car_range)
                                .setBackGroundLevel(0.9f)
                                .setOutsideTouchable(true)
                                .setWidthAndHeight(ll_car_range.getWidth() - DisplayUtil.dpTopx(mContext, 10),
                                        ViewGroup.LayoutParams.WRAP_CONTENT)
                                .setViewOnclickListener(BusinessCarApplyCreateOrderActivity.this)
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
     * 点击提交
     */
    private void applyCar() {
        //获取审批人列表接口
        String UnitCode = selectUnitCode;
        Map<String, Object> params1 = new HashMap<>();
        params1.put("unitCode", UnitCode);
        Retrofit retrofit = new RequestUtil().getRequestClient(URL.ZHONGCHE_93);
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        Call<Map<String, Object>> call12 = request.getAuditorByUnitCode(params1);
        new RequestUtil().requestDataNoLoading(call12, new RequestCallback() {
            @Override
            public void requestSuccess(Object map, int type) {
                dismissLoading();
                Map<String, Object> result = (Map<String, Object>) map;
                if (null != result) {
                    String code = null == result.get("code") ? "" : result.get("code").toString();
                    String codeMsg = null == result.get("codeMsg") ? "" : result.get("codeMsg").toString();
                    if (code.equals("00000")) {
                        List<Map<String, Object>> data = (ArrayList) result.get("data");
                        if (data.isEmpty()) {
                            ToastUtil.toast("当前核算主体未找到审核人，请联系管理员添加！");
                        } else {
                            auditorList = new ArrayList<>();
                            if (data.size() == 1) {//如果只有一个审批人员
                                for (int i = 0; i < data.size(); i++) {
                                    auditorEntity = new HashMap<>();
                                    auditorEntity.put("auditorId", data.get(i).get("auditorId").toString());
                                    auditorEntity.put("auditorName", data.get(i).get("auditorName").toString());
                                    auditorEntity.put("headquarters", data.get(i).get("headquarters").toString());
                                    auditorEntity.put("email", data.get(i).get("email").toString());
                                }
                                sendForm();
                            } else {
                                for (int i = 0; i < data.size(); i++) {
                                    Map<String, Object> Object = new HashMap<>();
                                    Object.put("auditorId", data.get(i).get("auditorId").toString());
                                    Object.put("auditorName", data.get(i).get("auditorName").toString());
                                    Object.put("headquarters", data.get(i).get("headquarters").toString());
                                    Object.put("email", data.get(i).get("email").toString());
                                    if (i == 0) {
                                        Object.put("isCheck", "true");
                                    } else {
                                        Object.put("isCheck", "false");
                                    }

                                    auditorList.add(Object);
                                }
                                showPopupWindows(8, auditorList);
                            }
                        }
                    }
                } else {
                    //请求失败
                    ToastUtil.toastError(BusinessCarApplyCreateOrderActivity.this);
                }
            }

            @Override
            public void requestFail(int type) {
                dismissLoading();
            }

            @Override
            public void requestCancel(int type) {
                dismissLoading();
            }
        }, 8, this);
    }

    /**
     * 验证表单数据
     */
    private void createApplyCarRequest() {
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
        String importantCustomer = isImprotantClient ? "1" : "0";
        String customerLevelJob = et_client_level.getText().toString().trim();

        if (StringUtil.isEmpty(mobile)) {
            showToast("请填写联系方式");
            return;
        }
        if (!StringUtil.isEmpty(mobile) && mobile.length() != 11) {
            showToast("请填写正确的联系方式");
            return;
        }
        if (StringUtil.isEmpty(userName)) {
            userName = "";
        }
        if (StringUtil.isEmpty(userMobile)) {
            userMobile = "";
        }
        if (selectCompanyStr == "") {
            showToast("请选择核算主体");
            return;
        }
        if (selectUnitStr == "") {
            showToast("请选择部门");
            return;
        }
        if (StringUtil.isEmpty(userName) && !StringUtil.isEmpty(userMobile)) {
            showToast("请填写用车人姓名");
            return;
        }
        if (!StringUtil.isEmpty(userName) && StringUtil.isEmpty(userMobile)) {
            showToast("请填写用车人电话");
            return;
        }
        if (!StringUtil.isEmpty(userMobile) && userMobile.length() != 11) {
            showToast("请填写正确的用车人电话");
            return;
        }
        if (isImprotantClient || !StringUtil.isEmpty(customerLevelJob)) {
            if (StringUtil.isEmpty(userName) || StringUtil.isEmpty(userMobile)) {
                showToast("请填写用车人姓名与电话");
                return;
            }
        }
        if (mChoseGetAllCarType == null) {
            showToast("请选择用车类型");
            return;
        }
        if (StringUtil.isEmpty(carNumber)) {
            showToast("请填写用车数量");
            return;
        }
        if (StringUtil.isEmpty(peopleNumber)) {
            showToast("请填写乘车人数");
            return;
        }
        if ("选择用车开始时间".equals(startTime)) {
            showToast("请选择用车开始时间");
            return;
        }
        if ("选择用车结束时间".equals(endTime)) {
            showToast("请选择用车结束时间");
            return;
        }
        if (mChoseGetAllCarRange == null) {
            showToast("请选择用车范围");
            return;
        }
        if (StringUtil.isEmpty(applyReason)) {
            showToast("请填写申请事由");
            return;
        }
        applyCar();//获取审批人
    }

    //提交表单
    public void sendForm() {
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
        String importantCustomer = isImprotantClient ? "1" : "0";
        String customerLevelJob = et_client_level.getText().toString().trim();
        //审批人信息
        String auditorId = auditorEntity.get("auditorId").toString().trim();
        String auditorName = auditorEntity.get("auditorName").toString().trim();
        String headquarters = auditorEntity.get("headquarters").toString().trim();
        String email = auditorEntity.get("email").toString().trim();

        User user = AppUtils.getUserInfo(mContext);
        Map<String, Object> params = new HashMap<>();
        params.put("applicatId", user.getEmployeeCode());
        params.put("applicatName", user.getEmployeeName());
        params.put("mobile", mobile);
        params.put("userName", userName);
        params.put("userMobile", userMobile);
        params.put("carNumber", carNumber);
        params.put("importantCustomer", importantCustomer);
        params.put("customerLevelJob", customerLevelJob);
//        params.put("unitCode", null==mChoseCompanyBean.getCompanyCode()?"":mChoseCompanyBean.getCompanyCode());
        params.put("unitCode", selectUnitCode);
//        params.put("unitName", null==mChoseCompanyBean.getUnitName()?"":mChoseCompanyBean.getUnitName());
        params.put("unitName", selectUnitStr);
//        params.put("accountingSubjectCode", null==mChoseCompanyBean.getCompanyCode()?"":mChoseCompanyBean.getCompanyCode());
        params.put("accountingSubjectCode", selectCompanyCode);
//        params.put("accountingSubjectName", null==mChoseCompanyBean.getCompanyName()?"":mChoseCompanyBean.getCompanyName());
        params.put("accountingSubjectName", selectCompanyStr);
        params.put("email", user.getMail() + "#" + email);
        params.put("carType", mChoseGetAllCarType.getTypeCode());
        params.put("carRange", mChoseGetAllCarRange.getRangeCode());
        params.put("numberPeople", peopleNumber);
        params.put("startTime", DateUtils.StrToDate(startTime).getTime());
        params.put("endTime", DateUtils.StrToDate(endTime).getTime());
        params.put("applyReason", applyReason);
        //
        params.put("auditorId", auditorId);
        params.put("auditorName", auditorName);
        params.put("headquarters", headquarters);
        showLoading();
        RetrofitFactory.getInstance().createApi(CarApi.class).createApplyCar(params)
                .enqueue(new CallBack<Object>() {
                    @Override
                    public void success(Object object) {
                        dismissLoading();
                        showToast("创建成功");
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
        if (layoutResId == R.layout.popu_businesscar_apply_car_type) {
            ListView lv_car_type = (ListView) view.findViewById(R.id.lv_car_type);
            mBusinessCarApplyPopuCarTypeAdapter = new BusinessCarApplyPopuCarTypeAdapter(this, mGetAllCarTypeList);
            lv_car_type.setAdapter(mBusinessCarApplyPopuCarTypeAdapter);
            lv_car_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mChoseGetAllCarType = mGetAllCarTypeList.get(position);
                    tv_car_type.setText(mChoseGetAllCarType.getTypeName());
                    carTypePopupWindow.dismiss();
                }
            });
        } else if (layoutResId == R.layout.popu_businesscar_apply_car_range) {
            ListView lv_car_range = (ListView) view.findViewById(R.id.lv_car_range);
            mBusinessCarApplyPopuCarRangeAdapter = new BusinessCarApplyPopuCarRangeAdapter(this, mGetAllCarRangeList);
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

    private void showPopupWindows(final int type, final List<Map<String, Object>> datalist) {
        LinearLayout ly_main = (LinearLayout) findViewById(R.id.ly_main);
        //构建一个popupwindow的布局
        popupView = this.getLayoutInflater().inflate(R.layout.popupwindow_select_person, null);
        final PopupWinListAdapter popAdapter = new PopupWinListAdapter(this, datalist, type);
//        for(int i=0;i<popAdapter.getCount();i++) {
//            datalist.get(i).put("isCheck","false");
//        }
        ListView listView = (ListView) popupView.findViewById(R.id.popupwin_list);
        listView.setAdapter(popAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < popAdapter.getCount(); i++) {
                    datalist.get(i).put("isCheck", "false");
                }
                datalist.get(position).put("isCheck", "true");
                popAdapter.notifyDataSetChanged();
            }
        });

        TextView popuptitle = (TextView) popupView.findViewById(R.id.popup_title);
        if (type == 3) {//核算主体
            popupView.findViewById(R.id.select_person).setVisibility(View.GONE);
            popupView.findViewById(R.id.popupwin_list).setVisibility(View.VISIBLE);
            popuptitle.setText("选择核算主体");
            TextView tv_ok = (TextView) popupView.findViewById(R.id.select_person_ok);
            tv_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < datalist.size(); i++) {
                        Map<String, Object> tmpEntity = datalist.get(i);
                        if ((tmpEntity.get("isCheck") + "").equals("true")) {
                            selectAccountEntity = tmpEntity;
                        }
                    }
                    if (selectAccountEntity == null) {
//                        AlertUtil.show(this, "请选择核算主体", "确定", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                            }
//                        }, "取消", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                            }
//                        }, "选择核算主体");
//                        return;
                    }
                    window.dismiss();
                    tv_apply_accounting_subject.setText(selectAccountEntity.get("companyName") + "");
                    selectCompanyStr = selectAccountEntity.get("companyName") + "";
                    selectCompanyCode = selectAccountEntity.get("companyCode") + "";

                    List<Map<String, Object>> tmpAccountEntityList = new ArrayList<>();
                    for (int j = 0; j < accountEntityList.size(); j++) {
                        Map<String, Object> map = accountEntityList.get(j);
                        String mapCompanyName = null == map.get("companyName") ? "" : map.get("companyName").toString();
                        if (mapCompanyName.equals(selectAccountEntity.get("companyName") + "")) {
                            accountEntityList.get(j).put("isCheck", "true");

                        } else {
                            accountEntityList.get(j).put("isCheck", "false");
                        }
                    }
//
                    List<Map<String, Object>> unitInfo = new ArrayList<>();
                    for (int m = 0; m < ((List<Map<String, Object>>) selectAccountEntity.get("unitInfo")).size(); m++) {
                        unitInfo.add(((List<Map<String, Object>>) selectAccountEntity.get("unitInfo")).get(m));

                    }


                    unitEntityList = new ArrayList<>();

                    for (int mm = 0; mm < unitInfo.size(); mm++) {
                        Map<String, Object> tmpInfo = unitInfo.get(mm);
                        if (mm == 0) {
                            tmpInfo.put("isCheck", "true");
                        } else {
                            tmpInfo.put("isCheck", "false");
                        }

                    }
                    unitEntityList.addAll(unitInfo);
                    if (unitEntityList.size() == 1) {
                        selectunitEntity = unitEntityList.get(0);
                        tv_apply_apartment.setText(selectunitEntity.get("unitName") + "");
                        selectUnitStr = selectunitEntity.get("unitName") + "";
                        selectUnitCode = selectunitEntity.get("unitCode") + "";
                    } else if (unitEntityList.size() > 1) {
                        showPopupWindows(7, unitEntityList);
                        tv_apply_apartment.setText("");
                        selectUnitStr = "";
                        selectUnitCode = "";
                    } else if (unitEntityList.size() == 0) {
                        tv_apply_apartment.setText("");
                        selectUnitStr = "";
                        selectUnitCode = "";
                    }

                }
            });
        } else if (type == 7) {
            popupView.findViewById(R.id.select_person).setVisibility(View.GONE);
            popupView.findViewById(R.id.popupwin_list).setVisibility(View.VISIBLE);
            TextView tv_ok = (TextView) popupView.findViewById(R.id.select_person_ok);
            popuptitle.setText("选择部门");
            tv_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    window.dismiss();
//                    selectunitEntity = datalist.get(position);
                    for (int i = 0; i < datalist.size(); i++) {
                        Map<String, Object> tmpEntity = datalist.get(i);
                        if ((tmpEntity.get("isCheck") + "").equals("true")) {
                            selectunitEntity = tmpEntity;
                        }
                    }
                    tv_apply_apartment.setText(selectunitEntity.get("unitName") + "");
                    selectUnitStr = selectunitEntity.get("unitName") + "";
                    selectUnitCode = selectunitEntity.get("unitCode") + "";

                }
            });
        } else if (type == 8) {
            popupView.findViewById(R.id.select_person).setVisibility(View.GONE);
            popupView.findViewById(R.id.popupwin_list).setVisibility(View.VISIBLE);
            TextView tv_ok = (TextView) popupView.findViewById(R.id.select_person_ok);
            popuptitle.setText("选择审核人");
            tv_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    window.dismiss();
//                    selectunitEntity = datalist.get(position);
                    for (int i = 0; i < datalist.size(); i++) {
                        Map<String, Object> tmpEntity = datalist.get(i);
                        if ((tmpEntity.get("isCheck") + "").equals("true")) {
                            auditorEntity = tmpEntity;
                        }
                    }
                    sendForm();
                }
            });
        }

        popupView.findViewById(R.id.select_person_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();

            }
        });

        //创建PopupWindow对象，指定宽度和高度
        int pop_width = (int) (AppUtils.getDeviceWidth(this) * 0.8);
        int pop_height = (int) (AppUtils.getDeviceWidth(this) * 1.1);
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
        window.showAtLocation(ll_container, Gravity.CENTER, 0, 0);
    }

    /**
     * 显示弹窗（选择核算主体界面）
     *
     * @param
     */
    private void showPopupWindows(final List<GetSelectEmployee.CompanyBean> datalist) {
        //构建一个popupwindow的布局
        popupView = getLayoutInflater().inflate(R.layout.popupwindow_select_person, null);
        final PopupWinBusinessCarListAdapter popAdapter = new PopupWinBusinessCarListAdapter(this, datalist);
        //弹出类型选择窗口时，默认设置第一个为选择项
        for (int i = 0; i < datalist.size(); i++) {
            if (i == 0) {
                datalist.get(i).setCheck(true);
            } else {
                datalist.get(i).setCheck(false);
            }
        }
        ListView listView = (ListView) popupView.findViewById(R.id.popupwin_list);
        listView.setAdapter(popAdapter);
        //类型列表点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < popAdapter.getCount(); i++) {
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
                for (int i = 0; i < datalist.size(); i++) {
                    if (datalist.get(i).isCheck()) {
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
        int pop_width = (int) (AppUtils.getDeviceWidth(this) * 0.8);
        int pop_height = (int) (AppUtils.getDeviceWidth(this) * 1.1);
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
        window.showAtLocation(ll_container, Gravity.CENTER, 0, 0);
    }

    /**
     * 弹窗外阴影
     *
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
