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
import com.neusoft.zcapplication.entity.GetApplyCarsDeal;
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
 * Desc:代办理
 */

public class BusinessCarHandleFragment extends BaseFragment {

    private View fragmentView;
    private ListView lv_business_car_handle;
    private BusinessCarHandleAdapter mBusinessCarHandleAdapter;
    private List<GetApplyCarsDeal> dataList;

    public static BusinessCarHandleFragment getInstance(){
        BusinessCarHandleFragment fragment = new BusinessCarHandleFragment();
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
            fragmentView = inflater.inflate(R.layout.fragment_business_car_handle, container, false);
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
        lv_business_car_handle = (ListView) fragmentView.findViewById(R.id.lv_business_car_handle);
        dataList = new ArrayList<>();
        mBusinessCarHandleAdapter = new BusinessCarHandleAdapter(getContext(),dataList);
        lv_business_car_handle.setAdapter(mBusinessCarHandleAdapter);
        lv_business_car_handle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GetApplyCarsDeal getApplyCarsDeal = mBusinessCarHandleAdapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("getApplyCarsDeal",getApplyCarsDeal);
                startActivity(BusinessCarHandleDetailActivity.class,bundle);
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
        RetrofitFactory.getInstance().createApi(CarApi.class).getApplyCars_Deal(params)
                .enqueue(new CallBack<List<GetApplyCarsDeal>>() {
                    @Override
                    public void success(List<GetApplyCarsDeal> getApplyCarsDealList) {
                        dismissLoading();
                        if(getApplyCarsDealList.size()>0){
                            mBusinessCarHandleAdapter.setList(getApplyCarsDealList);
                        }else{
                            showToast("列表数据为空");
                            mBusinessCarHandleAdapter.setList(getApplyCarsDealList);
                        }
                        mBusinessCarHandleAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void fail(String code) {
                        dismissLoading();
                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Events.BusinessCarHandleChange businessCarHandleChange){
        getAllCheckApplyCars();
    }

}
