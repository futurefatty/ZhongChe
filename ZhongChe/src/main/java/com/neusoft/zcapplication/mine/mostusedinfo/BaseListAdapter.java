package com.neusoft.zcapplication.mine.mostusedinfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.neusoft.zcapplication.R;

import java.util.List;


public class BaseListAdapter extends BaseAdapter{
    private Context context;
    private List<String[]> list;
    private int type; //0,1,2 对应 旅客、报销凭证、签证 3个Fragment

    public BaseListAdapter(Context context, List<String[]> list, int type){
        this.context = context;
        this.list = list;
        this.type = type;
    }
    public void setList(List<String[]> list){
        this.list = list;
    }
    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public String[] getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        String[] items = getItem(position);
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

        if(type == 0) {
            holder.item1.setText(items[0]);
            holder.item3.setText(items[1]);
            holder.item4.setText(items[2]);
        }
        else if(type == 1) {
            holder.item1.setText(items[0]);
            holder.item3.setVisibility(View.GONE);
            holder.item4.setVisibility(View.GONE);
        }
        else {
            holder.item2.setVisibility(View.VISIBLE);
            holder.item1.setText(items[0]);
            holder.item2.setText(items[1]);
            holder.item3.setText(items[2]);
            holder.item4.setText(items[3]);
        }

        return convertView;
    }

    class ViewHolder{
        TextView item1,item2,item3,item4;
    }
}
