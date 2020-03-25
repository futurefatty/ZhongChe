package com.neusoft.zcapplication.HotelService;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.base.BaseActivity;
import com.neusoft.zcapplication.http.NetWorkRequest;
import com.neusoft.zcapplication.http.RequestCallback;
import com.neusoft.zcapplication.http.RequestUtil;
import com.neusoft.zcapplication.http.URL;
import com.neusoft.zcapplication.tools.AppUtils;
import com.neusoft.zcapplication.tools.DisplayUtil;
import com.neusoft.zcapplication.tools.ToastUtil;
import com.neusoft.zcapplication.widget.PopupWinListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * 酒店入住信息页面
 */

public class HotelListQueryActivity extends BaseActivity implements View.OnClickListener,RequestCallback {
    private HotelListQueryAdapter adapter;
    private View popupView;
    private PopupWindow window = new PopupWindow();
    private Call<Map<String,Object>> dataCall,detailCall,queryCall;
    private double channelId,listHotelId;//点击确认的订单的供应商id和订单id
    private int queryPosition = -1;//确认订单的下标
    private boolean getQuery = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_list_query);
        initView();
        getQueryHotelList();
    }
    private void initView(){
        AppUtils.setStateBar(HotelListQueryActivity.this,findViewById(R.id.frg_status_bar));
        findViewById(R.id.btn_back).setOnClickListener(this);//返回

        List<Map<String,Object>> list = new ArrayList<>();
        adapter =  new HotelListQueryAdapter(HotelListQueryActivity.this,list);
        ListView listView = (ListView)findViewById(R.id.act_hotel_query_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String,Object> m = adapter.getItem(position);
                if(!getQuery){
                    queryPosition = position;
                    getOrderDetailInfo(m);
                }else{
                    ToastUtil.toastNeedData(HotelListQueryActivity.this,"正在获取数据，请稍候！");
                }
            }
        });
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
    public void onBackPressed() {
        super.onBackPressed();
        //如果当前正在加载详情数据，则取消数据
        if(null != detailCall && detailCall.isExecuted()){
            detailCall.cancel();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(null != dataCall && dataCall.isExecuted()){
            dataCall.cancel();
        }
        if(null != detailCall && detailCall.isExecuted()){
            detailCall.cancel();
        }
        if(null != queryCall && queryCall.isExecuted()){
            queryCall.cancel();
        }
    }

    @Override
    public void requestSuccess(Object map, int type) {
        Map<String,Object> m = (Map<String,Object>) map;
        String code = null == m.get("code") ? "" : m.get("code").toString();
        if(type == 1){
            if(code.equals("00000")){
                List<Map<String,Object>> list = (List<Map<String, Object>>) m.get("data");
                if(list.size() > 0){
                    adapter.setList(list);
                    adapter.notifyDataSetChanged();
                }else{
                    ToastUtil.toastNeedData(HotelListQueryActivity.this,"暂无相关数据！");
                }
            }else{
                ToastUtil.toastNeedData(HotelListQueryActivity.this,"获取数据失败，请稍候再试！");
            }
        }else if(type == 2){
            getQuery = false;
            //显示详情数据
            if(code.equals("00000")){
                List<Map<String,Object>> list = (List<Map<String, Object>>) m.get("data");
                showPopupWindows(9,list);
            }else{
                ToastUtil.toastNeedData(HotelListQueryActivity.this,"获取数据失败，请稍候再试！");
            }
        }else if(type == 3){
            //确认入住信息请求成功的返回结果
            if(code.equals("00000")){
                ToastUtil.toastNeedData(HotelListQueryActivity.this,"入住信息确认成功！");
                List<Map<String,Object>> hotelList = adapter.getList();
                List<Map<String,Object>> newList = new ArrayList<>();
                for(int i = 0 ; i < hotelList.size() ;i++){
                    if(i != queryPosition){
                        newList.add(hotelList.get(i));
                    }
                }
                adapter.setList(newList);
                adapter.notifyDataSetChanged();
            }else{
                ToastUtil.toastNeedData(HotelListQueryActivity.this,"入住信息确认失败，请稍候再试！");
            }
        }
    }

    @Override
    public void requestFail(int type) {
        if(type == 1){
            ToastUtil.toastNeedData(HotelListQueryActivity.this,"获取数据失败，请稍候再试！");
        }else if(type  == 2){
            getQuery = false;
            ToastUtil.toastNeedData(HotelListQueryActivity.this,"获取数据失败，请稍候再试！");
        }else if(type == 3){
            ToastUtil.toastNeedData(HotelListQueryActivity.this,"入住信息确认失败，请稍候再试！");
        }
    }

    @Override
    public void requestCancel(int type) {

    }

    /**
     * 获取列表数据
     */
    private void getQueryHotelList(){
        Map<String,Object> params = new HashMap<>();
        params.put("ciphertext","test");
        params.put("employeeCode",new AppUtils().getUserInfo(HotelListQueryActivity.this).getEmployeeCode());
        params.put("loginType", URL.LOGIN_TYPE);
        Retrofit retrofit = new RequestUtil().getRequestClient(URL.ZHONGCHE_93);
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        dataCall = request.getQueryHotelOrderList(params);
        new RequestUtil().requestData(dataCall,this,1,"加载数据...",true,HotelListQueryActivity.this);
    }

    /**
     * 获取订单详情
     * @param map
     */
    private void getOrderDetailInfo(Map<String,Object> map){
        getQuery = true;
        Map<String,Object> params = new HashMap<>();
        double supplierId = (double)map.get("supplierId");
        listHotelId =(double) map.get("id");
        channelId = supplierId;
        String orderApplyId = map.get("orderApplyId").toString();
        params.put("ciphertext","test");
        params.put("orderApplyId",orderApplyId);//酒店订单id
        params.put("loginType", URL.LOGIN_TYPE);
        params.put("style", supplierId);//供应商id
        Retrofit retrofit = new RequestUtil().getRequestClient(URL.ZHONGCHE_93);
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        detailCall = request.getQueryHotelOrderDetail(params);
        new RequestUtil().requestData(detailCall,this,2,"加载数据...",true,HotelListQueryActivity.this);
    }

    /**
     * 确认入住信息
     * @param list
     */
    private void queryData(List<Map<String,Object>> list){
        Map<String,Object> params = new HashMap<>();
//        double supplierId = (double)map.get("supplierId");
//        String orderApplyId = map.get("orderApplyId").toString();
        //遍历出选择数据的id
        List<String> idList = new ArrayList<>();
        for(int i = 0 ; i < list.size() ;i++){
            String roomInfoId = list.get(i).get("roomInfoId").toString();
            idList.add(roomInfoId);
        }
        params.put("ciphertext","test");
        params.put("id",(int)listHotelId);//酒店记录的id(数据库表的id)
        params.put("loginType", URL.LOGIN_TYPE);
        params.put("roomInfoIdList",idList);
        params.put("style", (int)channelId);//供应商id
        params.put("userNo", new AppUtils().getUserInfo(HotelListQueryActivity.this).getEmployeeCode());
        Retrofit retrofit = new RequestUtil().getRequestClient(URL.ZHONGCHE_93);
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        queryCall = request.updateHotelInDay(params);
        new RequestUtil().requestData(queryCall,this,3,"正在提交数据...",true,HotelListQueryActivity.this);
    }
    private void showPopupWindows(final int type, final List<Map<String,Object>> datalist) {
        LinearLayout ly_main = (LinearLayout) findViewById(R.id.ly_main);
        //构建一个popupwindow的布局
        popupView = HotelListQueryActivity.this.getLayoutInflater().inflate(R.layout.popupwindow_select_person, null);
        final PopupWinListAdapter popAdapter = new PopupWinListAdapter(HotelListQueryActivity.this,datalist,type);

        ListView listView = (ListView) popupView.findViewById(R.id.popupwin_list);
        listView.setAdapter(popAdapter);
        //类型列表点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String isCheck = null == datalist.get(position).get("isCheck") ?
                        "false" : datalist.get(position).get("isCheck").toString();
                /**
                 * 如果当前点击位置为选中状态，则将当前位置之后的数据都设为未选中状态
                 * 如果当前点击位置为未选中状态，则将当前位置之前的数据都设为选中状态
                 */
                for(int j = 0 ; j < datalist.size() ;j++){
                    if(isCheck.equals("true")){
                        if(j >= position){
                            datalist.get(j).put("isCheck","false");
                        }
                    }else{
                        if(j <= position){
                            datalist.get(j).put("isCheck","true");
                        }
                    }
                }
                popAdapter.notifyDataSetChanged();
            }
        });

        TextView popuptitle = (TextView) popupView.findViewById(R.id.popup_title);
        if(type == 9) {//出行方式
            popupView.findViewById(R.id.select_person).setVisibility(View.GONE);
            popupView.findViewById(R.id.popupwin_list).setVisibility(View.VISIBLE);
            popuptitle.setText("确认入住天数");
            TextView tv_ok = (TextView) popupView.findViewById(R.id.select_person_ok);
            tv_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<Map<String,Object>> allList =  popAdapter.getList();
                    List<Map<String,Object>> newList = new ArrayList<Map<String, Object>>();
;
                    for(int j = 0 ; j < allList.size() ;j++){
                        String isCheck = null == allList.get(j).get("isCheck") ?
                                "false" : allList.get(j).get("isCheck").toString();
                        if(isCheck.equals("true")){
                            newList.add(allList.get(j));
                        }
                    }
                    if(newList.size() > 0){
                        window.dismiss();
                        queryData(newList);
                    }else{
                        ToastUtil.toastNeedData(HotelListQueryActivity.this,"请选择入住信息~");
                    }

                }
            });
        }
        popupView.findViewById(R.id.select_person_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });

        //创建PopupWindow对象，指定宽度和高度
        int winWidth = DisplayUtil.getDeviceWidth(HotelListQueryActivity.this);
        int pop_width = (int)(winWidth * 0.9);
        window = new PopupWindow(popupView, pop_width, (int)(winWidth * 1.1));
//                window.setAnimationStyle(R.style.popup_window_anim);
        //设置背景颜色
        window.setBackgroundDrawable(new ColorDrawable(90000000));
        //设置可以获取焦点
        window.setFocusable(true);
        // 设置可以触摸弹出框以外的区域
        window.setOutsideTouchable(true);
        // TODO：更新popupwindow的状态
        window.update();
        popOutShadow(window);
        window.showAtLocation(ly_main, Gravity.CENTER,0,0);
    }
    /**
     * 弹窗外阴影
     * @param popupWindow
     */
    private void popOutShadow(PopupWindow popupWindow) {
        WindowManager.LayoutParams lp = HotelListQueryActivity.this.getWindow().getAttributes();
        lp.alpha = 0.3f;//设置阴影透明度
        HotelListQueryActivity.this.getWindow().setAttributes(lp);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            /**当关闭弹窗给筛选条件赋值*/
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = HotelListQueryActivity.this.getWindow().getAttributes();
                lp.alpha = 1f;
                HotelListQueryActivity.this.getWindow().setAttributes(lp);
            }
        });
    }
}
