package com.neusoft.zcapplication.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.neusoft.zcapplication.Bean.User;
import com.neusoft.zcapplication.Constant.Constant;
import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.api.OrderApi;
import com.neusoft.zcapplication.base.BaseActivity;
import com.neusoft.zcapplication.entity.QueryFeeback;
import com.neusoft.zcapplication.http.now.CallBack;
import com.neusoft.zcapplication.http.now.RetrofitFactory;
import com.neusoft.zcapplication.tools.AppUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 意见与反馈
 */
public class SuggestAndFeedbackRecordActivity extends BaseActivity implements View.OnClickListener {

    private ListView lv_feedback_record;
    private FeedbackRecordAdapter mFeedbackRecordAdapter;
    private List<QueryFeeback> mQueryFeebackList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest_feedback_record);
        initView();
        initData();
    }

    private void initView(){
        AppUtils.setStateBar(SuggestAndFeedbackRecordActivity.this,findViewById(R.id.frg_status_bar));
        findViewById(R.id.btn_back).setOnClickListener(this);
        lv_feedback_record = (ListView) findViewById(R.id.lv_feedback_record);
        mQueryFeebackList = new ArrayList<>();
        mFeedbackRecordAdapter = new FeedbackRecordAdapter(mContext,mQueryFeebackList);
        lv_feedback_record.setAdapter(mFeedbackRecordAdapter);
        lv_feedback_record.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("queryFeeback",mQueryFeebackList.get(position));
                startActivity(SuggestAndFeedbackRecordDetailActivity.class,bundle);
            }
        });
    }

    private void initData() {
        queryFeeback();
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
    private void queryFeeback() {
        User user = AppUtils.getUserInfo(SuggestAndFeedbackRecordActivity.this);
        Map<String,Object> params = new HashMap<>();
        params.put("ciphertext","test");
        params.put("employeeCode", user.getEmployeeCode());
        params.put("loginType", Constant.APP_TYPE);

        showLoading();
        RetrofitFactory.getInstance().createApi(OrderApi.class).queryFeeback(params)
                .enqueue(new CallBack<List<QueryFeeback>>() {
                    @Override
                    public void success(List<QueryFeeback> queryFeebackList) {
                        dismissLoading();
                        mQueryFeebackList.clear();
                        mQueryFeebackList.addAll(queryFeebackList);
                        mFeedbackRecordAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void fail(String code) {
                        dismissLoading();
                    }
                });
    }

}
