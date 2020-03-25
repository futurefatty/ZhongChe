package com.neusoft.zcapplication.Calendar;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.neusoft.zcapplication.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * 日历，日期适配器
 */

public class CalendarGridViewAdapter extends BaseAdapter {
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");//日期格式化

    private SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");//日期格式化
    private List<String> gvList;//存放天
    private String goDay, backDay, nowDay;
    private Context context;
    private boolean hideDateText;
    private int dateTextType;//选择中的日期下面显示的文字类型 1 机票类型文字，2酒店类型文字

    public CalendarGridViewAdapter(List<String> gvList, String goDay, String today,
                                   Context context, int dateTextType) {
        super();
        this.gvList = gvList;
        this.goDay = goDay;
        this.context = context;
        this.nowDay = today;
        this.dateTextType = dateTextType;
//        Log.i("------>","存放天数的list:"+gvList.size());
    }

    public CalendarGridViewAdapter(List<String> gvList, String goDay, String today,
                                   Context context, int dateTextType, boolean hideDateText) {
        super();
        this.gvList = gvList;
        this.goDay = goDay;
        this.context = context;
        this.nowDay = today;
        this.dateTextType = dateTextType;
        this.hideDateText = hideDateText;
//        Log.i("------>","存放天数的list:"+gvList.size());
    }

    public void setBackDay(String backDay) {
        this.backDay = backDay;
    }

    @Override
    public int getCount() {
        return gvList.size();
    }

    @Override
    public String getItem(int position) {
        return gvList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GridViewHolder holder;
        if (convertView == null) {
            holder = new GridViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_calendar_grid_view_, null);
            holder.tvPrice = (TextView) convertView.findViewById(R.id.tv_calendar);
            holder.tvDay = (TextView) convertView.findViewById(R.id.tv_calendar_day);
            convertView.setTag(holder);
        } else {
            holder = (GridViewHolder) convertView.getTag();
        }
        String[] date = getItem(position).split(",");
        //
//        Log.i("--->","日期数组date = " + getItem(position));// yyyy-mm,dd
        holder.tvDay.setText(date[1]);
        holder.tvDay.setTextColor(Color.parseColor("#000000"));
        //设置周末两天的文字颜色
        if ((position + 1) % 7 == 0 || (position) % 7 == 0) {
            holder.tvDay.setTextColor(Color.parseColor("#FF6600"));
        }
        if (!date[1].equals(" ")) {
            String day = date[1];
            if (Integer.parseInt(date[1]) < 10) {
                day = "0" + date[1];
            }
            //今天
            if ((date[0] + "-" + day).equals(nowDay)) {
                holder.tvDay.setTextColor(Color.parseColor("#FF6600"));
                holder.tvDay.setTextSize(15);
                String todayStr = context.getResources().getString(R.string.text_today);
                holder.tvDay.setText(todayStr);
            }
            //用户选择的日期
            if (!"".equals(goDay) && (date[0] + "-" + day).equals(goDay)) {
                convertView.setBackgroundColor(Color.parseColor("#33B5E5"));
                holder.tvDay.setTextColor(Color.WHITE);
                holder.tvDay.setText(date[1]);
                if (dateTextType == 1) {
                    if (!hideDateText) {
                        holder.tvPrice.setText("出发");
                    }
                } else if (dateTextType == 2) {
                    holder.tvPrice.setText("入住");
                }
                holder.tvPrice.setTextColor(Color.WHITE);

//                holder.tv.setText("预约");
//                viewIn=convertView;
//                positionIn=date[1];
            }
            if (null != backDay && (date[0] + "-" + day).equals(backDay)) {
                convertView.setBackgroundColor(Color.parseColor("#33B5E5"));
                holder.tvDay.setTextColor(Color.WHITE);
                holder.tvDay.setText(date[1]);
//                holder.tvPrice.setText("离店");
                if (dateTextType == 1) {
                    if (!hideDateText){
                        holder.tvPrice.setText("到达");
                    }

                } else if (dateTextType == 2) {
                    holder.tvPrice.setText("离店");
                }
                holder.tvPrice.setTextColor(Color.WHITE);
            }
            try {
                //若日历日期<当前日期，则不能选择
                if (dateFormat2.parse(date[0] + "-" + day).getTime() < dateFormat2.parse(nowDay).getTime()) {
                    holder.tvDay.setTextColor(Color.parseColor("#c7ced4"));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return convertView;
    }

    class GridViewHolder {
        TextView tvDay, tvPrice;
    }
}
