package com.neusoft.zcapplication.mine.backlog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.neusoft.zcapplication.Bean.User;
import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.api.CarApi;
import com.neusoft.zcapplication.base.BaseFragment;
import com.neusoft.zcapplication.entity.GetApplyCarsAudit;
import com.neusoft.zcapplication.event.Events;
import com.neusoft.zcapplication.http.now.CallBack;
import com.neusoft.zcapplication.http.now.RetrofitFactory;
import com.neusoft.zcapplication.tools.AppUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: TenzLiu
 * Time: 2018/6/14 13:55
 * Desc:待审核
 */

public class BusinessCarVerifyFragment extends BaseFragment {

    private View fragmentView;
    private ListView lv_business_car_verify;
    private BusinessCarVerifyAdapter mBusinessCarVerifyAdapter;
    private List<GetApplyCarsAudit> dataList;

    public static BusinessCarVerifyFragment getInstance(){
        BusinessCarVerifyFragment fragment = new BusinessCarVerifyFragment();
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
            fragmentView = inflater.inflate(R.layout.fragment_business_car_verify, container, false);
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
        lv_business_car_verify = (ListView) fragmentView.findViewById(R.id.lv_business_car_verify);
        dataList = new ArrayList<>();
        mBusinessCarVerifyAdapter = new BusinessCarVerifyAdapter(getContext(),dataList);
        lv_business_car_verify.setAdapter(mBusinessCarVerifyAdapter);
        lv_business_car_verify.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GetApplyCarsAudit getApplyCarsAudit = mBusinessCarVerifyAdapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("getApplyCarsAudit",getApplyCarsAudit);
                startActivity(BusinessCarVerifyDetailActivity.class,bundle);
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        //getAllCheckApplyCars();//获取数据
    }

    /**
     * 获取我的待办列表
     */
    private void getAllCheckApplyCars() {
        Map<String,Object> params = new HashMap<>();
        User user = AppUtils.getUserInfo(mContext);
        params.put("auditorId", user.getEmployeeCode());

        showLoading();
        RetrofitFactory.getInstance().createApi(CarApi.class).getApplyCars_Audit(params)
                .enqueue(new CallBack<List<GetApplyCarsAudit>>() {
                    @Override
                    public void success(List<GetApplyCarsAudit> getApplyCarsAuditList) {
                        dismissLoading();
                        if(getApplyCarsAuditList.size()>0){
                            mBusinessCarVerifyAdapter.setList(getApplyCarsAuditList);
                        }else{
                            showToast("列表数据为空");
                            mBusinessCarVerifyAdapter.setList(getApplyCarsAuditList);
                        }
                        mBusinessCarVerifyAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void fail(String code) {
                        dismissLoading();
                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Events.BusinessCarVerifyChange businessCarVerifyChange){
        getAllCheckApplyCars();
    }

}
