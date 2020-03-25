package com.neusoft.zcapplication.mine.mostusedinfo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.neusoft.zcapplication.Bean.User;
import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.base.BaseActivity;
import com.neusoft.zcapplication.http.NetWorkRequest;
import com.neusoft.zcapplication.http.RequestCallback;
import com.neusoft.zcapplication.http.RequestUtil;
import com.neusoft.zcapplication.http.URL;
import com.neusoft.zcapplication.mine.mostusedinfo.adapter.PassengerListAdapter;
import com.neusoft.zcapplication.tools.AppUtils;
import com.neusoft.zcapplication.tools.DisplayUtil;
import com.neusoft.zcapplication.tools.ToastUtil;
import com.neusoft.zcapplication.widget.swipList.SwipeMenu;
import com.neusoft.zcapplication.widget.swipList.SwipeMenuCreator;
import com.neusoft.zcapplication.widget.swipList.SwipeMenuItem;
import com.neusoft.zcapplication.widget.swipList.SwipeMenuListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * 常用旅客信息
 */

public class PassengerActivity extends BaseActivity implements View.OnClickListener,RequestCallback {
    private PassengerListAdapter adapter;
    private Call<Map<String,Object>> contactCall;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frg_passenger);
        initView();
        getContactsData();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.btn_add_passenger:
                //添加乘客信息
                intent = new Intent(PassengerActivity.this,AddPassengerActivity.class);
                startActivityForResult(intent,4401);
                break;
            case R.id.btn_back:
                finish();
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(contactCall.isExecuted()){
            contactCall.cancel();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(null != data){
            if(requestCode  == 4401){
                //刷新列表数据
                getContactsData();
            }
        }
    }

    private void initView(){
        findViewById(R.id.btn_add_passenger).setOnClickListener(this);
        findViewById(R.id.btn_back).setOnClickListener(this);
        List<Map<String,Object>> list = new ArrayList<>();
//        for(int i = 0 ;i < 4 ; i++){
//            Map<String,Object> map = new HashMap<>();
//            map.put("name","邓生");
//            map.put("data","3821**********1023");
//            map.put("type","身份证");
//            list.add(map);
//        }
        SwipeMenuListView listView  =  (SwipeMenuListView) findViewById(R.id.frg_passenger_list);
        adapter = new PassengerListAdapter(list,PassengerActivity.this);
        listView.setAdapter(adapter);
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                //如果当前语言环境为中文，则item宽度需要*2
                int itemWidth = DisplayUtil.spTopx(PassengerActivity.this,40);
//                int imgSize = DisplayUtil.spTopx(PassengerActivity.this,30);
                SwipeMenuItem delItem = new SwipeMenuItem( PassengerActivity.this);
                delItem.setBackground(R.drawable.bg_list_del_btn);
//                delItem.setImgResId(R.mipmap.icon_delete);
//                delItem.setImgSize(imgSize);
                // set item background
                delItem.setWidth(itemWidth);//设置item宽度
                delItem.setTitle("删除");
                delItem.setTitleSize(14);
                delItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(delItem);
            }
        };
        // set creator
        listView.setMenuCreator(creator);
        // step 2. listener item click event
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                Map<String,Object> map = adapter.getList().get(position);
                List<Map<String,Object>> list = adapter.getList();
                switch (index) {
                    case 0:
                        Log.d("--->", "删除");
                        //如果当前有删除操作未完成，则不能进行下一次删除操作
//                        if(!isDelNow){
//                            isDelNow = true;
//                        }
                        break;
                }
                return false;
            }
        });

        // set SwipeListener
        listView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {
                // swipe start
            }
            @Override
            public void onSwipeEnd(int position) {
                // swipe end
            }
        });
        // set MenuStateChangeListener
        listView.setOnMenuStateChangeListener(new SwipeMenuListView.OnMenuStateChangeListener() {
            @Override
            public void onMenuOpen(int position) {
                Log.i("--->","onMenuOpen:"+ position);
            }

            @Override
            public void onMenuClose(int position) {
                Log.i("--->","onMenuClose:"+ position);
            }
        });
        AppUtils.setStateBar(PassengerActivity.this,findViewById(R.id.frg_status_bar));
    }

    @Override
    public void requestCancel(int type) {

    }

    @Override
    public void requestSuccess(Object map, int type) {
        Map<String,Object> result = (Map<String, Object>) map;
        if(null != result){
            String code = null == result.get("code") ? "" : result.get("code").toString();
            String codeMsg = null == result.get("codeMsg") ? "" : result.get("codeMsg").toString();
            if(code.equals("00000")) {
                List<Map<String,Object>> data = (ArrayList) result.get("data");

                adapter.setList(data);
                adapter.notifyDataSetChanged();
            }else {
                ToastUtil.toastError(PassengerActivity.this);
            }
        }else{
            //请求失败
            ToastUtil.toastError(PassengerActivity.this);
        }
    }

    @Override
    public void requestFail(int type) {
        ToastUtil.toastError(PassengerActivity.this);
    }

    /**
     * 获取常用联系人信息
     */
    private void getContactsData() {
        User user = new AppUtils().getUserInfo(PassengerActivity.this);
        Map<String,Object> params = new HashMap<>();
        params.put("ciphertext","test");
        params.put("loginType", URL.LOGIN_TYPE);
        params.put("employeeCode", user.getEmployeeCode());
        Retrofit retrofit = new RequestUtil().getRequestClient(URL.ZHONGCHE_93);
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        contactCall = request.queryContacts(params);
        new RequestUtil().requestData(contactCall,this,1,"加载中...",true,PassengerActivity.this);
    }
}
