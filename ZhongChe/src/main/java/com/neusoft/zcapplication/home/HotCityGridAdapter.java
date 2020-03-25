package com.neusoft.zcapplication.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.entity.QueryHotCity;

import java.util.List;

/**
 * 热门城市适配器
 */

public class HotCityGridAdapter extends BaseAdapter {

    private List<QueryHotCity> list;
    private Context context;

    public HotCityGridAdapter(List<QueryHotCity> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setList(List<QueryHotCity> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public QueryHotCity getItem(int position) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_grid_single_tv, null);
            holder.nameTv = (TextView) convertView.findViewById(R.id.item_grid_single_textView);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        QueryHotCity queryHotCity = list.get(position);
        String cityName = queryHotCity.getCityName();
        String areaName = queryHotCity.getHotareaName();
//        String txt = cityName + " "  + areaName;
        holder.nameTv.setText(areaName);
        return convertView;
    }

    class ViewHolder{
        TextView nameTv;
    }

}
