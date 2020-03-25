package com.neusoft.zcapplication.CarService;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.neusoft.zcapplication.Bean.User;
import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.ShowViewActivity;
import com.neusoft.zcapplication.TicketService.ServiceGridAdapter;
import com.neusoft.zcapplication.api.SupplierApi;
import com.neusoft.zcapplication.base.BaseActivity;
import com.neusoft.zcapplication.http.now.CallBack;
import com.neusoft.zcapplication.http.now.RetrofitFactory;
import com.neusoft.zcapplication.tools.AppUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 车辆服务
 */
public class CarServiceActivity extends BaseActivity implements View.OnClickListener {

    private GridView gridView;
    private ServiceGridAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_service);
        initView();
    }

    private void initView(){
        AppUtils.setStateBar(CarServiceActivity.this,findViewById(R.id.frg_status_bar));
        TextView title = (TextView) findViewById(R.id.tv_page_title);
        title.setText("车辆服务");
        findViewById(R.id.btn_back).setOnClickListener(this);
        String[] textArr = {"火车票","滴滴出行","公务用车申请(株洲)"};
        String[] picArr = {"icon_other_train","icon_car_didi","icon_car_own"};
        List<Map<String,String>> datalist = new ArrayList<>();
        for(int i=0;i<picArr.length;i++) {
            Map<String,String> item = new HashMap<>();
            item.put("name",textArr[i]);
            item.put("ids",picArr[i]);
            datalist.add(item);
        }
        gridView  =  (GridView) findViewById(R.id.ticket_gridView);
        adapter = new ServiceGridAdapter(CarServiceActivity.this,datalist);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        //火车票
                        getTCTrainData();
                        break;
                    case 1:
                        //跳转滴滴
                        Bundle bundle = getIntent().getExtras();
                        HashMap<String,String> map = (HashMap<String, String>) bundle.getSerializable("map");
                        String url = "http://webapp.diditaxi.com.cn/?maptype=wgs&lat=" +
                                map.get("fromlat")+"&lng=" + map.get("fromlng") +
                                "&toname=%e4%b8%ad%e5%9b%bd%e4%b8%ad%e8%bd%a6%e6%a0%aa%e6%b4%b2%e7%94%b5%e5%8a%9b%e6%9c%ba%e8%bd%a6%e7%a0%94%e7%a9%b6%e6%89%80%e6%9c%89%e9%99%90%e5%85%ac%e5%8f%b8&toaddr=%e5%be%97%e5%ae%9e%e2%bc%a4%e5%a4%a7%e5%8e%a6%e5%81%9c%e2%bb%8b%e8%bd%a6%e5%9c%ba&phone=13800138000&channel=xxxxgb\n";
                        Intent intent = new Intent(mContext,ShowViewActivity.class);
                        intent.putExtra("url",url);
                        startActivity(intent);
                        break;
                    case 2:
                        //公车申请
                        startActivity(BusinessCarApplyActivity.class);
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
        }
    }

    /**
     * 同程火车票
     */
    private void getTCTrainData( ){
        Map<String,Object> params = new HashMap<>();
        User user = AppUtils.getUserInfo(CarServiceActivity.this);
        params.put("ciphertext","test");
        params.put("interfaceType",2);
        params.put("style",5);//：1：美亚；2：慧通；3：HRS；4：携程 ,5同程
        params.put("userId",user.getEmployeeCode());

        showLoading();
        RetrofitFactory.getInstance().createApi(SupplierApi.class).tcTrain(params)
                .enqueue(new CallBack<String>() {
                    @Override
                    public void success(String url) {
                        dismissLoading();
                        Intent intent = new Intent(mContext,ShowViewActivity.class);
                        intent.putExtra("url",url);
                        intent.putExtra("showAlert",true);
                        intent.putExtra("hasPay",true);
                        intent.putExtra("payType","tc");//同程支付
                        startActivity(intent);
                    }

                    @Override
                    public void fail(String code) {
                        dismissLoading();
                    }
                });
    }

}
