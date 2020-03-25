package com.neusoft.zcapplication.mine.mostusedinfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.base.BaseActivity;

/**
 * 新增签证信息
 */
public class AddVisaActivity extends BaseActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_visa);
        initView();
    }

    private void initView(){
        findViewById(R.id.btn_back).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
        }
    }
}
