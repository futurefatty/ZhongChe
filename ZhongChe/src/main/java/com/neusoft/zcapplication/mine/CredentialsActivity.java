package com.neusoft.zcapplication.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.neusoft.zcapplication.Bean.User;
import com.neusoft.zcapplication.Constant.Constant;
import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.api.OrderApi;
import com.neusoft.zcapplication.base.BaseActivity;
import com.neusoft.zcapplication.entity.GetCredentialsData;
import com.neusoft.zcapplication.http.now.CallBack;
import com.neusoft.zcapplication.http.now.RetrofitFactory;
import com.neusoft.zcapplication.tools.AppUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 证件维护
 */
public class CredentialsActivity extends BaseActivity implements View.OnClickListener{

    private CredentialsAdapter adapter;//适配器
    private boolean hasIdCard;//true 该用户有身份证
    private int delPosition =  -1;//当前正在删除的证件下标
    private boolean delNow;//true正在删除证件

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_credentials);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCredentialsData();//获取用户的证件数据
    }

    private void initView(){
        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.btn_add_credentials).setOnClickListener(this);
        findViewById(R.id.btn_del_credentials).setOnClickListener(this);//显示删除证件按钮
        //列表
        ListView listView = (ListView)findViewById(R.id.listview_credentials);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GetCredentialsData getCredentialsData = adapter.getItem(position);
                boolean isShowDelBtn = adapter.isShowDelBtn();
                if(isShowDelBtn){
                    if(!delNow){
                        delNow = true;
                        delPosition = position;
                        int dataId = getCredentialsData.getId();
                        delCredentialsData(dataId);
                    }
                }else{
                    Intent intent = new Intent(mContext,CredentialsDetailActivity.class);
                    intent.putExtra("data", getCredentialsData);
                    intent.putExtra("addData",false);
                    intent.putExtra("hasIdCard",hasIdCard);
                    startActivity(intent);
                }
            }
        });
        List<GetCredentialsData> list  = new ArrayList<>();
        adapter = new CredentialsAdapter(list,CredentialsActivity.this );
        listView.setAdapter(adapter);

        AppUtils.setStateBar(CredentialsActivity.this,findViewById(R.id.frg_status_bar));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(null != data){
            if(requestCode == 4401){
                //更新列表数据
                getCredentialsData();//重新获取数据
            }else if(requestCode == 4402){
                //添加列表数据
                getCredentialsData();//重新获取数据
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_add_credentials:
                //增加证件
                Intent intent = new Intent(mContext,CredentialsDetailActivity.class);
                intent.putExtra("addData",true);//true 新增数据
                intent.putExtra("hasIdCard",hasIdCard);//是否有身份证
                startActivity(intent);
                break;
            case R.id.btn_del_credentials:
                //触发证件列表删除按钮
//                ToastUtil.toastNeedData(CredentialsActivity.this,"敬请期待~");
                adapter.toggleShowDelBtn();
                adapter.notifyDataSetInvalidated();
                break;
        }
    }

    /**
     * 获取证件列表
     */
    private void getCredentialsData() {
        User user = AppUtils.getUserInfo(mContext);
        Map<String,Object> params = new HashMap<>();
        params.put("ciphertext","test");
        params.put("loginType", Constant.APP_TYPE);
        params.put("employeeCode", user.getEmployeeCode());

        showLoading();
        RetrofitFactory.getInstance().createApi(OrderApi.class).getCredentialsData(params)
                .enqueue(new CallBack<List<GetCredentialsData>>() {
                    @Override
                    public void success(List<GetCredentialsData> getCredentialsDataList) {
                        dismissLoading();
                        if(getCredentialsDataList.size() > 0){
                            //判断该用户是否有身份证证件信息
                            for (GetCredentialsData listMap: getCredentialsDataList) {
                                int documentId = listMap.getDocumentId();
                                if(documentId == 1){
                                    hasIdCard = true;
                                }
                            }
                            adapter.setList(getCredentialsDataList);
                            adapter.notifyDataSetChanged();
                        }else{
                            showToast("暂无证件数据");
                        }
                    }

                    @Override
                    public void fail(String code) {
                        dismissLoading();
                    }
                });
    }

    /**
     * 删除证件
     */
    private void delCredentialsData(int id) {
        User user = AppUtils.getUserInfo(mContext);
        Map<String,Object> params = new HashMap<>();
        params.put("ciphertext","test");
        params.put("loginType", Constant.APP_TYPE);
        params.put("employeeCode", user.getEmployeeCode());
        params.put("id",id);

        showLoading();
        RetrofitFactory.getInstance().createApi(OrderApi.class).delCredentialsData(params)
                .enqueue(new CallBack<Object>() {
                    @Override
                    public void success(Object object) {
                        dismissLoading();
                        showToast("证件删除成功");
                        List<GetCredentialsData> list = adapter.getList();
                        List<GetCredentialsData> newList = new ArrayList<>();
                        for(int i = 0 ;i < list.size() ;i++){
                            if(i != delPosition){
                                newList.add(list.get(i));
                            }
                        }
                        adapter.setList(newList);
                        adapter.notifyDataSetChanged();
                        delNow = false;
                    }

                    @Override
                    public void fail(String code) {
                        dismissLoading();
                        delNow = false;
                    }
                });
    }

}
