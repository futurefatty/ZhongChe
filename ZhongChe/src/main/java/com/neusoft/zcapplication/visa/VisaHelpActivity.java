package com.neusoft.zcapplication.visa;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.base.BaseActivity;
import com.neusoft.zcapplication.http.NetWorkRequest;
import com.neusoft.zcapplication.http.RequestCallback;
import com.neusoft.zcapplication.http.RequestUtil;
import com.neusoft.zcapplication.http.URL;
import com.neusoft.zcapplication.tools.AppUtils;
import com.neusoft.zcapplication.tools.ToastUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * 签证详情
 **/
public class VisaHelpActivity extends BaseActivity implements View.OnClickListener,RequestCallback {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visa_help_detail);
        initView();
        getData();
    }

    private void initView(){
        findViewById(R.id.btn_back).setOnClickListener(this);
        AppUtils.setStateBar(VisaHelpActivity.this,findViewById(R.id.frg_status_bar));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
        }
    }
    private void getData(){
        Map<String,Object> params = new HashMap<>();
        params.put("ciphertext","test");
        String code = getIntent().getStringExtra("code");
        params.put("abbr",code);
        params.put("loginType", URL.LOGIN_TYPE);
        Retrofit retrofit = new RequestUtil().getRequestClient(URL.ZHONGCHE_93);
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        Call<Map<String,Object>> call = request.queryVisaByAbbr(params);
        new RequestUtil().requestData(call,this,1,mContext);
    }

    @Override
    public void requestSuccess(Object obj, int type) {
        Map<String,Object> result = (Map<String, Object>) obj;
        if(null != result){
            String code = null == result.get("code") ? "" : result.get("code").toString();
//            String codeMsg = (String) result.get("codeMsg");
            if(code.equals("00000")) {
                List<Map<String,Object>> list = (List<Map<String, Object>>) result.get("data");
                if(list.size() > 0){
                    Map<String,Object> map  = list.get(0);
                    String VISACONTENT = null ==  map.get("VISACONTENT") ?
                            "" : map.get("VISACONTENT").toString();
                    String COUNTRYNAME = null ==  map.get("COUNTRYNAME") ?
                            "" : map.get("COUNTRYNAME").toString();
                    TextView textView = (TextView)findViewById(R.id.visa_help_detail_txt);
                    TextView countryTv = (TextView)findViewById(R.id.visa_help_detail_country);
                    textView.setText(VISACONTENT.replace("\\n","\n"));
                    countryTv.setText(COUNTRYNAME);

                }else{
                    ToastUtil.toastHandleError(VisaHelpActivity.this);
                }

            }else{
                ToastUtil.toastHandleError(VisaHelpActivity.this);
            }
        }else{
            //请求失败
            ToastUtil.toastHandleError(VisaHelpActivity.this);
        }
    }

    @Override
    public void requestFail(int type) {
        ToastUtil.toastHandleFail(VisaHelpActivity.this);
    }

    @Override
    public void requestCancel(int type) {

    }
}
