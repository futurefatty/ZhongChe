package com.neusoft.zcapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.neusoft.zcapplication.Bean.User;
import com.neusoft.zcapplication.Constant.Constant;
import com.neusoft.zcapplication.api.OrderApi;
import com.neusoft.zcapplication.approval.ApprovalActivity;
import com.neusoft.zcapplication.base.BaseFragment;
import com.neusoft.zcapplication.event.Events;
import com.neusoft.zcapplication.http.NetWorkRequest;
import com.neusoft.zcapplication.http.RequestCallback;
import com.neusoft.zcapplication.http.RequestUtil;
import com.neusoft.zcapplication.http.URL;
import com.neusoft.zcapplication.http.now.CallBack;
import com.neusoft.zcapplication.http.now.RetrofitFactory;
import com.neusoft.zcapplication.mine.order.FlightSchemesActivity;
import com.neusoft.zcapplication.tools.AppUtils;
import com.neusoft.zcapplication.tools.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * 首页预定申请单tab页面
 */

public class AllOrderFragment extends BaseFragment implements View.OnClickListener, RequestCallback {

    private View allOrderFragment;
    private TextView btn_edit_delete;
    private int navIndex = 0;//tab栏下标 0 国内  1 国际  2 酒店
    private ApplyOrderListAdapter inAdapter, outAdapter, hotelAdapter;
    private List<Integer> delPosition;
    private List<Call<Map<String, Object>>> callList;

    private boolean isEdit;
    private XRefreshView refreshView;

    private final int mPinnedTime = 1000;

    public static AllOrderFragment getInstance() {
        AllOrderFragment fragment = new AllOrderFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (null == allOrderFragment) {
            allOrderFragment = inflater.inflate(R.layout.frg_all_order, container, false);
            initView();
        } else {
            ViewGroup parent = (ViewGroup) allOrderFragment.getParent();
            if (parent != null) {
                parent.removeView(allOrderFragment);
            }
        }
        return allOrderFragment;
    }

    @Override
    protected void initDataRepeat() {
        super.initDataRepeat();
        if (navIndex == 0) {
            queryOrderList(1);//国内机票
        } else if (navIndex == 1) {
            queryOrderList(2);//国际机票
        } else {
            queryOrderList(3);//酒店
        }
    }

    private void initView() {
        AppUtils.setStateBar(getActivity(), allOrderFragment.findViewById(R.id.frg_status_bar));
        callList = new ArrayList<>();
        allOrderFragment.findViewById(R.id.frg_add_bill).setOnClickListener(this);
        allOrderFragment.findViewById(R.id.frg_all_order_inner).setOnClickListener(this);
        allOrderFragment.findViewById(R.id.frg_all_order_out).setOnClickListener(this);
        allOrderFragment.findViewById(R.id.frg_all_order_hotel).setOnClickListener(this);
        btn_edit_delete = (TextView) allOrderFragment.findViewById(R.id.btn_edit_delete);
        btn_edit_delete.setOnClickListener(this);


//        refreshView = (XRefreshView) allOrderFragment.findViewById(R.id.contentView);
//        refreshView.setPullRefreshEnable(true);
//        // 设置是否可以上拉加载
//        refreshView.setPullLoadEnable(false);
//        refreshView.setCustomHeaderView(new CustomHeader(getActivity(),mPinnedTime));
//        // 设置上次刷新的时间
//        //当下拉刷新被禁用时，调用这个方法并传入false可以不让头部被下拉
//        refreshView.setMoveHeadWhenDisablePullRefresh(true);
//        // 设置时候可以自动刷新
//        refreshView.setAutoRefresh(false);
//        refreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
//            @Override
//            public void onRefresh(boolean isPullDown) {
//                if (navIndex == 0){
//                    queryOrderList(1);//国内机票
//                }else if(navIndex == 1){
//                    queryOrderList(2);//国际机票
//                }else{
//                    queryOrderList(3);//酒店
//                }
//            }
//
//            @Override
//            public void onLoadMore(boolean isSilence) {
//
//            }
//
//            @Override
//            public void onRelease(float direction) {
//                super.onRelease(direction);
//                if (direction > 0) {
//                } else {
//                }
//            }
//        });
        ListView inListView = (ListView) allOrderFragment.findViewById(R.id.frg_apply_order_list_in);
        ListView outListView = (ListView) allOrderFragment.findViewById(R.id.frg_apply_order_list_out);
        ListView hotelListView = (ListView) allOrderFragment.findViewById(R.id.frg_apply_order_list_hotel);

        List<Map<String, Object>> list = new ArrayList<>();
        inAdapter = new ApplyOrderListAdapter(getActivity(), list, 0);
        inListView.setAdapter(inAdapter);
        inListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (inAdapter.getType() == 1) {
                    Map<String, Object> item = inAdapter.getItem(position);
                    if (null == item.get("isChecked") || "no".equals(item.get("isChecked"))) {
                        item.put("isChecked", "yes");
                    } else {
                        item.put("isChecked", "no");
                    }
                    List<Map<String, Object>> list = inAdapter.getList();
                    list.set(position, item);
                    inAdapter.setList(list);
                    inAdapter.notifyDataSetChanged();
                } else {
                    Map<String, Object> map = inAdapter.getItem(position);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("map", (Serializable) map);
                    startActivity(ApplyAdvanceOrderDetailActivity.class, bundle);
                }
            }
        });

        List<Map<String, Object>> outList = new ArrayList<>();
        outAdapter = new ApplyOrderListAdapter(getActivity(), outList, 0);
        outListView.setAdapter(outAdapter);
        outListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (outAdapter.getType() == 1) {
                    //选择Item操作
                    Map<String, Object> item = outAdapter.getItem(position);
                    if (null == item.get("isChecked") || "no".equals(item.get("isChecked"))) {
                        item.put("isChecked", "yes");
                    } else {
                        item.put("isChecked", "no");
                    }
                    List<Map<String, Object>> list = outAdapter.getList();
                    list.set(position, item);
                    outAdapter.setList(list);
                    outAdapter.notifyDataSetChanged();
                } else {
                    //查看详情
//                    Intent intent;
//                    String isBuy = outAdapter.getList().get(position).get("isBuy")+"";//1已经确定了方案
//                    String isPlan = outAdapter.getList().get(position).get("isPlan")+"";
//                    if ("0.0".equals(isPlan)&&"1.0".equals(isBuy)){
//                        //
//                        intent = new Intent(getActivity(),FlightSchemes2Activity.class);
//                        intent.putExtra("combineId", outAdapter.getList().get(position).get("COMBINEID")+"");
//                        intent.putExtra("APPLICANTID", outAdapter.getList().get(position).get("applicateId")+"");
////                        intent.putExtra("BUYID", outAdapter.getList().get(position).get("BUYID")+"");
//                        startActivity(intent);
//                    }else {
//                        intent = new Intent(getActivity(),FlightSchemesActivity.class);
//                        intent.putExtra("combineId", outAdapter.getList().get(position).get("COMBINEID")+"");
//                        intent.putExtra("APPLICANTID", outAdapter.getList().get(position).get("applicateId")+"");
//                        startActivityForResult(intent,1001);
//                    }

//1001                    Intent intent = new Intent(getActivity(), FlightSchemesActivity.class);
//                    Map<String, Object> map = outAdapter.getList().get(position);
////                    double supplerId = null == map.get("SUPPLIERID") ? -1.0 :(double)map.get("SUPPLIERID");//供应商id
////                    int supplerIdInt = (int) supplerId;
//                    String combineId = null == map.get("COMBINEID") ? "" : map.get("COMBINEID").toString();//国际机票订单综合编号
//                    String applicantId = null == map.get("applicateId") ? "" : map.get("applicateId").toString();//提交该申请单的用户的员工编号
//
//                    double isBuy = null == map.get("isBuy") ? 0.0 : (double) map.get("isBuy");//供应商id
//                    int isBuyInt = (int) isBuy;
//                    double type = null == map.get("type") ? 1.0 : (double) map.get("type");//
//                    int typeInt = (int) type;
//                    String orderStateName = null == map.get("ORDERSTATENAME") ? "" : map.get("ORDERSTATENAME").toString();
//
//                    intent.putExtra("combineId", combineId);
////                    intent.putExtra("supplerId", "-1");//这里没有供应商id，可不传，默认为-1
//                    intent.putExtra("applicantId", applicantId);
//                    intent.putExtra("isBuy", isBuyInt);
//                    intent.putExtra("type", typeInt);
//                    intent.putExtra(FlightSchemesActivity.IS_ORDER_APPLY, true);
//                    intent.putExtra("orderStateName", orderStateName);
//                    startActivityForResult(intent, 1001);
                    Map<String, Object> map = outAdapter.getItem(position);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("map", (Serializable) map);
                    startActivity(ApplyAdvanceOrderDetailActivity.class, bundle);
                }
            }
        });
        List<Map<String, Object>> hotelList = new ArrayList<>();
        hotelAdapter = new ApplyOrderListAdapter(getActivity(), hotelList, 0);
        hotelAdapter.setDataType(1);
        hotelListView.setAdapter(hotelAdapter);
        hotelListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (hotelAdapter.getType() == 1) {
                    Map<String, Object> item = hotelAdapter.getItem(position);
                    if (null == item.get("isChecked") || "no".equals(item.get("isChecked"))) {
                        item.put("isChecked", "yes");
                    } else {
                        item.put("isChecked", "no");
                    }
                    List<Map<String, Object>> list = hotelAdapter.getList();
                    list.set(position, item);
                    hotelAdapter.setList(list);
                    hotelAdapter.notifyDataSetChanged();
                } else {
                    Map<String, Object> map = hotelAdapter.getItem(position);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("map", (Serializable) map);
                    startActivity(HotelApplyAdvanceOrderDetailActivity.class, bundle);
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        //取消加载数据
        for (Call<Map<String, Object>> call : callList) {
            if (call.isExecuted()) {
                call.cancel();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (navIndex == 0) {
            queryOrderList(1);//国内机票
        } else if (navIndex == 1) {
            queryOrderList(2);//国际机票
        } else {
            queryOrderList(3);//酒店
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frg_add_bill:
                Intent intent = new Intent(getActivity(), ApprovalActivity.class);
                intent.putExtra("navIndex", navIndex);//0 当前选择的是国内机票，1国际机票
                startActivityForResult(intent, 2001);
                break;
            case R.id.frg_all_order_inner:
                navIndex = 0;
                if (isEdit) {
                    List<Map<String, Object>> list = inAdapter.getList();
                    List<Map<String, Object>> newList = new ArrayList<>();
                    for (int i = 0; i < list.size(); i++) {
                        Map<String, Object> item = list.get(i);
                        item.put("isChecked", "no");
                        newList.add(item);
                    }
                    inAdapter.setType(0);
                    inAdapter.setList(newList);
                    inAdapter.notifyDataSetChanged();
                }
                isEdit = false;
                btn_edit_delete.setText("编辑");
                btn_edit_delete.setVisibility(View.VISIBLE);
                setNavBarStatus(navIndex);
                inAdapter.setType(0);
                inAdapter.notifyDataSetChanged();
                queryOrderList(1);//国内机票
                break;
            case R.id.frg_all_order_out:
                navIndex = 1;
                isEdit = false;
                btn_edit_delete.setText("编辑");
                btn_edit_delete.setVisibility(View.INVISIBLE);
                setNavBarStatus(navIndex);
                outAdapter.setType(0);
                outAdapter.notifyDataSetChanged();
                queryOrderList(2);//国际机票
                break;
            case R.id.frg_all_order_hotel:
                navIndex = 2;
                if (isEdit) {
                    List<Map<String, Object>> list = hotelAdapter.getList();
                    List<Map<String, Object>> newList = new ArrayList<>();
                    for (int i = 0; i < list.size(); i++) {
                        Map<String, Object> item = list.get(i);
                        item.put("isChecked", "no");
                        newList.add(item);
                    }
                    hotelAdapter.setType(0);
                    hotelAdapter.setList(newList);
                    hotelAdapter.notifyDataSetChanged();
                }
                isEdit = false;
                btn_edit_delete.setText("编辑");
                btn_edit_delete.setVisibility(View.VISIBLE);
                setNavBarStatus(navIndex);
                hotelAdapter.setType(0);
                hotelAdapter.notifyDataSetChanged();
                queryOrderList(3);//酒店
                break;
            case R.id.btn_edit_delete:
                if (!isEdit) {
                    isEdit = true;
                    btn_edit_delete.setText("删除");
                    if (navIndex == 0) {//国内
                        inAdapter.setType(1);
                        inAdapter.notifyDataSetChanged();
                    } else {
                        hotelAdapter.setType(1);
                        hotelAdapter.notifyDataSetChanged();
                    }

                } else {
                    List<String> deleteIds = new ArrayList<>();
                    List<String> orderApplyIdList = new ArrayList<>();
                    List<Map<String, Object>> delList;
                    List<Integer> position = new ArrayList<>();
                    if (navIndex == 0) {//国内
                        delList = inAdapter.getList();
                        for (int i = 0; i < delList.size(); i++) {
                            Map<String, Object> item = delList.get(i);
                            if (null != item.get("isChecked") && "yes".equals(item.get("isChecked"))) {
                                String id = item.get("id") + "";
                                String orderApplyId = null == item.get("orderApplyId") ?
                                        "" : item.get("orderApplyId").toString();
                                deleteIds.add(id.substring(0, id.indexOf(".")));
                                orderApplyIdList.add(orderApplyId);
                                position.add(i);
                            }
                        }
                    } else {//酒店
                        delList = hotelAdapter.getList();
                        for (int i = 0; i < delList.size(); i++) {
                            Map<String, Object> item = delList.get(i);
                            if (null != item.get("isChecked") && "yes".equals(item.get("isChecked"))) {
                                String id = item.get("id") + "";
                                deleteIds.add(id.substring(0, id.indexOf(".")));
                                String orderApplyId = null == item.get("orderApplyId") ?
                                        "" : item.get("orderApplyId").toString();
                                orderApplyIdList.add(orderApplyId);
                                position.add(i);
                            }
                        }
                    }
                    if (deleteIds.size() == 0) {
                        /*AlertUtil.show2(getActivity(), "请选择您想要删除的预订申请单", "确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        });*/
                        //没有选择切回未编辑状态
                        isEdit = false;
                        btn_edit_delete.setText("编辑");
                        List<Map<String, Object>> list = inAdapter.getList();
                        List<Map<String, Object>> newList = new ArrayList<>();
                        for (Map<String, Object> item : list) {
                            item.put("isChecked", "no");
                            newList.add(item);
                        }
                        inAdapter.setList(newList);
                        inAdapter.setType(0);
                        inAdapter.notifyDataSetChanged();

                        List<Map<String, Object>> outList = hotelAdapter.getList();
                        List<Map<String, Object>> newOutList = new ArrayList<>();
                        for (Map<String, Object> item : outList) {
                            item.put("isChecked", "no");
                            newOutList.add(item);
                        }
                        hotelAdapter.setList(newOutList);
                        hotelAdapter.setType(0);
                        hotelAdapter.notifyDataSetChanged();
                        return;
                    }
                    delPosition = position;
                    deleteOrder(navIndex + 1, deleteIds, orderApplyIdList);
                }
                break;
        }
    }

    /**
     * 查询预定申请单列表数据
     *
     * @param dataType //0:全部，1：国内机票，2：国际机票，3：酒店
     */
    private void queryOrderList(final int dataType) {
        //如果当前选中的是预定申请单fragment,则提示获取数据成功或失败
        Map<String, Object> params = new HashMap<>();
        User user = AppUtils.getUserInfo(getActivity());
        String employeeCode = user.getEmployeeCode();
        params.put("ciphertext", "test");
        params.put("employeeCode", employeeCode);
        params.put("loginType", URL.LOGIN_TYPE);
        params.put("type", dataType);
        Retrofit retrofit = new RequestUtil().getRequestClient(URL.ZHONGCHE_93);
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        Call<Map<String, Object>> call = request.getOrderList(params);
        callList.add(call);
        new RequestUtil().requestData(call, this, dataType, getContext());

    }

    /**
     * 删除预订申请单
     *
     * @param type 1-国内，2-国际
     * @param ids  订单id集合
     */
    private void deleteOrder(int type, List<String> ids, final List<String> orderApplyId) {
        Map<String, Object> params = new HashMap<>();
        User user = AppUtils.getUserInfo(getActivity());
        params.put("ciphertext", "test");
        params.put("id", ids);
        params.put("orderApplyId", orderApplyId);
        params.put("loginType", Constant.APP_TYPE);
        params.put("employeeCode", user.getEmployeeCode());
        params.put("type", type);

        /*Retrofit retrofit = new RequestUtil().getRequestClient(URL.ZHONGCHE_93);
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        Call<Map<String,Object>> call = request.deleteOrder(params);
        new RequestUtil().requestData(call,this,3,getContext());*/
        showLoading();
        RetrofitFactory.getInstance().createApi(OrderApi.class).deleteOrder(params)
                .enqueue(new CallBack<Object>() {
                    @Override
                    public void success(Object object) {
                        dismissLoading();
                        if ((navIndex + 1) == 1) {
                            List<Map<String, Object>> list = inAdapter.getList();
                            List<Map<String, Object>> newList = new ArrayList<>();
                            for (int i = 0; i < list.size(); i++) {
                                if (!delPosition.contains(i)) {
                                    Map<String, Object> item = list.get(i);
                                    item.put("isChecked", "no");
                                    newList.add(item);
                                }
                            }
                            inAdapter.setType(0);
                            inAdapter.setList(newList);
                            inAdapter.notifyDataSetChanged();
                        } else {
                            List<Map<String, Object>> list = hotelAdapter.getList();
                            List<Map<String, Object>> newList = new ArrayList<>();
                            for (int i = 0; i < list.size(); i++) {
                                if (!delPosition.contains(i)) {
                                    Map<String, Object> item = list.get(i);
                                    item.put("isChecked", "no");
                                    newList.add(item);
                                }
                            }
                            hotelAdapter.setType(0);
                            hotelAdapter.setList(newList);
                            hotelAdapter.notifyDataSetChanged();
                        }
                        isEdit = false;
                        btn_edit_delete.setText("编辑");
                        EventBus.getDefault().post(new Events.DeleteOrderIds(orderApplyId));
                    }

                    @Override
                    public void fail(String code) {
                        dismissLoading();
                    }
                });
    }

    /**
     * 设置标签
     *
     * @param index
     */
    private void setNavBarStatus(int index) {
        navIndex = index;
        TextView innerTv = (TextView) allOrderFragment.findViewById(R.id.frg_all_order_inner);
        TextView outTv = (TextView) allOrderFragment.findViewById(R.id.frg_all_order_out);
        TextView hotelTv = (TextView) allOrderFragment.findViewById(R.id.frg_all_order_hotel);
        ListView inListView = (ListView) allOrderFragment.findViewById(R.id.frg_apply_order_list_in);
        ListView outListView = (ListView) allOrderFragment.findViewById(R.id.frg_apply_order_list_out);
        ListView hotelListView = (ListView) allOrderFragment.findViewById(R.id.frg_apply_order_list_hotel);
        if (index == 0) {
            innerTv.setBackgroundResource(R.drawable.navbar_checked);
            outTv.setBackgroundResource(R.drawable.navbar_unchecked);
            hotelTv.setBackgroundResource(R.drawable.navbar_unchecked);
            innerTv.setTextColor(Color.parseColor("#c70019"));
            outTv.setTextColor(Color.parseColor("#999999"));
            hotelTv.setTextColor(Color.parseColor("#999999"));
            inListView.setVisibility(View.VISIBLE);
            outListView.setVisibility(View.GONE);
            hotelListView.setVisibility(View.GONE);
        } else if (index == 1) {
            outTv.setBackgroundResource(R.drawable.navbar_checked);
            innerTv.setBackgroundResource(R.drawable.navbar_unchecked);
            hotelTv.setBackgroundResource(R.drawable.navbar_unchecked);
            outTv.setTextColor(Color.parseColor("#c70019"));
            innerTv.setTextColor(Color.parseColor("#999999"));
            hotelTv.setTextColor(Color.parseColor("#999999"));
            outListView.setVisibility(View.VISIBLE);
            inListView.setVisibility(View.GONE);
            hotelListView.setVisibility(View.GONE);
        } else {
            hotelTv.setBackgroundResource(R.drawable.navbar_checked);
            innerTv.setBackgroundResource(R.drawable.navbar_unchecked);
            outTv.setBackgroundResource(R.drawable.navbar_unchecked);
            hotelTv.setTextColor(Color.parseColor("#c70019"));
            innerTv.setTextColor(Color.parseColor("#999999"));
            outTv.setTextColor(Color.parseColor("#999999"));
            hotelListView.setVisibility(View.VISIBLE);
            inListView.setVisibility(View.GONE);
            outListView.setVisibility(View.GONE);
        }
    }

    @Override
    public void requestSuccess(Object map, int type) {
//        refreshView.stopRefresh();
        //如果当前选中的是预定申请单fragment,则提示获取数据成功或失败
        switch (type) {
            case 1:
                //请求国内，国际预定申请单
                if (null != getActivity()) {
                    Map<String, Object> result = (Map<String, Object>) map;
                    showListData(1, result);
                }
                break;
            case 2:
                //请求国内，国际预定申请单
                if (null != getActivity()) {
                    Map<String, Object> result = (Map<String, Object>) map;
                    showListData(2, result);
                }
                break;
            case 3:
                //请求酒店预定申请单
                if (null != getActivity()) {
                    Map<String, Object> result = (Map<String, Object>) map;
                    showListData(3, result);
                }
                break;
        }
    }

    /**
     * 显示列表数据的方法
     *
     * @param type
     * @param result
     */
    private void showListData(int type, Map<String, Object> result) {
        final int viewpagerIndex = ((MainActivity) getActivity()).getPagerIndex();
        if (null != result) {
            String code = null == result.get("code") ? "" : result.get("code").toString();
//            String codeMsg = (String) result.get("codeMsg");
            List<Map<String, Object>> datalist = (ArrayList) result.get("data");
            if (code.equals("00000")) {
                if (type == 1) {
                    //显示国内预定申请单数据
                    inAdapter.setList(datalist);
                    inAdapter.notifyDataSetChanged();
                } else if (type == 2) {
                    //显示国际预定申请单数据
                    outAdapter.setList(datalist);
                    outAdapter.notifyDataSetChanged();
                } else {
                    //显示酒店预定申请单数据
                    hotelAdapter.setList(datalist);
                    hotelAdapter.notifyDataSetChanged();
                }
                //获取数据成功后，若当前选中的是国内的tab栏，则显示国内机票列表，否则显示国际列表
                ListView inListView = (ListView) allOrderFragment.findViewById(R.id.frg_apply_order_list_in);
                ListView outListView = (ListView) allOrderFragment.findViewById(R.id.frg_apply_order_list_out);
                ListView hotelListView = (ListView) allOrderFragment.findViewById(R.id.frg_apply_order_list_hotel);
                if (navIndex == 0) {
                    //显示国内预定申请单列表
                    inListView.setVisibility(View.VISIBLE);
                    outListView.setVisibility(View.GONE);
                    hotelListView.setVisibility(View.GONE);
                } else if (navIndex == 1) {
                    //显示国际预定申请单列表
                    outListView.setVisibility(View.VISIBLE);
                    inListView.setVisibility(View.GONE);
                    hotelListView.setVisibility(View.GONE);
                } else {
                    //显示酒店预定申请单列表
                    hotelListView.setVisibility(View.VISIBLE);
                    inListView.setVisibility(View.GONE);
                    outListView.setVisibility(View.GONE);
                }
            } else {
                //如果当前页为预定申请单页面，并且只显示当前类型（国内或国际）的提示当前显示的预定申请单
                if (viewpagerIndex == 1 && type == (navIndex + 1)) {
                    if (null != getActivity()) {
                        ToastUtil.toastError(getActivity());
                    }
                }
            }
        } else {
            //请求失败
            //如果当前页为预定申请单页面，并且只显示当前类型（国内或国际）的提示当前显示的预定申请单
            if (viewpagerIndex == 1 && type == (navIndex + 1)) {
                ToastUtil.toastError(getActivity());
            }
        }
    }

    @Override
    public void requestFail(int type) {
        //如果当前选中的是预定申请单fragment,则提示获取数据成功或失败
//        refreshView.stopRefresh();
        final int viewpagerIndex = ((MainActivity) getActivity()).getPagerIndex();
        switch (type) {
            case 1:
                if (null != getActivity()) {
                    //如果当前页为预定申请单页面，并且只显示当前类型（国内或国际）的提示当前显示的预定申请单
                    if (viewpagerIndex == 1 && type == (navIndex + 1)) {
                        ToastUtil.toastFail(getActivity());
                    }
                }
                break;
            case 2:
                if (null != getActivity()) {
                    //如果当前页为预定申请单页面，并且只显示当前类型（国内或国际）的提示当前显示的预定申请单
                    if (viewpagerIndex == 1 && type == (navIndex + 1)) {
                        ToastUtil.toastHandleFail(getActivity());
                    }
                }
                break;
            case 3:
                if (null != getActivity()) {
                    //如果当前页为预定申请单页面，并且只显示当前类型（国内或国际）的提示当前显示的预定申请单
                    if (viewpagerIndex == 1 && type == (navIndex + 1)) {
                        ToastUtil.toastHandleFail(getActivity());
                    }
                }
                break;
        }
    }

    @Override
    public void requestCancel(int type) {
//        for (Call<Map<String,Object>> call: callList) {
//            if(call.isExecuted()){
//                call.cancel();
//            }
//        }
    }

}
