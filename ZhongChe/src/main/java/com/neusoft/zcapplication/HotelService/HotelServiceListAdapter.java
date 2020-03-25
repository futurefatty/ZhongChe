package com.neusoft.zcapplication.HotelService;

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
 * Created by Administrator on 2017/8/22.
 * 酒店服务适配器
 */

public class HotelServiceListAdapter extends BaseAdapter{
    private Context context;
    private List<Map<String,Object>> list;
    private int type; //

    public HotelServiceListAdapter(Context context, List<Map<String,Object>> list, int type){
        this.context = context;
        this.list = list;
        this.type = type;
    }
    public void setList(List<Map<String,Object>> list){
    }
    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public Map<String,Object> getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        Map<String,Object> item = getItem(position);
        if(null == convertView){
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_ticket_service_list,null);

            holder.item_text =(TextView) convertView.findViewById(R.id.item_text);
            holder.item_pic =(ImageView) convertView.findViewById(R.id.item_pic);

            convertView.setTag(holder);
        }else{
            holder =(ViewHolder) convertView.getTag();
        }
        holder.item_text.setText((String)item.get("text"));
        holder.item_pic.setImageResource((int)item.get("pic"));

        return convertView;
    }

    class ViewHolder{
        TextView item_text;
        ImageView item_pic;
    }
}
