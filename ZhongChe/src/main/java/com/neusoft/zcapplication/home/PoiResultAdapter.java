package com.neusoft.zcapplication.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.neusoft.zcapplication.R;

import java.util.List;

/**
 * 搜索结果适配器
 */

public class PoiResultAdapter extends BaseAdapter {
    private List<PoiItem> list;
    private Context context;

    public PoiResultAdapter(List<PoiItem> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public List<PoiItem> getList() {
        return list;
    }

    public void setList(List<PoiItem> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public PoiItem getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holer;
        if(null == convertView){
            holer = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_list_poi,null);
            holer.nameTv = (TextView)convertView.findViewById(R.id.item_list_poi_tv);
            convertView.setTag(holer);
        }else{
            holer = (ViewHolder)convertView.getTag();
        }
        PoiItem poiInfo = list.get(position);
        String name = poiInfo.getTitle();
        holer.nameTv.setText(name);

        return convertView;
    }
    class ViewHolder {
        TextView nameTv;
    }
}
