package com.neusoft.zcapplication.dataAnalyze;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.entity.StaffGoModel;
import com.neusoft.zcapplication.tools.DateUtils;

import java.util.Date;
import java.util.List;

public class StaffGoListAdapter extends BaseAdapter {
        private Context context;
        private List<StaffGoModel> list;

        public StaffGoListAdapter(Context context, List<StaffGoModel> list){
            this.context = context;
            this.list = list;
        }
        public void setList(List<StaffGoModel> list){
            this.list = list;
        }
        @Override
        public int getCount() {
            return this.list.size();
        }

        @Override
        public StaffGoModel getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            StaffGoModel staffGoModel = getItem(position);
            if(null == convertView){
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.item_staffgo,null);
                holder.tv_flight_name= (TextView) convertView.findViewById(R.id.tv_flight_name);
                holder.tv_name= (TextView) convertView.findViewById(R.id.tv_name);
                holder.tv_depart= (TextView) convertView.findViewById(R.id.tv_depart);
                holder.tv_to_city= (TextView) convertView.findViewById(R.id.tv_toCity);
                holder.tv_mobile= (TextView) convertView.findViewById(R.id.tv_mobile);
                holder.tv_from_city= (TextView) convertView.findViewById(R.id.tv_fromCity);
                holder.tv_endTime= (TextView) convertView.findViewById(R.id.tv_to_time);
                holder.tv_startTime= (TextView) convertView.findViewById(R.id.tv_from_time);
                holder.tv_flight_no = (TextView) convertView.findViewById(R.id.tv_flight_no);
                convertView.setTag(holder);
            }else{
                holder =(ViewHolder) convertView.getTag();
            }
            holder.tv_flight_name.setText(staffGoModel.getCarrierName());
            holder.tv_name.setText(staffGoModel.getEmployeeName());
            holder.tv_depart.setText(staffGoModel.getAccountEntiyt());
            holder.tv_to_city.setText(staffGoModel.getToCityName());
            holder.tv_mobile.setText(staffGoModel.getMobil());
            holder.tv_from_city.setText(staffGoModel.getFromCityName());
            holder.tv_startTime.setText("出发时间:" + staffGoModel.getFromPlanDate());
            holder.tv_endTime.setText("到达时间:" +staffGoModel.getToPlanDate());
            holder.tv_flight_no.setText(staffGoModel.getFlightNo());
            return convertView;
        }

        class ViewHolder{
            TextView tv_flight_name,tv_from_city,tv_to_city,tv_startTime,tv_endTime,tv_name,tv_mobile,tv_depart,tv_flight_no;
        }

}

