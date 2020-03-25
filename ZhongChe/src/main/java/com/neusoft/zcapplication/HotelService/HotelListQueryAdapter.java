package com.neusoft.zcapplication.HotelService;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.neusoft.zcapplication.R;

import java.util.List;
import java.util.Map;

/**
 * 待确认入住酒店信息列表适配器
 */

public class HotelListQueryAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String,Object>> list;

    public HotelListQueryAdapter(Context context, List<Map<String, Object>> list) {
        this.context = context;
        this.list = list;
    }

    public List<Map<String, Object>> getList() {
        return list;
    }

    public void setList(List<Map<String, Object>> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
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
        ViewHolder holder ;
        if(null == convertView){
            holder = new ViewHolder();

            convertView = LayoutInflater.from(context).inflate(R.layout.item_query_hotel_order,null);
            holder.hotelNameTv = (TextView)convertView.findViewById(R.id.item_hotel_name);
            holder.dateTv = (TextView)convertView.findViewById(R.id.item_time);
            holder.addrTv = (TextView)convertView.findViewById(R.id.item_address);
            holder.statusTv = (TextView)convertView.findViewById(R.id.item_status);
            holder.channelTv = (TextView)convertView.findViewById(R.id.item_hotel_channel);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        Map<String,Object> map = list.get(position);
        String name = null == map.get("hotelName") ? "" : map.get("hotelName").toString();
        String orderApplyId = null == map.get("orderApplyId") ? "" : map.get("orderApplyId").toString();
        String fromTime = null == map.get("fromTime") ? "" : map.get("fromTime").toString();
        String toTime = null == map.get("toTime") ? "" : map.get("toTime").toString();
        String supplierName = null == map.get("supplierName") ? "" : map.get("supplierName").toString();
        String time = fromTime + "至" + toTime;

        holder.hotelNameTv.setText(name);
        holder.addrTv.setText("预定申请单号：" + orderApplyId);
        holder.dateTv.setText(time);
        holder.statusTv.setText("待确认");
        holder.channelTv.setText(supplierName);
        return convertView;
    }
    class ViewHolder{
        TextView hotelNameTv,dateTv,addrTv,statusTv,channelTv;
    }
}
