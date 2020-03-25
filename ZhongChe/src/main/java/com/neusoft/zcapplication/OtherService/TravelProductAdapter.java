package com.neusoft.zcapplication.OtherService;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.entity.GetTravelProducts;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2017/8/22.
 * 旅游产品适配器
 */

public class TravelProductAdapter extends BaseAdapter{

    private Context context;
    private List<GetTravelProducts> list;

    public TravelProductAdapter(Context context, List<GetTravelProducts> list){
        this.context = context;
        this.list = list;
    }
    public void setList(List<GetTravelProducts> list){
        this.list = list;
    }
    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public GetTravelProducts getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        GetTravelProducts getTravelProducts = getItem(position);
        if(null == convertView){
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_travel_product,null);

            holder.iv_icon =(ImageView) convertView.findViewById(R.id.iv_icon);
            holder.tv_title =(TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_from_city =(TextView) convertView.findViewById(R.id.tv_from_city);
            holder.tv_to_city =(TextView) convertView.findViewById(R.id.tv_to_city);
            holder.tv_price =(TextView) convertView.findViewById(R.id.tv_price);
            holder.tv_time =(TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_day =(TextView) convertView.findViewById(R.id.tv_day);

            convertView.setTag(holder);
        }else{
            holder =(ViewHolder) convertView.getTag();
        }
        Picasso.with(context)
                .load(getTravelProducts.getSmallImage())
                .fit()
                .centerCrop()
                .into(holder.iv_icon);
        holder.tv_title.setText(getTravelProducts.getTitle());
        holder.tv_from_city.setText("出发地:"+getTravelProducts.getFromCityName());
        holder.tv_to_city.setText("目的地:"+getTravelProducts.getToCityName());
        holder.tv_price.setText("￥"+getTravelProducts.getPrice()+"起");
        holder.tv_time.setText(getTravelProducts.getTravelTime());
        holder.tv_day.setText("出游天数:"+getTravelProducts.getTripDay());
        return convertView;
    }

    class ViewHolder{
        ImageView iv_icon;
        TextView tv_title;
        TextView tv_from_city;
        TextView tv_to_city;
        TextView tv_price;
        TextView tv_time;
        TextView tv_day;
    }

}
