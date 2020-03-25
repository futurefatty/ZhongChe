package com.neusoft.zcapplication.mine.order;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.neusoft.zcapplication.Bean.User;
import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.tools.AlertUtil;
import com.neusoft.zcapplication.tools.AppUtils;
import com.neusoft.zcapplication.widget.DefinedExpandListView;

import java.util.List;
import java.util.Map;

/**
 * 国际机票订单详情页列表适配器
 */

public class FlightSchemesListAdapter extends BaseAdapter {
    private Context context;
    private List<List<Map<String, Object>>> list;
    private int orderType; //1 采购订单,2 改签订单,其他 图片订单
    private String orderStateName;
    private String applicantId;
    //    private FlightSchemesPassengerListAdapter passengerListAdapter;
    private ClickEvent clickEvent;
    private boolean hasOrder;// true 已经预定了该机票，false 未预定该机票
    //是否是预定页面进来的 如果是则不现实确认按钮
    private boolean isOrderApply;

    public FlightSchemesListAdapter(Context context, List<List<Map<String, Object>>> list,
                                    String applicantId, ClickEvent clickEvent, boolean hasOrder,
                                    int orderType, String orderStateName) {
        this.context = context;
        this.list = list;
        this.applicantId = applicantId;
        this.clickEvent = clickEvent;
        this.hasOrder = hasOrder;
        this.orderType = orderType;
        this.orderStateName = orderStateName;
    }

    public void setList(List<List<Map<String, Object>>> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public List<Map<String, Object>> getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final List<Map<String, Object>> item = getItem(position);
        final ViewHolder holder;
        if (null == convertView) {
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_flight_schemes_list, null);

            holder.listView = (DefinedExpandListView) convertView.findViewById(R.id.passenger_list);
            holder.btn_book = (TextView) convertView.findViewById(R.id.btn_book);
            holder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
            holder.priceTv1 = (TextView) convertView.findViewById(R.id.item_flight_schemes_price_detail_tv1);
            holder.priceTv2 = (TextView) convertView.findViewById(R.id.item_flight_schemes_price_detail_tv2);
            holder.priceTv3 = (TextView) convertView.findViewById(R.id.item_flight_schemes_price_detail_tv3);
            holder.priceTv4 = (TextView) convertView.findViewById(R.id.item_flight_schemes_price_detail_tv4);
            //价格详情布局
            holder.priceLayout = (LinearLayout) convertView.findViewById(R.id.item_flight_schemes_price_detail_layout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        double totalPrice = 0;
        for (Map<String, Object> map : item) {
            double totalPriceDouble = null == map.get("TOTALPRICE") ? 0.0 : (double) map.get("TOTALPRICE");
            totalPrice = totalPrice + totalPriceDouble;
        }
        User user = AppUtils.getUserInfo(context);
        holder.tv_price.setText("￥" + totalPrice);
        final SchemesExpandListViewAdapter expAdapter = new SchemesExpandListViewAdapter(context, item);
        holder.listView.setAdapter(expAdapter);
        holder.listView.setGroupIndicator(null);

        //如果有预定方案，则不显示预定按钮,并且，方案都处于展开状态
        if (hasOrder) {
            holder.btn_book.setVisibility(View.GONE);
            expAdapter.setShowArrow(false);//已经确定了方案的话，不显示折叠列表箭头
            for (int i = 0; i < item.size(); i++) {
                holder.listView.expandGroup(i);
            }
            holder.listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                @Override
                public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                    return true;
                }
            });
        } else {
            if (isOrderApply) {
                holder.btn_book.setVisibility(View.GONE);
            } else {
                holder.btn_book.setVisibility(View.VISIBLE);
            }
            holder.listView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                @Override
                public void onGroupExpand(int groupPosition) {
                    // TODO Auto-generated method stub
                    for (int i = 0; i < expAdapter.getGroupCount(); i++) {
                        if (i != groupPosition) {
                            holder.listView.collapseGroup(i);
                        }
                    }
                }
            });
            //只有提交方案的人才能选择出行方案
            if (applicantId.equals(user.getEmployeeCode())) {
                holder.btn_book.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //遍历判断是否所有的乘机人的乘机申请是否都通过了审批
                        boolean canOrder = true;
                        for (Map<String, Object> mm : item) {
                            //1审批通过，0 未审批通过 2, 审批中
                            String isApprove = null == mm.get("ISAPPROVE") ? "0" : mm.get("ISAPPROVE").toString();
                            if (isApprove.equals("0") || isApprove.equals("2")) {
                                canOrder = false;
                            }
                        }
                        if (canOrder) {
                            clickEvent.book(item.get(0));
                        } else {
                            AlertUtil.show2(context, "您的预定申请单没有发布完成，不能确认方案！", "确认", null);
                        }
                    }
                });
            } else {
                holder.btn_book.setOnClickListener(null);
                holder.btn_book.setTextColor(Color.parseColor("#999999"));
                holder.btn_book.setBackgroundResource(R.drawable.cancel_btn_border);
            }
        }
        double ticketPrice = 0.0,//票面价
                servicePrice = 0.0,//服务费
                taxPrice = 0.0,//税费
                changePrice = 0.0,//改签费
                returnPrice = 0.0,//退票费
                usedPrice = 0.0;//已使用航段费
        for (Map<String, Object> map : item) {
            //票面价
            double price = null == map.get("PRICE") ? 0.0 : (double) map.get("PRICE");
            ticketPrice += price;
            //服务费
            double svsPrice = null == map.get("SERVICEPRICE") ? 0.0 : (double) map.get("SERVICEPRICE");
            servicePrice += svsPrice;
            //税费
            double tPrice = null == map.get("TAX") ? 0.0 : (double) map.get("TAX");
            taxPrice += tPrice;
            //改签费
            double cPrice = null == map.get("CHANGEPRICE") ? 0.0 : (double) map.get("CHANGEPRICE");
            changePrice += cPrice;
            //退票费
            double rPrice = null == map.get("RETURNPRICE") ? 0.0 : (double) map.get("RETURNPRICE");
            returnPrice += rPrice;
            //已使用航段费
            double uPrice = null == map.get("USEDFLIGHTPRICE") ? 0.0 : (double) map.get("USEDFLIGHTPRICE");
            usedPrice += uPrice;

        }
//        LogUtil.d("票面价:***********************"+ticketPrice);
//        LogUtil.d("服务费:***********************"+servicePrice);
//        LogUtil.d("税费:***********************"+taxPrice);
//        LogUtil.d("改签费:***********************"+changePrice);
//        LogUtil.d("退票费:***********************"+returnPrice);
//        LogUtil.d("已使用航段费:***********************"+usedPrice);

        if (orderType == 1) {
//            holder.orderType.setText("采购订单");
//            holder.priceLayout.setVisibility(View.VISIBLE);
            holder.priceTv1.setText("票面价：￥" + ticketPrice);
            holder.priceTv2.setText("服务费：￥" + servicePrice);
            holder.priceTv3.setText("税费：￥" + taxPrice);
        } else if (orderType == 2) {
//            holder.orderType.setText("改签订单");
            holder.priceTv1.setText("票面价：￥" + ticketPrice);
            holder.priceTv2.setText("服务费：￥" + servicePrice);
            holder.priceTv4.setText("改签费：￥" + changePrice);
            holder.priceTv3.setText("税费：￥" + taxPrice);
        } else {
//            holder.orderType.setText("退票订单");
            holder.priceTv2.setText("服务费：￥" + servicePrice);
            holder.priceTv4.setText("改签费：￥" + changePrice);
            holder.priceTv1.setText("退票费：￥" + returnPrice);
            holder.priceTv3.setText("已使用航段费：￥" + usedPrice);
        }
        return convertView;
    }


    public void setOrderApply(boolean isOrderApply) {
        this.isOrderApply = isOrderApply;
    }

    interface ClickEvent {
        void book(Map<String, Object> map);
    }

    class ViewHolder {
        DefinedExpandListView listView;
        TextView tv_price, btn_book;
        LinearLayout priceLayout;//价格明细布局
        TextView priceTv1, priceTv2, priceTv3, priceTv4;
    }

}
