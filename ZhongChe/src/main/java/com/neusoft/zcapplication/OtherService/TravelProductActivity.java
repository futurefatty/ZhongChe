package com.neusoft.zcapplication.OtherService;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.neusoft.zcapplication.Bean.User;
import com.neusoft.zcapplication.Constant.Constant;
import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.api.TravelApi;
import com.neusoft.zcapplication.base.BaseActivity;
import com.neusoft.zcapplication.entity.GetTravelProducts;
import com.neusoft.zcapplication.http.now.CallBack;
import com.neusoft.zcapplication.http.now.RetrofitFactory;
import com.neusoft.zcapplication.tools.AppUtils;
import com.neusoft.zcapplication.widget.popuwindow.CommonPopupWindow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: TenzLiu
 * Time: 2018/6/19 14:45
 * Desc:旅游产品
 */

public class TravelProductActivity extends BaseActivity implements View.OnClickListener, CommonPopupWindow.ViewInterface {

    private LinearLayout btn_back;
    private LinearLayout ll_more;
    private LinearLayout ll_condition;
    private TextView tv_condition;
    private EditText et_key_word;
    private TextView tv_search;
    private ListView lv_travel_product;
    private TextView tv_no_data;
    private TravelProductAdapter mTravelProductAdapter;
    private List<GetTravelProducts> dataList;

    private CommonPopupWindow conditionPopupWindow;
    private TravelProductConditionAdapter mTravelProductConditionAdapter;
    private List<String> mConditionList = new ArrayList<>();
    private String travelType;
    private String cityName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_product);
        initView();
        initData();
        getAllApplyUseCars();
    }

    private void initData() {
        travelType = "1";
        cityName = "";
        mConditionList.add("出发地");
        mConditionList.add("目的地");
    }

    private void initView() {
        AppUtils.setStateBar(mContext,findViewById(R.id.frg_status_bar));
        btn_back = (LinearLayout) findViewById(R.id.btn_back);
        ll_more = (LinearLayout) findViewById(R.id.ll_more);
        btn_back.setOnClickListener(this);
        ll_more.setOnClickListener(this);
        ll_condition = (LinearLayout) findViewById(R.id.ll_condition);
        tv_condition = (TextView) findViewById(R.id.tv_condition);
        et_key_word = (EditText) findViewById(R.id.et_key_word);
        tv_search = (TextView) findViewById(R.id.tv_search);
        lv_travel_product = (ListView) findViewById(R.id.lv_travel_product);
        tv_no_data = (TextView) findViewById(R.id.tv_no_data);
        ll_condition.setOnClickListener(this);
        tv_search.setOnClickListener(this);
        dataList = new ArrayList<>();
        mTravelProductAdapter = new TravelProductAdapter(mContext,dataList);
        lv_travel_product.setAdapter(mTravelProductAdapter);
        lv_travel_product.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GetTravelProducts getTravelProducts = mTravelProductAdapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("getTravelProducts",getTravelProducts);
                startActivity(TravelProductDetailActivity.class,bundle);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.ll_more:
                //跳转携程
                Intent intent = new Intent(this,CtripActivity.class);
                intent.putExtra("url",skipLink(3));
                startActivity(intent);
                break;
            case R.id.ll_condition:
                conditionPopupWindow = new CommonPopupWindow.Builder(mContext)
                        .setView(R.layout.popu_businesscar_apply_car_type)
                        .setBackGroundLevel(0.9f)
                        .setOutsideTouchable(true)
                        .setWidthAndHeight(ll_condition.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT)
                        .setViewOnclickListener(this)
                        .create();
                conditionPopupWindow.showAsDropDown(ll_condition);
                break;
            case R.id.tv_search:
                cityName = et_key_word.getText().toString().trim();
                getAllApplyUseCars();
                break;
        }
    }

    /**
     * 获取我的待办列表
     */
    private void getAllApplyUseCars() {
        Map<String,Object> params = new HashMap<>();
        params.put("travelType", travelType);
        params.put("cityName", cityName);

        showLoading();
        RetrofitFactory.getInstance().createApi(TravelApi.class).getTravelProducts(params)
                .enqueue(new CallBack<List<GetTravelProducts>>() {
                    @Override
                    public void success(List<GetTravelProducts> getTravelProductsList) {
                        dismissLoading();
                        dataList.clear();
                        dataList.addAll(getTravelProductsList);
                        if(getTravelProductsList.size()>0){
                            mTravelProductAdapter.setList(dataList);
                            lv_travel_product.setVisibility(View.VISIBLE);
                            tv_no_data.setVisibility(View.GONE);
                            if(!"".equals(cityName)){
                                et_key_word.setText("");
                                et_key_word.setHint(cityName);
                            }
                        }else{
                            showToast("列表数据为空");
                            mTravelProductAdapter.setList(dataList);
                            lv_travel_product.setVisibility(View.GONE);
                            tv_no_data.setVisibility(View.VISIBLE);
                            if(!"".equals(cityName)){
                                et_key_word.setText("");
                                et_key_word.setHint(cityName);
                            }
                        }
                        mTravelProductAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void fail(String code) {
                        dismissLoading();
                        dataList.clear();
                        mTravelProductAdapter.setList(dataList);
                        lv_travel_product.setVisibility(View.GONE);
                        tv_no_data.setVisibility(View.VISIBLE);
                        mTravelProductAdapter.notifyDataSetChanged();
                        if(!"".equals(cityName)){
                            et_key_word.setText("");
                            et_key_word.setHint(cityName);
                        }
                    }
                });
    }


    @Override
    public void getChildView(View view, int layoutResId) {
        if(layoutResId == R.layout.popu_businesscar_apply_car_type){
            ListView lv_car_type = (ListView) view.findViewById(R.id.lv_car_type);
            mTravelProductConditionAdapter = new TravelProductConditionAdapter(this,mConditionList);
            lv_car_type.setAdapter(mTravelProductConditionAdapter);
            lv_car_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    tv_condition.setText(mConditionList.get(position));
                    if(position == 0){
                        travelType = "1";
                        et_key_word.setText("");
                    }else{
                        travelType = "2";
                        et_key_word.setText("");
                    }
                    conditionPopupWindow.dismiss();
                }
            });
        }
    }

    /**
     * 获取链接
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
