package com.neusoft.zcapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.neusoft.zcapplication.Bean.User;
import com.neusoft.zcapplication.base.BaseFragment;
import com.neusoft.zcapplication.http.NetWorkRequest;
import com.neusoft.zcapplication.http.URL;
import com.neusoft.zcapplication.tools.AppUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


/**
 * 首页，国际预定申请单列表页面
 */

public class OutOrderFragment extends BaseFragment implements View.OnClickListener{

    private View orderFragment;
//    private List<Map<String,Object>> orderList = new ArrayList<>();
    private ApplyOrderListAdapter adapter;

    public static OutOrderFragment getInstance(){
        OutOrderFragment fragment = new OutOrderFragment();
        return fragment;
    }

//    private DefinedListView listView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
       super.onCreateView(inflater,container, savedInstanceState);
        if(null == orderFragment){
            orderFragment = inflater.inflate(R.layout.frg_order, container, false);
            initView();

        }else{
            ViewGroup parent = (ViewGroup) orderFragment.getParent();
            if(parent != null){
                parent.removeView(orderFragment);
            }
        }

        return orderFragment;
    }

    @Override
    protected void initData() {
        super.initData();
        queryOrderList(2);
    }

    private void initView() {
        ListView listView  =  (ListView) orderFragment.findViewById(R.id.frg_apply_order_list);
//        orderFragment.findViewById(R.id.frg_add_bill).setOnClickListener(this);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                List<Map<String,Object>> l = adapter.getList();
//            }
//        });
        List<Map<String,Object>> list = new ArrayList<>();
        adapter = new ApplyOrderListAdapter(getActivity(),list,0);
        listView.setAdapter(adapter);
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(null != data){
//            if(requestCode == 2001) {
//                queryOrderList(0);
//            }
//        }
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.frg_add_bill:
//                Intent intent = new Intent(getActivity(), ApprovalActivity.class);
//                startActivityForResult(intent,2001);
//                break;
        }
    }

    /**
     *查询预定申请单列表数据
     * @param dataType //0:全部，1：国内机票，2：国际机票，3：酒店
     */
    private void queryOrderList(int dataType) {
        Map<String,Object> params = new HashMap<>();
        User user = new AppUtils().getUserInfo(getActivity());
        String employeeCode = user.getEmployeeCode();
        params.put("ciphertext","test");
        params.put("employeeCode",employeeCode);
        params.put("loginType",URL.LOGIN_TYPE);
        params.put("type",dataType);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL.ZHONGCHE_93)
                .baseUrl("http://192.168.1.115:4001/")
                //增加返回值为String的支持
                .addConverterFactory(ScalarsConverterFactory.create())
                //增加返回值为Gson的支持(以实体类返回)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        Call<Map<String,Object>> call = request.getOrderList(params);
        call.enqueue(new Callback<Map<String,Object>>() {
            /**
             * @param call
             * @param response
             */
            @Override
            public void onResponse(Call<Map<String,Object>> call, Response<Map<String,Object>> response) {
                Map<String,Object> result = response.body();
                if(null != result){
                    String code = (String) result.get("code");
                    String codeMsg = (String) result.get("codeMsg");
                    List<Map<String,Object>> datalist = (ArrayList)result.get("data");
                    if(code.equals("00000")) {
                        adapter.setList(datalist);
                        adapter.notifyDataSetChanged();
                    }else {

                    }
                }else{
                    //请求失败
                    Toast.makeText(getActivity(),"请求失败",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Map<String,Object>> call, Throwable t) {
                Toast.makeText(getActivity(),"请求失败",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
