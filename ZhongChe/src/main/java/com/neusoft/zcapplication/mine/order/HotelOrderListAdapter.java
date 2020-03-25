package com.neusoft.zcapplication.mine.order;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.tools.LogUtil;
import com.neusoft.zcapplication.tools.ToastUtil;

import java.util.List;
import java.util.Map;

/**
 * 酒店适配器
 */
public class HotelOrderListAdapter extends BaseAdapter{
    private Context context;
    private List<Map<String,Object>> list;
    private int type; //
    private HotelItemClick itemBtnClick;

    public HotelOrderListAdapter(Context context, List<Map<String,Object>> list, int type){
        this.context = context;
        this.list = list;
        this.type = type;
    }

    public void setItemBtnClick(HotelItemClick itemBtnClick) {
        this.itemBtnClick = itemBtnClick;
    }

    public List<Map<String, Object>> getList() {
        return list;
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
            convertView = inflater.inflate(R.layout.item_order_hotel,null);

            holder.orderType =(TextView) convertView.findViewById(R.id.item_order_type);
            holder.hotelName =(TextView) convertView.findViewById(R.id.item_hotel_name);
            holder.address =(TextView) convertView.findViewById(R.id.item_address);
            holder.price =(TextView) convertView.findViewById(R.id.item_price);
            holder.time =(TextView) convertView.findViewById(R.id.item_time);
            holder.reason =(TextView) convertView.findViewById(R.id.item_reason);
            holder.status =(TextView) convertView.findViewById(R.id.item_status);
            holder.btnNav =(TextView) convertView.findViewById(R.id.item_btn_nav);
            holder.btnReturn =(TextView) convertView.findViewById(R.id.item_btn_return);
            holder.btnChange =(TextView) convertView.findViewById(R.id.item_btn_change);
            holder.btnComment =(TextView) convertView.findViewById(R.id.item_btn_comment);
            holder.channelTv =(TextView) convertView.findViewById(R.id.item_hotel_order_channel);
            holder.empNameTv =(TextView) convertView.findViewById(R.id.item_hotel_empName);
            holder.cancelTv =(TextView) convertView.findViewById(R.id.item_hotel_free_cancel_time);
            //协议、月结、是否可以取消
            holder.xyTv =(TextView) convertView.findViewById(R.id.item_hotel_type_xy);
            holder.yjTv =(TextView) convertView.findViewById(R.id.item_hotel_type_yj);
            holder.canCancelTv =(TextView) convertView.findViewById(R.id.item_hotel_type_cancel);

            convertView.setTag(holder);
        }else{
            holder =(ViewHolder) convertView.getTag();
        }

        if (item.get("longitude") == null || item.get("latitude") == null){
            holder.btnNav.setVisibility(View.GONE);
        }else {
            holder.btnNav.setVisibility(View.VISIBLE);
        }
        holder.btnNav.setOnClickListener(new HotelBtnClickListener(position));
        holder.hotelName.setText(item.get("hotelName")+"");
        String address = null == item.get("address") ? "" : item.get("address").toString();
        holder.address.setText(address);
        holder.price.setText("￥"+item.get("price")+"");
        holder.time.setText(item.get("fromTime")+"至"+item.get("toTime"));
        holder.reason.setText("因公/因私");
        holder.status.setText(item.get("stateName")+"");
        //渠道名称
        String channelName = null == item.get("supplierName")
                ? "" : item.get("supplierName").toString();
        holder.channelTv.setText(channelName);
        String empName = null == item.get("empName") ? "" : "入住人:" + item.get("empName").toString();
        holder.empNameTv.setText(empName);
        if((double)item.get("type") == 1.0) {
            holder.orderType.setText("采购订单");
        }
        else if((Double)item.get("type") == 2.0) {
            holder.orderType.setText("改签订单");
        }else {
            holder.orderType.setText("退订订单");
        }
        //是否协议酒店：1是，0否 ,
        String isAgreement = null == item.get("isAgreement") ? "" :  item.get("isAgreement").toString();
        //是否可取消：1可，0不可 ,
        String isCancel = null == item.get("isCancel") ? "" :  item.get("isCancel").toString();
        //月结酒店：1月结，0非月结
        String payStyle = null == item.get("payStyle") ? "" :  item.get("payStyle").toString();
        if(isAgreement.equals("1")){
            holder.xyTv.setVisibility(View.VISIBLE);
        }else{
            holder.xyTv.setVisibility(View.GONE);
        }
        String supplierId = null == item.get("supplierId")
                ? "" : item.get("supplierId").toString();
        //如果不可取消，则不显示免费取消截止时间 1是可取消
        if(isCancel.equals("1")){
            holder.canCancelTv.setVisibility(View.GONE);
            holder.cancelTv.setVisibility(View.VISIBLE);
        }else{
            if("4".equals(supplierId)||"4.0".equals(supplierId)){
                //携程
                holder.canCancelTv.setVisibility(View.GONE);
            }else{
                holder.canCancelTv.setVisibility(View.VISIBLE);
            }
            holder.cancelTv.setVisibility(View.GONE);
        }
        if(payStyle.equals("1")){
            holder.yjTv.setText("月结");
        }else{
            holder.yjTv.setText("现付");
        }
        if("4".equals(supplierId)||"4.0".equals(supplierId)){
            //携程
            holder.yjTv.setVisibility(View.GONE);
        }else{
            holder.yjTv.setVisibility(View.VISIBLE);
        }
        String returnDate = null == item.get("returnDate") ? "" :  item.get("returnDate").toString();
        if(!returnDate.equals("")){
            holder.cancelTv.setText("免费取消截止时间:" + returnDate);
        }

        /**
         * 酒店订单状态：已入住、未入住、已离店、已退订
         * 1、未入住：只能点击退订按钮；
         * 2、已离店、已退订、已入住： 只能点击评价
         */
        String stateName = null == item.get("stateName") ? "" : item.get("stateName").toString();
        if(stateName.equals("已离店") || stateName.equals("已入住") || stateName.equals("已退订")){
            holder.btnReturn.setTextColor(Color.parseColor("#999999"));
            holder.btnReturn.setBackgroundResource(R.drawable.cancel_btn_border);
            holder.btnReturn.setOnClickListener(null);

            holder.btnComment.setTextColor(Color.parseColor("#c70019"));
            holder.btnComment.setBackgroundResource(R.drawable.comment_btn_border);
            holder.btnComment.setOnClickListener(new HotelBtnClickListener(position));
        }else if(stateName.equals("未入住")){
            holder.btnReturn.setTextColor(Color.parseColor("#c70019"));
            holder.btnReturn.setBackgroundResource(R.drawable.comment_btn_border);
            holder.btnReturn.setOnClickListener(new HotelBtnClickListener(position));

            holder.btnComment.setTextColor(Color.parseColor("#999999"));
            holder.btnComment.setBackgroundResource(R.drawable.cancel_btn_border);
            holder.btnComment.setOnClickListener(null);
        }else{
            holder.btnReturn.setTextColor(Color.parseColor("#999999"));
            holder.btnReturn.setBackgroundResource(R.drawable.cancel_btn_border);
            holder.btnReturn.setOnClickListener(null);

            holder.btnComment.setTextColor(Color.parseColor("#999999"));
            holder.btnComment.setBackgroundResource(R.drawable.cancel_btn_border);
            holder.btnComment.setOnClickListener(null);
        }
//        if(stateName.equals("已出票")) {
//            holder.btnComment.setTextColor(Color.parseColor("#c70019"));
//            holder.btnComment.setBackgroundResource(R.drawable.comment_btn_border);
//            holder.btnComment.setOnClickListener(new HotelBtnClickListener(position));
////            holder.btnComment.setOnClickListener(new View.OnClickListener() {
////                     @Override
////                     public void onClick(View v) {
////                         Intent intent = new Intent(context,SendCommentActivity.class);
////                         intent.putExtra("supplierId",item.get("supplierId")+"");
////                         intent.putExtra("supplierName",item.get("supplierName")+"");
////                         intent.putExtra("id",item.get("id")+"");
////                         String ticketType = item.get("type")+"";
////                         ticketType = ticketType.substring(0,ticketType.indexOf("."));
////                         intent.putExtra("ticketType",Integer.parseInt(ticketType));
////                         intent.putExtra("type",3);
////                         context.startActivity(intent);
////                     }
////                 }
////            );
//
//            holder.btnReturn.setTextColor(Color.parseColor("#c70019"));
//            holder.btnReturn.setBackgroundResource(R.drawable.comment_btn_border);
//            holder.btnReturn.setOnClickListener(new HotelBtnClickListener(position));
//        }else {
//            holder.btnComment.setTextColor(Color.parseColor("#999999"));
//            holder.btnComment.setBackgroundResource(R.drawable.cancel_btn_border);
//            holder.btnComment.setOnClickListener(null);
//
//            holder.btnReturn.setTextColor(Color.parseColor("#999999"));
//            holder.btnReturn.setBackgroundResource(R.drawable.cancel_btn_border);
//        }

        return convertView;
    }
interface HotelItemClick{
    void cancelHotel(int position);
    void hotelNav(int position);
}

private class HotelBtnClickListener implements View.OnClickListener{
    private int position;

    public HotelBtnClickListener( int position) {
        this.position = position;
    }

    @Override
    public void onClick(View v) {
        Map<String,Object>  item = list.get(position);
        switch (v.getId()){
            case R.id.item_btn_comment:
                //评论
                Intent intent = new Intent(context,SendCommentActivity.class);
                intent.putExtra("supplierId",item.get("supplierId")+"");
                intent.putExtra("supplierName",item.get("supplierName")+"");
                intent.putExtra("id",item.get("id")+"");
                String ticketType = item.get("type")+"";
                ticketType = ticketType.substring(0,ticketType.indexOf("."));
                intent.putExtra("ticketType",Integer.parseInt(ticketType));
                intent.putExtra("type",3);
                context.startActivity(intent);
                break;
            case R.id.item_btn_return:
                //退订酒店订单
//                Map<String,Object> params = new HashMap<>();
//
//                params.put("ciphertext","test");
//                params.put("employeeCode",new AppUtils().getUserInfo(context).getEmployeeCode());
//                params.put("loginType", URL.loginType);
//                params.put("orderID",item.get("id"));//订单id
//                params.put("style",item.get("supplierId"));//渠道id
//                Retrofit retrofit = new RequestUtil().getRequestClient(URL.ZHONGCHE_93);
//                NetWorkRequest request = retrofit.create(NetWorkRequest.class);
//                Call<Map<String,Object>> call = request.cancelHotelOrder(params);
//                new RequestUtil().requestData(call, (RequestCallback) this,1,"正在提交数据...",false,context);
                itemBtnClick.cancelHotel(position);
                break;
            case  R.id.item_btn_nav:
                itemBtnClick.hotelNav(position);
                break;
        }
    }
}

    class ViewHolder{
        TextView orderType,hotelName,price,time,reason,address,
                status,btnNav,btnReturn,btnChange,btnComment,channelTv;
        TextView empNameTv;
        TextView cancelTv;//免费取消截止时间
        TextView xyTv,yjTv,canCancelTv;//协议、月结、是否可以取消
    }
}
