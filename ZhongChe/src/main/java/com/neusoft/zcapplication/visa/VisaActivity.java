package com.neusoft.zcapplication.visa;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ListView;

import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.base.BaseActivity;
import com.neusoft.zcapplication.tools.AppUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 签证
 **/
public class VisaActivity extends BaseActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_visa);
        initView();
    }

    private void initView(){
        List<List<Map<String,String>>> gridList = new ArrayList<>();
        /**
         * 亚洲
         **/
        List<Map<String,String>> list_asia = new ArrayList<>();
        String[] name_asia = {"柬埔寨","印度尼西亚","日本","韩国","马来西亚","新加坡","斯里兰卡","台湾","泰国","越南"};
        String[] ids_asia = {"icon_country_cambodia",
                "icon_country_indonesia",
                "icon_country_japan",
                "icon_country_korea",
                "icon_country_malaysia",
                "icon_country_singapore",
                "icon_country_srilanka",
                "icon_country_taiwan",
                "icon_country_thailand",
                "icon_country_vietnam"};
        String[] codeStr = {"KH","ID","JP","KR","MY","SG","LK","TW","TH","VN"};
        for(int i = 0 ;i < name_asia.length ; i++){
            Map<String,String> m = new HashMap<>();
            m.put("name",name_asia[i]);
            m.put("ids",ids_asia[i]);
            m.put("code",codeStr[i]);
            list_asia.add(m);
        }
        gridList.add(list_asia);

        /**
         * 欧洲
         **/
        List<Map<String,String>> list_europe = new ArrayList<>();
        String[] name_europe = {"奥地利","比利时","捷克","英国","芬兰","德国","荷兰","拉脱维亚","俄罗斯","瑞典"};
        String[] ids_europe = {"icon_country_austria",
                "icon_country_belgium",
                "icon_country_chech",
                "icon_country_england",
                "icon_country_finland",
                "icon_country_germany",
                "icon_country_holland",
                "icon_country_latvia",
                "icon_country_russia",
                "icon_country_sweden"};
        String[] code_europe = {"AT","BE","CZ","GB","FI","DE","NL","LV","RU","SE"};
        for(int i = 0 ;i < name_europe.length ; i++){
            Map<String,String> m = new HashMap<>();
            m.put("name",name_europe[i]);
            m.put("ids",ids_europe[i]);
            m.put("code",code_europe[i]);
            list_europe.add(m);
        }
        gridList.add(list_europe);

        /**
         * 美洲
         **/
        List<Map<String,String>> list_america = new ArrayList<>();
        String[] name_america = {"美国","墨西哥"};
        String[] ids_america = {"icon_country_american","icon_country_mexico"};
        String[] code_america = {"US","MX"};
        for(int i = 0 ;i < name_america.length ; i++){
            Map<String,String> m = new HashMap<>();
            m.put("name",name_america[i]);
            m.put("ids",ids_america[i]);
            m.put("code",code_america[i]);
            list_america.add(m);
        }
        gridList.add(list_america);

        /**
         * 澳洲
         **/
        List<Map<String,String>> list_australia = new ArrayList<>();
        String[] name_australia = {"澳大利亚"};
        String[] ids_australia = {"icon_country_australia"};
        String[] code_australia = {"AU"};
        for(int i = 0 ;i < name_australia.length ; i++){
            Map<String,String> m = new HashMap<>();
            m.put("name",name_australia[i]);
            m.put("ids",ids_australia[i]);
            m.put("code",code_australia[i]);
            list_australia.add(m);
        }
        gridList.add(list_australia);

        ListView visaList = (ListView) findViewById(R.id.visa_country_list);
        VisaListAdapter vAdapter = new VisaListAdapter(VisaActivity.this,gridList);
        visaList.setAdapter(vAdapter);
        findViewById(R.id.btn_back).setOnClickListener(this);
        AppUtils.setStateBar(VisaActivity.this,findViewById(R.id.frg_status_bar));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;

        }
    }

    public int getDeviceWidth(){
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        return w_screen;
    }
}
