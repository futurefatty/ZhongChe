package com.neusoft.zcapplication.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.neusoft.zcapplication.Bean.User;
import com.neusoft.zcapplication.Constant.Constant;
import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.api.OrderApi;
import com.neusoft.zcapplication.base.BaseActivity;
import com.neusoft.zcapplication.entity.QueryFeeback;
import com.neusoft.zcapplication.entity.QueryFeebackDetail;
import com.neusoft.zcapplication.http.now.CallBack;
import com.neusoft.zcapplication.http.now.RetrofitFactory;
import com.neusoft.zcapplication.tools.AppUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 意见与反馈
 */
public class SuggestAndFeedbackRecordDetailActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_content,tv_reply;
    private QueryFeeback mQueryFeeback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest_feedback_record_detail);
        initView();
        initData();
    }

    private void initView(){
        AppUtils.setStateBar(SuggestAndFeedbackRecordDetailActivity.this,findViewById(R.id.frg_status_bar));
        findViewById(R.id.btn_back).setOnClickListener(this);
        tv_content = (TextView) findViewById(R.id.tv_content);
        tv_reply = (TextView) findViewById(R.id.tv_reply);
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            mQueryFeeback = (QueryFeeback) bundle.getSerializable("queryFeeback");
            tv_content.setText(mQueryFeeback.getDetail());
        }
        if(mQueryFeeback != null){
            getParentData();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
        }
    }

    /**
     * 投诉与建议_获取当前用户反馈列表
     */
    private void getParentData() {
        User user = AppUtils.getUserInfo(SuggestAndFeedbackRecordDetailActivity.this);
        Map<String,Object> params = new HashMap<>();
        params.put("ciphertext","test");
        params.put("employeeCode", user.getEmployeeCode());
        params.put("loginType", Constant.APP_TYPE);
        params.put("suggestionsId", mQueryFeeback.getId());

        showLoading();
        RetrofitFactory.getInstance().createApi(OrderApi.class).getParent(params)
                .enqueue(new CallBack<List<QueryFeebackDetail>>() {
                    @Override
                    public void success(List<QueryFeebackDetail> queryFeebackDetailList) {
                        dismissLoading();
                        if(queryFeebackDetailList.size()>0){
                            tv_reply.setText(queryFeebackDetailList.get(0).getDetail());
                        }else{
                            tv_reply.setText("无");
                        }
                    }

                    @Override
                    public void fail(String code) {
                        dismissLoading();
                        tv_reply.setText("无");
                    }
                });
    }

}
