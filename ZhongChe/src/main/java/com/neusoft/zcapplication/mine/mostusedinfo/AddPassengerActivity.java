package com.neusoft.zcapplication.mine.mostusedinfo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.neusoft.zcapplication.Bean.User;
import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.base.BaseActivity;
import com.neusoft.zcapplication.http.NetWorkRequest;
import com.neusoft.zcapplication.http.RequestCallback;
import com.neusoft.zcapplication.http.RequestUtil;
import com.neusoft.zcapplication.http.URL;
import com.neusoft.zcapplication.mine.mostusedinfo.adapter.AddPsgCardListAdapter;
import com.neusoft.zcapplication.mine.mostusedinfo.bean.PsgCardBean;
import com.neusoft.zcapplication.tools.AlertUtil;
import com.neusoft.zcapplication.tools.AppUtils;
import com.neusoft.zcapplication.tools.DateUtils;
import com.neusoft.zcapplication.tools.ToastUtil;
import com.neusoft.zcapplication.widget.DefinedListView;
import com.neusoft.zcapplication.widget.PopupWinListAdapter;
import com.neusoft.zcapplication.widget.TimeSelector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * 添加常用联系人
 */

public class AddPassengerActivity extends BaseActivity implements View.OnClickListener,
        AddPsgCardListAdapter.ItemViewClick,RequestCallback{
    private AddPsgCardListAdapter adapter;
    private TimeSelector timeSelector;
    private int timeIndex;//选择证件有效期item下标
    private PopupWindow window = new PopupWindow();
    private View popupView;
    private Map<String,Object> selectType;//选择的证件类型map
    private List<Map<String,Object>> typeList;//获取到的证件类型数据
    private int selectTypeIndex;//切换证件item的下标
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_passenger);
        initView();
        getDocumentType();//获取证件类型
    }

    private void initView(){
        typeList = new ArrayList<>();
        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.btn_personal_save).setOnClickListener(this);
        findViewById(R.id.btn_add_certificate).setOnClickListener(this);
        findViewById(R.id.btn_del_certificate).setOnClickListener(this);
        //设置title栏样式
        AppUtils.setStateBar(AddPassengerActivity.this,findViewById(R.id.frg_status_bar));

        //证件列表初始化
        DefinedListView listView = (DefinedListView)findViewById(R.id.add_psg_list_view);
        List<PsgCardBean> list = new ArrayList<>();
        list.add(generalCardData());
        adapter = new AddPsgCardListAdapter(list,AddPassengerActivity.this);
        listView.setAdapter(adapter);
        adapter.setItemViewClick(this);//添加点击监控

        timeSelector = new TimeSelector(AddPassengerActivity.this, new TimeSelector.ResultHandler() {
            @Override
            public void handle(String time) {
                String timeStr = time.substring(0,10);
                updateCardDate(timeIndex,timeStr);
            }
        }, DateUtils.generalBeginDate(), DateUtils.generalEndDate(50));
        timeSelector.setMode(TimeSelector.MODE.YMD);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_personal_save:
                //保存旅客信息
                addContacts();
//                intent = new Intent();
//                setResult(RESULT_OK,intent);
//                finish();
                break;
            case R.id.btn_add_certificate:
                //添加证书按钮
                List<PsgCardBean> oldList = adapter.getList();
                oldList.add(generalCardData());
                adapter.setList(oldList);
                adapter.notifyDataSetChanged();
                break;
            case R.id.btn_del_certificate:
                //删除证书按钮
                adapter.toggleDelBtn();
                adapter.notifyDataSetInvalidated();
                break;
        }
    }

    /**
     * 删除证件类型
     * @param position
     */
    @Override
    public void delItem(int position) {
        List<PsgCardBean> oldList = adapter.getList();
        List<PsgCardBean> list = new ArrayList<>();
        for(int i = 0 ; i < oldList.size(); i++){
            if(i != position){
                list.add(oldList.get(i));
            }
        }
        adapter.setList(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void selectDate(int position) {
        timeIndex = position;
        timeSelector.show();
    }

    @Override
    public void selectCardType(int position) {
        selectTypeIndex = position;
        showPopupWindows(typeList);
    }

    @Override
    public void requestFail(int type) {
        if(type == 1){
            ToastUtil.toastNeedData(AddPassengerActivity.this,"添加联系人失败，请稍候再试~");
        }
    }

    @Override
    public void requestSuccess(Object map, int type) {
        Map<String,Object> result = (Map<String,Object>) map;
        if(type == 1){
            if(null != result){
                String code = null == result.get("code") ? "" : result.get("code").toString();
                if(code.equals("00000")) {
                    ToastUtil.toastNeedData(AddPassengerActivity.this,"添加联系人成功~");
                    Intent intent = new Intent();
                    setResult(RESULT_OK,intent);
                    finish();
                }else{
                    ToastUtil.toastNeedData(AddPassengerActivity.this,"添加联系人失败，请稍候再试~");
                }
            }else{
                ToastUtil.toastNeedData(AddPassengerActivity.this,"添加联系人失败，请稍候再试~");
            }
        }else if(type == 2){
            //获取证件类型信息
            if(null != result){
                String code = null == result.get("code") ? "" : result.get("code").toString();
                if(code.equals("00000")) {
                    List<Map<String,Object>> datalist = (ArrayList)result.get("data");
                    typeList.addAll(datalist);
                }else{
                    ToastUtil.toastError(AddPassengerActivity.this);
                }
            }else{
                //请求失败
                ToastUtil.toastError(AddPassengerActivity.this);
            }
        }
    }

    @Override
    public void requestCancel(int type) {

    }

    /**
     * 生成证件信息
     * @return
     */
    private PsgCardBean generalCardData(){
        PsgCardBean bean = new PsgCardBean();
        return bean;
    }

    /**
     * 更新证件有效日期
     * @param index
     * @param cardDate
     */
    private void updateCardDate(int index,String cardDate){
        List<PsgCardBean> list = adapter.getList();
        for(int i = 0 ;i < list.size() ; i++){
            if(i  == index){
                list.get(i).setCardDate(cardDate);
                break;
            }
        }
        adapter.notifyDataSetInvalidated();
    }

    /**
     * 获取证件分类
     */
    private void getDocumentType() {
        Map<String,Object> params = new HashMap<>();
        params.put("ciphertext","test");
        params.put("loginType", URL.LOGIN_TYPE);
        Retrofit retrofit = new RequestUtil().getRequestClient(URL.ZHONGCHE_93);
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        Call<Map<String,Object>> call = request.getDocumentType(params);
        new RequestUtil().requestData(call,this,2,"加载中...",false,AddPassengerActivity.this);
    }
    /**
     * 显示弹窗
     */
    private void showPopupWindows(final List<Map<String,Object>> datalist) {
        popupView = AddPassengerActivity.this.getLayoutInflater().inflate(R.layout.popupwindow_select_person, null);
        final PopupWinListAdapter popAdapter = new PopupWinListAdapter(AddPassengerActivity.this,datalist,5);
        for(int i=0;i<popAdapter.getCount();i++) {
            datalist.get(i).put("isCheck","false");
        }
        ListView listView = (ListView) popupView.findViewById(R.id.popupwin_list);
        listView.setAdapter(popAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for(int i=0;i<popAdapter.getCount();i++) {
                    datalist.get(i).put("isCheck","false");
                }
                datalist.get(position).put("isCheck","true");
                selectType = datalist.get(position);
                popAdapter.notifyDataSetChanged();
            }
        });

        TextView popuptitle = (TextView) popupView.findViewById(R.id.popup_title);
        popupView.findViewById(R.id.select_person).setVisibility(View.GONE);
        popupView.findViewById(R.id.popupwin_list).setVisibility(View.VISIBLE);
        popuptitle.setText("选择证件类型");
        TextView tv_ok = (TextView) popupView.findViewById(R.id.select_person_ok);
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null == selectType){
                    ToastUtil.toastNeedData(AddPassengerActivity.this,"请选择证件类型!");
                }else{
                    //判断当前是否选择的为身份证
                    int documentId = (int)((double) selectType.get("id"));
                    String cardName = selectType.get("name")+"" ;
                    if(hasSameTypeCard(documentId)){
                        window.dismiss();
                        AlertUtil.show(AddPassengerActivity.this,"您已添加了" + cardName+"，不能重复添加~","确定",null,"取消",null,"温馨提示");
                    }else{
                        List<PsgCardBean> cardList = adapter.getList();
                        for(int i= 0 ;i < cardList.size();i++){
                            if(i  == selectTypeIndex){
                                cardList.get(i).setId(documentId);
                                cardList.get(i).setCardType(cardName);
                                adapter.setList(cardList);
                                adapter.notifyDataSetChanged();
                                break;
                            }
                        }
                        window.dismiss();
                    }
                }
            }
        });

        popupView.findViewById(R.id.select_person_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });

        int pop_width = (int)(getDeviceWidth()*0.9);
        window = new PopupWindow(popupView, pop_width, (int)(getDeviceWidth()*1.1));
        //设置动画
//                window.setAnimationStyle(R.style.popup_window_anim);
        //设置背景颜色
        window.setBackgroundDrawable(new ColorDrawable(90000000));
        //设置可以获取焦点
        window.setFocusable(true);
        // 设置可以触摸弹出框以外的区域
        window.setOutsideTouchable(true);
        window.update();

        popOutShadow(window);
        //以下拉的方式显示，并且可以设置显示的位置
//                        window.showAsDropDown(inputSearch, 0, 50);
        window.showAtLocation(findViewById(R.id.add_psg_root), Gravity.CENTER,0,0);
    }
    public int getDeviceWidth(){
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        return w_screen;
    }

    /**
     * 弹窗外阴影
     * @param popupWindow
     */
    private void popOutShadow(PopupWindow popupWindow) {
        WindowManager.LayoutParams lp = AddPassengerActivity.this.getWindow().getAttributes();
        lp.alpha = 0.3f;//设置阴影透明度
        AddPassengerActivity.this.getWindow().setAttributes(lp);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            /**当关闭弹窗给筛选条件赋值*/
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = AddPassengerActivity.this.getWindow().getAttributes();
                lp.alpha = 1f;
                AddPassengerActivity.this.getWindow().setAttributes(lp);
            }
        });
    }

    /**
     * 判断是否有相同的证件类型
     * @param cardId
     * @return 若用同类证件，则返回true
     */
    private boolean hasSameTypeCard(int cardId){
        List<PsgCardBean> list = adapter.getList();
        boolean bool = false ;
        for(int i = 0 ; i < list.size() ;i++){
            int id = list.get(i).getId();
            if(cardId == id){
                bool = true;
                break;
            }
        }
        return bool;
    }
    private void addContacts(){
        //中文名
        EditText nameCnEt = (EditText)findViewById(R.id.act_add_psg_cn_name);
        String nameCnStr = nameCnEt.getText().toString().trim();
        //英文姓
        EditText nameEnEt = (EditText)findViewById(R.id.act_add_psg_en_name);
        String nameEnStr = nameEnEt.getText().toString().trim();
        //英文名
        EditText nameEnEt1 = (EditText)findViewById(R.id.act_add_psg_en_name1);
        String nameEnStr1 = nameEnEt1.getText().toString().trim();
        if(nameCnStr.equals("")){
            ToastUtil.toastNeedData(AddPassengerActivity.this,"请填写的中文姓名~");
        }else if(nameEnStr.equals("")){
            ToastUtil.toastNeedData(AddPassengerActivity.this,"请填写的英文姓~");
        }else if(nameEnStr1.equals("")){
            ToastUtil.toastNeedData(AddPassengerActivity.this,"请填写的英文名~");
        }else{
            if(hasFillCardInfo()){
                int id = getIntent().getIntExtra("id",-1);
                Map<String,Object> params = new HashMap<>();
                params.put("ciphertext","test");
                params.put("cnName",nameCnStr);
                User user = AppUtils.getUserInfo(AddPassengerActivity.this);
                params.put("employeeCode",user.getEmployeeCode());// 登录进去的员工编号
                params.put("familyName",nameEnStr);
                if(id != -1){
                    params.put("id",id);//数据id，当新增操作时，此字段不填
                }
                //该联系人的证件信息
                List<Map<String,Object>> cdList = new ArrayList<>();
                for(int i = 0 ;i < adapter.getList().size();i++){
                    PsgCardBean psgBean = adapter.getList().get(i);
                    Map<String,Object> mm = new HashMap<>();
                    mm.put("documentId",psgBean.getId());
                    mm.put("documentInfo",psgBean.getCardType());
                    mm.put("enddate",psgBean.getCardDate());
                    if(id != -1){
                        mm.put("id",id);
                    }
                    cdList.add(mm);
                }
                params.put("contactsDocumentList",cdList);
                params.put("loginType", URL.LOGIN_TYPE);
                params.put("secondName", nameEnStr1);
                Retrofit retrofit = new RequestUtil().getRequestClient(URL.ZHONGCHE_93);
                NetWorkRequest request = retrofit.create(NetWorkRequest.class);
                Call<Map<String,Object>> call = request.addContacts(params);
                new RequestUtil().requestData(call,this,1,"加载中...",false,AddPassengerActivity.this);
            }
        }
    }

    /**
     * 判断是否填完整了证件信息
     * @return
     */
    private boolean hasFillCardInfo(){
        List<PsgCardBean> list = adapter.getList();
        boolean hasData = true;
        if(list.size() > 0){
            for(int i = 0 ;i < list.size();i++){
                PsgCardBean bean = list.get(i);
                if(bean.getCardNum().equals("")){
                    ToastUtil.toastNeedData(AddPassengerActivity.this,"请填写完整证件号码~");
                    hasData = false;
                    break;
                }else if(bean.getCardType().equals("")){
                    ToastUtil.toastNeedData(AddPassengerActivity.this,"请选择证件类型~");
                    hasData = false;
                    break;
                }else if(bean.getCardDate().equals("")){
                    ToastUtil.toastNeedData(AddPassengerActivity.this,"请选择证件有效期~");
                    hasData = false;
                    break;
                }else{

                }
            }
        }else{
            ToastUtil.toastNeedData(AddPassengerActivity.this,"请添加证件~");
            return false;
        }
        return hasData;
    }
}
