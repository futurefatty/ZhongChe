package com.neusoft.zcapplication.mine.personalinfo;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.neusoft.zcapplication.Bean.User;
import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.api.MemberApi;
import com.neusoft.zcapplication.base.BaseActivity;
import com.neusoft.zcapplication.http.URL;
import com.neusoft.zcapplication.http.now.CallBack;
import com.neusoft.zcapplication.http.now.RetrofitFactory;
import com.neusoft.zcapplication.tools.AppUtils;
import com.neusoft.zcapplication.tools.Base64Utils;
import com.neusoft.zcapplication.tools.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 修改密码
 */
public class ChangePasswordActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        initView();
    }

    private void initView(){
        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.btn_confirm).setOnClickListener(this);
        AppUtils.setStateBar(ChangePasswordActivity.this,findViewById(R.id.frg_status_bar));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_confirm:
                EditText oldPassword = (EditText)findViewById(R.id.et_old_password);
                EditText newPassword = (EditText)findViewById(R.id.et_new_password);
                EditText confirmPassword = (EditText)findViewById(R.id.et_confirm);
                String opassword = oldPassword.getText().toString();
                String npassword = newPassword.getText().toString();
                String cpassword = confirmPassword.getText().toString();
                if (StringUtil.isEmpty(opassword)){
                    showToast("请填写原密码");
                    return;
                }
                if (StringUtil.isEmpty(npassword)){
                    showToast("请填写新密码");
                    return;
                }
                if (StringUtil.isEmpty(cpassword)){
                    showToast("请填写确认密码");
                    return;
                }
                if (!npassword.equals(cpassword)){
                    showToast("您两次输入的密码不一致，请检查并重新输入");
                    return;
                }
                opassword = Base64Utils.base64Encode(Base64Utils.base64Encode(opassword.getBytes()).getBytes());
                npassword = Base64Utils.base64Encode(Base64Utils.base64Encode(npassword.getBytes()).getBytes());
                setPassword(opassword,npassword);
                break;
        }
    }

    /**
     * 修改密码
     */
    private void setPassword(String oldPassword,String newPassword){
        User user = AppUtils.getUserInfo(ChangePasswordActivity.this);
        Map<String,Object> params = new HashMap<>();
        params.put("ciphertext","test");
        params.put("employeeCode",user.getEmployeeCode());
        params.put("loginType", URL.LOGIN_TYPE);
        params.put("oldPassword",oldPassword);
        params.put("newPassword",newPassword);

        showLoading();
        RetrofitFactory.getInstance().createApi(MemberApi.class).setPassword(params)
                .enqueue(new CallBack<Object>() {
                    @Override
                    public void success(Object object) {
                        dismissLoading();
                        showToastSuccess();
                        finish();
                    }

                    @Override
                    public void fail(String code) {
                        dismissLoading();
                    }
                });

    }

}
