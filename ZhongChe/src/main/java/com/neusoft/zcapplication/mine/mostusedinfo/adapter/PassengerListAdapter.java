package com.neusoft.zcapplication.mine.mostusedinfo.adapter;

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
 * 常用乘客列表适配器
 */

public class PassengerListAdapter extends BaseAdapter {
    private List<Map<String,Object>> list ;
    private Context context;

    public PassengerListAdapter(List<Map<String, Object>> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public List<Map<String, Object>> getList() {
        return list;
    }

    public void setList(List<Map<String, Object>> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
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
        ViewHolder holder ;
        if(null == convertView){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_passenger,null);

            holder.nameTv = (TextView)convertView.findViewById(R.id.item_psg_name);
//            holder.typeTv = (TextView)convertView.findViewById(R.id.item_psg_type);
//            holder.dataTv = (TextView)convertView.findViewById(R.id.item_psg_type_info);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        Map<String,Object> map = list.get(position);
        String name = null == map.get("cnName") ? "" : map.get("cnName").toString();
//        String employeeCode = null == map.get("employeeCode") ? "" : map.get("employeeCode").toString();

        holder.nameTv.setText(name);
//        holder.typeTv.setText("员工编号:" + employeeCode);
//        holder.dataTv.setText(data);
        return convertView;
    }

    class ViewHolder{
        TextView nameTv,typeTv,dataTv;
    }
}
