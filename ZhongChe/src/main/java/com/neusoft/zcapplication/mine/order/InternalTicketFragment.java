package com.neusoft.zcapplication.mine.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.neusoft.zcapplication.Bean.User;
import com.neusoft.zcapplication.Constant.Constant;
import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.ShowViewActivity;
import com.neusoft.zcapplication.api.OrderApi;
import com.neusoft.zcapplication.base.BaseFragment;
import com.neusoft.zcapplication.entity.GetPayment;
import com.neusoft.zcapplication.flight.inland.InlandFlightOrderDetailActivity;
import com.neusoft.zcapplication.http.NetWorkRequest;
import com.neusoft.zcapplication.http.RequestCallback;
import com.neusoft.zcapplication.http.RequestUtil;
import com.neusoft.zcapplication.http.URL;
import com.neusoft.zcapplication.http.now.CallBack;
import com.neusoft.zcapplication.http.now.RetrofitFactory;
import com.neusoft.zcapplication.tools.AppUtils;
import com.neusoft.zcapplication.tools.DisplayUtil;
import com.neusoft.zcapplication.tools.ToastUtil;
import com.neusoft.zcapplication.widget.BottomDialog;
import com.neusoft.zcapplication.widget.CheckDataPopWindow;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;


/**
 * 国内机票
 */

public class InternalTicketFragment extends BaseFragment implements View.OnClickListener, RequestCallback,
        InternalOrderListAdapter.ClickEvent, CheckDataPopWindow.PopWindowBtnListener, InternalOrderListAdapter.Option {
    private List<Map<String, Object>> dataList;
    private InternalOrderListAdapter adapter;
    private int type = 0, timeSlot = 5;//查询订单参数，订单类型、时间
    private BottomDialog bottomDialog;
    private int sortType = 2;//排序方式，1-按时间升序，2-按时间降序
    private TextView tv_sort_type;
    private View frgView;
    private CheckDataPopWindow popupView;//弹窗

    private Map<String, Object> listItem;//退票点击的item数据
    private int returnIndex = -1;
    private SwipeRefreshLayout swipeRefreshLayout;

    public static InternalTicketFragment getInstance() {
        InternalTicketFragment fragment = new InternalTicketFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (null == frgView) {
            frgView = inflater.inflate(R.layout.frg_internal_order_list, container, false);
            initView();

        } else {
            ViewGroup parent = (ViewGroup) frgView.getParent();
            if (parent != null) {
                parent.removeView(frgView);
            }
        }
        return frgView;
    }

    private void initView() {
        tv_sort_type = (TextView) frgView.findViewById(R.id.tv_sort_type);
        swipeRefreshLayout = (SwipeRefreshLayout) frgView.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        swipeRefreshLayout.setOnRefreshListener(() -> getInternalOrderList());
        frgView.findViewById(R.id.btn_filter).setOnClickListener(this);
        frgView.findViewById(R.id.btn_sort).setOnClickListener(this);
        dataList = new ArrayList<>();
        ListView listView = (ListView) frgView.findViewById(R.id.order_list);
        swipeRefreshLayout.setOnChildScrollUpCallback((parent, child) -> ViewCompat.canScrollVertically(listView, -1));
        adapter = new InternalOrderListAdapter(getActivity(), dataList, 0, this);
        adapter.setClickEvent(this);
        listView.setAdapter(adapter);
        bottomDialog = new
                BottomDialog(getActivity(), this, 2);
    }

    @Override
    protected void initData() {
        super.initData();
        getInternalOrderList();
    }


    public void refresh() {
        getInternalOrderList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_filter:
                bottomDialog.showBottomDialog();
                break;
            case R.id.btn_sort:
                if (sortType == 1) {
                    tv_sort_type.setText("按日期升序");
                    sortType = 2;
                } else if (sortType == 2) {
                    tv_sort_type.setText("按日期降序");
                    sortType = 1;
                }
                sortByDate();
                adapter.setList(dataList);
                adapter.notifyDataSetChanged();
                break;
            case R.id.radio_order0:
                type = 0;
                bottomDialog.checkItem(1, v.getId());
                break;
            case R.id.radio_order1:
                type = 1;
                bottomDialog.checkItem(1, v.getId());
                break;
            case R.id.radio_order2:
                type = 2;
                bottomDialog.checkItem(1, v.getId());
                break;
            case R.id.radio_order3:
                type = 3;
                bottomDialog.checkItem(1, v.getId());
                break;
            case R.id.radio_time0:
                timeSlot = 0;
                bottomDialog.checkItem(2, v.getId());
                break;
            case R.id.radio_time1:
                timeSlot = 1;
                bottomDialog.checkItem(2, v.getId());
                break;
            case R.id.radio_time2:
                timeSlot = 2;
                bottomDialog.checkItem(2, v.getId());
                break;
            case R.id.radio_time3:
                timeSlot = 3;
                bottomDialog.checkItem(2, v.getId());
                break;
            case R.id.radio_time4:
                timeSlot = 4;
                bottomDialog.checkItem(2, v.getId());
                break;
            case R.id.radio_time5:
                timeSlot = 5;
                bottomDialog.checkItem(2, v.getId());
                break;
            case R.id.btn_ok:
                bottomDialog.dismiss();
                getInternalOrderList();
                break;
            case R.id.btn_cancel:
                bottomDialog.dismiss();
                break;
        }
    }

    /**
     * 获取国内机票订单列表
     */
    private void getInternalOrderList() {

//        try {
//            JSONObject obj = new JSONObject();
//            obj.put("abc", 8898);
//            obj.put("cba", 8898.0);
//            Map<String, Object> stringObjectMap = JSONUtils.gsonToMaps(obj.toString());
//            LogUtil.d("SUNYUAN" + stringObjectMap.get("abc"));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        User user = AppUtils.getUserInfo(mActivity);
        Map<String, Object> params = new HashMap<>();
        params.put("ciphertext", "test");
        params.put("employeeCode", user.getEmployeeCode());
        params.put("loginType", URL.LOGIN_TYPE);
        params.put("timeSlot", timeSlot);
        params.put("type", type);
        Retrofit retrofit = new RequestUtil().getRequestClient(URL.ZHONGCHE_93);
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        Call<Map<String, Object>> call = request.getInternalOrderList(params);
        new RequestUtil().requestData(call, this, 1, getContext());
    }

    /**
     * 按时间排序
     */
    private void sortByDate() {
        Collections.sort(dataList, new SortByDate());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1010) {
            getInternalOrderList();
        }
    }


    @Override
    public void requestSuccess(Object map, int type) {
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }

        Map<String, Object> result = (Map<String, Object>) map;
        if (type == 1) {
            if (null != result) {
                String code = null == result.get("code") ? "" : result.get("code").toString();
                String codeMsg = null == result.get("codeMsg") ? "" : result.get("codeMsg").toString();
                List<Map<String, Object>> data = (ArrayList) result.get("data");
                if (code.equals("00000")) {
                    dataList = data;
                    sortByDate();
                    adapter.setList(dataList);
                    adapter.notifyDataSetChanged();
                } else {
                    if (null != getActivity()) {
                        ToastUtil.toastError(getActivity());
                    }
                }
            } else {
                //请求失败
                if (null != getActivity()) {
                    ToastUtil.toastError(getActivity());
                }
            }
        } else if (type == 2) {
            //退票成功
            if (null != result) {
                String code = null == result.get("code") ? "" : result.get("code").toString();
                String codeMsg = null == result.get("codeMsg") ? "" : result.get("codeMsg").toString();
                if ("00000".equals(code)) {
                    ToastUtil.toastNeedData(getActivity(), "退票成功！");
                    //修改状态
                    List<Map<String, Object>> list = adapter.getList();
                    list.get(returnIndex).put("ORDERSTATE", "退票中");
                    adapter.setList(list);
                    adapter.notifyDataSetChanged();
//                String statusStr = null == item.get("ORDERSTATE") ? ""  : item.get("ORDERSTATE").toString();
                } else if ("40001".equals(code)) {
                    ToastUtil.toastNeedData(getActivity(), "退票失败：" + codeMsg);
                } else {
                    ToastUtil.toastNeedData(getActivity(), "退票失败，请稍候再试！");
                }
            } else {
                //请求失败
                ToastUtil.toastNeedData(getActivity(), "退票失败，请稍候再试！");
            }
        }
    }


    @Override
    public void requestFail(int type) {
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
        if (type == 1) {
            if (null != getActivity()) {
                ToastUtil.toastFail(getActivity());
            }
        } else if (type == 2) {
            //退票
            if (null != getActivity()) {
                ToastUtil.toastNeedData(getActivity(), "退票失败，请稍候再试！");
            }
        }
    }

    @Override
    public void requestCancel(int type) {
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    /**
     * 改签
     *
     * @param item
     */
    @Override
    public void changeOrder(Map<String, Object> item) {
        String supplierId = null == item.get("SUPPLIERID") ? "" : item.get("SUPPLIERID").toString();
        if (!TextUtils.isEmpty(supplierId) && supplierId.contains(".")) {
            supplierId = supplierId.substring(0, supplierId.indexOf("."));
        }
        String orderId = null == item.get("ORDERID") ? "" : item.get("ORDERID").toString();
        Intent intent = new Intent(getActivity(), ShowViewActivity.class);
//        Bundle bundle = new Bundle();
//        BunbleParam bunbleParam = new BunbleParam();
//        bunbleParam.setMap(item);
//        bundle.putSerializable("orderItem",bunbleParam);
        intent.putExtra("orderItem", (Serializable) item);
//        intent.putExtras(bundle);
        String startDate = item.get("FROMPLANDATE") + "";
        startDate = startDate.substring(0, 10);
        //判断订单乘机日期若为过去的时间，则将日期设为当前日期
        /*if(DateUtils.isPaseByTime(startDate)){
            startDate = DateUtils.getDate(0);
        }*/
        String userTel = "";
        int payType = (null == item.get("PAYTYPE") || "".equals(item.get("PAYTYPE").toString())) ? 0 : Double.valueOf(item.get("PAYTYPE").toString()).intValue();
        try {
            if (item.get("TELEPHONE") != null) {
                BigDecimal bd = new BigDecimal(item.get("TELEPHONE") + "");
                userTel = bd.toPlainString();
            }
        } catch (Exception e) {
            userTel = item.get("TELEPHONE") + "";
        }
        User user = new AppUtils().getUserInfo(getActivity());
        String flightNo = item.get("FLIGHTNO").toString();
        String cpcode = flightNo.substring(0, 2);
        String formCityCode = "";
        String toCityCode = "";
        if ("PEK".equals("" + item.get("FROMCITYCODE"))) {
            formCityCode = "222";
        } else if ("SHA".equals("" + item.get("FROMCITYCODE"))) {
            formCityCode = "111";
        } else {
            formCityCode = "" + item.get("FROMCITYCODE");
        }
        if ("PEK".equals("" + item.get("TOCITYCODE"))) {
            toCityCode = "222";
        } else if ("SHA".equals("" + item.get("TOCITYCODE"))) {
            toCityCode = "111";
        } else {
            toCityCode = "" + item.get("TOCITYCODE");
        }

//      个人票价 需要拼接在改签url后面 map.put("price", listItem.get("TICKETPRICE"));

        String url = Constant.ZHONGCHE_H5 + "oneWayFlightList.html?" +
                "departCity=" + item.get("FROMCITYNAME") +
                "&departCityCode=" + formCityCode +
                "&reachCity=" + item.get("TOCITYNAME") +
                "&reachCityCode=" + toCityCode +
                "&startDate=" + startDate +
                "&billNo=" + item.get("ORDERAPPLYID") +
                "&tripType=0" +
                "&cptxt=string&userNo=" + item.get("APPLICANTID") +
                "&userName=" + item.get("APPLICANTNAME") +
                "&userTel=" + userTel +
                "&userEmail=" + item.get("EMAIL") +
                "&userId=" + user.getIdCard() +
                "&loginUserNo=" + item.get("EMPLOYEECODE") +
                "&loginUserName=" + item.get("EMPLOYEENAME") +
                "&isChange=1&cpcode=" + cpcode +
                "&payType=" + payType +
                "&style=" + supplierId +
                "&OrderGuid=" + orderId +
                "&OrderticketPrice=" + item.get("TICKETPRICE");
//        Log.i("--->","改签：" + item);
        intent.putExtra("url", url);
        intent.putExtra("payType", "payMent");
        intent.putExtra("hasPay", true);
        startActivityForResult(intent, 1010);
    }

    /**
     * 选择退票原因，确定按钮点击事件
     *
     * @param reasonMap
     * @param position
     */
    @Override
    public void queryClick(Map<String, Object> reasonMap, int position) {
        if (null == reasonMap) {
            ToastUtil.toastNeedData(getActivity(), "请选择退票原因~");
            return;
        }
        String opRemark = reasonMap.get("reason").toString();
        User user = new AppUtils().getUserInfo(getActivity());
        Map<String, Object> params = new HashMap<>();
//        params.put("applicantId",user.getEmployeeCode());
        params.put("applicantId", listItem.get("APPLICANTID"));//乘机人id
        params.put("cabin", listItem.get("CABIN"));//舱位
        params.put("ciphertext", "test");
        params.put("opRemark", opRemark);//退改原因
//        params.put("companyId", "0");
        params.put("contactName", listItem.get("EMPLOYEENAME"));
        params.put("loginType", URL.LOGIN_TYPE);
        String mobileStr;
        if (listItem.get("TELEPHONE") != null) {
            BigDecimal bd = new BigDecimal(listItem.get("TELEPHONE") + "");
            mobileStr = bd.toPlainString();
        } else {
            mobileStr = "";
        }
        params.put("mobile", mobileStr);
        params.put("originalOrderNo", listItem.get("ORDERID") + "");
        params.put("passengerType", "成人");
        int[] segmentList = {1};
        params.put("segmentList", segmentList);
        String style = listItem.get("SUPPLIERID") + "";
        params.put("style", style.substring(0, style.indexOf(".")));
        params.put("userName", listItem.get("EMPLOYEENAME"));
        params.put("userNo", listItem.get("EMPLOYEECODE"));

        Map<String, Object> map = new HashMap<>();
        map.put("carrierName", listItem.get("CARRIERNAME"));
        map.put("costConstruction", listItem.get("COSTCONSTRUCTION"));
        map.put("flightNo", listItem.get("FLIGHTNO"));
        map.put("fromCityName", listItem.get("FROMCITYNAME"));
        map.put("fromCityCode", listItem.get("FROMCITYCODE"));
        map.put("fromDate", listItem.get("FROMDATE"));
        map.put("fromPlanDate", listItem.get("FROMPLANDATE"));
        map.put("fuelCost", listItem.get("FUELCOST"));
//        是否违规：0：否 1：是 ,
        Object isIllegal = null == listItem.get("ISILLEGAL") ? 0 : listItem.get("ISILLEGAL");
        map.put("isIllegal", isIllegal);
        //增加提交字段,操作退票人的员工号
//        map.put("applicantId",user.getEmployeeCode());
        double type = null == listItem.get("type") ? 0 : (double) listItem.get("type");// type为1 传1，2 传0；
        if (type == 1) {
            map.put("isOldReturn", 1);//该订单未改签
        } else {
            map.put("isOldReturn", 0);//该订单已改签
        }
        map.put("isShare", 0);
        map.put("orderApplyId", listItem.get("ORDERAPPLYID"));
        map.put("originalPrice", listItem.get("ORIGINALPRICE"));
        map.put("price", listItem.get("TICKETPRICE"));
        String supplierId = style.substring(0, style.indexOf("."));
        if (8 == Integer.parseInt(supplierId)) {
            map.put("servicePrice", listItem.get("PRICE"));
        } else {
            map.put("servicePrice", listItem.get("SERVICEPRICE"));
        }
        map.put("supplierId", supplierId);
        map.put("toCityName", listItem.get("TOCITYNAME"));
        map.put("toCityCode", listItem.get("TOCITYCODE"));
        map.put("toDate", listItem.get("TODATE"));
        map.put("toPlanDate", listItem.get("TOPLANDATE"));
        params.put("domesticOrderReturnModel", map);
        String fromCityCode = null == listItem.get("FROMCITYCODE") ? "" : listItem.get("FROMCITYCODE").toString();
        String toCityCode = null == listItem.get("TOCITYCODE") ? "" : listItem.get("TOCITYCODE").toString();
        params.put("fromCityCode", fromCityCode);
        params.put("toCityCode", toCityCode);
        String returnChangeRule = null == listItem.get("RETURNCHANGERULE") ? "" : listItem.get("RETURNCHANGERULE").toString();
        //退改签规则
        params.put("returnChangeRule", returnChangeRule);
//        Log.i("--->",";;退票传输参数：" + params);
        Retrofit retrofit = new RequestUtil().getRequestClient(URL.ZHONGCHE_93);
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        Call<Map<String, Object>> call = request.returnOrder(params);
        new RequestUtil().requestData(call, this, 2, "退票中...", true, getActivity());
    }

    /**
     * 退票
     *
     * @param item
     * @param position
     */
    @Override
    public void refundOrder(Map<String, Object> item, int position) {
//        double supplierId = null == item.get("SUPPLIERID") ? 0 : (double) item.get("SUPPLIERID");
//        if (8 == supplierId) {
//            AlertUtil.show(mContext, "抱歉，泛嘉机票暂不支持在线退改签；\n如需退改，请拨打客服热线：\n400-600-9666", "确定", null);
//            return;
//        }
        listItem = item;
        returnIndex = position;
        //退票原因数据
        List<Map<String, Object>> reasonList = new ArrayList<>();
        Map<String, Object> rm1 = new HashMap<>();
        rm1.put("reason", "行程变更或者改期。");
        rm1.put("isCheck", "false");
        Map<String, Object> rm2 = new HashMap<>();
//        rm2.put("reason","<strong><font color=#c70019>航班延误</font></strong>、" +
//                "<strong><font color=#c70019>取消</font></strong>等" +
//                "<strong><font color=#c70019>非自愿退票</font></strong>或者改期；");
        rm2.put("reason", "航班延误、" +
                "取消等" +
                "非自愿退票或者改期；");
        rm2.put("isCheck", "false");
        reasonList.add(rm1);
        reasonList.add(rm2);

        int winWidth = DisplayUtil.getDeviceWidth(getActivity());
        int pop_width = (int) (winWidth * 0.9);
        int popHeight = (int) (winWidth * 1.1);
        popupView = new CheckDataPopWindow(getActivity(), reasonList, 10, 0, position, pop_width, popHeight);
        //显示窗口
        popupView.showAtLocation(frgView.findViewById(R.id.frg_inter_order_root), Gravity.CENTER, 0, 0);
        popupView.setBtnClickListener(this);

        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.4f;
        getActivity().getWindow().setAttributes(lp);
    }

    @Override
    public void pay(int position, String changeFlag, String orderApplyId, String orderCode) {
        Map<String, Object> params = new HashMap<>();
        params.put("ciphertext", "test");
        params.put("loginType", Constant.APP_TYPE);
        params.put("type", 1);
        params.put("changeFlag", changeFlag);
        params.put("orderApplyId", orderApplyId);
        params.put("orderCode", orderCode);
        showLoading();
        RetrofitFactory.getInstance().createApi(OrderApi.class).payment(params)
                .enqueue(new CallBack<GetPayment>() {
                    @Override
                    public void success(GetPayment getPayment) {
                        dismissLoading();
                        Map<String, Object> itemList = adapter.getList().get(position);
                        boolean isFanJiaOrder = AppUtils.isFanJiaOrder(itemList);
                        String url = getPayment.getUrl();
                        if (TextUtils.isEmpty(url)) {
                            return;
                        }
                        if (isFanJiaOrder) {
                            InlandFlightOrderDetailActivity.startActivity(mActivity, url, InlandFlightOrderDetailActivity.ORDER_LIST_PAY_WHEN);
                            return;
                        }
                        Intent intent = new Intent(mContext, ShowViewActivity.class);
                        intent.putExtra("url", url);
                        intent.putExtra("payType", "payMent");
                        intent.putExtra("hasPay", true);
                        startActivityForResult(intent, 1010);
                    }

                    @Override
                    public void fail(String code) {
                        dismissLoading();
                    }
                });
    }

    class SortByDate implements Comparator {
        public int compare(Object o1, Object o2) {
            Map<String, Object> m1 = (Map<String, Object>) o1;
            Map<String, Object> m2 = (Map<String, Object>) o2;
            String firstDay = null == m1.get("FROMPLANDATE") ? "" : m1.get("FROMPLANDATE").toString();
            String secondDay = null == m2.get("FROMPLANDATE") ? "" : m2.get("FROMPLANDATE").toString();

            if (firstDay == null && secondDay == null)
                return 0;
            if (firstDay == null)
                return -1;
            if (secondDay == null)
                return 1;
            if (firstDay.equals("")) {
                return -1;
            }
            if (secondDay.equals("")) {
                return 1;
            }
            //boolean result = DateUtils.isFirstDayBeforeSecondDay(firstDay.substring(0,10),secondDay.substring(0,10));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            try {
                Date d1 = sdf.parse(firstDay);
                calendar.setTime(d1);
                long time1 = calendar.getTimeInMillis();
                Date d2 = sdf.parse(secondDay);
                calendar.setTime(d2);
                long time2 = calendar.getTimeInMillis();
                if (sortType == 1) {
                    return Long.valueOf(time1).compareTo(Long.valueOf(time2));
                } else {
                    return Long.valueOf(time2).compareTo(Long.valueOf(time1));
                }
            } catch (ParseException e) {
                if (sortType == 1) {
                    return 1;
                } else {
                    return -1;
                }
            }
            /*if(result) {
                if (sortType == 1) {
                    return -1;
                } else {
                    return 1;
                }
            }else {
                if (sortType == 1) {
                    return 1;
                } else {
                    return -1;
                }
            }*/
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (adapter != null) {
            adapter.cancelAllTimers();
        }
    }


}
