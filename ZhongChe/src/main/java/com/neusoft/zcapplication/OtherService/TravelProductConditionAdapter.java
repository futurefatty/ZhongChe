package com.neusoft.zcapplication.OtherService;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.entity.GetAllCarType;

import java.util.List;

/**
 * Created by Administrator on 2017/8/22.
 * 旅游产品筛选条件适配器
 */

public class TravelProductConditionAdapter extends BaseAdapter{

    private Context context;
    private List<String> list;

    public TravelProductConditionAdapter(Context context, List<String> list){
        this.context = context;
        this.list = list;
    }
    public void setList(List<String> list){
        this.list = list;
    }
    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public String getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        String name = getItem(position);
        if(null == convertView){
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_business_car_apply_popu,null);

            holder.tv_name =(TextView) convertView.findViewById(R.id.tv_name);

            convertView.setTag(holder);
        }else{
            holder =(ViewHolder) convertView.getTag();
        }
        holder.tv_name.setText(name);
        return convertView;
    }

    class ViewHolder{
        TextView tv_name;
    }
}
