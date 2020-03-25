package com.neusoft.zcapplication.CarService;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.entity.GetAllCarRange;
import com.neusoft.zcapplication.entity.GetAllCarType;

import java.util.List;

/**
 * Created by Administrator on 2017/8/22.
 */

public class BusinessCarApplyPopuCarRangeAdapter extends BaseAdapter{

    private Context context;
    private List<GetAllCarRange> list;

    public BusinessCarApplyPopuCarRangeAdapter(Context context, List<GetAllCarRange> list){
        this.context = context;
        this.list = list;
    }
    public void setList(List<GetAllCarRange> list){
        this.list = list;
    }
    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public GetAllCarRange getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        GetAllCarRange getAllCarRange = getItem(position);
        if(null == convertView){
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_business_car_apply_popu,null);

            holder.tv_name =(TextView) convertView.findViewById(R.id.tv_name);

            convertView.setTag(holder);
        }else{
            holder =(ViewHolder) convertView.getTag();
        }
        holder.tv_name.setText(getAllCarRange.getRangeName());
        return convertView;
    }

    class ViewHolder{
        TextView tv_name;
    }
}
