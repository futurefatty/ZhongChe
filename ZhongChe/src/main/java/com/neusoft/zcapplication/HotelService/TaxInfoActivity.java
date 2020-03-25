package com.neusoft.zcapplication.HotelService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.neusoft.zcapplication.Bean.User;
import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.base.BaseActivity;
import com.neusoft.zcapplication.http.NetWorkRequest;
import com.neusoft.zcapplication.http.RequestCallback;
import com.neusoft.zcapplication.http.RequestUtil;
import com.neusoft.zcapplication.http.URL;
import com.neusoft.zcapplication.tools.AppUtils;
import com.neusoft.zcapplication.tools.ToastUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;

public class TaxInfoActivity extends BaseActivity implements View.OnClickListener,RequestCallback {
    private TaxListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tax_information);
        initView();
    }

    private void initView(){
        List<Map<String,Object>> dataList = new ArrayList<>();
        ListView listView  =  (ListView)findViewById(R.id.frg_reimburse_list);
        adapter = new TaxListAdapter(TaxInfoActivity.this,dataList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String,Object> item = adapter.getItem(position);
                Intent intent = new Intent(TaxInfoActivity.this,TaxDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("item",(Serializable) item);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.btn_add_reimburse).setOnClickListener(this);

        getTaxInfoList("");
        AppUtils.setStateBar(TaxInfoActivity.this,findViewById(R.id.frg_status_bar));

        EditText editText  = (EditText)findViewById(R.id.et_search_tax_info);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    String text = v.getText().toString().trim();
                    if(text.length() > 0){
                        getTaxInfoList(text);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_add_reimburse:
                intent = new Intent(TaxInfoActivity.this,AddTaxInfoActivity.class);
                startActivityForResult(intent,101);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101){
            getTaxInfoList("");
        }
    }

    /**
     * 获取税票信息列表
     */
    private void getTaxInfoList(String keyword) {
        User user = new AppUtils().getUserInfo(TaxInfoActivity.this);
        String employeeCode = user.getEmployeeCode();
        Map<String,Object> params = new HashMap<>();
        params.put("ciphertext","test");
        params.put("employeeCode", employeeCode);
        params.put("loginType", URL.LOGIN_TYPE);
        params.put("keyword", keyword);
        Retrofit retrofit = new RequestUtil().getRequestClient(URL.ZHONGCHE_93);
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        Call<Map<String,Object>> call = request.getTaxInfoList(params);
        new RequestUtil().requestData(call,this,1,mContext);
    }

    @Override
    public void requestSuccess(Object map, int type) {
        Map<String,Object> result = (Map<String, Object>) map;
        if(null != result){
            String code = null  == result.get("code") ? "" : result.get("code").toString();
            String codeMsg = null  == result.get("codeMsg") ? "" : result.get("codeMsg").toString();
            if(code.equals("00000")) {
                List<Map<String,Object>> data = (ArrayList) result.get("data");
                adapter.setList(data);
                adapter.notifyDataSetChanged();
            }else{
                ToastUtil.toastError(TaxInfoActivity.this);
            }
        }else{
            //请求失败
            ToastUtil.toastError(TaxInfoActivity.this);
        }
    }

    @Override
    public void requestFail(int type) {
        ToastUtil.toastFail(TaxInfoActivity.this);
    }

    @Override
    public void requestCancel(int type) {

    }
}
