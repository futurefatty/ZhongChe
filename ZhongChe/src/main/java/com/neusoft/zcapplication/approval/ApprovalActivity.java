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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;

import com.crcc.commonlib.event.Event;
import com.crcc.commonlib.event.EventBusUtil;
import com.crcc.commonlib.event.EventCode;
import com.neusoft.zcapplication.Bean.FlightItem;
import com.neusoft.zcapplication.Bean.HotelTripItem;
import com.neusoft.zcapplication.Bean.IPDProject;
import com.neusoft.zcapplication.Bean.OuterTripItem;
import com.neusoft.zcapplication.Bean.PersonItem;
import com.neusoft.zcapplication.Bean.User;
import com.neusoft.zcapplication.Calendar.CalendarActivity;
import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.base.BaseActivity;
import com.neusoft.zcapplication.city.AirportActivity;
import com.neusoft.zcapplication.http.NetWorkRequest;
import com.neusoft.zcapplication.http.RequestCallback;
import com.neusoft.zcapplication.http.RequestUtil;
import com.neusoft.zcapplication.http.URL;
import com.neusoft.zcapplication.tools.AlertUtil;
import com.neusoft.zcapplication.tools.AppUtils;
import com.neusoft.zcapplication.tools.LogUtil;
import com.neusoft.zcapplication.tools.StringUtil;
import com.neusoft.zcapplication.tools.ToastUtil;
import com.neusoft.zcapplication.widget.DefinedGridView;
import com.neusoft.zcapplication.widget.DefinedListView;
import com.neusoft.zcapplication.widget.PopupWinListAdapter;

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

public class ApprovalActivity extends BaseActivity implements View.OnClickListener, FlightListAdapter.SelectCity,
        FlightListAdapter.ShowPopupWin, RequestCallback, TripItemClickListener {

    private FlightListAdapter adapter;//行程适配器
    private PersonListAdapter adapterPerson;//同行人适配器
    private DefinedGridView gridView;
    private supplierListAdapter gridAdapter;
    private List<Map<String, Object>> supplierDataList;
    private List<PersonItem> listPerson;
    private PersonItem ProjectPersonItem;//
    private int pageIndex;//0-国内，1-国际 2国内酒店
    private boolean showDelTrip = false;//是否显示删除航程左边的删除图标
    private int city_position;
    private PopupWindow window = new PopupWindow();
    private View popupView;
    private Map<String, Object> selectPerson, currentUser, ProjectLeader;//选择的出行人，当前用户,项目经理，ipd项目选中数据
    private Map<String, Object> selectTripType;//选择的出行方式
    private List<Map<String, Object>> personInfoList;//同行人信息

    private boolean canClick = true;//提交按钮开关，防止重复提交订单
    private HotelTripListAdapter hotelTripListAdapter;//酒店行程列表适配器
    private int hotelTripIndex;//酒店行程的下标
    private OutTripListAdapter outGoAdapter;//国际机票行程适配器

    public static final String INNTEAR_FLIGHT_APPLY_ORDER = "INNTEAR_FLIGHT_APPLY_ORDER";
    public Switch is_ipd_switch;//是否是ipd
    public LinearLayout is_ipd_ll;//是否是ipd显示内容
    public TextView ipd_project_name;//项目名称
    public TextView ipd_project_mark;//项目令号
    public TextView ipd_task_name;//任务名称
    public TextView ipd_task_coding;//任务编码
    public TextView ipd_manager_number;//项目经理工号
    public TextView ipd_manager_name;//项目经理名称
    public TextView ipd_approval_personnel;//出差任务审批人
    public TextView ipd_approval_personnel_number;//出差任务审批人工号
    public List<Map<String, Object>> IPDProjectList;//
    public Map<String, Object> ipdProject;
    public String isIpd = "0";
    /**
     * 国际机票行程方案
     */
    public static final String INTERNATION_FLIGHT_JURNEY_SCHEME = "INTERNATION_FLIGHT_JURNEY_SCHEME";
    /**
     * 国际机票查询后选择的行程
     */
    private Map<Integer, OuterTripItem> outerTripIdMapper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frg_approval);
        initView();
        initViewData();//初始化数据
        getPersonList(true);
    }

    private void initView() {
        ProjectLeader = new HashMap<>();
        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.btn_add_trip).setOnClickListener(this);
        findViewById(R.id.btn_del_trip).setOnClickListener(this);
        findViewById(R.id.btn_inner).setOnClickListener(this);
        findViewById(R.id.btn_outer).setOnClickListener(this);
        findViewById(R.id.tab_hotel_apply_bill).setOnClickListener(this);//国内酒店
        findViewById(R.id.btn_add_person).setOnClickListener(this);
        findViewById(R.id.btn_del_person).setOnClickListener(this);
        findViewById(R.id.btn_save_order).setOnClickListener(this);
        gridView = (DefinedGridView) findViewById(R.id.supplierList);
        is_ipd_ll = (LinearLayout) this.findViewById(R.id.is_ipd_ll);
        is_ipd_switch = (Switch) this.findViewById(R.id.is_ipd_switch);
        is_ipd_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //Todo
                    ipdProject = new HashMap<>();
                    isIpd = "1";
                    is_ipd_ll.setVisibility(View.VISIBLE);
                } else {
                    //Todo
                    isIpd = "0";
                    is_ipd_ll.setVisibility(View.GONE);
                    ipd_project_name.setText("");//项目名称
                    ipd_project_mark.setText("");//项目令号
                    ipd_task_name.setText("");//任务名称
                    ipd_task_coding.setText("");//任务编码
                    ipd_manager_number.setText("");//项目经理工号
                    ipd_manager_name.setText("");//项目经理名称
                    ipd_approval_personnel.setText("");//出差任务审批人
                    ipd_approval_personnel_number.setText("");//出差任务审批人工号
                    ipdProject.clear();

                }
            }
        });
        ipd_project_name = (TextView) this.findViewById(R.id.ipd_project_name);
        ipd_project_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IPDProjectList = new ArrayList<Map<String, Object>>();
                showPopupWindows(14, IPDProjectList);
            }
        });
        ipd_project_mark = (TextView) this.findViewById(R.id.ipd_project_mark);
        ipd_task_name = (TextView) this.findViewById(R.id.ipd_task_name);
        ipd_task_coding = (TextView) this.findViewById(R.id.ipd_task_coding);
        ipd_manager_number = (TextView) this.findViewById(R.id.ipd_manager_number);
        ipd_manager_name = (TextView) this.findViewById(R.id.ipd_manager_name);
        ipd_approval_personnel = (TextView) this.findViewById(R.id.ipd_approval_personnel);
        ipd_approval_personnel_number = (TextView) this.findViewById(R.id.ipd_approval_personnel_number);
        //test
        String[] from = {"img", "text"};

        int[] to = {R.id.img, R.id.text};

        //图标下的文字
        String name[] = {"时钟", "信号", "宝箱", "秒钟", "大象", "FF", "记事本", "书签", "印象", "商店", "主题", "迅雷"};
        supplierDataList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < name.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("img", "");
            map.put("text", name[i]);
            supplierDataList.add(map);
        }


        gridAdapter = new supplierListAdapter(this, supplierDataList);

        gridView.setAdapter(gridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                gridAdapter.choiceState(arg2);
            }
        });

        DefinedListView listView = (DefinedListView) findViewById(R.id.flight_list);
        List<FlightItem> list = new ArrayList<>();
        adapter = new FlightListAdapter(ApprovalActivity.this, list);
        adapter.setSelectCity(this);
        adapter.setShowPopupWin(this);
        listView.setAdapter(adapter);

        adapter.setItemCellClick(this);

        DefinedListView listViewPerson = (DefinedListView) findViewById(R.id.person_list);
        listPerson = new ArrayList<>();
        adapterPerson = new PersonListAdapter(ApprovalActivity.this, listPerson);
        listViewPerson.setAdapter(adapterPerson);
        listViewPerson.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //判断当前是否显示了删除按钮，则item点击事件响应删除事件，否则进入详情
                boolean isDelBtnShow = adapterPerson.isShowDel();
                if (isDelBtnShow) {
//                    List<PersonItem> personList = adapterPerson.getList();
                    PersonItem nowItem = adapterPerson.getItem(position);
//                    List<PersonItem> newPersonList = new ArrayList<PersonItem>();

                    for (int a = 0; a < listPerson.size(); a++) {
                        String empCode = listPerson.get(a).getEmployeeCode();
                        if (empCode.equals(nowItem.getEmployeeCode())) {
                            listPerson.remove(a);
                            break;
                        }
                    }
                    adapterPerson.setList(listPerson);
                    adapterPerson.notifyDataSetChanged();
                } else {
                    PersonItem item = listPerson.get(position);
                    item.setAccountEntityList((ArrayList<Map<String, Object>>) selectPerson.get("company"));
                    String name = item.getName();
                    String employeeCode = item.getEmployeeCode();
                    String idNo = item.getIdNo();//身份证
                    String mobile = item.getMobile();//手机号码
                    String accountEntity = item.getAccountEntity();//核算主体
                    String unitName = item.getUnitName();
                    String unitCode = item.getUnitCode();
                    String projectLeaderEmployeeCode = item.getProjectLeaderEmployeeCode();
                    String projectLeaderName = item.getProjectLeaderName();
                    String Position = item.getPosition();
                    boolean isShowItem = item.isShowItem();//是否显示Item
                    List<Map<String, Object>> accountEntityList = item.getAccountEntityList();
                    List<Map<String, Object>> credentialsInfo = item.getCredentialsInfo();//证件类型

                    Map<String, Object> map = new HashMap<>();
                    map.put("name", name);
                    map.put("employeeCode", employeeCode);
                    map.put("idNo", idNo);
                    map.put("mobile", mobile);
                    map.put("accountEntity", accountEntity);
                    map.put("unitName", unitName);
                    map.put("unitCode", unitCode);
                    map.put("isShowItem", isShowItem);
                    List<Map<String, Object>> list = (ArrayList<Map<String, Object>>) selectPerson.get("company");
                    map.put("accountEntityList", list);
                    map.put("accountEntity", accountEntity);
                    map.put("credentialsInfo", credentialsInfo);
                    map.put("projectLeaderEmployeeCode", projectLeaderEmployeeCode + "");
                    map.put("projectLeaderName", projectLeaderName + "");
                    map.put("position", Position + "");
                    Intent intent = new Intent(ApprovalActivity.this, EditChuXinRenActivity.class);
                    //                Bundle bundle = new Bundle();
                    //                bundle.putSerializable("item",item);
                    //                intent.putExtras(bundle);
                    intent.putExtra("item", (Serializable) map);

                    intent.putExtra("position", position);
                    startActivityForResult(intent, 101);
                }
            }
        });
        //设置顶部状态栏样式
        AppUtils.setStateBar(ApprovalActivity.this, findViewById(R.id.frg_status_bar));
        //酒店行程
        HotelTripItem hotelTripItem = generalHotelTrip();
        List<HotelTripItem> hotelTripList = new ArrayList<>();
        hotelTripList.add(hotelTripItem);
        hotelTripListAdapter = new HotelTripListAdapter(ApprovalActivity.this, hotelTripList);
        DefinedListView hotelTripListView = (DefinedListView) findViewById(R.id.hotel_trip_list_view);//酒店行程
        hotelTripListView.setAdapter(hotelTripListAdapter);
        hotelTripListAdapter.setItemCellClick(this);
        pageIndex = getIntent().getIntExtra("navIndex", 0);
        if (1 == pageIndex) {
            findViewById(R.id.btn_outer).performClick();
        }
        //国际机票行程
        DefinedListView outGoTripListView = (DefinedListView) findViewById(R.id.out_trip_go_list_view);//国际返去程
        List<OuterTripItem> outGoList = new ArrayList<>();
        outGoList.add(generalOuterTrip());
        outGoAdapter = new OutTripListAdapter(ApprovalActivity.this, outGoList);
        outGoAdapter.setOutTripListAdapter(outGoAdapter);
        outGoAdapter.setItemCellClick(this);//item删除按钮点击事件
        outGoTripListView.setAdapter(outGoAdapter);
    }

    private void initViewData() {
        FlightItem item = generalInnerTrip();
        //国内H5机票 预定申请过来需要填充数据
        FlightItem applyOrder = (FlightItem) getIntent().getSerializableExtra(INNTEAR_FLIGHT_APPLY_ORDER);
        if (applyOrder != null) {
            item.setFromCity(applyOrder.getFromCity());
            item.setFromCityCode(applyOrder.getFromCityCode());
            item.setToCity(applyOrder.getToCity());
            item.setToCityCode(applyOrder.getToCityCode());
            item.setStartTime(applyOrder.getStartTime());
        }
        adapter.getList().add(item);
        adapter.notifyDataSetChanged();
        EditText editText = (EditText) findViewById(R.id.et_out_reason);
        editText.setText("");
        //人员列表
        listPerson.clear();
        adapterPerson.setList(listPerson);
        adapterPerson.notifyDataSetChanged();
        //如果是国际机票进来
        if (1 == pageIndex) {
            //是否是选择行程方案过来的
            ArrayList<OuterTripItem> outerTripItemsOrigin = (ArrayList<OuterTripItem>)
                    getIntent().getSerializableExtra(INTERNATION_FLIGHT_JURNEY_SCHEME);
            if (outerTripItemsOrigin != null && !outerTripItemsOrigin.isEmpty()) {
                outerTripIdMapper = new HashMap<>();
                for (int index = 0; index < outerTripItemsOrigin.size(); index++) {
                    OuterTripItem outerTripItem = outerTripItemsOrigin.get(index);
                    outerTripItem.setId(index);
                    outerTripIdMapper.put(index, outerTripItem.copy());
                }
                outGoAdapter.setList(outerTripItemsOrigin);
                outGoAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 查询IPD项目信息
     *
     * @param projName
     */
    private void requestIPD(String projName) {
        User user = AppUtils.getUserInfo(ApprovalActivity.this);
        String employeeCode = user.getEmployeeCode();
        Map<String, Object> params = new HashMap<>();
        params.put("ciphertext", "test");
        params.put("employeeCode", employeeCode);
        params.put("loginType", URL.LOGIN_TYPE);
        params.put("projName", projName);
        Retrofit retrofit = new RequestUtil().getRequestClient(URL.ZHONGCHE_93);
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        Call<Map<String, Object>> call = request.getpmsList(params);
        new RequestUtil().requestData(call, this, 14, "正在获取数据", false, ApprovalActivity.this);
    }

    /**
     * 生成国内机票行程数据
     *
     * @return
     */
    private FlightItem generalInnerTrip() {
        FlightItem item = new FlightItem();
        item.setShowItem(true);
        item.setShowDel(false);
//        item.setFromCity("长沙");
//        item.setToCity("北京");
//        item.setFromCityCode("CSX");
//        item.setToCityCode("PEK");
//        item.setStartTime(DateUtils.getDate(0));
        item.setBookHotel(0);
//        item.setCheckinTime(DateUtils.getDate(0));
//        item.setCheckoutTime(DateUtils.getDate(1));
        item.setTripType(2);
        item.setTripMode("飞机-经济舱");
        return item;
    }

    /**
     * 生成国际行程数据
     *
     * @return
     */
    private OuterTripItem generalOuterTrip() {
        OuterTripItem outerTripItem = new OuterTripItem();
//        outerTripItem.setStartTime(DateUtils.getDate(1));
        return outerTripItem;
    }

    /**
     * 生成酒店行程数据
     *
     * @return
     */
    private HotelTripItem generalHotelTrip() {
        HotelTripItem hotelTripItem = new HotelTripItem();
//        hotelTripItem.setCheckInDate(DateUtils.getDate(1));
//        hotelTripItem.setCheckOutDate(DateUtils.getDate(2));
        hotelTripItem.setCheckInDate("");
        hotelTripItem.setCheckOutDate("");
        return hotelTripItem;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
                //添加国内机票行程
                if (pageIndex == 0) {
                    List<FlightItem> list = adapter.getList();
                    if (list != null && 10 > list.size()) {
                        FlightItem item = generalInnerTrip();
                        list.add(item);
                        adapter.setList(list);
                        adapter.notifyDataSetChanged();
                    } else {
                        AlertUtil.show2(ApprovalActivity.this, "最多只能有10个行程", "确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        });
                    }
                } else if (pageIndex == 1) {
                    //添加国际机票行程数据
                    List<OuterTripItem> outGoList = outGoAdapter.getList();
                    outGoList.add(generalOuterTrip());
                    outGoAdapter.setList(outGoList);
                    outGoAdapter.notifyDataSetChanged();
                } else if (pageIndex == 2) {
                    //添加酒店行程数据
                    List<HotelTripItem> hotelList = hotelTripListAdapter.getList();
                    HotelTripItem hotelItem = generalHotelTrip();
                    hotelList.add(hotelItem);

                    hotelTripListAdapter.setList(hotelList);
                    hotelTripListAdapter.notifyDataSetChanged();
                }
                break;
            case R.id.btn_del_trip:
                if (pageIndex == 0) {
                    if (!showDelTrip) {
                        showDelTrip = true;
                        adapter.setShowDelBtn(true);
                    } else {
                        adapter.setShowDelBtn(false);
                        showDelTrip = false;
                    }
                    adapter.notifyDataSetChanged();
                } else if (pageIndex == 1) {
                    //删除国际机票行程数据
                    outGoAdapter.toggleShowDelBtn();
                    outGoAdapter.notifyDataSetInvalidated();
                } else {
                    hotelTripListAdapter.toggleShowDelBtn();
                    hotelTripListAdapter.notifyDataSetInvalidated();
                }
                break;
            case R.id.btn_add_person:
                //如果已经获取到了授权人信息，点击添加同行人直接显示人员弹窗,否则重新获取数据
                if (null != personInfoList && personInfoList.size() >= 0) {
                    showPopupWindows(1, personInfoList);
                } else {
                    getPersonList(false);
                }
                break;
            case R.id.btn_del_person:
//                if(!showDelPerson) {
//                    for(int i=0;i<adapterPerson.getCount();i++) {
//                        listPerson.get(i).setShowDel(true);
//                    }
//                    showDelPerson = true;
//                }
//                else {
//                    for(int i=0;i<adapterPerson.getCount();i++) {
//                        listPerson.get(i).setShowDel(false);
//                    }
//                    showDelPerson = false;
//                }
//
//                adapterPerson.setList(listPerson);
                adapterPerson.toggleDelBtn();
                adapterPerson.notifyDataSetChanged();
                break;
            case R.id.btn_save_order:
                if (canClick) {
                    if (pageIndex == 0) {//国内
                        canClick = false;
                        saveOrderApply();
                    } else if (pageIndex == 1) {//国际
                        canClick = false;
                        saveOrderApply();
                    } else {//国内酒店
                        if (checkHotelData()) {
                            canClick = false;
                            saveHotelOrderApply();
                        }
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
        if (null != data) {
            if (requestCode == 3001) {
                //选择了出发城市
//                fromCityCode= data.getStringExtra("code");
//                fromCityName = data.getStringExtra("name");
//                tv_city_from.setText(fromCityName);
//                int type = data.getIntExtra("type",0);//0 国内城市  1  国际城市

            } else if (requestCode == 3002) {
                //选择了到达城市
//                toCityCode = data.getStringExtra("code");
//                toCityName = data.getStringExtra("name");
//                tv_city_to.setText(toCityName);
//                int type = data.getIntExtra("type",0);//0 国内城市  1  国际城市
            } else if (requestCode == 3003) {
                //选择了到达城市
                String cityCode = data.getStringExtra("code");
                String cityName = data.getStringExtra("name");
//                tv_city_to.setText(toCityName);
                int type = data.getIntExtra("type", 0);//0 国内城市  1  国际城市
                List<FlightItem> list = adapter.getList();
                list.get(city_position).setFromCity(cityName);
                list.get(city_position).setFromCityCode(cityCode);
                adapter.notifyDataSetChanged();
            } else if (requestCode == 3004) {
                //选择了到达城市
                String cityCode = data.getStringExtra("code");
                String cityName = data.getStringExtra("name");
                List<FlightItem> list = adapter.getList();
                list.get(city_position).setToCityCode(cityCode);
//                tv_city_to.setText(toCityName);
                int type = data.getIntExtra("type", 0);//0 国内城市  1  国际城市
                list.get(city_position).setToCity(cityName);
                adapter.notifyDataSetChanged();
            } else if (requestCode == 101) {
                //修改人员信息后的返回事件
//                Bundle bundle = data.getExtras();
//                PersonItem personItem = (PersonItem) bundle.getSerializable("item");

                Map<String, Object> map = (Map<String, Object>) data.getSerializableExtra("item");
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
                personItem.setUnitCode(map.get("unitCode") + "");
                personItem.setUnitName(map.get("unitName") + "");
                personItem.setCompanyCode(map.get("companyCode") + "");
                personItem.setPosition(map.get("position") + "");
                String projectLeaderEmployeeCode = map.get("projectLeaderEmployeeCode").toString();
                String projectLeaderName = map.get("projectLeaderName").toString();
                if (StringUtil.isEmpty(projectLeaderEmployeeCode)) {
                    projectLeaderEmployeeCode = "";
                }
                if (StringUtil.isEmpty(projectLeaderName)) {
                    projectLeaderName = "";
                }
                personItem.setProjectLeaderEmployeeCode(projectLeaderEmployeeCode);
                personItem.setProjectLeaderName(projectLeaderName);
                int position = data.getIntExtra("position", 0);
                listPerson.set(position, personItem);
                adapterPerson.notifyDataSetChanged();
            } else if (requestCode == 2102) {
                //选择了酒店行程的日期
                String firstDay = data.getStringExtra("firstDay");
                String secondDay = data.getStringExtra("secondDay");
                List<HotelTripItem> hotelList = hotelTripListAdapter.getList();
                for (int i = 0; i < hotelList.size(); i++) {
                    if (i == hotelTripIndex) {
                        hotelList.get(i).setCheckInDate(firstDay);
                        hotelList.get(i).setCheckOutDate(secondDay);
                    }
                }
                hotelTripListAdapter.setList(hotelList);
                hotelTripListAdapter.notifyDataSetChanged();
            } else if (requestCode == 2103) {
                String cityCode = data.getStringExtra("code");
                String cityName = data.getStringExtra("name");
                List<HotelTripItem> hotelList = hotelTripListAdapter.getList();
                for (int i = 0; i < hotelList.size(); i++) {
                    if (i == hotelTripIndex) {
                        hotelList.get(i).setCityName(cityName);
                        hotelList.get(i).setCityCode(cityCode);
                    }
                }
                hotelTripListAdapter.setList(hotelList);
                hotelTripListAdapter.notifyDataSetChanged();
            } else if (requestCode == 2202) {
                //选择国际行程出发时间
                String firstDay = data.getStringExtra("firstDay");
                int position = data.getIntExtra("position", -1);
                if (position != -1) {
                    List<OuterTripItem> outTripList = outGoAdapter.getList();
                    for (int i = 0; i < outTripList.size(); i++) {
                        if (i == position) {
                            outTripList.get(i).setStartTime(firstDay);
                            break;
                        }
                    }
                    outGoAdapter.setList(outTripList);
                    outGoAdapter.notifyDataSetChanged();
                }
            } else if (requestCode == 2302) {
                //选择国际行程出发时间
                String firstDay = data.getStringExtra("firstDay");
                int position = data.getIntExtra("position", -1);
                if (position != -1) {
                    List<FlightItem> tripList = adapter.getList();
                    for (int i = 0; i < tripList.size(); i++) {
                        if (i == position) {
                            tripList.get(i).setStartTime(firstDay);
                            break;
                        }
                    }
                    adapter.setList(tripList);
                    adapter.notifyDataSetChanged();
                }
            } else if (requestCode == 2402) {
                //选择国际行程出发时间
                String firstDay = data.getStringExtra("firstDay");
                String secondDay = data.getStringExtra("secondDay");
                int position = data.getIntExtra("position", -1);
                if (position != -1) {
                    List<FlightItem> tripList = adapter.getList();
                    for (int i = 0; i < tripList.size(); i++) {
                        if (i == position) {
                            tripList.get(i).setCheckinTime(firstDay);
                            tripList.get(i).setCheckoutTime(secondDay);
                            break;
                        }
                    }
                    adapter.setList(tripList);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }


    /**
     * @param position
     * @param type     0-出发城市, 1-到达城市
     */
    @Override
    public void selectFromcity(int position, int type) {
        city_position = position;
        Intent cityFrom = new Intent(ApprovalActivity.this, AirportActivity.class);
        String tripType = adapter.getItem(position).getTripMode();
        if (tripType != null && tripType.contains("飞机")) {
            cityFrom.putExtra("dataType", 0);
        } else {
            cityFrom.putExtra("dataType", 1);
        }
        cityFrom.putExtra("inCity", "yes");
        if (type == 0) {
            startActivityForResult(cityFrom, 3003);
        } else {
            startActivityForResult(cityFrom, 3004);
        }
    }

    /**
     * 选择出行方式
     *
     * @param position
     */
    @Override
    public void selectTripMode(int position) {
        city_position = position;
        List<FlightItem> list = adapter.getList();
        if (selectTripType != null && list.get(city_position).getTripMode() != null) {
            LinearLayout ly_main = (LinearLayout) findViewById(R.id.ly_main);
            window.update();
            popOutShadow(window);
            window.showAtLocation(ly_main, Gravity.CENTER, 0, 0);
        } else {
            getTripType();
        }
    }

    PopupWinListAdapter popAdapter;
    List<Map<String, Object>> datalist;

    /**
     * 显示弹窗
     *
     * @param type 1选择同行人界面，2选择出行方式； 3选择核算主体界面，6 选择证件类型界面
     */
    private void showPopupWindows(final int type, List<Map<String, Object>> list) {
        this.datalist = list;
        LinearLayout ly_main = (LinearLayout) findViewById(R.id.ly_main);
        //构建一个popupwindow的布局
        popupView = ApprovalActivity.this.getLayoutInflater().inflate(R.layout.popupwindow_select_person, null);
        popAdapter = new PopupWinListAdapter(ApprovalActivity.this, this.datalist, type);
//        for(int i=0;i<popAdapter.getCount();i++) {
//            datalist.get(i).put("isCheck","false");
//        }
        if (type == 2) {
            //默认出行方式为第二种
            for (int i = 0; i < datalist.size(); i++) {
                String id = datalist.get(i).get("id").toString();
                if (id.equals("2.0")) {
                    datalist.get(i).put("isCheck", "true");
                    selectTripType = datalist.get(i);
                }
            }
//            datalist.get(1).put("isCheck", "true");
//            selectTripType = datalist.get(1);
        } else if (type == 1) {
            //选择同行人时，默认选择的用户为当前登录用户，不在dataList的数据里面
            for (int i = 0; i < popAdapter.getCount(); i++) {
                datalist.get(i).put("isCheck", "false");
            }
            selectPerson = currentUser;
        } else if (type == 12) {
            for (int i = 0; i < datalist.size(); i++) {
                if (i == 0) {
                    datalist.get(0).put("isCheck", "true");
                    ProjectLeader = datalist.get(0);
                } else {
                    datalist.get(i).put("isCheck", "false");
                }
            }
        } else if (type == 14) {//初始化ipd项目选择
            for (int i = 0; i < datalist.size(); i++) {
                datalist.get(i).put("isCheck", "false");
            }
        } else {
            //弹出类型选择窗口时，默认设置第一个为选择项
            for (int g = 0; g < datalist.size(); g++) {
                if (g == 0) {
                    datalist.get(0).put("isCheck", "true");
                } else {
                    datalist.get(g).put("isCheck", "false");
                }
            }
        }
        ListView listView = (ListView) popupView.findViewById(R.id.popupwin_list);
        listView.setAdapter(popAdapter);
        //类型列表点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < popAdapter.getCount(); i++) {
                    datalist.get(i).put("isCheck", "false");
                }
                datalist.get(position).put("isCheck", "true");
                popAdapter.notifyDataSetChanged();
                if (type == 1) {
                    //选择人
                    ImageView icon_check = (ImageView) popupView.findViewById(R.id.icon_check);
                    icon_check.setImageResource(R.drawable.btn_singleselection_nor);
                    selectPerson = datalist.get(position);
                } else if (type == 2) {
                    //添加行程，选择出行方式
                    selectTripType = datalist.get(position);
                } else if (type == 3) {
                    //选择核算主体
//                    selectAccountEntity = datalist.get(position);
                } else if (type == 5) {
                    //选择项目经理
                } else if (type == 14) {
                    //ipd项目选择
                    ipdProject = datalist.get(position);
                    ipd_project_name.setText(ipdProject.get("projName").toString());//项目名称
                    ipd_project_mark.setText(ipdProject.get("costcode").toString());//项目令号
                    ipd_task_name.setText(ipdProject.get("taskName").toString());//任务名称
                    ipd_task_coding.setText(ipdProject.get("id").toString());//任务编码
                    ipd_manager_number.setText(ipdProject.get("pmCode").toString());//项目经理工号
                    ipd_manager_name.setText(ipdProject.get("pmName").toString());//项目经理名称
                    if (ipdProject.containsKey("approverName")) {
                        if (StringUtil.isEmpty(ipdProject.get("approverName").toString())) {
                            ipd_approval_personnel_number.setText(ipdProject.get("pmCode").toString());//出差任务审批人工号
                            ipd_approval_personnel.setText(ipdProject.get("pmName").toString());//出差任务审批人
                        } else {
                            ipd_approval_personnel_number.setText(ipdProject.get("approver").toString());//出差任务审批人工号
                            ipd_approval_personnel.setText(ipdProject.get("approverName").toString());//出差任务审批人
                        }
                    } else {
                        ipd_approval_personnel_number.setText(ipdProject.get("pmCode").toString());//出差任务审批人工号
                        ipd_approval_personnel.setText(ipdProject.get("pmName").toString());//出差任务审批人
                    }
                } else if (type == 12) {
                    ProjectLeader = datalist.get(position);
                }
            }
        });

        TextView popuptitle = (TextView) popupView.findViewById(R.id.popup_title);
        if (type == 1) {
            //出行人员
            TextView current_username = (TextView) popupView.findViewById(R.id.item_current_username);
            current_username.setText(currentUser.get("employeeName") + "");
//            popupView.findViewById(R.id.popupwin_list).setVisibility(View.GONE);
            popupView.findViewById(R.id.select_person).setVisibility(View.VISIBLE);
            TextView tv_ok = (TextView) popupView.findViewById(R.id.select_person_ok);
            tv_ok.setText("下一步");
            tv_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < listPerson.size(); i++) {
                        PersonItem item = listPerson.get(i);
                        if (null != item.getEmployeeCode() && item.getEmployeeCode().equals(selectPerson.get("employeeCode"))) {
                            AlertUtil.show2(ApprovalActivity.this, "您已添加过该人员", "确定", new View.OnClickListener() {
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
                    List<Map<String, Object>> companyList = (List<Map<String, Object>>) selectPerson.get("company");
                    List<Map<String, Object>> documentInfoList = (List<Map<String, Object>>) selectPerson.get("documentInfo");
                    if (documentInfoList.size() == 1) {
                        //设置第一个证件类型为选中的状态
                        documentInfoList.get(0).put("isCheck", "true");
                        selectPerson.put("documentInfo", documentInfoList);//缓存全局
                        if (companyList.size() == 1) {
                            //创建酒店预定申请单是，需判断用户是否有身份证证件
                            if (pageIndex == 2 && !hasIDCard(documentInfoList)) {
                                AlertUtil.show2(ApprovalActivity.this, "该员工没有添加身份证，不能创建国内酒店申请单喔~", "确定", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                    }
                                });
                                window.dismiss();
                            } else {
                                List<Map<String, Object>> uintInfoList = null;
                                try {
                                    //部门可能为空
                                    uintInfoList = (List<Map<String, Object>>) companyList.get(0).get("unitInfo");
                                } catch (Exception e) {

                                }

                                if (uintInfoList == null || uintInfoList.size() <= 1) {
                                    PersonItem itemPerson = new PersonItem();
                                    itemPerson.setShowItem(true);
                                    itemPerson.setShowDel(false);
                                    itemPerson.setName(selectPerson.get("employeeName") + "");
                                    itemPerson.setEmployeeCode(selectPerson.get("employeeCode") + "");
                                    itemPerson.setIdNo(selectPerson.get("idCard") + "");
                                    itemPerson.setMobile(selectPerson.get("mobil") + "");
                                    Map<String, Object> map = companyList.get(0);//默认选中核算主体
                                    itemPerson.setAccountEntity(map.get("companyName") + "");
                                    itemPerson.setCompanyCode(map.get("companyCode") + "");
                                    itemPerson.setPosition(selectPerson.get("position") + "");
                                    if (uintInfoList == null || uintInfoList.size() == 0) {
                                        itemPerson.setUnitCode("");
                                        itemPerson.setUnitName("");
                                    } else {
                                        Map<String, Object> uintInfo = uintInfoList.get(0);
                                        itemPerson.setUnitCode(uintInfo.get("unitCode") + "");
                                        itemPerson.setUnitName(uintInfo.get("unitName") + "");
                                    }

                                    itemPerson.setCredentialsInfo(documentInfoList);
                                    //设置第一个核算主体为选中状态
                                    companyList.get(0).put("isCheck", "true");
                                    itemPerson.setAccountEntityList(companyList);

                                    listPerson.add(itemPerson);
                                    adapterPerson.setList(listPerson);
                                    adapterPerson.notifyDataSetChanged();
                                } else {
                                    //进入部门选择页面
                                    selectPerson.put("companyCode", companyList.get(0).get("companyCode"));
                                    selectPerson.put("companyName", companyList.get(0).get("companyName"));
                                    showPopupWindows(7, uintInfoList);
                                }

                            }
                        } else {
                            //设置选中的证件类型，并显示核算主体选择界面
                            selectPerson.put("documentInfo", documentInfoList);
                            showPopupWindows(3, companyList);
                        }
                    } else if (documentInfoList.size() > 1) {
                        //显示选择证件类型界面
                        /**
                         * 1、创建国内机票预定申请单时
                         *   a、若用户有身份证件信息，设置人员身份证为选中状态，并判断用户有多少个核算主体：
                         *      i、若用户有多少个核算主体，则进入选择核算主体界面；
                         *      ii、若用户只有一个核算主体，则直接添加该人员
                         *   b、若没有身份证，则进入选择证件的弹窗
                         * 2、其他情况直接进入选择证件界面
                         */
                        if (pageIndex == 0) {
                            boolean hasIdCard = false;
                            for (int f = 0; f < documentInfoList.size(); f++) {
                                Map<String, Object> fMap = documentInfoList.get(f);
                                double documentId = (double) fMap.get("DOCUMENTID");
                                if (documentId == 1) {
                                    hasIdCard = true;
                                    documentInfoList.get(f).put("isCheck", "true");
//                                    break;
                                } else {
                                    documentInfoList.get(f).put("isCheck", "false");
                                }
                            }
                            if (hasIdCard) {
                                //                               //更新缓存的员工信息
//                                selectPerson.put("documentInfo",documentInfoList);//缓存全局
                                if (companyList.size() == 1) {
                                    List<Map<String, Object>> uintInfoList = null;
                                    try {
                                        //部门可能为空
                                        uintInfoList = (List<Map<String, Object>>) companyList.get(0).get("unitInfo");
                                    } catch (Exception e) {
                                    }

                                    if (uintInfoList == null || uintInfoList.size() <= 1) {
                                        PersonItem itemPerson = new PersonItem();
                                        itemPerson.setShowItem(true);
                                        itemPerson.setShowDel(false);
                                        itemPerson.setName(selectPerson.get("employeeName") + "");
                                        itemPerson.setEmployeeCode(selectPerson.get("employeeCode") + "");
                                        itemPerson.setIdNo(selectPerson.get("idCard") + "");
                                        itemPerson.setMobile(selectPerson.get("mobil") + "");
                                        Map<String, Object> map = companyList.get(0);//默认选中核算主体
                                        itemPerson.setAccountEntity(map.get("companyName") + "");
                                        itemPerson.setCompanyCode(map.get("companyCode") + "");
                                        itemPerson.setPosition(selectPerson.get("position") + "");
                                        if (uintInfoList == null || uintInfoList.size() == 0) {
                                            itemPerson.setUnitCode("");
                                            itemPerson.setUnitName("");
                                        } else {
                                            Map<String, Object> uintInfo = uintInfoList.get(0);
                                            itemPerson.setUnitCode(uintInfo.get("unitCode") + "");
                                            itemPerson.setUnitName(uintInfo.get("unitName") + "");
                                        }

                                        itemPerson.setCredentialsInfo(documentInfoList);
                                        //设置第一个核算主体为选中状态
                                        companyList.get(0).put("isCheck", "true");
                                        itemPerson.setAccountEntityList(companyList);

                                        listPerson.add(itemPerson);
                                        adapterPerson.setList(listPerson);
                                        adapterPerson.notifyDataSetChanged();
                                    } else {
                                        //进入部门选择页面
                                        selectPerson.put("companyName", companyList.get(0).get("companyName") + "");
                                        selectPerson.put("companyCode", companyList.get(0).get("companyCode") + "");

                                        showPopupWindows(7, uintInfoList);
                                    }
                                } else {
                                    //设置选中的证件类型，并显示核算主体选择界面
                                    selectPerson.put("documentInfo", documentInfoList);
                                    showPopupWindows(3, companyList);
                                }
                            } else {
                                showPopupWindows(6, documentInfoList);
                            }
                        } else {
                            showPopupWindows(6, documentInfoList);
                        }
                    } else {
                        ToastUtil.toastNeedData(ApprovalActivity.this, "该人员没有可用证件信息！");
                    }
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
//                        Toast.makeText(ApprovalActivity.this,"该人员没有核算主体",Toast.LENGTH_SHORT).show();
//                    }
                }
            });
            //选择当前用户（自己）时的事件
            popupView.findViewById(R.id.item_current_user).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageView icon_check = (ImageView) popupView.findViewById(R.id.icon_check);
                    icon_check.setImageResource(R.drawable.btn_singleselection_pressed);
                    for (int i = 0; i < popAdapter.getCount(); i++) {
                        datalist.get(i).put("isCheck", "false");
                    }
                    popAdapter.notifyDataSetChanged();
                    selectPerson = currentUser;
                }
            });
            popuptitle.setText("选择出行人员");
            selectTripType = null;
        } else if (type == 2) {
            //出行方式
            popupView.findViewById(R.id.select_person).setVisibility(View.GONE);
            popupView.findViewById(R.id.popupwin_list).setVisibility(View.VISIBLE);
            popuptitle.setText("选择出行方式");
            TextView tv_ok = (TextView) popupView.findViewById(R.id.select_person_ok);
            tv_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    window.dismiss();
                    String tripTypeName = selectTripType.get("name") + "";
                    List<FlightItem> list = adapter.getList();
                    String tripMode = list.get(city_position).getTripMode();
                    if ((!tripTypeName.contains("飞机") && tripMode.contains("飞机")) ||
                            (tripTypeName.contains("飞机") && !tripMode.contains("飞机"))) {
                        list.get(city_position).setFromCity("");
                        list.get(city_position).setToCity("");
                    }
                    list.get(city_position).setTripMode(selectTripType.get("name") + "");
                    double tripId = (double) selectTripType.get("id");
                    list.get(city_position).setTripType((int) tripId);
                    adapter.notifyDataSetChanged();
                }
            });
        } else if (type == 3) {
            //核算主体
            popupView.findViewById(R.id.select_person).setVisibility(View.GONE);
            popupView.findViewById(R.id.popupwin_list).setVisibility(View.VISIBLE);
            popuptitle.setText("选择核算主体");
            TextView tv_ok = (TextView) popupView.findViewById(R.id.select_person_ok);
            tv_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if(selectAccountEntity == null) {
//                        AlertUtil.show(ApprovalActivity.this, "请选择核算主体", "确定", new View.OnClickListener() {
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
//                    itemPerson.setShowItem(true);
//                    itemPerson.setShowDel(false);
//                    itemPerson.setName(selectPerson.get("employeeName")+"");
//                    itemPerson.setEmployeeCode(selectPerson.get("employeeCode")+"");
//                    itemPerson.setIdNo(selectPerson.get("idCard")+"");
//                    itemPerson.setMobile(selectPerson.get("mobil")+"");
                    Map<String, Object> selectedItem = null;
                    //遍历出选择的核算主体
                    for (int n = 0; n < datalist.size(); n++) {
                        Map<String, Object> pItem = datalist.get(n);
                        String pItemCheck = null == pItem.get("isCheck")
                                ? "false" : pItem.get("isCheck").toString();
                        if (pItemCheck.equals("true")) {
                            selectedItem = pItem;
                            itemPerson.setAccountEntity(pItem.get("companyName") + "");
                            itemPerson.setCompanyCode(pItem.get("companyCode") + "");
//                            itemPerson.setAccountEntity(pItem.get("companyCode") + "");
//                            itemPerson.setAccountEntityList(datalist);
                            selectPerson.put("accountEntityList", datalist);
                            break;
                        }
                    }
                    for (Map<String, Object> m6 : datalist) {
                        Log.i("--->", "核算主体：" + m6);

                    }
                    List<Map<String, Object>> uintInfoList = null;
                    try {
                        //部门可能为空
                        uintInfoList = (List<Map<String, Object>>) selectedItem.get("unitInfo");
                    } catch (Exception e) {

                    }
                    if (uintInfoList == null || uintInfoList.size() <= 1) {
                        window.dismiss();
                        itemPerson.setShowItem(true);
                        itemPerson.setShowDel(false);
                        itemPerson.setName(selectPerson.get("employeeName") + "");
                        itemPerson.setEmployeeCode(selectPerson.get("employeeCode") + "");
                        itemPerson.setIdNo(selectPerson.get("idCard") + "");
                        itemPerson.setMobile(selectPerson.get("mobil") + "");
                        itemPerson.setAccountEntityList(datalist);//设置核算主体数据
                        List<Map<String, Object>> documentInfoList = (List<Map<String, Object>>) selectPerson.get("documentInfo");
                        itemPerson.setCredentialsInfo(documentInfoList);
                        itemPerson.setAccountEntity(selectedItem.get("companyName") + "");
                        itemPerson.setCompanyCode(selectedItem.get("companyCode") + "");
                        itemPerson.setPosition(selectPerson.get("position") + "");
                        if (uintInfoList == null || uintInfoList.size() == 0) {
                            itemPerson.setUnitCode("");
                            itemPerson.setUnitName("");
                        } else {
                            Map<String, Object> uintInfo = uintInfoList.get(0);
                            itemPerson.setUnitCode(uintInfo.get("unitCode") + "");
                            itemPerson.setUnitName(uintInfo.get("unitName") + "");
                        }

                        listPerson.add(itemPerson);
                        adapterPerson.notifyDataSetChanged();
                    } else {
                        selectPerson.put("companyName", itemPerson.getAccountEntity());
                        selectPerson.put("companyCode", itemPerson.getCompanyCode());
                        showPopupWindows(7, uintInfoList);
                    }

                }
            });
        } else if (type == 6) {
            //选择证件类型
            popupView.findViewById(R.id.select_person).setVisibility(View.GONE);
            popupView.findViewById(R.id.popupwin_list).setVisibility(View.VISIBLE);
            TextView tv_ok = (TextView) popupView.findViewById(R.id.select_person_ok);
            popuptitle.setText("选择证件类型");
            tv_ok.setText("下一步");
            tv_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map<String, Object> selectMap = popAdapter.getSelectMap();
                    if (null == selectMap) {
                        AlertUtil.show(ApprovalActivity.this, "请选证件类型", "确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        }, "取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        }, "选择证件类型");
                        return;
                    } else {
                        window.dismiss();
                        List<Map<String, Object>> companylist = (List<Map<String, Object>>) selectPerson.get("company");
                        //判断当前选中的人是否有多个核算主体
                        if (companylist.size() > 1) {
                            selectPerson.put("documentInfo", datalist);
                            showPopupWindows(3, companylist);
                        } else if (companylist.size() <= 1) {
                            PersonItem itemPerson = new PersonItem();
                            itemPerson.setShowItem(true);
                            itemPerson.setShowDel(false);
                            itemPerson.setName(selectPerson.get("employeeName") + "");
                            itemPerson.setEmployeeCode(selectPerson.get("employeeCode") + "");
                            itemPerson.setIdNo(selectPerson.get("idCard") + "");
                            itemPerson.setMobile(selectPerson.get("mobil") + "");
                            Map<String, Object> map = companylist.get(0);//默认选中核算主体
                            itemPerson.setAccountEntity(map.get("companyName") + "");
                            itemPerson.setPosition(selectPerson.get("position") + "");
                            //TODO  当首次进来时 出行人员只有一个人时并且一个部门时 此时原有程序逻辑没有显示部门信息,2019/7/9修复
                            //TODO  删除后从新添加原有程序是走另外一套逻辑所以不存在此问题
                            List<Map<String, Object>> uintInfoList = null;
                            try {
                                //部门可能为空
                                uintInfoList = (List<Map<String, Object>>) map.get("unitInfo");
                            } catch (Exception e) {

                            }

                            if (uintInfoList != null && !uintInfoList.isEmpty()) {
                                Map<String, Object> uintInfo = uintInfoList.get(0);
                                itemPerson.setUnitCode(uintInfo.get("unitCode") + "");
                                itemPerson.setUnitName(uintInfo.get("unitName") + "");
                            }
//                            List<Map<String,Object>> documentInfoList = (List<Map<String, Object>>) selectPerson.get("documentInfo");
                            itemPerson.setCredentialsInfo(datalist);

                            listPerson.add(itemPerson);
                            adapterPerson.setList(listPerson);
                            adapterPerson.notifyDataSetChanged();
                        } else {
                            ToastUtil.toastNeedData(ApprovalActivity.this, "该人员没有核算主体");
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
                    for (int i = 0; i < popAdapter.getCount(); i++) {
                        datalist.get(i).put("isCheck", "false");
                    }
                    popAdapter.notifyDataSetChanged();
                    selectPerson = currentUser;
                }
            });
            selectTripType = null;
        } else if (type == 7) {
            //选择核算主体下的部门
            popupView.findViewById(R.id.select_person).setVisibility(View.GONE);
            popupView.findViewById(R.id.popupwin_list).setVisibility(View.VISIBLE);
            popuptitle.setText("选择部门");
            TextView tv_ok = (TextView) popupView.findViewById(R.id.select_person_ok);
            tv_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if(selectAccountEntity == null) {
//                        AlertUtil.show(ApprovalActivity.this, "请选择核算主体", "确定", new View.OnClickListener() {
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
                    itemPerson.setName(selectPerson.get("employeeName") + "");
                    itemPerson.setEmployeeCode(selectPerson.get("employeeCode") + "");
                    itemPerson.setIdNo(selectPerson.get("idCard") + "");
                    itemPerson.setMobile(selectPerson.get("mobil") + "");
//                    itemPerson.setAccountEntity(selectAccountEntity.get("companyName")+"");
                    //遍历出选择的核算主体
//                    for(int n = 0 ; n < datalist.size() ; n++){
//                        Map<String,Object> pItem = datalist.get(n);
//                        String pItemCheck = null == pItem.get("isCheck")
//                                ? "false" : pItem.get("isCheck").toString();
//                        if(pItemCheck.equals("true")){
//                            itemPerson.setAccountEntity(pItem.get("companyName") + "");
//                            break;
//                        }
//                    }

                    itemPerson.setAccountEntity(selectPerson.get("companyName") + "");
                    Map<String, Object> uintInfo = datalist.get(0);

                    for (int n = 0; n < datalist.size(); n++) {
//                        Log.i("--->","核算主体：" + m6);
                        Map<String, Object> pItem = datalist.get(n);
                        String pItemCheck = null == pItem.get("isCheck")
                                ? "false" : pItem.get("isCheck").toString();
                        if (pItemCheck.equals("true")) {
                            itemPerson.setUnitCode(pItem.get("unitCode") + "");
                            itemPerson.setUnitName(pItem.get("unitName") + "");
                        }

                    }
                    itemPerson.setPosition(selectPerson.get("position") + "");
                    itemPerson.setCompanyCode(selectPerson.get("companyCode") + "");
//                    itemPerson.setAccountEntityList(datalist);//设置核算主体数据
                    itemPerson.setAccountEntityList((ArrayList<Map<String, Object>>) (selectPerson.get("accountEntityList")));
                    List<Map<String, Object>> documentInfoList = (List<Map<String, Object>>) selectPerson.get("documentInfo");
                    itemPerson.setCredentialsInfo(documentInfoList);
                    listPerson.add(itemPerson);
                    adapterPerson.notifyDataSetChanged();
                }
            });
        } else if (type == 12) {
            //选择核算主体下的部门
            popupView.findViewById(R.id.select_person).setVisibility(View.GONE);
            popupView.findViewById(R.id.popupwin_list).setVisibility(View.VISIBLE);
            popupView.findViewById(R.id.popup_search).setVisibility(View.VISIBLE);//显示搜索按钮
            popupView.findViewById(R.id.popup_project_manager).setVisibility(View.VISIBLE);//显示项目输入框信息
            popuptitle.setText("选择项目经理");
            TextView tv_ok = (TextView) popupView.findViewById(R.id.select_person_ok);
            tv_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    window.dismiss();
                    int size = ProjectLeader.size();
                    if (size != 0) {
                        updateProjectLeader(ProjectLeader.get("EMPLOYEECODE").toString(), ProjectLeader.get("EMPLOYEENAME").toString());
                    }

                }
            });
            //点击搜索按钮
            TextView popup_search = (TextView) popupView.findViewById(R.id.popup_search);
            popup_search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText codeEt = (EditText) popupView.findViewById(R.id.popup_number);
                    EditText nameEt = (EditText) popupView.findViewById(R.id.popup_name);
                    String EmployeeCode = codeEt.getText().toString().trim();
                    String Name = nameEt.getText().toString().trim();
                    showProjectPopu(EmployeeCode, Name);
                }
            });
        } else if (type == 14) {
            popupView.findViewById(R.id.select_person).setVisibility(View.GONE);
            popupView.findViewById(R.id.popupwin_list).setVisibility(View.VISIBLE);
            popupView.findViewById(R.id.depart_search).setVisibility(View.VISIBLE);
            EditText depart_search_et = (EditText) popupView.findViewById(R.id.depart_search_et);
            TextView depart_search_bt = (TextView) popupView.findViewById(R.id.depart_search_bt);
            popuptitle.setText("选择IPD项目");
            TextView tv_ok = (TextView) popupView.findViewById(R.id.select_person_ok);
            //点击确认按钮
            tv_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    window.dismiss();
                }
            });
            //点击搜索按钮
            depart_search_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String m = depart_search_et.getText().toString().trim();
                    requestIPD(m);
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
        int pop_width = (int) (getDeviceWidth() * 0.9);
        window = new PopupWindow(popupView, pop_width, (int) (getDeviceWidth() * 1.1));
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
        window.showAtLocation(ly_main, Gravity.CENTER, 0, 0);
    }

    public int getDeviceWidth() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        return w_screen;
    }

    /**
     * 弹窗外阴影
     *
     * @param popupWindow
     */
    private void popOutShadow(PopupWindow popupWindow) {
        WindowManager.LayoutParams lp = ApprovalActivity.this.getWindow().getAttributes();
        lp.alpha = 0.3f;//设置阴影透明度
        ApprovalActivity.this.getWindow().setAttributes(lp);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            /**当关闭弹窗给筛选条件赋值*/
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = ApprovalActivity.this.getWindow().getAttributes();
                lp.alpha = 1f;
                ApprovalActivity.this.getWindow().setAttributes(lp);
            }
        });
    }

    /**
     * 获取已授权人员列表
     */
    private void getPersonList(final boolean firstIn) {
        User user = AppUtils.getUserInfo(ApprovalActivity.this);
        String employeeCode = user.getEmployeeCode();
//        showLoading("正在获取数据",true);
        Map<String, Object> params = new HashMap<>();
        params.put("ciphertext", "test");
        params.put("employeeCode", employeeCode);
        params.put("loginType", URL.LOGIN_TYPE);
        Retrofit retrofit = new RequestUtil().getRequestClient(URL.ZHONGCHE_93);
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        Call<Map<String, Object>> call = request.getPersonList(params);
        new RequestUtil().requestData(call, this, 3, "正在获取数据", true, ApprovalActivity.this);
    }

    /**
     * 获取出行方式列表
     */
    private void getTripType() {
        User user = AppUtils.getUserInfo(ApprovalActivity.this);
        Map<String, Object> params = new HashMap<>();
        params.put("ciphertext", "test");
        params.put("loginType", URL.LOGIN_TYPE);
        params.put("employeeCode", user.getEmployeeCode());
        Retrofit retrofit = new RequestUtil().getRequestClient(URL.ZHONGCHE_93);
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        Call<Map<String, Object>> call = request.getTripType(params);
        new RequestUtil().requestData(call, this, 2, mContext);
    }

    /**
     * 保存预订申请单
     */
    private void saveOrderApply() {
        EditText outreason = (EditText) findViewById(R.id.et_out_reason);
        if (!checkForm()) {//验证表单
            canClick = true;
            return;
        }
        //创建国际预定申请单，判断不能使用身份证
        if (pageIndex == 1) {
            String userIdName = usedIdCardName();
            if (userIdName.length() > 0) {
                AlertUtil.show2(ApprovalActivity.this, "不能使用身份证订购国际机票！", "确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
                canClick = true;
                return;
            }
        }
//        String fromTxt = tv_city_from.getText().toString().trim();
//        String toTxt = tv_city_to.getText().toString().trim();
        //显示加载状态
//        showLoading("正在提交数据",false);
        Map<String, Object> params = new HashMap<>();
        params.put("ciphertext", "test");
        //0-国内，1-国际
        if (pageIndex == 1) {
//            params.put("datetimeEndVal",time_end.getText()+"");
//            params.put("datetimeStartVal",time_start.getText()+"");

            EditText suggestEt = (EditText) findViewById(R.id.outer_trip_suggest);
            String suggestion = suggestEt.getText().toString().trim();
            params.put("suggestion", suggestion);
            //添加多国际行程
            List<Map<String, Object>> outTripList = new ArrayList<>();
            for (int i = 0; i < outGoAdapter.getList().size(); i++) {
                OuterTripItem outItem = outGoAdapter.getItem(i);
                if (outerTripIdMapper != null) {
                    OuterTripItem outerTripItemOrigin = outerTripIdMapper.get(outItem.getId());
                    if (!outerTripItemOrigin.equals(outItem)) {
                        outItem.setIsType(1);
                        outItem.setReferenceScheme(new ArrayList<>());
                    }
                }
                Map<String, Object> mm = new HashMap<>();
                mm.put("fromDate", outItem.getStartTime());
                mm.put("startCity", outItem.getFromCityName());
                mm.put("toCity", outItem.getToCityName());
                mm.put("isType", outItem.getIsType());
                mm.put("referenceScheme", outItem.getReferenceScheme());
                outTripList.add(mm);
            }
            params.put("createInterTripList", outTripList);
        } else {
            //国内机票行程
            //封装出差行程列表数据
            List<Map<String, Object>> tripList = new ArrayList<>();
            List<FlightItem> list = adapter.getList();//总的行程数据
            for (int j = 0; j < list.size(); j++) {
                Map<String, Object> item = new HashMap<>();
                FlightItem flightItem = list.get(j);
                int tripType = flightItem.getTripType();
                //出发到达城市三字码
                String fromCityName = flightItem.getFromCity();
                String fromCityCode = flightItem.getFromCityCode();
                String toCityName = flightItem.getToCity();
                String toCityCode = flightItem.getToCityCode();

//            Log.i("--->","出行类型:" + tripType);
                if (tripType == 1 || tripType == 2) {
                    //出行方式为飞机
                    item.put("isHotelCity", "1");//1传入的是机场类型的城市数据
//                    AppUtils.saveFlightCityHistory(ApprovalActivity.this,fromCityName + "-" + fromCityCode,toCityName + "-" + toCityCode );
                } else {
                    item.put("isHotelCity", "0");//0传入的是酒店类型的城市数据
//                    AppUtils.saveHotelCityHistory(ApprovalActivity.this,fromCityName + "-" + fromCityCode,toCityName + "-" + toCityCode );
                }
//            Log.i("--->","---"+ flightItem.getFromCityCode() + "====" +flightItem.getToCityCode());
                item.put("checkInTime", flightItem.getCheckinTime());
                item.put("checkOutTime", flightItem.getCheckoutTime());
                item.put("isHotel", flightItem.isBookHotel());
                item.put("startCity", flightItem.getFromCity());
                item.put("startDate", flightItem.getStartTime());
                item.put("toCity", flightItem.getToCity());
                item.put("tripType", flightItem.getTripType());
                item.put("startCityCode", flightItem.getFromCityCode());
                item.put("toCityCode", flightItem.getToCityCode());
//            Log.i("--->","行程数据： " + item);
                tripList.add(item);
            }
            params.put("createTripList", tripList);
        }
//        params.put("destination",toTxt);
//        params.put("origin",fromTxt);
        params.put("outreason", outreason.getText() + "");
        User user = AppUtils.getUserInfo(ApprovalActivity.this);
        String employeeCode = user.getEmployeeCode();
        String employeeName = user.getEmployeeName();
        params.put("employeeCode", employeeCode);
        params.put("employeeName", employeeName);
        params.put("loginType", URL.LOGIN_TYPE);
        params.put("type", pageIndex);
        //暂时传空字符串
        params.put("destinationCode", "");//到达城市三字码
        params.put("originCode", "");//出发城市三字码

        //封装出行人员列表数据
        List<Map<String, Object>> togetherManList = new ArrayList<>();
        for (int i = 0; i < listPerson.size(); i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("companyName", listPerson.get(i).getAccountEntity());
            item.put("employeeCode", listPerson.get(i).getEmployeeCode());
            item.put("employeeName", listPerson.get(i).getName());
            item.put("companyCode", listPerson.get(i).getCompanyCode());
            item.put("unitCode", listPerson.get(i).getUnitCode());
            item.put("unitName", listPerson.get(i).getUnitName());
            item.put("projectLeaderEmployeeCode", listPerson.get(i).getProjectLeaderEmployeeCode());
            //遍历出选择的证件id
            List<Map<String, Object>> documentInfoList = listPerson.get(i).getCredentialsInfo();
            for (int j = 0; j < documentInfoList.size(); j++) {
                Map<String, Object> mm = documentInfoList.get(j);
                String isCheck = null == mm.get("isCheck") ? "false" : mm.get("isCheck").toString();
                if (isCheck.equals("true")) {
                    double mmId = null == mm.get("DOCUMENTID") ? 0 : (double) mm.get("DOCUMENTID");
                    String documentInfo = null == mm.get("DOCUMENTINFO")
                            ? "" : mm.get("DOCUMENTINFO").toString();
                    item.put("documentId", mmId);
                    item.put("documentInfo", documentInfo);
                    break;
                }
            }
            togetherManList.add(item);
//            Log.i("--->","出行人员："+ item);
        }

        ////2020年三月份新增
        if (isIpd == "1") {
            if (listPerson.size() == 1) {
                if (!listPerson.get(0).getEmployeeCode().equals(employeeCode)) {
                    ToastUtil.toastNeedData(ApprovalActivity.this, "IPD项目同行人只能是自己且只允许一个同行人！");
                    canClick = true;
                    return;
                }
            } else {
                ToastUtil.toastNeedData(ApprovalActivity.this, "IPD项目同行人只能是自己且只允许一个同行人！");
                canClick = true;
                return;
            }
            if (!ipdProject.containsKey("projName") || StringUtil.isEmpty(ipdProject.get("projName").toString())) {
                ToastUtil.toastNeedData(ApprovalActivity.this, "IPD项目必选项目名称");
                canClick = true;
                return;
            }
            params.put("isIPD", isIpd);//是否IPD项目
            params.put("projName", ipdProject.get("projName"));//项目名称
            params.put("costCode", ipdProject.get("costcode"));//项目成本工作令号
            params.put("taskId", ipdProject.get("id"));//任务编号
            params.put("taskName", ipdProject.get("taskName"));//任务名称
            params.put("pmName", ipdProject.get("pmName"));//项目经理姓名
            params.put("pmCode", ipdProject.get("pmCode"));//项目经理工号
            String approverName = "";
            String approver = "";
            if (StringUtil.isEmpty(ipdProject.get("approverName") + "")) {
                approverName = employeeName;
            } else {
                approverName = ipdProject.get("approverName") + "";
            }
            if (StringUtil.isEmpty(ipdProject.get("approver") + "")) {
                approver = employeeCode;
            } else {
                approver = ipdProject.get("approver") + "";
            }
            params.put("approverName", approverName);//审核人姓名
            params.put("approver", approver);//审核人工号
        }
        params.put("createTogetherManList", togetherManList);

        Retrofit retrofit = new RequestUtil().getRequestClient(URL.ZHONGCHE_93);
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        System.out.println("--------------------请求参数--------------------------" + params.toString());
        Call<Map<String, Object>> call = request.saveOrderApply(params);
        new RequestUtil().requestData(call, this, 1, "正在提交数据...", false, ApprovalActivity.this);
    }

    /**
     * 创建酒店预定申请单
     */
    private void saveHotelOrderApply() {
        EditText reasonEt = (EditText) findViewById(R.id.et_out_reason);
        String reason = reasonEt.getText().toString().trim();
        if (reason.equals("")) {
            AlertUtil.show2(ApprovalActivity.this, "请填写出差事由", "确定", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            return;
        }
        //显示加载状态
        Map<String, Object> params = new HashMap<>();
        //封装出差行程列表数据
        List<Map<String, Object>> tripList = new ArrayList<>();
        List<HotelTripItem> list = hotelTripListAdapter.getList();//总的行程数据

        for (int j = 0; j < list.size(); j++) {
            Map<String, Object> item = new HashMap<>();
            HotelTripItem hotelItem = list.get(j);

            item.put("checkInTime", hotelItem.getCheckInDate());
            item.put("checkOutTime", hotelItem.getCheckOutDate());
            item.put("isHotel", 1);//是否入住酒店,1：是；0：否 ,
            item.put("isHotelCity", 0);//城市数据类型，0：酒店城市数据，1：机场城市数据 ,
            item.put("toCity", hotelItem.getCityName());
            item.put("toCityCode", hotelItem.getCityCode());
//            AppUtils.saveHotelCityHistory(ApprovalActivity.this,hotelItem.getCityName() + "-" + hotelItem.getCityCode(),"");
//            Log.i("--->","酒店行程数据： " + item);
            tripList.add(item);

        }
        params.put("ciphertext", "test");
        params.put("outreason", reason);
        User user = AppUtils.getUserInfo(ApprovalActivity.this);
        String employeeCode = user.getEmployeeCode();
        String employeeName = user.getEmployeeName();
        params.put("employeeCode", employeeCode);
        params.put("employeeName", employeeName);
        params.put("loginType", URL.LOGIN_TYPE);
        params.put("type", 0);//是否是国际预定申请单，0：不是，1：是
//        //暂时传空字符串
//        params.put("destinationCode","");//到达城市三字码
//        params.put("originCode","");//出发城市三字码

        //封装出行人员列表数据
        List<Map<String, Object>> togetherManList = new ArrayList<>();
        for (int i = 0; i < listPerson.size(); i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("companyName", listPerson.get(i).getAccountEntity());
            item.put("companyCode", listPerson.get(i).getCompanyCode());
            item.put("employeeCode", listPerson.get(i).getEmployeeCode());
            item.put("employeeName", listPerson.get(i).getName());
            item.put("unitCode", listPerson.get(i).getUnitCode());
            item.put("unitName", listPerson.get(i).getUnitName());
            //遍历出选择的证件id
            List<Map<String, Object>> documentInfoList = listPerson.get(i).getCredentialsInfo();
            for (int j = 0; j < documentInfoList.size(); j++) {
                Map<String, Object> mm = documentInfoList.get(j);
                String isCheck = null == mm.get("isCheck") ? "false" : mm.get("isCheck").toString();
                if (isCheck.equals("true")) {
                    double mmId = null == mm.get("DOCUMENTID") ? 0 : (double) mm.get("DOCUMENTID");
                    item.put("documentId", mmId);
                    String documentInfo = null == mm.get("DOCUMENTINFO")
                            ? "" : mm.get("DOCUMENTINFO").toString();
                    item.put("documentInfo", documentInfo);
                    break;
                }
            }
            togetherManList.add(item);
        }

        params.put("createTogetherManList", togetherManList);
        params.put("createTripList", tripList);

        Retrofit retrofit = new RequestUtil().getRequestClient(URL.ZHONGCHE_93);
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        Call<Map<String, Object>> call = request.saveOrderApplyHotelOnly(params);
        //酒店的预定申请单和机票的预定申请单类型走一样的返回响应
        new RequestUtil().requestData(call, this, 1, "正在提交数据...", false, ApprovalActivity.this);
    }

    /**
     * 检查酒店的预定申请单数据是否填写完整
     *
     * @return
     */
    private boolean checkHotelData() {
        boolean result = true;
        String toastMsg = "";//
        List<HotelTripItem> list = hotelTripListAdapter.getList();//总的行程数据

        EditText reasonEt = (EditText) findViewById(R.id.et_out_reason);
        String reason = reasonEt.getText().toString().trim();
        if (list.size() == 0) {
            toastMsg = "请添加行程";
            result = false;
        } else if (listPerson.size() == 0) {
            //判断是否有通信人信息
            toastMsg = "请添加出行人员";
            result = false;
        } else if (reason.equals("")) {
            toastMsg = "请填写出差事由";
            result = false;
        } else {
            for (int j = 0; j < list.size(); j++) {
                HotelTripItem hotelTripItem = list.get(j);
                String toCity = hotelTripItem.getCityName();
                //因为有默认的入住离店日期，所有只需要判断是否有选择城市信息
                if (null == toCity || toCity.equals("")) {
                    toastMsg = "请选择目的地";
                    result = false;
                    break;
                }
                String checkInDate = null == hotelTripItem.getCheckInDate() ? "" : hotelTripItem.getCheckInDate();
                String checkOutDate = null == hotelTripItem.getCheckOutDate() ? "" : hotelTripItem.getCheckOutDate();
                if (checkInDate.equals("") || checkOutDate.equals("")) {
                    toastMsg += "请选择行程" + (j + 1) + "的入住、离店时间~";
                    result = false;
                    break;
                }
            }
            for (int i = 0; i < list.size(); i++) {
                HotelTripItem hotelTripItemOne = list.get(i);
                for (int j = i + 1; j < list.size(); j++) {
                    HotelTripItem hotelTripItemTwo = list.get(j);
                    if (hotelTripItemOne.getCityName().equals(hotelTripItemTwo.getCityName())
                            && hotelTripItemOne.getCheckInDate().equals(hotelTripItemTwo.getCheckInDate())
                            && hotelTripItemOne.getCheckOutDate().equals(hotelTripItemTwo.getCheckOutDate())) {
                        toastMsg = "不能添加目的地、入住时间、离店时间同时相同的行程";
                        result = false;
                    }
                }
            }
        }

        if (!result) {
            AlertUtil.show2(ApprovalActivity.this, toastMsg, "确定", new View.OnClickListener() {
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
        if (pageIndex == 0) {//国内
            if (list.size() == 0) {
                msg = "请添加行程";
                result = false;
            } else {
                for (int a = 0; a < list.size(); a++) {
                    FlightItem inFlightItem = list.get(a);
                    if (inFlightItem.getTripMode() == null || "".equals(inFlightItem.getTripMode())) {
                        msg = "请选择出行方式";
                        result = false;
                        break;
                    } else if (inFlightItem.getFromCity() == null || "".equals(inFlightItem.getFromCity())) {
                        msg = "请选择出发城市";
                        result = false;
                        break;
                    } else if (inFlightItem.getToCity() == null || "".equals(inFlightItem.getToCity())) {
                        msg = "请选择目的地市";
                        result = false;
                        break;
                    } else if (inFlightItem.getStartTime().equals("")) {
                        msg = "请选择出发时间";
                        result = false;
                        break;
                    }
                }
                for (int i = 0; i < list.size(); i++) {
                    FlightItem flightItemOne = list.get(i);
                    for (int j = i + 1; j < list.size(); j++) {
                        FlightItem flightItemTwo = list.get(j);
                        if (flightItemOne.getFromCity().equals(flightItemTwo.getFromCity())
                                && flightItemOne.getToCity().equals(flightItemTwo.getToCity())
                                && flightItemOne.getStartTime().equals(flightItemTwo.getStartTime())) {
                            msg = "不能添加出发地、目的地、出发时间同时相同的行程";
                            result = false;
                        }
                    }
                }
            }
            if (listPerson.size() == 0) {
                msg = "请添加出行人员";
                result = false;
            } else {
//                for (int i = 0; i < listPerson.size(); i++) {
//                    String Position = listPerson.get(i).getPosition();
//                    String LeaderEmployeeCode = listPerson.get(i).getProjectLeaderEmployeeCode();
//                    if (null != Position && !Position.isEmpty() && !Position.equals("A")&& !Position.equals("B")&& !Position.equals("C")) {
//                        if (null == LeaderEmployeeCode || LeaderEmployeeCode.isEmpty()) {
//                            msg = "IPD项目差旅超标申请必填";
//                            result = false;
//                        }
//                    }
//                }
            }
        } else {//国际
//            if(time_start.getText().equals("")||time_end.getText().equals("")) {
//                msg = "请填写时间";
//                result = false;
//            }
            List<OuterTripItem> outList = outGoAdapter.getList();
            if (outList.size() == 0) {
                msg = "请添加行程";
                result = false;
            } else if (outList.size() > 0) {
                for (int i = 0; i < outList.size(); i++) {
                    OuterTripItem tripItem = outList.get(i);
                    String fromCity = tripItem.getFromCityName();
                    String toCity = tripItem.getToCityName();
                    String startTime = tripItem.getStartTime();
                    if (fromCity.equals("")) {
                        msg = "请填写行程" + (i + 1) + "的出发城市";
                        result = false;
                        break;
                    } else if (toCity.equals("")) {
                        msg = "请填写行程" + (i + 1) + "的到达城市";
                        result = false;
                        break;
                    } else if (startTime.equals("")) {
                        msg = "请填选择行程" + (i + 1) + "的出发时间";
                        result = false;
                        break;
                    }
                }
                for (int i = 0; i < outList.size(); i++) {
                    OuterTripItem outerTripItemOne = outList.get(i);
                    for (int j = i + 1; j < outList.size(); j++) {
                        OuterTripItem outerTripItemTwo = outList.get(j);
                        if (outerTripItemOne.getFromCityName().equals(outerTripItemTwo.getFromCityName())
                                && outerTripItemOne.getToCityName().equals(outerTripItemTwo.getToCityName())
                                && outerTripItemOne.getStartTime().equals(outerTripItemTwo.getStartTime())) {
                            msg = "不能添加出发地、目的地、出发时间同时相同的行程";
                            result = false;
                        }
                    }
                }
            }
            if (listPerson.size() == 0) {
                msg = "请添加出行人员";
                result = false;
            } else {
//                for (int i = 0; i < listPerson.size(); i++) {
//                    String Position = listPerson.get(i).getPosition();
//                    String LeaderEmployeeCode = listPerson.get(i).getProjectLeaderEmployeeCode();
//                    if (null != Position && !Position.isEmpty() && !Position.equals("A") && !Position.equals("B") && !Position.equals("C")) {
//                        if (null == LeaderEmployeeCode || LeaderEmployeeCode.isEmpty()) {
//                            msg = "IPD项目差旅超标申请必填";
//                            result = false;
//                        }
//                    }
//                }
            }
            EditText suggestEt = (EditText) findViewById(R.id.outer_trip_suggest);
            String suggestion = suggestEt.getText().toString().trim();
            if (suggestion.equals("")) {
                msg = "请填出行建议";
                result = false;
            }
        }

        EditText outreason = (EditText) findViewById(R.id.et_out_reason);
        String reason = outreason.getText().toString();
        if (reason.equals("")) {
            msg = "请填写出差事由";
            result = false;
        }

        if (!result) {
            AlertUtil.show2(ApprovalActivity.this, msg, "确定", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }
        return result;
    }

    /**
     * 返回使用了身份证件的用户姓名
     *
     * @return
     */
    private String usedIdCardName() {
        String userStr = "";
        for (int i = 0; i < listPerson.size(); i++) {
            //遍历出选择的证件id
            List<Map<String, Object>> documentInfoList = listPerson.get(i).getCredentialsInfo();
            for (int j = 0; j < documentInfoList.size(); j++) {
                Map<String, Object> mm = documentInfoList.get(j);
                String isCheck = null == mm.get("isCheck") ? "false" : mm.get("isCheck").toString();
                if (isCheck.equals("true")) {
                    double mmId = null == mm.get("DOCUMENTID") ? 0 : (double) mm.get("DOCUMENTID");
                    String userName = null == mm.get("EMPLOYEENAME") ? "" : mm.get("EMPLOYEENAME").toString();
                    if (mmId == 1) {
                        userStr += userName;
                    }
                }
            }
        }
        return userStr;
    }

    @Override
    public void requestSuccess(Object map, int type) {
        Map<String, Object> result = (Map<String, Object>) map;
        switch (type) {
            case 1:
                //提交国内机票预定申请单成功后的返回事件
                if (null != result) {
                    String code = null == result.get("code") ? "" : result.get("code").toString();
                    if (code.equals("00000")) {
                        ToastUtil.toastHandleSuccess(ApprovalActivity.this);
                        Intent intent = new Intent();
                        if (this.pageIndex == 0) {
                            intent.putExtra("updateType", 0);
                        } else {
                            intent.putExtra("updateType", 1);
                        }
                        EventBusUtil.sendEvent(new Event(EventCode.INNTEAR_FLIGHT_APPLY_ORDER));
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        ToastUtil.toastHandleError(ApprovalActivity.this);
                    }
                } else {
                    //请求失败
                    ToastUtil.toastHandleError(ApprovalActivity.this);
                }
//                stopLoading();
                canClick = true;
                break;
            case 2:
                if (null != result) {
//                    String code = (String) result.get("code");
//                    String codeMsg = (String) result.get("codeMsg");
                    String code = null == result.get("code") ? "" : result.get("code").toString();
                    String codeMsg = null == result.get("codeMsg") ? "" : result.get("codeMsg").toString();
                    if (code.equals("00000")) {
                        List<Map<String, Object>> datalist = (ArrayList<Map<String, Object>>) result.get("data");
                        showPopupWindows(2, datalist);
                    } else {
                        ToastUtil.toastError(ApprovalActivity.this);
                    }
                } else {
                    //请求失败
                    ToastUtil.toastError(ApprovalActivity.this);
                }
                break;
            case 3:
                //获取已授权人员列表数据成功后的回调事件
//                stopLoading();
                if (null != result) {
                    String code = null == result.get("code") ? "" : result.get("code").toString();
                    String codeMsg = null == result.get("codeMsg") ? "" : result.get("codeMsg").toString();

                    if (code.equals("00000")) {
                        //将自己的数据剔除出来（第一个）
                        personInfoList = (List<Map<String, Object>>) result.get("data");
                        currentUser = personInfoList.get(0);
                        selectPerson = currentUser;
                        personInfoList.remove(0);
                        if (null != personInfoList && personInfoList.size() > 0) {
//                        Log.i("--->","personInfoList:" + personInfoList);
                            showPopupWindows(1, personInfoList);
                        } else {
                            /**
                             * 判断当前用户的证件类型个数
                             * 1、若当前用户的证件个数为1,
                             *    a、若其核算主体个数只有1个，则直接新增该人员；
                             *    b、若其核算主体个数只有多个，则弹出选择核算主体的界面；
                             *    c、若其没有核算主0体，则不能订票；
                             *  2、若当前证件个数为多个，则进入选择证件类型界面；
                             *  3、若当前用户没有证件个数，则提示不能订票
                             */
                            List<Map<String, Object>> companylist = (List<Map<String, Object>>) selectPerson.get("company");
                            List<Map<String, Object>> documentInfoList = (List<Map<String, Object>>) selectPerson.get("documentInfo");

                            if (documentInfoList.size() == 1) {
                                documentInfoList.get(0).put("isCheck", "true");
                                selectPerson.put("documentInfo", documentInfoList);
                                if (companylist.size() > 1) {
                                    //进入选择核算主体信息界面

                                    showPopupWindows(3, companylist);
                                } else if (companylist.size() == 1) {
                                    //单个核算主体选择
                                    //判断是否有多个部门
                                    List<Map<String, Object>> uintInfoList = null;
                                    try {
                                        //部门可能为空
                                        uintInfoList = (List<Map<String, Object>>) companylist.get(0).get("unitInfo");
                                    } catch (Exception e) {

                                    }
                                    if (uintInfoList == null || uintInfoList.size() <= 1) {
                                        PersonItem itemPerson = new PersonItem();
                                        itemPerson.setShowItem(true);
                                        itemPerson.setShowDel(false);
                                        itemPerson.setName(selectPerson.get("employeeName") + "");
                                        itemPerson.setEmployeeCode(selectPerson.get("employeeCode") + "");
                                        itemPerson.setIdNo(selectPerson.get("idCard") + "");
                                        itemPerson.setMobile(selectPerson.get("mobil") + "");
                                        itemPerson.setPosition(selectPerson.get("position") + "");
                                        itemPerson.setCredentialsInfo(documentInfoList);
                                        itemPerson.setAccountEntityList(companylist);
                                        Object cpnObj = selectPerson.get("company");
                                        if (uintInfoList == null || uintInfoList.size() == 0) {
                                            itemPerson.setUnitCode("");
                                            itemPerson.setUnitName("");
                                        } else {
                                            Map<String, Object> uintInfo = uintInfoList.get(0);
                                            itemPerson.setUnitCode(uintInfo.get("unitCode") + "");
                                            itemPerson.setUnitName(uintInfo.get("unitName") + "");
                                        }
                                        if (null != cpnObj) {
                                            List<Map<String, Object>> company = (List<Map<String, Object>>) selectPerson.get("company");
                                            if (company.size() > 0) {
                                                String companyStr = company.get(0).get("companyName").toString();
                                                String companyCode = company.get(0).get("companyCode").toString();
                                                itemPerson.setAccountEntity(companyStr);
                                                itemPerson.setCompanyCode(companyCode);
                                            } else {
                                                itemPerson.setCompanyCode("");
                                                itemPerson.setAccountEntity("");
                                            }
                                        } else {
                                            itemPerson.setCompanyCode("");
                                            itemPerson.setAccountEntity("");
                                        }

                                        List<PersonItem> selfList = new ArrayList<PersonItem>();
                                        selfList.add(itemPerson);
                                        listPerson.add(itemPerson);
                                        adapterPerson.setList(selfList);
                                        adapterPerson.notifyDataSetChanged();
                                    } else {
                                        //进入部门选择页面
                                        selectPerson.put("companyCode", companylist.get(0).get("companyCode"));
                                        selectPerson.put("companyName", companylist.get(0).get("companyName"));
                                        showPopupWindows(7, uintInfoList);
                                    }
                                } else {
                                    ToastUtil.toastNeedData(ApprovalActivity.this, "该用户没有可用的核算主体！");
                                }
                            } else if (documentInfoList.size() > 1) {
                                //进入选择证件类型界面
                                showPopupWindows(6, documentInfoList);
                            } else {
                                ToastUtil.toastNeedData(ApprovalActivity.this, "该用户没有可用的证件！");
                            }


//                            if(companylist.size() > 1){
//                                showPopupWindows(3,companylist);
//                            }else{
//
//                                PersonItem itemPerson = new PersonItem();
//                                itemPerson.setShowItem(true);
//                                itemPerson.setShowDel(false);
//                                itemPerson.setName(selectPerson.get("employeeName")+"");
//                                itemPerson.setEmployeeCode(selectPerson.get("employeeCode")+"");
//                                itemPerson.setIdNo(selectPerson.get("idCard")+"");
//                                itemPerson.setMobile(selectPerson.get("mobil")+"");
//                                itemPerson.setCredentialsInfo(documentInfoList);
//                                Object cpnObj = selectPerson.get("company");
//                                if(null != cpnObj){
//                                    List<Map<String,Object>> company = (List<Map<String, Object>>) selectPerson.get("company");
//                                    if(company.size() > 0){
//                                        String companyStr = company.get(0).get("companyName").toString();
//                                        itemPerson.setAccountEntity(companyStr);
//                                    }else{
//                                        itemPerson.setAccountEntity("");
//                                    }
//                                }else{
//                                    itemPerson.setAccountEntity("");
//                                }
//
//                                List<PersonItem> selfList = new ArrayList<PersonItem>();
//                                selfList.add(itemPerson);
//                                listPerson.add(itemPerson);
//                                adapterPerson.setList(selfList);
//                                adapterPerson.notifyDataSetChanged();
//                            }
                        }
                    } else {
                        ToastUtil.toastError(ApprovalActivity.this);
                    }
                } else {
                    //请求失败
                    ToastUtil.toastError(ApprovalActivity.this);
                }
                break;
            case 4:
                if (null != result) {
                    String code = null == result.get("code") ? "" : result.get("code").toString();
                    String codeMsg = null == result.get("codeMsg") ? "" : result.get("codeMsg").toString();
                    if (code.equals("00000")) {
                        List<Map<String, Object>> list = (ArrayList<Map<String, Object>>) result.get("data");
                        int size = ProjectLeader.size();
                        for (int i = 0; i < list.size(); i++) {
                            if (size == 0) {
                                if (i == 0) {
                                    list.get(0).put("isCheck", "true");
                                    ProjectLeader = list.get(0);
                                } else {
                                    list.get(i).put("isCheck", "false");
                                }
                            } else {
                                if (ProjectLeader.get("EMPLOYEECODE").toString().equals(list.get(i).toString())) {
                                    list.get(i).put("isCheck", "true");
                                } else {
                                    list.get(i).put("isCheck", "false");
                                }
                            }


                        }
                        datalist = list;
                        popAdapter.setList(list);
                        popAdapter.notifyDataSetChanged();
                        // ToastUtil.toastNeedData(ApprovalActivity.this, datalist.toString());
                    } else {
                        ToastUtil.toastError(ApprovalActivity.this);
                    }
                } else {
                    //请求失败
                    ToastUtil.toastError(ApprovalActivity.this);
                }
                break;
            case 14://  请求到IPD项目信息
                if (null != result) {
                    String code = null == result.get("code") ? "" : result.get("code").toString();
                    if (code.equals("1")) {
                        List<Map<String, Object>> list = (ArrayList<Map<String, Object>>) result.get("data");
//                        ToastUtil.toastNeedData(ApprovalActivity.this, list.toString());
                        IPDProjectList = list;
                        datalist = list;
                        popAdapter.setList(IPDProjectList);
                        popAdapter.notifyDataSetChanged();
                    } else {
                        ToastUtil.toastError(ApprovalActivity.this);
                    }

                } else {
                    //请求失败
                    ToastUtil.toastError(ApprovalActivity.this);
                }
                break;
        }
    }

    @Override
    public void requestFail(int type) {
        switch (type) {
            case 1:
//                stopLoading();
                ToastUtil.toastHandleFail(ApprovalActivity.this);
                canClick = true;
                break;
            case 2:
                ToastUtil.toastFail(ApprovalActivity.this);
                break;
            case 3:
//                stopLoading();
                ToastUtil.toastFail(ApprovalActivity.this);
            case 14:
//                stopLoading();
                ToastUtil.toastFail(ApprovalActivity.this);
        }
    }

    @Override
    public void requestCancel(int type) {

    }

    /**
     * 选择出发地
     *
     * @param position
     * @param type     0 国内机票 1 国际机票 2 国内酒店
     */
    @Override
    public void selectDepartCityByType(int position, int type) {

    }

    /**
     * 选择目的地城市
     *
     * @param position
     * @param type     0 国内机票 1 国际机票 2 国内酒店
     */
    @Override
    public void selectDesCityByType(int position, int type) {
        if (type == 2) {
            hotelTripIndex = position;
            Intent intent = new Intent(ApprovalActivity.this, AirportActivity.class);
            intent.putExtra("dataType", 1);
            intent.putExtra("inCity", "yes");
            startActivityForResult(intent, 2103);

        }
    }

    @Override
    public void delTripByType(int position, int type) {
        if (type == 2) {
            List<HotelTripItem> hotelList = hotelTripListAdapter.getList();
            List<HotelTripItem> list = new ArrayList<>();
            for (int i = 0; i < hotelList.size(); i++) {
                if (i != position) {
                    list.add(hotelList.get(i));
                }
            }
            hotelTripListAdapter.setList(list);
            hotelTripListAdapter.notifyDataSetChanged();
        } else if (type == 1) {
            List<OuterTripItem> oldList = outGoAdapter.getList();
            List<OuterTripItem> list = new ArrayList<>();
            for (int i = 0; i < oldList.size(); i++) {
                if (i != position) {
                    list.add(oldList.get(i));
                }
            }
            outGoAdapter.setList(list);
            outGoAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 选择日期
     *
     * @param position
     * @param type     0 国内机票 1 国际机票 2 国内酒店
     */
    @Override
    public void selectDateByType(int position, int type) {
        if (type == 2) {
            hotelTripIndex = position;
            Intent intent = new Intent(ApprovalActivity.this, CalendarActivity.class);
            HotelTripItem hotelTripItem = hotelTripListAdapter.getItem(position);
            String checkIn = hotelTripItem.getCheckInDate();
            String checkOut = hotelTripItem.getCheckOutDate();
            intent.putExtra("selectType", 2);//2选择酒店日期
            intent.putExtra("days", 2);//需要选择多个日期
            intent.putExtra("firstDay", checkIn);
            intent.putExtra("secondDay", checkOut);
            startActivityForResult(intent, 2102);
        } else if (type == 1) {
            //选择国际机票时间
            Intent intent = new Intent(ApprovalActivity.this, CalendarActivity.class);
            OuterTripItem outerTripItem = outGoAdapter.getItem(position);
            String checkIn = outerTripItem.getStartTime();
            intent.putExtra("selectType", 1);//1选择国际机票日期
            intent.putExtra("days", 1);
            intent.putExtra("firstDay", checkIn);
            intent.putExtra("position", position);
            startActivityForResult(intent, 2202);
        }

    }

    @Override
    public void selectFlightDateByType(int position, int type, int dayType) {

        if (type == 0) {
            FlightItem outerTripItem = adapter.getItem(position);
            switch (dayType) {
                case 0:
                    Intent intent = new Intent(ApprovalActivity.this, CalendarActivity.class);
//            FlightItem outerTripItem = adapter.getItem(position);
//            String checkIn = outerTripItem.getStartTime();
                    intent.putExtra("selectType", 0);//1选择国际机票日期
                    intent.putExtra("days", 1);
                    intent.putExtra("firstDay", outerTripItem.getStartTime());
                    intent.putExtra("position", position);
                    startActivityForResult(intent, 2302);
                    break;
                case 1:
                    String checkIn = outerTripItem.getCheckinTime();
                    String startTime = outerTripItem.getStartTime();
                    if (checkIn.equals("") || checkIn == null) {
                        checkIn = startTime;
                    }
                    String checkOut = outerTripItem.getCheckoutTime();
                    Intent intent1 = new Intent(ApprovalActivity.this, CalendarActivity.class);
//            FlightItem outerTripItem = adapter.getItem(position);
//            String checkIn = outerTripItem.getStartTime();
                    intent1.putExtra("selectType", 2);//1选择国际机票日期
                    intent1.putExtra("days", 2);//需要选择多个日期
                    intent1.putExtra("firstDay", checkIn);
                    intent1.putExtra("secondDay", checkOut);
                    intent1.putExtra("position", position);
                    startActivityForResult(intent1, 2402);
                    break;

            }

        }
    }

    /**
     * 点击顶部tab的响应事件
     *
     * @param tabIndex
     */
    private void changeTab(int tabIndex) {
        TextView inner = (TextView) findViewById(R.id.text_inner);
        TextView outer = (TextView) findViewById(R.id.text_outer);
        TextView hotelTabTv = (TextView) findViewById(R.id.tab_hotel_apply_bill);

        LinearLayout cursor1 = (LinearLayout) findViewById(R.id.act_list_cursor);
        LinearLayout cursor2 = (LinearLayout) findViewById(R.id.act_list_cursor2);
        LinearLayout cursor3 = (LinearLayout) findViewById(R.id.act_list_cursor3);
        LinearLayout supplierView = (LinearLayout) findViewById(R.id.supplierView);

        LinearLayout flightLayout = (LinearLayout) findViewById(R.id.ly_flight); //航程列表
        LinearLayout tripOuter = (LinearLayout) findViewById(R.id.trip_for_outer);//国际行程
        DefinedListView hotelTripList = (DefinedListView) findViewById(R.id.hotel_trip_list_view);//酒店行程
        pageIndex = tabIndex;

        LinearLayout suggestLayout = (LinearLayout) findViewById(R.id.frg_approval_suggest_layout); //国际航程建议

        LinearLayout is_ipd_switch_ll = (LinearLayout) findViewById(R.id.is_ipd_switch_ll);
        if (tabIndex == 0) {
            //显示国内机票申请单
            cursor1.setVisibility(View.VISIBLE);
            cursor2.setVisibility(View.INVISIBLE);
            cursor3.setVisibility(View.INVISIBLE);

            inner.setTextColor(Color.parseColor("#c70019"));
            outer.setTextColor(Color.parseColor("#999999"));
            hotelTabTv.setTextColor(Color.parseColor("#999999"));

            flightLayout.setVisibility(View.VISIBLE);
            tripOuter.setVisibility(View.GONE);
            hotelTripList.setVisibility(View.GONE);
            suggestLayout.setVisibility(View.GONE);
            supplierView.setVisibility(View.GONE);
            //是否显示国内机票时的IPD开关
            is_ipd_switch_ll.setVisibility(View.VISIBLE);
        } else if (tabIndex == 1) {
            //显示国际机票申请单
            //下标
            cursor1.setVisibility(View.INVISIBLE);
            cursor2.setVisibility(View.VISIBLE);
            cursor3.setVisibility(View.INVISIBLE);

            inner.setTextColor(Color.parseColor("#999999"));
            outer.setTextColor(Color.parseColor("#c70019"));
            hotelTabTv.setTextColor(Color.parseColor("#999999"));

            flightLayout.setVisibility(View.GONE);
            tripOuter.setVisibility(View.VISIBLE);
            hotelTripList.setVisibility(View.GONE);
            suggestLayout.setVisibility(View.VISIBLE);
            supplierView.setVisibility(View.GONE);
            //是否显示国际机票时的IPD开关
            is_ipd_switch_ll.setVisibility(View.VISIBLE);
        } else {
            //显示国内酒店申请单
            cursor1.setVisibility(View.INVISIBLE);
            cursor2.setVisibility(View.INVISIBLE);
            cursor3.setVisibility(View.VISIBLE);

            inner.setTextColor(Color.parseColor("#999999"));
            outer.setTextColor(Color.parseColor("#999999"));
            hotelTabTv.setTextColor(Color.parseColor("#c70019"));

            flightLayout.setVisibility(View.GONE);
            hotelTripList.setVisibility(View.VISIBLE);
            tripOuter.setVisibility(View.GONE);
            suggestLayout.setVisibility(View.GONE);
            supplierView.setVisibility(View.GONE);
            //是否显示国内酒店时的IPD开关
            is_ipd_switch_ll.setVisibility(View.GONE);
        }
    }

    private boolean hasIDCard(List<Map<String, Object>> infoList) {
        boolean bool = false;
        for (int i = 0; i < infoList.size(); i++) {
            double documentId = (double) infoList.get(i).get("DOCUMENTID");
            if (documentId == 1) {
                bool = true;
                break;
            }
        }
        return bool;
    }

    //项目经理点击事件回调
    public void showProjectPopu(PersonItem item) {
        ProjectPersonItem = item;
        List<Map<String, Object>> datelist = new ArrayList<Map<String, Object>>();
        showPopupWindows(12, datelist);

    }

    //项目经理点击事件回调
    public void showProjectPopu(String EmployeeCode, String Name) {
        User user = AppUtils.getUserInfo(ApprovalActivity.this);
        String employeeCode = user.getEmployeeCode();
        Map<String, Object> params = new HashMap<>();
        params.put("employeeCode", EmployeeCode);
        params.put("employeeName", Name);
        params.put("ciphertext", "test");
        params.put("loginType", URL.LOGIN_TYPE);
        params.put("size", "100");
        params.put("page", "1");
        Retrofit retrofit = new RequestUtil().getRequestClient(URL.ZHONGCHE_93);
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        Call<Map<String, Object>> call = request.selectProjectLeader(params);
        new RequestUtil().requestData(call, this, 4, "正在获取数据", true, ApprovalActivity.this);
    }


    //更新列表中项目经理信息
    public void updateProjectLeader(String ProjectLeaderEmployeeCode, String ProjectLeaderName) {

        for (int i = 0; i < listPerson.size(); i++) {
            PersonItem PersonItem = listPerson.get(i);
            if (null != PersonItem.getEmployeeCode() && PersonItem.getEmployeeCode().equals(ProjectPersonItem.getEmployeeCode())) {
                listPerson.get(i).setProjectLeaderEmployeeCode(ProjectLeaderEmployeeCode);
                listPerson.get(i).setProjectLeaderName(ProjectLeaderName);
            }
        }
        adapterPerson.setList(listPerson);
        adapterPerson.notifyDataSetChanged();
    }
}
