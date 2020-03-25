package com.neusoft.zcapplication.mine.personalinfo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.neusoft.zcapplication.Bean.User;
import com.neusoft.zcapplication.Constant.Constant;
import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.api.MemberApi;
import com.neusoft.zcapplication.base.BaseActivity;
import com.neusoft.zcapplication.http.now.CallBack;
import com.neusoft.zcapplication.http.now.RetrofitFactory;
import com.neusoft.zcapplication.tools.AppUtils;
import com.neusoft.zcapplication.widget.ClearIconEditText;

import java.util.HashMap;
import java.util.Map;


public class EditPersonalInfoActivity extends BaseActivity implements View.OnClickListener{

    private ClearIconEditText et_birthday1,et_address1,et_mobil,et_mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_personal_info);
        initView();
    }

    private void initView(){
        User user = AppUtils.getUserInfo(EditPersonalInfoActivity.this);
        
        findViewById(R.id.btn_personal_save).setOnClickListener(this);
        findViewById(R.id.btn_personal_back).setOnClickListener(this);

        TextView tv_employeeName = (TextView) findViewById(R.id.et_employeeName);
        TextView tv_gender = (TextView) findViewById(R.id.et_gender);
//        TextView et_birthday = (TextView) findViewById(R.id.et_birthday);
//        et_address = (ClearIconEditText) findViewById(R.id.et_address);
        et_mobil = (ClearIconEditText) findViewById(R.id.et_mobil);
        et_mail = (ClearIconEditText) findViewById(R.id.et_mail);

        tv_employeeName.setText(user.getEmployeeName());
        if("1".equals(user.getGender())){
            tv_gender.setText("男");
        }else if("2".equals(user.getGender())){
            tv_gender.setText("女");
        }
//        String birthStr = "null".equals(user.getBirthday())?"":user.getBirthday();
//        if(birthStr.length() > 10){
//            et_birthday.setText(birthStr.substring(0,10));
//        }else{
//            et_birthday.setText("");
//        }
//        et_address.setText("null".equals(user.getAddress())?"":user.getAddress());
        et_mobil.setText("null".equals(user.getMobil())?"":user.getMobil());
        et_mail.setText("null".equals(user.getMail())?"":user.getMail());
        AppUtils.setStateBar(EditPersonalInfoActivity.this,findViewById(R.id.frg_status_bar));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_personal_save:
                updateEmployeeInfo();
                break;
            case R.id.btn_personal_back:
                finish();
                break;

        }
    }

    /**
     * 编辑完成保存
     */
    private void updateEmployeeInfo() {
        User user = AppUtils.getUserInfo(EditPersonalInfoActivity.this);
        String telStr = et_mobil.getText().toString().trim();
        String emailStr = et_mail.getText().toString().trim();
        if(telStr.equals("") || telStr.length()!=11){
            showToast("请输入正确的联系电话!");
            return;
        }
        if(emailStr.equals("") || !AppUtils.isEmail(emailStr)){
            showToast("请输入正确的邮箱地址!");
            return;
        }
        Map<String,Object> params = new HashMap<>();
        params.put("ciphertext","test");
        params.put("employeeCode", user.getEmployeeCode());
        params.put("employeeName", user.getEmployeeName());
        params.put("gender", user.getGender());
        params.put("birthday", user.getBirthday());
        params.put("address", user.getAddress());
        params.put("mobil", telStr);
        params.put("mail", emailStr);
        params.put("loginType", Constant.APP_TYPE);

        showLoading();
        RetrofitFactory.getInstance().createApi(MemberApi.class).updateEmployeeInfo(params)
                .enqueue(new CallBack<Object>() {
                    @Override
                    public void success(Object object) {
                        dismissLoading();
                        showToastSuccess();
                        //修改用户信息成功，保存信息到本地
                        User user = AppUtils.getUserInfo(EditPersonalInfoActivity.this);
//                      user.setAddress(et_address.getText().toString());
                        user.setMail(et_mail.getText().toString());
                        user.setMobil(et_mobil.getText().toString());
//                      user.setBirthday(et_birthday.getText().toString());
//                      TextView et_birthday = (TextView) findViewById(R.id.et_birthday);
//                      user.setBirthday(et_birthday.getText().toString());
                        AppUtils.saveUserInfo(EditPersonalInfoActivity.this,user);
                        Intent intent = new Intent(EditPersonalInfoActivity.this,PersonalInfoActivity.class);
                        setResult(101,intent);
                        finish();
                    }

                    @Override
                    public void fail(String code) {
                        dismissLoading();
                    }
                });
    }

}
