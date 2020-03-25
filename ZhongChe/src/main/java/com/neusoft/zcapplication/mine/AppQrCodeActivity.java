package com.neusoft.zcapplication.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.base.BaseActivity;
import com.neusoft.zcapplication.tools.AppUtils;

/**
 * 二维码页面
 */

public class AppQrCodeActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);
        initView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
        }
    }

    private void initView(){
        findViewById(R.id.btn_back).setOnClickListener(this);
        AppUtils.setStateBar(AppQrCodeActivity.this,findViewById(R.id.frg_status_bar));
    }

}
