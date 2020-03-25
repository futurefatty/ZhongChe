package com.neusoft.zcapplication.approval;

import android.content.Intent;
import android.graphics.Color;
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

import com.crcc.commonlib.utils.JSONUtils;
import com.neusoft.zcapplication.Bean.FlightItem;
import com.neusoft.zcapplication.Bean.HotelTripItem;
import com.neusoft.zcapplication.Bean.OuterTripItem;
import com.neusoft.zcapplication.Bean.PersonItem;
import com.neusoft.zcapplication.Bean.User;
import com.neusoft.zcapplication.Calendar.CalendarActivity;
import com.neusoft.zcapplication.city.AirportActivity;
import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.base.BaseActivity;
import com.neusoft.zcapplication.http.NetWorkRequest;
import com.neusoft.zcapplication.http.RequestCallback;
import com.neusoft.zcapplication.http.RequestUtil;
import com.neusoft.zcapplication.http.URL;
import com.neusoft.zcapplication.tools.AlertUtil;
import com.neusoft.zcapplication.tools.AppUtils;
import com.neusoft.zcapplication.tools.DateUtils;
import com.neusoft.zcapplication.tools.ToastUtil;
import com.neusoft.zcapplication.widget.DefinedListView;
import com.neusoft.zcapplication.widget.PopupWinListAdapter;
import com.neusoft.zcapplication.widget.TimeSelector;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;


/**
 * 预订申请
 */

public class NewApprovalActivity extends BaseActivity implements View.OnClickListener,FlightListAdapter.SelectCity,
        FlightListAdapter.ShowPopupWin,RequestCallback ,TripItemClickListener {

    private FlightListAdapter adapter;//行程适配器
//    private List<FlightItem> list;
    private PersonListAdapter adapterPerson;//同行人适配器
    private List<PersonItem> listPerson;
    private int type;//0-国内，1-国际
    private boolean showDelTrip = false;//是否显示删除航程左边的删除图标
    private boolean showDelPerson = false;//是否显示删除同行人左边的删除图标
//    private boolean hasAbroadCity,hasAbroadCity1;//true 选择了国际城市
//    private String fromCityCode,fromCityName="广州",toCityCode,toCityName="纽约";
//    private TextView time_start,time_end;//tv_city_from,tv_city_to,
//    private EditText tv_city_from,tv_city_to;//国际出发、到达城市
    private TimeSelector timeSelector;
    private int timeTypeIndex;
    private int city_position;
    private PopupWindow window = new PopupWindow();
    private View popupView;
    private Map<String,Object> selectPerson,currentUser;//选择的出行人，当前用户
    private Map<String,Object> selectAccountEntity;//选择的核算主体
    private Map<String,Object> selectTripType;//选择的出行方式
    private List<Map<String,Object>> personInfoList;//同行人信息
    private boolean canClick = true;//提交按钮开关，防止重复提交订单
    private HotelTripListAdapter hotelTripListAdapter;//酒店行程列表适配器
    private int hotelTripIndex;//酒店行程的下标
    private OutTripListAdapter outGoAdapter;//,outBackAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_approval);
        initView();
        initViewData();//初始换数据
        getPersonList(true);
    }

    private void initView(){
        findViewById(R.id.btn_back).setOnClickListener(this);

        findViewById(R.id.btn_add_trip).setOnClickListener(this);
        findViewById(R.id.btn_del_trip).setOnClickListener(this);
        findViewById(R.id.btn_inner).setOnClickListener(this);
        findViewById(R.id.btn_outer).setOnClickListener(this);
        findViewById(R.id.tab_hotel_apply_bill).setOnClickListener(this);//国内酒店
//        findViewById(R.id.btn_exchange).setOnClickListener(this);
        findViewById(R.id.btn_add_person).setOnClickListener(this);
        findViewById(R.id.btn_del_person).setOnClickListener(this);
        findViewById(R.id.btn_save_order).setOnClickListener(this);

        DefinedListView listView = (DefinedListView)findViewById(R.id.flight_list);

        List<FlightItem> list = new ArrayList<>();
        adapter = new FlightListAdapter(NewApprovalActivity.this,list);
        adapter.setSelectCity(this);
        adapter.setShowPopupWin(this);
        listView.setAdapter(adapter);

        DefinedListView listViewPerson = (DefinedListView)findViewById(R.id.person_list);
        listPerson = new ArrayList<>();
//        listPerson.add(itemPerson);
        adapterPerson = new PersonListAdapter(NewApprovalActivity.this,listPerson);
        listViewPerson.setAdapter(adapterPerson);
        listViewPerson.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PersonItem item = listPerson.get(position);
                item.setAccountEntityList((ArrayList)selectPerson.get("company"));
                Intent intent = new Intent(NewApprovalActivity.this,EditChuXinRenActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("item",item);
//                intent.putExtras(bundle);
                intent.putExtra("item",(Serializable)item);
                intent.putExtra("position",position);
                startActivityForResult(intent,101);
            }
        });
        //设置顶部状态栏样式
        AppUtils.setStateBar(NewApprovalActivity.this,findViewById(R.id.frg_status_bar));
        //酒店行程
        HotelTripItem hotelTripItem = generalHotelTrip();
        List<HotelTripItem> hotelTripList = new ArrayList<>();
        hotelTripList.add(hotelTripItem);
        hotelTripListAdapter = new HotelTripListAdapter(NewApprovalActivity.this,hotelTripList);
        DefinedListView hotelTripListView = (DefinedListView)findViewById(R.id.hotel_trip_list_view);//酒店行程
        hotelTripListView.setAdapter(hotelTripListAdapter);
        hotelTripListAdapter.setItemCellClick(this);

        int tabIndex = getIntent().getIntExtra("navIndex",0);
        if(tabIndex == 0){
//            findViewById(R.id.btn_inner).performClick();
        }else if(tabIndex == 1){
            findViewById(R.id.btn_outer).performClick();
            type = 1;
        }
        //
        DefinedListView outGoTripListView = (DefinedListView)findViewById(R.id.out_trip_go_list_view);//国际返去程
        List<OuterTripItem> outGoList = new ArrayList<>();
        outGoList.add(generalOuterTrip());
        outGoAdapter = new OutTripListAdapter(NewApprovalActivity.this,outGoList);
        outGoAdapter.setOutTripListAdapter(outGoAdapter);
        outGoTripListView.setAdapter(outGoAdapter);

    }

    private void initViewData(){

        FlightItem item = generalInnerTrip();
        List<FlightItem> list = new ArrayList<>();
//        list.clear();
        list.add(item);
//        innitList.add(item);
        adapter.setList(list);
        adapter.notifyDataSetChanged();
        adapter.setSelectCity(this);
        adapter.setShowPopupWin(this);

        EditText editText = (EditText)findViewById(R.id.et_out_reason);
        editText.setText("");
        //人员列表
        listPerson.clear();
        adapterPerson.setList(listPerson);
        adapterPerson.notifyDataSetChanged();
    }

    /**
     * 生成国内机票行程数据
     * @return
     */
    private FlightItem generalInnerTrip(){
        FlightItem item = new FlightItem();
        item.setShowItem(true);
        item.setShowDel(false);
        item.setFromCity("广州");
        item.setToCity("上海");
        item.setFromCityCode("CAN");
        item.setToCityCode("SHA");
        item.setStartTime(DateUtils.getDate(0));
        item.setBookHotel(0);
        item.setCheckinTime(DateUtils.getDate(0));
        item.setCheckoutTime(DateUtils.getDate(1));
        item.setTripType(2);
        item.setTripMode("飞机-经济舱");
        return item;
    }

    /**
     * 生成国际行程数据
     * @return
     */
    private OuterTripItem generalOuterTrip(){
        OuterTripItem outerTripItem = new OuterTripItem();
        outerTripItem.setStartTime(DateUtils.getDate(1));
        return outerTripItem;
    }
    /**
     * 生成酒店行程数据
     * @return
     */
    private HotelTripItem generalHotelTrip(){
        HotelTripItem hotelTripItem = new HotelTripItem();
        hotelTripItem.setCheckInDate(DateUtils.getDate(1));
        hotelTripItem.setCheckOutDate(DateUtils.getDate(2));
        return hotelTripItem;
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_inner: //点击国内,显示航程
                changeTab(0);
                break;
            case R.id.btn_outer: //点击国际,隐藏航程
                changeTab(1);
                break;

            case R.id.tab_hotel_apply_bill:
                //酒店tab
                changeTab(2);
                break;
            case R.id.btn_add_trip:
                //添加行程
                if(type == 0){
                    List<FlightItem> list = adapter.getList();
                    if (list != null && 10 > list.size()){
                        FlightItem item = generalInnerTrip();
                        list.add(item);
                        adapter.setList(list);
                        adapter.notifyDataSetChanged();
                    }else {
                        AlertUtil.show2(NewApprovalActivity.this, "最多只能有10个行程", "确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        });
                    }
                }else if(type == 1){
                    //添加国际返程数据
                    List<OuterTripItem> outGoList = outGoAdapter.getList();
                    outGoList.add(generalOuterTrip());
                    outGoAdapter.setList(outGoList);
                    outGoAdapter.notifyDataSetChanged();

                }else if(type == 2){
                    //添加酒店行程数据
                    List<HotelTripItem> hotelList = hotelTripListAdapter.getList();
                    HotelTripItem hotelItem = generalHotelTrip();
                    hotelList.add(hotelItem);

                    hotelTripListAdapter.setList(hotelList);
                    hotelTripListAdapter.notifyDataSetChanged();
                }
                break;
            case R.id.btn_del_trip:
                //删除行程
                if(type  == 0){
                    if(!showDelTrip) {
                        showDelTrip = true;
                        adapter.setShowDelBtn(true);
                    }else {
                        adapter.setShowDelBtn(false);
                        showDelTrip = false;
                    }
                    adapter.notifyDataSetChanged();
                }else if(type == 1){
                    outGoAdapter.toggleShowDelBtn();
                    outGoAdapter.notifyDataSetInvalidated();
                }else{
                    hotelTripListAdapter.toggleShowDelBtn();
                    hotelTripListAdapter.notifyDataSetInvalidated();
                }
                break;
            case R.id.time_end:
                timeTypeIndex = 1;
                timeSelector.show();
                break;
            case R.id.time_start:
                timeTypeIndex = 0;
                timeSelector.show();
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
                if(!showDelPerson) {
                    for(int i=0;i<adapterPerson.getCount();i++) {
                        listPerson.get(i).setShowDel(true);
                    }
                    showDelPerson = true;
                }
                else {
                    for(int i=0;i<adapterPerson.getCount();i++) {
                        listPerson.get(i).setShowDel(false);
                    }
                    showDelPerson = false;
                }

                adapterPerson.setList(listPerson);
                adapterPerson.notifyDataSetChanged();
                break;
            case R.id.btn_save_order:
                    if (canClick){
                        if(type == 2){
        //                    ToastUtil.toastNeedData(ApprovalActivity.this,"功能稍候开放~");
                            if(checkHotelData()){
                                canClick = false;
                                saveHotelOrderApply();
                            }
                        }else{
                            canClick = false;
                            saveOrderApply();
                        }
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
            if(requestCode == 3001 ){
                //选择了出发城市
//                fromCityCode= data.getStringExtra("code");
//                fromCityName = data.getStringExtra("name");
//                tv_city_from.setText(fromCityName);
//                int type = data.getIntExtra("type",0);//0 国内城市  1  国际城市

            }else if(requestCode == 3002 ){
                //选择了到达城市
//                toCityCode = data.getStringExtra("code");
//                toCityName = data.getStringExtra("name");
//                tv_city_to.setText(toCityName);
//                int type = data.getIntExtra("type",0);//0 国内城市  1  国际城市
            }else if(requestCode == 3003 ){
                //选择了到达城市
                String cityCode = data.getStringExtra("code");
                String cityName = data.getStringExtra("name");
//                tv_city_to.setText(toCityName);
                int type = data.getIntExtra("type",0);//0 国内城市  1  国际城市
                List<FlightItem> list = adapter.getList();
                list.get(city_position).setFromCity(cityName);
                list.get(city_position).setFromCityCode(cityCode);
                adapter.notifyDataSetChanged();
            }else if(requestCode == 3004 ){
                //选择了到达城市
                String cityCode = data.getStringExtra("code");
                String cityName = data.getStringExtra("name");
                List<FlightItem> list = adapter.getList();
                list.get(city_position).setToCityCode(cityCode);
//                tv_city_to.setText(toCityName);
                int type = data.getIntExtra("type",0);//0 国内城市  1  国际城市
                list.get(city_position).setToCity(cityName);
                adapter.notifyDataSetChanged();
            }else if(requestCode == 101 ){
                Bundle bundle = data.getExtras();
                PersonItem personItem = (PersonItem) bundle.getSerializable("item");
                int position = data.getIntExtra("position",0);
                listPerson.set(position,personItem);
                adapterPerson.notifyDataSetChanged();
            }else if(requestCode == 2102){

                //选择了酒店行程的日期
                String firstDay = data.getStringExtra("firstDay");
                String secondDay = data.getStringExtra("secondDay");
                List<HotelTripItem> hotelList = hotelTripListAdapter.getList();
                for(int i = 0 ; i < hotelList.size(); i++){
                    if(i == hotelTripIndex){
                        hotelList.get(i).setCheckInDate(firstDay);
                        hotelList.get(i).setCheckOutDate(secondDay);
                    }
                }
                hotelTripListAdapter.setList(hotelList);
                hotelTripListAdapter.notifyDataSetChanged();
            }else if(requestCode == 2103){
                String cityCode = data.getStringExtra("code");
                String cityName = data.getStringExtra("name");
                List<HotelTripItem> hotelList = hotelTripListAdapter.getList();
                for(int i = 0 ; i < hotelList.size(); i++){
                    if(i == hotelTripIndex){
                        hotelList.get(i).setCityName(cityName);
                        hotelList.get(i).setCityCode(cityCode);
                    }
                }
                hotelTripListAdapter.setList(hotelList);
                hotelTripListAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * @param position
     * @param type 0-出发城市, 1-到达城市
     */
    @Override
    public void selectFromcity(int position, int type) {
        city_position = position;
        Intent cityFrom = new Intent(NewApprovalActivity.this, AirportActivity.class);
        String tripType = adapter.getItem(position).getTripMode();
        if (tripType!=null&&tripType.contains("飞机")){
            cityFrom.putExtra("dataType",0);
        }
        else {
            cityFrom.putExtra("dataType",1);
        }
        cityFrom.putExtra("inCity","yes");
        if(type == 0) {
            startActivityForResult(cityFrom,3003);
        }
        else {
            startActivityForResult(cityFrom,3004);
        }
    }

    /**
     * 选择出行方式
     * @param position
     */
    @Override
    public void selectTripMode(int position) {
        city_position = position;
        List<FlightItem> list = adapter.getList();
        if (selectTripType!=null&&list.get(city_position).getTripMode()!=null){
            LinearLayout ly_main = (LinearLayout) findViewById(R.id.ly_main);
            window.update();
            popOutShadow(window);
            window.showAtLocation(ly_main, Gravity.CENTER,0,0);
        }
        else {
            getTripType();
        }
    }

    /**
     * 显示弹窗
     * @param type
     */
    private void showPopupWindows(final int type, final List<Map<String,Object>> datalist) {
        LinearLayout ly_main = (LinearLayout) findViewById(R.id.ly_main);
        //构建一个popupwindow的布局
        popupView = NewApprovalActivity.this.getLayoutInflater().inflate(R.layout.popupwindow_select_person, null);
        final PopupWinListAdapter popAdapter = new PopupWinListAdapter(NewApprovalActivity.this,datalist,type);
        for(int i=0;i<popAdapter.getCount();i++) {
            datalist.get(i).put("isCheck","false");
        }
        if (type == 2){
            datalist.get(1).put("isCheck","true");
            selectTripType = datalist.get(1);
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
                    selectAccountEntity = datalist.get(position);
                }else if(type  == 5){
                    //选择证件类型
                }
            }
        });

        TextView popuptitle = (TextView) popupView.findViewById(R.id.popup_title);
        if(type == 1) {//出行人员
            TextView current_username = (TextView) popupView.findViewById(R.id.item_current_username);
            current_username.setText(currentUser.get("employeeName")+"");
//            popupView.findViewById(R.id.popupwin_list).setVisibility(View.GONE);
            popupView.findViewById(R.id.select_person).setVisibility(View.VISIBLE);
            TextView tv_ok = (TextView) popupView.findViewById(R.id.select_person_ok);
            tv_ok.setText("下一步");
            tv_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i=0;i<listPerson.size();i++){
                        PersonItem item = listPerson.get(i);
                        if (null!=item.getEmployeeCode()&&item.getEmployeeCode().equals(selectPerson.get("employeeCode"))){
                            AlertUtil.show2(NewApprovalActivity.this, "您已添加过该人员", "确定", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            });
                            return;
                        }
                    }
                    window.dismiss();
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
                    List<Map<String,Object>> companyList = (List<Map<String,Object>>) selectPerson.get("company");
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
                            selectPerson.put("documentInfo",documentInfoList);
                            //设置选中的证件类型，并显示核算主体选择界面
                            showPopupWindows(3,documentInfoList);
                        }
                    }else if(documentInfoList.size() > 1){
                        //显示选择证件类型界面
                        showPopupWindows(6,documentInfoList);
                    }else{
                        ToastUtil.toastNeedData(NewApprovalActivity.this,"该人员没有可用证件信息！");
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
            popuptitle.setText("选择出行人员");
            selectTripType = null;
        }else if(type == 2) {//出行方式
            popupView.findViewById(R.id.select_person).setVisibility(View.GONE);
            popupView.findViewById(R.id.popupwin_list).setVisibility(View.VISIBLE);
            popuptitle.setText("选择出行方式");
            TextView tv_ok = (TextView) popupView.findViewById(R.id.select_person_ok);
            tv_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    window.dismiss();
                    String tripTypeName = selectTripType.get("name")+"";
                    List<FlightItem> list = adapter.getList();
                    String tripMode = list.get(city_position).getTripMode();
                    if ((!tripTypeName.contains("飞机")&&tripMode.contains("飞机")) ||
                            (tripTypeName.contains("飞机")&&!tripMode.contains("飞机"))){
                        list.get(city_position).setFromCity("");
                        list.get(city_position).setToCity("");
                    }
                    list.get(city_position).setTripMode(selectTripType.get("name")+"");
                    double tripId = (double)selectTripType.get("id");
                    list.get(city_position).setTripType((int)tripId);
                    adapter.notifyDataSetChanged();
                }
            });
        }else if(type == 3) {
            //核算主体
            popupView.findViewById(R.id.select_person).setVisibility(View.GONE);
            popupView.findViewById(R.id.popupwin_list).setVisibility(View.VISIBLE);
            popuptitle.setText("选择核算主体");
            TextView tv_ok = (TextView) popupView.findViewById(R.id.select_person_ok);
            tv_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(selectAccountEntity == null) {
                        AlertUtil.show(NewApprovalActivity.this, "请选择核算主体", "确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        }, "取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        },"选择核算主体");
                        return;
                    }
                    window.dismiss();
                    PersonItem itemPerson = new PersonItem();
                    itemPerson.setShowItem(true);
                    itemPerson.setShowDel(false);
                    itemPerson.setName(selectPerson.get("employeeName")+"");
                    itemPerson.setEmployeeCode(selectPerson.get("employeeCode")+"");
                    itemPerson.setIdNo(selectPerson.get("idCard")+"");
                    itemPerson.setMobile(selectPerson.get("mobil")+"");
                    itemPerson.setAccountEntity(selectAccountEntity.get("companyName")+"");
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
                        AlertUtil.show(NewApprovalActivity.this, "请选证件类型", "确定", new View.OnClickListener() {
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
                            ToastUtil.toastNeedData(NewApprovalActivity.this,"该人员没有核算主体");
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
        WindowManager.LayoutParams lp = NewApprovalActivity.this.getWindow().getAttributes();
        lp.alpha = 0.3f;//设置阴影透明度
        NewApprovalActivity.this.getWindow().setAttributes(lp);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            /**当关闭弹窗给筛选条件赋值*/
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = NewApprovalActivity.this.getWindow().getAttributes();
                lp.alpha = 1f;
                NewApprovalActivity.this.getWindow().setAttributes(lp);
            }
        });
    }

    /**
     * 获取已授权人员列表
     */
    private void getPersonList(final boolean firstIn) {
        User user = new AppUtils().getUserInfo(NewApprovalActivity.this);
        String employeeCode = user.getEmployeeCode();
//        showLoading("正在获取数据",true);
        Map<String,Object> params = new HashMap<>();
        params.put("ciphertext","test");
        params.put("employeeCode",employeeCode);
        params.put("loginType",URL.LOGIN_TYPE);
        Retrofit retrofit = new RequestUtil().getRequestClient(URL.ZHONGCHE_93);
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        Call<Map<String,Object>> call = request.getPersonList(params);
        new RequestUtil().requestData(call,this,3,"正在获取数据",true,NewApprovalActivity.this);
    }

    /**
     * 获取出行方式列表
     */
    private void getTripType() {
        Map<String,Object> params = new HashMap<>();
        params.put("ciphertext","test");
        params.put("loginType",URL.LOGIN_TYPE);
        Retrofit retrofit = new RequestUtil().getRequestClient(URL.ZHONGCHE_93);
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        Call<Map<String,Object>> call = request.getTripType(params);
        new RequestUtil().requestData(call,this,2,mContext);
    }

    /**
     * 保存预订申请单
     */
    private void saveOrderApply() {
        EditText outreason = (EditText) findViewById(R.id.et_out_reason);
        if(!checkForm()) {//验证表单
            canClick = true;
            return;
        }
        Map<String,Object> params = new HashMap<>();
        params.put("ciphertext","test");
        //0-国内，1-国际
        params.put("outreason",outreason.getText()+"");
        User user = AppUtils.getUserInfo(NewApprovalActivity.this);
        String employeeCode = user.getEmployeeCode();
        String employeeName = user.getEmployeeName();
        params.put("employeeCode",employeeCode);
        params.put("employeeName",employeeName);
        params.put("loginType",URL.LOGIN_TYPE);
        params.put("type",type);
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
            //遍历出选择的证件id
            List<Map<String,Object>> documentInfoList = listPerson.get(i).getCredentialsInfo();
            for (int j = 0 ; j < documentInfoList.size() ; j++){
                Map<String,Object> mm = documentInfoList.get(j);
                String isCheck = null == mm.get("isCheck") ? "false" :  mm.get("isCheck").toString();
                if(isCheck.equals("true")){
                    double mmId = null == mm.get("DOCUMENTID") ? 0 :(double) mm.get("DOCUMENTID");
                    item.put("documentId",mmId);
                }
            }
            Log.i("--->","出行人员："+ item);
            togetherManList.add(item);
        }
        //封装出差行程列表数据
        List<Map<String,Object>> tripList = new ArrayList<>();
        List<FlightItem> list = adapter.getList();//总的行程数据
        for (int j=0;j<list.size();j++){
            Map<String,Object> item = new HashMap<>();
            int tripType = list.get(j).getTripType();
//            Log.i("--->","出行类型:" + tripType);
            if(tripType == 1 || tripType == 2){
                //出行方式为飞机
                item.put("isHotelCity","1");//1传入的是机场类型的城市数据
            }else{
                item.put("isHotelCity","0");//0传入的是酒店类型的城市数据
            }
            FlightItem flightItem = list.get(j);
//            Log.i("--->","---"+ flightItem.getFromCityCode() + "====" +flightItem.getToCityCode());
            item.put("checkInTime",flightItem.getCheckinTime());
            item.put("checkOutTime",flightItem.getCheckoutTime());
            item.put("isHotel",flightItem.isBookHotel());
            item.put("startCity",flightItem.getFromCity());
            item.put("startDate",flightItem.getStartTime());
            item.put("toCity",flightItem.getToCity());
            item.put("tripType",flightItem.getTripType());
            item.put("startCityCode",flightItem.getFromCityCode());
            item.put("toCityCode",flightItem.getToCityCode());
//            Log.i("--->","行程数据： " + item);
            tripList.add(item);
        }
        params.put("createTogetherManList",togetherManList);
        params.put("createTripList",tripList);

        Retrofit retrofit = new RequestUtil().getRequestClient(URL.ZHONGCHE_93);
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        Log.e("SUNYUAN", JSONUtils.gsonString(params));
        Call<Map<String,Object>> call = request.saveOrderApply(params);
        new RequestUtil().requestData(call,this,1,"正在提交数据...",false,NewApprovalActivity.this);
    }

    /**
     * 创建酒店预定申请单
     */
    private void saveHotelOrderApply() {
        EditText reasonEt = (EditText) findViewById(R.id.et_out_reason);
        String reason = reasonEt.getText().toString().trim();
        if(reason.equals("")){
            AlertUtil.show2(NewApprovalActivity.this, "请填写出差事由", "确定", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            return;
        }
        //显示加载状态
        Map<String,Object> params = new HashMap<>();
        params.put("ciphertext","test");
        params.put("outreason",reason);
        User user = new AppUtils().getUserInfo(NewApprovalActivity.this);
        String employeeCode = user.getEmployeeCode();
        String employeeName = user.getEmployeeName();
        params.put("employeeCode",employeeCode);
        params.put("employeeName",employeeName);
        params.put("loginType",URL.LOGIN_TYPE);
        params.put("type",0);//是否是国际预定申请单，0：不是，1：是
//        //暂时传空字符串
//        params.put("destinationCode","");//到达城市三字码
//        params.put("originCode","");//出发城市三字码

        //封装出行人员列表数据
        List<Map<String,Object>> togetherManList = new ArrayList<>();
        for (int i=0;i<listPerson.size();i++){
            Map<String,Object> item = new HashMap<>();
            item.put("companyName",listPerson.get(i).getAccountEntity());
            item.put("employeeCode",listPerson.get(i).getEmployeeCode());
            item.put("employeeName",listPerson.get(i).getName());
            //遍历出选择的证件id
            List<Map<String,Object>> documentInfoList = listPerson.get(i).getCredentialsInfo();
            for (int j = 0 ; j < documentInfoList.size() ; j++){
                Map<String,Object> mm = documentInfoList.get(j);
                String isCheck = null == mm.get("isCheck") ? "false" :  mm.get("isCheck").toString();
                if(isCheck.equals("true")){
                    double mmId = null == mm.get("DOCUMENTID") ? 0 :(double) mm.get("DOCUMENTID");
                    item.put("documentId",mmId);
                }
            }
            togetherManList.add(item);
        }
        //封装出差行程列表数据
        List<Map<String,Object>> tripList = new ArrayList<>();
        List<HotelTripItem> list = hotelTripListAdapter.getList();//总的行程数据
        for (int j=0;j<list.size();j++){
            Map<String,Object> item = new HashMap<>();
            HotelTripItem hotelItem = list.get(j);
//            Log.i("--->","---"+ flightItem.getFromCityCode() + "====" +flightItem.getToCityCode());
            item.put("checkInTime",hotelItem.getCheckInDate());
            item.put("checkOutTime",hotelItem.getCheckOutDate());
            item.put("isHotel",1);//是否入住酒店,1：是；0：否 ,
            item.put("isHotelCity",0);//城市数据类型，0：酒店城市数据，1：机场城市数据 ,
            item.put("toCity",hotelItem.getCityName());
            item.put("toCityCode",hotelItem.getCityCode());
//            Log.i("--->","酒店行程数据： " + item);
            tripList.add(item);
        }
        params.put("createTogetherManList",togetherManList);
        params.put("createTripList",tripList);

        Retrofit retrofit = new RequestUtil().getRequestClient(URL.ZHONGCHE_93);
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        Call<Map<String,Object>> call = request.saveOrderApplyHotelOnly(params);
        //酒店的预定申请单和机票的预定申请单类型走一样的返回响应
        new RequestUtil().requestData(call,this,1,"正在提交数据...",false,NewApprovalActivity.this);
    }

    /**
     * 检查酒店的预定申请单数据是否填写完整
     * @return
     */
    private boolean checkHotelData(){
        boolean result = true;
        String toastMsg = "" ;//
        List<HotelTripItem> list = hotelTripListAdapter.getList();//总的行程数据

        EditText reasonEt = (EditText) findViewById(R.id.et_out_reason);
        String reason = reasonEt.getText().toString().trim();
        if(list.size() == 0){
            toastMsg += "请添加行程";
            result = false;
        }else if(listPerson.size()==0 ){
            //判断是否有通信人信息
            toastMsg += "请添加出行人员";
            result = false;
        }else if(reason.equals("")){
            toastMsg += "请填写出差事由";
            result = false;
        }else{
            for (int j=0;j<list.size();j++){
                HotelTripItem hotelTripItem = list.get(j);
                String toCity = hotelTripItem.getCityName();
                //因为有默认的入住离店日期，所有只需要判断是否有选择城市信息
                if(null == toCity || toCity.equals("")){
                    toastMsg += "请选择目的地市";
                    result = false;
                    break;
                }
            }
        }

        if(!result) {
            AlertUtil.show2(NewApprovalActivity.this, toastMsg, "确定", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }
        return result;
    }
    /**
     * 表单验证
     */
    private boolean checkForm() {
        boolean result = true;
        String msg = "";
        List<FlightItem> list = adapter.getList();
        if(type == 0) {//国内
            if(list.size()==0) {
                msg = "请添加行程";
                result = false;
            }else {
                for (int a=0;a<list.size();a++){
                    if(list.get(a).getTripMode()==null||"".equals(list.get(a).getTripMode())) {
                        msg = "请选择出行方式";
                        result = false;
                        break;
                    }

                    if(list.get(a).getFromCity()==null||"".equals(list.get(a).getFromCity())) {
                        msg = "请选择出发城市";
                        result = false;
                        break;
                    }

                    if(list.get(a).getToCity()==null||"".equals(list.get(a).getToCity())) {
                        msg = "请选择目的地市";
                        result = false;
                        break;
                    }
                }
            }
            if(listPerson.size()==0) {
                msg = "请添加出行人员";
                result = false;
            }
        }else {//国际
            if(listPerson.size()==0) {
                msg = "请添加出行人员";
                result = false;
            }
        }

        EditText outreason = (EditText) findViewById(R.id.et_out_reason);
        String reason = outreason.getText().toString();
        if(reason.equals("")) {
            msg = "请填写出差事由";
            result = false;
        }

        if(!result) {
            AlertUtil.show2(NewApprovalActivity.this, msg, "确定", new View.OnClickListener() {
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
//                    String codeMsg = (String) result.get("codeMsg");
//                    String data = (String) result.get("data");

                    if(code.equals("00000")) {
                        ToastUtil.toastHandleSuccess(NewApprovalActivity.this);
                        initViewData();//清空数据
                        Intent intent = new Intent();
                        if(this.type == 0){
                            intent.putExtra("updateType",0);
                        }else{
                            intent.putExtra("updateType",1);
                        }
                        setResult(RESULT_OK,intent);
                        finish();
//                        ((MainActivity)getActivity()).changeViewPagerIndex(0);//跳转回首页
                    }
                    else {
                        ToastUtil.toastHandleError(NewApprovalActivity.this);
                    }
                }else{
                    //请求失败
                    ToastUtil.toastHandleError(NewApprovalActivity.this);
                }
//                stopLoading();
                canClick = true;
                break;
            case 2:
                if(null != result){
                    String code = null == result.get("code") ? "" : result.get("code").toString();
//                    String codeMsg = (String) result.get("codeMsg");

                    if(code.equals("00000")) {
                        List<Map<String,Object>> datalist = (ArrayList)result.get("data");
                        showPopupWindows(2,datalist);
                    }
                    else {
                        ToastUtil.toastError(NewApprovalActivity.this);
                    }
                }else{
                    //请求失败
                    ToastUtil.toastError(NewApprovalActivity.this);
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
                             *    c、若其没有核算主体，则不能订票；
                             *  2、若当前证件个数为多个，则进入选择证件类型界面；
                             *  3、若当前用户没有证件个数，则提示不能订票
                             */
                            List<Map<String,Object>> companylist = (List<Map<String,Object>>) selectPerson.get("company");
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
                                    ToastUtil.toastNeedData(NewApprovalActivity.this,"该用户没有可用的核算主体！");
                                }
                            }else if(documentInfoList.size() > 1){
                                //进入选择证件类型界面
                                showPopupWindows(6,documentInfoList);
                            }else{
                                ToastUtil.toastNeedData(NewApprovalActivity.this,"该用户没有可用的证件！");
                            }
                        }
                    }else {
                        ToastUtil.toastError(NewApprovalActivity.this);
                    }
                }else{
                    //请求失败
                    ToastUtil.toastError(NewApprovalActivity.this);
                }
                break;
        }
    }

    @Override
    public void requestFail(int type) {
        switch (type){
            case 1:
//                stopLoading();
                ToastUtil.toastHandleFail(NewApprovalActivity.this);
                canClick = true;
                break;
            case 2:
                ToastUtil.toastFail(NewApprovalActivity.this);
                break;
            case 3:
//                stopLoading();
                ToastUtil.toastFail(NewApprovalActivity.this);
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
    /**
     * 选择目的地城市
     * @param position
     * @param type 0 国内机票 1 国际机票 2 国内酒店
     */
    @Override
    public void selectDesCityByType(int position, int type) {
        if(type == 2){
            hotelTripIndex = position;
            Intent intent = new Intent(NewApprovalActivity.this, AirportActivity.class);
            intent.putExtra("dataType",1);
            intent.putExtra("inCity","yes");
            startActivityForResult(intent,2103);

        }
    }

    @Override
    public void delTripByType(int position, int type) {
        if(type == 2){
            List<HotelTripItem> hotelList = hotelTripListAdapter.getList();
            List<HotelTripItem> list = new ArrayList<>();
            for(int i = 0 ;i < hotelList.size() ; i++){
                if(i != position){
                    list.add(hotelList.get(i));
                }
            }
            hotelTripListAdapter.setList(list);
            hotelTripListAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 选择日期
     * @param position
     * @param type 0 国内机票 1 国际机票 2 国内酒店
     */
    @Override
    public void selectDateByType(int position, int type) {
        if(type == 2){
            hotelTripIndex = position;
            Intent intent = new Intent(NewApprovalActivity.this, CalendarActivity.class);
            HotelTripItem hotelTripItem = hotelTripListAdapter.getItem(position);
            String chechIn = hotelTripItem.getCheckInDate();
            String chechOut = hotelTripItem.getCheckOutDate();
            intent.putExtra("selectType",2);//2选择酒店日期
            intent.putExtra("days",2);//需要选择多
            intent.putExtra("firstDay",chechIn);
            intent.putExtra("secondDay",chechOut);
            startActivityForResult(intent,2102);
        }
    }

    @Override
    public void selectFlightDateByType(int position, int type, int dayType) {

    }

    /**
     * 点击顶部tab的响应事件
     * @param tabIndex
     */
    private void changeTab(int tabIndex){
        TextView inner = (TextView) findViewById(R.id.text_inner);
        TextView outer = (TextView) findViewById(R.id.text_outer);
        TextView hotelTabTv = (TextView) findViewById(R.id.tab_hotel_apply_bill);

        LinearLayout cursor1 = (LinearLayout)findViewById(R.id.act_list_cursor);
        LinearLayout cursor2 = (LinearLayout)findViewById(R.id.act_list_cursor2);
        LinearLayout cursor3 = (LinearLayout)findViewById(R.id.act_list_cursor3);

        LinearLayout flightLayout = (LinearLayout)findViewById(R.id.ly_flight); //航程列表
        LinearLayout addTripLayout = (LinearLayout)findViewById(R.id.ly_btn_flight);//添加行程布局
        LinearLayout tripOuter = (LinearLayout)findViewById(R.id.trip_for_outer);//国际行程
        DefinedListView hotelTripList = (DefinedListView)findViewById(R.id.hotel_trip_list_view);//酒店行程
        type = tabIndex;

        if(tabIndex == 0){
            //显示国内机票申请单
            cursor1.setVisibility(View.VISIBLE);
            cursor2.setVisibility(View.INVISIBLE);
            cursor3.setVisibility(View.INVISIBLE);

            inner.setTextColor(Color.parseColor("#c70019"));
            outer.setTextColor(Color.parseColor("#999999"));
            hotelTabTv.setTextColor(Color.parseColor("#999999"));

            flightLayout.setVisibility(View.VISIBLE);
//            addTripLayout.setVisibility(View.VISIBLE);
            tripOuter.setVisibility(View.GONE);
            hotelTripList.setVisibility(View.GONE);
        }else if(tabIndex == 1){
            //显示国际机票申请单
            //下标
            cursor1.setVisibility(View.INVISIBLE);
            cursor2.setVisibility(View.VISIBLE);
            cursor3.setVisibility(View.INVISIBLE);

            inner.setTextColor(Color.parseColor("#999999"));
            outer.setTextColor(Color.parseColor("#c70019"));
            hotelTabTv.setTextColor(Color.parseColor("#999999"));

            flightLayout.setVisibility(View.GONE);
//            addTripLayout.setVisibility(View.GONE);
            tripOuter.setVisibility(View.VISIBLE);
            hotelTripList.setVisibility(View.GONE);
        }else{
            //显示国内酒店申请单
            cursor1.setVisibility(View.INVISIBLE);
            cursor2.setVisibility(View.INVISIBLE);
            cursor3.setVisibility(View.VISIBLE);

            inner.setTextColor(Color.parseColor("#999999"));
            outer.setTextColor(Color.parseColor("#999999"));
            hotelTabTv.setTextColor(Color.parseColor("#c70019"));

            flightLayout.setVisibility(View.GONE);
//            addTripLayout.setVisibility(View.VISIBLE);
            hotelTripList.setVisibility(View.VISIBLE);
            tripOuter.setVisibility(View.GONE);
        }
    }
}
