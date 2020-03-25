package com.neusoft.zcapplication.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.neusoft.zcapplication.Constant.Constant;
import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.api.ZCApi;
import com.neusoft.zcapplication.base.BaseActivity;
import com.neusoft.zcapplication.entity.QueryHotCity;
import com.neusoft.zcapplication.http.now.CallBack;
import com.neusoft.zcapplication.http.now.RetrofitFactory;
import com.neusoft.zcapplication.tools.AppUtils;
import com.neusoft.zcapplication.tools.LogUtil;
import com.neusoft.zcapplication.tools.StringUtil;
import com.neusoft.zcapplication.tools.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 周边地址搜索
 */

public class PoiActivity extends BaseActivity implements View.OnClickListener, PoiSearch.OnPoiSearchListener {

    private TextView tv_tips;
    private GridView act_poi_gird_view;
    private ListView act_list_poi;
    private PoiSearch poiSearch ;
    private PoiResultAdapter adapter;
    private String mCity = "";
    private String mKeyWord = "";
    private int pageNum;
    private List<PoiItem> mPoiItemList = new ArrayList<>();
    private HotCityGridAdapter gridAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi);
        initView();
        getHotCityData();
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
        AppUtils.setStateBar(PoiActivity.this,findViewById(R.id.frg_status_bar));
        tv_tips = (TextView) findViewById(R.id.tv_tips);
        act_poi_gird_view = (GridView) findViewById(R.id.act_poi_gird_view);
        act_list_poi = (ListView) findViewById(R.id.act_list_poi);
        final String cityName = getIntent().getStringExtra("cityName");
        EditText poiEt = (EditText)findViewById(R.id.et_search_poi);
        poiEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0){
                    LogUtil.d("onTextChanged:------------" + s.toString());
                    pageNum = 0;
                    act_poi_gird_view.setVisibility(View.GONE);
                    act_list_poi.setVisibility(View.VISIBLE);
                    searchByKeyWord(cityName,s.toString(),0);
                }else{
                    act_poi_gird_view.setVisibility(View.VISIBLE);
                    act_list_poi.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        adapter = new PoiResultAdapter(mPoiItemList,PoiActivity.this);
        act_list_poi.setAdapter(adapter);
        act_list_poi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PoiItem poiItem = adapter.getItem(position);
                String address = poiItem.getTitle();
                LatLonPoint latLonPoint = poiItem.getLatLonPoint();
                double lat = latLonPoint.getLatitude();
                double lng = latLonPoint.getLongitude();
                checkBack(lat,lng,address,true);
            }
        });
        //热门城市名称
        List<QueryHotCity> list = new ArrayList<>();
        gridAdapter = new HotCityGridAdapter(list,PoiActivity.this);
        act_poi_gird_view.setAdapter(gridAdapter);

        act_poi_gird_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                QueryHotCity queryHotCity = gridAdapter.getItem(position);
                String googleLon = queryHotCity.getGoogleLon();
                String googleLat = queryHotCity.getGoogleLat();
                String cityName = queryHotCity.getCityName();
                String areaName = queryHotCity.getHotareaName();
                String txt = cityName + " "  + areaName;
                double lng = Double.parseDouble(googleLon);
                double lat = Double.parseDouble(googleLat);
                checkBack(lat,lng,areaName,false);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 搜索
     * @param city
     * @param keyWord
     */
    private void searchByKeyWord(String city, String keyWord, int pageNum) {
        PoiSearch.Query query = new PoiSearch.Query(keyWord,"",city);
        query.requireSubPois(true);
        query.setPageSize(100);
        query.setPageNum(pageNum);
        poiSearch = new PoiSearch(PoiActivity.this,query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int code) {
        if(code == AMapException.CODE_AMAP_SUCCESS){
            if(pageNum == 0){
                mPoiItemList.clear();
            }
            List<String> poiNameList = new ArrayList<>();
            for (PoiItem poiItem : mPoiItemList) {
                poiNameList.add(poiItem.getTitle());
            }
            for (PoiItem poiItem : poiResult.getPois()) {
                if(!poiNameList.contains(poiItem.getTitle())){
                    mPoiItemList.add(poiItem);
                    poiNameList.add(poiItem.getTitle());
                }
            }
            adapter.setList(mPoiItemList);
            adapter.notifyDataSetChanged();
            pageNum ++;
        }else{
            ToastUtil.toast("没有查询到数据");
            pageNum = 0;
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int code) {
        if(code == AMapException.CODE_AMAP_SUCCESS){
            List<String> poiNameList = new ArrayList<>();
            for (PoiItem mPoiItem : mPoiItemList) {
                poiNameList.add(mPoiItem.getTitle());
            }
            if(!poiNameList.contains(poiItem.getTitle())){
                mPoiItemList.add(poiItem);
            }
            adapter.setList(mPoiItemList);
            adapter.notifyDataSetChanged();
        }else{
            ToastUtil.toast("没有查询到数据");
        }
    }

    /**
     * 获取热门城市数据
     */
    private void getHotCityData(){
        Intent intent = getIntent();
        int style = intent.getIntExtra("style",2);
        String cityId = intent.getStringExtra("cityId");
        Map<String,Object> params = new HashMap<>();
        params.put("ciphertext","test");
        params.put("cityId",cityId);//城市编码
        params.put("loginType", Constant.APP_TYPE);
        params.put("style",style);//城市编码类型：1：酒店城市编；2：机场 三字码

        showLoading();
        RetrofitFactory.getInstance().createApi(ZCApi.class).queryHotCity(params)
                .enqueue(new CallBack<List<QueryHotCity>>() {
                    @Override
                    public void success(List<QueryHotCity> queryHotCityList) {
                        dismissLoading();
                        gridAdapter.setList(queryHotCityList);
                        gridAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void fail(String code) {
                        dismissLoading();
                    }
                });
    }

    /**
     * 点击返回按钮
     * @param lat
     * @param lng
     * @param address
     */
    private void checkBack(double lat,double lng,String address,boolean changeLngLat){
        Intent intent = new Intent();
        intent.putExtra("address",address);
        intent.putExtra("lat",lat);
        intent.putExtra("lng",lng);
        intent.putExtra("change",changeLngLat);
        setResult(RESULT_OK,intent);
        finish();
    }

}
