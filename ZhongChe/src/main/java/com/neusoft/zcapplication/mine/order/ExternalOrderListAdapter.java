package com.neusoft.zcapplication.mine.order;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.neusoft.zcapplication.R;

import java.util.List;
import java.util.Map;

/**
 * 国际机票订单适配器
 */

public class ExternalOrderListAdapter extends BaseAdapter{
    private Context context;
    private List<Map<String,Object>> list;
    private int type; //

    public ExternalOrderListAdapter(Context context, List<Map<String,Object>> list, int type){
        this.context = context;
        this.list = list;
        this.type = type;
    }
    public void setList(List<Map<String,Object>> list){
        this.list = list;
    }
    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public Map<String,Object> getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        final Map<String,Object> item = getItem(position);
        if(null == convertView){
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_order_external,null);

            holder.orderType =(TextView) convertView.findViewById(R.id.item_order_type);
            holder.fromCity =(TextView) convertView.findViewById(R.id.item_fromCity);
            holder.toCity =(TextView) convertView.findViewById(R.id.item_toCity);
            holder.time =(TextView) convertView.findViewById(R.id.item_time);
//            holder.reason =(TextView) convertView.findViewById(R.id.item_reason);
            holder.status =(TextView) convertView.findViewById(R.id.item_status);
            holder.btnDel =(TextView) convertView.findViewById(R.id.item_btn_del);
            holder.btnReturn =(TextView) convertView.findViewById(R.id.item_btn_return);
            holder.btnChange =(TextView) convertView.findViewById(R.id.item_btn_change);
            holder.btnComment =(TextView) convertView.findViewById(R.id.item_btn_comment);
            holder.applyOrderId =(TextView) convertView.findViewById(R.id.item_apply_order_id);
            holder.psgTv =(TextView) convertView.findViewById(R.id.item_order_external_psg);
            holder.timeTv =(TextView) convertView.findViewById(R.id.item_order_external_time);
            holder.billsNoTv =(TextView) convertView.findViewById(R.id.item_apply_order_bills_num);
            holder.timeLayout =(LinearLayout) convertView.findViewById(R.id.item_order_external_time_layout);

            convertView.setTag(holder);
        }else{
            holder =(ViewHolder) convertView.getTag();
        }

        holder.applyOrderId.setText(item.get("COMBINEID")+"");
        holder.fromCity.setText(item.get("FROMCITYID")+"");
        holder.toCity.setText(item.get("TOCITYID")+"");
        String orderTimeStr = null == item.get("ORDERTIME") ? "--" : item.get("ORDERTIME").toString();
        holder.time.setText( "预订日期：" + orderTimeStr);
//        holder.reason.setText("因公/因私");
        //遍历显示乘客人员名字
        Object psgObj = item.get("passengerArr");
        String psgStr = "";
        String billsStr = "预定申请单号：";//预定申请单号
        if(psgObj != null){
            List<Map<String,Object>> psgList = (List<Map<String, Object>>) psgObj;
            for (int i = 0 ; i < psgList.size() ; i++){
                String psgName = null == psgList.get(i).get("PASSENGER") ? "" : psgList.get(i).get("PASSENGER").toString();
                if(psgStr.length() > 0){
                    psgStr += "、";
                }
                psgStr += psgName ;
                if(i > 0){
                    billsStr += "、";
                }
                String bill =  psgList.get(i).get("ORDERAPPLYID").toString();
                billsStr += bill;
            }
        }
        holder.psgTv.setText(psgStr);//乘机人
        holder.billsNoTv.setText(billsStr);//所有的预定申请单号
        if((Double)item.get("type") == 1.0) {
            holder.orderType.setText("采购订单");
        }
        else if((Double)item.get("type") == 2.0) {
            holder.orderType.setText("改签订单");
        }
        else {
            holder.orderType.setText("退票订单");
        }
        String orderStateName = null == item.get("ORDERSTATENAME") ? "" : item.get("ORDERSTATENAME").toString();
        holder.status.setText(orderStateName);
        //已提交状态的订单不显示出发时间
        String startTime = null == item.get("FROMDATE") ? "" : item.get("FROMDATE").toString();
        if((!orderStateName.equals("已提交"))&& startTime.length() != 0){
            //已出票状态，显示出发、到达时间
            holder.timeLayout.setVisibility(View.VISIBLE);
//            String toTime = null == item.get("TODATE") ? "--" : item.get("TODATE").toString();
            String timeStr = startTime ;
            holder.timeTv.setText(timeStr);
        }else{
            holder.timeLayout.setVisibility(View.GONE);
        }
        if(orderStateName.equals("已出票")) {
            holder.btnComment.setTextColor(Color.parseColor("#c70019"));
            holder.btnComment.setBackgroundResource(R.drawable.comment_btn_border);
            holder.btnComment.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         Intent intent = new Intent(context,SendCommentActivity.class);
                         intent.putExtra("supplierId",item.get("SUPPLIERID")+"");
                         intent.putExtra("id",item.get("ID")+"");
                         String ticketType = item.get("type")+"";
                         ticketType = ticketType.substring(0,ticketType.indexOf("."));
                         intent.putExtra("ticketType",Integer.parseInt(ticketType));
                         intent.putExtra("type",2);
                         context.startActivity(intent);
                     }
                 }
            );
        }else {
            holder.btnComment.setTextColor(Color.parseColor("#999999"));
            holder.btnComment.setBackgroundResource(R.drawable.cancel_btn_border);
            holder.btnComment.setOnClickListener(null);
        }

        return convertView;
    }

    class ViewHolder{
        TextView orderType,fromCity,toCity,time,reason,status,btnDel,btnReturn,btnChange,btnComment,applyOrderId;
        TextView psgTv,timeTv,billsNoTv;
        LinearLayout timeLayout;
    }
}
