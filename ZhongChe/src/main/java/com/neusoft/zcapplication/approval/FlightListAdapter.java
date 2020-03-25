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
import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.tools.DateUtils;
import com.neusoft.zcapplication.widget.TimeSelector;

import java.util.ArrayList;
import java.util.List;

/**
 * 航程列表适配器
 * Created by Administrator on 2017/8/22.
 */

public class FlightListAdapter extends BaseAdapter{
    private Context context;
    private List<FlightItem> list;
    private TimeSelector timeSelector;
    private TextView timeStart, time_checkout, time_checkin;
    private int timeTypeIndex;
    private SelectCity selectCity;
    private ShowPopupWin showPopupWin;
    private int item_position;
    private boolean showDelBtn;//显示删除按钮的标识
    private TripItemClickListener itemCellClick;
    public void setItemCellClick(TripItemClickListener itemCellClick) {
        this.itemCellClick = itemCellClick;
    }
    public FlightListAdapter(final Context context, List<FlightItem> list){
        this.context = context;
        this.list = list;
        this.showDelBtn = false;

        timeSelector = new TimeSelector(context, new TimeSelector.ResultHandler() {
            @Override
            public void handle(String time) {

                String timeStr = time.substring(0,10);
                if(timeTypeIndex == 1) {
                    //设置出发时间
                    timeStart.setText(timeStr);
                    getItem(item_position).setStartTime(timeStr);

                    time_checkin.setText(timeStr);
                    getItem(item_position).setCheckinTime(timeStr);
                    String nextDay = DateUtils.generalNextDayOfOneDay(timeStr,1);
                    time_checkout.setText(nextDay);
                    getItem(item_position).setCheckoutTime(nextDay);

                }else if(timeTypeIndex == 2) {
                    //设置酒店入住时间
                    //判断选择的入住时间是否在预定的机票出发时间之前
                    String startDate = timeStart.getText().toString().trim();
                    boolean bl = DateUtils.isFirstDayBeforeSecondDay(timeStr,startDate);
                    if(bl&&!timeStr.equals(startDate)){
                        Toast.makeText(context,"请选择" + startDate + "当天或之后的日期",Toast.LENGTH_SHORT).show();
                        return;
                    }else{
//                        time_checkin.setText(timeStr);
//                        getItem(item_position).setCheckoutTime(timeStr);
//                        getItem(item_position).setCheckinTime(timeStr);

                        /**判断当前选择的入住时间是否在离店时间之前，
                         * 若不是，则设置离店时间为当前时间的第二天的日期
                         * 否则，直接设置离店时间
                         */
                        String leftDay = time_checkout.getText().toString().trim();
                        if(leftDay.equals("")){
                            time_checkin.setText(timeStr);
                            getItem(item_position).setCheckinTime(timeStr);
                        }else{
                            boolean bool = DateUtils.checkinDayUsable(timeStr,leftDay);
                            if(!bool){
                                time_checkin.setText(timeStr);
                                String nextDay = DateUtils.generalNextDayOfOneDay(timeStr,1);
                                time_checkout.setText(nextDay);
                                getItem(item_position).setCheckinTime(timeStr);
                                getItem(item_position).setCheckoutTime(nextDay);
                            }else{
                                time_checkin.setText(timeStr);
                                getItem(item_position).setCheckinTime(timeStr);
                            }
                        }
                    }


                }else if(timeTypeIndex == 3) {
                    //设置酒店离店时间
                    //判断离店时间是否在入住时间之前
                    String inDay = time_checkin.getText().toString().trim();
                    boolean bool = DateUtils.isFirstDayBeforeSecondDay(timeStr,inDay);
                    if(bool){
                        Toast.makeText(context,"请选择" + inDay + "之后的日期",Toast.LENGTH_SHORT).show();
                    }else{
                        time_checkout.setText(timeStr);
                        getItem(item_position).setCheckoutTime(timeStr);
                    }
                }
            }
        }, DateUtils.generalBeginDate(), DateUtils.generalEndDate());
        timeSelector.setMode(TimeSelector.MODE.YMD);
    }

    public boolean isShowDelBtn() {
        return showDelBtn;
    }

    public void setShowDelBtn(boolean showDelBtn) {
        this.showDelBtn = showDelBtn;
    }

    public List<FlightItem> getList() {
        if(null == list){
            return new ArrayList<FlightItem>();
        }else{
            return list;
        }
    }

    public void setList(List<FlightItem> list){
        this.list = list;
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public FlightItem getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final FlightItem item = getItem(position);
        if(null == convertView){
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_flight_list,null);

//            holder.item1 =(PxTextView) convertView.findViewById(R.id.item_order_list_item1);
            holder.del_trip =(ImageView) convertView.findViewById(R.id.icon_del_trip);
            holder.city_form =(TextView) convertView.findViewById(R.id.city_from);
            holder.city_to =(TextView) convertView.findViewById(R.id.city_to);
            holder.time_start = (TextView)convertView.findViewById(R.id.time_start);
            holder.time_checkout = (TextView)convertView.findViewById(R.id.time_checkout);
            holder.time_checkin = (TextView)convertView.findViewById(R.id.time_checkin);
            holder.btn_exchange = (LinearLayout) convertView.findViewById(R.id.btn_exchange);
            holder.trip_mode = (TextView) convertView.findViewById(R.id.trip_mode);
            holder.ck_bookHotel = (CheckBox) convertView.findViewById(R.id.ck_book_hotel);
            holder.btn_bookHotel = (LinearLayout) convertView.findViewById(R.id.btn_book_hotel);
            holder.ly_hotel_day = (LinearLayout) convertView.findViewById(R.id.ly_hotel_day);

            convertView.setTag(holder);
        }else{
            holder =(ViewHolder) convertView.getTag();
        }

        holder.time_start.setText(item.getStartTime());

//        //点击“删除航程”，显示或隐藏item前面的删除按钮
//        boolean isShowDel = item.isShowDel();
        if(showDelBtn) {
            holder.del_trip.setVisibility(View.VISIBLE);
        }else {
            holder.del_trip.setVisibility(View.GONE);
        }
        holder.del_trip.setOnClickListener(new DelListener(convertView,position));
        holder.city_form.setText(item.getFromCity());
        holder.city_form.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectCity!=null) {
                    selectCity.selectFromcity(position, 0);
                }
            }
        });
        holder.city_to.setText(item.getToCity());
        holder.city_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectCity!=null) {
                    selectCity.selectFromcity(position, 1);
                }
            }
        });
        //选择出发时间
        holder.time_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != itemCellClick){
                    itemCellClick.selectFlightDateByType(position,0,0);
                }
//                timeStart = holder.time_start;
//                time_checkout = holder.time_checkout;
//                time_checkin = holder.time_checkin;
//                timeTypeIndex = 1;
//                item_position = position;
//                timeSelector.show();


            }
        });
        //选择入住时间
        holder.time_checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                timeStart = holder.time_start;
//                time_checkin = holder.time_checkin;
//                time_checkout = holder.time_checkout;
//                timeTypeIndex = 2;
//                item_position = position;
//                timeSelector.show();

                if(null != itemCellClick){
                    itemCellClick.selectFlightDateByType(position,0,1);
                }
            }
        });
        //选择离店时间
        holder.time_checkin.setText(item.getCheckinTime());
        holder.time_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                time_checkout = holder.time_checkout;
//                time_checkin = holder.time_checkin;
//                timeTypeIndex = 3;
//                item_position = position;
//                timeSelector.show();

                if(null != itemCellClick){
                    itemCellClick.selectFlightDateByType(position,0,2);
                }
            }
        });
        holder.time_checkout.setText(item.getCheckoutTime());
        holder.btn_exchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = item.getFromCity();
                String tempCode = item.getFromCityCode();
                item.setFromCity(item.getToCity());
                item.setFromCityCode(item.getToCityCode());
                item.setToCityCode(tempCode);
                item.setToCity(temp);
                holder.city_form.setText(item.getFromCity());
                holder.city_to.setText(item.getToCity());
            }
        });
        holder.trip_mode.setText(item.getTripMode());
        holder.trip_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(showPopupWin!=null) {
                    showPopupWin.selectTripMode(position);
                }
            }
        });

        //点击酒店预订时的事件
        holder.btn_bookHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(item.isBookHotel() == 1) {
                    holder.ck_bookHotel.setChecked(false);
                    list.get(position).setBookHotel(0);
                    holder.ly_hotel_day.setVisibility(View.GONE);
                }
                else {
                    holder.ck_bookHotel.setChecked(true);
                    list.get(position).setBookHotel(1);
                    holder.ly_hotel_day.setVisibility(View.VISIBLE);
                }
            }
        });
        if(item.isBookHotel() == 1) {
            holder.ly_hotel_day.setVisibility(View.VISIBLE);
        }
        else {
            holder.ly_hotel_day.setVisibility(View.GONE);
        }

        if(item.isShowItem()) {
            convertView.setVisibility(View.VISIBLE);
        }
        else {
            convertView.setVisibility(View.GONE);
        }
        return convertView;
    }

    public void setSelectCity(SelectCity selectCity) {
        this.selectCity = selectCity;
    }

    public void setShowPopupWin(ShowPopupWin showPopupWin) {
        this.showPopupWin = showPopupWin;
    }

    interface SelectCity {
        void selectFromcity(int position, int type);
    }

    interface ShowPopupWin {
        void selectTripMode(int position);
    }

    class ViewHolder{
        TextView city_form, city_to, time_start, time_checkout, time_checkin, trip_mode;
        ImageView del_trip;
        LinearLayout btn_exchange, btn_bookHotel, ly_hotel_day;
        CheckBox ck_bookHotel;
    }

    class DelListener implements View.OnClickListener {
        private View view;
        private int position;

        public DelListener (View view, int position) {
            this.view = view;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            list.remove(position);
            view.setVisibility(View.GONE);
        }
    }
}
