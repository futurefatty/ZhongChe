package com.neusoft.zcapplication.mine.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.neusoft.zcapplication.Bean.User;
import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.base.BaseFragment;
import com.neusoft.zcapplication.http.NetWorkRequest;
import com.neusoft.zcapplication.http.RequestCallback;
import com.neusoft.zcapplication.http.RequestUtil;
import com.neusoft.zcapplication.http.URL;
import com.neusoft.zcapplication.tools.AppUtils;
import com.neusoft.zcapplication.tools.DateUtils;
import com.neusoft.zcapplication.tools.ToastUtil;
import com.neusoft.zcapplication.widget.BottomDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;


/**
 * 国外机票
 *
 */

public class ExternalTicketFragment extends BaseFragment implements View.OnClickListener,RequestCallback {

    //PAYSTATUS 1：未付款  2：已付款
    //PAYTYPE  1：月结  2：现付

    private List<Map<String,Object>> dataList;
    private ExternalOrderListAdapter adapter;
    private BottomDialog bottomDialog;
    private int type = 0,timeSlot = 5;//查询订单参数，订单类型、时间
    private int sortType = 2;//排序方式，1-按时间升序，2-按时间降序
    private TextView tv_sort_type;
    private View frgView;

    public static ExternalTicketFragment getInstance(){
        ExternalTicketFragment fragment = new ExternalTicketFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(null == frgView){
            frgView = inflater.inflate(R.layout.frg_external_order_list, container, false);
            initView();

        }else{
            ViewGroup parent = (ViewGroup) frgView.getParent();
            if(parent != null){
                parent.removeView(frgView);
            }
        }
        return frgView;
    }

    @Override
    protected void initData() {
        super.initData();
        getExternalOrderList();//获取国际机票列表数据
    }

    private void initView(){
        tv_sort_type = (TextView)frgView.findViewById(R.id.tv_sort_type);
        frgView.findViewById(R.id.btn_filter).setOnClickListener(this);
        frgView.findViewById(R.id.btn_sort).setOnClickListener(this);
        dataList = new ArrayList<>();
        ListView listView  =  (ListView)frgView.findViewById(R.id.order_list);
        adapter = new ExternalOrderListAdapter(getActivity(),dataList,1);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                Map<String,Object> map = dataList.get(position);
                double supplerId = null == map.get("SUPPLIERID") ? -1.0 :(double)map.get("SUPPLIERID");//供应商id
                int supplerIdInt = (int) supplerId;
                String combineId = null == map.get("COMBINEID") ? "" : map.get("COMBINEID").toString();//国际机票订单综合编号
                String applicantId = null == map.get("APPLICANTID") ? "" : map.get("APPLICANTID").toString();//提交该申请单的用户的员工编号

                double isBuy = null == map.get("ISBUY") ? 0.0 :(double)map.get("ISBUY");//供应商id
                int isBuyInt = (int) isBuy;
                double type = null == map.get("type") ? 1.0 :(double)map.get("type");//
                String orderStateName = null == map.get("ORDERSTATENAME")? "":map.get("ORDERSTATENAME").toString();
                int typeInt = (int) type;
//                if (null!=dataList.get(position).get("ISBUY")&&"1.0".equals(dataList.get(position).get("ISBUY")+"")){
////                    intent = new Intent();
//                    intent.setClass(getActivity(),FlightSchemes2Activity.class);
//                    intent.putExtra("combineId", combineId);
//                    intent.putExtra("supplerId", supplerIdInt);
//                    intent.putExtra("applicantId", applicantId);
//                    intent.putExtra("isBuy", isBuyInt);
////                    intent.putExtra("BUYID", dataList.get(position).get("BUYID")+"");
//                    startActivity(intent);
//                }else {
                    intent.setClass(getActivity(),FlightSchemesActivity.class);
                    intent.putExtra("combineId", combineId);
                    intent.putExtra("supplerId", supplerIdInt);
                    intent.putExtra("applicantId", applicantId);
                    intent.putExtra("isBuy", isBuyInt);
                    intent.putExtra("type", typeInt);
                    intent.putExtra("orderStateName", orderStateName);
                    startActivityForResult(intent,1001);
//                }

//                if(null!=dataList.get(position).get("ISTOGETHER")&&(double)dataList.get(position).get("ISTOGETHER") == 0.0){//有同行人
//                    intent.putExtra("type",2);
//                }
//                else if(null!=dataList.get(position).get("ISTOGETHER")&&(double)dataList.get(position).get("ISTOGETHER") == 1.0){//无同行人
//                    intent.putExtra("type",1);
//                }
            }
        });

        bottomDialog = new BottomDialog(getActivity(),this,2);

//        frgView.findViewById(R.id.btn_add_passenger).setOnClickListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001){
            getExternalOrderList();
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.btn_filter:
                bottomDialog.showBottomDialog();
                break;
            case R.id.btn_sort:
                if (sortType == 1){
                    tv_sort_type.setText("按日期升序");
                    sortType = 2;
                }
                else if (sortType == 2){
                    tv_sort_type.setText("按日期降序");
                    sortType = 1;
                }
                sortByDate();
                adapter.setList(dataList);
                adapter.notifyDataSetChanged();
                break;
            case R.id.radio_order0:
                type = 0;
                bottomDialog.checkItem(1,v.getId());
                break;
            case R.id.radio_order1:
                type = 1;
                bottomDialog.checkItem(1,v.getId());
                break;
            case R.id.radio_order2:
                type = 2;
                bottomDialog.checkItem(1,v.getId());
                break;
            case R.id.radio_order3:
                type = 3;
                bottomDialog.checkItem(1,v.getId());
                break;
            case R.id.radio_time0:
                timeSlot = 0;
                bottomDialog.checkItem(2,v.getId());
                break;
            case R.id.radio_time1:
                timeSlot = 1;
                bottomDialog.checkItem(2,v.getId());
                break;
            case R.id.radio_time2:
                timeSlot = 2;
                bottomDialog.checkItem(2,v.getId());
                break;
            case R.id.radio_time3:
                timeSlot = 3;
                bottomDialog.checkItem(2,v.getId());
                break;
            case R.id.radio_time4:
                timeSlot = 4;
                bottomDialog.checkItem(2,v.getId());
                break;
            case R.id.radio_time5:
                timeSlot = 5;
                bottomDialog.checkItem(2,v.getId());
                break;
            case R.id.btn_ok:
                bottomDialog.dismiss();
                getExternalOrderList();
                break;
            case R.id.btn_cancel:
                bottomDialog.dismiss();
                break;
        }
    }

    /**
     * 获取国内机票订单列表
     */
    private void getExternalOrderList() {
        Map<String,Object> params = new HashMap<>();
        params.put("ciphertext","test");
        User user = AppUtils.getUserInfo(getActivity());
        String employeeCode =  user.getEmployeeCode();
        params.put("employeeCode", employeeCode);
        params.put("loginType",URL.LOGIN_TYPE);
        params.put("timeSlot",timeSlot);
        params.put("employeeName",user.getEmployeeName());
        params.put("type",type);
        Retrofit retrofit = new RequestUtil().getRequestClient(URL.ZHONGCHE_93);
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        Call<Map<String,Object>> call = request.getExternalOrderList(params);
        new RequestUtil().requestData(call,this,1,getContext());
    }

    private void sortByDate(){
        Collections.sort(dataList,new SortByDate());
        for (Map<String,Object> map: dataList){
//            Log.i("*******",map.get("ORDERTIME")+"");
        }
    }

    @Override
    public void requestSuccess(Object map, int type) {
        Map<String,Object> result = (Map<String, Object>) map;
        if(null != result){
            String code = null == result.get("code") ? "" : result.get("code").toString();
//            String codeMsg = (String) result.get("codeMsg");

            if(code.equals("00000")) {
                List<Map<String,Object>> data = (ArrayList) result.get("data");
                dataList = data;
                sortByDate();
                adapter.setList(dataList);
                adapter.notifyDataSetChanged();
            }
            else {
                if(null != getActivity()){
                    ToastUtil.toastError(getActivity());
                }
            }
        }else{
            //请求失败
            if(null != getActivity()){
                ToastUtil.toastError(getActivity());
            }
        }
    }

    @Override
    public void requestFail(int type) {
        if(null != getActivity()){
            ToastUtil.toastFail(getActivity());
        }
    }

    @Override
    public void requestCancel(int type) {

    }

    class SortByDate implements Comparator {
        public int compare(Object o1, Object o2) {
            Map<String,Object> m1 = (Map<String,Object>) o1;
            Map<String,Object> m2 = (Map<String,Object>) o2;
            String firstDay = (String)m1.get("ORDERTIME");
            String secondDay = (String)m2.get("ORDERTIME");

            if (firstDay == null && secondDay == null)
                return 0;
            if (firstDay == null)
                return -1;
            if (secondDay == null)
                return 1;
            if(firstDay.equals("")){
                return -1;
            }
            if(secondDay.equals("")){
                return 1;
            }
            //boolean result = DateUtils.isFirstDayBeforeSecondDay((String)m1.get("ORDERTIME"),(String)m2.get("ORDERTIME"));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar =  Calendar.getInstance();
            try {
                Date d1 = sdf.parse(firstDay);
                calendar.setTime(d1);
                long time1 = calendar.getTimeInMillis();
                Date d2 = sdf.parse(secondDay);
                calendar.setTime(d2);
                long time2 = calendar.getTimeInMillis();
                if(sortType == 1){
                    return Long.valueOf(time1).compareTo(Long.valueOf(time2));
                }else{
                    return Long.valueOf(time2).compareTo(Long.valueOf(time1));
                }
            }catch (ParseException e){
                if (sortType == 1) {
                    return 1;
                } else {
                    return -1;
                }
            }

            /*if(result) {
                if (sortType == 1) {
                    return -1;
                } else {
                    return 1;
                }
            } else {
                if (sortType == 1) {
                    return 1;
                } else {
                    return -1;
                }
            }*/
        }
    }
}
