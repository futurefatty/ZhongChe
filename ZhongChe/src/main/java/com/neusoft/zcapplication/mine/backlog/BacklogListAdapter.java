package com.neusoft.zcapplication.mine.backlog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.entity.GetBacklogData;

import java.util.List;

/**
 * Created by Administrator on 2017/8/22.
 * 我的待办适配器
 */

public class BacklogListAdapter extends BaseAdapter{
    private Context context;
    private List<GetBacklogData> list;
    private int type; //

    public BacklogListAdapter(Context context, List<GetBacklogData> list, int type){
        this.context = context;
        this.list = list;
        this.type = type;
    }
    public void setList(List<GetBacklogData> list){
        this.list = list;
    }
    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public GetBacklogData getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        GetBacklogData getBacklogData = getItem(position);
        if(null == convertView){
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_backlog_list,null);

            holder.state =(TextView) convertView.findViewById(R.id.tv_state);
            holder.type =(TextView) convertView.findViewById(R.id.tv_type);
            holder.date =(TextView) convertView.findViewById(R.id.tv_date);
            holder.nameTv =(TextView) convertView.findViewById(R.id.item_backlog_apply_name);
            holder.billNoTv =(TextView) convertView.findViewById(R.id.item_backlog_bill_no);

            convertView.setTag(holder);
        }else{
            holder =(ViewHolder) convertView.getTag();
        }
        String applicateNameStr = getBacklogData.getApplicateName();//申请人
        String orderApplyId = getBacklogData.getOrderApplyId();//预定申请单编号
        holder.state.setText(getBacklogData.getState());
        holder.type.setText(getBacklogData.getType());
        String dateTime = getBacklogData.getDateTime();
        holder.date.setText(dateTime);

        holder.nameTv.setText(applicateNameStr);
//        String billNo = "SL20171109000" + (21 + position);
        holder.billNoTv.setText(orderApplyId);

        return convertView;
    }

    class ViewHolder{
        TextView state, type, date,nameTv,billNoTv;
    }
}
