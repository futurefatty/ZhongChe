package com.neusoft.zcapplication.mine.backlog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.entity.GetBacklogData;
import com.neusoft.zcapplication.tools.LogUtil;
import com.neusoft.zcapplication.tools.StringUtil;

import java.util.List;
import java.util.Map;

/**
 * 我的待办详情页行程列表数据
 */

public class JobTripListAdapter extends BaseAdapter {
    private Context context;
    private List<GetBacklogData.TripInfoBean> list;

    public JobTripListAdapter(Context context, List<GetBacklogData.TripInfoBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public GetBacklogData.TripInfoBean getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(null == convertView){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_job_trip,null);
            holder.rl_job_trip_type = (RelativeLayout) convertView.findViewById(R.id.rl_job_trip_type);
            holder.tripTypeTv = (TextView)convertView.findViewById(R.id.item_job_trip_type);
            holder.fromCityTv = (TextView)convertView.findViewById(R.id.item_job_trip_from_city);
            holder.toCityTv = (TextView)convertView.findViewById(R.id.item_job_trip_to_city);
            holder.startDayTv = (TextView)convertView.findViewById(R.id.item_job_trip_time_start);
            holder.checkInTv = (TextView)convertView.findViewById(R.id.item_job_trip_time_checkin);
            holder.checkOutTv = (TextView)convertView.findViewById(R.id.item_job_trip_time_checkout);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        GetBacklogData.TripInfoBean tripInfoBean = list.get(position);
        String travelName = tripInfoBean.getTravelName();
        String toCity = tripInfoBean.getToCity();
        String fromCity = tripInfoBean.getFromCity();
        String fromDate = tripInfoBean.getFromDate();

        if(StringUtil.isEmpty(travelName)){
            holder.rl_job_trip_type.setVisibility(View.GONE);
        }else{
            holder.tripTypeTv.setText(travelName);
            holder.rl_job_trip_type.setVisibility(View.VISIBLE);
        }
        holder.fromCityTv.setText(fromCity);
        holder.toCityTv.setText(toCity);
        holder.startDayTv.setText(fromDate);

        return convertView;
    }
    class ViewHolder{
        RelativeLayout rl_job_trip_type;
        TextView fromCityTv,toCityTv,tripTypeTv,startDayTv;
        TextView checkInTv,checkOutTv;
    }
}
