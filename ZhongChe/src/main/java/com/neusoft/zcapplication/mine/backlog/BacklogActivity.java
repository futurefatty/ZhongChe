package com.neusoft.zcapplication.mine.backlog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.neusoft.zcapplication.Bean.User;
import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.api.OrderApi;
import com.neusoft.zcapplication.base.BaseActivity;
import com.neusoft.zcapplication.entity.GetBacklogData;
import com.neusoft.zcapplication.http.URL;
import com.neusoft.zcapplication.http.now.CallBack;
import com.neusoft.zcapplication.http.now.RetrofitFactory;
import com.neusoft.zcapplication.tools.AppUtils;
import com.neusoft.zcapplication.tools.DateUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的待办
 */
public class BacklogActivity extends BaseActivity implements View.OnClickListener {

    private BacklogListAdapter adapter;
    private List<GetBacklogData> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_backlog);
        initView();
        getBacklogList();//获取数据
    }

    private void initView(){
        findViewById(R.id.btn_back).setOnClickListener(this);

        dataList = new ArrayList<>();
        ListView listView  =  (ListView) findViewById(R.id.backlog_list);
        adapter = new BacklogListAdapter(BacklogActivity.this,dataList,0);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(BacklogActivity.this,BacklogDealActivity.class);
                GetBacklogData getBacklogData = adapter.getItem(position);
                String itemId = getBacklogData.getId();
                intent.putExtra("id",itemId);
                int type = 1;
                String typeStr = getBacklogData.getType();
                if (typeStr.equals("预定申请单")){
                    type = 1;
                }
                else if (typeStr.equals("核算主体变更")){
                    type = 2;
                }
                List<GetBacklogData.TripInfoBean> tripInfoList = getBacklogData.getTripInfo();
                intent.putExtra("type",type);
                intent.putExtra("tripInfo",(Serializable) tripInfoList);
                String reason = getBacklogData.getReason();
                String applicateName = getBacklogData.getApplicateName();
                String dateTime = getBacklogData.getDateTime();
                intent.putExtra("reason",reason);
                intent.putExtra("applicateName",applicateName);
                intent.putExtra("dateTime",dateTime);
                startActivityForResult(intent,101);
            }
        });
        AppUtils.setStateBar(BacklogActivity.this,findViewById(R.id.frg_status_bar));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101){
            getBacklogList();
        }
    }

    /**
     * 获取我的待办列表
     */
    private void getBacklogList() {
        Map<String,Object> params = new HashMap<>();
        params.put("ciphertext","test");
        User user = AppUtils.getUserInfo(BacklogActivity.this);
        params.put("employeeCode", user.getEmployeeCode());
        params.put("loginType", URL.LOGIN_TYPE);

        showLoading();
        RetrofitFactory.getInstance().createApi(OrderApi.class).getBacklogData(params)
                .enqueue(new CallBack<List<GetBacklogData>>() {
                    @Override
                    public void success(List<GetBacklogData> getBacklogDataList) {
                        dismissLoading();
                        if(getBacklogDataList.size()>0){
                            adapter.setList(sortByDate(getBacklogDataList));
                        }else{
                            showToast("列表数据为空");
                            adapter.setList(getBacklogDataList);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void fail(String code) {
                        dismissLoading();
                    }
                });
    }

    /**
     * 按时间排序
     */
    private List<GetBacklogData> sortByDate(List<GetBacklogData> list){
        Collections.sort(list,new Comparator<GetBacklogData>() {

            @Override

            public int compare(GetBacklogData compare,GetBacklogData bycompare) {
                boolean result = DateUtils.isFirstDayBeforeSecondDay(compare.getDateTime(),bycompare.getDateTime());
                if(result){
                    return 1;
                }else{
                    return -1;
                }

            }});
        return list;
    }

}
