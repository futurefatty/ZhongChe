package com.neusoft.zcapplication.mine.authorization;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.neusoft.zcapplication.Bean.User;
import com.neusoft.zcapplication.Constant.Constant;
import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.api.OrderApi;
import com.neusoft.zcapplication.base.BaseActivity;
import com.neusoft.zcapplication.entity.GetAuthorizationInfo;
import com.neusoft.zcapplication.http.now.CallBack;
import com.neusoft.zcapplication.http.now.RetrofitFactory;
import com.neusoft.zcapplication.tools.AppUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的授权
 */
public class AuthorizationActivity extends BaseActivity implements View.OnClickListener,
        AuthorizationListAdapter.ClickEvent {

    private AuthorizationListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_authorization);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPersonList();
    }

    private void initView(){
        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.btn_authorization).setOnClickListener(this);

        List<GetAuthorizationInfo> list = new ArrayList<>();
        ListView listView  =  (ListView) findViewById(R.id.authorization_list);
        adapter = new AuthorizationListAdapter(AuthorizationActivity.this,list,0);
        listView.setAdapter(adapter);
        adapter.setClickEvent(this);

        AppUtils.setStateBar(AuthorizationActivity.this,findViewById(R.id.frg_status_bar));
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_authorization:
                intent = new Intent(AuthorizationActivity.this,AuthorizationSearchActivity.class);
                startActivityForResult(intent,101);
                break;
        }
    }

    /**
     * 获取已授权人员列表
     */
    private void getPersonList() {
        User user = AppUtils.getUserInfo(AuthorizationActivity.this);
        String employeeCode = user.getEmployeeCode();
        Map<String,Object> params = new HashMap<>();
        params.put("ciphertext","test");
        params.put("employeeCode", employeeCode);
        params.put("loginType", Constant.APP_TYPE);

        showLoading();
        RetrofitFactory.getInstance().createApi(OrderApi.class).getAuthorizationInfo(params)
                .enqueue(new CallBack<List<GetAuthorizationInfo>>() {
                    @Override
                    public void success(List<GetAuthorizationInfo> getAuthorizationInfoList) {
                        dismissLoading();
                        if(getAuthorizationInfoList.size()>0){
                            adapter.setList(getAuthorizationInfoList);
                        }else{
                            showToast("列表数据为空");
                            adapter.setList(getAuthorizationInfoList);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void fail(String code) {
                        dismissLoading();
                    }
                });
    }

    @Override
    public void cancelAuthorization(String beAgreeCode, final int position) {
        Map<String,Object> params = new HashMap<>();
        User user = AppUtils.getUserInfo(AuthorizationActivity.this);
        String employeeCode = user.getEmployeeCode();
        params.put("ciphertext","test");
        params.put("employeeCode", employeeCode);
        params.put("loginType", Constant.APP_TYPE);
        params.put("beAgreeCode", beAgreeCode);

        showLoading();
        RetrofitFactory.getInstance().createApi(OrderApi.class).cancelAuthorization(params)
                .enqueue(new CallBack<Object>() {
                    @Override
                    public void success(Object object) {
                        dismissLoading();
                        List<GetAuthorizationInfo> dataList = adapter.getList();
                        dataList.remove(position);
                        adapter.setList(dataList);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void fail(String code) {
                        dismissLoading();
                    }
                });
    }

}
