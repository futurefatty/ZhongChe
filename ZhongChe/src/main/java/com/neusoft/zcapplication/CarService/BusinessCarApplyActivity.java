package com.neusoft.zcapplication.CarService;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.neusoft.zcapplication.Bean.User;
import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.api.CarApi;
import com.neusoft.zcapplication.base.BaseActivity;
import com.neusoft.zcapplication.entity.GetAllApplyUseCars;
import com.neusoft.zcapplication.event.Events;
import com.neusoft.zcapplication.http.URL;
import com.neusoft.zcapplication.http.now.CallBack;
import com.neusoft.zcapplication.http.now.RetrofitFactory;
import com.neusoft.zcapplication.tools.AppUtils;
import com.neusoft.zcapplication.tools.DateUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: TenzLiu
 * Time: 2018/6/19 14:45
 * Desc: 查询该申请人的所有用车申请单列表
 */

public class BusinessCarApplyActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_cancel,tv_delete;
    private LinearLayout btn_back;
    private ListView lv_business_car_apply;
    private BusinessCarApplyAdapter mBusinessCarApplyAdapter;
    private List<GetAllApplyUseCars> dataList;
    private boolean isEdit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_car_apply);
        EventBus.getDefault().register(this);
        initView();
        getAllApplyUseCars();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initView() {
        AppUtils.setStateBar(mContext,findViewById(R.id.frg_status_bar));
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_delete = (TextView) findViewById(R.id.tv_delete);
        btn_back = (LinearLayout) findViewById(R.id.btn_back);
        tv_cancel.setOnClickListener(this);
        tv_delete.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        findViewById(R.id.tv_apply_business_car).setOnClickListener(this);
        lv_business_car_apply = (ListView) findViewById(R.id.lv_business_car_apply);
        dataList = new ArrayList<>();
        mBusinessCarApplyAdapter = new BusinessCarApplyAdapter(mContext,dataList);
        lv_business_car_apply.setAdapter(mBusinessCarApplyAdapter);
        lv_business_car_apply.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(isEdit){
                    if(dataList.get(position).getAuditStatus()!=0){
                        showToast("只能删除待审批申请单");
                        return;
                    }
                    dataList.get(position).setCheck(!dataList.get(position).isCheck());
                    mBusinessCarApplyAdapter.notifyDataSetChanged();
                }else{
                    GetAllApplyUseCars getAllApplyUseCars = mBusinessCarApplyAdapter.getItem(position);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("getAllApplyUseCars",getAllApplyUseCars);
                    startActivity(BusinessCarApplyDetailActivity.class,bundle);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.tv_cancel:
                //取消
                for(int i=0;i<dataList.size();i++){
                    dataList.get(i).setCheck(false);
                }
                mBusinessCarApplyAdapter.setEdit(false);
                mBusinessCarApplyAdapter.notifyDataSetChanged();
                isEdit = false;
                tv_cancel.setVisibility(View.GONE);
                break;
            case R.id.tv_delete:
                //删除
                if(!isEdit){
                    mBusinessCarApplyAdapter.setEdit(true);
                    mBusinessCarApplyAdapter.notifyDataSetChanged();
                    tv_cancel.setVisibility(View.VISIBLE);
                    isEdit = true;
                }else{
                    delApplyCar();
                }
                break;
            case R.id.tv_apply_business_car:
                //创建申请单
                startActivity(BusinessCarApplyCreateOrderActivity.class);
                break;
        }
    }

    /**
     * 查询该申请人的所有用车申请单列表
     */
    private void getAllApplyUseCars() {
        Map<String,Object> params = new HashMap<>();
        User user = AppUtils.getUserInfo(mContext);
        params.put("applicatId", user.getEmployeeCode());

        showLoading();
        RetrofitFactory.getInstance().createApi(CarApi.class).getAllApplyUseCars(params)
                .enqueue(new CallBack<List<GetAllApplyUseCars>>() {
                    @Override
                    public void success(List<GetAllApplyUseCars> getAllApplyUseCarsList) {
                        dismissLoading();
                        dataList.clear();
                        dataList.addAll(getAllApplyUseCarsList);
                        if(getAllApplyUseCarsList.size()>0){
                            mBusinessCarApplyAdapter.setList(dataList);
                        }else{
                            showToast("列表数据为空");
                            mBusinessCarApplyAdapter.setList(dataList);
                        }
                        mBusinessCarApplyAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void fail(String code) {
                        dismissLoading();
                    }
                });
    }

    /**
     * 删除用车
     */
    private void delApplyCar() {
        StringBuilder sbIds = new StringBuilder();
        for(int i=0;i<dataList.size();i++){
            if(dataList.get(i).isCheck()){
                if(sbIds.length()==0){
                    sbIds.append(""+dataList.get(i).getId());
                }else{
                    sbIds.append(","+dataList.get(i).getId());
                }
            }
        }
        if(sbIds.length()==0){
            showToast("请选择申请单");
            return;
        }

        Map<String,Object> params = new HashMap<>();
        params.put("id", sbIds);

        showLoading();
        RetrofitFactory.getInstance().createApi(CarApi.class).delApplyCar(params)
                .enqueue(new CallBack<Object>() {
                    @Override
                    public void success(Object object) {
                        dismissLoading();
                        showToast("删除成功");
                        List<GetAllApplyUseCars> checkDataList = new ArrayList<>();
                        for(int i=0;i<dataList.size();i++){
                            if(dataList.get(i).isCheck()){
                                checkDataList.add(dataList.get(i));
                            }
                        }
                        dataList.removeAll(checkDataList);
                        mBusinessCarApplyAdapter.setEdit(false);
                        mBusinessCarApplyAdapter.notifyDataSetChanged();
                        isEdit = false;
                        tv_cancel.setVisibility(View.GONE);
                    }

                    @Override
                    public void fail(String code) {
                        dismissLoading();
                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Events.BusinessCarApplyChange businessCarApplyChange){
        for(int i=0;i<dataList.size();i++){
            dataList.get(i).setCheck(false);
        }
        mBusinessCarApplyAdapter.setEdit(false);
        mBusinessCarApplyAdapter.notifyDataSetChanged();
        isEdit = false;
        tv_cancel.setVisibility(View.GONE);
        getAllApplyUseCars();
    }

}
