package com.neusoft.zcapplication.TicketService;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.neusoft.zcapplication.ApplyOrderListAdapter;
import com.neusoft.zcapplication.Bean.User;
import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.base.BaseActivity;
import com.neusoft.zcapplication.http.NetWorkRequest;
import com.neusoft.zcapplication.http.RequestCallback;
import com.neusoft.zcapplication.http.RequestUtil;
import com.neusoft.zcapplication.http.URL;
import com.neusoft.zcapplication.mine.order.FlightSchemesActivity;
import com.neusoft.zcapplication.tools.AppUtils;
import com.neusoft.zcapplication.tools.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * 因私国际申请
 */
public class PrivateInternationalApplyActivity extends BaseActivity implements View.OnClickListener, RequestCallback {

    private ListView lv_apply_order;
    private TextView tv_add_apply_order;
    private privateInternationalApplyOrderListAdapter mApplyOrderListAdapter;
    private List<Call<Map<String,Object>>> callList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_international_apply);
        initView();
        initData();
    }

    private void initView(){
        TextView title = (TextView) findViewById(R.id.tv_page_title);
        title.setText("因私国际申请");
        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.tv_add_apply_order).setOnClickListener(this);
        AppUtils.setStateBar(PrivateInternationalApplyActivity.this,findViewById(R.id.frg_status_bar));

        callList = new ArrayList<>();
        tv_add_apply_order = (TextView) findViewById(R.id.tv_add_apply_order);
        lv_apply_order  = (ListView) findViewById(R.id.lv_apply_order);
        List<Map<String,Object>> list = new ArrayList<>();
        mApplyOrderListAdapter = new privateInternationalApplyOrderListAdapter(this,list,0);
        lv_apply_order.setAdapter(mApplyOrderListAdapter);

        lv_apply_order.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PrivateInternationalApplyActivity.this,FlightSchemesActivity.class);
                Map<String,Object> map = mApplyOrderListAdapter.getList().get(position);
//                    double supplerId = null == map.get("SUPPLIERID") ? -1.0 :(double)map.get("SUPPLIERID");//供应商id
//                    int supplerIdInt = (int) supplerId;
                String combineId = null == map.get("COMBINEID") ? "" : map.get("COMBINEID").toString();//国际机票订单综合编号
                String applicantId = null == map.get("applicateId") ? "" : map.get("applicateId").toString();//提交该申请单的用户的员工编号

                double isBuy = null == map.get("isBuy") ? 0.0 :(double)map.get("isBuy");//供应商id
                int isBuyInt = (int) isBuy;
                double type = null == map.get("type") ? 1.0 :(double)map.get("type");//
                int typeInt = (int) type;
                String orderStateName = null == map.get("ORDERSTATENAME")? "":map.get("ORDERSTATENAME").toString();

                intent.putExtra("combineId", combineId);
//                    intent.putExtra("supplerId", "-1");//这里没有供应商id，可不传，默认为-1
                intent.putExtra("applicantId", applicantId);
                intent.putExtra("isBuy", isBuyInt);
                intent.putExtra("type", typeInt);
                intent.putExtra("orderStateName", orderStateName);
                intent.putExtra("internationalPrivate", 1);
                startActivityForResult(intent,1001);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        queryOrderList(2);
    }

    private void initData() {
//        queryOrderList(2);
    }


    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.tv_add_apply_order:
                startActivityForResult(PrivateInternationalApplyCreateActivity.class,2001);
                break;
        }
    }

    /**
     *查询预定申请单列表数据
     * @param dataType //0:全部，1：国内机票，2：国际机票，3：酒店
     */
    private void queryOrderList(int dataType) {
        //如果当前选中的是预定申请单fragment,则提示获取数据成功或失败
//        final int viewpagerIndex = ((MainActivity)getActivity()).getPagerIndex();
        Map<String,Object> params = new HashMap<>();
        User user = AppUtils.getUserInfo(this);
        String employeeCode = user.getEmployeeCode();
        params.put("ciphertext","test");
        params.put("employeeCode",employeeCode);
        params.put("loginType", URL.LOGIN_TYPE);
        params.put("type",dataType);
        params.put("internationalPrivate",1);//internationalPrivate      是否国际因私申请单，0：不是，1：是
//        Log.i("--->","参数:" + params);
        Retrofit retrofit = new RequestUtil().getRequestClient(URL.ZHONGCHE_93);
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        Call<Map<String,Object>> call = request.getOrderList(params);
        callList.add(call);
        new RequestUtil().requestData(call,this,dataType,this);
    }

    /**
     * 显示列表数据的方法
     * @param type
     * @param result
     */
    private void showListData(int type,Map<String,Object> result){
        if(null != result){
            String code = null == result.get("code")?"" :result.get("code").toString();
            List<Map<String,Object>> datalist = (ArrayList)result.get("data");
            if(code.equals("00000")) {
                //显示国内预定申请单数据
                mApplyOrderListAdapter.setList(datalist);
                mApplyOrderListAdapter.notifyDataSetChanged();
            }else {
                //如果当前页为预定申请单页面，并且只显示当前类型（国内或国际）的提示当前显示的预定申请单
                ToastUtil.toastError(this);
            }
        }else{
            //请求失败
            //如果当前页为预定申请单页面，并且只显示当前类型（国内或国际）的提示当前显示的预定申请单
            ToastUtil.toastError(this);
        }
    }

    @Override
    public void requestSuccess(Object map, int type) {
        //如果当前选中的是预定申请单fragment,则提示获取数据成功或失败
        switch (type){
            case 2:
                //请求国内，国际预定申请单
                Map<String,Object> result = (Map<String, Object>) map;
                showListData(1,result);
                break;
        }
    }

    @Override
    public void requestFail(int type) {
        //如果当前选中的是预定申请单fragment,则提示获取数据成功或失败
        switch (type){
            case 1:
                ToastUtil.toastFail(this);
                break;
        }
    }

    @Override
    public void requestCancel(int type) {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*if (requestCode == 1001){
            queryOrderList(2);//国际机票
        }else if (requestCode == 2001){
            queryOrderList(2);//国际机票
        }*/
    }

}
