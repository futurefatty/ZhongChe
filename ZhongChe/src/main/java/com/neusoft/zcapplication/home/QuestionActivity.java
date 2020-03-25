package com.neusoft.zcapplication.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.base.BaseActivity;
import com.neusoft.zcapplication.http.NetWorkRequest;
import com.neusoft.zcapplication.http.RequestCallback;
import com.neusoft.zcapplication.http.RequestUtil;
import com.neusoft.zcapplication.http.URL;
import com.neusoft.zcapplication.tools.AppUtils;
import com.neusoft.zcapplication.tools.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * 常见问题
 */

public class QuestionActivity extends BaseActivity implements View.OnClickListener,RequestCallback{

    private TabLayout tab_question;
    private EditText et_keyword;
    private QuestionAdapter adapter;
    private Call<Map<String,Object>> questionCall;

    private String keyword;
    private String questionType;
    private String isHot;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        initView();
        initData();
        getQuestionsData(keyword);
    }

    private void initData() {
        keyword = "";
        questionType = "";
        isHot = "";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(null != questionCall && questionCall.isExecuted()){
            questionCall.isCanceled();
        }
    }

    private void initView(){
        findViewById(R.id.btn_back).setOnClickListener(this);
        tab_question = (TabLayout) findViewById(R.id.tab_question);
        AppUtils.setStateBar(QuestionActivity.this,findViewById(R.id.frg_status_bar));

        ListView listView = (ListView)findViewById(R.id.list_view_question);
        List<Map<String,Object>> list = new ArrayList<>();
        adapter = new QuestionAdapter(list,QuestionActivity.this);
        listView.setAdapter(adapter);

        et_keyword = (EditText)findViewById(R.id.et_search_question);
        et_keyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    keyword = v.getText().toString().trim();
                    getQuestionsData(keyword);
                    return true;
                }
                return false;
            }
        });
        tab_question.removeAllTabs();
        tab_question.addTab(tab_question.newTab().setText("所有"));
        tab_question.addTab(tab_question.newTab().setText("机票"));
        tab_question.addTab(tab_question.newTab().setText("酒店"));
        tab_question.addTab(tab_question.newTab().setText("签证"));
        tab_question.addTab(tab_question.newTab().setText("旅游"));
        tab_question.addTab(tab_question.newTab().setText("热门"));
        tab_question.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        questionType = "";
                        isHot = "";
                        break;
                    case 1:
                        questionType = "1";
                        isHot = "";
                        break;
                    case 2:
                        questionType = "2";
                        isHot = "";
                        break;
                    case 3:
                        questionType = "3";
                        isHot = "";
                        break;
                    case 4:
                        questionType = "4";
                        isHot = "";
                        break;
                    case 5:
                        questionType = "";
                        isHot = "1";
                        break;
                }
                keyword = et_keyword.getText().toString().trim();
                getQuestionsData(keyword);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void requestSuccess(Object map, int type) {
        if(type == 1){
            Map<String,Object> result = (Map<String, Object>) map;
            String code = null == result.get("code") ? "" :result.get("code").toString();
            if(code.equals("00000")){
                Object listObj = result.get("data");
                if(null == listObj){
                    ToastUtil.toastFail(QuestionActivity.this);
                }else{
                    List<Map<String,Object>> list  = (List<Map<String,Object>>)listObj;
                    adapter.setList(list);
                    adapter.notifyDataSetChanged();
                    if(list.size() == 0){
                        ToastUtil.toastNoData(QuestionActivity.this );
                    }
                }
            }else{
//                ToastUtil.toastNeedData(QuestionActivity.this,"获取数据失败~");
                ToastUtil.toastFail(QuestionActivity.this);
            }
        }
    }

    @Override
    public void requestFail(int type) {
        if(type == 1){
            ToastUtil.toastFail(QuestionActivity.this);
        }
    }

    @Override
    public void requestCancel(int type) {

    }

    private void getQuestionsData(String keyword) {
        Map<String,Object> params = new HashMap<>();
        params.put("ciphertext","test");
        params.put("loginType", URL.LOGIN_TYPE);
        params.put("keyword", keyword);
        params.put("questionType", questionType);
        params.put("isHot", isHot);
        Retrofit retrofit = new RequestUtil().getRequestClient(URL.ZHONGCHE_93);
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        questionCall = request.queryQuestions(params);
        new RequestUtil().requestData(questionCall,this,1,"加载中...",true,QuestionActivity.this);
    }
}
