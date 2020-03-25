package com.neusoft.zcapplication.Calendar;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;
import java.util.Map;

/**
 * 日历适配器
 */
public class CalendarAdapter extends BaseAdapter {
    private Context cotext;
    private List<Map<String,String>> list;

    public CalendarAdapter(Context cotext, List<Map<String, String>> list) {
        this.cotext = cotext;
        this.list = list;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
