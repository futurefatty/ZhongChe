package com.neusoft.zcapplication.HotelService;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.neusoft.zcapplication.R;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/22.
 * 税票凭证抬头适配器
 */

public class TaxListAdapter extends BaseAdapter{
    private Context context;
    private List<Map<String,Object>> list;

    public TaxListAdapter(Context context, List<Map<String,Object>> list){
        this.context = context;
        this.list = list;
    }
    public void setList(List<Map<String,Object>> list){
        this.list = list;
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
            convertView = inflater.inflate(R.layout.item_common_list,null);
            holder.item1 =(TextView) convertView.findViewById(R.id.item_base_list_item1);
            holder.item2 =(TextView) convertView.findViewById(R.id.item_base_list_item2);
            holder.item3 =(TextView) convertView.findViewById(R.id.item_base_list_item3);
            holder.item4 =(TextView) convertView.findViewById(R.id.item_base_list_item4);

            convertView.setTag(holder);
        }else{
            holder =(ViewHolder) convertView.getTag();
        }

        holder.item1.setText(item.get("companyName")+"");
        holder.item3.setVisibility(View.GONE);
        holder.item4.setVisibility(View.GONE);

        return convertView;
    }

    class ViewHolder{
        TextView item1,item2,item3,item4;
    }
}
