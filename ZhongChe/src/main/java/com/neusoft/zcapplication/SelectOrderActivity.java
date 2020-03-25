package com.neusoft.zcapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.neusoft.zcapplication.Bean.BunbleParam;
import com.neusoft.zcapplication.base.BaseActivity;
import com.neusoft.zcapplication.http.NetWorkRequest;
import com.neusoft.zcapplication.http.RequestCallback;
import com.neusoft.zcapplication.http.RequestUtil;
import com.neusoft.zcapplication.http.URL;
import com.neusoft.zcapplication.tools.AlertUtil;
import com.neusoft.zcapplication.tools.AppUtils;
import com.neusoft.zcapplication.tools.ToastUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * 选择订单
 */
public class SelectOrderActivity extends BaseActivity implements View.OnClickListener,RequestCallback {
    private ApplyOrderListAdapter adapter;
//    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_order);
        initView();
        queryOrderList();
    }

    private void initView(){
        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.select_order_query).setOnClickListener(this);
        ListView listView  =  (ListView) findViewById(R.id.apply_order_list);
        List<Map<String,Object>> list = new ArrayList<>();
        final int dataType = getIntent().getIntExtra("type",1);//0:全部，1：国内机票，2：国际机票，3：酒店
        adapter = new ApplyOrderListAdapter(SelectOrderActivity.this,list,1);
        if(dataType == 3){
            adapter.setDataType(1);
        }
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /**
                 * 若是取消选中的预定申请单，则直接取消
                 * 否则判断是否有选择好的预定申请单
                 * 若有，再次选中的时候，需判断当前选中的预定申请单的出发时间，出发、到达地点是否一致
                 * 不一致的话，提示用户
                 */
                List<Map<String,Object>> l = adapter.getList();
                Map<String,Object> map = l.get(position);
                String checkStr = null == map.get("isChecked")?"" :map.get("isChecked").toString();
                if(checkStr.equals("yes")){
                    l.get(position).put("isChecked","");
                    adapter.setList(l);
                    adapter.notifyDataSetChanged();
                }else{
                    Map<String,Object> sm = adapter.getSelectMap();
                    if(null == sm){
                        //没有已选的预定申请单
                        l.get(position).put("isChecked","yes");
                        adapter.setList(l);
                        adapter.notifyDataSetChanged();
                    }else{
                        //判断当前选中的预定单的信息是否与之前选中的预定申请单的信息一致
                        String fromCity1 = null == sm.get("fromCity")?"":sm.get("fromCity").toString();
                        String toCity1 = null == sm.get("toCity")?"":sm.get("toCity").toString();
                        String fromDate1 = null == sm.get("fromDate")?"":sm.get("fromDate").toString();
                        String travelType1 = null ==  sm.get("travelType") ?"": sm.get("travelType").toString();
                        int paytype1 = (null ==  sm.get("paytype")||"".equals(sm.get("paytype").toString())) ?0: Double.valueOf(sm.get("paytype").toString()).intValue();

                        String fromCity2 = null == map.get("fromCity")?"":map.get("fromCity").toString();
                        String toCity2 = null == map.get("toCity")?"":map.get("toCity").toString();
                        String fromDate2 = null == map.get("fromDate")?"":map.get("fromDate").toString();
                        String travelType2 = null ==  map.get("travelType") ?"": map.get("travelType").toString();
                        int paytype2 = (null ==  map.get("paytype")||"".equals(map.get("paytype").toString())) ?0: Double.valueOf(map.get("paytype").toString()).intValue();
                        //0:全部，1：国内机票，2：国际机票，3：酒店
                        if(dataType == 3){
                            //如果是酒店类型的预定申请单，则判断入住、离店日期是否一致
                            String checkInTime1 = null ==  sm.get("checkInTime") ?"": sm.get("checkInTime").toString();
                            String checkOutTime1 = null ==  sm.get("checkOutTime") ?"": sm.get("checkOutTime").toString();
                            String checkInTime2 = null ==  map.get("checkInTime") ?"": map.get("checkInTime").toString();
                            String checkOutTime2 = null ==  map.get("checkOutTime") ?"": map.get("checkOutTime").toString();
                            if(fromCity1.equals(fromCity2) && toCity1.equals(toCity2)
                                    && checkInTime1.equals(checkInTime2) && checkOutTime1.equals(checkOutTime2)){
                                l.get(position).put("isChecked","yes");
                                adapter.setList(l);
                                adapter.notifyDataSetChanged();
                            }else{
                                String txt = "必须选择目的地,出发地,日期相同的单";
                                AlertUtil.show2(SelectOrderActivity.this, txt, "确定", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                    }
                                });
                            }
                        }else if(dataType == 1){
                            if(fromCity1.equals(fromCity2) && toCity1.equals(toCity2) && fromDate1.equals(fromDate2)){
                                if(travelType1.equals(travelType2)){
                                    if(paytype1 == 1||paytype2 == 1){
                                        String txt = "存在现付主体不能多选！";
                                        AlertUtil.show2(SelectOrderActivity.this, txt, "确定", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                            }
                                        });
                                    }else{
                                        l.get(position).put("isChecked","yes");
                                        adapter.setList(l);
                                        adapter.notifyDataSetChanged();
                                    }
                                }else{
                                    String txt = "不能选择关联舱位不一致的预定申请单！";
                                    AlertUtil.show2(SelectOrderActivity.this, txt, "确定", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                        }
                                    });
                                }
                            }else{
                                String txt = "必须选择目的地,出发地,日期相同的单";
                                AlertUtil.show2(SelectOrderActivity.this, txt, "确定", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                    }
                                });
                            }
                        }else{
                            if(fromCity1.equals(fromCity2) && toCity1.equals(toCity2) && fromDate1.equals(fromDate2)){
                                if(travelType1.equals(travelType2)){
                                    l.get(position).put("isChecked","yes");
                                    adapter.setList(l);
                                    adapter.notifyDataSetChanged();
                                }else{
                                    String txt = "不能选择关联舱位不一致的预定申请单！";
                                    AlertUtil.show2(SelectOrderActivity.this, txt, "确定", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                        }
                                    });
                                }
                            }else{
                                String txt = "必须选择目的地,出发地,日期相同的单";
                                AlertUtil.show2(SelectOrderActivity.this, txt, "确定", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                    }
                                });
                            }
                        }

                    }
                }
            }
        });
        AppUtils.setStateBar(SelectOrderActivity.this,findViewById(R.id.frg_status_bar));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.select_order_query:
                queryBtnClicked();
                break;
        }
    }

    /**
     * 点击确定，返回数据
     */
    private void queryBtnClicked(){
        List<Map<String,Object>> l = adapter.getList();
//        String billsStr = "";
        StringBuilder sb = new StringBuilder();
        StringBuilder sbForId = new StringBuilder();//预定申请单id
        StringBuilder sbEmployeeCode = new StringBuilder();//预定申请单员工id
        int paytype = 0;//paytype
        Map<String,Object> returnMap = new HashMap<>();
        for(int i = 0 ;i < l.size() ;i++){
            Map<String,Object> map = l.get(i);
            String str = null == map.get("isChecked") ? "" : map.get("isChecked").toString();
            if(str.equals("yes")){
                if(returnMap.size() == 0){
                    returnMap = map;
                }
                String newTypeStr = null ==  map.get("travelType") ?"": map.get("travelType").toString();
                //将舱位等级最高的人的信息加进map
                if(newTypeStr.contains("商务")){
                    returnMap.put("travelType",newTypeStr);
                    returnMap.put("employeeCode", map.get("employeeCode"));
                    returnMap.put("employeeName",map.get("employeeName"));
                    returnMap.put("paytype",map.get("paytype"));
                }
                if(!sb.toString().equals("")){
                    sb.append(",");
                    sbForId.append(",");
                    sbEmployeeCode.append(",");
                }
                //判断可使用的舱位类型
                String orderApplyId = map.get("orderApplyId").toString();
                sb.append(orderApplyId);
                sbForId.append(map.get("id").toString());
                sbEmployeeCode.append(map.get("empCode").toString());
                paytype = (null ==  map.get("paytype")||"".equals(map.get("paytype").toString())) ?0: Double.valueOf(map.get("paytype").toString()).intValue();
            }
        }
        String billsStr = sb.toString();
        if(billsStr.length() == 0){
            Toast.makeText(SelectOrderActivity.this,"请选择预定申请单号！",Toast.LENGTH_SHORT).show();
        }else{
            Intent intent = new Intent();
//            Bundle bundle = new Bundle();
//            BunbleParam bunbleParam = new BunbleParam();
            returnMap.put("orderApplyId",sb.toString());
            returnMap.put("id",sbForId.toString());
            returnMap.put("employeeCodes",sbEmployeeCode.toString());
            returnMap.put("paytype",paytype);
//            bunbleParam.setMap(returnMap);
//            bundle.putSerializable("bunbleParam",(Serializable) bunbleParam);
//            intent.putExtras(bundle);

            intent.putExtra("bunbleParam",(Serializable) returnMap);


            setResult(101,intent);
            finish();
        }
    }
    /**
     * 查询预定申请单列表
     */
    private void queryOrderList() {
        Map<String,Object> params = new HashMap<>();
        params.put("isIndexQueryOrderApply","1");
        params.put("ciphertext","test");
        params.put("employeeCode",getIntent().getStringExtra("employeeCode"));
        params.put("loginType",URL.LOGIN_TYPE);
        int type = getIntent().getIntExtra("type",1);
//        Log.i("--->","type:" + type);
        params.put("type",type);//0:全部，1：国内机票，2：国际机票，3：酒店
        Retrofit retrofit = new RequestUtil().getRequestClient(URL.ZHONGCHE_93);
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        Call<Map<String,Object>> call = request.getOrderList(params);
        new RequestUtil().requestData(call,this,1,"正在加载",true,SelectOrderActivity.this);
    }

    @Override
    public void requestSuccess(Object map, int type) {
        Map<String,Object> result = (Map<String, Object>) map;
        if(null != result){
            String code = null == result.get("code")?"" :result.get("code").toString();
//            String codeMsg = (String) result.get("codeMsg");
            List<Map<String,Object>> dataList = (ArrayList)result.get("data");
//            Log.i("--->","dataList:" + dataList);
//                            Toast.makeText(LoginActivity.this,result.toString(),Toast.LENGTH_SHORT).show();
            if(code.equals("00000")) {
                List<Map<String,Object>> list = new ArrayList<>();
                String ordersStr = getIntent().getStringExtra("id");
                //设置已选的预定申请单位选中状态
//                if(!ordersStr.equals("")){
                    for (Map<String,Object> item:dataList) {
                        String stateStr = item.get("approveState") + "";
                        //过滤出已审批的数据
                        if(ordersStr.equals("")){
                            if(stateStr.equals("1.0")){
                                list.add(item);
                            }
                        }else{
                            String nowId = item.get("id").toString();
                            //过滤出已审批的数据
                            if(stateStr.equals("1.0")){
                                if(ordersStr.indexOf(nowId) != -1){
                                    item.put("isChecked","yes");
                                }
                                list.add(item);
                            }
                        }
                    }
//                }
                adapter.setList(list);
                adapter.notifyDataSetChanged();
            }else {
                ToastUtil.toastError(SelectOrderActivity.this);
            }
        }else{
            //请求失败
            ToastUtil.toastError(SelectOrderActivity.this);
        }
    }

    @Override
    public void requestFail(int type) {
        ToastUtil.toastFail(SelectOrderActivity.this);
    }

    @Override
    public void requestCancel(int type) {

    }
}
