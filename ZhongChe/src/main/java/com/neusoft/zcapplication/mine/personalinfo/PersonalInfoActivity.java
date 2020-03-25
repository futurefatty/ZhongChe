package com.neusoft.zcapplication.mine.personalinfo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.neusoft.zcapplication.Bean.User;
import com.neusoft.zcapplication.LoginActivity;
import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.base.BaseActivity;
import com.neusoft.zcapplication.tools.AppManagerUtil;
import com.neusoft.zcapplication.tools.AppUtils;

/**
 * 个人资料
 */
public class PersonalInfoActivity extends BaseActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        initView();
    }

    private void initView(){
        User user = AppUtils.getUserInfo(PersonalInfoActivity.this);
        findViewById(R.id.btn_personal_edit).setOnClickListener(this);
        findViewById(R.id.btn_personal_back).setOnClickListener(this);
        findViewById(R.id.btn_change_password).setOnClickListener(this);
        findViewById(R.id.btn_logout).setOnClickListener(this);

        TextView tv_employeeName = (TextView) findViewById(R.id.tv_employeeName);
        TextView tv_gender = (TextView) findViewById(R.id.tv_gender);
//        TextView tv_birthday = (TextView) findViewById(R.id.tv_birthday);
//        TextView tv_address = (TextView) findViewById(R.id.tv_address);
        TextView tv_mobil = (TextView) findViewById(R.id.tv_mobil);
        TextView tv_mail = (TextView) findViewById(R.id.tv_mail);
        tv_employeeName.setText(user.getEmployeeName());
        if("1".equals(user.getGender())){
            tv_gender.setText("男");
        }else if("2".equals(user.getGender())){
            tv_gender.setText("女");
        }
//        String birthStr = "null".equals(user.getBirthday())?"":user.getBirthday();
//        if(birthStr.length() > 10){
//            tv_birthday.setText(birthStr.substring(0,10));
//        }else{
//            tv_birthday.setText("");
//        }
//        tv_address.setText("null".equals(user.getAddress())?"":user.getAddress());
        tv_mobil.setText("null".equals(user.getMobil())?"":user.getMobil());
        tv_mail.setText("null".equals(user.getMail())?"":user.getMail());
        AppUtils.setStateBar(PersonalInfoActivity.this,findViewById(R.id.frg_status_bar));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_personal_edit:
                startActivityForResult(EditPersonalInfoActivity.class,101);
                break;
            case R.id.btn_change_password:
                startActivity(ChangePasswordActivity.class);
                break;
            case R.id.btn_personal_back:
                finish();
                break;
            case R.id.btn_logout:
                //用户点击退出登录按钮时，清除用户登录密码
                User user = AppUtils.getUserInfo(PersonalInfoActivity.this);
                user.setPassword("");
                AppUtils.saveUserInfo(PersonalInfoActivity.this,user);
                startActivity(LoginActivity.class);
                AppManagerUtil.getInstance().finishAllActivityExcept(LoginActivity.class);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 101){
            initView();
        }
    }

}
