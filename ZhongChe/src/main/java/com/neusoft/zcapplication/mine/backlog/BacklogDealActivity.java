package com.neusoft.zcapplication.mine.backlog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.neusoft.zcapplication.Bean.User;
import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.base.BaseActivity;
import com.neusoft.zcapplication.entity.GetBacklogData;
import com.neusoft.zcapplication.event.Events;
import com.neusoft.zcapplication.http.NetWorkRequest;
import com.neusoft.zcapplication.http.RequestCallback;
import com.neusoft.zcapplication.http.RequestUtil;
import com.neusoft.zcapplication.http.URL;
import com.neusoft.zcapplication.tools.AppUtils;
import com.neusoft.zcapplication.tools.ToastUtil;
import com.neusoft.zcapplication.widget.DefinedListView;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * 我的代办，审批页面
 */
public class BacklogDealActivity extends BaseActivity implements View.OnClickListener,RequestCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_backlog_deal);
        initView();
    }

    private void initView(){
        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.btn_agree).setOnClickListener(this);
        findViewById(R.id.btn_refuse).setOnClickListener(this);

        AppUtils.setStateBar(BacklogDealActivity.this,findViewById(R.id.frg_status_bar));
        Intent dataIntent = getIntent();
        List<GetBacklogData.TripInfoBean> tripInfoList = (List<GetBacklogData.TripInfoBean>) dataIntent.getSerializableExtra("tripInfo");
        JobTripListAdapter adapter = new JobTripListAdapter(BacklogDealActivity.this,tripInfoList);
        DefinedListView listView = (DefinedListView)findViewById(R.id.act_backlog_deal_list);
        listView.setAdapter(adapter);

        String reason = dataIntent.getStringExtra("reason");
        String applicateName = dataIntent.getStringExtra("applicateName");
        String dateTime = dataIntent.getStringExtra("dateTime");

        TextView reasonTv = (TextView)findViewById(R.id.act_backlog_deal_reason);
        TextView nameTv = (TextView)findViewById(R.id.act_backlog_deal_name);
        TextView dateTimeTv = (TextView)findViewById(R.id.act_backlog_deal_date);
        reasonTv.setText(reason);
        nameTv.setText(applicateName);
        dateTimeTv.setText(dateTime);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_agree:
                approveBacklog("0");
                break;
            case R.id.btn_refuse:
                approveBacklog("1");
                break;
        }
    }

    /**
     * 审批我的待办
     * @param approveVal 0  同意 ；1 拒绝
     */
    private void approveBacklog(String approveVal) {
        EditText editText = (EditText)findViewById(R.id.act_backlog_txt);
        String reason ;
        String text = editText.getText().toString().trim();
        if(text.equals("")){
//            ToastUtil.toastNeedData(BacklogDealActivity.this,"请填写审批意见！");
            reason = approveVal.equals("0") ? "同意":"拒绝";
        }else{
            reason = text;
        }
            Map<String,Object> params = new HashMap<>();
            params.put("ciphertext","test");
            User user = AppUtils.getUserInfo(BacklogDealActivity.this);
            params.put("employeeCode", user.getEmployeeCode());
            params.put("loginType", URL.LOGIN_TYPE);
            params.put("approveVal",approveVal);
            params.put("id",getIntent().getStringExtra("id"));
            params.put("suggestion",reason);//审批意见
            params.put("type", getIntent().getIntExtra("type",1));//代办审批类别，1：预定申请单，2：核算主体变更，3：国际方案
            Retrofit retrofit = new RequestUtil().getRequestClient(URL.ZHONGCHE_93);
            NetWorkRequest request = retrofit.create(NetWorkRequest.class);
            Call<Map<String,Object>> call = request.approveOrder(params);
            new RequestUtil().requestData(call,this,1,mContext);
//        }
    }

    @Override
    public void requestSuccess(Object map, int type) {
        Map<String,Object> result = (Map<String, Object>) map;
        if(null != result){
            String code = null == result.get("code") ? "" : result.get("code").toString();
//            String codeMsg = (String) result.get("codeMsg");
            if(code.equals("00000")) {
                ToastUtil.toastHandleSuccess(BacklogDealActivity.this);
                EventBus.getDefault().post(new Events.ApplyVerifyChange());
                finish();
            }
            else {
                ToastUtil.toastHandleError(BacklogDealActivity.this);
            }
        }else{
            //请求失败
            ToastUtil.toastHandleError(BacklogDealActivity.this);
        }
    }

    @Override
    public void requestFail(int type) {
        ToastUtil.toastHandleFail(BacklogDealActivity.this);
    }

    @Override
    public void requestCancel(int type) {

    }
}
