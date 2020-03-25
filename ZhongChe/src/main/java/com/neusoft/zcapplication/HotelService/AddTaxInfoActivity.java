package com.neusoft.zcapplication.HotelService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.neusoft.zcapplication.Bean.User;
import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.base.BaseActivity;
import com.neusoft.zcapplication.http.NetWorkRequest;
import com.neusoft.zcapplication.http.RequestCallback;
import com.neusoft.zcapplication.http.RequestUtil;
import com.neusoft.zcapplication.http.URL;
import com.neusoft.zcapplication.tools.AlertUtil;
import com.neusoft.zcapplication.tools.AppUtils;
import com.neusoft.zcapplication.tools.ToastUtil;
import com.neusoft.zcapplication.widget.ClearIconEditText;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * 新增税票凭证抬头
 */
public class AddTaxInfoActivity extends BaseActivity implements View.OnClickListener,RequestCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_tax_info);
        initView();
    }

    private void initView(){
        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.btn_save).setOnClickListener(this);
        AppUtils.setStateBar(AddTaxInfoActivity.this,findViewById(R.id.frg_status_bar));
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_save:
                addTaxInfo();
                break;
        }
    }

    /**
     * 新增税票信息
     */
    private void addTaxInfo() {
        if (!checkForm()){
            return;
        }
        User user = AppUtils.getUserInfo(AddTaxInfoActivity.this);
        String employeeCode = user.getEmployeeCode();
        ClearIconEditText et_unitName = (ClearIconEditText) findViewById(R.id.item_unit_name);
        ClearIconEditText et_taxNo = (ClearIconEditText) findViewById(R.id.item_tax_no);
        ClearIconEditText et_address = (ClearIconEditText) findViewById(R.id.item_address);
        ClearIconEditText et_phone = (ClearIconEditText) findViewById(R.id.item_phone);
        ClearIconEditText et_bank = (ClearIconEditText) findViewById(R.id.item_bank);
        ClearIconEditText et_bankCount = (ClearIconEditText) findViewById(R.id.item_bank_count);

        Map<String,Object> params = new HashMap<>();
        params.put("bankAccount",et_bankCount.getText().toString().trim());
        params.put("companyAddress",et_address.getText().toString().trim());
        params.put("companyName",et_unitName.getText().toString().trim());
        params.put("companyPhone",et_phone.getText().toString().trim());
        params.put("createPersonnel",employeeCode);
//        params.put("createTime","test");
        params.put("depositBank",et_bank.getText().toString().trim());
        params.put("employeeCode",employeeCode);
//        params.put("id","test");
        params.put("taxCode",et_taxNo.getText().toString().trim());
//        params.put("loginType", URL.LOGIN_TYPE);
        Retrofit retrofit = new RequestUtil().getRequestClient(URL.ZHONGCHE_93);
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        Call<Map<String,Object>> call = request.addTaxInfo(params);
        new RequestUtil().requestData(call,this,1, mContext);
    }

    @Override
    public void requestSuccess(Object map, int type) {
        Map<String,Object> result = (Map<String, Object>) map;
        if(null != result){
            String code = null == result.get("code") ? "" : result.get("code").toString();
//            String codeMsg = (String) result.get("codeMsg");

            if(code.equals("00000")) {
                ToastUtil.toastHandleSuccess(AddTaxInfoActivity.this);

                finish();
            }
            else {
                ToastUtil.toastHandleError(AddTaxInfoActivity.this);
            }
        }else{
            //请求失败
            ToastUtil.toastHandleError(AddTaxInfoActivity.this);
        }
    }

    @Override
    public void requestFail(int type) {
        ToastUtil.toastHandleFail(AddTaxInfoActivity.this);
    }

    @Override
    public void requestCancel(int type) {

    }

    /**
     * 表单验证
     */
    private boolean checkForm() {
        boolean result = true;
        String msg = "";
        EditText et_unitName = (EditText) findViewById(R.id.item_unit_name);
        EditText et_taxNo = (EditText) findViewById(R.id.item_tax_no);
        EditText et_address = (EditText) findViewById(R.id.item_address);
        EditText et_phone = (EditText) findViewById(R.id.item_phone);
        EditText et_bank = (EditText) findViewById(R.id.item_bank);
        EditText et_bankCount = (EditText) findViewById(R.id.item_bank_count);

        String unitName = et_unitName.getText().toString();
        if("".equals(unitName)) {
            msg = "请填写单位名称";
            result = false;
        }
        String taxNo = et_taxNo.getText().toString();
        if("".equals(taxNo)) {
            msg = "请填写税号";
            result = false;
        }
        else if (!(15==taxNo.length()||18==taxNo.length()||20==taxNo.length())){
            msg = "税号长度只能为：15、18、20其中一种";
            result = false;
        }

        String address = et_address.getText().toString();
        if("".equals(address)) {
            msg = "请填写单位地址";
            result = false;
        }
        String phone = et_phone.getText().toString();
        String regex = "(?:(\\(\\+?86\\))(0[0-9]{2,3}\\-?)?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?)|" +
                "(?:(86-?)?(0[0-9]{2,3}\\-?)?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?)";
        String regex2 = "^(0\\d{2}-\\d{8}(-\\d{1,4})?)|(0\\d{3}-\\d{7,8}(-\\d{1,4})?)$";
        if("".equals(phone)) {
            msg = "请填写电话号码";
            result = false;
        }
        else if (!Pattern.matches(regex2, phone)) {
            msg = "电话号码格式为：区号-座机号码-分机号码（010-88888888-1234）" +
                    "\n或：区号-座机号码（0123-88888888）";
            result = false;
        }
        String bank = et_bank.getText().toString();
        if("".equals(bank)) {
            msg = "请填写开户银行";
            result = false;
        }
        String bankCount = et_bankCount.getText().toString();
        if("".equals(bankCount)) {
            msg = "请填写银行账户";
            result = false;
        }
        else if (bankCount.length()<8||bankCount.length()>27){
            msg = "银行账户长度必须在8-27间";
            result = false;
        }

        if(!result) {
            AlertUtil.show2(AddTaxInfoActivity.this, msg, "确定", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }
        return result;
    }
}
