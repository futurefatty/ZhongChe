package com.neusoft.zcapplication.mine.backlog;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.neusoft.zcapplication.Bean.User;
import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.api.OrderApi;
import com.neusoft.zcapplication.base.BaseFragment;
import com.neusoft.zcapplication.entity.GetBacklogData;
import com.neusoft.zcapplication.event.Events;
import com.neusoft.zcapplication.http.URL;
import com.neusoft.zcapplication.http.now.CallBack;
import com.neusoft.zcapplication.http.now.RetrofitFactory;
import com.neusoft.zcapplication.tools.AppUtils;
import com.neusoft.zcapplication.tools.DateUtils;
import com.neusoft.zcapplication.tools.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: TenzLiu
 * Time: 2018/6/14 13:55
 * Desc: 用车待办理
 */

public class ApplyVerifyFragment extends BaseFragment {

    private View fragmentView;
    private ListView lv_apply_verify;
    private BacklogListAdapter adapter;
    private List<GetBacklogData> dataList;

    private boolean isLoadDataFirst;

    public static ApplyVerifyFragment getInstance(){
        ApplyVerifyFragment fragment = new ApplyVerifyFragment();
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        //lazyLoadData();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(null == fragmentView){
            fragmentView = inflater.inflate(R.layout.fragment_apply_verify, container, false);
            initView();
        }else{
            ViewGroup parent = (ViewGroup) fragmentView.getParent();
            if(parent != null){
                parent.removeView(fragmentView);
            }
        }
        return fragmentView;
    }

    private void initView() {
        lv_apply_verify = (ListView) fragmentView.findViewById(R.id.lv_apply_verify);
        dataList = new ArrayList<>();
        adapter = new BacklogListAdapter(getActivity(),dataList,0);
        lv_apply_verify.setAdapter(adapter);
        lv_apply_verify.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext,BacklogDealActivity.class);
                GetBacklogData getBacklogData = adapter.getItem(position);
                String itemId = getBacklogData.getId();
                intent.putExtra("id",itemId);
                int type = 1;
                String typeStr = getBacklogData.getType();
                if (typeStr.equals("预定申请单")){
                    type = 1;
                }
                else if (typeStr.equals("核算主体变更")){
                    type = 2;
                }
                List<GetBacklogData.TripInfoBean> tripInfoList = getBacklogData.getTripInfo();
                intent.putExtra("type",type);
                intent.putExtra("tripInfo",(Serializable) tripInfoList);
                String reason = getBacklogData.getReason();
                String applicateName = getBacklogData.getApplicateName();
                String dateTime = getBacklogData.getDateTime();
                intent.putExtra("reason",reason);
                intent.putExtra("applicateName",applicateName);
                intent.putExtra("dateTime",dateTime);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        if(isLoadDataFirst){
            return;
        }
        isLoadDataFirst = true;
        getBacklogList();//获取数据
    }

    /**
     * 获取我的待办列表
     */
    private void getBacklogList() {
        Map<String,Object> params = new HashMap<>();
        params.put("ciphertext","test");
        User user = AppUtils.getUserInfo(mContext);
        params.put("employeeCode", user.getEmployeeCode());
        params.put("loginType", URL.LOGIN_TYPE);

        showLoading();
        RetrofitFactory.getInstance().createApi(OrderApi.class).getBacklogData(params)
                .enqueue(new CallBack<List<GetBacklogData>>() {
                    @Override
                    public void success(List<GetBacklogData> getBacklogDataList) {
                        dismissLoading();
                        if(getBacklogDataList.size()>0){
                            adapter.setList(sortByDate(getBacklogDataList));
                        }else{
                            showToast("列表数据为空");
                            adapter.setList(getBacklogDataList);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void fail(String code) {
                        dismissLoading();
                    }
                });
    }

    /**
     * 按时间排序
     */
    private List<GetBacklogData> sortByDate(List<GetBacklogData> list){
        Collections.sort(list,new Comparator<GetBacklogData>() {

            @Override

            public int compare(GetBacklogData compare,GetBacklogData bycompare) {
                boolean result = DateUtils.isFirstDayBeforeSecondDay(compare.getDateTime(),bycompare.getDateTime());
                if(result){
                    return 1;
                }else{
                    return -1;
                }

            }});
        return list;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Events.ApplyVerifyChange applyVerifyChange){
        getBacklogList();
    }

}
