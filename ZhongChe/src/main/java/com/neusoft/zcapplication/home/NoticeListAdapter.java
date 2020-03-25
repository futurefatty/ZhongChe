package com.neusoft.zcapplication.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.entity.GetAllNotice;
import com.neusoft.zcapplication.entity.GetNotice;
import com.neusoft.zcapplication.tools.DateUtils;

import org.w3c.dom.Text;

import java.util.Date;
import java.util.List;

public class NoticeListAdapter extends BaseAdapter {
    private Context context;
    private List<GetNotice> list;

    public NoticeListAdapter(Context context, List<GetNotice> list){
        this.context = context;
        this.list = list;
    }
    public void setList(List<GetNotice> list){
        this.list = list;
    }
    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public GetNotice getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        GetNotice noticeList = getItem(position);
        if(null == convertView){
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_notice,null);

            holder.tv_title =(TextView) convertView.findViewById(R.id.tv_notice_title);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_notice_time);
            convertView.setTag(holder);
        }else{
            holder =(ViewHolder) convertView.getTag();
        }

        holder.tv_title.setText(noticeList.getNOTICE_TITLE());
        if (noticeList.getPUBLISH_TIME().length() > 6){
            holder.tv_time.setText(noticeList.getPUBLISH_TIME().substring(0,noticeList.getPUBLISH_TIME().length() -6));
        }else{
            holder.tv_time.setText(noticeList.getPUBLISH_TIME());
        }


        return convertView;
    }

    class ViewHolder{
        TextView tv_title;
        TextView tv_time;
    }

}
