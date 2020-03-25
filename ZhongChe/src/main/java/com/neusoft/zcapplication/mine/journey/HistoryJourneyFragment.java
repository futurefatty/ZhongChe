package com.neusoft.zcapplication.mine.journey;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.neusoft.zcapplication.Bean.User;
import com.neusoft.zcapplication.Constant.Constant;
import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.base.BaseFragment;
import com.neusoft.zcapplication.http.NetWorkRequest;
import com.neusoft.zcapplication.http.RequestCallback;
import com.neusoft.zcapplication.http.RequestUtil;
import com.neusoft.zcapplication.http.URL;
import com.neusoft.zcapplication.tools.AppUtils;
import com.neusoft.zcapplication.tools.DateUtils;
import com.neusoft.zcapplication.tools.ToastUtil;
import com.neusoft.zcapplication.widget.BottomDialogForJourney;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * 历史行程
 */
public class HistoryJourneyFragment extends BaseFragment implements View.OnClickListener,RequestCallback {
    private BottomDialogForJourney bottomDialog;
    private int sortType = 2;//排序方式，1-按时间升序，2-按时间降序
    private TextView tv_sort_type;
    private int type = 0,timeSlot = 5;//查询订单参数，订单类型、时间
    private View frgView;
    private NowJourneyAdapter adapter;

    public static HistoryJourneyFragment getInstance(){
        HistoryJourneyFragment fragment = new HistoryJourneyFragment();
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    protected void initData() {
        super.initData();
        getData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(null == frgView){
            frgView = inflater.inflate(R.layout.frg_history_journey, container, false);
            initView();

        }else{
            ViewGroup parent = (ViewGroup) frgView.getParent();
            if(parent != null){
                parent.removeView(frgView);
            }
        }
        return frgView;
    }

    private void initView(){
        ListView listView  =  (ListView)frgView.findViewById(R.id.frg_history_list);
        List<Map<String,Object>> list = new ArrayList<>();
        adapter = new NowJourneyAdapter(getActivity(),1,list);
        listView.setAdapter(adapter);

        tv_sort_type = (TextView)frgView.findViewById(R.id.tv_sort_type);
        frgView.findViewById(R.id.btn_filter).setOnClickListener(this);
        frgView.findViewById(R.id.btn_sort).setOnClickListener(this);
        bottomDialog = new BottomDialogForJourney(getActivity(),this,2);
    }

    private void getData(){
        Retrofit retrofit = new RequestUtil().getRequestClient(URL.ZHONGCHE_93);
        Map<String,Object> params = new HashMap<>();
        params.put("ciphertext","string");
        User user = new AppUtils().getUserInfo(getActivity());
        String employeeCode =  user.getEmployeeCode();
        params.put("employeeCode",employeeCode);
//        params.put("employeeCode","20139232");
        params.put("loginType", Constant.APP_TYPE);
        params.put("type", type);
        params.put("timeSlot", timeSlot);

        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        Call<Map<String,Object>> call = request.getHistoryScheduleData(params);
        new RequestUtil().requestData(call,this,1,getContext());
    }

    @Override
    public void onClick(View v) {
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
                adapter.setList(sortByDate(adapter.getList()));
                adapter.notifyDataSetChanged();
                break;
            case R.id.btn_ok:
                getData();
                bottomDialog.dismiss();
                break;
            case R.id.btn_cancel:
                bottomDialog.dismiss();
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
        }
    }

    /**
     * 按时间排序
     */
    private List<Map<String,Object>> sortByDate(List<Map<String,Object>> list){
        List<Map<String,Object>> retList = list;
        Collections.sort(retList,new SortByDate());
        for (Map<String,Object> map: retList){
//            Log.i("*******",map.get("fromDate")+"");
        }

        return retList;
    }

    @Override
    public void requestSuccess(Object map, int type) {
        Map<String,Object> result = (Map<String, Object>) map;
        if(null != result){
            String code = null == result.get("code") ? "" : result.get("code").toString();
//            String codeMsg = (String) result.get("codeMsg");
//            Log.i("--->","返回结果：" + codeMsg);
            if(code.equals("00000")) {
                List<Map<String,Object>> list = (List<Map<String,Object>>)result.get("data");
//                Log.i("--->","返回结果：" + list.size());
                adapter.setList(sortByDate(list));
                adapter.notifyDataSetChanged();

            }else {
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
            boolean result = DateUtils.isFirstDayBeforeSecondDay((String)m1.get("fromDate"),(String)m2.get("fromDate"));
            if(result) {
                if (sortType == 1) {
                    return -1;
                }
                else {
                    return 1;
                }
            }
            else {
                if (sortType == 1) {
                    return 1;
                }
                else {
                    return -1;
                }
            }
        }
    }
}
