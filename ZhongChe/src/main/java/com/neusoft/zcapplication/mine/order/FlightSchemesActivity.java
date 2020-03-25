package com.neusoft.zcapplication.mine.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.neusoft.zcapplication.Bean.User;
import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.base.BaseActivity;
import com.neusoft.zcapplication.http.NetWorkRequest;
import com.neusoft.zcapplication.http.RequestCallback;
import com.neusoft.zcapplication.http.RequestUtil;
import com.neusoft.zcapplication.http.URL;
import com.neusoft.zcapplication.tools.AppUtils;
import com.neusoft.zcapplication.tools.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * 查看方案
 */
public class FlightSchemesActivity extends BaseActivity implements View.OnClickListener,
        FlightSchemesListAdapter.ClickEvent, RequestCallback {
    private FlightSchemesListAdapter adapter;

    //是否是预定审批进来的
    public static final String IS_ORDER_APPLY = "IS_ORDER_APPLY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_flight_schemes);
        initView();
//        initImageView();
//        showData();
        getInterMethod();
    }

    private void initView() {
        findViewById(R.id.btn_back).setOnClickListener(this);
        List<List<Map<String, Object>>> list = new ArrayList<>();
        ListView listView = (ListView) findViewById(R.id.flight_schemes_list);
        int isBuy = getIntent().getIntExtra("isBuy", 0);
        boolean hasOrder = isBuy == 1 ? true : false;
        String applicantId = getIntent().getStringExtra("applicantId");
        int type = getIntent().getIntExtra("type", 1);//1 采购订单,2 改签订单,其他 图片订单
        String orderStateName = getIntent().getStringExtra("orderStateName");
        boolean isOrderApply = getIntent().getBooleanExtra(IS_ORDER_APPLY, false);
        adapter = new FlightSchemesListAdapter(FlightSchemesActivity.this, list, applicantId,
                this, hasOrder, type, orderStateName);
        adapter.setOrderApply(isOrderApply);
        listView.setAdapter(adapter);
        AppUtils.setStateBar(FlightSchemesActivity.this, findViewById(R.id.frg_status_bar));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
        }
    }

    /**
     * 获取国际机票详情方案
     */
    private void getInterMethod() {
        Map<String, Object> params = new HashMap<>();
        Intent dataIntent = getIntent();
        User user = new AppUtils().getUserInfo(FlightSchemesActivity.this);
        params.put("ciphertext", "test");
        params.put("employeeCode", user.getEmployeeCode());
        params.put("employeeName", user.getEmployeeName());//登录用户名称
        int isBuy = dataIntent.getIntExtra("isBuy", 0);
        params.put("isBuy", isBuy);//是否有买方案
//        Log.i("--->","=====isBuy =" + isBuy);
        params.put("combineId", dataIntent.getStringExtra("combineId"));
        params.put("loginType", URL.LOGIN_TYPE);
        int supplerId = dataIntent.getIntExtra("supplerId", -1);
        int internationalPrivate = dataIntent.getIntExtra("internationalPrivate", 0);
        if (supplerId == -1) {
            //params.put("supplierId", "");//供应商id
        } else {
            params.put("supplierId", supplerId);//供应商id
        }
        params.put("internationalPrivate", internationalPrivate);
        int type = dataIntent.getIntExtra("type", 1);
        params.put("type", type);

//        params.put("ciphertext","test");
//        params.put("employeeCode", "20110816");
//        params.put("employeeName", "李广龙");//登录用户名称
//        params.put("isBuy ", "1");//是否有买方案
//        params.put("combineId", "IN2017122000021");
//        params.put("loginType", URL.LOGIN_TYPE);
//        params.put("supplierId", "4");//供应商id


        Retrofit retrofit = new RequestUtil().getRequestClient(URL.ZHONGCHE_93);
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        Call<Map<String, Object>> call = request.getInterMethod(params);
        new RequestUtil().requestData(call, this, 1, "加载中...", true, FlightSchemesActivity.this);
    }

    @Override
    public void book(Map<String, Object> map) {
        //选择预定方案
        double buyId = Double.parseDouble(map.get("BUYID") + "");
        double sequence = Double.parseDouble(map.get("SEQUENCE") + "");
        int sequenceInt = (int) sequence;
        Map<String, Object> params = new HashMap<>();
        params.put("ciphertext", "test");
        User user = AppUtils.getUserInfo(FlightSchemesActivity.this);
        params.put("employeeCode", user.getEmployeeCode());
        params.put("combineId", getIntent().getStringExtra("combineId"));
//        params.put("combineId", "1507879636704");
        params.put("loginType", URL.LOGIN_TYPE);
//        params.put("type", getIntent().getIntExtra("type",1));
        params.put("buyId", (int) buyId);
        params.put("sequence", sequenceInt);//方案序号
        int type = getIntent().getIntExtra("type", 1);
        int supplerId = getIntent().getIntExtra("supplerId", -1);
        int isBuy = getIntent().getIntExtra("isBuy", 0);
        int internationalPrivate = getIntent().getIntExtra("internationalPrivate", 0);
        params.put("type", type);
        if (supplerId != -1) {
            params.put("supplierId", supplerId);
        }
        params.put("isBuy", isBuy);
        params.put("internationalPrivate", internationalPrivate);
        Retrofit retrofit = new RequestUtil().getRequestClient(URL.ZHONGCHE_93);
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        Call<Map<String, Object>> call = request.book(params);
        new RequestUtil().requestData(call, this, 2, mContext);
    }

    @Override
    public void requestSuccess(Object map, int type) {
        Map<String, Object> result = (Map<String, Object>) map;
        switch (type) {
            case 1:
                //获取方案数据
                if (null != result) {
                    String code = null == result.get("code") ? "" : result.get("code").toString();
                    String codeMsg = null == result.get("codeMsg") ? "" : result.get("codeMsg").toString();

                    if (code.equals("00000")) {
                        List<List<Map<String, Object>>> data = (ArrayList) result.get("data");
                        if (data.size() == 0) {
                            ToastUtil.toastNoData(FlightSchemesActivity.this);
                        } else {
                            adapter.setList(data);
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        ToastUtil.toastError(FlightSchemesActivity.this);
                    }
                } else {
                    //请求失败
                    ToastUtil.toastError(FlightSchemesActivity.this);
                }
                break;
            case 2:
                //预定方案回调
                if (null != result) {
                    String code = null == result.get("code") ? "" : result.get("code").toString();
//                    String codeMsg = (String) result.get("codeMsg");
                    if (code.equals("00000")) {
                        ToastUtil.toastHandleSuccess(FlightSchemesActivity.this);
                        finish();
                    } else {
                        ToastUtil.toastHandleError(FlightSchemesActivity.this);
                    }
                } else {
                    //请求失败
                    ToastUtil.toastHandleError(FlightSchemesActivity.this);
                }
                break;
        }
    }

    @Override
    public void requestFail(int type) {
        switch (type) {
            case 1:
                ToastUtil.toastFail(FlightSchemesActivity.this);
                break;
            case 2:
                ToastUtil.toastHandleFail(FlightSchemesActivity.this);
                break;
        }
    }

    @Override
    public void requestCancel(int type) {

    }
}
