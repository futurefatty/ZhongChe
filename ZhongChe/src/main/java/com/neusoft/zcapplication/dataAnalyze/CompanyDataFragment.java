package com.neusoft.zcapplication.dataAnalyze;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.internal.LinkedTreeMap;
import com.neusoft.zcapplication.Bean.User;
import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.api.CompanyDataApi;
import com.neusoft.zcapplication.base.BaseFragment;
import com.neusoft.zcapplication.entity.MonthlyIndexDatas;
import com.neusoft.zcapplication.entity.MonthlyTicketing;
import com.neusoft.zcapplication.event.Events;
import com.neusoft.zcapplication.http.NetWorkRequest;
import com.neusoft.zcapplication.http.RequestCallback;
import com.neusoft.zcapplication.http.RequestUtil;
import com.neusoft.zcapplication.http.URL;
import com.neusoft.zcapplication.http.now.CallBack;
import com.neusoft.zcapplication.http.now.JsonUtil;
import com.neusoft.zcapplication.http.now.RetrofitFactory;
import com.neusoft.zcapplication.tools.AlertUtil;
import com.neusoft.zcapplication.tools.AppUtils;
import com.neusoft.zcapplication.tools.LogUtil;
import com.neusoft.zcapplication.tools.StringUtil;
import com.neusoft.zcapplication.tools.ToastUtil;
import com.neusoft.zcapplication.widget.PopupWinListAdapter;
import com.neusoft.zcapplication.widget.SinglePickSelector;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Retrofit;


public class CompanyDataFragment extends BaseFragment implements View.OnClickListener, RequestCallback {
    private View fragmentView;

    private TextView mTvTotalCost;


    private LinearLayout mLlYeaer;
    private LinearLayout mLlMonth;
    private TextView mTvYear;
    private TextView mTvMonth;

    private TextView mTvEndorseTicketTotal;//国内额总计
    private TextView mTvEndorseTicketDomestic; //国际机票消费额
    private TextView mTvEndorseTicketInternational;//酒店消费额

    private TextView mTvEndorseRankPerTotal; // 国内张数
    private TextView mTvEndorseRankPerDomestic;//国际机票张数
    private TextView mTvEndorseRankPerInternational;//酒店

    private TextView mTvEndorseRankTotal; //国内平均报价
    private TextView mTvEndorseRankDomestic;//国际机票报价
    private TextView mTvEndorseRankInternational; //酒店机票报价


    private TextView mTvEndorseRankPerTotal1;//国内退改金额
    private TextView mTvEndorseRankPerDomestic1;//国际机票退改金额
    private TextView mTvEndorseRankPerInternational1;//酒店退改金额

    private TextView mTvTransaction1;//在线交易指标
    private TextView mTvTransaction2;//在线交易标准

    private TextView mTvRelation2;//关联及时率2
    private TextView mTvRelation1;//关联及时率1

    private TextView mTvBook2;//违规预订率2
    private TextView mTvBook1;//违规预订率1

    private TextView mTvSettlement1;//结算支付及时率1
    private TextView mTvSettlement2;//结算支付及时率2

    private TextView mTvChange2;//退改签率标准值
    private TextView mTvChange1;//退改签率指标值

    private SinglePickSelector yearSelector, monthSelector;//选择器

    private View popupView;

    private EditText tv_depart;
    private EditText tv_company;

    private Map<String, Object> selectAccountEntity;//选择的核算主体
    private Map<String, Object> selectunitEntity;//选择的部门

    private List<Map<String, Object>> accountEntityList;//核算主体列表
    private List<Map<String, Object>> unitEntityList;//部门主体列表
    private int year = -1;
    private int month = -1;

    private PopupWindow window = new PopupWindow();

    private List<String> yearDataList = new ArrayList<>();
    private List<String> monthDataList = new ArrayList<>();

    private Map<String, Object> currentPerson;

    private String selectCompanyStr = "";
    private String selectUnitStr = "";
    private String selectUnitCode = "";

    private List<Map<String, Object>> currentCompany;

    private LinearLayout depart_search;//部门搜索框

    public CompanyDataFragment() {

    }


    public static CompanyDataFragment getInstance() {
        CompanyDataFragment fragment = new CompanyDataFragment();
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {//相当于onResume
            // initData();
            getPersonList();
        } else {

        }
    }


    private void getPersonList() {
        User user = AppUtils.getUserInfo(getActivity());
        String employeeCode = user.getEmployeeCode();
//        showLoading("正在获取数据",true);
        Map<String, Object> params = new HashMap<>();
        params.put("ciphertext", "test");
        params.put("employeeCode", employeeCode);
        params.put("loginType", URL.LOGIN_TYPE);
        Retrofit retrofit = new RequestUtil().getRequestClient(URL.ZHONGCHE_93);
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        Call<Map<String, Object>> call = request.getPersonList(params);
//        new RequestUtil().requestData(call, this, 3, "正在获取数据", true, getActivity());
        new RequestUtil().requestDataNoLoading(call, this, 3, getActivity());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (null == fragmentView) {
            fragmentView = inflater.inflate(R.layout.fragment_company_data, container, false);
            initView();
        } else {
            ViewGroup parent = (ViewGroup) fragmentView.getParent();
            if (parent != null) {
                parent.removeView(fragmentView);
            }
        }
        return fragmentView;
    }

    private void initView() {
        mTvEndorseRankPerInternational1 = (TextView) fragmentView.findViewById(R.id.tv_endorse_rank_per_international1);
        mTvTotalCost = (TextView) fragmentView.findViewById(R.id.tv_total_cost);
        mTvEndorseRankInternational = (TextView) fragmentView.findViewById(R.id.tv_endorse_rank_international);
        mTvRelation2 = (TextView) fragmentView.findViewById(R.id.tv_relation2);
        mTvRelation1 = (TextView) fragmentView.findViewById(R.id.tv_relation1);
        mTvEndorseRankPerTotal1 = (TextView) fragmentView.findViewById(R.id.tv_endorse_rank_per_total1);
        mTvSettlement1 = (TextView) fragmentView.findViewById(R.id.tv_settlement1);
        mTvEndorseTicketDomestic = (TextView) fragmentView.findViewById(R.id.tv_endorse_ticket_domestic);
        mTvEndorseTicketInternational = (TextView) fragmentView.findViewById(R.id.tv_endorse_ticket_international);
        mLlYeaer = (LinearLayout) fragmentView.findViewById(R.id.ll_yeaer);
        mLlYeaer.setOnClickListener(this);
        mTvEndorseRankPerTotal = (TextView) fragmentView.findViewById(R.id.tv_endorse_rank_per_total);
        mTvSettlement2 = (TextView) fragmentView.findViewById(R.id.tv_settlement2);
        mTvEndorseRankPerDomestic = (TextView) fragmentView.findViewById(R.id.tv_endorse_rank_per_domestic);
        mTvEndorseTicketTotal = (TextView) fragmentView.findViewById(R.id.tv_endorse_ticket_total);
        mTvEndorseRankDomestic = (TextView) fragmentView.findViewById(R.id.tv_endorse_rank_domestic);
        mTvChange2 = (TextView) fragmentView.findViewById(R.id.tv_change2);
        mTvChange1 = (TextView) fragmentView.findViewById(R.id.tv_change1);
        mTvEndorseRankTotal = (TextView) fragmentView.findViewById(R.id.tv_endorse_rank_total);
        mTvEndorseRankPerDomestic1 = (TextView) fragmentView.findViewById(R.id.tv_endorse_rank_per_domestic1);
        mTvTransaction1 = (TextView) fragmentView.findViewById(R.id.tv_transaction1);
        mLlMonth = (LinearLayout) fragmentView.findViewById(R.id.ll_month);
        mLlMonth.setOnClickListener(this);
        mTvTransaction2 = (TextView) fragmentView.findViewById(R.id.tv_transaction2);
        mTvBook2 = (TextView) fragmentView.findViewById(R.id.tv_book2);
        mTvBook1 = (TextView) fragmentView.findViewById(R.id.tv_book1);
        mTvYear = (TextView) fragmentView.findViewById(R.id.tv_year);
        mTvMonth = (TextView) fragmentView.findViewById(R.id.tv_month);
        mTvEndorseRankPerInternational = (TextView) fragmentView.findViewById(R.id.tv_endorse_rank_per_international);
        fragmentView.findViewById(R.id.departView).setOnClickListener(this);
        fragmentView.findViewById(R.id.company_view).setOnClickListener(this);

        tv_company = (EditText) fragmentView.findViewById(R.id.company_tv);
        tv_depart = (EditText) fragmentView.findViewById(R.id.tv_depart);
        tv_company.setOnClickListener(this);
        tv_depart.setOnClickListener(this);
        yearDataList.clear();
        Calendar c = Calendar.getInstance();
        int currentYear = c.get(Calendar.YEAR); // 获取当前年份
        mTvYear.setText("" + Integer.valueOf(currentYear) + "");
        int currentMonth = c.get(Calendar.MONTH); // 获取当前年份
        mTvMonth.setText((currentMonth + 1) + "");
        month = currentMonth + 1;
        int startYear = 1770;
        year = currentYear;
        for (int i = currentYear; i >= startYear; i--) {
            yearDataList.add(i + "年");
        }
        monthDataList.add("全部");
        for (int i = 0; i < 12; i++) {
            monthDataList.add((i + 1) + "月");
        }

        yearSelector = new SinglePickSelector(getActivity(), new SinglePickSelector.ResultHandler() {
            @Override
            public void handle(String data) {
                year = Integer.valueOf(data.replace("年", ""));
                mTvYear.setText("" + year);
                getCompanyData();
            }
        }, yearDataList);
        monthSelector = new SinglePickSelector(getActivity(), new SinglePickSelector.ResultHandler() {
            @Override
            public void handle(String data) {
                if ("全部".equals(data)) {
                    month = -1;
                    mTvMonth.setText("全部");
                } else {
                    month = Integer.valueOf(data.replace("月", ""));
                    mTvMonth.setText("" + month);
                }
                getCompanyData();
            }
        }, monthDataList);
    }

    private void selectCompany() {
        User user = AppUtils.getUserInfo(getActivity());
        String employeeCode = user.getEmployeeCode();
        Map<String, Object> params = new HashMap<>();
        params.put("employeeCode", employeeCode);
        params.put("ciphertext", "test");
        params.put("loginType", URL.LOGIN_TYPE);
        Retrofit retrofit = new RequestUtil().getRequestClient(URL.ZHONGCHE_93);
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        Call<Map<String, Object>> call = request.selectCompanyNameByEmployeeCode(params);
        new RequestUtil().requestDataNoLoading(call, this, 12, getActivity());
    }

    private void getCompanyData() {
        User user = AppUtils.getUserInfo(getActivity());
        String employeeCode = user.getEmployeeCode();
        Map<String, Object> params = new HashMap<>();
        params.put("ciphertext", "test");
        params.put("employeeCode", employeeCode + "");
        params.put("loginType", URL.LOGIN_TYPE);
        params.put("particularYear", year + "");
        if (month != -1) {
            if (month < 10) {
                params.put("ymonth", year + "0" + month);
            } else {
                params.put("ymonth", year + "" + month);
            }
        } else {
            params.put("ymonth", year+"");
        }
//        params.put("unitName", selectUnitStr);//不传字段值
        params.put("unitName", "");
        params.put("unitCode", selectUnitCode);
        params.put("accountEntity", selectCompanyStr);

//        ToastUtil.toast("正在查询" + JsonUtil.toJson(params));
        LogUtil.d(JsonUtil.toJson(params));
        RetrofitFactory.getInstance().createApi(CompanyDataApi.class).getMonthlyTicketingData(params)
                .enqueue(new CallBack<List<MonthlyTicketing>>() {
                    @Override
                    public void success(List<MonthlyTicketing> response) {
                        clearViewData();
                        if (response.size() > 0) {
                            MonthlyTicketing model = response.get(0);

//                            mTvEndorseTicketDomestic.setText(model.getInterPrice() == null ? "-" : "" +
//                                    new BigDecimal(Double.parseDouble(model.getInterPrice())).setScale(1,BigDecimal.ROUND_UNNECESSARY).floatValue() );
                            mTvEndorseTicketDomestic.setText(model.getInterPrice().equals("-") ? "-" : "" +
                                    parseNumber(formatFloatNumber(Double.parseDouble(model.getInterPrice()))));
                            mTvEndorseTicketTotal.setText(model.getDomesticPrice().equals("-") ? "-" : "" +
                                    parseNumber(formatFloatNumber(Double.parseDouble(model.getDomesticPrice()))));
                            mTvEndorseTicketInternational.setText(model.getHotelPrice().equals("-") ? "-" : "" +
                                    parseNumber(formatFloatNumber(Double.parseDouble(model.getHotelPrice()))));

                            mTvEndorseRankPerTotal.setText(model.getDomestiCount().equals("-") ? "-" : parseInt(model.getDomestiCount()));
                            mTvEndorseRankPerDomestic.setText(model.getInterCount().equals("-") ? "-" : parseInt(model.getInterCount()));
                            mTvEndorseRankPerInternational.setText(model.getHotelInDay().equals("-") ? "-" : parseInt(model.getHotelInDay()));

                            mTvEndorseRankTotal.setText(model.getDomesticAverageFare().equals("-") ? "-" : "" +
                                    parseNumber(formatFloatNumber(Double.parseDouble(model.getDomesticAverageFare()))));
                            mTvEndorseRankDomestic.setText(model.getInterAverageFare().equals("-") ? "-" : "" +
                                    parseNumber(formatFloatNumber(Double.parseDouble(model.getInterAverageFare()))));
                            mTvEndorseRankInternational.setText(model.getHotelAverageFare().equals("-") ? "-" : "" +
                                    parseNumber(formatFloatNumber(Double.parseDouble(model.getHotelAverageFare()))));

                            mTvEndorseRankPerTotal1.setText(model.getDomesticRetreatPrice().equals("-") ? "-" : "" +
                                    parseNumber(formatFloatNumber(Double.parseDouble(model.getDomesticRetreatPrice()))));
                            mTvEndorseRankPerDomestic1.setText(model.getInterRetreatPrice().equals("-") ? "-" : "" +
                                    parseNumber(formatFloatNumber(Double.parseDouble(model.getInterRetreatPrice()))));
                            mTvEndorseRankPerInternational1.setText(model.getHotelRetreatPrice().equals("-") ? "-" : "" +
                                    parseNumber(formatFloatNumber(Double.parseDouble(model.getHotelRetreatPrice()))));
//                            mTvTotalCost.setText(StringUtil.);
                            mTvTotalCost.setText(model.getSumPrice().equals("-") ? "-" : "¥ " +
                                    parseNumber(formatFloatNumber(Double.parseDouble(model.getSumPrice()))));
                        }
                    }

                    @Override
                    public void fail(String code) {
                        clearViewData();
                    }
                });
        RetrofitFactory.getInstance().createApi(CompanyDataApi.class).getMonthlyIndexDatas(params)
                .enqueue(new CallBack<List<MonthlyIndexDatas>>() {
                    @Override
                    public void success(List<MonthlyIndexDatas> response) {
                        clearIndexData();
                        if (response.size() > 0) {
                            MonthlyIndexDatas model = response.get(0);
                            //退改签率指标值
                            mTvChange1.setText(model.getBalanceFare().equals("-") ? "-" :
                                    (model.getBalanceFare().contains(".")) ?
                                            formatFloatNumber(Double.parseDouble(model.getBalanceFare())) + "%" :
//                                            new BigDecimal(model.getBalanceFare()).setScale(2,BigDecimal.ROUND_HALF_UP).floatValue() + "%"
                                            model.getBalanceFare() + "%");

                            mTvChange2.setText(model.getStBalanceFare().equals("-") ? "-" :
                                    model.getStBalanceFare().contains(".") ?
                                            formatFloatNumber(Double.parseDouble(model.getStBalanceFare())) + "%" :
                                            model.getStBalanceFare() + "%"
                            );
//

//                            结算支付及时率
                            mTvSettlement1.setText(model.getRetreatFare().equals("-") ? "-" :
                                    model.getRetreatFare().contains(".") ?
                                            formatFloatNumber(Double.parseDouble(model.getRetreatFare())) + "%" :
                                            model.getRetreatFare() + "%"
                            );
                            mTvSettlement2.setText(model.getStRetreatFare().equals("-") ? "-" :
                                    model.getStRetreatFare().contains(".") ?
                                            formatFloatNumber(Double.parseDouble(model.getStRetreatFare())) + "%" :
                                            model.getStRetreatFare() + "%"
                            );
//
//        private TextView mTvRelation2;//关联及时率标准
//        private TextView mTvRelation1;//关联及时率指标
                            mTvRelation1.setText(model.getTimelyFare().equals("-") ? "-" :
                                    model.getTimelyFare().contains(".") ?
                                            formatFloatNumber(Double.parseDouble(model.getTimelyFare())) + "%" :
                                            model.getTimelyFare() + "%");

                            mTvRelation2.setText(model.getStTimelyFare().equals("-") ? "-" :
                                    model.getStTimelyFare().contains(".") ?
                                            formatFloatNumber(Double.parseDouble(model.getStTimelyFare())) + "%" :
                                            model.getStTimelyFare() + "%");
//
//        private TextView mTvTransaction1;//在线交易指标
//        private TextView mTvTransaction2;//在线交易标准
                            mTvTransaction1.setText(model.getOnlineFare().equals("-") ? "-" :
                                    model.getOnlineFare().contains(".") ?
                                            formatFloatNumber(Double.parseDouble(model.getOnlineFare())) + "%" :
                                            model.getOnlineFare() + "%");

                            mTvTransaction2.setText(model.getStOnlineFare().equals("-") ? "-" :
                                    model.getStOnlineFare().contains(".") ?
                                            formatFloatNumber(Double.parseDouble(model.getStOnlineFare())) + "%" :
                                            model.getStOnlineFare() + "%");
//
//        private TextView mTvBook2;//违规预订率标准
//        private TextView mTvBook1;//违规预订率指标
                            mTvBook1.setText(model.getIllegalFare().equals("-") ? "-" :
                                    model.getIllegalFare().contains(".") ?
                                            formatFloatNumber(Double.parseDouble(model.getIllegalFare())) + "%" :
                                            model.getIllegalFare() + "%");
                            mTvBook2.setText(model.getStIllegalFare().equals("-") ? "-" :
                                    model.getStIllegalFare().contains(".") ?
                                            formatFloatNumber(Double.parseDouble(model.getStIllegalFare())) + "%" :
                                            model.getStIllegalFare() + "%");

//                            tv_endorse_flight_ticket_total_cost.setText("" + parseNumber("" + internationalCost));
                        }
                    }

                    @Override
                    public void fail(String code) {
                        clearIndexData();
                    }
                });

    }

    public static String parseNumber(String number) {
        DecimalFormat df = new DecimalFormat(",###,###.00");
        String format = df.format(new BigDecimal(number));
        if (".00".equals(format)) {
            return "0.00";
        } else {
            return format;
        }
    }

    public static String parseInt(String number) {
        DecimalFormat df = new DecimalFormat(",###,###");
        String format = df.format(new BigDecimal(number));
        if ("".equals(format)) {
            return "0";
        } else {
            return format;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_yeaer:
                yearSelector.show();
                break;
            case R.id.ll_month:
                monthSelector.show();
                break;
            case R.id.company_view:
            case R.id.company_tv:
                if (accountEntityList.size() == 0) {
                    ToastUtil.toast("核算主体列表为空");
                    return;
                }
                showPopupWindows(3, accountEntityList);
                break;

            case R.id.view_depart:
            case R.id.tv_depart:
                if (tv_company.getText().length() == 0) {
                    ToastUtil.toast("请先选择核算主体");
                    return;
                }
                for (int i = 0; i < unitEntityList.size(); i++) {
                    unitEntityList.get(i).put("isShow", "true");
                }
                showPopupWindows(7, unitEntityList);
                break;
        }
    }

    private void showPopupWindows(final int type, final List<Map<String, Object>> datalist) {
        LinearLayout ly_main = (LinearLayout) fragmentView.findViewById(R.id.ly_main);
        //构建一个popupwindow的布局
        popupView = getActivity().getLayoutInflater().inflate(R.layout.popupwindow_select_person, null);
        final PopupWinListAdapter popAdapter = new PopupWinListAdapter(getActivity(), datalist, type);
//        for(int i=0;i<popAdapter.getCount();i++) {
//            datalist.get(i).put("isCheck","false");
//        }
        ListView listView = (ListView) popupView.findViewById(R.id.popupwin_list);
        listView.setAdapter(popAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < popAdapter.getCount(); i++) {
                    datalist.get(i).put("isCheck", "false");
                }
                datalist.get(position).put("isCheck", "true");
                popAdapter.notifyDataSetChanged();
            }
        });

        TextView popuptitle = (TextView) popupView.findViewById(R.id.popup_title);
        if (type == 3) {//核算主体
            popupView.findViewById(R.id.select_person).setVisibility(View.GONE);
            popupView.findViewById(R.id.popupwin_list).setVisibility(View.VISIBLE);
            popuptitle.setText("选择核算主体");
            TextView tv_ok = (TextView) popupView.findViewById(R.id.select_person_ok);
            tv_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < datalist.size(); i++) {
                        Map<String, Object> tmpEntity = datalist.get(i);
                        if ((tmpEntity.get("isCheck") + "").equals("true")) {
                            selectAccountEntity = tmpEntity;
                        }
                    }
                    if (selectAccountEntity == null) {
                        AlertUtil.show(getActivity(), "请选择核算主体", "确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        }, "取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        }, "选择核算主体");
                        return;
                    }
                    window.dismiss();
                    tv_company.setText(selectAccountEntity.get("companyName") + "");
                    selectCompanyStr = selectAccountEntity.get("companyName") + "";

                    List<Map<String, Object>> tmpAccountEntityList = new ArrayList<>();
                    for (int j = 0; j < accountEntityList.size(); j++) {
//                        tmpAccountEntityList.add(accountEntityList.get(i));
                        Map<String, Object> map = accountEntityList.get(j);
                        String mapCompanyName = null == map.get("companyName") ? "" : map.get("companyName").toString();
                        if (mapCompanyName.equals(selectAccountEntity.get("companyName") + "")) {
                            accountEntityList.get(j).put("isCheck", "true");

                        } else {
                            accountEntityList.get(j).put("isCheck", "false");
                        }
                    }
//                            accountEntityList;
//                    for (int i = 0; i < tmpAccountEntityList.size(); i++) {
//                        Map<String, Object> map = tmpAccountEntityList.get(i);
//                        String mapCompanyName = null == map.get("companyName") ? "" : map.get("companyName").toString();
//                        if (mapCompanyName.equals(selectAccountEntity.get("companyName") + "")) {
//                            tmpAccountEntityList.get(i).put("isCheck", "true");
//
//                        } else {
//                            tmpAccountEntityList.get(i).put("isCheck", "false");
//                        }
//                    }
                    List<Map<String, Object>> unitInfo = new ArrayList<>();
                    List<Map<String, Object>> filtrateUnitInfo;
                    filtrateUnitInfo = ((List<Map<String, Object>>) selectAccountEntity.get("unitInfo"));
                    for (int m = 0; m < filtrateUnitInfo.size(); m++) {
                        Map<String, Object> selectUnitInfo = new HashMap<>();
                        selectUnitInfo.put("unitName", filtrateUnitInfo.get(m).get("unitName"));
                        selectUnitInfo.put("unitCode", filtrateUnitInfo.get(m).get("unitCode"));
                        unitInfo.add(selectUnitInfo);

                    }
                    unitEntityList = new ArrayList<>();
                    Map<String, Object> unity = new LinkedTreeMap<>();
                    unity.put("unitName", "全部");
                    unity.put("isCheck", "true");
                    unity.put("isShow", "true");
                    if (unitInfo.size() > 1) {
                        tv_depart.setText("");
                        selectUnitStr = "";
                        selectUnitCode = "";
                        unitEntityList.add(unity);
                    }
                    for (int mm = 0; mm < unitInfo.size(); mm++) {
                        Map<String, Object> tmpInfo = unitInfo.get(mm);
                        if (mm == 0 && unitInfo.size() == 1) {
                            tmpInfo.put("isCheck", "true");
                            tv_depart.setText(unitInfo.get(mm).get("unitName").toString());
                            selectUnitStr = unitInfo.get(mm).get("unitName").toString();
                            selectUnitCode = unitInfo.get(mm).get("unitCode").toString();
                        } else {
                            tmpInfo.put("isCheck", "false");
                        }
                        tmpInfo.put("isShow", "true");
                    }
                    unitEntityList.addAll(unitInfo);
//                    tv_depart.setText("");
//                    selectUnitStr = "";

                    getCompanyData();
                }
            });
        } else if (type == 7) {
            popupView.findViewById(R.id.select_person).setVisibility(View.GONE);
            popupView.findViewById(R.id.popupwin_list).setVisibility(View.VISIBLE);
            popupView.findViewById(R.id.depart_search).setVisibility(View.VISIBLE);
            EditText depart_search_et = (EditText) popupView.findViewById(R.id.depart_search_et);
            TextView depart_search_bt = (TextView) popupView.findViewById(R.id.depart_search_bt);
            depart_search_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String m = depart_search_et.getText().toString().trim();
                    if (!StringUtil.isEmpty(m)) {
                        for (int i = 0; i < datalist.size(); i++) {
                            if (!datalist.get(i).get("unitName").toString().contains(m)) {
                                datalist.get(i).put("isShow", "false");
                            } else {
                                datalist.get(i).put("isShow", "true");
                            }
                        }
                        popAdapter.setList(datalist);
                        popAdapter.notifyDataSetChanged();
                    } else {
                        for (int i = 0; i < datalist.size(); i++) {
                            if (!m.matches(datalist.get(i).get("unitName").toString())) {
                                datalist.get(i).put("isShow", "true");
                            }
                        }
                        popAdapter.setList(datalist);
                        popAdapter.notifyDataSetChanged();
                    }
                }
            });
            TextView tv_ok = (TextView) popupView.findViewById(R.id.select_person_ok);
            popuptitle.setText("选择部门");
            tv_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    window.dismiss();
                    for (int i = 0; i < datalist.size(); i++) {
                        Map<String, Object> tmpEntity = datalist.get(i);
                        if ((tmpEntity.get("isCheck") + "").equals("true")) {
                            selectunitEntity = tmpEntity;
                        }
                    }
                    tv_depart.setText(selectunitEntity.get("unitName") + "");
                    if (selectunitEntity.get("unitName").equals("全部")) {
                        selectUnitStr = "";
                        selectUnitCode = "";
                    } else {
                        selectUnitStr = selectunitEntity.get("unitName") + "";
                        selectUnitCode = selectunitEntity.get("unitCode") + "";
                    }

                    getCompanyData();
                }
            });
        }

        popupView.findViewById(R.id.select_person_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();

            }
        });

        // 创建PopupWindow对象，指定宽度和高度
        int pop_width = (int) (getDeviceWidth() * 0.9);
        window = new PopupWindow(popupView, pop_width, (int) (getDeviceWidth() * 1.1));
        //  设置动画
//                window.setAnimationStyle(R.style.popup_window_anim);
        //设置背景颜色
        window.setBackgroundDrawable(new ColorDrawable(90000000));
        //设置可以获取焦点
        window.setFocusable(true);
        // 设置可以触摸弹出框以外的区域
        window.setOutsideTouchable(true);
        //  更新popupwindow的状态
        window.update();

        popOutShadow(window);
        //  以下拉的方式显示，并且可以设置显示的位置
//                        window.showAsDropDown(inputSearch, 0, 50);
        window.showAtLocation(ly_main, Gravity.CENTER, 0, 0);
    }

    /**
     * 弹窗外阴影
     *
     * @param popupWindow
     */
    private void popOutShadow(PopupWindow popupWindow) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.3f;//设置阴影透明度
        getActivity().getWindow().setAttributes(lp);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            /**当关闭弹窗给筛选条件赋值*/
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 1f;
                getActivity().getWindow().setAttributes(lp);
            }
        });
    }


    public int getDeviceWidth() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        return w_screen;
    }

    @Override
    public void requestSuccess(Object map, int type) {
        if (type == 3) {
            Map<String, Object> result = (Map<String, Object>) map;
            if (null != result) {
                String code = null == result.get("code") ? "" : result.get("code").toString();
                String codeMsg = null == result.get("codeMsg") ? "" : result.get("codeMsg").toString();
                if (code.equals("00000")) {
                    currentPerson = ((List<Map<String, Object>>) result.get("data")).get(0);
                    List<Map<String, Object>> companyList = (List<Map<String, Object>>) currentPerson.get("company");
                    accountEntityList = companyList;

                    if (companyList.size() == 0) {
                        ToastUtil.toast("核算主体列表为空");
                        return;
                    }
                    for (int i = 0; i < accountEntityList.size(); i++) {
                        Map<String, Object> company = companyList.get(i);
                        if (i == 0) {
                            company.put("isCheck", "true");
                            selectAccountEntity = company;
                        } else {
                            company.put("isCheck", "false");
                        }
                    }
                    selectCompany();
                    Map<String, Object> firstCompany = companyList.get(0);
                    unitEntityList = new ArrayList<>();
                    for (int i = 0; i < (((List<Map<String, Object>>) firstCompany.get("unitInfo"))).size(); i++) {
                        Map<String, Object> filtrateUnitInfo = (((List<Map<String, Object>>) firstCompany.get("unitInfo"))).get(i);
                        Map<String, Object> selectUnitInfo = new HashMap<>();
                        selectUnitInfo.put("unitName", filtrateUnitInfo.get("unitName"));
                        selectUnitInfo.put("unitCode", filtrateUnitInfo.get("unitCode"));
                        selectUnitInfo.put("isShow", "true");
                        unitEntityList.add(selectUnitInfo);
                    }

                    if (((List<Map<String, Object>>) firstCompany.get("unitInfo")).size() == 0) {
                        getCompanyData();
                        return;
                    }
                    for (int i = 0; i < unitEntityList.size(); i++) {
                        Map<String, Object> unit = unitEntityList.get(i);
                        if (i == 0 && unitEntityList.size() == 1) {
                            unit.put("isCheck", "true");
                            selectunitEntity = unitEntityList.get(i);
                            tv_depart.setText(selectunitEntity.get("unitName").toString());
                            selectUnitCode = selectunitEntity.get("unitCode").toString();
                        } else {
                            unit.put("isCheck", "false");
                        }
                        unit.put("isShow", "true");
                    }
                    Map<String, Object> unity = new LinkedTreeMap<>();
                    unity.put("unitName", "全部");
                    unity.put("isCheck", "true");
                    unity.put("isShow", "true");
                    if (unitEntityList.size() > 1) {
                        selectUnitCode = "";
                        unitEntityList.add(0, unity);
                        selectunitEntity = unity;
                    }
                    tv_company.setText(firstCompany.get("companyName") + "");
                    selectCompanyStr = tv_company.getText().toString();

//                if (((List<Map<String, Object>>) firstCompany.get("unitInfo")) != null || ((List<Map<String, Object>>) firstCompany.get("unitInfo")).size() > 0) {
//                    Map<String, Object> firstUnit = ((List<Map<String, Object>>) firstCompany.get("unitInfo")).get(0);
//
//                    tv_depart.setText(firstUnit.get("unitName") + "");
//                }

                    selectUnitStr = tv_depart.getText().toString();
                    getCompanyData();
                } else {
                    ToastUtil.toastError(getActivity());
                }
            } else {
                //请求失败
                ToastUtil.toastError(getActivity());
            }
        } else if (type == 12) {
            Map<String, Object> result = (Map<String, Object>) map;
            if (null != result) {
                String code = null == result.get("code") ? "" : result.get("code").toString();
                String codeMsg = null == result.get("codeMsg") ? "" : result.get("codeMsg").toString();
                if (code.equals("00000")) {
                    List<Map<String, Object>> data = (ArrayList) result.get("data");
                    for (int i = 0; i < data.size(); i++) {
                        Map<String, Object> map1 = new HashMap<>();
                        map1.put("companyName", data.get(i).get("companyName").toString());
                        map1.put("companyCode", data.get(i).get("companyCode").toString());
                        map1.put("isCheck", "false");
                        List<Map<String, Object>> unitList = (List<Map<String, Object>>) data.get(i).get("unitInfo");
                        List<Map<String, Object>> unitList2 = new ArrayList<>();
                        for (int j = 0; j < unitList.size(); j++) {
                            Map<String, Object> unitMap = new HashMap<>();
                            unitMap.put("unitName", unitList.get(j).get("UNIT_NAME").toString());
                            unitMap.put("unitCode", unitList.get(j).get("UNIT_CODE").toString());
                            unitList2.add(unitMap);
                        }
                        map1.put("unitInfo", unitList2);
                        accountEntityList.add(map1);
                    }
                    accountEntityList = removeRepeatMapByKey(accountEntityList, "companyName");
                }
            }
        }


    }

    @Override
    public void requestFail(int type) {

    }

    @Override
    public void requestCancel(int type) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Events.CompanyDataHandleChange businessCarHandleChange) {
        getCompanyData();
    }

    private void clearViewData() {
        mTvEndorseTicketDomestic.setText("-");
        mTvEndorseTicketTotal.setText("-");
        mTvEndorseTicketInternational.setText("-");

        mTvEndorseRankPerTotal.setText("-");
        mTvEndorseRankPerDomestic.setText("-");
        mTvEndorseRankPerInternational.setText("-");

        mTvEndorseRankTotal.setText("-");
        mTvEndorseRankDomestic.setText("-");
        mTvEndorseRankInternational.setText("-");

        mTvEndorseRankPerTotal1.setText("-");
        mTvEndorseRankPerDomestic1.setText("-");
        mTvEndorseRankPerInternational1.setText("-");
        mTvTotalCost.setText("¥ 0.0");


//        private TextView mTvEndorseTicketTotal;//国内额总计
//        private TextView mTvEndorseTicketDomestic; //国际机票消费额
//        private TextView mTvEndorseTicketInternational;//酒店消费额
//
//        private TextView mTvEndorseRankPerTotal; // 国内张数
//        private TextView mTvEndorseRankPerDomestic;//国际机票张数
//        private TextView mTvEndorseRankPerInternational;//酒店
//
//        private TextView mTvEndorseRankTotal; //国内平均报价
//        private TextView mTvEndorseRankDomestic;//国际机票报价
//        private TextView mTvEndorseRankInternational; //酒店机票报价
//
//
//        private TextView mTvEndorseRankPerTotal1;//国内退改金额
//        private TextView mTvEndorseRankPerDomestic1;//国际机票退改金额
//        private TextView mTvEndorseRankPerInternational1;//酒店退改金额
//
//        private TextView mTvSettlement1;//结算支付及时率1
//        private TextView mTvSettlement2;//结算支付及时率2
//
//        private TextView mTvChange2;//退改签率标准值
//        private TextView mTvChange1;//退改签率指标值
//
//        private TextView mTvRelation2;//关联及时率2
//        private TextView mTvRelation1;//关联及时率1
//
//        private TextView mTvTransaction1;//在线交易指标
//        private TextView mTvTransaction2;//在线交易标准
//
//        private TextView mTvBook2;//违规预订率2
//        private TextView mTvBook1;//违规预订率1
    }

    private void clearIndexData() {
        mTvSettlement1.setText("-");
        mTvSettlement2.setText("-");

        mTvChange2.setText("-");
        mTvChange1.setText("-");

        mTvRelation2.setText("-");
        mTvRelation1.setText("-");

        mTvTransaction1.setText("-");
        mTvTransaction2.setText("-");

        mTvBook2.setText("-");
        mTvBook1.setText("-");
    }

    public String formatFloatNumber(double value) {
        if (value != 0.00) {
            java.text.DecimalFormat df = new java.text.DecimalFormat("########.00");
            return df.format(value);
        } else {
            return "0.00";
        }

    }

    public String formatFloatNumber(Double value) {
        if (value != null) {
            if (value.doubleValue() != 0.00) {
                java.text.DecimalFormat df = new java.text.DecimalFormat("########.00");
                return df.format(value.doubleValue());
            } else {
                return "0.00";
            }
        }
        return "";
    }

    public List<Map<String, Object>> removeRepeatMapByKey(List<Map<String, Object>> list, String mapKey) {

        //把list中的数据转换成msp,去掉同一id值多余数据，保留查找到第一个id值对应的数据
        List<Map<String, Object>> listMap = new ArrayList<>();
        Map<String, Map> msp = new HashMap<>();
        for (int i = list.size() - 1; i >= 0; i--) {
            Map map = list.get(i);
            String id = (String) map.get(mapKey);
            map.remove(mapKey);
            msp.put(id, map);
        }
        //把msp再转换成list,就会得到根据某一字段去掉重复的数据的List<Map>
        Set<String> mspKey = msp.keySet();
        for (String key : mspKey) {
            Map newMap = msp.get(key);
            newMap.put(mapKey, key);
            listMap.add(newMap);
        }
        return listMap;
    }

}
