package com.neusoft.zcapplication.visa;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.widget.CircleImageView;

import java.util.List;
import java.util.Map;

public class VisaGridAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String,String>> list;

    public VisaGridAdapter(Context context, List<Map<String, String>> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Map<String,String> getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_visa_grid,null);
        TextView nameTv = (TextView)convertView.findViewById(R.id.item_country_name);
        CircleImageView imageView = (CircleImageView)convertView.findViewById(R.id.item_country_icon);

        Map<String,String> map = list.get(position);

        String name = map.get("name").toString();
        String ids = map.get("ids").toString();

        int resId = context.getResources().getIdentifier(ids,"mipmap",context.getPackageName());

        nameTv.setText(name);
        imageView.setImageResource(resId);

        return convertView;
    }
}
