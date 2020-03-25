package com.neusoft.zcapplication.HotelService;

import android.os.Bundle;
import android.view.View;

import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.base.BaseActivity;
import com.neusoft.zcapplication.tools.AppUtils;

/**
 * 差旅标准
 */
public class RuleActivity extends BaseActivity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rule);
        initView();
    }

    private void initView(){
        findViewById(R.id.btn_back).setOnClickListener(this);
        AppUtils.setStateBar(RuleActivity.this,findViewById(R.id.frg_status_bar));
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
        }
    }
}
