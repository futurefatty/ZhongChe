package com.neusoft.zcapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.crcc.commonlib.utils.JSONUtils;
import com.neusoft.zcapplication.Bean.User;
import com.neusoft.zcapplication.Constant.Constant;
import com.neusoft.zcapplication.api.MemberApi;
import com.neusoft.zcapplication.base.BaseActivity;
import com.neusoft.zcapplication.entity.GetVersion;
import com.neusoft.zcapplication.entity.Login;
import com.neusoft.zcapplication.gestureLock.GestureVerifyActivity;
import com.neusoft.zcapplication.http.URL;
import com.neusoft.zcapplication.http.now.CallBack;
import com.neusoft.zcapplication.http.now.RetrofitFactory;
import com.neusoft.zcapplication.tools.AlertUtil;
import com.neusoft.zcapplication.tools.AppUtils;
import com.neusoft.zcapplication.tools.Base64Utils;
import com.neusoft.zcapplication.tools.LogUtil;
import com.neusoft.zcapplication.tools.StringUtil;
import com.neusoft.zcapplication.update.UpdateApk;
import com.neusoft.zcapplication.widget.ClearIconEditText;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.functions.Consumer;

/**
 * 登录
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private ClearIconEditText et_username, et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        AppUtils.setStateBar(mContext, findViewById(R.id.frg_status_bar));
        AppUtils.clearCityHistory(LoginActivity.this);//清除城市缓存
        //检查版本是否需要检查手机读写权限6.0
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            requestPermissions();
        } else {
            checkAppVersion();
        }
    }

    /**
     * 申请权限
     */
    private void requestPermissions() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) throws Exception {
                        if (!granted) {
                            showToast("app未能获取全部相关权限");
                            finish();
                        } else {
                            checkAppVersion();
                        }
                    }
                });
    }

    /**
     *
     */
    private void initView() {
        findViewById(R.id.btn_login).setOnClickListener(this);
        et_username = (ClearIconEditText) findViewById(R.id.et_username);
        et_password = (ClearIconEditText) findViewById(R.id.et_password);
        User user = AppUtils.getUserInfo(LoginActivity.this);
        String employeeCode = user.getEmployeeCode();
        if (!employeeCode.equals("")) {
            et_username.setText(employeeCode);
            et_username.setSelection(employeeCode.length());
        } else {
            et_username.setText("");
            et_password.setText("");
        }
        //获取版本号
        try {
            String versionName = LoginActivity.this.getPackageManager()
                    .getPackageInfo(LoginActivity.this.getPackageName(), 0).versionName;
            TextView versionTv = (TextView) findViewById(R.id.act_login_version);
            versionTv.setText(String.format("版本号%s", versionName));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                String employeeCode = et_username.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                if (StringUtil.isEmpty(employeeCode)) {
                    Toast.makeText(LoginActivity.this, "请输入工号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (URL.IP.contains("http://58.20.212.75:8001")) {
                    //测试环境不用手动输入密码
                    password = "12";
                } else if (StringUtil.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                login(employeeCode, password);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null != data) {
            //手势密码返回
            if (requestCode == 1009) {
                User user = AppUtils.getUserInfo(LoginActivity.this);
                String employeeCode = user.getEmployeeCode();
                String password = user.getPassword();
                login(employeeCode, password);
            }
        }
    }

    /**
     * 登录方法
     */
    private void login(String employeeCode, final String password) {
        String base64Password = Base64Utils.base64Encode(Base64Utils.base64Encode(password.getBytes()).getBytes());
        Map<String, Object> params = new HashMap<>();
        params.put("ciphertext", "test");
        params.put("employeeCode", employeeCode);
        params.put("loginType", Constant.APP_TYPE);
        params.put("password", base64Password);

        showLoading();
        Map<String, String> params1 = new HashMap<>();
        params1.put("uaccount", "emhvbmdkdw==");
        params1.put("upaswd", "MTIzNDU2");

        LogUtil.d(JSONUtils.gsonString(params));

//        OkGo.post("http://127.0.0.1:8103/accountf/login/process").params(params1).headers("Content-Type","application/x-www-form-urlencoded; charset=UTF-8")
//                .execute(new AbsCallback<Object>() {
//                    @Override
//                    public void onSuccess(Object o, okhttp3.Call call, okhttp3.Response response) {
//                        showToast("登录成功");
//                    }
//
//                    @Override
//                    public void onError(okhttp3.Call call, okhttp3.Response response, Exception e) {
//                        showToast("登录失败");
//                        super.onError(call, response, e);
//                    }
//
//                    @Override
//                    public Object convertSuccess(okhttp3.Response response) {
//                        return null;
//                    }
//                });


        RetrofitFactory.getInstance().createApi(MemberApi.class).login(params)
                .enqueue(new CallBack<Login>() {
                    @Override
                    public void success(Login login) {
                        dismissLoading();
                        showToast("登录成功");
                        //保存用户信息
                        User user = new User();
                        user.setEmployeeName(login.getEmployeeName());
                        user.setId(login.getId());
                        user.setEmployeeCode(login.getEmployeeCode());
                        user.setAddress(login.getAddress());
                        user.setBirthday(login.getBirthday());
                        user.setMobil(login.getMobil());
                        user.setMail(login.getMail());
                        user.setGender(login.getGender());
                        user.setIdCard(login.getIdCard());
                        user.setPayCode(login.getPayCode());
                        user.setUnitCode(login.getUnitCode());
                        user.setUnitName(login.getUnitName());
                        //用户输入的密码
                        user.setPassword(password);
                        AppUtils.saveUserInfo(LoginActivity.this, user);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void fail(String code) {
                        dismissLoading();
                    }
                });
    }

    /**
     * 检查版本更新
     */
    private void checkAppVersion() {
        showLoading();
        RetrofitFactory.getInstance().createApi(MemberApi.class).getNewVersion(1)
                .enqueue(new CallBack<List<GetVersion>>() {
                    @Override
                    public void success(List<GetVersion> response) {
                        dismissLoading();
                        boolean isShouldContinue = true;
                        for (GetVersion getVersion : response) {
                            //type ：1安卓，0 ios
                            if ("1".equals(getVersion.getType())) {
                                String version = getVersion.getVersion();
                                int appVersion = AppUtils.getVersionCode(mContext);
                                int versionInt = Integer.parseInt(version);
                                if (appVersion < versionInt) {
                                    isShouldContinue = false;
                                    AlertUtil.unCancelableDialog(LoginActivity.this,
                                            "检测到新版本，是否下载？", "确定",
                                            new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    //下载apk
                                                    downLoadApk();
                                                }
                                            },
                                            "取消",
                                            new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    //强制更新
                                                    finish();
                                                }
                                            }, "温馨提示");
                                }
                            }
                        }
                        if (isShouldContinue) {
                            //获取版本成功后，若没新版本信息，判断是否进入手势密码验证界面
                            gestureLogin();
                        }
                    }

                    @Override
                    public void fail(String code) {
                        dismissLoading();
                    }
                });
    }

    /**
     * download apk
     */
    private void downLoadApk() {
//      SimpleDateFormat format = new SimpleDateFormat("yyyymmddHHmm");
//      Date date = new Date();
//      String dateStr = format.format(date);
//      String name = "CRRC" + dateStr +".apk";
        String name = "CRRC.apk";
        new UpdateApk(mContext, Constant.DOWNLOADURL, name);
    }

    /**
     * 进入手势密码登录界面
     */
    private void gestureLogin() {
        User user = AppUtils.getUserInfo(mContext);
        String employeeCode = user.getEmployeeCode();
        String password = user.getPassword();
        String gstStr = AppUtils.getGestureData(mContext, employeeCode);
//        Log.i("--->","employeeCode:" + employeeCode + ";password =" + password + ";gstStr =" + gstStr) ;
        if (!StringUtil.isEmpty(gstStr) && !StringUtil.isEmpty(employeeCode) && !StringUtil.isEmpty(password)) {
            startActivityForResult(GestureVerifyActivity.class, 1009);
        }
    }

}
