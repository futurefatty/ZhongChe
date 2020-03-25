package com.neusoft.zcapplication.approval;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.neusoft.zcapplication.Bean.FlightItem;
import com.neusoft.zcapplication.Bean.HotelTripItem;
import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.tools.DateUtils;
import com.neusoft.zcapplication.widget.TimeSelector;

import java.util.List;

/**
 * 酒店行程列表适配器
 */

public class HotelTripListAdapter extends BaseAdapter{
    private Context context;
    private List<HotelTripItem> list;
    private boolean showDetBtn;//显示删除按钮的标识
    private TripItemClickListener itemCellClick;
    public HotelTripListAdapter(final Context context, List<HotelTripItem> list){
        this.context = context;
        this.list = list;
        this.showDetBtn = false;//默认设置不显示删除按钮
    }

    public void setItemCellClick(TripItemClickListener itemCellClick) {
        this.itemCellClick = itemCellClick;
    }

    public List<HotelTripItem> getList() {
        return list;
    }

    public void setList(List<HotelTripItem> list) {
        this.list = list;
    }

    public void toggleShowDelBtn() {
        if(showDetBtn){
            showDetBtn = false;
        }else{
            showDetBtn = true;
        }
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public HotelTripItem getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(null == convertView){
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_hotel_trip_list,null);
            holder.delLayout = (LinearLayout)convertView.findViewById(R.id.item_hotel_trip_det_btn) ;
            holder.dateLayout = (LinearLayout)convertView.findViewById(R.id.item_hotel_trip_date_layout) ;
            holder.toCityTv =(TextView) convertView.findViewById(R.id.item_hotel_trip_to_city);
            holder.checkInTv =(TextView) convertView.findViewById(R.id.item_hotel_trip_checkIn_date);
            holder.checkOutTv =(TextView) convertView.findViewById(R.id.item_hotel_trip_checkOut_date);
            holder.daysTv =(TextView) convertView.findViewById(R.id.item_hotel_trip_days);

            convertView.setTag(holder);
        }else{
            holder =(ViewHolder) convertView.getTag();
        }
        HotelTripItem item = getItem(position);

        String cityName = null == item.getCityName() ? "" : item.getCityName();
        String checkInDate = null == item.getCheckInDate() ? "" : item.getCheckInDate();
        String checkOutDate = null == item.getCheckOutDate() ? "" : item.getCheckOutDate();
        if(!checkInDate.equals("") && !checkOutDate.equals("")){
            int differDays = DateUtils.differDaysWithDays(checkOutDate,checkInDate);
            holder.daysTv.setText(differDays + "晚");
        }else{
            holder.daysTv.setText("");
        }
        holder.toCityTv.setText(cityName);
        holder.checkInTv.setText(checkInDate);
        holder.checkOutTv.setText(checkOutDate);
        holder.toCityTv.setOnClickListener(new ItemClickLister(position));
        holder.dateLayout.setOnClickListener(new ItemClickLister(position));
        holder.delLayout.setOnClickListener(new ItemClickLister(position));
        if(showDetBtn){
            holder.delLayout.setVisibility(View.VISIBLE);
        }else{
            holder.delLayout.setVisibility(View.GONE);
        }
        return convertView;
    }

    class ItemClickLister implements View.OnClickListener{
        private int position;

        public ItemClickLister(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.item_hotel_trip_date_layout:
                    if(null != itemCellClick){
                        itemCellClick.selectDateByType(position,2);
                    }
                    break;
                case R.id.item_hotel_trip_to_city:
                    if(null != itemCellClick){
                        itemCellClick.selectDesCityByType(position,2);
                    }
                    break;
                case R.id.item_hotel_trip_det_btn:
                    if(null != itemCellClick){
                        itemCellClick.delTripByType(position,2);
                    }
                    break;
            }
        }
    }

    class ViewHolder{
        TextView toCityTv, checkInTv, checkOutTv,daysTv;
        LinearLayout delLayout,dateLayout;
    }
}
