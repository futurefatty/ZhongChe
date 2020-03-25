package com.neusoft.zcapplication.TicketService;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.neusoft.zcapplication.Bean.OuterTripItem;
import com.neusoft.zcapplication.Bean.PersonItem;
import com.neusoft.zcapplication.Bean.User;
import com.neusoft.zcapplication.Calendar.CalendarActivity;
import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.approval.OutTripListAdapter;
import com.neusoft.zcapplication.approval.TripItemClickListener;
import com.neusoft.zcapplication.base.BaseActivity;
import com.neusoft.zcapplication.http.NetWorkRequest;
import com.neusoft.zcapplication.http.RequestCallback;
import com.neusoft.zcapplication.http.RequestUtil;
import com.neusoft.zcapplication.http.URL;
import com.neusoft.zcapplication.tools.AlertUtil;
import com.neusoft.zcapplication.tools.AppUtils;
import com.neusoft.zcapplication.tools.StringUtil;
import com.neusoft.zcapplication.tools.ToastUtil;
import com.neusoft.zcapplication.widget.DefinedListView;
import com.neusoft.zcapplication.widget.PopupWinListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;

public class PrivateInternationalApplyCreateActivity extends BaseActivity implements
        View.OnClickListener,RequestCallback, TripItemClickListener, PrivateInternationalPersonListAdapter.Option {

    private DefinedListView listViewPerson;
    private DefinedListView outGoTripListView;
    private PrivateInternationalPersonListAdapter adapterPerson;//同行人适配器
    private List<PersonItem> listPerson;
    private PopupWindow window = new PopupWindow();
    private View popupView;
    private Map<String,Object> selectPerson,currentUser;//选择的出行人，当前用户
    private Map<String,Object> selectTripType;//选择的出行方式
    private List<Map<String,Object>> personInfoList;//同行人信息
    private boolean canClick = true;//提交按钮开关，防止重复提交订单
    private OutTripListAdapter outGoAdapter;//国际机票行程适配器
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_international_apply_create);
        initView();
        initViewData();//初始化数据
        getPersonList(true);
    }

    private void initView(){
        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.btn_add_trip).setOnClickListener(this);
        findViewById(R.id.btn_del_trip).setOnClickListener(this);
        findViewById(R.id.btn_add_person).setOnClickListener(this);
        findViewById(R.id.btn_del_person).setOnClickListener(this);
        findViewById(R.id.btn_save_order).setOnClickListener(this);

        listViewPerson = (DefinedListView)findViewById(R.id.person_list);
        listPerson = new ArrayList<>();
        adapterPerson = new PrivateInternationalPersonListAdapter(PrivateInternationalApplyCreateActivity.this,listPerson,this);
        listViewPerson.setAdapter(adapterPerson);
        //设置顶部状态栏样式
        AppUtils.setStateBar(PrivateInternationalApplyCreateActivity.this,findViewById(R.id.frg_status_bar));
        //国际机票行程
        outGoTripListView = (DefinedListView)findViewById(R.id.out_trip_go_list_view);//国际返去程
        List<OuterTripItem> outGoList = new ArrayList<>();
        outGoList.add(generalOuterTrip());
        outGoAdapter = new OutTripListAdapter(PrivateInternationalApplyCreateActivity.this,outGoList);
        outGoAdapter.setOutTripListAdapter(outGoAdapter);
        outGoAdapter.setItemCellClick(this);//item删除按钮点击事件
        outGoTripListView.setAdapter(outGoAdapter);

    }

    private void initViewData(){
        //人员列表
        listPerson.clear();
        adapterPerson.setList(listPerson);
        adapterPerson.notifyDataSetChanged();
    }

    /**
     * 生成国际行程数据
     * @return
     */
    private OuterTripItem generalOuterTrip(){
        OuterTripItem outerTripItem = new OuterTripItem();
//        outerTripItem.setStartTime(DateUtils.getDate(1));
        return outerTripItem;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_add_trip:
                //添加国际机票行程数据
                List<OuterTripItem> outGoList = outGoAdapter.getList();
                outGoList.add(generalOuterTrip());
                outGoAdapter.setList(outGoList);
                outGoAdapter.notifyDataSetChanged();
                break;
            case R.id.btn_del_trip:
                //删除国际机票行程数据
                outGoAdapter.toggleShowDelBtn();
                outGoAdapter.notifyDataSetInvalidated();
                break;
            case R.id.btn_add_person:
                //如果已经获取到了授权人信息，点击添加同行人直接显示人员弹窗,否则重新获取数据
                if(null != personInfoList && personInfoList.size() >= 0){
                    showPopupWindows(1,personInfoList);
                }else{
                    getPersonList(false);
                }
                break;
            case R.id.btn_del_person:
                adapterPerson.toggleDelBtn();
                adapterPerson.notifyDataSetChanged();
                break;
            case R.id.btn_save_order:
                if (canClick){
                    canClick = false;
                    saveOrderApply();
                }
                break;
            case R.id.btn_back:
                finish();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(null != data){
            if(requestCode == 101 ){
                //修改人员信息后的返回事件
//                Bundle bundle = data.getExtras();
//                PersonItem personItem = (PersonItem) bundle.getSerializable("item");

                Map<String,Object> map = (Map<String, Object>) data.getSerializableExtra("item");
//                map.put("name",name);
//                map.put("employeeCode",employeeCode);
//                map.put("idNo",idNo);
//                map.put("mobile",mobile);
//                map.put("accountEntity",accountEntity);
//                map.put("isShowItem",isShowItem);
//                List<Map<String, Object>> list = (ArrayList)selectPerson.get("company");
//                map.put("accountEntityList",list);
//                map.put("accountEntity",accountEntity);
//                map.put("credentialsInfo",credentialsInfo);


                PersonItem personItem = new PersonItem();
                personItem.setName(map.get("name").toString());
                personItem.setEmployeeCode(map.get("employeeCode").toString());
                personItem.setIdNo(map.get("idNo").toString());
                personItem.setMobile(map.get("mobile").toString());
                personItem.setAccountEntity(map.get("accountEntity").toString());
                personItem.setShowItem((Boolean) map.get("isShowItem"));
                List<Map<String, Object>> accountEntityList = (List<Map<String, Object>>) map.get("accountEntityList");
                List<Map<String, Object>> credentialsInfo = (List<Map<String, Object>>) map.get("credentialsInfo");
                personItem.setAccountEntityList(accountEntityList);
                personItem.setCredentialsInfo(credentialsInfo);

                int position = data.getIntExtra("position",0);
                listPerson.set(position,personItem);
                adapterPerson.notifyDataSetChanged();
            }else if(requestCode == 2202){
                //选择国际行程出发时间
                String firstDay = data.getStringExtra("firstDay");
                int position = data.getIntExtra("position",-1);
                if(position != -1){
                    List<OuterTripItem> outTripList = outGoAdapter.getList();
                    for(int i = 0 ; i < outTripList.size(); i++){
                        if(i == position){
                            outTripList.get(i).setStartTime(firstDay);
                            break;
                        }
                    }
                    outGoAdapter.setList(outTripList);
                    outGoAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    /**
     * 显示弹窗
     * @param type  1选择同行人界面，2选择出行方式； 3选择核算主体界面，6 选择证件类型界面
     */
    private void showPopupWindows(final int type, final List<Map<String,Object>> datalist) {
        LinearLayout ly_main = (LinearLayout) findViewById(R.id.ly_main);
        //构建一个popupwindow的布局
        popupView = PrivateInternationalApplyCreateActivity.this.getLayoutInflater().inflate(R.layout.popupwindow_select_person, null);
        final PopupWinListAdapter popAdapter = new PopupWinListAdapter(PrivateInternationalApplyCreateActivity.this,datalist,type);
//        for(int i=0;i<popAdapter.getCount();i++) {
//            datalist.get(i).put("isCheck","false");
//        }
        if (type == 2){
            //默认出行方式为第二种
            datalist.get(1).put("isCheck","true");
            selectTripType = datalist.get(1);
        }else if(type  == 1){
            //选择同行人时，默认选择的用户为当前登录用户，不在dataList的数据里面
            for(int i=0;i < popAdapter.getCount();i++) {
                datalist.get(i).put("isCheck","false");
            }
        }else{
            //弹出类型选择窗口时，默认设置第一个为选择项
            for(int g = 0 ;g < datalist.size();g++){
                if(g == 0){
                    datalist.get(0).put("isCheck","true");
                }else{
                    datalist.get(g).put("isCheck","false");
                }
            }
        }
        ListView listView = (ListView) popupView.findViewById(R.id.popupwin_list);
        listView.setAdapter(popAdapter);
        //类型列表点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for(int i=0;i<popAdapter.getCount();i++) {
                    datalist.get(i).put("isCheck","false");
                }
                datalist.get(position).put("isCheck","true");
                popAdapter.notifyDataSetChanged();
                if(type == 1) {
                    //选择人
                    ImageView icon_check = (ImageView) popupView.findViewById(R.id.icon_check);
                    icon_check.setImageResource(R.drawable.btn_singleselection_nor);
                    selectPerson = datalist.get(position);
                }else if(type == 2) {
                    //添加行程，选择出行方式
                    selectTripType = datalist.get(position);
                }else if(type == 3) {
                    //选择核算主体
//                    selectAccountEntity = datalist.get(position);
                }else if(type  == 5){
                    //选择证件类型
                }
            }
        });

        TextView popuptitle = (TextView) popupView.findViewById(R.id.popup_title);
        if(type == 1) {
            //出行人员
            TextView current_username = (TextView) popupView.findViewById(R.id.item_current_username);
            current_username.setText(currentUser.get("employeeName")+"");
//            popupView.findViewById(R.id.popupwin_list).setVisibility(View.GONE);
            popupView.findViewById(R.id.select_person).setVisibility(View.VISIBLE);
            TextView tv_ok = (TextView) popupView.findViewById(R.id.select_person_ok);
            tv_ok.setText("确定");
            tv_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i=0;i<listPerson.size();i++){
                        PersonItem item = listPerson.get(i);
                        if (null!=item.getEmployeeCode()&&item.getEmployeeCode().equals(selectPerson.get("employeeCode"))){
                            AlertUtil.show2(PrivateInternationalApplyCreateActivity.this, "您已添加过该人员", "确定", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            });
                            return;
                        }
                    }
                    window.dismiss();
                    PersonItem itemPerson = new PersonItem();
                    itemPerson.setShowItem(true);
                    itemPerson.setShowDel(false);
                    itemPerson.setName(selectPerson.get("employeeName")+"");
                    itemPerson.setEmployeeCode(selectPerson.get("employeeCode")+"");
                    itemPerson.setIdNo(selectPerson.get("idCard")+"");
                    itemPerson.setMobile(selectPerson.get("mobil")+"");
                    listPerson.add(itemPerson);
                    adapterPerson.setList(listPerson);
                    adapterPerson.notifyDataSetChanged();
                    //选择某个人员
                    /**
                     * 1、若该人员只有一个可用证件类型
                     *  a、若该人员一个核算主体，则直接添加人员；
                     *  b、若有多个核算主体，则下一步进入选择核算主体的步骤；
                     * 2、若该人员有多个可用证件
                     *    a、若该人员一个核算主体，则直接添加人员；
                     *    b、若有多个核算主体，则下一步进入选择核算主体的步骤；
                     * 3、若该人员没有可用证件，则直接提示无法添加该乘客
                     *
                     */
                    /*List<Map<String,Object>> companyList = (List<Map<String,Object>>) selectPerson.get("company");
                    List<Map<String,Object>> documentInfoList = (List<Map<String,Object>>) selectPerson.get("documentInfo");
                    if(documentInfoList.size() == 1){
                        //设置第一个证件类型为选中的状态
                        documentInfoList.get(0).put("isCheck","true");
                        selectPerson.put("documentInfo",documentInfoList);//缓存全局
                        if(companyList.size() == 1){
                            PersonItem itemPerson = new PersonItem();
                            itemPerson.setShowItem(true);
                            itemPerson.setShowDel(false);
                            itemPerson.setName(selectPerson.get("employeeName")+"");
                            itemPerson.setEmployeeCode(selectPerson.get("employeeCode")+"");
                            itemPerson.setIdNo(selectPerson.get("idCard")+"");
                            itemPerson.setMobile(selectPerson.get("mobil")+"");
                            Map<String,Object> map = companyList.get(0);//默认选中核算主体
                            itemPerson.setAccountEntity(map.get("companyName")+"");
                            itemPerson.setCredentialsInfo(documentInfoList);
                            //设置第一个核算主体为选中状态
                            companyList.get(0).put("isCheck","true");
                            itemPerson.setAccountEntityList(companyList);

                            listPerson.add(itemPerson);
                            adapterPerson.setList(listPerson);
                            adapterPerson.notifyDataSetChanged();
                        }else{
                            //设置选中的证件类型，并显示核算主体选择界面
                            selectPerson.put("documentInfo",documentInfoList);
                            showPopupWindows(3,companyList);
                        }
                    }else if(documentInfoList.size() > 1){
                        //显示选择证件类型界面
                        *//**
                         * 1、创建国内机票预定申请单时
                         *   a、若用户有身份证件信息，设置人员身份证为选中状态，并判断用户有多少个核算主体：
                         *      i、若用户有多少个核算主体，则进入选择核算主体界面；
                         *      ii、若用户只有一个核算主体，则直接添加该人员
                         *   b、若没有身份证，则进入选择证件的弹窗
                         * 2、其他情况直接进入选择证件界面
                         *//*
                        showPopupWindows(6,documentInfoList);
                    }else{
                        ToastUtil.toastNeedData(PrivateInternationalApplyCreateActivity.this,"该人员没有可用证件信息！");
                    }*/
//                    //判断当前选中的人是否有多个核算主体
//                    if(companyList.size() > 1){
//                        showPopupWindows(3,companyList);
//                    }else if(companyList.size() == 1){
//                        PersonItem itemPerson = new PersonItem();
//                        itemPerson.setShowItem(true);
//                        itemPerson.setShowDel(false);
//                        itemPerson.setName(selectPerson.get("employeeName")+"");
//                        itemPerson.setEmployeeCode(selectPerson.get("employeeCode")+"");
//                        itemPerson.setIdNo(selectPerson.get("idCard")+"");
//                        itemPerson.setMobile(selectPerson.get("mobil")+"");
//                        Map<String,Object> map = companyList.get(0);//默认选中核算主体
//                        itemPerson.setAccountEntity(map.get("companyName")+"");
//                        listPerson.add(itemPerson);
//                        adapterPerson.setList(listPerson);
//                        adapterPerson.notifyDataSetChanged();
//                    }else{
//                        Toast.makeText(PrivateInternationalApplyCreateActivity.this,"该人员没有核算主体",Toast.LENGTH_SHORT).show();
//                    }
                }
            });
            //选择当前用户（自己）时的事件
            popupView.findViewById(R.id.item_current_user).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageView icon_check = (ImageView) popupView.findViewById(R.id.icon_check);
                    icon_check.setImageResource(R.drawable.btn_singleselection_pressed);
                    for(int i=0;i<popAdapter.getCount();i++) {
                        datalist.get(i).put("isCheck","false");
                    }
                    popAdapter.notifyDataSetChanged();
                    selectPerson = currentUser;
                }
            });
            popuptitle.setText("选择出行人员");
            selectTripType = null;
        }else if(type == 3) {
            //核算主体
            popupView.findViewById(R.id.select_person).setVisibility(View.GONE);
            popupView.findViewById(R.id.popupwin_list).setVisibility(View.VISIBLE);
            popuptitle.setText("选择核算主体");
            TextView tv_ok = (TextView) popupView.findViewById(R.id.select_person_ok);
            tv_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if(selectAccountEntity == null) {
//                        AlertUtil.show(PrivateInternationalApplyCreateActivity.this, "请选择核算主体", "确定", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                            }
//                        }, "取消", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                            }
//                        },"选择核算主体");
//                        return;
//                    }
                    window.dismiss();
                    PersonItem itemPerson = new PersonItem();
                    itemPerson.setShowItem(true);
                    itemPerson.setShowDel(false);
                    itemPerson.setName(selectPerson.get("employeeName")+"");
                    itemPerson.setEmployeeCode(selectPerson.get("employeeCode")+"");
                    itemPerson.setIdNo(selectPerson.get("idCard")+"");
                    itemPerson.setMobile(selectPerson.get("mobil")+"");
//                    itemPerson.setAccountEntity(selectAccountEntity.get("companyName")+"");
                    //遍历出选择的核算主体
                    for(int n = 0 ; n < datalist.size() ; n++){
                        Map<String,Object> pItem = datalist.get(n);
                        String pItemCheck = null == pItem.get("isCheck")
                                ? "false" : pItem.get("isCheck").toString();
                        if(pItemCheck.equals("true")){
                            itemPerson.setAccountEntity(pItem.get("companyName") + "");
                            break;
                        }
                    }
                    for (Map<String,Object> m6 : datalist) {
                        Log.i("--->","核算主体：" + m6);

                    }
                    itemPerson.setAccountEntityList(datalist);//设置核算主体数据
                    List<Map<String,Object>> documentInfoList = (List<Map<String, Object>>) selectPerson.get("documentInfo");
                    itemPerson.setCredentialsInfo(documentInfoList);
                    listPerson.add(itemPerson);
                    adapterPerson.notifyDataSetChanged();
                }
            });
        }else if(type == 6){
            //选择证件类型
            popupView.findViewById(R.id.select_person).setVisibility(View.GONE);
            popupView.findViewById(R.id.popupwin_list).setVisibility(View.VISIBLE);
            TextView tv_ok = (TextView) popupView.findViewById(R.id.select_person_ok);
            popuptitle.setText("选择证件类型");
            tv_ok.setText("下一步");
            tv_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map<String,Object> selectMap = popAdapter.getSelectMap();
                    if(null == selectMap){
                        AlertUtil.show(PrivateInternationalApplyCreateActivity.this, "请选证件类型", "确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        }, "取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        },"选择证件类型");
                        return;
                    }else{
                        window.dismiss();
                        List<Map<String,Object>> companylist = (List<Map<String,Object>>) selectPerson.get("company");
                        //判断当前选中的人是否有多个核算主体
                        if(companylist.size() > 1){
                            selectPerson.put("documentInfo",datalist);
                            showPopupWindows(3,companylist);
                        }else if(companylist.size() == 1){
                            PersonItem itemPerson = new PersonItem();
                            itemPerson.setShowItem(true);
                            itemPerson.setShowDel(false);
                            itemPerson.setName(selectPerson.get("employeeName")+"");
                            itemPerson.setEmployeeCode(selectPerson.get("employeeCode")+"");
                            itemPerson.setIdNo(selectPerson.get("idCard")+"");
                            itemPerson.setMobile(selectPerson.get("mobil")+"");
                            Map<String,Object> map = companylist.get(0);//默认选中核算主体
                            itemPerson.setAccountEntity(map.get("companyName")+"");

//                            List<Map<String,Object>> documentInfoList = (List<Map<String, Object>>) selectPerson.get("documentInfo");
                            itemPerson.setCredentialsInfo(datalist);

                            listPerson.add(itemPerson);
                            adapterPerson.setList(listPerson);
                            adapterPerson.notifyDataSetChanged();
                        }else{
                            ToastUtil.toastNeedData(PrivateInternationalApplyCreateActivity.this,"该人员没有核算主体");
                        }
                    }
                }
            });
            //选择当前用户（自己）时的事件
            popupView.findViewById(R.id.item_current_user).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageView icon_check = (ImageView) popupView.findViewById(R.id.icon_check);
                    icon_check.setImageResource(R.drawable.btn_singleselection_pressed);
                    for(int i=0;i<popAdapter.getCount();i++) {
                        datalist.get(i).put("isCheck","false");
                    }
                    popAdapter.notifyDataSetChanged();
                    selectPerson = currentUser;
                }
            });
            selectTripType = null;
        }

        popupView.findViewById(R.id.select_person_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });

        //创建PopupWindow对象，指定宽度和高度
        int pop_width = (int)(getDeviceWidth()*0.9);
        window = new PopupWindow(popupView, pop_width, (int)(getDeviceWidth()*1.1));
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
        //以下拉的方式显示，并且可以设置显示的位置
//                        window.showAsDropDown(inputSearch, 0, 50);
        window.showAtLocation(ly_main, Gravity.CENTER,0,0);
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
        WindowManager.LayoutParams lp = PrivateInternationalApplyCreateActivity.this.getWindow().getAttributes();
        lp.alpha = 0.3f;//设置阴影透明度
        PrivateInternationalApplyCreateActivity.this.getWindow().setAttributes(lp);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            /**当关闭弹窗给筛选条件赋值*/
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = PrivateInternationalApplyCreateActivity.this.getWindow().getAttributes();
                lp.alpha = 1f;
                PrivateInternationalApplyCreateActivity.this.getWindow().setAttributes(lp);
            }
        });
    }

    /**
     * 获取已授权人员列表
     */
    private void getPersonList(final boolean firstIn) {
        User user = AppUtils.getUserInfo(PrivateInternationalApplyCreateActivity.this);
        String employeeCode = user.getEmployeeCode();
//        showLoading("正在获取数据",true);
        Map<String,Object> params = new HashMap<>();
        params.put("ciphertext","test");
        params.put("employeeCode",employeeCode);
        params.put("loginType",URL.LOGIN_TYPE);
        Retrofit retrofit = new RequestUtil().getRequestClient(URL.ZHONGCHE_93);
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        Call<Map<String,Object>> call = request.getPersonList(params);
        new RequestUtil().requestData(call,this,3,"正在获取数据",true,PrivateInternationalApplyCreateActivity.this);
    }

    /**
     * 保存预订申请单
     */
    private void saveOrderApply() {
        if(!checkForm()) {//验证表单
            canClick = true;
            return;
        }
//        String fromTxt = tv_city_from.getText().toString().trim();
//        String toTxt = tv_city_to.getText().toString().trim();
        //显示加载状态
//        showLoading("正在提交数据",false);
        Map<String,Object> params = new HashMap<>();
        params.put("ciphertext","test");
        //0-国内，1-国际
        EditText suggestEt = (EditText)findViewById(R.id.outer_trip_suggest);
        String suggestion = suggestEt.getText().toString().trim();
        params.put("suggestion",suggestion);
        //添加多国际行程
        List<Map<String,Object>> outTripList = new ArrayList<>();
        for(int i = 0 ;i < outGoAdapter.getList().size(); i++){
            OuterTripItem outItem = outGoAdapter.getItem(i);
            Map<String,Object> mm = new HashMap<>();
            mm.put("fromDate",outItem.getStartTime());
            mm.put("startCity",outItem.getFromCityName());
            mm.put("toCity",outItem.getToCityName());
            outTripList.add(mm);
        }
        params.put("createInterTripList",outTripList);
//        params.put("destination",toTxt);
//        params.put("origin",fromTxt);
        params.put("outreason","因私国际");
        User user = AppUtils.getUserInfo(PrivateInternationalApplyCreateActivity.this);
        String employeeCode = user.getEmployeeCode();
        String employeeName = user.getEmployeeName();
        params.put("employeeCode",employeeCode);
        params.put("employeeName",employeeName);
        params.put("loginType",URL.LOGIN_TYPE);
        params.put("type",1);
        //暂时传空字符串
        params.put("destinationCode","");//到达城市三字码
        params.put("originCode","");//出发城市三字码

        //封装出行人员列表数据
        List<Map<String,Object>> togetherManList = new ArrayList<>();
        for (int i=0;i<listPerson.size();i++){
            Map<String,Object> item = new HashMap<>();
            item.put("companyName",listPerson.get(i).getAccountEntity());
            item.put("employeeCode",listPerson.get(i).getEmployeeCode());
            item.put("employeeName",listPerson.get(i).getName());
            item.put("telephone",listPerson.get(i).getMobile());
            item.put("documentInfo",listPerson.get(i).getIdcard());
            item.put("documentId","8");
            item.put("documentTypeName","因私普通护照");
            item.put("companyName","因私国际");
            togetherManList.add(item);
//            Log.i("--->","出行人员："+ item);
        }

        params.put("createTogetherManList",togetherManList);
        params.put("internationalPrivate",1);

        Retrofit retrofit = new RequestUtil().getRequestClient(URL.ZHONGCHE_93);
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        Call<Map<String,Object>> call = request.saveOrderApply(params);
        new RequestUtil().requestData(call,this,1,"正在提交数据...",false,PrivateInternationalApplyCreateActivity.this);
    }

    /**
     * 表单验证
     */
    private boolean checkForm() {
        boolean result = true;
        String msg = "";
        List<OuterTripItem> outList = outGoAdapter.getList();
        if(outList.size() == 0){
            msg = "请添加行程";
            result = false;
        }else if(outList.size() > 0){
            for(int i = 0 ;i < outList.size() ;i++){
                OuterTripItem tripItem = outList.get(i);
                String fromCity = tripItem.getFromCityName();
                String toCity = tripItem.getToCityName();
                String startTime = tripItem.getStartTime();
                if(fromCity.equals("")){
                    msg = "请填写行程" + (i + 1) + "的出发城市";
                    result = false;
                    break;
                }else if(toCity.equals("")){
                    msg = "请填写行程" + (i + 1) + "的到达城市";
                    result = false;
                    break;
                }else if(startTime.equals("")){
                    msg = "请填选择行程" + (i + 1) + "的出发时间";
                    result = false;
                    break;
                }
            }
            for (int i=0;i<outList.size();i++){
                OuterTripItem outerTripItemOne = outList.get(i);
                for (int j=i+1;j<outList.size();j++){
                    OuterTripItem outerTripItemTwo = outList.get(j);
                    if(outerTripItemOne.getFromCityName().equals(outerTripItemTwo.getFromCityName())
                            && outerTripItemOne.getToCityName().equals(outerTripItemTwo.getToCityName())
                            && outerTripItemOne.getStartTime().equals(outerTripItemTwo.getStartTime())){
                        msg = "不能添加出发地、目的地、出发时间同时相同的行程";
                        result = false;
                    }
                }
            }
        }
        if(listPerson.size()==0) {
            msg = "请添加出行人员";
            result = false;
        }else{
            //全选遍历出行人ListView的选项，每个选项就相当于布局配置文件中的RelativeLayout
            for(int i = 0; i < listViewPerson.getChildCount(); i++){
                View view = listViewPerson.getChildAt(i);
                EditText et_idNo = (EditText) view.findViewById(R.id.item_person_idNo);
                EditText et_mobile = (EditText) view.findViewById(R.id.item_person_mobile);
                String idCard = et_idNo.getText().toString().trim();
                String mobile = et_mobile.getText().toString().trim();
                if(StringUtil.isEmpty(idCard) || StringUtil.isEmpty(mobile)){
                    msg = "请完善出行人员信息";
                    result = false;
                    break;
                }else{
                    PersonItem itemPerson = listPerson.get(i);
                    itemPerson.setIdcard(idCard);
                    itemPerson.setMobile(mobile);
                }
            }
        }
        EditText suggestEt = (EditText)findViewById(R.id.outer_trip_suggest);
        String suggestion = suggestEt.getText().toString().trim();
        if(suggestion.equals("")){
            msg = "请填出行建议";
            result = false;
        }
        if(!result) {
            AlertUtil.show2(PrivateInternationalApplyCreateActivity.this, msg, "确定", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }
        return result;
    }

    @Override
    public void requestSuccess(Object map, int type) {
        Map<String,Object> result = (Map<String, Object>) map;
        switch (type){
            case 1:
                //提交国内机票预定申请单成功后的返回事件
                if(null != result){
                    String code = null == result.get("code") ? "" : result.get("code").toString();
                    String codeMsg = null == result.get("codeMsg") ? "" : result.get("codeMsg").toString();
//                    String data = (String) result.get("data");

                    if(code.equals("00000")) {
                        ToastUtil.toastHandleSuccess(PrivateInternationalApplyCreateActivity.this);
                        initViewData();//清空数据
                        Intent intent = new Intent();
                        intent.putExtra("updateType",1);
                        setResult(RESULT_OK,intent);
                        finish();
//                        ((MainActivity)getActivity()).changeViewPagerIndex(0);//跳转回首页
                    }
                    else {
                        ToastUtil.toastHandleError(PrivateInternationalApplyCreateActivity.this);
                    }
                }else{
                    //请求失败
                    ToastUtil.toastHandleError(PrivateInternationalApplyCreateActivity.this);
                }
//                stopLoading();
                canClick = true;
                break;
            case 2:
                if(null != result){
//                    String code = (String) result.get("code");
//                    String codeMsg = (String) result.get("codeMsg");
                    String code = null == result.get("code") ? "" : result.get("code").toString();
                    String codeMsg = null == result.get("codeMsg") ? "" : result.get("codeMsg").toString();
                    if(code.equals("00000")) {
                        List<Map<String,Object>> datalist = (ArrayList)result.get("data");
                        showPopupWindows(2,datalist);
                    }
                    else {
                        ToastUtil.toastError(PrivateInternationalApplyCreateActivity.this);
                    }
                }else{
                    //请求失败
                    ToastUtil.toastError(PrivateInternationalApplyCreateActivity.this);
                }
                break;
            case 3:
                //获取已授权人员列表数据成功后的回调事件
//                stopLoading();
                if(null != result){
                    String code = null == result.get("code")?"": result.get("code").toString();
                    String codeMsg = null == result.get("codeMsg")?"": result.get("codeMsg").toString();

                    if(code.equals("00000")) {
                        //将自己的数据剔除出来（第一个）
                        personInfoList = (List<Map<String,Object>>)result.get("data");
                        currentUser = personInfoList.get(0);
                        selectPerson = currentUser;
                        personInfoList.remove(0);
                        if(null != personInfoList && personInfoList.size() > 0){
//                        Log.i("--->","personInfoList:" + personInfoList);
                            showPopupWindows(1,personInfoList);
                        }else{
                            /**
                             * 判断当前用户的证件类型个数
                             * 1、若当前用户的证件个数为1,
                             *    a、若其核算主体个数只有1个，则直接新增该人员；
                             *    b、若其核算主体个数只有多个，则弹出选择核算主体的界面；
                             *    c、若其没有核算主0体，则不能订票；
                             *  2、若当前证件个数为多个，则进入选择证件类型界面；
                             *  3、若当前用户没有证件个数，则提示不能订票
                             */
                            /*List<Map<String,Object>> companylist = (List<Map<String,Object>>) selectPerson.get("company");
                            List<Map<String,Object>> documentInfoList = (List<Map<String,Object>>) selectPerson.get("documentInfo");

                            if(documentInfoList.size() == 1){
                                documentInfoList.get(0).put("isCheck","true");
                                selectPerson.put("documentInfo",documentInfoList);
                                if(companylist.size() > 1){
                                    //进入选择核算主体信息界面

                                    showPopupWindows(3,companylist);
                                }else if(companylist.size() == 1){
                                    PersonItem itemPerson = new PersonItem();
                                    itemPerson.setShowItem(true);
                                    itemPerson.setShowDel(false);
                                    itemPerson.setName(selectPerson.get("employeeName")+"");
                                    itemPerson.setEmployeeCode(selectPerson.get("employeeCode")+"");
                                    itemPerson.setIdNo(selectPerson.get("idCard")+"");
                                    itemPerson.setMobile(selectPerson.get("mobil")+"");
                                    itemPerson.setCredentialsInfo(documentInfoList);
                                    Object cpnObj = selectPerson.get("company");
                                    if(null != cpnObj){
                                        List<Map<String,Object>> company = (List<Map<String, Object>>) selectPerson.get("company");
                                        if(company.size() > 0){
                                            String companyStr = company.get(0).get("companyName").toString();
                                            itemPerson.setAccountEntity(companyStr);
                                        }else{
                                            itemPerson.setAccountEntity("");
                                        }
                                    }else{
                                        itemPerson.setAccountEntity("");
                                    }

                                    List<PersonItem> selfList = new ArrayList<PersonItem>();
                                    selfList.add(itemPerson);
                                    listPerson.add(itemPerson);
                                    adapterPerson.setList(selfList);
                                    adapterPerson.notifyDataSetChanged();
                                }else{
                                    ToastUtil.toastNeedData(PrivateInternationalApplyCreateActivity.this,"该用户没有可用的核算主体！");
                                }
                            }else if(documentInfoList.size() > 1){
                                //进入选择证件类型界面
                                showPopupWindows(6,documentInfoList);
                            }else{
                                ToastUtil.toastNeedData(PrivateInternationalApplyCreateActivity.this,"该用户没有可用的证件！");
                            }*/


                        }
                    }else {
                        ToastUtil.toastError(PrivateInternationalApplyCreateActivity.this);
                    }
                }else{
                    //请求失败
                    ToastUtil.toastError(PrivateInternationalApplyCreateActivity.this);
                }
                break;
        }
    }

    @Override
    public void requestFail(int type) {
        switch (type){
            case 1:
//                stopLoading();
                ToastUtil.toastHandleFail(PrivateInternationalApplyCreateActivity.this);
                canClick = true;
                break;
            case 2:
                ToastUtil.toastFail(PrivateInternationalApplyCreateActivity.this);
                break;
            case 3:
//                stopLoading();
                ToastUtil.toastFail(PrivateInternationalApplyCreateActivity.this);
        }
    }

    @Override
    public void requestCancel(int type) {

    }
    /**
     * 选择出发地
     * @param position
     * @param type 0 国内机票 1 国际机票 2 国内酒店
     */
    @Override
    public void selectDepartCityByType(int position, int type) {

    }

    @Override
    public void selectDesCityByType(int position, int type) {

    }

    @Override
    public void delTripByType(int position, int type) {
        List<OuterTripItem> oldList = outGoAdapter.getList();
        List<OuterTripItem> list = new ArrayList<>();
        for(int i = 0 ;i < oldList.size() ; i++){
            if(i != position){
                list.add(oldList.get(i));
            }
        }
        outGoAdapter.setList(list);
        outGoAdapter.notifyDataSetChanged();
    }

    /**
     * 选择日期
     * @param position
     * @param type 0 国内机票 1 国际机票 2 国内酒店
     */
    @Override
    public void selectDateByType(int position, int type) {
        //选择国际机票时间
        Intent intent = new Intent(PrivateInternationalApplyCreateActivity.this, CalendarActivity.class);
        OuterTripItem outerTripItem = outGoAdapter.getItem(position);
        String checkIn = outerTripItem.getStartTime();
        intent.putExtra("selectType",1);//1选择国际机票日期
        intent.putExtra("days",1);
        intent.putExtra("firstDay",checkIn);
        intent.putExtra("position",position);
        startActivityForResult(intent,2202);
    }

    @Override
    public void selectFlightDateByType(int position, int type, int dayType) {

    }


    @Override
    public void onDeleteClick(int position) {
        //判断当前是否显示了删除按钮，则item点击事件响应删除事件，否则进入详情
        boolean isDelBtnShow = adapterPerson.isShowDel();
        if(isDelBtnShow){
//                    List<PersonItem> personList = adapterPerson.getList();
            PersonItem nowItem = adapterPerson.getItem(position);
//                    List<PersonItem> newPersonList = new ArrayList<PersonItem>();
            for(int a = 0 ; a < listPerson.size();a++){
                String empCode = listPerson.get(a).getEmployeeCode();
                if(empCode.equals(nowItem.getEmployeeCode())){
                    listPerson.remove(a);
                    break;
                }
            }
            adapterPerson.setList(listPerson);
            adapterPerson.notifyDataSetChanged();
        }
    }
}
