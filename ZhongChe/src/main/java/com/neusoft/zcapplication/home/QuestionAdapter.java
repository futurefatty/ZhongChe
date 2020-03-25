package com.neusoft.zcapplication.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.tools.DisplayUtil;
import com.neusoft.zcapplication.tools.LogUtil;

import java.util.List;
import java.util.Map;

/**
 * 常见问题适配器
 */

public class QuestionAdapter extends BaseAdapter {
    private List<Map<String,Object>> list;
    private Context context;

    public QuestionAdapter(List<Map<String, Object>> list, Context context) {
        this.list = list;
        this.context = context;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_question,null);
            holder.titleTv = (TextView)convertView.findViewById(R.id.item_question_question);
            holder.textTv = (TextView)convertView.findViewById(R.id.item_question_answer);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        DisplayUtil.measureWidthAndHeight(holder.titleTv);
        int titleMeasuredHeight = holder.titleTv.getMeasuredHeight();
        DisplayUtil.setMargins(holder.textTv,0,-(titleMeasuredHeight/2),0,0);
        DisplayUtil.setPaddings(holder.textTv,DisplayUtil.dpTopx(context,10),titleMeasuredHeight/2+DisplayUtil.dpTopx(context,10),DisplayUtil.dpTopx(context,10),DisplayUtil.dpTopx(context,10));
        Map<String,Object> map = list.get(position);
        String question = null == map.get("question") ? "" : map.get("question").toString();
        String answer = null == map.get("answer") ? "" : map.get("answer").toString();
        String questionStr = (position + 1) + "." + question;
        holder.titleTv.setText(questionStr);
        holder.textTv.setText(answer.replaceAll("\\n","\n"));

        return convertView;
    }

    class ViewHolder{
        TextView titleTv,textTv;
    }
}
