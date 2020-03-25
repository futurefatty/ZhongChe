package com.neusoft.zcapplication.HotelService;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.base.BaseActivity;
import com.neusoft.zcapplication.tools.AppUtils;

import java.util.Map;

/**
 * 税票凭证抬头
 */
public class TaxDetailActivity extends BaseActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tax_detail);
        initView();
        setTaxInfo();
    }

    private void initView(){
        findViewById(R.id.btn_back).setOnClickListener(this);
        AppUtils.setStateBar(TaxDetailActivity.this,findViewById(R.id.frg_status_bar));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
        }
    }

    /**
     * 设置税票信息
     */
    private void setTaxInfo() {
        TextView tv_unitName = (TextView) findViewById(R.id.item_unit_name);
        TextView tv_taxNo = (TextView) findViewById(R.id.item_tax_no);
        TextView tv_address = (TextView) findViewById(R.id.item_address);
        TextView tv_phone = (TextView) findViewById(R.id.item_phone);
        TextView tv_bank = (TextView) findViewById(R.id.item_bank);
        TextView tv_bankCount = (TextView) findViewById(R.id.item_bank_count);

        Bundle bundle = getIntent().getExtras();
        Map<String,Object> map = (Map<String, Object>) bundle.getSerializable("item");
        String unitName = map.get("companyName") == null?"":map.get("companyName")+"";
        String taxNo = map.get("taxCode") == null?"":map.get("taxCode")+"";
        String address = map.get("companyAddress") == null?"":map.get("companyAddress")+"";
        String phone = map.get("companyPhone") == null?"":map.get("companyPhone")+"";
        String bank = map.get("depositBank") == null?"":map.get("depositBank")+"";
        String bankCount = map.get("bankAccount") == null?"":map.get("bankAccount")+"";
        tv_unitName.setText(unitName);
        tv_taxNo.setText(taxNo);
        tv_address.setText(address);
        tv_phone.setText(phone);
        tv_bank.setText(bank);
        tv_bankCount.setText(bankCount);
    }
}
