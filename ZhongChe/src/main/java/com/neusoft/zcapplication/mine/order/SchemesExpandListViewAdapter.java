package com.neusoft.zcapplication.mine.order;

import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.neusoft.zcapplication.Constant.Constant;
import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.http.URL;
import com.neusoft.zcapplication.tools.LogUtil;
import com.neusoft.zcapplication.tools.ToastUtil;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class SchemesExpandListViewAdapter extends BaseExpandableListAdapter {
    private List<Map<String, Object>> list;
    private Context context;
    private SchemesExpandListViewAdapter adapter;
    private boolean showArrow = true;

    public SchemesExpandListViewAdapter(Context context, List<Map<String, Object>> list) {
        this.context = context;
        this.list = list;
    }

    public void setAdapter(SchemesExpandListViewAdapter adapter) {
        this.adapter = adapter;
    }

    public void setListData(String string) {
        Gson gson = new Gson();
        List<Map<String, Object>> strList = gson.fromJson(string, List.class);
        this.list = strList;
    }

    public List<Map<String, Object>> getList() {
        return this.list;
    }

    public void setList(List<Map<String, Object>> list) {
        this.list = list;
    }

    public void setShowArrow(boolean showArrow) {
        this.showArrow = showArrow;
    }

    //折叠框个数
    @Override
    public int getGroupCount() {
        //   return generalsTypes.length;
        return list.size();
    }

    //折叠框子item个数
    @Override
    public int getChildrenCount(int groupPosition) {
        //    return generals[groupPosition].length;
//    	String str = list.get(groupPosition).get("detail").toString();
        List<Map<String, Object>> infor = (List<Map<String, Object>>) (list.get(groupPosition).get("detail"));
        if (null == (list.get(groupPosition).get("detail"))) {
            return 0;
        } else {
            return infor.size();
        }
    }

    @Override
    public Map<String, Object> getGroup(int groupPosition) {
        //    return generalsTypes[groupPosition];
        return list.get(groupPosition);
    }

    //某个子item的数据
    @Override
    public Map<String, Object> getChild(int groupPosition, int childPosition) {

//    	String inforStr = list.get(groupPosition).get("detail").toString();
//    	List<Map<String,Object>> infor = new Gson().fromJson(inforStr,List.class);
        List<Map<String, Object>> infor = (List<Map<String, Object>>) (list.get(groupPosition).get("detail"));

        return infor.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        GroupViewHolder holder = null;
        if (null == convertView) {
            holder = new GroupViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_flight_schemes_passenger_list, null);
            holder.arrowImg = (ImageView) convertView.findViewById(R.id.icon_down_go);

            holder.passenger = (TextView) convertView.findViewById(R.id.tv_passenger);
            holder.departDate = (TextView) convertView.findViewById(R.id.tv_depart_date);
            holder.departTime = (TextView) convertView.findViewById(R.id.tv_depart_time);
            holder.arrivalTime = (TextView) convertView.findViewById(R.id.tv_arrival_time);
            holder.type = (TextView) convertView.findViewById(R.id.tv_type);
            holder.duration = (TextView) convertView.findViewById(R.id.tv_duration);
            holder.fromCityTv = (TextView) convertView.findViewById(R.id.item_exp_group_from_city);
            holder.toCityTv = (TextView) convertView.findViewById(R.id.item_exp_group_to_city);
            holder.supplierImg = (ImageView) convertView.findViewById(R.id.icon_supplierImg);

            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }
        Map<String, Object> item = getGroup(groupPosition);
        holder.passenger.setText(null == item.get("PASSENGER") ? "" : item.get("PASSENGER") + "");
        holder.departDate.setText(null == item.get("DEPARTDATE") ? "" : item.get("DEPARTDATE") + "");
        holder.departTime.setText(null == item.get("DEPARTTIME") ? "" : item.get("DEPARTTIME") + "");
        holder.arrivalTime.setText(null == item.get("ARRIVETIME") ? "" : item.get("ARRIVETIME") + "");
        holder.type.setText(null == item.get("TYPE") ? "" : item.get("TYPE") + "");
        String durationTime = null == item.get("DURATION") ? "" : item.get("DURATION").toString();
        String toAirport = null == item.get("ARRIVEAIRPORT") ? "" : item.get("ARRIVEAIRPORT").toString();
        String fromAirport = null == item.get("DEPARTAIRPORT") ? "" : item.get("DEPARTAIRPORT").toString();
        double supplierId = null == item.get("SUPPLIERID") ? 0 : (double) item.get("SUPPLIERID");
        holder.duration.setText(durationTime);
        holder.fromCityTv.setText(fromAirport);
        holder.toCityTv.setText(toAirport);
        if (isExpanded) {
            //展开
            holder.arrowImg.setImageResource(R.mipmap.icon_arrow_up_gray);
        } else {
            holder.arrowImg.setImageResource(R.mipmap.icon_arrow_down_gray);
        }
        if (!showArrow) {
            holder.arrowImg.setVisibility(View.INVISIBLE);
        }
        String url = null == item.get("SUPPLIERLOGO") ? "" : item.get("SUPPLIERLOGO").toString();
        if (8 == supplierId) {
            if (!TextUtils.isEmpty(url)) {
                String logoUrl = url.substring(3);
                Picasso.with(context).load("http://58.20.212.75:9001/travel-web/" + logoUrl).
                        error(R.mipmap.img_fanjia_logo)
                        .into(holder.supplierImg);
            } else {
                Picasso.with(context).load(R.mipmap.img_fanjia_logo)
                        .into(holder.supplierImg);
            }
        } else {
            if (!TextUtils.isEmpty(url)) {
                String logoUrl = url.substring(3);
                Picasso.with(context).load("http://58.20.212.75:9001/travel-web/" + logoUrl).into(holder.supplierImg);
            }
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder holder;
        if (null == convertView) {
            holder = new ChildViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_flight_leg, parent, false);

            holder.startTimeTv = (TextView) convertView.findViewById(R.id.exp_child_start_time);
            holder.endTimeTv = (TextView) convertView.findViewById(R.id.exp_child_end_time);
            holder.startDateTv = (TextView) convertView.findViewById(R.id.exp_child_start_date);
            holder.endDateTv = (TextView) convertView.findViewById(R.id.exp_child_end_date);

            holder.dptCity = (TextView) convertView.findViewById(R.id.exp_child_start_city);
            holder.desCity = (TextView) convertView.findViewById(R.id.exp_child_des_city);
            holder.dptAirport = (TextView) convertView.findViewById(R.id.exp_child_dpt_airport);
            holder.desAirport = (TextView) convertView.findViewById(R.id.exp_child_des_airport);
            holder.flightInfoTv = (TextView) convertView.findViewById(R.id.exp_child_flight_info);
            holder.durationTv = (TextView) convertView.findViewById(R.id.exp_child_duration);
            holder.tranInfoTv = (TextView) convertView.findViewById(R.id.exp_child_tran_info);//中转信息

//    		holder.imageView=(ImageView)convertView.findViewById(R.id.fodder_expandable_child_item_play);
//    		holder.nameTv=(TextView)convertView.findViewById(R.id.fodder_expandable_child_item_name);
//    		holder.inforTv=(TextView)convertView.findViewById(R.id.fodder_expandable_child_item_infor);
//    		holder.checkTv=(ImageView)convertView.findViewById(R.id.fodder_expandable_child_item_check);
//    		holder.centerLayout = (LinearLayout)convertView.findViewById(R.id.fodder_expandable_child_item_center);

            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }
//    	holder.imageView.setOnClickListener(new ChildClick(1,groupPosition, childPosition));
//    	holder.checkTv.setOnClickListener(new ChildClick(0,groupPosition, childPosition));
//    	holder.centerLayout.setOnClickListener(new ChildClick(0,groupPosition, childPosition));
//    	
        Map<String, Object> map = getChild(groupPosition, childPosition);
        String endTime = map.get("ARRIVETIME").toString();
        String desAirport = map.get("ARRIVEAIRPORT").toString();
        String startTime = map.get("DEPARTTIME").toString();
        String dptAirport = map.get("DEPARTAIRPORT").toString();
        String companyName = map.get("CARRIERNAME").toString();
        String flightNo = map.get("FLIGHT").toString();
        //出发到达航站楼
        String ft = null == map.get("FROMTERMINAL") ? "" : map.get("FROMTERMINAL").toString().replace(".0", "");
        String tt = null == map.get("TOTERMINAL") ? "" : map.get("TOTERMINAL").toString().replace(".0", "");
        if (ft.equals("--")) {
            holder.dptAirport.setText(dptAirport);
        } else {
            holder.dptAirport.setText(dptAirport + ft);
        }
        if (tt.equals("--")) {
            holder.desAirport.setText(desAirport);
        } else {
            holder.desAirport.setText(desAirport + tt);
        }

//    	String arrTime = map.get("ARRIVETIME").toString();

        String duration = map.get("DURATION").toString();//飞行时长

        holder.startTimeTv.setText(startTime);
        holder.endTimeTv.setText(endTime);
//		holder.dptAirport.setText(dptAirport);
//		holder.desAirport.setText(desAirport);
        //航司，航班号信息
        String infoStr = companyName + flightNo;
        holder.flightInfoTv.setText(infoStr);
        holder.durationTv.setText(duration);
        String tranCity = null == map.get("TRANSFERNAME") ? "" : map.get("TRANSFERNAME").toString();
        String tranTime = null == map.get("TRANSFERTIME") ? "" : map.get("TRANSFERTIME").toString();
        if (!tranCity.equals("") || !tranTime.equals("")) {
            String tranStr = tranCity + "  " + tranTime;
            holder.tranInfoTv.setText(tranStr);//中转信息
            holder.tranInfoTv.setVisibility(View.VISIBLE);
        } else {
            holder.tranInfoTv.setText("");//中转信息
            holder.tranInfoTv.setVisibility(View.GONE);
        }
        //出发、到达日期
        String startDate = null == map.get("DEPARTDATE") ? "" : map.get("DEPARTDATE").toString();
        String endDate = null == map.get("ARRIVEDATE") ? "" : map.get("ARRIVEDATE").toString();
        holder.startDateTv.setText(startDate);
        holder.endDateTv.setText(endDate);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class GroupViewHolder {
        ImageView arrowImg, supplierImg;
        TextView passenger;
        TextView departDate;
        TextView departTime;
        TextView arrivalTime;
        TextView type;
        TextView duration;
        TextView fromCityTv, toCityTv;
    }

    class ChildViewHolder {
        TextView startTimeTv, endTimeTv, startDateTv, endDateTv;
        TextView dptCity, desCity;
        TextView dptAirport, desAirport;
        TextView flightInfoTv, durationTv, tranInfoTv;
    }
}
