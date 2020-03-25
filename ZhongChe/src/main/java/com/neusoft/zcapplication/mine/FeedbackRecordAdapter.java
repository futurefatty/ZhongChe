package com.neusoft.zcapplication.mine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.entity.QueryFeeback;

import java.util.List;

/**
 * Created by Administrator on 2017/8/22.
 * 意见反馈记录适配器
 */

public class FeedbackRecordAdapter extends BaseAdapter{

    private Context context;
    private List<QueryFeeback> list;

    public FeedbackRecordAdapter(Context context, List<QueryFeeback> list){
        this.context = context;
        this.list = list;
    }
    public void setList(List<QueryFeeback> list){
        this.list = list;
    }
    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public QueryFeeback getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        QueryFeeback queryFeeback = getItem(position);
        if(null == convertView){
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_feedback_record,null);

            holder.tv_type =(TextView) convertView.findViewById(R.id.tv_type);
            holder.tv_subject =(TextView) convertView.findViewById(R.id.tv_subject);
            holder.tv_content =(TextView) convertView.findViewById(R.id.tv_content);

            convertView.setTag(holder);
        }else{
            holder =(ViewHolder) convertView.getTag();
        }
        holder.tv_type.setText(queryFeeback.getSuggestionType());
        holder.tv_subject.setText(queryFeeback.getSupperType());
        holder.tv_content.setText(queryFeeback.getDetail());
        return convertView;
    }

    class ViewHolder{
        TextView tv_type,tv_subject,tv_content;
    }
}
