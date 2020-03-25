package com.neusoft.zcapplication.mine.journey;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.neusoft.zcapplication.R;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/8.
 * 行程适配器
 */

public class NowJourneyAdapter extends BaseAdapter {
    private Context context;
    private int type ;//0 当前行程，1 历史行程
    private List<Map<String,Object>> list;
    public NowJourneyAdapter(Context context,int type,List<Map<String,Object>> list) {
        this.context = context;
        this.type = type;
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
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if(null == convertView){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_now_list,null);
            holder.channelTv = (TextView) convertView.findViewById(R.id.flight_date);
            holder.dptCity = (TextView) convertView.findViewById(R.id.item_now_dpt);
            holder.desCity = (TextView) convertView.findViewById(R.id.item_now_des);
            holder.dptAirport = (TextView) convertView.findViewById(R.id.item_now_dpt_airport);
            holder.desAirport = (TextView) convertView.findViewById(R.id.item_now_dpt_airport);
            holder.startTimeTv = (TextView) convertView.findViewById(R.id.item_now_start_time);
            holder.endTimeTv = (TextView) convertView.findViewById(R.id.item_now_end_time);
            holder.flightNoTv = (TextView) convertView.findViewById(R.id.item_now_flight);
            holder.arrowImg = (ImageView) convertView.findViewById(R.id.item_now_center_arrow);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        Map<String,Object> map = list.get(position);
        //1 国内机票 2 国际机票 3 酒店
        double type = map.get("type") == null ? 1 : Double.parseDouble(map.get("type").toString() );
        if(type == 1){
            holder.channelTv.setText("国内机票");
            String flightNo = map.get("flightNo").toString();
            String fromCity = map.get("fromCity").toString();//出发城市
            String toCity = map.get("toCity").toString();//出发城市

            holder.flightNoTv.setText(flightNo);
            holder.arrowImg.setVisibility(View.VISIBLE);
            holder.dptCity.setText(fromCity);
            holder.desCity.setText(toCity);
        }else if(type == 2){
            holder.channelTv.setText("国际机票");
            String flightNo = map.get("flightNo").toString();
            String fromCity = map.get("fromCity").toString();//出发城市

            holder.flightNoTv.setText(flightNo);
            holder.arrowImg.setVisibility(View.VISIBLE);
            holder.dptCity.setText(fromCity);
        }else{
            holder.channelTv.setText("酒店信息");
            String hotelName = map.get("hotelName").toString();
            String cityName = map.get("cityName").toString();//入住酒店所在城市的名称

            holder.arrowImg.setVisibility(View.GONE);
            holder.dptCity.setText(hotelName);
            holder.desCity.setText(cityName);
            holder.flightNoTv.setText("");
        }
        String fromDate = map.get("fromDate").toString();
        String backDate = map.get("backDate").toString();
        if(fromDate.length() > 3){
            holder.startTimeTv.setText(fromDate.substring(0,fromDate.length() - 3));
        }else{
            holder.startTimeTv.setText("--");
        }
        if(backDate.length() > 3){
            holder.endTimeTv.setText(backDate.substring(0,backDate.length() - 3));
        }else{
            holder.endTimeTv.setText("--");
        }

//        if(type == 0){
//            convertView = LayoutInflater.from(context).inflate(R.layout.item_now_list,null);
//        }else{
//            convertView = LayoutInflater.from(context).inflate(R.layout.item_history_list,null);
//        }
//
        return convertView;
    }
    class ViewHolder{
        TextView channelTv,dptCity,desCity,dptAirport,desAirport;
        TextView startTimeTv,endTimeTv, flightNoTv;
        ImageView arrowImg ;
    }
}
