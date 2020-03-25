package com.neusoft.zcapplication.approval;

import android.content.Context;
import android.nfc.cardemulation.OffHostApduService;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.neusoft.zcapplication.Bean.PersonItem;
import com.neusoft.zcapplication.R;

import java.util.List;
import java.util.Map;

public class supplierListAdapter extends BaseAdapter {
    private Context context;
    private boolean[] ischeckArr;
    private List<Map<String, Object>> supplierDataList;
    public supplierListAdapter(Context context,List<Map<String,Object>> list){
        this.context = context;
        this.supplierDataList = list;
        if (ischeckArr == null){
            ischeckArr = new boolean[list.size()];
            for (int i = 0; i < list.size(); i++) {
                ischeckArr[i] = true;
            }
        }
    }
    @Override
    public int getCount() {
        if (this.supplierDataList != null){
            return this.supplierDataList.size();
        }else {
            return 0;
        }
    }

    @Override
    public Object getItem(int i) {
        return supplierDataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        final supplierListAdapter.ViewHolder holder;
        if(null == convertView){
            holder = new supplierListAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.supplier_item,null);
//            holder.item1 =(PxTextView) convertView.findViewById(R.id.item_order_list_item1);
           holder.img = (ImageView)convertView.findViewById(R.id.img);
           holder.name = (TextView)convertView.findViewById(R.id.text);
            convertView.setTag(holder);
        }else{
            holder =(supplierListAdapter.ViewHolder) convertView.getTag();
        }
        if (ischeckArr[position]) {
            holder.img.setImageResource(R.drawable.icon_selection_check);
        } else {
            holder.img.setImageResource(R.drawable.icon_selection_uncheck);
        }


        return convertView;
    }

    private class ViewHolder{
        ImageView img;
        TextView name;
    }
    public void choiceState(int post) {
        /**
         *  传递过来所点击的position,如果是本身已经是选中状态,就让他变成不是选中状态,
         *  如果本身不是选中状态,就让他变成选中状态
         */

        ischeckArr[post] = ischeckArr[post] == true ? false : true;

        this.notifyDataSetChanged();
    }

}
