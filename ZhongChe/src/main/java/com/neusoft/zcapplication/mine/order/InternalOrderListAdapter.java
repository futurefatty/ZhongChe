package com.neusoft.zcapplication.mine.order;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.http.RequestCallback;
import com.neusoft.zcapplication.tools.AppUtils;
import com.neusoft.zcapplication.tools.CountDownUtil;
import com.neusoft.zcapplication.tools.DateUtils;
import com.neusoft.zcapplication.tools.ToastUtil;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InternalOrderListAdapter extends BaseAdapter implements RequestCallback {

    private Context context;
    private List<Map<String, Object>> list;
    private Option option;
    private int type; //
    private ClickEvent clickEvent;
    private int returnIndex;//点击退票时点击的item的下标

    private Map<Integer, CountDownUtil> leftTimeMap = new HashMap();
    private Calendar calendar;

    public InternalOrderListAdapter(Context context, List<Map<String, Object>> list, int type, Option option) {
        this.context = context;
        this.list = list;
        this.type = type;
        this.option = option;
        calendar = Calendar.getInstance();

        registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                cancelAllTimers();
            }
        });
    }


    public List<Map<String, Object>> getList() {
        return list;
    }

    public void setList(List<Map<String, Object>> list) {
        this.list = list;
    }

    public void setClickEvent(ClickEvent clickEvent) {
        this.clickEvent = clickEvent;
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public Map<String, Object> getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        final Map<String, Object> item = getItem(position);
        if (null == convertView) {
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_order_internal, null);
            holder.orderType = (TextView) convertView.findViewById(R.id.item_order_type);
            holder.fromCity = (TextView) convertView.findViewById(R.id.item_fromCity);
            holder.toCity = (TextView) convertView.findViewById(R.id.item_toCity);
            holder.price = (TextView) convertView.findViewById(R.id.item_price);
            holder.time = (TextView) convertView.findViewById(R.id.item_time);
            holder.reason = (TextView) convertView.findViewById(R.id.item_reason);
            holder.flightCorporation = (TextView) convertView.findViewById(R.id.item_flight_corporation);
            holder.status = (TextView) convertView.findViewById(R.id.item_status);
            holder.btnDel = (TextView) convertView.findViewById(R.id.item_btn_del);
            holder.btnReturn = (TextView) convertView.findViewById(R.id.item_btn_return);
            holder.item_btn_pay = (TextView) convertView.findViewById(R.id.item_btn_pay);
            holder.btnChange = (TextView) convertView.findViewById(R.id.item_btn_change);
            holder.btnComment = (TextView) convertView.findViewById(R.id.item_btn_comment);
            holder.passenger = (TextView) convertView.findViewById(R.id.item_passenger);
            holder.applyOrderId = (TextView) convertView.findViewById(R.id.item_apply_order_id);
            holder.planFromTimeTv = (TextView) convertView.findViewById(R.id.item_plan_from_time);
            holder.planToTimeTv = (TextView) convertView.findViewById(R.id.item_plan_to_time);
            holder.flightInfoTv = (TextView) convertView.findViewById(R.id.item_internal_flight_info);
            holder.centerLayout = (RelativeLayout) convertView.findViewById(R.id.item_order_internal_center_layout);
            holder.tv_pay_out_time_tip = (TextView) convertView.findViewById(R.id.tv_pay_out_time_tip);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.passenger.setText(item.get("EMPLOYEENAME") + "");
        holder.applyOrderId.setText(item.get("ORDERAPPLYID") + "");
        String fromCity = item.get("FROMCITYNAME") == null ? "" : item.get("FROMCITYNAME") + "";
        String toCity = item.get("TOCITYNAME") == null ? "" : item.get("TOCITYNAME") + "";
        holder.fromCity.setText(fromCity);
        holder.toCity.setText(toCity);
        double servicePrice = (double) (item.get("SERVICEPRICE") == null ? 0 : item.get("SERVICEPRICE"));
        Object cstCstObj = item.get("COSTCONSTRUCTION");
//        Log.i("--->","---" + (int)cstCstObj );
//        double costConstruction = 0.0 ;
//        if(cstCstObj instanceof Integer){
//            costConstruction += (int)(cstCstObj ==null?0:cstCstObj);
//        }else if(cstCstObj instanceof Double){
//            costConstruction += (double)(cstCstObj ==null?0.0:cstCstObj);
//        }
//        double fuelCost = (double)(item.get("FUELCOST")==null?0:item.get("FUELCOST"));
//        double price = (double)(item.get("PRICE")==null?0:item.get("PRICE"));
//        holder.price.setText("￥"+(servicePrice + costConstruction + fuelCost + price )+"");
        String totalPrice = item.get("TOTALPRICE") == null ? "" : item.get("TOTALPRICE").toString();
        holder.price.setText("￥" + totalPrice);
        String time = item.get("FROMPLANDATE") + "";
        holder.time.setText("出发日期：" + time.substring(0, 10));
        holder.reason.setText("因公/因私");
        holder.flightCorporation.setText("航空公司");
        holder.status.setText(item.get("ORDERSTATE") + "");
        String fromplandate = null == item.get("FROMPLANDATE") ? "--" : item.get("FROMPLANDATE").toString();
        fromplandate = fromplandate.length() > 3 ? fromplandate.substring(0, fromplandate.length() - 3) : fromplandate;
        String toplandate = null == item.get("TOPLANDATE") ? "--" : item.get("TOPLANDATE").toString();
        toplandate = toplandate.length() > 3 ? toplandate.substring(0, toplandate.length() - 3) : toplandate;
        String planTimeStr = "出发时间：" + fromplandate;
        holder.planFromTimeTv.setText(planTimeStr);
        holder.planToTimeTv.setText("到达时间：" + toplandate);
        String flightNo = null == item.get("FLIGHTNO") ? "" : item.get("FLIGHTNO").toString();
        String flightCompany = null == item.get("CARRIERNAME") ? "" : item.get("CARRIERNAME").toString();
        holder.flightInfoTv.setText(flightCompany + flightNo);
        int payType = (null == item.get("PAYTYPE") || "".equals(item.get("PAYTYPE").toString())) ? 0 : Double.valueOf(item.get("PAYTYPE").toString()).intValue();//0:月结 1:现付
        final int payState = (null == item.get("PAYSTATE") || "".equals(item.get("PAYTYPE").toString())) ? 2 : Double.valueOf(item.get("PAYSTATE").toString()).intValue();//1:未付款 2:已付款
        int stateInt;
        /**
         * a、已出票、改签失败 、已改签、退票失败：退票、改签按钮可点击，评价按钮不可点击 stateInt = 0;
         *     1、如果是机票未过期：退票、改签按钮可点击，评价按钮不可点击 stateInt = 0;
         *     2、已过期：只有评价按钮可点击 stateInt = 2;
         * b、未出票、已提交、未提交、出票失败、 改签中、已过期、退票中、出票中、已退票：
         *      退票、改签、评价按钮都不可点击 stateInt = 1;
         * c、已完成的票，只有评价按钮可点击 stateInt = 2
         * ：
         */
        String statusStr = null == item.get("ORDERSTATE") ? "" : item.get("ORDERSTATE").toString();
        if (statusStr.equals("已出票") || statusStr.equals("改签失败")
                || statusStr.equals("已改签") || statusStr.equals("退票失败")) {
            stateInt = 0;
//            if (!DateUtils.isPaseByTime(time)){
//            }else{
//                stateInt = 2;
//            }
        } else if (statusStr.equals("未出票") || statusStr.equals("已提交")
                || statusStr.equals("未提交") || statusStr.equals("出票失败")
                || statusStr.equals("改签中") || statusStr.equals("退票中")
                || statusStr.equals("出票中") || statusStr.equals("已退票")) {
            stateInt = 1;
        } else if (statusStr.equals("已完成")) {
            stateInt = 2;
        } else {
            stateInt = 1;
        }
        if (stateInt == 0) {
            holder.btnReturn.setTextColor(Color.parseColor("#c70019"));
            holder.btnReturn.setBackgroundResource(R.drawable.comment_btn_border);
            holder.btnReturn.setOnClickListener(new MyClickListener(position, 1));

            holder.btnChange.setTextColor(Color.parseColor("#c70019"));
            holder.btnChange.setBackgroundResource(R.drawable.comment_btn_border);
            holder.btnChange.setOnClickListener(new MyClickListener(position, 2));

            holder.btnComment.setTextColor(Color.parseColor("#999999"));
            holder.btnComment.setBackgroundResource(R.drawable.cancel_btn_border);
            holder.btnComment.setOnClickListener(null);

        } else if (stateInt == 1) {
            holder.btnReturn.setTextColor(Color.parseColor("#999999"));
            holder.btnReturn.setBackgroundResource(R.drawable.cancel_btn_border);
            holder.btnReturn.setOnClickListener(null);

            holder.btnChange.setTextColor(Color.parseColor("#999999"));
            holder.btnChange.setBackgroundResource(R.drawable.cancel_btn_border);
            holder.btnChange.setOnClickListener(null);

            holder.btnComment.setTextColor(Color.parseColor("#999999"));
            holder.btnComment.setBackgroundResource(R.drawable.cancel_btn_border);
            holder.btnComment.setOnClickListener(null);
        } else {
            holder.btnReturn.setTextColor(Color.parseColor("#999999"));
            holder.btnReturn.setBackgroundResource(R.drawable.cancel_btn_border);
            holder.btnReturn.setOnClickListener(null);

            holder.btnChange.setTextColor(Color.parseColor("#999999"));
            holder.btnChange.setBackgroundResource(R.drawable.cancel_btn_border);
            holder.btnChange.setOnClickListener(null);

            holder.btnComment.setTextColor(Color.parseColor("#c70019"));
            holder.btnComment.setBackgroundResource(R.drawable.cancel_btn_border);
            holder.btnComment.setOnClickListener(new MyClickListener(position, 3));

        }
        //详情
        holder.centerLayout.setOnClickListener(new MyClickListener(position, 4));
//        if(item.get("ORDERSTATE").equals("已出票")) {
//            //已过期的订单不能退票和改签
//            if (!DateUtils.isPaseByDate(time)){
//                holder.btnReturn.setTextColor(Color.parseColor("#c70019"));
//                holder.btnReturn.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.comment_btn_border));
//                holder.btnReturn.setOnClickListener(new MyClickListener(item,1));
//
//                holder.btnChange.setTextColor(Color.parseColor("#c70019"));
//                holder.btnChange.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.comment_btn_border));
        //                holder.btnChange.setOnClickListener(new MyClickListener(item,2));
//            }
//            else {
//                holder.btnReturn.setTextColor(Color.parseColor("#999999"));
//                holder.btnReturn.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.cancel_btn_border));
//                holder.btnReturn.setOnClickListener(null);
//
//                holder.btnChange.setTextColor(Color.parseColor("#999999"));
//                holder.btnChange.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.cancel_btn_border));
//                holder.btnChange.setOnClickListener(null);
//            }
//
//            holder.btnComment.setTextColor(Color.parseColor("#c70019"));
//            holder.btnComment.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.comment_btn_border));
//            holder.btnComment.setOnClickListener(new View.OnClickListener() {
//                     @Override
//                     public void onClick(View v) {
//                         Intent intent = new Intent(context,SendCommentActivity.class);
//                         intent.putExtra("supplierId",item.get("SUPPLIERID")+"");
//                         intent.putExtra("id",item.get("ID")+"");
//                         String ticketType = item.get("type")+"";
//                         ticketType = ticketType.substring(0,ticketType.indexOf("."));
//                         intent.putExtra("ticketType",Integer.parseInt(ticketType));
//                         intent.putExtra("type",1);
//                         context.startActivity(intent);
//                     }
//                 }
//            );
//        }else {
//            holder.btnReturn.setTextColor(Color.parseColor("#999999"));
//            holder.btnReturn.setBackgroundResource(R.drawable.cancel_btn_border);
//            holder.btnReturn.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.cancel_btn_border));
//            holder.btnReturn.setOnClickListener(null);
//
//            holder.btnChange.setTextColor(Color.parseColor("#999999"));
//            holder.btnChange.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.cancel_btn_border));
//            holder.btnChange.setOnClickListener(null);
//
//            holder.btnComment.setTextColor(Color.parseColor("#999999"));
//            holder.btnComment.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.cancel_btn_border));
//            holder.btnComment.setOnClickListener(null);
//        }

        if ((Double) item.get("type") == 1.0) {
            holder.orderType.setText("采购订单");
        } else if ((Double) item.get("type") == 2.0) {
            holder.orderType.setText("改签订单");
        } else {
            holder.orderType.setText("退票订单");
        }
        ViewHolder finalHolder = holder;
        finalHolder.item_btn_pay.setTag(R.id.item_position, position);
        finalHolder.item_btn_pay.setTag(R.id.item_obj, item);
        String estimatedTime = null == item.get("ESTIMATEDTIME") ? "" : item.get("ESTIMATEDTIME").toString();
        boolean fanJiaOrder = AppUtils.isFanJiaOrder(item);
        String point = null == item.get("POINT") ? "" : item.get("POINT").toString();
        if (fanJiaOrder && TextUtils.isEmpty(estimatedTime) && statusStr.equals("改签中") && payType == 1) {
            finalHolder.item_btn_pay.setTextColor(Color.parseColor("#c70019"));
            finalHolder.item_btn_pay.setBackgroundResource(R.drawable.comment_btn_border);
            holder.item_btn_pay.setVisibility(View.VISIBLE);
            holder.item_btn_pay.setText("最终价格获取中");
            if (!TextUtils.isEmpty(point)) {
                finalHolder.tv_pay_out_time_tip.setVisibility(View.VISIBLE);
                finalHolder.tv_pay_out_time_tip.setText(point);
            } else {
                finalHolder.tv_pay_out_time_tip.setVisibility(View.GONE);
            }
            holder.item_btn_pay.setOnClickListener(btnPayClickListener);
        } else {
            finalHolder.item_btn_pay.setTextColor(Color.parseColor("#999999"));
            finalHolder.item_btn_pay.setBackgroundResource(R.drawable.cancel_btn_border);
            //payType 1是现付 0是月结
            if (payType == 1) {
                //现付订单
                if ((Double) item.get("type") == 3.0) {
                    //退票订单
                    finalHolder.item_btn_pay.setVisibility(View.GONE);
                    finalHolder.item_btn_pay.setText("已支付");
                    finalHolder.tv_pay_out_time_tip.setVisibility(View.GONE);
                } else {
                    //payState 1 待支付
                    if (payState == 1) {
                        String time1;
                        if (!TextUtils.isEmpty(estimatedTime)) {
                            time1 = estimatedTime;
                        } else {
                            time1 = null == item.get("ORDERTIME") ? "" : item.get("ORDERTIME").toString();
                        }
                        String time2 = DateUtils.DateToSecondStr(new Date(System.currentTimeMillis()));
                        //泛嘉20 分钟 美亚24小时
                        boolean check;
                        long differTime = DateUtils.differTimeWithMsec(time1, time2);
                        long millisInFuture;
                        if (fanJiaOrder) {
                            millisInFuture = 20 * 60 * 1000 - differTime;
                            check = (differTime * 1.0 / (1000 * 60)) < 20;
                        } else {
                            millisInFuture = 24 * 60 * 60 * 1000 - differTime;
                            check = (differTime * 1.0 / (1000 * 60 * 60)) < 24;
                        }
                        if (check) {
                            if (!TextUtils.isEmpty(point)) {
                                finalHolder.tv_pay_out_time_tip.setVisibility(View.VISIBLE);
                                finalHolder.tv_pay_out_time_tip.setText(point);
                            } else {
                                finalHolder.tv_pay_out_time_tip.setVisibility(View.GONE);
                            }
                            holder.item_btn_pay.setText("待支付 " + DateUtils.getHours(millisInFuture));
                            finalHolder.item_btn_pay.setVisibility(View.VISIBLE);
                            finalHolder.item_btn_pay.setTextColor(Color.parseColor("#c70019"));
                            finalHolder.item_btn_pay.setBackgroundResource(R.drawable.comment_btn_border);
                            position = (int) finalHolder.item_btn_pay.getTag(R.id.item_position);
                            CountDownUtil cdu = leftTimeMap.get(position);
                            if (cdu == null) {
                                cdu = new CountDownUtil(millisInFuture, 1000) {
                                    @Override
                                    public void onFinish(Object tag) {
                                        int finshPosition = (int) tag;
                                        //当前位置
                                        int position = (int) finalHolder.item_btn_pay.getTag(R.id.item_position);
                                        if (position == finshPosition) {
                                            finalHolder.item_btn_pay.setText("支付超期");
                                            finalHolder.tv_pay_out_time_tip.setVisibility(View.GONE);
                                            finalHolder.item_btn_pay.setVisibility(View.VISIBLE);
                                            finalHolder.item_btn_pay.setTextColor(Color.parseColor("#999999"));
                                            finalHolder.item_btn_pay.setBackgroundResource(R.drawable.cancel_btn_border);
                                        }
                                    }

                                    @Override
                                    public void onTick(long millisUntilFinished, Object tag) {
                                        /**
                                         * tickPosition 已开启的计时器回调位置
                                         * 比如position 0 开启倒计时,此时滑动到11位置 一样有倒计时
                                         * 那么此时当前位置在11,但是计时器却是一直在运行，所以{@link CountDownUtil#onTick(long, Object)}
                                         * 回调两次，而此时只需要刷新当前展示的item位置ui,就需要知道当时在0位置时设置的position信息。以便
                                         * 接下来判断开启计时器的位置和当前显示位置是否一致。所以就需要给{@link CountDownUtil}绑定一个tag，而这个
                                         * tag就是开启时的位置。
                                         */
                                        int tickPosition = (int) tag;
                                        //当前位置
                                        int position = (int) finalHolder.item_btn_pay.getTag(R.id.item_position);
                                        if (position == tickPosition) {
                                            finalHolder.item_btn_pay.setText("待支付 " +
                                                    DateUtils.getHours(millisUntilFinished));
                                        }
                                    }

                                };
                                leftTimeMap.put(position, cdu);
                                cdu.setTag(finalHolder.item_btn_pay.getTag(R.id.item_position));
                                cdu.start();
                            }
                            finalHolder.item_btn_pay.setOnClickListener(btnPayClickListener);
                        } else {
                            finalHolder.tv_pay_out_time_tip.setVisibility(View.GONE);
                            finalHolder.item_btn_pay.setText("支付超期");
                            finalHolder.item_btn_pay.setVisibility(View.VISIBLE);
                            finalHolder.item_btn_pay.setTextColor(Color.parseColor("#999999"));
                            finalHolder.item_btn_pay.setBackgroundResource(R.drawable.cancel_btn_border);
                        }
                    } else {
                        finalHolder.tv_pay_out_time_tip.setVisibility(View.GONE);
                        finalHolder.item_btn_pay.setText("已支付");
                        finalHolder.item_btn_pay.setVisibility(View.VISIBLE);
                        finalHolder.item_btn_pay.setTextColor(Color.parseColor("#999999"));
                        finalHolder.item_btn_pay.setBackgroundResource(R.drawable.cancel_btn_border);
                    }
                }
            } else {
                finalHolder.tv_pay_out_time_tip.setVisibility(View.GONE);
                finalHolder.item_btn_pay.setVisibility(View.GONE);
                finalHolder.item_btn_pay.setText("已支付");
            }
        }
        return convertView;
    }

    //作为严谨的码工,当然要善始善终
    public void cancelAllTimers() {
        Set<Map.Entry<Integer, CountDownUtil>> s = leftTimeMap.entrySet();
        Iterator it = s.iterator();
        while (it.hasNext()) {
            try {
                Map.Entry pairs = (Map.Entry) it.next();
                CountDownUtil cdt = (CountDownUtil) pairs.getValue();
                cdt.cancel();
            } catch (Exception e) {
            }
        }
        leftTimeMap.clear();
    }


    @Override
    public void requestSuccess(Object map, int type) {
        Map<String, Object> result = (Map<String, Object>) map;
        if (null != result) {
            String code = null == result.get("code") ? "" : result.get("code").toString();
            String codeMsg = null == result.get("codeMsg") ? "" : result.get("codeMsg").toString();
//            String data = (String) result.get("data");
            if ("00000".equals(code)) {
                ToastUtil.toastHandleSuccess(context);
                //修改状态
                list.get(returnIndex).put("ORDERSTATE", "退票中");
                notifyDataSetChanged();
//                String statusStr = null == item.get("ORDERSTATE") ? ""  : item.get("ORDERSTATE").toString();
            } else if ("40001".equals(code)) {
                ToastUtil.toastNeedData(context, "退票失败：" + codeMsg);
            } else {
                ToastUtil.toastHandleError(context);
            }
        } else {
            //请求失败
            ToastUtil.toastHandleError(context);
        }
    }

    @Override
    public void requestFail(int type) {
        ToastUtil.toastHandleFail(context);
    }

    @Override
    public void requestCancel(int type) {

    }


    View.OnClickListener btnPayClickListener = v -> {
        TextView textView = (TextView) v;
        String content = textView.getText().toString().trim();
        if (content.contains("待支付") || content.contains("最终价格获取中")) {
            Map<String, Object> item = (Map<String, Object>) v.getTag(R.id.item_obj);
            //改签标记,正常订单支付：0，改签订单支付：1 ,
            String changeFlag = "";
            if ((Double) item.get("type") == 2.0) {
                //改签订单
                changeFlag = "1";
            } else {
                changeFlag = "0";
            }
            //预定申请单编号
            String orderApplyId = item.get("ORDERAPPLYID").toString();
            //订单编号
            String orderCode = item.get("ORDERID").toString();
            option.pay((Integer) v.getTag(R.id.item_position), changeFlag, orderApplyId, orderCode);
        }
    };

    interface ClickEvent {
        //改签订单
        void changeOrder(Map<String, Object> item);

        void refundOrder(Map<String, Object> item, int position);//退票
    }

    class ViewHolder {
        TextView orderType, fromCity, toCity, price, time, reason, flightCorporation,
                status, btnDel, btnReturn, btnChange, btnComment, passenger, applyOrderId, item_btn_pay,
                tv_pay_out_time_tip;
        TextView planFromTimeTv, planToTimeTv;//计划出行时间
        TextView flightInfoTv;
        RelativeLayout centerLayout;//详情入口
    }

    class MyClickListener implements View.OnClickListener {
        private int type;
        private int position;

        public MyClickListener(int position, int type) {
            this.position = position;
            this.type = type;
        }

        @Override
        public void onClick(View v) {
            final Map<String, Object> map = list.get(position);
            if (type == 1) {
                returnIndex = position;
                clickEvent.refundOrder(map, position);
//                AlertUtil.show(context, "确定退票", "确定", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        returnOrder(map);
//                    }
//                }, "取消", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                    }
//                },"取消订单");
//                return;
            } else if (type == 2) {
                if (clickEvent != null) {
                    clickEvent.changeOrder(map);
                }
            } else if (type == 3) {
                Intent intent = new Intent(context, SendCommentActivity.class);
                intent.putExtra("supplierId", map.get("SUPPLIERID") + "");
                intent.putExtra("id", map.get("ID") + "");
                String ticketType = map.get("type") + "";
                ticketType = ticketType.substring(0, ticketType.indexOf("."));
                intent.putExtra("ticketType", Integer.parseInt(ticketType));
                intent.putExtra("type", 1);
                context.startActivity(intent);
            } else if (type == 4) {
                Intent intent = new Intent(context, InternalOrderDetailActivity.class);
                intent.putExtra("detailData", (Serializable) map);
                context.startActivity(intent);
            }
        }
    }

    public interface Option {
        void pay(int position, String changeFlag, String orderApplyId, String orderCode);
    }

}
