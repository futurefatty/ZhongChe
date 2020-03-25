package com.neusoft.zcapplication.OtherService;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.neusoft.zcapplication.Bean.User;
import com.neusoft.zcapplication.Constant.Constant;
import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.TicketService.ServiceGridAdapter;
import com.neusoft.zcapplication.base.BaseActivity;
import com.neusoft.zcapplication.mine.SuggestAndFeedbackActivity;
import com.neusoft.zcapplication.tools.AppUtils;
import com.neusoft.zcapplication.visa.VisaActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 旅游服务
 */
public class OtherServiceActivity extends BaseActivity implements View.OnClickListener{

    private GridView gridView;
    private ServiceGridAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_service);
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView(){
        AppUtils.setStateBar(OtherServiceActivity.this,findViewById(R.id.frg_status_bar));
        TextView title = (TextView) findViewById(R.id.tv_page_title);
        title.setText("旅游服务");
        findViewById(R.id.btn_back).setOnClickListener(this);
        String[] textArr = {"门票","旅游","签证","数据分析","投诉与建议"};
        String[] picArr = {"icon_other_gateticket",
                        "icon_other_travel",
                        "icon_other_visa",
                        "icon_other_dataanalysis",
                        "icon_mine_suggestion"};
        List<Map<String,String>> datalist = new ArrayList<>();
        for(int i=0;i<picArr.length-2;i++) {
            Map<String,String> item = new HashMap<>();
            item.put("name",textArr[i]);
            item.put("ids",picArr[i]);
            datalist.add(item);
        }
        gridView  =  (GridView) findViewById(R.id.ticket_gridView);
        adapter = new ServiceGridAdapter(OtherServiceActivity.this,datalist);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                String url = "";
                switch (position) {
                    case 0:
                        //携程
                        String link = skipLink(1);//获取携程跳转链接
                        intent = new Intent(OtherServiceActivity.this,CtripActivity.class);
                        intent.putExtra("url",link);
                        startActivity(intent);
                        break;
                    case 1:
                        //旅游产品
//                        intent = new Intent(OtherServiceActivity.this,CtripActivity.class);
//                        intent.putExtra("url",skipLink(3));
                        intent = new Intent(OtherServiceActivity.this,TravelProductActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        //签证
                        intent = new Intent(OtherServiceActivity.this,VisaActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        //报表
//                        url = Constant.ZHONGCHE_H5 + "dataCharts.html";
//                        intent = new Intent(OtherServiceActivity.this,ShowViewActivity.class);
//                        intent.putExtra("url",url);
//                        startActivity(intent);
                        startActivity(DataReportActivity.class);
                        break;
                    case 4:
                        //投诉与建议页面
                        intent = new Intent(OtherServiceActivity.this,SuggestAndFeedbackActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
        }
    }

    /**
     * 获取携程链接
     * @param type
     * @return
     */
    private String skipLink(int type){
        //opt=1&code=CRCC_21111111111922&cust=crrc&product=1&failUrl=m.baidu.com"
        //product:	产品编码，1、门票首页；2、门票订单列表页；3、度假首页；4、度假订单列表页
        StringBuilder sb = new StringBuilder(Constant.CTRIP_LINK);
        User user = AppUtils.getUserInfo(mContext);
        String payCode = user.getPayCode();
        sb.append("&code=");
        sb.append(payCode);
        sb.append("&product=");
        sb.append(type);
        return sb.toString();
    }

}
