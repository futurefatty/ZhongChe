package com.neusoft.zcapplication.city;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.base.BaseActivity;
import com.neusoft.zcapplication.http.NetWorkRequest;
import com.neusoft.zcapplication.http.RequestCallback;
import com.neusoft.zcapplication.http.RequestUtil;
import com.neusoft.zcapplication.http.URL;
import com.neusoft.zcapplication.tools.AppUtils;
import com.neusoft.zcapplication.tools.ToastUtil;
import com.neusoft.zcapplication.widget.LoadingDialog;
import com.neusoft.zcapplication.widget.SideBar;
import com.neusoft.zcapplication.widget.SlideTabBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * 机场、城市列表页面
 */

public class AirportActivity extends BaseActivity implements View.OnClickListener,
        SortAdapter.GridItemCheckListener, SlideTabBar.OnTabBarChangeListener, RequestCallback {
    private SideBar sideBar;//侧边字母栏
    private TextView textDialog;
    private SortAdapter adapter;//列表适配器
    private ListView listView;
    private List<AirportBean> cityList;
    private LoadingDialog loadDialog;//提交数据时的加载状态
    private Call<Map<String, Object>> call;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_airport);
        initView();
        int dataType = getIntent().getIntExtra("dataType", 0);//1 获取城市数据，0获取机场数据
        getData(dataType);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        requestCancel(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
        }
    }

    @Override
    public void gridChecked(AirportBean bean) {
        String name = bean.getAirportName();
        String code = bean.getAirportCode();
        boolean isAbroadCity = bean.isAbroadCity();
        checkBack(code, name, isAbroadCity);
    }

    @Override
    public void onTabBarChange(int position) {
        if (position == 0) {
            adapter.setShowAbroadData(false);
        } else {
            adapter.setShowAbroadData(true);
        }
        adapter.notifyDataSetChanged();
    }

    private void initView() {
        findViewById(R.id.btn_back).setOnClickListener(this);
        listView = (ListView) findViewById(R.id.act_airport_list_view);
        List<AirportBean> list = new ArrayList<>();
        //由于存在历史和热门两种类型视图，需要添加两个空的数据
        list.add(new AirportBean());
        list.add(new AirportBean());

        //历史城市数据
        List<AirportBean> historyList = new ArrayList<>();
        int dataType = getIntent().getIntExtra("dataType", 0);//1 获取城市数据，0获取机场数据
        if (dataType == 0) {
            String history = AppUtils.getFlightCityHistory(AirportActivity.this);
//             Log.i("--->","酒店数据：" + history);
            if (history.length() > 0) {
                String[] ary = history.split(AppUtils.CITIES_SPLIT);
                for (int i = 0; i < ary.length; i++) {
                    String nameCode = ary[i];
                    String[] nameCodeAry = nameCode.split(AppUtils.NAME_CODE_SPLIT);
                    if (nameCodeAry.length == 2) {
                        AirportBean bean = new AirportBean();
                        bean.setAirportName(nameCodeAry[0]);
                        bean.setAirportCode(nameCodeAry[1]);
                        historyList.add(bean);
                    }
                }
            }
        } else {
            String history = AppUtils.getHotelCityHistory(AirportActivity.this);
            if (history.length() > 0) {
                String[] ary = history.split(AppUtils.CITIES_SPLIT);
                for (int i = 0; i < ary.length; i++) {
                    String nameCode = ary[i];
                    String[] nameCodeAry = nameCode.split(AppUtils.NAME_CODE_SPLIT);
                    if (nameCodeAry.length == 2) {
                        AirportBean bean = new AirportBean();
                        bean.setAirportName(nameCodeAry[0]);
                        bean.setAirportCode(nameCodeAry[1]);
                        historyList.add(bean);
                    }
                }
            }
        }

        //切换国际、国内城市的按钮
        SlideTabBar slideTabBar = (SlideTabBar) findViewById(R.id.city_slide_bar);
        slideTabBar.setOnTabBarChangeListener(this);
        String inCity = getIntent().getStringExtra("inCity");
        if ("yes".equals(inCity)) {
            findViewById(R.id.toggle_bar).setVisibility(View.GONE);
        }

        List<AirportBean> hotList = new ArrayList<>();
        adapter = new SortAdapter(AirportActivity.this, list, historyList, hotList);
        adapter.setGridItemCheckListener(this);//热门、历史数据点击事件
        listView.setAdapter(adapter);
        sideBar = (SideBar) findViewById(R.id.side_bar);
        textDialog = (TextView) findViewById(R.id.txt_dialog);
        sideBar.setTextView(textDialog);

        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s, int pos) {
                if (s.equals("热门")) {
                    listView.setSelection(0);
                } else if (s.equals("历史")) {
                    listView.setSelection(1);
                } else {
                    int position = adapter.getPositionForSection(s.charAt(0));
                    if (position != -1) {
                        listView.setSelection(position - 1);
                    }
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                AirportBean bean = adapter.getItem(position);
                String name = bean.getAirportName();
                String code = bean.getAirportCode();
                boolean isAbroadCity = bean.isAbroadCity();

                checkBack(code, name, isAbroadCity);
            }
        });
        //搜索框
        final EditText searchEt = (EditText) findViewById(R.id.et_search_city);
        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<AirportBean> newList = new ArrayList<AirportBean>();
                if (cityList != null && s.toString().trim().length() > 0) {
                    //searchEt.getText().toString()
                    newList.add(new AirportBean());
                    newList.add(new AirportBean());
                    for (AirportBean item : cityList) {
                        if (null != item.getAirportName() && item.getAirportName().contains(s.toString().trim())) {
//                            Toast.makeText(AirportActivity.this,"---"+item.getAirportName(),Toast.LENGTH_SHORT).show();
                            newList.add(item);
                        }
                    }
                    adapter.setList(newList, null);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        AppUtils.setStateBar(AirportActivity.this, findViewById(R.id.frg_status_bar));
    }

    /**
     * 获取机场，城市数据
     *
     * @param dataType 1 获取城市数据，0获取机场数据
     */
    private void getData(final int dataType) {
        showLoading("正在加载数据...", true);
        Map<String, Object> params = new HashMap<>();
        params.put("ciphertext", "test");
        params.put("loginType", URL.LOGIN_TYPE);
        Retrofit retrofit = new RequestUtil().getRequestClient(URL.ZHONGCHE_93);
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
//        Call<Map<String,Object>> call ;
        if (dataType == 0) {
            call = request.getCity(params);//获取机场数据
        } else {
            call = request.getHotelCity(params);//获取城市数据
        }
        new RequestUtil().requestData(call, this, 1, mContext);
    }

    /**
     * 解析数据
     *
     * @param map
     */
    private void resolveCityData(Map<String, Object> map) {
        resolveCityDataByType(map, 0);
        resolveCityDataByType(map, 1);

    }

    /**
     * 解析城市数据
     *
     * @param cityMap
     * @param type    0 显示国内城市 1 显示国际城市数据
     */
    private void resolveCityDataByType(Map<String, Object> cityMap, int type) {
        //之前已选的城市3字码
//        Intent intent = getIntent();
//        String startCode = intent.getStringExtra("startCode");
//        String endCode = intent.getStringExtra("endCode");

        List<AirportBean> dataInner = new ArrayList<AirportBean>();
        List<AirportBean> dataHot = new ArrayList<>();//热门城市
        Map<String, Object> innerCityMap;
        boolean isAbroadCity;
        //全部城市
        List<Map<String, Object>> allCityList;
        if (type == 0) {
            //国内城市
            allCityList = (List<Map<String, Object>>) cityMap.get("domestic");
            isAbroadCity = false;
        } else {
            //国际城市
            allCityList = (List<Map<String, Object>>) cityMap.get("internation");
            isAbroadCity = true;
        }
        dataInner.add(new AirportBean());

        for (int i = 0; i < allCityList.size(); i++) {
            Map<String, Object> mm = allCityList.get(i);

            List<Map<String, Object>> childList = (List<Map<String, Object>>) mm.get("cities");
            String startChar = mm.get("start_char").toString();
            for (int j = 0; j < childList.size(); j++) {
                Map<String, Object> childMap = childList.get(j);
                String cityCode = childMap.get("city_code").toString();//城市3字码
                String cityNameCN = childMap.get("city_name_cn").toString();//中文名
                String isHotCity = null == childMap.get("isHotCity") ? "0" : childMap.get("isHotCity").toString();// 1 热门城市，0 非热门城市
//                String cityNameEN = childMap.get("city_name_en").toString();//英文名

                AirportBean bean = new AirportBean();
                //设置全部城市列表数据中的城市三字码为startCode 或endCode的值的机场为选中状态
//                if(null != startCode && startCode.equals(cityCode)){
//                    bean.setChecked(true);
//                }
//                if(null != endCode && endCode.equals(cityCode)){
//                    bean.setChecked(true);
//                }

                bean.setAbroadCity(isAbroadCity);

                bean.setSortLetters(startChar);
                bean.setAirportCode(cityCode);
                bean.setAirportName(cityNameCN);
//                bean.setAirportNameEn(cityNameEN);
                dataInner.add(bean);
                if (isHotCity.equals("1")) {
                    dataHot.add(bean);
                }
            }
        }
        if (type == 0) {
            cityList = dataInner;
        }

//        //国内热门城市
////        List<Map<String,Object>> hotCityList = ( List<Map<String,Object>>) innerCityMap.get("hotCity");
//        List<AirportBean> dataHot = new ArrayList<>();
//        for(int j = 0 ; j < hotCityList.size() ; j++){
//            Map<String,Object> childMap = hotCityList.get(j);
//
//            String cityCode = childMap.get("cityCode").toString();//城市3字码
//            String cityNameCN = childMap.get("cityNameCN").toString();//中文名
//            String cityNameEN = childMap.get("cityNameEN").toString();//英文名
//
//            AirportBean bean = new AirportBean();
////            bean.setSortLetters(startChar);
//            bean.setAbroadCity(isAbroadCity);
//            bean.setAirportCode(cityCode);
//            bean.setAirportName(cityNameCN);
//            bean.setAirportNameEn(cityNameEN);
//            //设置热门城市列表数据中的城市三字码为startCode 或endCode的值的机场为选中状态
//            if(null != startCode && startCode.equals(cityCode)){
//                bean.setChecked(true);
//            }
//            if(null != endCode && endCode.equals(cityCode)){
//                bean.setChecked(true);
//            }
//            dataHot.add(bean);
//        }
        if (type == 0) {
            adapter.setList(dataInner, dataHot);
            adapter.notifyDataSetChanged();
        } else {
            adapter.setAbroadList(dataInner, dataHot);
        }
    }

    /**
     * 选择机场，返回
     *
     * @param code
     * @param name
     * @param isAbroadCity true 国际城市 false 国内城市
     */
    private void checkBack(String code, String name, boolean isAbroadCity) {
        Intent intent = new Intent();
        intent.putExtra("code", code);
        intent.putExtra("name", name);
        if (isAbroadCity) {
            intent.putExtra("type", 1);
        } else {
            intent.putExtra("type", 0);
        }
        int dataType = getIntent().getIntExtra("dataType", 0);//1 获取城市数据，0获取机场数据
        if (dataType == 1) {
            //保存酒店城市记录
            AppUtils.saveHotelCityHistory(AirportActivity.this, name + AppUtils.NAME_CODE_SPLIT + code, "");
        } else {
            //保存机场城市记录
            AppUtils.saveFlightCityHistory(AirportActivity.this, name + AppUtils.NAME_CODE_SPLIT + code, "");
        }
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void requestSuccess(Object map, int type) {
        Map<String, Object> result = (Map<String, Object>) map;
        dismissLoading();
        if (null != result) {
//                    boolean bool = (Boolean) result.get("state");
            String code = null == result.get("code") ? "" : result.get("code").toString();
//            String codeMsg = (String) result.get("codeMsg");
            if (code.equals("00000")) {
                Map<String, Object> data = (Map<String, Object>) result.get("data");
                resolveCityData(data);
            } else {
                //请求失败
                ToastUtil.toastError(AirportActivity.this);
            }
        } else {
            //请求失败
            ToastUtil.toastError(AirportActivity.this);
        }
    }

    @Override
    public void requestFail(int type) {
        //请求失败
        dismissLoading();
        ToastUtil.toastFail(AirportActivity.this);
    }

    @Override
    public void requestCancel(int type) {
        if (null != call && call.isExecuted()) {
            call.cancel();
        }
    }

}
