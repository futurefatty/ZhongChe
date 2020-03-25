package com.neusoft.zcapplication.mine.order;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

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
 * 我的订单
 **/
public class FlightSchemes2Activity extends BaseActivity implements View.OnClickListener,
        FlightSchemesList2Adapter.ClickEvent,RequestCallback {
    private FlightSchemesList2Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_flight_schemes);
        initView();
//        initImageView();
//        showData();
//        getInterMethod();
    }

    private void initView(){
        findViewById(R.id.btn_back).setOnClickListener(this);
        List<List<Map<String,Object>>> list = new ArrayList<>();
        ListView listView = (ListView) findViewById(R.id.flight_schemes_list);
        adapter = new FlightSchemesList2Adapter(FlightSchemes2Activity.this,list,getIntent().getStringExtra("applicantId"),this);
        listView.setAdapter(adapter);

        AppUtils.setStateBar(FlightSchemes2Activity.this,findViewById(R.id.frg_status_bar));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;

        }
    }

    /**
     * 获取国际机票详情方案
     */
    private void getInterMethod() {
        Map<String,Object> params = new HashMap<>();
        params.put("ciphertext","test");
        User user = AppUtils.getUserInfo(FlightSchemes2Activity.this);
        params.put("employeeCode", user.getEmployeeCode());
        params.put("combineId", getIntent().getStringExtra("combineId"));
//        params.put("combineId", "1507879636704");
        params.put("loginType", URL.LOGIN_TYPE);
//        params.put("type", getIntent().getIntExtra("type",1));
        params.put("type", 2);
        Retrofit retrofit = new RequestUtil().getRequestClient(URL.ZHONGCHE_93);
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        Call<Map<String,Object>> call = request.getInterMethod(params);
        new RequestUtil().requestData(call,this,1,"加载中...",false,FlightSchemes2Activity.this);
    }

    @Override
    public void book(int buyId) {
        Map<String,Object> params = new HashMap<>();
        params.put("ciphertext","test");
        User user = AppUtils.getUserInfo(FlightSchemes2Activity.this);
        params.put("employeeCode", user.getEmployeeCode());
        params.put("combineId", getIntent().getStringExtra("combineId"));
//        params.put("combineId", "1507879636704");
        params.put("loginType", URL.LOGIN_TYPE);
//        params.put("type", getIntent().getIntExtra("type",1));
        params.put("buyId", buyId);
        Retrofit retrofit = new RequestUtil().getRequestClient(URL.ZHONGCHE_93);
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        Call<Map<String,Object>> call = request.book(params);
        new RequestUtil().requestData(call,this,2,mContext);
    }

    @Override
    public void requestSuccess(Object map, int type) {
        Map<String,Object> result = (Map<String, Object>) map;
        switch (type){
            case 1:
                if(null != result){
                    String code = null == result.get("code") ?"": result.get("code").toString();
                    String codeMsg = null == result.get("codeMsg") ?"": result.get("codeMsg").toString();

                    if(code.equals("00000")) {
                        List<List<Map<String,Object>>> data = (ArrayList) result.get("data");
                        if (data.size()==0){
                            Toast.makeText(FlightSchemes2Activity.this,"暂无数据",Toast.LENGTH_SHORT).show();
                        }else {
//                            String buyId = getIntent().getStringExtra("BUYID");
                            List<List<Map<String,Object>>> newList = new ArrayList<>();
                            for (List<Map<String,Object>> itemList:data){
                                Map<String,Object> item = itemList.get(0);
                                if ("1.0".equals(item.get("ISBUY")+"")){
                                    newList.add(itemList);
                                }
                            }

                            adapter.setList(newList);
                            adapter.notifyDataSetChanged();
                        }
                    }
                    else {
                        ToastUtil.toastError(FlightSchemes2Activity.this);
                    }
                }else{
                    //请求失败
                    ToastUtil.toastError(FlightSchemes2Activity.this);
                }
                break;
            case 2:
                if(null != result){
                    String code = (String) result.get("code");
                    String codeMsg = (String) result.get("codeMsg");

                    if(code.equals("00000")) {
                        ToastUtil.toastHandleSuccess(FlightSchemes2Activity.this);
                    }
                    else {
                        ToastUtil.toastHandleError(FlightSchemes2Activity.this);
                    }
                }else{
                    //请求失败
                    ToastUtil.toastHandleError(FlightSchemes2Activity.this);
                }
                break;
        }
    }

    @Override
    public void requestFail(int type) {
        switch (type){
            case 1:
                ToastUtil.toastFail(FlightSchemes2Activity.this);
                break;
            case 2:
                ToastUtil.toastHandleFail(FlightSchemes2Activity.this);
                break;
        }
    }

    @Override
    public void requestCancel(int type) {

    }
}
